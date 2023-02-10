package com.hy.usercenter.service;

import com.hy.usercenter.model.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

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

}