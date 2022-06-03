package capstone.jejuTourrecommend.web;


import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("ACCESS-TOKEN")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:3000","http://3.34.29.242:8080")
                .allowedMethods("*")
                .maxAge(3600);
    }

}



