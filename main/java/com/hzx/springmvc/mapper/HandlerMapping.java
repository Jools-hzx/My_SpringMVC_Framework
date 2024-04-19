package com.hzx.springmvc.mapper;

import com.hzx.springmvc.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 18:47
 * @description: TODO
 */
public class HandlerMapping {

    private List<HandlerMapper> handlerMapperList = new ArrayList<>();

    public HandlerMapping() {
    }

    //遍历所有类，使用反射判断 Method 是否被 RequestMapping 注释
    public void executeHandlerMapping(ConcurrentHashMap<String, Object> map) throws ClassNotFoundException {
        if (map.isEmpty()) return;
        Enumeration<String> beanIds = map.keys();
        while (beanIds.hasMoreElements()) {     //遍历所有 Bean 的Class类对象
            String beanId = beanIds.nextElement();
            Object bean = map.get(beanId);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String uri = annotation.value();
                    //将uri\bean对象示例\方法封装成一个 HandlerMapper
                    HandlerMapper handlerMapper = new HandlerMapper(uri, bean, method);
                    this.handlerMapperList.add(handlerMapper);
                }
            }
        }
    }

    public List<HandlerMapper> getHandlerMapperList() {
        return handlerMapperList;
    }
}
