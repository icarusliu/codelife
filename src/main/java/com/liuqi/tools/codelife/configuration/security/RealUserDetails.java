package com.liuqi.tools.codelife.configuration.security;

import com.liuqi.tools.codelife.entity.Role;
import com.liuqi.tools.codelife.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserDetails的实现类
 *
 * @Author: LiuQI
 * @Created: 2018/3/23 20:03
 * @Version: V1.0
 **/
public class RealUserDetails implements UserDetails {
    private User user;
    
    private String remoteIp;
    
    public RealUserDetails(User user) {
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Optional<Collection<Role>> roles = Optional.ofNullable(user.getRoles());
        
        Collection<Role> list = roles.orElse(Collections.EMPTY_LIST);
        return list.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
    
    public String getRemoteIp() {
        return remoteIp;
    }
    
    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
}
