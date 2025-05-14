package org.example.web_homework_server.a_controller;


import lombok.extern.slf4j.Slf4j;
import org.example.web_homework_server.b_service.FileService;
import org.example.web_homework_server.b_service.UserService;
import org.example.web_homework_server.pojo.Result;
import org.example.web_homework_server.pojo.User;
import org.example.web_homework_server.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        User u = userService.login(user);
        if(u != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("imaUrl", u.getImgUrl());
            claims.put("username", u.getUsername());
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        }
        else{
            return Result.error("用户名或密码错误");
        }
    }

    @GetMapping("/list")
    public Result getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public Result getUser(@PathVariable String username) {
        return Result.success(userService.getUser(username));
    }



    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (userService.getUser(user.getUsername()) == null) {
            user.setImgUrl("/resources/images/默认头像.jpg");
            userService.register(user);
            fileService.createFolder(user.getUsername(), user.getUsername());
            User u = userService.login(user);
            if(u != null) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("imaUrl", u.getImgUrl());
                claims.put("username", u.getUsername());
                String jwt = JwtUtils.generateJwt(claims);
                return Result.success(jwt);
            }
            else{
                return Result.success("请手动登录");
            }
        }
        else{
            return Result.error("用户名已被占用");
        }
    }

    @DeleteMapping("/{username}")
    public Result delete(@PathVariable String username) {
        return Result.success(userService.delete(username));
    }

    @PostMapping("/change")
    public Result change(@RequestBody User user) {
        userService.delete(user.getUsername());
        userService.register(user);
        return Result.success();
    }
}
