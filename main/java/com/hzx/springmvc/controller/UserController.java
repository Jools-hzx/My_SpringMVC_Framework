package com.hzx.springmvc.controller;

import com.hzx.springmvc.annotation.*;
import com.hzx.springmvc.bean.User;
import com.hzx.springmvc.service.UserService;
import jdk.nashorn.internal.ir.IfNode;

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

    @RequestMapping("/user/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam("id") String id,
                        @RequestParam("name") String name) {
        Boolean login = userService.login(id, name);
        if (login) {
            request.setAttribute("name", name);
//            return "forward:/login_ok.jsp";
//            return "redirect:/login_ok.jsp";
            return "/login_ok.jsp";
        } else {
            request.setAttribute("name", name);
            return "forward:/login_error.jsp";
        }
    }

    @RequestMapping("/user/find")
    public void findUsers(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam("key") String name) {

        response.setContentType("text/html;charset=utf-8");
        List<User> users = userService.listUsersById(name);
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

    @RequestMapping("/user/list/json")
    @ResponseBody
    public List listUsersByJson(HttpServletRequest request,
                                HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");
        return userService.listUsers();
    }
}
