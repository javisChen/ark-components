package com.ark.component.cache.core.key;

/**
 * 缓存Key生成配置
 *
 * @author JC
 */
public class KeyGenerateConfig {
    
    private boolean enableEnvPrefix = true;  // 是否启用环境前缀
    private String appPrefix;                // 应用前缀

    public static KeyGenerateConfig create() {
        return new KeyGenerateConfig();
    }

    public KeyGenerateConfig disableEnvPrefix() {
        this.enableEnvPrefix = false;
        return this;
    }

    public KeyGenerateConfig appPrefix(String appPrefix) {
        this.appPrefix = appPrefix;
        return this;
    }

    public boolean isEnableEnvPrefix() {
        return enableEnvPrefix;
    }

    public String getAppPrefix() {
        return appPrefix;
    }
} 