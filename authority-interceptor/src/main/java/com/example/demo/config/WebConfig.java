package com.example.demo.config;

import com.example.demo.interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig.
 *
 * @author Thou
 * @date 2022/10/6
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthorityInterceptor())
                .addPathPatterns("/**");
    }
}
