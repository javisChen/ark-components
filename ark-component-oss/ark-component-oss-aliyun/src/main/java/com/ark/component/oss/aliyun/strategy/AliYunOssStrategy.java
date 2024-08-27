package com.ark.component.oss.aliyun.strategy;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.ark.component.oss.aliyun.AliYunOssProperties;
import com.ark.component.oss.api.OssType;
import com.ark.component.oss.core.strategy.AbstractOssStrategy;

import java.io.InputStream;

public class AliYunOssStrategy extends AbstractOssStrategy {

    private OSS ossClient;
    private final AliYunOssProperties aliYunOssProperties;

    public AliYunOssStrategy(AliYunOssProperties aliYunOssProperties) {
        this.aliYunOssProperties = aliYunOssProperties;
        init();
    }

    public void init() {
        this.ossClient = new OSSClientBuilder()
                .build(aliYunOssProperties.getEndPoint(),
                        aliYunOssProperties.getAccessKey(),
                        aliYunOssProperties.getSecretKey());
    }

    @Override
    public String put(String bucketName, String objectName, InputStream inputstream) {
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputstream);
        System.out.println(putObjectResult);
        return "super.put(bucketName, objectName, inputstream)";
    }

    @Override
    public OssType ossType() {
        return OssType.ALI_CLOUD;
    }
}
