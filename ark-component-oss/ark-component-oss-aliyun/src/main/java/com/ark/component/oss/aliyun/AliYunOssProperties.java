package com.ark.component.oss.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ark.component.oss.aliyun")
@Data
public class AliYunOssProperties {

    private String endPoint;
    private String accessKey;
    private String secretKey;
    private Boolean enabled = true;


}
