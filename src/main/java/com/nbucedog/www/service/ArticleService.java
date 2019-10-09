package com.nbucedog.www.service;

import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.dao.repository.ArticleDAO;
import com.nbucedog.www.model.Articles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("#article.user.id==authentication.principal.id")
    public void save(Article article){
        if(article.getId() != null)
            return; //防止伪造id，造成权限泄漏
        articleDAO.save(article);
    }

    @PreAuthorize("#article.user.id==authentication.principal.id")
    @Transactional
    public void update(Article article){
        articleDAO.save(article);
    }

    @Secured("ROLE_MASTER")
    @PreAuthorize("#article.user.id==authentication.principal.id or hasAuthority('delete_article')")
    public void delete(Article article){
        articleDAO.delete(article);
    }

    @Transactional
    public void updateViewsById(Integer id,Integer views){
        articleDAO.updateViewsById(id,views);
    }
}
