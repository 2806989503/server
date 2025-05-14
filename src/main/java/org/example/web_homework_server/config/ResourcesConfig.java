package org.example.web_homework_server.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{

    @Value("${image.storage.location}")
    private String imageStorageLocation;

    @Value("${image.read.location}")
    private String imageReadLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 通过url访问项目外的目录图片*/
        registry
                .addResourceHandler(imageReadLocation+"**")
                .addResourceLocations("file:"+ imageStorageLocation);
    }

}