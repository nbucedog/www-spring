package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Permission;
import com.nbucedog.www.dao.entity.Role;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.dao.repository.UserDAO;
import com.nbucedog.www.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserDAO userDAO;
    public void save(User user){
        userDAO.save(user);
    }

    public void delete(User user){
        userDAO.delete(user);
    }
    public User findById(int id){
        return userDAO.findById(id).orElse(null);
    }

    public User findByUsername(String username){
        return userDAO.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
        System.out.println("UserService:"+s);
        User user = userDAO.findByUsername(s);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role:user.getRoleSet()){
            for (Permission permission:role.getPermissionSet())
            authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
        }
        SecurityUser securityUser = new SecurityUser(user.getUsername(),user.getPassword(),authorities);
        securityUser.setId(user.getId());
        return securityUser;
    }
}
