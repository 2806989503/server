package org.example.web_homework_server.b_service;

import org.example.web_homework_server.pojo.Image;

public interface ImageService {
    int saveImage(Image image);

    String getUrl(String name);

    boolean deleteIamge(long id);

}
