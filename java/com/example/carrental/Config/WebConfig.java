package com.example.carrental.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String EXTERNAL_UPLOAD_DIR = "file:" + System.getProperty("user.home") + "/carrental_uploads/vehicles/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/vehicles/**")
                .addResourceLocations(EXTERNAL_UPLOAD_DIR);

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}