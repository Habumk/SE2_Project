package com.klearn.exception;

/**
 * Base exception class for K-Learn application.
 */
public class KLearnException extends RuntimeException {
    private final String errorCode;

    public KLearnException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public KLearnException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
