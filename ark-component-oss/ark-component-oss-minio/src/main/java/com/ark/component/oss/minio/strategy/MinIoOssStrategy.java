package com.ark.component.oss.minio.strategy;

import cn.hutool.core.io.FileUtil;
import com.ark.component.oss.core.strategy.AbstractOssStrategy;
import com.ark.component.oss.api.OssType;
import com.ark.component.oss.api.exception.OssException;
import com.ark.component.oss.minio.MinIoOssProperties;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

@Slf4j
public class MinIoOssStrategy extends AbstractOssStrategy {

    private MinioClient minioClient = null;

    private final MinIoOssProperties minIoOssProperties;

    public MinIoOssStrategy(MinIoOssProperties minIoOssProperties) {
        this.minIoOssProperties = minIoOssProperties;
        init();
    }

    public void init() {
        this.minioClient = MinioClient.builder()
                .endpoint(minIoOssProperties.getEndPoint())
                .credentials(minIoOssProperties.getAccessKey(), minIoOssProperties.getSecretKey())
                .build();
    }

    @Override
    public String put(String bucketName, String objectName, InputStream inputstream) {
        return put(false, bucketName, objectName, inputstream);
    }

    @Override
    public String put(boolean createBucketIfNotExist, String bucketName, String objectName, InputStream inputstream) {
        try {
            if (createBucketIfNotExist) {
                boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!bucketExists) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
            }
            PutObjectArgs.Builder argsBuilder = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputstream, inputstream.available(), -1);
            String contentType = FileUtil.getMimeType(objectName);
            if (StringUtils.isNotEmpty(contentType)) {
                argsBuilder.contentType(contentType);
            }
            minioClient.putObject(argsBuilder.build());
            return minIoOssProperties.getEndPoint() + File.separator + bucketName + File.separator + objectName;
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public InputStream get(String bucketName, String objectName) {
        try {
            GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
            return minioClient.getObject(args);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public OssType ossType() {
        return OssType.MINIO;
    }

//    @Override
//    public InputStream get(String bucketName, String objectName) {
//        try {
//            GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
//            return minioClient.getObject(args);
//        } catch (Exception e) {
//            throw new OssException(e);
//        }
//    }
}
