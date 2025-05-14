package org.example.web_homework_server.c_mapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.web_homework_server.pojo.Book;
import org.example.web_homework_server.pojo.Comment;
import org.example.web_homework_server.pojo.Post;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostMapper {


    Post select(long id);


    List<Comment> allComments(long id);

    void addComment(String comment, LocalDateTime createTime, long authorId, long spotId);

    long createPost(Post post);

    List<Long> listByUser(String username);

    boolean deletePost(long id);

    boolean deleteCommentsBySpotId(long id);

    List<Long> listAll();

    Book getBook(long id);
}
