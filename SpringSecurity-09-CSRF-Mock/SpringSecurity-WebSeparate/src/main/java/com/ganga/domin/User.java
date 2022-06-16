package com.ganga.domin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements UserDetails {

    @TableId
    private Integer id;
    private String username;
    private String password;

    @TableField("enabled")
    private Boolean enabled; //用户是否可用
    @TableField("accountNonExpired")
    private Boolean accountNonExpired; //账户是否过期
    @TableField("accountNonLocked")
    private Boolean accountNonLocked; //账户是否锁定
    @TableField("credentialsNonExpired")
    private Boolean credentialsNonExpired; //密码是否过期

    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //权限集合
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //写入权限
        roles.forEach((role)->{
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        //返回权限
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
