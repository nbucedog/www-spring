package com.nbucedog.www.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nbucedog.www.dao.entity.Article;
import com.nbucedog.www.dao.entity.Tag;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Articles implements Serializable {
    private static final long serialVersionUID = 11L;

    public Articles(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.date = article.getDate();
        this.nickname = article.getUser().getNickname();
        this.thumbs = article.getThumbs();
        this.views = article.getViews();
        this.comments = article.getReviewList().size();
        this.tagSet = article.getTagSet();
    }

    private int id;
    private String title;
    private String summary;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String nickname;
    private int thumbs;
    private int views;
    private int comments;

    @JsonIgnoreProperties("articleList")
    private Set<Tag> tagSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getThumbs() {
        return thumbs;
    }

    public void setThumbs(int thumbs) {
        this.thumbs = thumbs;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }
}
