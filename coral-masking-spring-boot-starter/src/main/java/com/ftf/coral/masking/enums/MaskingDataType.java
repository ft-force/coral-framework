package com.ftf.coral.masking.enums;

public enum MaskingDataType {

    json("json"), object("object");

    private String code;

    private MaskingDataType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
