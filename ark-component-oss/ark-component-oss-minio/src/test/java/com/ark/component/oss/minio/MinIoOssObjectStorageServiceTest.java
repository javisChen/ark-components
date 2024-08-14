package com.ark.component.oss.minio;

import cn.hutool.core.io.IoUtil;
import com.ark.component.oss.OssStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MinIoOssObjectStorageServiceTest {

    private OssStrategy ossStrategy;

    @BeforeEach
    public void setUp() {
        MinIoOssProperties minIoOssProperties = new MinIoOssProperties();
        minIoOssProperties.setEndPoint("http://127.0.0.1:9000");
        minIoOssProperties.setAccessKey("admin");
        minIoOssProperties.setSecretKey("admin123456");
        ossStrategy = new MinIoOssStrategy(minIoOssProperties);
    }

    @Test
    public void testPut() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/test.png");
        String ossUrl = ossStrategy.put("code", "test123.png", resourceAsStream);
        System.out.println(ossUrl);
        Assertions.assertNotNull(ossUrl);
    }

    @Test
    public void testGet() {
        InputStream ossUrl = ossStrategy.get("code", "test.txt");
        String read = IoUtil.read(ossUrl, StandardCharsets.UTF_8);
        System.out.println(read);
        Assertions.assertNotNull(read);
    }
}