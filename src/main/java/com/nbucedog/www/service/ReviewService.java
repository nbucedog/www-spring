package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Review;
import com.nbucedog.www.dao.repository.ReviewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewDAO reviewDAO;

    public Review findById(Integer id){
        return reviewDAO.findById(id).orElse(null);
    }

    @PreAuthorize("#review.user.id==authentication.principal.id")
    public void save(Review review){
        if (review.getId() != null)
            return; //防止伪造id，造成权限泄漏
        reviewDAO.save(review);
    }

    @PreAuthorize("#review.user.id==authentication.principal.id")
    public void delete(Review review){
        reviewDAO.delete(review);
    }

    public List<Review> findAllByArticleId(Integer articleId){
        return reviewDAO.findAllByArticleId(articleId);
    }

    public List<Review> findComments(){
        return reviewDAO.findComments();
    }
}
