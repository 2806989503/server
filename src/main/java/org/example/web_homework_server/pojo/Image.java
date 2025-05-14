package org.example.web_homework_server.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private Long id;
    private String name;
    private String url_read;
    private String url_local;
    private long viewId;
    private String userName;
}
