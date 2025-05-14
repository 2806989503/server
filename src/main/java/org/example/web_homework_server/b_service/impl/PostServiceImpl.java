package org.example.web_homework_server.b_service.impl;

import org.example.web_homework_server.c_mapper.ImageMapper;
import org.example.web_homework_server.c_mapper.PostMapper;
import org.example.web_homework_server.c_mapper.UserMapper;
import org.example.web_homework_server.pojo.Book;
import org.example.web_homework_server.pojo.Comment;
import org.example.web_homework_server.pojo.Post;
import org.example.web_homework_server.b_service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ImageMapper imageMapper;


    /**
    Post在数据库本身没有images【】属性，这里从image表查询后添加到Post里一起返回
     */
    @Override
    public Post select(long id_post) {
        Post post = postMapper.select(id_post);
        String[] images = imageMapper.selectReadUrl(id_post);
        post.setImages(images);
        return post;
    }

    @Override
    public List<Comment> allComments(long id) {
        return postMapper.allComments(id);
    }

    @Override
    public void addComment(long postId, Comment comment) {

        postMapper.addComment(
                comment.getReview(),
                LocalDateTime.now(),
                userMapper.getUser(comment.getUser().getUsername()).getId(),
                postId
        );

    }

    @Override
    public long createPost(Post post) {
        postMapper.createPost(post);
        return post.getId();
    }

    @Override
    public List<Long> listByUser(String username) {
        if(Objects.equals(username, "ALL")){
            return postMapper.listAll();
        }
        return postMapper.listByUser(username);
    }

    @Override
    @Transactional
    public boolean deletePost(long id) {
        return postMapper.deletePost(id) && postMapper.deleteCommentsBySpotId(id);
    }

    @Override
    public Book getBook(long id) {
        return postMapper.getBook(id);
    }

}
