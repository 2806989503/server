package org.example.web_homework_server.b_service;

import org.example.web_homework_server.pojo.Result;
import org.example.web_homework_server.pojo.User;

import java.util.List;

public interface UserService {

    User getUser(String username);

    void register(User user);

    boolean delete(String username);

    List<User> getAllUsers();

    User login(User user);
}
