//package com.nbucedog.www.dao.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import org.hibernate.annotations.Fetch;
//import org.hibernate.annotations.FetchMode;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "comments")
//public class Comment implements Serializable {
//    private static final long serialVersionUID = 2L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    private Integer id;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id",referencedColumnName = "id")
//    @JsonIgnoreProperties("comment")
//    private User user;
//
//    @OneToMany(mappedBy = "comment",fetch = FetchType.EAGER)
//    @Fetch(FetchMode.SUBSELECT)
//    private List<CommentReply> commentReplyList;
//
//    @Basic(fetch = FetchType.EAGER)
//    @Column(name = "date")
//    private Date date;
//
//    @Basic(fetch = FetchType.EAGER)
//    @Column(name = "content")
//    private String content;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public List<CommentReply> getCommentReplyList() {
//        return commentReplyList;
//    }
//
//    public void setCommentReplyList(List<CommentReply> commentReplyList) {
//        this.commentReplyList = commentReplyList;
//    }
//
//    public String getDate() {
//        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        return formatter.format(date);
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//}
