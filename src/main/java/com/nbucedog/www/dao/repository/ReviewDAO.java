package com.nbucedog.www.dao.repository;

import com.nbucedog.www.dao.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "ReviewDAO")
public interface ReviewDAO extends JpaRepository<Review,Integer>, JpaSpecificationExecutor<Review> {
    @Query("select r from Review r where r.article.id = ?1 order by r.id desc")
    List<Review> findAllByArticleId(Integer articleId);

    @Query("select c from Review c where c.article=null order by c.id desc")
    List<Review> findComments();
}
