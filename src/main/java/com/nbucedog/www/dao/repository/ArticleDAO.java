package com.nbucedog.www.dao.repository;
import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.dao.entity.User;
import com.nbucedog.www.model.Articles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository(value = "ArticleDAO")
public interface ArticleDAO extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {
    @Query("select new com.nbucedog.www.model.Articles(a) from Article a where a.publish=true")
    Page<Articles> getArticles(Pageable pageable);

    @Query("select new com.nbucedog.www.model.Articles(a) from Article a where a.publish=true and a.user=?1")
    Page<Articles> getArticles(User user, Pageable pageable);

    @Modifying
    @Query("update Article a set a.views=?2 where a.id=?1")
    void updateViewsById(Integer id,Integer views);

//    @Modifying
//    @Query("update Article a set a.title=?2,a.summary=?3,a.content=?4,a.tagSet=?5,a.publish=?6 where a.id =?1")
//    void updateById(Integer id, String title, String summary, String content, Set<Tag> tagSet, Boolean publish);
//    @Query("update ")
}
