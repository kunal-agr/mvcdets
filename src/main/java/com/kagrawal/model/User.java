package com.kagrawal.model;

import java.util.Date;

public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private Long mobile;
    private Date createdAt;

    public User() {}

    public User(int userId, String name, String email, Long mobile, Date createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.createdAt = createdAt;
    }

    public User(int userId, String name, String email, String password, Long mobile, Date createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.createdAt = createdAt;
    }

    public User(String name, String email, String password, Long mobile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getMobile() { return mobile; }
    public void setMobile(Long mobile) { this.mobile = mobile; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
