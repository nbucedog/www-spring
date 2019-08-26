package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Role;
import com.nbucedog.www.dao.repository.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleDAO roleDAO;
    public Role findById(int id){
        return roleDAO.findById(id).orElse(null);
    }
}
