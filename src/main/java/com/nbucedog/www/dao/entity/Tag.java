package com.nbucedog.www.dao.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag implements Serializable {
    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "tag")
    private String tag;

    @Basic
    @Column(name = "color")
    private String color;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_tag_relationships",joinColumns = {@JoinColumn(name = "tag_id")},inverseJoinColumns = {@JoinColumn(name = "article_id")})
    @JsonIgnoreProperties("tagSet")
    private List<Article> articleList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Article> getArticleList() {
        return articleList;
    }
//    @JsonIgnoreProperties("tagSet")
//    public List<Articles> getArticleList(){
//        List<Articles> articleList = new ArrayList<>();
//        for (Article article:this.articleList){
//            articleList.add(new Articles(article));
//        }
//        return articleList;
//    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
