package com.bob.commonutil.config;

import com.bob.commonutil.service.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonUtilConfig {
    @Bean
    public FileService fileService() {
        return new FileService();
    }
}

