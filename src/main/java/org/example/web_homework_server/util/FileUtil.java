package org.example.web_homework_server.util;

import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {


    /**
     *
     * 移动文件夹，同时用于重命名文件夹
     *
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @return 移动成功？
     */
    public static boolean moveFolder(String oldPath, String newPath) {
        Path sourceFolder = Paths.get(oldPath);
        Path targetFolder = Paths.get(newPath);

        try {
            // 尝试重命名文件夹，如果目标文件夹已存在，则替换它
            Files.move(sourceFolder, targetFolder, StandardCopyOption.REPLACE_EXISTING);
            return true; // 重命名成功
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
            return false; // 重命名失败
        }
    }


    /**
     *
     * 获取路径下所有项目名字
     *
     * @param path 路径
     * @return 文件及文件夹名字列表
     */
    public static List<String> getFileList(String path) {
        List<String> fileList = new ArrayList<>();
        Path startPath = Paths.get(path);

        // 使用try-with-resources自动关闭流
        try (Stream<Path> stream = Files.walk(startPath)) {
            stream.forEach(p -> {
                // 将路径转换为字符串并添加到列表中
                fileList.add(p.toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileList;
    }

    /**
     *
     * 获取一个 原名_时间戳 的文件名
     *
     * @param file 文件
     * @return 新名字
     */

    public static String getFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }
        }

        String fileNameWithoutExtension = originalFilename.contains(".") ? originalFilename.substring(0, originalFilename.lastIndexOf('.')) : originalFilename;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = dateFormat.format(new Date());
        return fileNameWithoutExtension + '_' +timestamp + fileExtension;
    }

    /**
     *
     * 储存文件
     *
     * @param file 文件
     * @param pathWithName 包含文件名的路径
     * @return 成功与否
     */

    public static boolean saveFile(MultipartFile file, String pathWithName) {
        // 文件存储逻辑，比如保存到服务器的某个目录下
        try {
            File destFile = new File(pathWithName);
            file.transferTo(destFile);
            return true;
        } catch (IOException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除指定路径的文件或目录。
     *
     * @param pathStr 要删除的文件或目录的路径。
     * @return 如果删除成功返回true，否则返回false。
     */
    public static boolean deleteFolderOrFile(String pathStr) {
        Path path = Paths.get(pathStr);
        File file = path.toFile();

        // 如果是文件，直接删除
        if (file.isFile()) {
            return file.delete();
        }
        // 如果是目录，递归删除目录下的所有文件和子目录
        else if (file.isDirectory()) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        if (exc == null) {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        } else {
                            // 目录遍历中出现异常
                            throw exc;
                        }
                    }
                });
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        // 如果路径不存在，返回false
        return false;
    }


    /**
     * 尝试重命名文件，并返回操作是否成功。
     *
     * @param filePath    原始文件的路径(不包含文件名）。
     * @param oldFileName 旧的文件名。
     * @param newFileName 新的文件名。
     * @return 如果文件重命名成功，则返回true；否则返回false。
     */
    public static boolean renameFile(String filePath, String oldFileName, String newFileName) {
        File fileToRename = new File(filePath + oldFileName);
        File newFile = new File(fileToRename.getParent(), newFileName);

        // 检查原始文件是否存在
        if (!fileToRename.exists()) {
            System.out.println("文件不存在，无法重命名。");
            return false;
        }

        // 检查新文件名是否已被使用
        if (newFile.exists()) {
            System.out.println("新文件名已被使用，无法重命名。");
            return false;
        }

        // 尝试重命名文件
        boolean success = fileToRename.renameTo(newFile);
        if (success) {
            System.out.println("文件重命名成功。");
        } else {
            System.out.println("文件重命名失败。");
        }
        return success;
    }


    /**
     * 从给定的URL中提取最后一部分作为路径。
     *
     * @param url 完整的URL字符串。
     * @return URL的最后一部分，如果URL为空或null，则返回null。
     */
    public static String extractLastPathSegment(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        // 移除URL末尾的斜杠
        url = url.replaceAll("/+$", "");
        // 使用最后一个'/'分割URL
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            // 如果没有'/'，则整个URL就是最后一部分
            return url;
        }
        // 返回最后一个'/'之后的部分
        return url.substring(lastSlashIndex + 1);
    }

    // 你可以在这里添加其他工具方法



//    public static boolean FileDownloadController() {
//        this.fileStorageLocation = Paths.get("path/to/your/files").toAbsolutePath().normalize();
//        // 确保目录存在
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
//        }
//    }


}
