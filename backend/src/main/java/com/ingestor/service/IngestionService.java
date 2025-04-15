package com.ingestor.service;

import com.ingestor.model.*;

import java.util.List;

public interface IngestionService {
    boolean testClickHouseConnection(ClickHouseConfig config);
    List<String> getClickHouseTables(ClickHouseConfig config);
    List<String> getClickHouseColumns(ClickHouseConfig config, String tableName);
    IngestionResult ingestClickHouseToCSV(IngestRequest request);
    IngestionResult ingestCSVToClickHouse(IngestRequest request);
}
