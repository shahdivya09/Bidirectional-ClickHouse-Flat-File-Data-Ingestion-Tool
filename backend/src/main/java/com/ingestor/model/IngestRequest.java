package com.ingestor.model;

import java.util.List;

public class IngestRequest {
    private ClickHouseConfig config;
    private String tableName;
    private List<String> columns;
    private String flatFilePath; // Path to CSV for ingest

    public ClickHouseConfig getConfig() { return config; }
    public void setConfig(ClickHouseConfig config) { this.config = config; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }

    public String getFlatFilePath() { return flatFilePath; }
    public void setFlatFilePath(String flatFilePath) { this.flatFilePath = flatFilePath; }
}
