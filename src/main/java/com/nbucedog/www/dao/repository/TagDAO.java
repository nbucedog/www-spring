package com.nbucedog.www.dao.repository;

import com.nbucedog.www.dao.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDAO extends JpaRepository<Tag,Integer>, JpaSpecificationExecutor<Tag> {
    @Query("select t from Tag t where UPPER(t.tag)=UPPER(?1)")
    Tag findByTag(String tag);
}
