package com.kagrawal.dao;

import com.kagrawal.model.User;

public interface UserDAO {

    User validateUser(String email, String password);
    User getUserById(int userId);
    boolean addUser(User user);
    User validateUserForResetPassword(String email, Long mobile);
    boolean updatePassword(int userId, String newPassword);
}
