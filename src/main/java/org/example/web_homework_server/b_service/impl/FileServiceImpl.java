package org.example.web_homework_server.b_service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.web_homework_server.b_service.FileService;
import org.example.web_homework_server.c_mapper.FileMapper;
import org.example.web_homework_server.pojo.File_my;
import org.example.web_homework_server.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileMapper fileMapper;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Override
    public boolean createFolder(String path, String username) {
        // 构建完整的文件夹路径
        Path fullPath = Paths.get(fileStorageLocation, path).toAbsolutePath();

        // 创建文件夹
        try {
            Files.createDirectories(fullPath);
            fileMapper.createFile(path, username);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean hasPermission(String username, String url) {
        File_my file = fileMapper.selectFileByPath(url);

        return file != null && (file.getUsername().equals(username) || file.getIsPublic() == 1);

//        String[] parts = url.split("[\\\\/]+");
//        for (int i = 1; i < parts.length; i++) {
//            System.out.println(parts[i]);
//        }
    }

    @Override
    public String getFileOwner(String path) {
        return fileMapper.selectFileByPath(path).getUsername();
    }

    @Override
    public boolean deleteFolderOrFile(String path) {
        if(FileUtil.deleteFolderOrFile(path)){
            fileMapper.deleteFolder(path);
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public int moveFolder(String oldPath, String newPath) {
        return fileMapper.moveFolder(oldPath, newPath);
    }

    @Override
    public boolean saveFile(MultipartFile file, String path, String username) {
        String filename = file.getOriginalFilename();
        String localPathWithName = fileStorageLocation + path + '/' + filename;

        log.info(localPathWithName);
        // 文件存储逻辑，比如保存到服务器的某个目录下
        if(FileUtil.saveFile(file, localPathWithName)){

            fileMapper.createFile(path + '/' + filename, username);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean renameFile(String filePath, String oldFileName, String newFileName) {
        if(FileUtil.renameFile(filePath, oldFileName, newFileName)){
            fileMapper.moveFolder(filePath+oldFileName, newFileName+newFileName);
            return true;
        }
        return false;
    }


}
