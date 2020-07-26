package com.ftf.coral.core.exception;

public class AuthException extends BusinessException {

    private static final long serialVersionUID = 1L;

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
