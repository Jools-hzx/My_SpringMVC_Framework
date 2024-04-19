package com.hzx.springmvc.service.impl;

import com.hzx.springmvc.annotation.Service;
import com.hzx.springmvc.bean.User;
import com.hzx.springmvc.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jools He
 * @version 1.0
 * @date 2024/4/19 18:05
 * @description: TODO
 */
@Service
public class UserServiceImpl implements UserService {


    private List<User> users;

    {
        users = new ArrayList<>();
        users.add(new User("1,", "AAA", 12));
        users.add(new User("2,", "BBB", 12));
        users.add(new User("3,", "CCC", 12));
        users.add(new User("231132,", "DDD", 12));
        users.add(new User("123111,", "EEE", 12));
    }

    @Override
    public List<User> listUsers() {
        return users;
    }

    @Override
    public List<User> listUsersById(String Id) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.getId().contains(Id)) results.add(user);
        }
        return results;
    }
}
