package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.ReviewReply;
import com.nbucedog.www.dao.repository.ReviewReplyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ReviewReplyService {
    @Autowired
    ReviewReplyDAO reviewReplyDAO;

    public ReviewReply findById(Integer id){
        return reviewReplyDAO.findById(id).orElse(null);
    }

    @PreAuthorize("#reviewReply.user.id==authentication.principal.id")
    public void save(ReviewReply reviewReply){
        if (reviewReply.getId() != null)
            return; //防止伪造id，造成权限泄漏
        reviewReplyDAO.save(reviewReply);
    }

    @PreAuthorize("#reviewReply.user.id==authentication.principal.id")
    public void delete(ReviewReply reviewReply) {
        reviewReplyDAO.deleteById(reviewReply.getId());
    }
}
