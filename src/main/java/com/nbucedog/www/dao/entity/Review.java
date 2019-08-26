package com.nbucedog.www.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review implements Serializable {
    private static final long serialVersionUID = 7L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id",referencedColumnName = "id")
    @JsonIgnoreProperties({"reviewList","reviewSet","title","summary","date","user","thumbs","views","content","tagSet","publish"})
    private Article article;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonIgnoreProperties({"reviewList","articleSet","reviewSet","roleSet","mail","phone","resume"})
    private User user;

    @OneToMany(mappedBy = "review",fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ReviewReply> reviewReplyList;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "date")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") 0作用
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date date;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "content")
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ReviewReply> getReviewReplyList() {
        return reviewReplyList;
    }

    public void setReviewReplyList(List<ReviewReply> reviewReplyList) {
        this.reviewReplyList = reviewReplyList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
