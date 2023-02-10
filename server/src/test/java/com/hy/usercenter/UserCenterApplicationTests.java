package com.hy.usercenter;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
@Log
class UserCenterApplicationTests {

    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        //测试加密
        /*MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest("abc".getBytes(StandardCharsets.UTF_8));*/

        String s1 = DigestUtils.md5DigestAsHex(("salt" + "mypwd").getBytes(StandardCharsets.UTF_8));
//        String s = new String(digest);
        log.info(s1);
    }

}
