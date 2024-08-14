package com.ark.component.oss.minio;

import cn.hutool.core.io.IoUtil;
import com.ark.component.oss.IObjectStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MinIoOssObjectStorageServiceTest {

    private IObjectStorageService iObjectStorageService;

    @BeforeEach
    public void setUp() {
        MinIOOssProperties minIoOssProperties = new MinIOOssProperties();
        minIoOssProperties.setEndPoint("http://127.0.0.1:9000");
        minIoOssProperties.setAccessKey("admin");
        minIoOssProperties.setSecretKey("admin123456");
        iObjectStorageService = new MinIoOssObjectStorageService(minIoOssProperties);
    }

    @Test
    public void testPut() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/test.png");
        String ossUrl = iObjectStorageService.put("code", "test123.png", resourceAsStream);
        System.out.println(ossUrl);
        Assertions.assertNotNull(ossUrl);
    }

    @Test
    public void testGet() {
        InputStream ossUrl = iObjectStorageService.get("code", "test.txt");
        String read = IoUtil.read(ossUrl, StandardCharsets.UTF_8);
        System.out.println(read);
        Assertions.assertNotNull(read);
    }
}