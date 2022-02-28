package com.kt.component.oss.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.kt.component.oss.AbstractObjectStorageService;

import java.io.InputStream;

public class AliYunObjectStorageService extends AbstractObjectStorageService {

    private OSS ossClient;
    private AliYunOssProperties aliYunOssProperties;

    public AliYunObjectStorageService(AliYunOssProperties aliYunOssProperties) {
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
}
