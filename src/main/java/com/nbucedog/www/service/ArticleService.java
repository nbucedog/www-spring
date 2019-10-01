package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.dao.repository.ArticleDAO;
import com.nbucedog.www.model.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;

    public Page<Articles> getArticles(int pageIndex, int pageSize, User user){
        Pageable pageable = PageRequest.of(pageIndex,pageSize, Sort.Direction.DESC,"id");
        if(user==null){
            return articleDAO.getArticles(pageable);
        }
        else {
            return articleDAO.getArticles(user, pageable);
        }
    }

//    public Page<Article> findByPage(int pageIndex, int pageSize, Example<Article> articleExample){
//        Pageable pageable = PageRequest.of(pageIndex,pageSize, Sort.Direction.DESC,"id");
//        return articleDAO.findAll(articleExample,pageable);
//    }

    public Article findById(Integer id){
        return articleDAO.findById(id).orElse(null);
    }

    public void save(Article article){
        articleDAO.save(article);
    }

    @Transactional
    public void update(Article article){
        articleDAO.save(article);
    }

    public void delete(Article article){
        articleDAO.delete(article);
    }

    @Transactional
    public void updateViewsById(Integer id,Integer views){
        articleDAO.updateViewsById(id,views);
    }
}
