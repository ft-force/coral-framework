package com.ftf.coral.web.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code = 0;

    private String message = "";

    private T data;

    public ResponseDTO() {}

    public ResponseDTO<T> success() {
        this.code = 0;
        return this;
    }

    public ResponseDTO<T> success(T data) {
        this.data = data;
        return this;
    }

    public ResponseDTO<T> failure() {
        this.code = -1;
        return this;
    }

    public ResponseDTO<T> failure(int code) {
        this.code = code;
        return this;
    }

    public ResponseDTO<T> failure(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    /**
     * 0 成功，非0 失败
     */
    @JsonIgnore
    public boolean isSuccess() {
        return code == 0;
    }

    /************************************************************************************/

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
