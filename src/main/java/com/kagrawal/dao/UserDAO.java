package com.kagrawal.dao;

import com.kagrawal.model.User;

public interface UserDAO {
    public User validateUser(String email, String password);
    public User getUserById(int userId);
    public boolean addUser(User user);
}
