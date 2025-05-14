package org.example.web_homework_server.a_controller;

import lombok.extern.slf4j.Slf4j;
import org.example.web_homework_server.b_service.FileService;
import org.example.web_homework_server.pojo.Result;
import org.example.web_homework_server.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;


@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/files")
public class   FileController {

    @Autowired
    private FileService fileService;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    private boolean isSelf(String path, String username){
        String owner = fileService.getFileOwner(path);
        return owner != null && owner.equals(username);
    }

    @PostMapping("/createFolder")
    public Result createFolder(@RequestBody String path,
                               @RequestAttribute(name = "username") String username) {


        if (isSelf(path, username)) return Result.error("权限不足");


        try {
            fileService.createFolder(path, username);
            return Result.success();
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建失败");
        }
    }

    @DeleteMapping("/deleteFolder")
    public Result deleteFolderOrFile(@RequestBody String path,
                                     @RequestAttribute(name = "username", required = false) String username) {

        if (isSelf(path, username)) return Result.error("权限不足");

        if(fileService.deleteFolderOrFile(path)){
            return Result.success("删除成功");
        }
        else{
            return Result.error("删除失败");
        }
    }

    @PostMapping("/listFiles")
    public Result listFiles(@RequestBody String path){

        String filePath = fileStorageLocation + path;

        log.info(filePath);


        List fileList = FileUtil.getFileList(filePath);
        if(fileList == null){
            return Result.error("未找到文件");
        }
        else{
            return Result.success(fileList);
        }
    }

    @PostMapping("/moveFolder")
    public Result moveFolder(@RequestBody String oldPath, @RequestBody String newPath,
                             @RequestAttribute(name = "username", required = false) String username){

        if (isSelf(oldPath, username)) return Result.error("权限不足");

        if(FileUtil.moveFolder(oldPath, newPath)){
            fileService.moveFolder(oldPath, newPath);
            return Result.success();
        }
        else {
            return Result.error("修改失败");
        }
    }

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam String path,
                             @RequestParam("file") MultipartFile file,
                             @RequestAttribute(name = "username", required = false) String username) {

//        log.info("路径："+path);
//        log.info("文件名"+file.getOriginalFilename());
        log.info("用户名" + username);

        if (isSelf(path, username)) return Result.error("权限不足");

        if(fileService.saveFile(file, path, username)){
            return Result.success();
        }
        else{
            return Result.error("上传失败");
        }

    }

    @PostMapping("/renameFile")
    public Result renameFile(@RequestBody String filePath,
                             @RequestBody String oldFileName,
                             @RequestBody String newFileName){

        if(fileService.renameFile(filePath, oldFileName, newFileName)){
            return Result.success();
        }
        else{
            return Result.error("修改失败");
        }
    }


    @GetMapping("/download")
    public ResponseEntity<UrlResource> downloadFile(@RequestParam String filePath) {

        try {
            // 直接使用前端传递的绝对路径
            log.info("Requested file path: {}", filePath);
            Path path = Path.of(filePath);
            log.info("Full file path: {}", path.toString());
            UrlResource resource = new UrlResource(path.toUri());
            log.info("File URI: {}", path.toUri().toString());

            // 检查文件是否存在
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=\"" + path.getFileName().toString() + "\"")
                        .body(resource); // 直接使用 resource，无需类型转换
            } else {
                log.error("File not found or not readable: {}", path);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }







}