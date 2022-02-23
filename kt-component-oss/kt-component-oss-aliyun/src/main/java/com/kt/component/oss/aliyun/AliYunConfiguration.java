package com.kt.component.oss.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kt.component.oss.minio")
@Data
@Configuration
public class AliYunConfiguration {

    private String endPoint;
    private String accessKey;
    private String secretKey;
}
