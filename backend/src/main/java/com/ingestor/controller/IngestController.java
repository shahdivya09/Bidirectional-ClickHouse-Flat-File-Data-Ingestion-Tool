package com.ingestor.controller;

import com.ingestor.model.*;
import com.ingestor.service.IngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin // Enable CORS for frontend
public class IngestController {

    @Autowired
    private IngestionService ingestionService;

    @PostMapping("/test-clickhouse-connection")
    public boolean testClickHouseConnection(@RequestBody ClickHouseConfig config) {
        return ingestionService.testClickHouseConnection(config);
    }

    @PostMapping("/list-clickhouse-tables")
    public List<String> listTables(@RequestBody ClickHouseConfig config) {
        return ingestionService.getClickHouseTables(config);
    }

    @PostMapping("/list-clickhouse-columns")
    public List<String> listColumns(@RequestBody TableRequest request) {
        return ingestionService.getClickHouseColumns(request.getConfig(), request.getTableName());
    }

    @PostMapping("/ingest-clickhouse-to-csv")
    public IngestionResult ingestToCSV(@RequestBody IngestRequest request) {
        return ingestionService.ingestClickHouseToCSV(request);
    }

    @PostMapping("/ingest-csv-to-clickhouse")
    public IngestionResult ingestCSVToClickHouse(@RequestBody IngestRequest request) {
        return ingestionService.ingestCSVToClickHouse(request);
    }
}
