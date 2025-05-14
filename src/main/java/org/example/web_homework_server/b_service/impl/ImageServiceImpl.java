package org.example.web_homework_server.b_service.impl;

import org.example.web_homework_server.b_service.ImageService;
import org.example.web_homework_server.c_mapper.ImageMapper;
import org.example.web_homework_server.pojo.Image;
import org.example.web_homework_server.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public int saveImage(Image image) {
        return imageMapper.insert(image);
    }

    @Override
    public String getUrl(String name) {
        return imageMapper.getUrl(name) + name;
    }



    @Override
    public boolean deleteIamge(long postId) {
        try {
            String[] images = imageMapper.selectLocalUrl(postId);
            for (String image : images) {
                FileUtil.deleteFolderOrFile(image);
            }
            imageMapper.deleteByPostId(postId);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
