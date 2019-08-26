package com.nbucedog.www.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article implements Serializable {
    public Article(){}

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "title", nullable = false)
    private String title;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "summary")
    private String summary;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonIgnoreProperties({"roleList","articleSet","mail","phone"})
    private User user;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "thumbs")
    private int thumbs;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "views")
    private int views;

    @OneToMany(mappedBy = "article",fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"article","user","reviewReplyList","date","content"})
    private List<Review> reviewList;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "content")
    private String content;

    @ManyToMany(fetch= FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinTable(name = "article_tag_relationships",joinColumns = {@JoinColumn(name = "article_id")},inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @JsonIgnoreProperties("articleList")
    private Set<Tag> tagSet;

    @Basic(fetch = FetchType.EAGER)
    @Column(name="publish")
    private Boolean publish;

    /**
     * setter and getter
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }
}
