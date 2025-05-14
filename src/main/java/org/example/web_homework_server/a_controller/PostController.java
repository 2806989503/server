package org.example.web_homework_server.a_controller;


import lombok.extern.slf4j.Slf4j;
import org.example.web_homework_server.b_service.ImageService;
import org.example.web_homework_server.b_service.UserService;
import org.example.web_homework_server.pojo.*;
import org.example.web_homework_server.b_service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mORr")
public class PostController {

    @Autowired
    private PostService postServer;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/book/{id}")
    public Result getBook(@PathVariable long id){
        return Result.success(postServer.getBook(id));
    }

    @GetMapping("/posts/{username}")
    public Result getPostListByUser(@PathVariable String username){
        if(username == null){
            return Result.error("未登录");
        }
        else{
            return Result.success(postServer.listByUser(username));
        }
    }


    @GetMapping("/{id}")
    public Result getPost(@PathVariable long id){
        log.info("查询某个帖子");
        Post mr = postServer.select(id);
        return Result.success(mr);
    }

    @PostMapping("/createPost")
    public Result createPost(@RequestBody Post post){
        log.info("创建帖子");
        try {
            long id = postServer.createPost(post);
            return Result.success(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }

    }

    @Transactional
    @DeleteMapping("/{id}/delete")
    public Result deletePost(@PathVariable long id){
        log.info("删除帖子:"+id);

        boolean f1 = postServer.deletePost(id);
        boolean f2 = imageService.deleteIamge(id);

        return Result.success();
    }

    @GetMapping("/{id}/comments")
    public Result getMountainOrRiverComments(@PathVariable Long id){
        log.info("查询某个帖子的评论");
        List<Comment> ct = postServer.allComments(id);
        return Result.success(ct);
    }

    @PostMapping("/{postId}/newComment")
    public Result addComment(@PathVariable long postId, @RequestBody Comment comment){
        log.info("添加评论");
        if(userService.getUser(comment.getUser().getUsername()) != null){
            postServer.addComment(postId, comment);
            return Result.success();
        }
        else{
            return Result.error("请先登录");
        }

    }

}
