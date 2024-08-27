package com.ark.component.oss.api;

public enum OssType {

    MINIO("MINIO"),

    ALI_CLOUD("ALI_CLOUD");

    private final String value;

    OssType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
