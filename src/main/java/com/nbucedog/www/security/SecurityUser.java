package com.nbucedog.www.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SecurityUser extends User{
    private Integer id;

    public SecurityUser(String username, String password, Collection<GrantedAuthority> authorities) throws IllegalArgumentException{
        super(username,password,authorities);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
