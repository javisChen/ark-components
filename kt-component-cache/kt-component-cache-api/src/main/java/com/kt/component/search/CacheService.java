//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kt.component.search;


/**
 * 定义缓存通用接口
 * @author victor
 */
public interface CacheService {

    void set(String key, Object value);

    boolean set(String key, Object value, Long expireTime);

    Object get(String key);

    void remove(String... keys);
}