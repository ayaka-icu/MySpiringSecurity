package com.ganga.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ganga.domin.User;
import com.ganga.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    void userTest(){
        List<User> list = userService.list();
        for (User user : list) {
            System.out.println(user);
        }
        /**
         * User(id=1, username=root, password={noop}123, enabled=1, accountNonExpired=1, accountNonLocked=1, credentialsNonExpired=1)
         * User(id=2, username=admin, password={noop}123, enabled=1, accountNonExpired=1, accountNonLocked=1, credentialsNonExpired=1)
         * User(id=3, username=blr, password={noop}123, enabled=1, accountNonExpired=1, accountNonLocked=1, credentialsNonExpired=1)
         */
    }

    @Test
    void selectByNameTest(){
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername,"root");
        User user = userService.getOne(qw);
        System.out.println(user);
    }

}
