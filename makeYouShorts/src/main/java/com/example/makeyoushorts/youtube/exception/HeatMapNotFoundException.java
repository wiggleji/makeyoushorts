package com.example.makeyoushorts.youtube.exception;

public class HeatMapNotFoundException extends RuntimeException {
    public HeatMapNotFoundException(String message) {
        super(message);
    }

    public HeatMapNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
