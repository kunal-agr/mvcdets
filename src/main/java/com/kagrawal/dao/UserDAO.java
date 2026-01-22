package com.kagrawal.dao;

import com.kagrawal.model.User;

public interface UserDAO {
    boolean addUser(User user);
    User validateUser(String email, String password);
    User getUserById(int userId);
    boolean updatePassword(int userId, String newPassword);
    User validateUserForResetPassword(String email, Long mobile);
    boolean validateUserByIdAndPassword(int userId, String password);
    User updateProfile(int userId, String name, Long mobile);
}
