package com.kt.component.oss.util;

public enum FileContentType {

    JPEG("FFD8FF", "image/jpeg"),
    PNG("89504E47", "image/png"),
    GIF("47494638", "image/gif"),
    TIFF("49492A00", "image/tiff"),
    BMP("424D", "image/bmp"),
    DWG("41433130", "application/x-autocad"),
    PSD("38425053", "application/x-psd"),
    RTF("7B5C727466", "application/rtf"),
    XML("3C3F786D6C", "text/xml"),
    HTML("68746D6C3E", "text/html"),
    EML("44656C69766572792D646174653A", "message/rfc822"),
    DBX("CFAD12FEC5FD746F", "application/x-dbx"),
    PST("2142444E", "application/x-pst"),
    XLS_DOC("D0CF11E0", "application/msword"),
    MDB("5374616E64617264204A", "application/msaccess"),
    WPD("FF575043", "application/x-wpd"),
    EPS("252150532D41646F6265", "application/postscript"),
    PDF("255044462D312E", "application/pdf"),
    QDF("AC9EBD8F", "application/x-qdf"),
    PWL("E3828596", "application/x-pwl"),
    ZIP("504B0304", "application/zip"),
    RAR("52617221", "application/octet-stream"),
    WAV("57415645", "audio/x-wav"),
    AVI("41564920", "video/x-msvideo"),
    RAM("2E7261FD", "audio/x-pn-realaudio"),
    RM("2E524D46", "audio/x-pn-realaudio"),
    MPG("000001BA", "video/mpeg"),
    MOV("6D6F6F76", "video/quicktime"),
    ASF("3026B2758E66CF11", "video/x-ms-asf"),
    MID("4D546864", "audio/mid");

    private final String value;
    private final String contentType;

    private FileContentType(String value, String contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public String getValue() {
        return this.value;
    }

    public String getContentType() {
        return this.contentType;
    }
}
