package com.ark.component.oss;

public enum OssType {

    MINIO("MINIO"),
    ALIYUN("ALIYUN");

    private final String value;

    OssType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
