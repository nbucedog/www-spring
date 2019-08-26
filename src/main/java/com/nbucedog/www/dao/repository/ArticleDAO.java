package com.nbucedog.www.dao.repository;
import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.model.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "ArticleDAO")
public interface ArticleDAO extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {
    @Query("select new com.nbucedog.www.model.Articles(a) from Article a")
    List<Articles> getArticles();

    @Modifying
    @Query("update Article a set a.views=?2 where a.id=?1")
    void updateViewsById(Integer id,Integer views);

//    @Modifying
//    @Query("update Article a set a.title=?2,a.summary=?3,a.content=?4,a.tagSet=?5,a.publish=?6 where a.id =?1")
//    void updateById(Integer id, String title, String summary, String content, Set<Tag> tagSet, Boolean publish);
//    @Query("update ")
}
