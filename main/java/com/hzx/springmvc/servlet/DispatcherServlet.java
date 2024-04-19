package com.hzx.springmvc.servlet;

import com.hzx.springmvc.ioc.HzxSpringApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 17:30
 * @description: TODO
 */
public class DispatcherServlet extends HttpServlet {

    public String contextPath;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.contextPath = getServletContext().getContextPath();
        //初始化容器，扫描包获取全类名
        HzxSpringApplicationContext ioc = new HzxSpringApplicationContext("config.xml");
        try {
            ioc.init(this.contextPath);
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
    }
}
