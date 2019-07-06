package com.example.demo.model;


import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user")
public class User {

    private Long id;

    private String username;

    private String password;

    public User(Long id, String userName, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
