package com.hy.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.usercenter.model.domain.SysUser;
import com.hy.usercenter.service.SysUserService;
import com.hy.usercenter.mapper.SysUserMapper;
import com.hy.usercenter.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    //加密盐-俗称搅屎棍
    public static final String SALT = "sjdk:&^%";

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
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

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

    @Override
    public SysUser doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1、校验用户登录信息是否合法
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 后续进行优化，先返回null
            return null;
        }
        if (userAccount.length() < 4) {
            return  null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        //账户不能有特殊字符，仅能包含字母、数字和下划线
        String pattern = "^[a-zA-Z0-9_]+$";
        boolean matches = userAccount.matches(pattern);
        if (!matches) {
            return null;
        }

        // 2. 校验密码是否输入正确，要和数据库中的密文密码去对比
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(SysUser::getUserAccount, userAccount);
        SysUser user = this.getOne(lambdaQueryWrapper);
        if (! encryptPassword.equals(user.getUserPassword())) {
            return null;
        }

        // 3.用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
        String maskPhoneNumber = UserUtils.maskPhoneNumber(user.getPhone());
        String maskEmail = UserUtils.maskEmail(user.getEmail());
        user.setPhone(maskPhoneNumber);
        user.setEmail(maskEmail);

        HttpSession session = request.getSession();
        session.setAttribute(userAccount, user);
        // 60分钟
        session.setMaxInactiveInterval(30 * 60);
        return user;
    }

}




