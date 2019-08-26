package com.nbucedog.www.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {
    public User(){}

    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "username",nullable = false)
    private String username;

    @Basic
    @Column(name = "password",nullable = false)
    @JsonIgnore
    private String password;

    @Basic
    @Column(name = "state")
    @JsonIgnore
    private Byte state;

    @Basic
    @Column(name = "nickname")
    private String nickname;

    @Basic
    @Column(name = "sex")
    private String sex;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_relationships",joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @JsonIgnoreProperties({"userList","userSet","role","permissionSet"})
    private Set<Role> roleSet;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
//    @JsonBackReference
    @JsonIgnoreProperties({"user","content"})
    private Set<Article> articleSet;

    @Basic
    @Column(name = "mail")
    private String mail;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "resume")
    private String resume;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public Set<Article> getArticleSet() {
        return articleSet;
    }

    public void setArticleSet(Set<Article> articleSet) {
        this.articleSet = articleSet;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
