package com.hy.usercenter;

import com.hy.usercenter.mapper.SysUserMapper;
import com.hy.usercenter.model.domain.SysUser;
import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log
public class UserTest {

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 测试user查询方法
     */
    @Test
    public void testSelectUserAll() {
        List<SysUser> sysUsers = sysUserMapper.selectList(null);
        Assert.assertNotEquals(0, sysUsers.size());
        log.info("查询结果："+sysUsers);
    }
}
