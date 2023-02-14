package com.hy.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hy.usercenter.common.BaseResponse;
import com.hy.usercenter.common.ResultEnum;
import com.hy.usercenter.common.ResultUtils;
import com.hy.usercenter.exception.CommonException;
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
    public BaseResponse<Long> doRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        /* controller只注重参数的校验，比较专一 */
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "表单必填参数存在空值");
        }
        Long register = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(register, ResultEnum.SUCCESS);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录信息
     * @param request 获取session用户状态
     * @return 返回用户脱敏的信息
     */
    @PostMapping("login")
    public BaseResponse<SysUser> doLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new CommonException(ResultEnum.PARAMS_ERROR, "表单必填参数存在空值");
        }
        SysUser user = sysUserService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user, ResultEnum.SUCCESS);
    }

    /**
     * 用户管理-用户连表显示
     *
     * @return 用户列表
     */
    @PostMapping("list")
    public BaseResponse<Page<SysUser>> userList(@RequestBody UserListDto paramsUser, HttpServletRequest request) {
        // 需要对用户进行鉴权 user_role 0 普通用户， 1 管理员
        if (Boolean.FALSE.equals(isAdmin(request))) {
            // 非管理员只能查看自己的信息
            SysUser user = (SysUser) request.getSession().getAttribute(USER_LOGIN_STATE);
            List<SysUser> sysUsers = Collections.singletonList(user);
            Page<SysUser> page= new Page<>();
            page.setRecords(sysUsers);
            return ResultUtils.success(page, ResultEnum.SUCCESS);
        }
        if (paramsUser.getUserAccount() == null) {
            paramsUser.setUserAccount("");
        }
        return ResultUtils.success(sysUserService.userList(paramsUser), ResultEnum.SUCCESS);
    }

    /**
     * 用户删除
     *
     * @param id 用户的id
     * @return 是否成功
     */
    @PostMapping("delete")
    public BaseResponse<Boolean> removeUser(@RequestBody Long id, HttpServletRequest request) {
        if (Boolean.FALSE.equals(isAdmin(request))) {
            return ResultUtils.fail(ResultEnum.DATA_DELETE_FAIL_ERROR);
        }
        return ResultUtils.success(sysUserService.removeById(id), ResultEnum.SUCCESS);
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
    public BaseResponse<SysUser> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SysUser user = (SysUser) session.getAttribute(USER_LOGIN_STATE);

        if (user == null) {
            throw new CommonException(ResultEnum.NO_LOGIN, "用户未登录");
        }
        //如果用户的信息更新频繁，应该拿去数据库中的用户信息，而不是缓存中的
        Long userId = user.getId();
        SysUser sysUser = sysUserService.getById(userId);
        return ResultUtils.success(sysUserService.maskUser(sysUser));
    }

    /**
     * 登出
     *
     */
    @PostMapping("outLogin")
    public void outLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(USER_LOGIN_STATE);
    }
}
