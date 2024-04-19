package com.hzx.springmvc.servlet;

import com.hzx.springmvc.ioc.HzxSpringApplicationContext;
import com.hzx.springmvc.mapper.HandlerMapper;
import com.hzx.springmvc.mapper.HandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 17:30
 * @description: TODO
 */
public class DispatcherServlet extends HttpServlet {

    public String contextPath;

    private HandlerMapping handlerMapping;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.contextPath = getServletContext().getContextPath();
        //初始化容器，扫描包获取全类名
        HzxSpringApplicationContext ioc = new HzxSpringApplicationContext("config.xml");
        try {
            //扫描所有bean的全类名；实例化组件对象
            ioc.init(this.contextPath);
            //构建处理器执行链
            this.handlerMapping = new HandlerMapping();
            handlerMapping.executeHandlerMapping(ioc.getSingletonObjects());
            System.out.println("!!!---处理器执行链构建完成 ---!!!");
            System.out.println(handlerMapping.getHandlerMapperList());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("!!! --- DispatcherServlet ----- doPost ----- invoked --- !!!!");
        try {
            //分发处理请求
            executeDispatch(req, resp);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeDispatch(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        List<HandlerMapper> handlerMapperList = this.handlerMapping.getHandlerMapperList();
        //检查执行链是否被初始化
        if (handlerMapperList.isEmpty()) return;

        //获取请求的 uri 映射; 清除无关的项目路径
        String requestURI = request.getRequestURI().replace(this.contextPath, "");
        System.out.println("请求的URI:" + requestURI);
        //遍历所有处理器映射器
        for (HandlerMapper handlerMapper : handlerMapperList) {
            if (handlerMapper.getUri().equals(requestURI)) {
                //如果uri匹配，执行后续操作
                Method method = handlerMapper.getMethod();
                Object bean = handlerMapper.getBean();
                method.invoke(bean, request, response);
            }
        }
    }
}
