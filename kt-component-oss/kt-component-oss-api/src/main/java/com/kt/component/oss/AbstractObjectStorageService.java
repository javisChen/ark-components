package com.kt.component.oss;

import java.io.InputStream;

public class AbstractObjectStorageService implements IObjectStorageService {

    @Override
    public String put(String bucketName, String objectName, InputStream inputstream) {
        return null;
    }

    @Override
    public void delete(String bucketName, String objectName) {

    }

    @Override
    public boolean exists(String bucketName, String objectName) {
        return false;
    }

    @Override
    public InputStream get(String bucketName, String objectName) {
        return null;
    }

    @Override
    public String getFileUrl(String objectName) {
        return null;
    }

}
