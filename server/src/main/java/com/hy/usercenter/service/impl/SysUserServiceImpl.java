package com.hy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.usercenter.model.domain.SysUser;
import com.hy.usercenter.service.SysUserService;
import com.hy.usercenter.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
* @author minsf
* @description 用户服务实现类
* @createDate 2023-02-08 11:36:05
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword)
    {
        //1、校验(也可以在实体类校验，推荐在这里)
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            return -1L; //应该抛异常，todo 待优化
        }
        if (userAccount.length() < 4) {
            return  -1L;
        }
        if (userPassword.length()<8 || checkPassword.length()<8) {
            return -1L;
        }

        //账户不能有特殊字符，仅能包含字母、数字和下划线
        String pattern = "^[a-zA-Z0-9_]+$";
        boolean matches = userAccount.matches(pattern);
        if (!matches) {
            return -1L;
        }

        if (!userPassword.equals(checkPassword)) {
            return  -1L;
        }

        //用户账户不能重复,放到最后，防止造成性能的浪费
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysUser::getUserAccount, userAccount);
        long count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            return -1L;
        }

        //2、加密 JAVA包自带的加密工具
        final String salt = "sjdk:&^%";
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes(StandardCharsets.UTF_8));

        //插入数据
        SysUser user = new SysUser();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);

        if (!save) {
            return -1L;
        }
        return user.getId();
    }
}




