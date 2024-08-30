package com.team3.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/uploads/**") // /uploads/로 시작하는 URL 패턴 처리
                .addResourceLocations("file:///C:/uploads/"); // 로컬 파일 시스템의 해당 경로로 매핑
    }

    /*
    /uploads/fb1491cf-76f1-4981-ad34-078f66d9cefc_게시글_목록.png
    으로 요청이 들어오면 spring boot가 c:/uplads/ 에서 해당 파일을 찾아서 제공
     */
}
