package org.example.web_homework_server.a_controller;

import lombok.extern.slf4j.Slf4j;
import org.example.web_homework_server.b_service.ImageService;
import org.example.web_homework_server.pojo.Image;
import org.example.web_homework_server.pojo.Result;
import org.example.web_homework_server.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Value("${image.storage.location}")
    private String imageStorageLocation;

    @Value("${image.read.location}")
    private String imageReadLocation;

    @PostMapping("/upload")
    public Result uploadImage(@RequestParam("file") MultipartFile file,
                              @RequestParam(name = "username") String username,
                              @RequestParam("postId") long postId) {



        try {
            //将文件名+时间戳
            String fileName = FileUtil.getFileName(file);

            // 文件存储逻辑，比如保存到服务器的某个目录下
            FileUtil.saveFile(file, imageStorageLocation + fileName);

            // 保存图片信息到数据库
            Image image = new Image();
            image.setName(fileName);
            image.setUrl_read(imageReadLocation + fileName);
            image.setUrl_local(imageStorageLocation + fileName);
            image.setViewId(postId);
            image.setUserName(username);
            imageService.saveImage(image);

            return Result.success(fileName);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈信息
            return Result.error("无法上传图片: " + file.getOriginalFilename() + "!");
        }
    }

    @GetMapping("{name}")
    public Result getImageUrl(@PathVariable String name) {
        return Result.success(imageService.getUrl(name));
    }


}
