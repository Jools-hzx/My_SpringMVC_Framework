package com.hzx.springmvc.servlet;

import com.hzx.springmvc.annotation.RequestParam;
import com.hzx.springmvc.bean.User;
import com.hzx.springmvc.ioc.HzxSpringApplicationContext;
import com.hzx.springmvc.mapper.HandlerMapper;
import com.hzx.springmvc.mapper.HandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 17:30
 * @description: TODO
 */
public class DispatcherServlet extends HttpServlet {

    public String contextPath;
    public String configXmlFileLocation;

    private HandlerMapping handlerMapping;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //获取项目 Application Context
        this.contextPath = getServletContext().getContextPath();
        //获取 XML 配置文件
        this.configXmlFileLocation = getServletConfig().
                getInitParameter("contextConfigLocation").
                split(":")[1];
        //初始化容器，扫描包获取全类名
        HzxSpringApplicationContext ioc = new HzxSpringApplicationContext(this.configXmlFileLocation);
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

    //处理请求，查找处理器映射器，调用处理器执行
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
                Object[] params = executeRequestParamMapping(method, request, response);
                method.invoke(bean, params);
                return;
            }
        }
        //如果没有匹配到 URI; 返回 404
        handleNotFound(request, response);
    }

    /**
     * 通过反射获取 Method 上的注解
     *
     * @param method    目标方法
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     * @return  返回映射完成的实参列表；否则报错
     */
    private Object[] executeRequestParamMapping(Method method,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        //获取形参数目
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];        //得到形参
            Class<?> paramType = parameter.getType();   //得到形参类型
            boolean set = true;     //标记是否将形参与实参匹配成功
            if (paramType.isAssignableFrom(HttpServletRequest.class)) {
                params[i] = request;
            } else if (paramType.isAssignableFrom(HttpServletResponse.class)) {
                params[i] = response;
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                String requestVal = parameter.getAnnotation(RequestParam.class).value();
                set = setRequestParamVal(requestVal, params, request, i);
            } else {
                //否则按照默认的形参名尝试匹配
                set = setRequestParamVal(parameter.getName(), params, request, i);
            }
            if (!set) { //如果未能正确的得到实参，报错
                throw new RuntimeException("Missing requested parameter, param-name:" + parameter.getName());
            }
        }
        return params;
    }

    /**
     * 将 request 携带的参数封装到实参数组的相应位置
     *
     * @param requestedVal @RequestParam 注解自定义的请求key
     * @param params       实参数组
     * @param request      HttpServletRequest 对象
     * @param index        形参数组当前的下标
     * @return true 表示找到符合 key 定义的 value; false 表示未找到
     */
    public boolean setRequestParamVal(String requestedVal,
                                      Object[] params,
                                      HttpServletRequest request,
                                      int index) {
        //获取 HttpServletRequest 携带的所有参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap.isEmpty()) return false;
        if (parameterMap.containsKey(requestedVal)) {
            //当前默认一个key对应一个value
            params[index] = parameterMap.get(requestedVal)[0];
            return true;
        }
        return false;
    }

    //处理无法匹配的 URI 映射请求
    private void handleNotFound(HttpServletRequest request,
                                HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write("<H1>404 NOT FOUND!!! [from DispatcherServlet]</h1>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
