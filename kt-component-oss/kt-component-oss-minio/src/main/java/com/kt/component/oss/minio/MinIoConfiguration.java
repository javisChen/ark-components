package com.kt.component.oss.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kt.component.oss.minio")
@Data
@Configuration
public class MinIoConfiguration {

    private String endPoint;
    private String accessKey;
    private String secretKey;
}
