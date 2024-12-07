package com.se518.teamproject;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public RateLimitingFilter rateLimitingFilterBean() {
        return new RateLimitingFilter();
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimitingFilterBean()); // Use the bean
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
