package com.ftf.coral.masking.exception;

@SuppressWarnings("serial")
public class DataMaskingException extends RuntimeException {

    public DataMaskingException(String msg) {
        super(msg);
    }

    public DataMaskingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
