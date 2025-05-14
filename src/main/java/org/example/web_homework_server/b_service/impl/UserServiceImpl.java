package org.example.web_homework_server.b_service.impl;

import org.example.web_homework_server.b_service.UserService;
import org.example.web_homework_server.c_mapper.UserMapper;
import org.example.web_homework_server.pojo.Result;
import org.example.web_homework_server.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    @Override
    public void register(User user) {
        userMapper.register(user);
    }

    @Override
    public boolean delete(String username) {
        return userMapper.delete(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }
}
