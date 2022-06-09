package com.ganga.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ganga.domin.Role;
import com.ganga.domin.User;
import com.ganga.mapper.RoleMapper;
import com.ganga.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 自定义UserDetailsService 自定义数据源
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    //注入dao
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    @Autowired
    public MyUserDetailService(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername,username);
        User user = userMapper.selectOne(qw);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名或密码错误");
        //获取权限信息
        List<Role> roles = roleMapper.getRolesByUid(user.getId());
        user.setRoles(roles);

        //返回封装好的UserDetails
        return user;
    }
}
