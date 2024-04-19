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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
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
