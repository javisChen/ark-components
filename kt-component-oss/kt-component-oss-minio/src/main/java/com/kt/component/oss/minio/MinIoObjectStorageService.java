package com.kt.component.oss.minio;

import cn.hutool.core.io.IoUtil;
import com.kt.component.oss.AbstractObjectStorageService;
import com.kt.component.oss.exception.OssException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Slf4j
public class MinIoObjectStorageService extends AbstractObjectStorageService {

    protected static final String BACKSLASH = "/";
    protected MinioClient minioClient = null;
    private final MinIoConfiguration minIoConfiguration;

    public MinIoObjectStorageService(MinIoConfiguration minIoConfiguration) {
        this.minIoConfiguration = minIoConfiguration;
        init();
    }

    public void init() {
        this.minioClient = MinioClient.builder()
                .endpoint(minIoConfiguration.getEndPoint())
                .credentials(minIoConfiguration.getAccessKey(), minIoConfiguration.getSecretKey())
                .build();
    }

    @Override
    public String put(String bucketName, String objectName, InputStream inputstream) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputstream, inputstream.available(), -1)
                    .build();
            GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .method(Method.GET)
                    .build();
            String endpoint = minIoConfiguration.getEndPoint();
            return endpoint + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            throw new OssException("OSS上传失败", e);
        }
    }

    @Override
    public InputStream get(String bucketName, String objectName) {
        try {
            GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .method(Method.GET)
                    .object(objectName).build();
            String objectUrl = minioClient.getPresignedObjectUrl(build);
            GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
            return minioClient.getObject(args);
        } catch (Exception e) {
            throw new OssException("OSS下载失败", e);
        }
    }
}
