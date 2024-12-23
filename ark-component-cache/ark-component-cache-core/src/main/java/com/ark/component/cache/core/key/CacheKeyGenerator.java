package com.ark.component.cache.core.key;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * 缓存key生成器
 * 支持灵活的key生成策略
 *
 * @author JC
 */
public class CacheKeyGenerator {
    
    private static final String DELIMITER = ":";
    private final Environment environment;
    private final KeyGenerateConfig config;

    private CacheKeyGenerator(Environment environment, KeyGenerateConfig config) {
        this.environment = environment;
        this.config = config;
    }

    /**
     * 生成缓存key
     *
     * @param key 原始key
     * @return 包装后的key
     */
    public String generate(String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Cache key cannot be empty");
        }

        StringBuilder sb = new StringBuilder();
        
        // 添加环境前缀
        if (config.isEnableEnvPrefix()) {
            String envPrefix = getEnvPrefix();
            if (StringUtils.hasText(envPrefix)) {
                sb.append(envPrefix).append(DELIMITER);
            }
        }
        
        // 添加应用前缀
        if (StringUtils.hasText(config.getAppPrefix())) {
            sb.append(config.getAppPrefix()).append(DELIMITER);
        }
        
        // 添加原始key
        sb.append(key);
        
        return sb.toString();
    }

    public String generate(String key, String appPrefix) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("Cache key cannot be empty");
        }

        StringBuilder sb = new StringBuilder();
        
        // 添加环境前缀
        if (config.isEnableEnvPrefix()) {
            String envPrefix = getEnvPrefix();
            if (StringUtils.hasText(envPrefix)) {
                sb.append(envPrefix).append(DELIMITER);
            }
        }
        
        // 使用传入的应用前缀替代配置的应用前缀
        if (StringUtils.hasText(appPrefix)) {
            sb.append(appPrefix).append(DELIMITER);
        }
        
        sb.append(key);
        return sb.toString();
    }

    private String getEnvPrefix() {
        return environment.getActiveProfiles().length > 0 ? 
            environment.getActiveProfiles()[0] : 
            environment.getDefaultProfiles()[0];
    }

    /**
     * 创建生成器构建器
     */
    public static Builder builder(Environment environment) {
        return new Builder(environment);
    }

    /**
     * 生成器构建器
     */
    public static class Builder {
        private final Environment environment;
        private final KeyGenerateConfig config = KeyGenerateConfig.create();

        private Builder(Environment environment) {
            this.environment = environment;
        }

        public Builder disableEnvPrefix() {
            config.disableEnvPrefix();
            return this;
        }

        public Builder appPrefix(String appPrefix) {
            config.appPrefix(appPrefix);
            return this;
        }

        public CacheKeyGenerator build() {
            return new CacheKeyGenerator(environment, config);
        }
    }
} 