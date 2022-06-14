package com.ganga.mapper;

import com.ganga.domin.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void userTest(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

}
