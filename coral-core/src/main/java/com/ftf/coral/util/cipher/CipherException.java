package com.ftf.coral.util.cipher;

public class CipherException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CipherException() {
        super();
    }

    public CipherException(String message) {
        super(message);
    }

    public CipherException(Throwable cause) {
        super(cause);
    }
}
