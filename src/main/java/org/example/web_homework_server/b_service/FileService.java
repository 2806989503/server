package org.example.web_homework_server.b_service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {


    boolean createFolder(String path, String username);

    boolean hasPermission(String username, String url);

    String getFileOwner(String path);

    boolean deleteFolderOrFile(String path);

    int moveFolder(String oldPath, String newPath);

    boolean saveFile(MultipartFile file, String path, String username);

    boolean renameFile(String filePath, String oldFileName, String newFileName);
}
