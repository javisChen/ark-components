package com.kt.component.oss.minio;

import com.kt.component.oss.AbstractObjectStorageService;
import com.kt.component.oss.exception.OssException;
import com.kt.component.oss.util.FileContentTypeUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

@Slf4j
public class MinIoOssObjectStorageService extends AbstractObjectStorageService {

    private MinioClient minioClient = null;
    private final MinIoOssProperties minIoOssProperties;

    public MinIoOssObjectStorageService(MinIoOssProperties minIoOssProperties) {
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
        try {
            PutObjectArgs.Builder argsBuilder = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputstream, inputstream.available(), -1);
            String contentType = FileContentTypeUtil.getType(objectName);
            if (StringUtils.isNotEmpty(contentType)) {
                argsBuilder.contentType(contentType);
            }
            minioClient.putObject(argsBuilder.build());
            return minIoOssProperties.getEndPoint() + File.separator + bucketName+ File.separator + objectName;
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
}
