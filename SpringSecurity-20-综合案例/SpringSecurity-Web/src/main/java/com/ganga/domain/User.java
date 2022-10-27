package com.ganga.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements UserDetails {

    @TableId
    private Integer id;

    private String username;
    private String password;

    @TableField("enabled")
    private boolean enabled;
    @TableField("accountNonExpired")
    private boolean accountNonExpired;
    @TableField("accountNonLocked")
    private boolean accountNonLocked;
    @TableField("credentialsNonExpired")
    private boolean credentialsNonExpired;

    // 非数据库字段
    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();

    //将授权信息 放入user对象当中
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //先定义一个授权集合
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        //将授权信息创建一个授权对象 并 传入授权集合当中
        roles.forEach(role -> {
            grantedAuthority.add(new SimpleGrantedAuthority(role.getName()));
        });
        //返回授权集合
        return grantedAuthority;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
