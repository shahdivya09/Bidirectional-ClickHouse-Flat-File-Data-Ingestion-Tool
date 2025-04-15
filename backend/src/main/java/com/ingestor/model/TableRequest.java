package com.ingestor.model;

public class TableRequest {
    private ClickHouseConfig config;
    private String tableName;

    public ClickHouseConfig getConfig() { return config; }
    public void setConfig(ClickHouseConfig config) { this.config = config; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
}
