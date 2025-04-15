package com.ingestor.utils;

import com.ingestor.model.IngestRequest;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.sql.*;
import java.util.List;

public class CSVHelper {

    public static long writeResultSetToCSV(ResultSet rs, String filePath) throws Exception {
        long rowCount = 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write headers
            String[] header = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                header[i] = metaData.getColumnName(i + 1);
            }
            writer.writeNext(header);

            // Write data rows
            while (rs.next()) {
                String[] data = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    data[i] = rs.getString(i + 1);
                }
                writer.writeNext(data);
                rowCount++;
            }
        }

        return rowCount;
    }

    public static long insertCSVToClickHouse(Connection conn, IngestRequest request) throws Exception {
        long rowCount = 0;
        List<String> columns = request.getColumns();
        String table = request.getTableName();
        String filePath = request.getFlatFilePath();

        StringBuilder query = new StringBuilder("INSERT INTO ")
                .append(table)
                .append(" (")
                .append(String.join(",", columns))
                .append(") VALUES (");

        query.append("?,".repeat(columns.size()));
        query.setLength(query.length() - 1); // remove last comma
        query.append(")");

        try (CSVReader reader = new CSVReader(new FileReader(filePath));
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            String[] headers = reader.readNext(); // Skip headers
            String[] row;

            while ((row = reader.readNext()) != null) {
                for (int i = 0; i < columns.size(); i++) {
                    stmt.setString(i + 1, row[i]);
                }
                stmt.addBatch();
                rowCount++;
            }

            stmt.executeBatch();
        }

        return rowCount;
    }
}
