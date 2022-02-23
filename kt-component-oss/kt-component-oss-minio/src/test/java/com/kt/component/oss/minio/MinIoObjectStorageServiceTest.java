package com.kt.component.oss.minio;

import cn.hutool.core.io.IoUtil;
import com.kt.component.oss.IObjectStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MinIoObjectStorageServiceTest {

    private IObjectStorageService iObjectStorageService;

    @Before
    public void setUp() {
        MinIoConfiguration minIoConfiguration = new MinIoConfiguration();
        minIoConfiguration.setEndPoint("http://127.0.0.1:9000");
//        minIoConfiguration.setAccessKey("admin");
//        minIoConfiguration.setSecretKey("admin123456");
        minIoConfiguration.setAccessKey("user");
        minIoConfiguration.setSecretKey("user123456");
        iObjectStorageService = new MinIoObjectStorageService(minIoConfiguration);
    }

    @Test
    public void testPut() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/test.txt");
        String ossUrl = iObjectStorageService.put("code", "project-a", resourceAsStream);
        System.out.println(ossUrl);
        Assertions.assertNotNull(ossUrl);
    }

    @Test
    public void testGet() {
        InputStream ossUrl = iObjectStorageService.get("code", "project-a");
        String read = IoUtil.read(ossUrl, StandardCharsets.UTF_8);
        System.out.println(read);
        Assertions.assertNotNull(read);
    }
}