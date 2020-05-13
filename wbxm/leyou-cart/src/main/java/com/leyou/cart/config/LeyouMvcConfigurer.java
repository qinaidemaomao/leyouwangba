package com.leyou.cart.config;

import com.leyou.cart.interceptor.LonginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lizichen
 * @create 2020-04-28 20:53
 */
@Configuration
public class LeyouMvcConfigurer implements WebMvcConfigurer {
    @Autowired
    private LonginInterceptor longinInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // /**拦截所有路径  /* 拦截一级路径
        registry.addInterceptor(longinInterceptor).addPathPatterns("/**");
    }
}

