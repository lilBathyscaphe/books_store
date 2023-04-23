package com.epam.bookstore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    private static final String ACCESS_DIR_FORMAT = "/%s/**";
    private static final String LOCATION_DIR_FORMAT = "file:%s/";

    @Value("${application.storage.baseDir}")
    private String previewImageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String accessUri = String.format(ACCESS_DIR_FORMAT, previewImageDir);
        String location = String.format(LOCATION_DIR_FORMAT, previewImageDir);
        registry.addResourceHandler(accessUri).addResourceLocations(location);
    }
}
