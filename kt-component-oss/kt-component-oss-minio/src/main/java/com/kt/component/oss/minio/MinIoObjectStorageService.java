package com.kt.component.oss.minio;

import com.kt.component.oss.AbstractObjectStorageService;
import com.kt.component.oss.exception.OssException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

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
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(build);
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(args);
            String endpoint = minIoConfiguration.getEndPoint();
            boolean customDomain = endpoint.startsWith("https://" + bucketName);
//            return customDomain ? endpoint + "/" + fileURI : endpoint + "/" + bucketName + "/" + fileURI;
            return "";
        } catch (Exception e) {
            throw new OssException("OSS上传失败", e);
        }
    }
}
