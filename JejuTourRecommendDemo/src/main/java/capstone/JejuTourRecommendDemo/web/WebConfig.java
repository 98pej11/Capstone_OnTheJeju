package capstone.JejuTourRecommendDemo.web;


import capstone.JejuTourRecommendDemo.web.argumentresolver.LoginMemberArgumentResolver;
import capstone.JejuTourRecommendDemo.web.interceptor.LogInterceptor;
import capstone.JejuTourRecommendDemo.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico"
                        , "/error/**" //오류 페이지 경로
                );


        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/add","/login","/logout",
                        "/css/**", "/*.ico", "/error/**","/error-page/**");
    }   //즉 보면 "/error" 주소는 로그인 체크 인터셉터작동을 제외(excludePathPattern)할 것임











}




























