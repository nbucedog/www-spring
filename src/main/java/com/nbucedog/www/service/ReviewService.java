package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Review;
import com.nbucedog.www.dao.repository.ReviewDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewDAO reviewDAO;

    public void save(Review review){
        reviewDAO.save(review);
    }

    public List<Review> findAllByArticleId(Integer articleId){
        return reviewDAO.findAllByArticleId(articleId);
    }

    public List<Review> findComments(){
        return reviewDAO.findComments();
    }
}
