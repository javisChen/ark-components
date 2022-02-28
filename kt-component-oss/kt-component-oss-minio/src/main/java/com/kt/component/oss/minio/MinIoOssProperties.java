package com.kt.component.oss.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kt.component.oss.minio")
@Data
public class MinIoOssProperties {

    private String endPoint;
    private String accessKey;
    private String secretKey;
}
