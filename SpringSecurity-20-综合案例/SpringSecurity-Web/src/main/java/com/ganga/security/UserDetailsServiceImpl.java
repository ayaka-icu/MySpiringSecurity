package com.ganga.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ganga.domain.Role;
import com.ganga.domain.User;
import com.ganga.mapper.RoleMapper;
import com.ganga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 实现自定义数据源
 * 实现密码自定更新加密
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {

    private final UserService userService;
    private final RoleMapper roleMapper;

    @Autowired
    public UserDetailsServiceImpl(UserService userService, RoleMapper roleMapper) {
        this.userService = userService;
        this.roleMapper = roleMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //用户输入的用户名 查询数据库
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername, username);
        User user = userService.getOne(qw);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名或密码错误");
        //查询当前用户的授权信息 并 将授权信息放入用户
        List<Role> roles = roleMapper.getRoleById(user.getId());
        user.setRoles(roles);
        //返回 UserDetails 对象
        return user;
    }


    /**
     * 实现密码 自动更新 和 加密
     *
     * @param user
     * @param newPassword
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        //更新数据库中的密码
        LambdaUpdateWrapper<User> uw = new LambdaUpdateWrapper<>();
        uw.eq(User::getUsername, user.getUsername()) //eq 条件
                .set(User::getPassword, newPassword);
        boolean isOK = userService.update(uw);
        //更新 UserDetails 认证对象
        if (isOK) {
            //由于user -> 接口是 UserDetails 类型 转成实现的User类型
            ((User) user).setPassword(newPassword);
        }
        //返回这个 UserDetails 对象
        return user;
    }
}
