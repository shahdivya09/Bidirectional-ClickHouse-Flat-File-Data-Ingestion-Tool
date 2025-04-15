package com.ingestor.service;

import com.ingestor.model.*;
import com.ingestor.utils.CSVHelper;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class IngestionServiceImpl implements IngestionService {

    private Connection getConnection(ClickHouseConfig config) throws SQLException {
        String url = String.format("jdbc:clickhouse://%s:%d/%s", config.getHost(), config.getPort(), config.getDatabase());

        return DriverManager.getConnection(
                url,
                config.getUser(),
                config.getJwtToken() // passed as password
        );
    }

    @Override
    public boolean testClickHouseConnection(ClickHouseConfig config) {
        try (Connection conn = getConnection(config)) {
            return conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<String> getClickHouseTables(ClickHouseConfig config) {
        List<String> tables = new ArrayList<>();
        String query = "SHOW TABLES";

        try (Connection conn = getConnection(config);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tables.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }

    @Override
    public List<String> getClickHouseColumns(ClickHouseConfig config, String tableName) {
        List<String> columns = new ArrayList<>();
        String query = "DESCRIBE TABLE " + tableName;

        try (Connection conn = getConnection(config);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                columns.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columns;
    }

    @Override
    public IngestionResult ingestClickHouseToCSV(IngestRequest request) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(String.join(",", request.getColumns()))
             .append(" FROM ").append(request.getTableName());

        long rowCount = 0;

        try (Connection conn = getConnection(request.getConfig());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            rowCount = CSVHelper.writeResultSetToCSV(rs, request.getFlatFilePath());

        } catch (Exception e) {
            return new IngestionResult(false, "Error: " + e.getMessage(), 0);
        }

        return new IngestionResult(true, "Export complete", rowCount);
    }

    @Override
    public IngestionResult ingestCSVToClickHouse(IngestRequest request) {
        long rowCount;

        try (Connection conn = getConnection(request.getConfig())) {
            rowCount = CSVHelper.insertCSVToClickHouse(conn, request);
        } catch (Exception e) {
            return new IngestionResult(false, "Error: " + e.getMessage(), 0);
        }

        return new IngestionResult(true, "Import complete", rowCount);
    }
}
