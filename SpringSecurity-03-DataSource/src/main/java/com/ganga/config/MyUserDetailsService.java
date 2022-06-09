package com.ganga.config;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ganga.domin.Role;
import com.ganga.domin.User;
import com.ganga.mapper.RoleMapper;
import com.ganga.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义数据源
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername,username);
        User user = userService.getOne(qw);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户不存在");
        //查询授权信息
        user.setRoles(roleMapper.getRolesByUid(user.getId()));//放入用户信息

        //返回这个user   确切的说 是 UserDetails 的实现类对象
        return user;
    }

}
