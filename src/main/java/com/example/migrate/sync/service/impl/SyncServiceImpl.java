package com.example.migrate.sync.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.migrate.commons.HttpUtils;
import com.example.migrate.sync.dao.SyncDao;
import com.example.migrate.sync.domain.OrderTemp;
import com.example.migrate.sync.domain.RowIdEntity;
import com.example.migrate.sync.domain.SQLEntity;
import com.example.migrate.sync.service.ISyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SyncServiceImpl implements ISyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);

    private static String SYNC_URL = "http://localhost:9988/sync";

    private static String DELETE_URL = "http://localhost:9988/deleteSync";

    @Autowired
    SyncDao syncDao;

    @Override
    public List<Map<String,Object>> getTableInfo(SQLEntity sqlEntity) {
        return syncDao.dynamicSql(sqlEntity);
    }

    @Override
    public List<Map<String, Object>> getSegmentInfo(RowIdEntity rowIdEntity) {
        return syncDao.dynamicRowid(rowIdEntity);
    }

    @Override
    public List<Map<String, Object>> getPartitionInfo(RowIdEntity rowIdEntity) {
        return syncDao.dynamicPartition(rowIdEntity);
    }

    @Override
    public long getCount(String tableName,String where) {
        return syncDao.getCount(tableName,where);
    }

    @Override
    public void testPost(SQLEntity sqlEntity) {

        try {
            HttpUtils.postUrlStringBody(SYNC_URL, JSON.toJSONString(sqlEntity));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void truncateTable(String tableName) {
        syncDao.truncateTable(tableName);
    }
}
