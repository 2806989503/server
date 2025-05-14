package org.example.web_homework_server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File_my {
    private String file_path;
    private String username;
    private int isPublic;
}
