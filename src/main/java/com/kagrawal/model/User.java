package com.kagrawal.model;

public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String createdAt;

    public User() {}

    public User(String name, String email, String password, String mobile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

    public User(int userId, String name, String email, String password, String mobile, String createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
