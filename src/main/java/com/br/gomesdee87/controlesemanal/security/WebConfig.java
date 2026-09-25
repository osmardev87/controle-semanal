package com.br.gomesdee87.controlesemanal.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ApiSessionInterceptor interceptor;
    public WebConfig(ApiSessionInterceptor interceptor) { this.interceptor = interceptor; }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/finace/**", "/api/users/**").excludePathPatterns("/finace/login", "/api/users/admin/**");
    }
}