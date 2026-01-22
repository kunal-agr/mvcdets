package com.kagrawal.dao;

import com.google.cloud.firestore.*;
import com.kagrawal.model.User;
import com.kagrawal.util.FirebaseUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

public class UserDAOImpl implements UserDAO {

    private final Firestore db;

    public UserDAOImpl() {
        this.db = FirebaseUtil.getFirestore();
    }

    @Override
    public boolean addUser(User user) {
        try {
            int userId = (int) (System.currentTimeMillis() / 1000);
            user.setUserId(userId);

            if (user.getMobile() != null) {
                user.setMobile(String.valueOf(user.getMobile()));
            }

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            user.setCreatedAt(LocalDateTime.now().format(formatter));

            db.collection("users")
                    .document(String.valueOf(userId))
                    .set(user)
                    .get();

            return true;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User validateUser(String email, String password) {
        try {
            QuerySnapshot snapshot = db.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("password", password)
                    .get()
                    .get();

            if (snapshot.isEmpty()) return null;

            return snapshot.getDocuments()
                    .get(0)
                    .toObject(User.class);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserById(int userId) {
        try {
            DocumentSnapshot doc = db.collection("users")
                    .document(String.valueOf(userId))
                    .get()
                    .get();

            if (!doc.exists()) return null;

            return doc.toObject(User.class);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        try {
            db.collection("users")
                    .document(String.valueOf(userId))
                    .update("password", newPassword)
                    .get();

            return true;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User validateUserForResetPassword(String email, Long mobile) {
        try {
            QuerySnapshot snapshot = db.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("mobile", String.valueOf(mobile))
                    .get()
                    .get();

            if (snapshot.isEmpty()) return null;

            return snapshot.getDocuments()
                    .get(0)
                    .toObject(User.class);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean validateUserByIdAndPassword(int userId, String password) {
        User user = getUserById(userId);
        return user != null && password.equals(user.getPassword());
    }

    @Override
    public User updateProfile(int userId, String name, Long mobile) {
        try {
            DocumentReference ref = db.collection("users")
                    .document(String.valueOf(userId));

            if (name != null) {
                ref.update("name", name).get();
            }

            if (mobile != null) {
                ref.update("mobile", String.valueOf(mobile)).get();
            }

            return ref.get().get().toObject(User.class);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
