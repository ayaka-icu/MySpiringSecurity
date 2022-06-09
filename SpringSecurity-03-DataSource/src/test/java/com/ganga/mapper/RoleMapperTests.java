package com.ganga.mapper;

import com.ganga.domin.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RoleMapperTests {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void roleTest(){
        List<Role> roles = roleMapper.getRolesByUid(2);
        for (Role role : roles) {
            System.out.println(role);
        }
    }

}
