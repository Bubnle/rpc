package com.lxz.provider;

import com.lxz.model.User;
import com.lxz.service.UserService;

public class UserServiceImpl implements UserService {
    public User getUser(User user) {

        System.out.println("user name is："+user.getName());
        return user;
    }

    @Override
    public Integer ret_20_Age(User user) {
        return 20+user.getAge();
    }
}
