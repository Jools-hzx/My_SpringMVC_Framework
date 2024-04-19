package com.hzx.springmvc.controller;

import com.hzx.springmvc.annotation.AutoWired;
import com.hzx.springmvc.annotation.Controller;
import com.hzx.springmvc.annotation.RequestMapping;
import com.hzx.springmvc.bean.User;
import com.hzx.springmvc.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 18:04
 * @description: TODO
 */

@Controller
public class UserController {

    @AutoWired
    private UserService userService;

    @RequestMapping("/user/list")
    public void listUsers(HttpServletRequest request,
                          HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");
        List<User> users = userService.listUsers();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(users.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
