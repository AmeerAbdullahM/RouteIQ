package com.routeiq.common.response;

import java.time.ZonedDateTime;

public class ErrorResponse {
    private String errorCode;
    private String message;
    private String correlationId;
    private ZonedDateTime timestamp;

    public ErrorResponse(String errorCode, String message, String correlationId) {
        this.errorCode = errorCode;
        this.message = message;
        this.correlationId = correlationId;
        this.timestamp = ZonedDateTime.now();
    }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public ZonedDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(ZonedDateTime timestamp) { this.timestamp = timestamp; }
}
