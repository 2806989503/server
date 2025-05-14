package org.example.web_homework_server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private String description;
    private long id = -1;
    private String[] images;
    private String title;
    private String userName;
}