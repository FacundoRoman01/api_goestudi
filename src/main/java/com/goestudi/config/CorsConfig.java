package com.goestudi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer  {

	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") 
                .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500") // Permite peticiones desde estos orígenes (puertos de desarrollo comunes para Live Server)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite los métodos HTTP que necesites
                .allowedHeaders("*"); // Permite todos los headers
    }
	
}