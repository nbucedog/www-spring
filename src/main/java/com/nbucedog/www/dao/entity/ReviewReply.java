package com.nbucedog.www.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
//import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "review_replies")
public class ReviewReply implements Serializable {
    private static final long serialVersionUID = 8L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonIgnoreProperties({"reviewReplyList","articleSet","reviewReplySet","roleSet","mail","phone","resume"})
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "r_user_id",referencedColumnName = "id")
    @JsonIgnoreProperties({"reviewReplyList","articleSet","reviewReplySet","roleSet","mail","phone","resume"})
    private User rUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id",referencedColumnName = "id")
//    @JsonIgnore
    @JsonIgnoreProperties({"article","user","reviewReplyList","content","date"})
    private Review review;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "date")
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getrUser() {
        return rUser;
    }

    public void setrUser(User rUser) {
        this.rUser = rUser;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
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
