package com.xttribute.object.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @GetMapping("/error-example")
    public ResponseEntity<ErrorResponse> getError() {
        ErrorResponse error = new ErrorResponse(400, "Invalid request parameters");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    static class ErrorResponse {
        private int errorCode;
        private String message;

        public ErrorResponse(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        // Getters and Setters
    }
}