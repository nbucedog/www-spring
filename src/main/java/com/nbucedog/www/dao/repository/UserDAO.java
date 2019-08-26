package com.nbucedog.www.dao.repository;

import com.nbucedog.www.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository(value = "UserDAO")
public interface UserDAO extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    @Query("select u from User u where lower(u.username) =lower(?1)")
    User findByUsername(String username);
}
