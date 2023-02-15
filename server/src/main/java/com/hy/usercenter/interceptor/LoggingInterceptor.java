package com.hy.usercenter.interceptor;

import com.hy.usercenter.model.domain.SysUser;
import lombok.extern.java.Log;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.hy.usercenter.constants.UserConstant.USER_LOGIN_STATE;

/**
 * @Description
 * @Author minsf
 * @Date 2023/2/15
 */
@Log
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();

        String requestURI = request.getRequestURI();

        String queryString = request.getQueryString();

        SysUser user = Optional.ofNullable((SysUser) request.getSession()
                .getAttribute(USER_LOGIN_STATE)).orElse(new SysUser());

        log.info("++++++++++++++++++++++++++++++++++++++");

        log.info("用户："+Optional.ofNullable(user.getUserAccount()).orElse("*")+"， 发起了请求"
        +"，请求路径："+requestURI+"，请求方法："+method+"，请求参数："+queryString);

        log.info("++++++++++++++++++++++++++++++++++++++");

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
