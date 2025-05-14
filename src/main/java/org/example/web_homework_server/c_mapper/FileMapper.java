package org.example.web_homework_server.c_mapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.web_homework_server.pojo.File_my;

@Mapper
public interface FileMapper {

    void createFile(String path, String username);

    File_my selectFileByPath(String path);

    int deleteFolder(String path);

    int moveFolder(String oldPath, String newPath);
}
