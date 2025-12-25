package com.kagrawal.dao;

import com.kagrawal.model.User;

import java.sql.Timestamp;

public interface UserDAO {

    User validateUser(String email, String password);
    User getUserById(int userId);
    boolean addUser(User user);
    User validateUserForResetPassword(String email, Long mobile);
    boolean validateUserByIdAndPassword(int userId, String password);
    boolean updatePassword(int userId, String newPassword);
}
