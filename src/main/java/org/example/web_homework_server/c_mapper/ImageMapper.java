package org.example.web_homework_server.c_mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.web_homework_server.pojo.Image;

@Mapper
public interface ImageMapper {
    int insert(Image image);

    String getUrl(String name);

    String[] selectLocalUrl(long idPost);

    boolean deleteByPostId(long postId);

    String[] selectReadUrl(long idPost);
}
