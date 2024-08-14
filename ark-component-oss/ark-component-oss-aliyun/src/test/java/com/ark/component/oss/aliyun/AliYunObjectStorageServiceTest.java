package com.ark.component.oss.aliyun;

import cn.hutool.core.io.IoUtil;
import com.ark.component.oss.OssStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AliYunObjectStorageServiceTest {

    private OssStrategy ossStrategy;

    @BeforeEach
    public void setUp() {
        AliYunOssProperties minIoConfiguration = new AliYunOssProperties();
        minIoConfiguration.setEndPoint("oss-cn-guangzhou.aliyuncs.com");
        minIoConfiguration.setAccessKey("");
        minIoConfiguration.setSecretKey("");
        ossStrategy = new AliYunOssStrategy(minIoConfiguration);
    }

    @Test
    public void testPut() {
        InputStream resourceAsStream = getClass().getResourceAsStream("/test.txt");
        String ossUrl = ossStrategy.put("kt-code-bucket", "eop/project-a", resourceAsStream);
        System.out.println(ossUrl);
        Assertions.assertNotNull(ossUrl);
    }

    @Test
    public void testGet() {
        InputStream ossUrl = ossStrategy.get("code", "project-a");
        String read = IoUtil.read(ossUrl, StandardCharsets.UTF_8);
        System.out.println(read);
        Assertions.assertNotNull(read);
    }
}