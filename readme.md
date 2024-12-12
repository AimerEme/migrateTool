A tool to help migrate data from origin DB(mostly Oracle,Mysql can also support) to StarRocks or DorisDB

working process(like ETL):
  1. exec sql select from originDB(Extract)
  2. divide in batchs by rownum partition and id range(Trans)
  3. Stream load to target DB(Load)

This tool can migrate data in a fast speed

How to use
  1. package into jar
  2. exec 'nohup   java -jar -Xmn4096m -Xms4096m -Xmx4096m xxx.jar & '
  3. exec POST command and see the log print. the command like dataX config

     curl -L -g -X POST 'http://localhost:9988/sync' -H 'Content-Type: application/json' -d '{
    "tableName" : "BACKUP_T_ORDER_TEMP_ITEM",
    "columns" : ["ITEM_ID","ORDER_ID","PRODUCT_ID","PRODUCT_CODE","PRODUCT_NAME","COUNT","PRICE","CHARGE_ID","FEES","FEE_TYPE","CP_PARTNER_ID","SUBMIT_TIME","CREATE_TIME"],
    "pageSize" : 100000,
    "separator" : "^A",
    "kafkaTopic" : "TOPIC_BACKUP_ORDER_TEMP_ITEM",
    "targetTableName" : "BACKUP_T_ORDER_TEMP_ITEM",
    "batchNum" : 2325
}'


tableName: the table in origin DB,data you want migrate out
columns:the columns of origin and  target table
pageSize: the size of per path,can influence the speed
separator: code may form datas in CSV,make sure your data dont contains the separator
kafkaTopic: if need Routine Load to Load data to StarRocks,Stream Load is not need it
targetTableName: the table in target DB,data you want migrate in
batchNum : the num of batch(total divide pagesize)