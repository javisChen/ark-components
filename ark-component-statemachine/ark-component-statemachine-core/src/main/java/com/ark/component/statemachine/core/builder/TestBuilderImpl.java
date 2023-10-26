package com.ark.component.statemachine.core.builder;

import cn.hutool.core.util.TypeUtil;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestBuilderImpl<S> implements TestBuilder<String, Integer> {

    private Class<S> type;


    public TestBuilderImpl() {
        // getInterfaceT(this, 0);
    }

    public static void main(String[] args) {
        Type type1 = new TypeToken<TestBuilderImpl<String>>() {
        }.getType();
        System.out.println(TypeUtil.getTypeArgument(type1));


    }

    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public Class<?> getInterfaceT(int index) {
        Type[] types = this.getClass().getInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);

    }


    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actType = parameterizedType.getActualTypeArguments()[index];
            return checkType(actType, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type t = pt.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    @Override
    public void getGenericType() {

    }
}
