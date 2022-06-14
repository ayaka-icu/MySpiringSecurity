package com.ganga.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ganga.domain.Role;
import com.ganga.domain.User;
import com.ganga.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * 自定义数据源
 * 密码更新
 */
@Service
public class LoginUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

    private final UserService userService;
    private final RoleMapper roleMapper;
    @Autowired
    public LoginUserDetailsService(UserService userService, RoleMapper roleMapper) {
        this.userService = userService;
        this.roleMapper = roleMapper;
    }


    /**
     * 自定义数据源
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1.从数据库中获取用户数据
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getUsername,username);
        User user = userService.getOne(qw);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名或密码错误");

        //2.获取用户权限信息 并封装到user对象中
        List<Role> roles = roleMapper.getRoles(user.getId());
        user.setRoles(roles);

        //3.返回封装好的 UserDetails
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
