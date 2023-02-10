package com.hy.usercenter.service;
import java.util.Date;

import com.hy.usercenter.model.domain.SysUser;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log
class SysUserServiceTest {

    @Resource
    private SysUserService sysUserService;

    @Test
    @DisplayName("测试查询库中的所有用户")
    public void testSelectAll() {
        List<SysUser> list = sysUserService.list();
        assertNotEquals(0, list.size());
    }

    @Test
    @DisplayName("测试插入用户")
    @Disabled
    public void testAddUser() {
        SysUser user = new SysUser();
        user.setUserName("dogfeng123");
        user.setUserAccount("dogfeng123");
        user.setUserPassword("123456789");
        user.setAvatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/Q3VcRlXbcCUjLBIAdcP9hsxuBrfZ1xPU0djJ33PJx0n9VjrAQ9ZRyO5e5RpYs1nOUg2spt1P1GPicgqnapBHNYA/132");
        user.setGender(0);
        user.setPhone("123");
        user.setEmail("935524011@qq.com");
        user.setUserRole(0);
        user.setUserStatus("1");

        boolean result = sysUserService.save(user);
        log.info("插入用户id："+user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("校验参数")
    @Disabled
    void userRegister() {
        String userAccount = "";
        String userPassword = "123456789";
        String checkPassword = "123456789";
        Long result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        //非空校验
        Assertions.assertEquals(-1L, result);
        //账户大于4位
        userAccount = "ab";
        result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1L, result);
        //密码大于等于8位
        userAccount = "minsf";
        userPassword = "1234567";
        checkPassword = "1234567";
        result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1L, result);
        //账户不包含特殊字符
        userAccount = "min sf";
        userPassword = "12345679  ";
        checkPassword = "12345679";
        result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1L, result);
        //校验密码必须和密码相同
        userAccount = "xxoo";
        userPassword = "123456789";
        checkPassword = "12345679";
        result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1L, result);
        // 成功测试
        userAccount = "dogfeng";
        userPassword = "12345679";
        checkPassword = "12345679";
        result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertNotEquals(-1L, result);
        // 不能有相同的账户
        userAccount = "dogfeng";
        userPassword = "12345679";
        checkPassword = "12345679";
        result = sysUserService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1L, result);
    }

    @Test
    @DisplayName("用户登录测试")
    void doLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String userAccount = "dogfeng";
        // 校验密码不匹配
        String userPassword = "12345679000";
        SysUser user = sysUserService.userLogin(userAccount, userPassword, request);
        Assertions.assertNull(user);
        // 登录成功
        userPassword = "12345679";
        user = sysUserService.userLogin(userAccount, userPassword, request);
        Assertions.assertNotNull(user);
        // 查看是否脱敏处理
        log.info(user.toString());
    }
}