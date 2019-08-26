package com.nbucedog.www.dao.repository;

import com.nbucedog.www.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
}
