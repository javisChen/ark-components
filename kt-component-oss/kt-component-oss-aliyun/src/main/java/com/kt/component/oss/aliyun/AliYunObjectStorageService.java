package com.kt.component.oss.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.kt.component.oss.AbstractObjectStorageService;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

public class AliYunObjectStorageService extends AbstractObjectStorageService {

    private OSS ossClient;
    private AliYunConfiguration aliYunConfiguration;

    public AliYunObjectStorageService(AliYunConfiguration aliYunConfiguration) {
        this.aliYunConfiguration = aliYunConfiguration;
        init();
    }

    public void init() {
        this.ossClient = new OSSClientBuilder()
                .build(aliYunConfiguration.getEndPoint(),
                        aliYunConfiguration.getAccessKey(),
                        aliYunConfiguration.getSecretKey());
    }

    @Override
    public String put(String bucketName, String objectName, InputStream inputstream) {
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputstream);
        System.out.println(putObjectResult);
        return "super.put(bucketName, objectName, inputstream)";
    }
}
