package com.ganga.mapper;

import com.ganga.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void getRoleByIdTest(){
        List<Role> roleById = roleMapper.getRoleById(1);
        for (Role role : roleById) {
            System.out.println(role);
        }
    }

}
