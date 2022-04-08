package com.kt.component.oss.aliyun;

import cn.hutool.core.io.IoUtil;
import com.kt.component.oss.IObjectStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AliYunObjectStorageServiceTest {

    private IObjectStorageService iObjectStorageService;

    @Before
    public void setUp() {
        AliYunOssProperties minIoConfiguration = new AliYunOssProperties();
        minIoConfiguration.setEndPoint("oss-cn-guangzhou.aliyuncs.com");
        minIoConfiguration.setAccessKey("LTAI5tB6EGUDPBWRtvwKFcxg");
        minIoConfiguration.setSecretKey("M9gV934ETKDXtHvaBXrK8NEtZHzEEZ");
        iObjectStorageService = new AliYunObjectStorageService(minIoConfiguration);
    }

    @org.junit.Test
    public void testPut() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/test.txt");
        String ossUrl = iObjectStorageService.put("kt-code-bucket", "eop/project-a", resourceAsStream);
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