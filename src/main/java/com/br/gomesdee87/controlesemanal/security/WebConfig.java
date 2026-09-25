package com.br.gomesdee87.controlesemanal.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ApiSessionInterceptor userInterceptor;
    private final AdminSessionInterceptor adminInterceptor;

    public WebConfig(ApiSessionInterceptor userInterceptor, AdminSessionInterceptor adminInterceptor) {
        this.userInterceptor = userInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/finace/**", "/api/users/**")
                .excludePathPatterns("/finace/login");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns("/api/admin/login", "/api/admin/setup", "/api/admin/setup/status");
    }
}