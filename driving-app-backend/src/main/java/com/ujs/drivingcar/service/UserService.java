package com.ujs.drivingcar.service;

import com.ujs.drivingcar.mapper.UserMapper;
import com.ujs.drivingcar.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserByPhone(String phone) {
        return userMapper.findUserByPhone(phone);
    }
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    public boolean addUser(User user){
        userMapper.addUser(user);
        return true;
    }
    public List<User> queryAll() {
        List<User> userList=userMapper.queryAll();
        return userList;
    }
}