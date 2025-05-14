package org.example.web_homework_server.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String imgUrl;
    private String password;
    private String username;
}