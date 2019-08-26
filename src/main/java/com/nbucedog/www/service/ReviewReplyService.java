package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.ReviewReply;
import com.nbucedog.www.dao.repository.ReviewReplyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewReplyService {
    @Autowired
    ReviewReplyDAO reviewReplyDAO;

    public void save(ReviewReply reviewReply){
        reviewReplyDAO.save(reviewReply);
    }
}
