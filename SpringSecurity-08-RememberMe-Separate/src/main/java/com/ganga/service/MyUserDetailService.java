package com.ganga.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ganga.domin.Role;
import com.ganga.domin.User;
import com.ganga.mapper.RoleMapper;
import com.ganga.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 自定义UserDetailsService 自定义数据源
 */
@Service
public class MyUserDetailService implements UserDetailsService , UserDetailsPasswordService {

    //注入dao
    private final UserService userService;
    private final RoleMapper roleMapper;
    @Autowired
    public MyUserDetailService(UserService userService, RoleMapper roleMapper) {
        this.userService = userService;
        this.roleMapper = roleMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username+" --- get");
        //获取用户
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername,username);
        User user = userService.getOne(qw);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名或密码错误");
        System.out.println(user.getUsername());
        //获取权限信息
        List<Role> roles = roleMapper.getRolesByUid(user.getId());
        user.setRoles(roles);

        //返回封装好的UserDetails
        return user;
    }


    /**
     * 更新密码
     *
     * 这里使用了默认的 DelegatingPasswordEncoder 也是推选使用的
     * @param user
     * @param newPassword
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        LambdaUpdateWrapper<User> qw = new LambdaUpdateWrapper<>();
        qw.eq(User::getUsername,user.getUsername())
                .set(User::getPassword,newPassword);
        boolean update = userService.update(qw);
        if (update){
            ((User) user).setPassword(newPassword);
        }

        return user;
    }


    // bcrypt
    // ldap
    // MD4
    // MD5
    // noop
    // pbkdf2
    // scrypt
    // SHA-1
    // SHA-256
    // sha256
    // argon2
    // {bcrypt}$2a$16$cD4Lqb.e4CKaxkQSqVHVSOx4hVpnzLshqDifnSty.8LtHCMqTRFia

}
