package org.example.web_homework_server.c_mapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.web_homework_server.pojo.Result;
import org.example.web_homework_server.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {

    User getUser(String username);

    void register(User user);

    boolean delete(String username);

    List<User> getAllUsers();

    User login(User user);
}
