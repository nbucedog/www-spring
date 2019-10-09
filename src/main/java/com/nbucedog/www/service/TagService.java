package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Tag;
import com.nbucedog.www.dao.repository.TagDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    TagDAO tagDAO;

    @PreAuthorize("hasAuthority('add_tag')")
    public void save(Tag tag){
        tagDAO.save(tag);
    }

    public Tag findById(int id){
        return tagDAO.findById(id).orElse(null);
    }

    public Tag findByTag(String tag){
        return tagDAO.findByTag(tag);
    }
}
