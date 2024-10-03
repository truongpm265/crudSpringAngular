package com.example.searchdq;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Cấu hình CORS cho tất cả các đường dẫn API bắt đầu bằng "/api/"
                .allowedOrigins("http://localhost:4200") // Cho phép nguồn gốc từ Angular app
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method được phép
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Cho phép sử dụng credentials (cookie, auth)
    }
}

