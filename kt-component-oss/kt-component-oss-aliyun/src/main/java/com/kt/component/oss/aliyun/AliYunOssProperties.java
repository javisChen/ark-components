package com.kt.component.oss.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kt.component.oss.aliyun")
@Data
public class AliYunOssProperties {

    private String endPoint;
    private String accessKey;
    private String secretKey;
    private Boolean enabled = true;


}
