package com.klearn.exception;

public class AuthenticationException extends KLearnException {
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_ERROR");
    }
}
