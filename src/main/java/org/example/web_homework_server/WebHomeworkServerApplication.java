package org.example.web_homework_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan
@SpringBootApplication
public class WebHomeworkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebHomeworkServerApplication.class, args);
    }

}

//写到文件权限，使用filter校验
