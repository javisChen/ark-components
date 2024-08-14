package com.ark.component.oss.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ark.component.oss.minio")
@Data
public class MinIOOssProperties {

    private String endPoint;

    private String accessKey;

    private String secretKey;

    private Boolean enabled = true;

}
