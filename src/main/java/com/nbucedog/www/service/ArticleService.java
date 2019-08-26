package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.dao.repository.ArticleDAO;
import com.nbucedog.www.model.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;

    public List<Articles> getArticles(){
        return articleDAO.getArticles();
    }

    public Page<Article> findByPage(int pageIndex, int pageSize, Example<Article> articleExample){
        Pageable pageable = PageRequest.of(pageIndex,pageSize, Sort.Direction.DESC,"id");
        return articleDAO.findAll(articleExample,pageable);
    }

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
