package org.example.web_homework_server.b_service;

import org.example.web_homework_server.pojo.Book;
import org.example.web_homework_server.pojo.Comment;
import org.example.web_homework_server.pojo.Post;

import java.util.List;

public interface PostService {
//    List<String> list(int isM);

    Post select(long id);

    List<Comment> allComments(long id);

    void addComment(long postId, Comment comment);

    long createPost(Post post);

    List<Long> listByUser(String username);

    boolean deletePost(long id);

    Book getBook(long id);
}
