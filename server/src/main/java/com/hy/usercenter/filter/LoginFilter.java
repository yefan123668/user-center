package com.hy.usercenter.filter;

import com.hy.usercenter.common.ResultEnum;
import com.hy.usercenter.exception.CommonException;
import com.hy.usercenter.model.domain.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hy.usercenter.constants.UserConstant.USER_LOGIN_STATE;

/**
 * @Description 登录校验拦截器。
 *
 * @Author minsf
 * @Date 2023/2/15
 */
public class LoginFilter implements Filter {


    /**
     * 每个请求都会在这里认证。
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //得到httpservletrequest
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        SysUser user = (SysUser)session.getAttribute(USER_LOGIN_STATE);

        String requestURI = request.getRequestURI();
        List<String> writeList = new ArrayList<>();
        writeList.add("/api/user/login");
        writeList.add("/api/user/register");

        // 白名单直接放行
        if (user == null && !writeList.contains(requestURI)) {
            throw new CommonException(ResultEnum.NO_LOGIN, "用户未登录");
        } else {
            //放行
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
