package com.xxl.mq.admin.mapper;

import com.xxl.mq.admin.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserMapperTest {
    private static Logger logger = LoggerFactory.getLogger(UserMapperTest.class);

    @Resource
    private UserMapper userMapper;

    @Test
    public void loadTest() throws Exception {
        User user = userMapper.loadByUserName("admin");
        Assertions.assertNotNull(user);
    }

}
