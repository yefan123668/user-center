package com.hy.usercenter.service;

import com.hy.usercenter.model.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author a
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2023-02-08 11:36:05
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 用户校验密码
     * @return 新用户ID
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    SysUser userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户列表查询，并脱敏
     *
     * @param userAccount
     * @return
     */
    List<SysUser> userList(String userAccount);

    /**
     * 用户脱敏
     *
     * @param user 原始用户info
     * @return 脱敏user
     */
    SysUser maskUser(SysUser user);
}
