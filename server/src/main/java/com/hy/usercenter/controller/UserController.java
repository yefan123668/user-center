package com.hy.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hy.usercenter.model.domain.SysUser;
import com.hy.usercenter.service.SysUserService;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.hy.usercenter.model.domain.request.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.hy.usercenter.constants.UserConstant.ADMIN;
import static com.hy.usercenter.constants.UserConstant.USER_LOGIN_STATE;

/**
 * 用户处理类
 * @author minsf
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册信息
     * @return 返回uid，后续优化
     */
    @PostMapping("register")
    public Long doRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        /* controller只注重参数的校验，比较专一 */
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            // todo 返回结果后续优化
            return -1L;
        }
        return sysUserService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录信息
     * @param request 获取session用户状态
     * @return 返回用户脱敏的信息
     */
    @PostMapping("login")
    public SysUser doLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 后续优化
            return null;
        }
        return sysUserService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 用户管理-用户连表显示
     *
     * @return 用户列表
     */
    @GetMapping("list")
    public List<SysUser> userList(String userAccount, HttpServletRequest request) {
        // 需要对用户进行鉴权 user_role 0 普通用户， 1 管理员
        if (Boolean.FALSE.equals(isAdmin(request))) {
            return Collections.emptyList();
        }
        return sysUserService.userList(userAccount);
    }

    /**
     * 用户删除
     *
     * @param id 用户的id
     * @return 是否成功
     */
    @PostMapping("delete")
    public Boolean removeUser(@RequestBody Long id, HttpServletRequest request) {
        if (Boolean.FALSE.equals(isAdmin(request))) {
            return false;
        }
        return sysUserService.removeById(id);
    }

    private Boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SysUser user = (SysUser)session.getAttribute(USER_LOGIN_STATE);
        if (user == null || !user.getUserRole().equals(ADMIN)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 获取当前用户的登录态
     *
     * @return 脱敏用户信息
     */
    @GetMapping("current")
    public SysUser getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SysUser user = (SysUser) session.getAttribute(USER_LOGIN_STATE);

        if (user == null) {
            return null;
        }
        //如果用户的信息更新频繁，应该拿去数据库中的用户信息，而不是缓存中的
        Long userId = user.getId();
        SysUser sysUser = sysUserService.getById(userId);
        return sysUserService.maskUser(sysUser);
    }
}
