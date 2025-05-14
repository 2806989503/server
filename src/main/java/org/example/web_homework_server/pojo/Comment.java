package org.example.web_homework_server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private long id;
    private String review;
    private User user;
    private LocalDateTime createdTime;
}
