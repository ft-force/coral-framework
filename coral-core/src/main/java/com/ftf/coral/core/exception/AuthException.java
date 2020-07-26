package com.ftf.coral.core.exception;

@SuppressWarnings("serial")
public class AuthException extends RuntimeException {

    public AuthException() {
        super();
    }

    public AuthException(final String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
