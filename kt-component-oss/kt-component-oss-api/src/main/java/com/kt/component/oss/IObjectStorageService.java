//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kt.component.oss;


import java.io.InputStream;
import java.io.OutputStream;

/**
 * 定义OSS通用接口
 * @author victor
 */
public interface IObjectStorageService {

    String put(String bucketName, String objectName, InputStream inputstream);

    void delete(String bucketName, String objectName);

    boolean exists(String bucketName, String objectName);

    OutputStream get(String fileURI);

    String getFileUrl(String objectName);

}
