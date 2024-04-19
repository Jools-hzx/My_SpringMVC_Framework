package com.hzx.springmvc.mapper;

import java.lang.reflect.Method;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 18:47
 * @description: TODO
 */
public class HandlerMapper {

    private String uri;
    private Object bean;
    private Method method;

    public HandlerMapper(String uri, Object bean, Method method) {
        this.uri = uri;
        this.bean = bean;
        this.method = method;
    }

    @Override
    public String toString() {
        return "HandlerMapper{" +
                "uri='" + uri + '\'' +
                ", bean=" + bean +
                ", method=" + method +
                '}';
    }
}
