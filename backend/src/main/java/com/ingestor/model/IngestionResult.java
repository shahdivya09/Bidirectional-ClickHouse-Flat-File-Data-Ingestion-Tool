package com.ingestor.model;

public class IngestionResult {
    private boolean success;
    private String message;
    private long recordCount;

    public IngestionResult() {}

    public IngestionResult(boolean success, String message, long recordCount) {
        this.success = success;
        this.message = message;
        this.recordCount = recordCount;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getRecordCount() { return recordCount; }
    public void setRecordCount(long recordCount) { this.recordCount = recordCount; }
}
