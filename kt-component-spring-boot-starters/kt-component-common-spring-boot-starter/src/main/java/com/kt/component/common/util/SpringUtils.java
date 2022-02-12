package com.kt.component.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getProperty(String key, Class<T> clazz) {
        return getApplicationContext().getEnvironment().getProperty(key, clazz);
    }

    public static String getApplicationName() {
        return getApplicationContext().getEnvironment().getProperty("spring.application.name", String.class);
    }

    public static String[] getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles();
    }

}
