package com.tao.po;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;

@Entity
public class User1 {
    private int userid;
    private String username;
    private String password;
    private byte[] pic;
    private int pagenum;
    private String picPath;
    private Set<Article> articles;

    public User1() {

    }

    public User1(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Id
    @Column(name = "userid", nullable = false)
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 11)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 16)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "pic", nullable = true)
    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    @Basic
    @Column(name = "pagenum", nullable = false)
    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    @Basic
    @Column(name = "pic_path", nullable = true, length = 100)
    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User1 user1 = (User1) o;

        if (userid != user1.userid) return false;
        if (pagenum != user1.pagenum) return false;
        if (username != null ? !username.equals(user1.username) : user1.username != null) return false;
        if (password != null ? !password.equals(user1.password) : user1.password != null) return false;
        if (!Arrays.equals(pic, user1.pic)) return false;
        if (picPath != null ? !picPath.equals(user1.picPath) : user1.picPath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userid;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(pic);
        result = 31 * result + pagenum;
        result = 31 * result + (picPath != null ? picPath.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "user")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}
