package com.kt.component.oss.minio;

import cn.hutool.core.io.IoUtil;
import com.kt.component.oss.IObjectStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MinIoOssObjectStorageServiceTest {

    private IObjectStorageService iObjectStorageService;

    @Before
    public void setUp() {
        MinIoOssProperties minIoOssProperties = new MinIoOssProperties();
        minIoOssProperties.setEndPoint("http://127.0.0.1:9000");
        minIoOssProperties.setAccessKey("admin");
        minIoOssProperties.setSecretKey("admin123456");
        iObjectStorageService = new MinIoOssObjectStorageService(minIoOssProperties);
    }

    @Test
    public void testPut() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/test.png");
        String ossUrl = iObjectStorageService.put("code", "test.png", resourceAsStream);
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