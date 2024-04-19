package com.hzx.springmvc.service;

import com.hzx.springmvc.bean.User;

import java.util.List;

public interface UserService {


    List<User> listUsers();

    List<User> listUsersById(String Id);
}
