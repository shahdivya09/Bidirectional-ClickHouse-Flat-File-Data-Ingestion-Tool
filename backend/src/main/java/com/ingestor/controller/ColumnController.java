package com.ingestor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ColumnController {

    @GetMapping("/columns")
    public ResponseEntity<List<String>> getColumns(@RequestParam String source) {
        if (source.equalsIgnoreCase("clickhouse")) {
            // Simulate ClickHouse table column fetch
            return ResponseEntity.ok(List.of("id", "name", "price", "timestamp"));
        } else if (source.equalsIgnoreCase("flatfile")) {
            // Return dummy CSV headers
            return ResponseEntity.ok(List.of("col1", "col2", "col3"));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
