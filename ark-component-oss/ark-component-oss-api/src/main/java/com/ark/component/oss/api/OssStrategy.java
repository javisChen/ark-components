//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ark.component.oss.api;


import java.io.InputStream;

/**
 * 定义OSS通用接口
 * @author victor
 */
public interface OssStrategy {

    /**
     * 上传对象
     */
    String put(String bucketName, String objectName, InputStream inputstream);

    String put(boolean createBucketIfNotExist, String bucketName, String objectName, InputStream inputstream);

    void delete(String bucketName, String objectName);

    boolean exists(String bucketName, String objectName);

    InputStream get(String bucketName, String objectName);

    String getFileUrl(String objectName);

    OssType ossType();

}
