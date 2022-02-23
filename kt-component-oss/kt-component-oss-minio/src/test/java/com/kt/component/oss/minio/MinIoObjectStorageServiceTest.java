package com.kt.component.oss.minio;

import cn.hutool.core.io.FileUtil;
import com.kt.component.oss.IObjectStorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedInputStream;
import java.io.File;

public class MinIoObjectStorageServiceTest {

    private IObjectStorageService iObjectStorageService;

    @Before
    public void setUp() {
        MinIoConfiguration minIoConfiguration = new MinIoConfiguration();
        minIoConfiguration.setEndPoint("http://127.0.0.1:9000");
        minIoConfiguration.setAccessKey("admin");
        minIoConfiguration.setSecretKey("admin123456");
        iObjectStorageService = new MinIoObjectStorageService(minIoConfiguration);
    }

    @Test
    public void put() {
        String pathname = "/Users/chenjiawei/code/myself/kt-cloud/kt-components/kt-component-oss/kt-component-oss-api/src/main/java/com/kt/component/oss/exception/OssException.java";
        BufferedInputStream inputStream = FileUtil.getInputStream(new File(pathname));
        String ossUrl = iObjectStorageService.put("code", "project-a", inputStream);
        System.out.println(ossUrl);
        Assertions.assertNotNull(ossUrl);
    }
}