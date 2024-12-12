package com.example.migrate.sync.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author jwh
 * @create 2018/5/16 10:49
 **/
public class RowIdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tableName;

    private String owner;

    private int segments;

    private String partition;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getSegments() {
        return segments;
    }

    public void setSegments(int segments) {
        this.segments = segments;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }
}
