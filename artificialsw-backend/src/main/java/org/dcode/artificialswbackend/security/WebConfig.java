package org.dcode.artificialswbackend.security; // SecurityConfig와 같은 패키지로 지정

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 실제 배포용에선 특정 Origin만 등록하세요
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }


}
