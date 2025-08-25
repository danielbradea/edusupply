package com.bid90.edusupply.exception;

import org.springframework.http.HttpStatus;

public class GroupException extends RuntimeException {

    private final HttpStatus status;

    public GroupException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public GroupException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}