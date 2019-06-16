package com.masiblue.backend.exception;

public class InvalidCsvException extends Exception {
    public InvalidCsvException(String message) {
        super(message);
    }

    public InvalidCsvException() {
    }
}
