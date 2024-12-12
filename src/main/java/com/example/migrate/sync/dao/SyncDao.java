package com.example.migrate.sync.dao;

import com.example.migrate.sync.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SyncDao {

    Map<String,Object> getOrderTempCount();

    List<Map<String,Object>> dynamicSql(SQLEntity sqlEntity);

    List<Map<String,Object>> dynamicRowid(RowIdEntity rowIdEntity);

    List<Map<String,Object>> dynamicPartition(RowIdEntity rowIdEntity);

    long getCount(@Param("tableName") String tableName,@Param("where") String where);

    void truncateTable(@Param("tableName") String tableName);
}
