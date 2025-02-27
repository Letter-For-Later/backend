package org.example.letter.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "http://localhost:8081",
                    "https://localhost:8081",
                    "http://sxzbbamahrynkdgc.tunnel-pt.elice.io",
                    "https://sxzbbamahrynkdgc.tunnel-pt.elice.io"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")  // JWT 토큰을 위한 헤더 노출
                .allowCredentials(true)
                .maxAge(3600);
    }
}
