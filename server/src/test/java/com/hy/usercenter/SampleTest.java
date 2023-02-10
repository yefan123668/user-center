package com.hy.usercenter;

import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)  //不设置就不启动spring
@Log
public class SampleTest {

    @Test
    public void testMethod() {
        log.info("测试方式示例，用junit的依赖");
    }
}
