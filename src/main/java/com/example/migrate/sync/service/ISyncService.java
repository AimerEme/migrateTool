package com.example.migrate.sync.service;

import com.example.migrate.sync.domain.OrderTemp;
import com.example.migrate.sync.domain.RowIdEntity;
import com.example.migrate.sync.domain.SQLEntity;

import java.util.List;
import java.util.Map;

public interface ISyncService {


    List<Map<String,Object>> getTableInfo(SQLEntity sqlEntity);

    List<Map<String,Object>> getSegmentInfo(RowIdEntity rowIdEntity);

    List<Map<String,Object>> getPartitionInfo(RowIdEntity rowIdEntity);

    long getCount(String tableName,String where);

    void testPost(SQLEntity sqlEntity);

    void truncateTable(String tableName);

}
