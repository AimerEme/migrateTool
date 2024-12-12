package com.example.migrate.sync.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.migrate.commons.StarRocksStreamLoad;
import com.example.migrate.sync.domain.RowIdEntity;
import com.example.migrate.sync.domain.SQLEntity;
import com.example.migrate.sync.kafka.producter.KafkaProducer;
import com.example.migrate.sync.service.ISyncService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private ISyncService syncService;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final int defaultSize = 50000;

    @Value("${starrocks.host}")
    private String host;

    @Value("${starrocks.database}")
    private String database;

    @Value("${starrocks.user}")
    private String user;

    @Value("${starrocks.password}")
    private String password;

    @Value("${starrocks.port}")
    private int port;

    @Value("${oracle.user}")
    private String owner;

    @RequestMapping("/sync")
    public String doSync(@RequestBody SQLEntity sqlEntity) {
        logger.info("同步接口请求报文{}", JSON.toJSONString(sqlEntity));
        long totalcount = 0;
        try {
            if (sqlEntity == null) {
                return "无法解析报文";
            }

            if (StringUtils.isEmpty(sqlEntity.getTableName())) {
                return "需要输入表名";
            }
            String tableName = sqlEntity.getTableName();

            if (StringUtils.isEmpty(sqlEntity.getTargetTableName())) {
                return "需要输入目标表名";
            }
            String targetTableName = sqlEntity.getTargetTableName();

            if (CollectionUtils.isEmpty(sqlEntity.getColumns())) {
                return "需要输入列名";
            }

            //若入参没有指定分区，则自动查询并分配
            String partitionIn = sqlEntity.getPartition();
            if (StringUtils.isEmpty(partitionIn)) {
                RowIdEntity partitionReq = new RowIdEntity();
                partitionReq.setTableName(tableName);
                List<Map<String, Object>> partitions = syncService.getPartitionInfo(partitionReq);
                //表是否有分区
                if (CollectionUtils.isEmpty(partitions)) {
                    segmentMethod(tableName, sqlEntity, targetTableName, totalcount, null);
                } else {
                    for (Map<String, Object> partition : partitions) {
                        String partitionName = (String) partition.get("PARTITION_NAME");
                        segmentMethod(tableName, sqlEntity, targetTableName, totalcount, partitionName);
                    }
                }
            } else {
                //指定了分区，则直接查询该分区并导入
                segmentMethod(tableName, sqlEntity, targetTableName, totalcount, partitionIn);
            }


            logger.info("全部流程执行结束，共处理{} 条", totalcount);
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }


        return "success";
    }

    void segmentMethod(String tableName, SQLEntity sqlEntity, String targetTableName, long totalcount, String partition) {
        try {
            int segments = 1;
            if (sqlEntity.getSegments() != 0) {
                segments = sqlEntity.getSegments();
            }
            RowIdEntity rowIdEntity = new RowIdEntity();
            rowIdEntity.setTableName(tableName);
            rowIdEntity.setSegments(segments);
            rowIdEntity.setOwner(owner);
            rowIdEntity.setPartition(partition);
            List<Map<String, Object>> rowIds = syncService.getSegmentInfo(rowIdEntity);
            int segmentCount = 0;

            long sequenceCount = 0;

            for (Map<String, Object> rowId : rowIds) {
                String id = rowId.get("DATA").toString();
                logger.info("开始处理第{} 段数据", segmentCount);
                try {
                    String tableNameLocal = tableName;
                    if (!StringUtils.isEmpty(partition)) {
                        StringBuilder tableNameBuilder = new StringBuilder();
                        tableNameBuilder.append(tableName);
                        tableNameBuilder.append(" PARTITION (");
                        tableNameBuilder.append(partition);
                        tableNameBuilder.append(")");
                        tableNameLocal = tableNameBuilder.toString();
                    }
                    long segmentResult = executeMethod(sqlEntity, tableNameLocal, targetTableName, id, segmentCount, sequenceCount);
                    segmentCount++;
                    totalcount = totalcount + segmentResult;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("执行分段操作发生错误：{}", e.getMessage(), e);
            e.printStackTrace();
        }

    }

    long executeMethod(SQLEntity sqlEntity, String tableName, String targetTableName,
                       String rowId, int segmentCount, long sequenceCount) throws IOException {

        long localCount = 0;

        String separator = sqlEntity.getSeparator();

        long batchSize = defaultSize;
        if (sqlEntity.getPageSize() > 0) {
            batchSize = sqlEntity.getPageSize();
        }

        sqlEntity.setRowIdWhere(rowId);

        StringBuilder columnBuilder = new StringBuilder();
        for (String column : sqlEntity.getColumns()) {
            columnBuilder.append(column).append(",");
        }
        columnBuilder.deleteCharAt(columnBuilder.length() - 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StarRocksStreamLoad load = new StarRocksStreamLoad(host, database, targetTableName, user, password, port);

        long total = syncService.getCount(tableName, rowId);

        for (int i = sqlEntity.getBatchNum(); i <= (total / batchSize); i++) {
            sqlEntity.setPageStart(i);
            sqlEntity.setPageEnd(i + 1);
            List<Map<String, Object>> datas = syncService.getTableInfo(sqlEntity);


            StringBuilder lineBuilder = new StringBuilder();
            //处理数据集合
            for (Map<String, Object> data : datas) {
                try {
                    //处理每一行，若值为null，keyset不会有该行
                    int lineSize = 0;
                    for (String column : sqlEntity.getColumns()) {
                        if (data.containsKey(column)) {
                            String value;
                            String type = data.get(column).getClass().getName();
                            if ("java.sql.Timestamp".equalsIgnoreCase(type)) {
                                //java.sql.Timestamp处理逻辑
                                Date date = (Date) data.get(column);
                                value = dateFormat.format(date);
                            } else if ("java.math.BigDecimal".equalsIgnoreCase(type)) {
                                BigDecimal bigDecimal = (BigDecimal) data.get(column);
                                value = bigDecimal.toString();
                            } else {

                                value = (String) data.get(column);

                            }

                            if (!StringUtils.isEmpty(value)) {

                                if (value.contains(sqlEntity.getSeparator())) {
                                    value = value.replaceAll(separator, "");
                                }
                                value = excludemethod(value);
                                lineBuilder.append(value);
                            } else {
                                lineBuilder.append("\\N");
                            }
                        } else {
                            lineBuilder.append("\\N");
                        }
                        lineSize++;
                        //到最后一个字段，拼接换行符
                        if (lineSize >= sqlEntity.getColumns().size()) {
                            lineBuilder.append("\n");
                        } else {
                            lineBuilder.append(separator);
                        }
                    }
                }catch (Exception e){
                    logger.info("处理数据行发生错误，跳过");
                }

            }

            if (lineBuilder.length() > 0) {
                lineBuilder.deleteCharAt(lineBuilder.length() - 1);
                String result;
                if (!StringUtils.isEmpty(sqlEntity.getOrderType()) && "DELETE".equals(sqlEntity.getOrderType())){
                    result = load.deleteData(lineBuilder.toString(), separator, columnBuilder.toString());
                }else {
                    result = load.sendData(lineBuilder.toString(), separator, columnBuilder.toString());
                }

                logger.info("第{}段 第 {} 批导入结果：{}", segmentCount, i, result);
//                String result = "fail";
                JSONObject jsonObject = JSON.parseObject(result);
                String loadResult = jsonObject.getString("Status");
                if (!"Success".equals(loadResult)) {
                    logger.info("第{}段 第 {} 批导入失败！", segmentCount, i);
//                String[] values = lineBuilder.toString().split("\n", -1);
//                //关于每个数据行，构建数据
//                for (String value : values) {
//                    String[] lines = value.split(separator);
//                    Map<String, String> map = new HashMap<>();
////
//                    for (int index = 0; index < sqlEntity.getColumns().size(); index++) {
//                        String info = lines[index];
//                        if ("\\N".equals(info)) {
//                            map.put(sqlEntity.getColumns().get(index), null);
//                        } else {
//                            map.put(sqlEntity.getColumns().get(index), info);
//                        }
//                    }
//                    kafkaProducer.sendMessage(sqlEntity.getKafkaTopic(), map);
//                    localCount++;
//                }
                } else {
                    String rows = jsonObject.getString("NumberTotalRows");
                    localCount = localCount + Integer.parseInt(rows);
                }


                logger.info("第{}段 第 {} 批处理完毕", segmentCount, i);
            }
        }
        logger.info("第{}段执行结束，系统获取到总条数为： {} ", segmentCount, total);
        logger.info("第{}段执行结束 执行结束，共处理 {} 条！", segmentCount, localCount);
        return localCount;
    }

    String excludemethod(String content) {
        content = content.replace("\r\n", "");
        content = content.replace("\n", "");
        return content;
    }
}
