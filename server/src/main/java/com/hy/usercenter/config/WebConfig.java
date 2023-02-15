package com.hy.usercenter.config;

import com.hy.usercenter.filter.LoginFilter;
import com.hy.usercenter.interceptor.LoggingInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description 对请求进行配置。
 *
 * @Author minsf
 * @Date 2023/2/15
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 登录过滤器进行注册
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<LoginFilter> registerLoginFilter() {
        FilterRegistrationBean<LoginFilter> filterRegister =
                new FilterRegistrationBean<>();
        filterRegister.setFilter(new LoginFilter());
        filterRegister.addUrlPatterns("/*");
        filterRegister.setOrder(1);

        //排除登录请求和注册请求
        filterRegister.addInitParameter("exclusions",
                "/api/user/login," +
                        "/api/user/register");

        return filterRegister;
    }


    /**
     * 全局请求日志
     *
     * @param registry 注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
    }
}
