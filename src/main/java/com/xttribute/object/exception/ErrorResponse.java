package com.xttribute.object.exception;

public class ErrorResponse {
    private int statusCode;
    private String message;

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    // Getter methods
    public String getMessage() {
    	return this.message;
    }
    
    public int getStatusCode() {
    	return this.statusCode;
    }
}