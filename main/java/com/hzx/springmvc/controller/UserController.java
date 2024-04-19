package com.hzx.springmvc.controller;

import com.hzx.springmvc.annotation.Controller;
import com.hzx.springmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 18:04
 * @description: TODO
 */

@Controller
public class UserController {

    @RequestMapping("/user/list")
    public void listUsers(HttpServletRequest request,
                          HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write("<H1>EMPTY-MESSAGE[from UserController]</h1>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
