package com.kagrawal.dao;

import com.kagrawal.model.User;
import com.kagrawal.util.DBConnection;
import com.kagrawal.util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserDAOImpl implements UserDAO {
    private MongoCollection<Document> userCollection;

    private static final String SELECT_BY_ID =
            "SELECT user_id, name, email, mobile, created_at " +
                    "FROM tbluser WHERE user_id = ?";

    private static final String UPDATE_PROFILE =
            "UPDATE tbluser SET name = ?, mobile = ? WHERE user_id = ?";

    public UserDAOImpl() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        userCollection = db.getCollection("users");
    }

    @Override
    public User validateUser(String email, String password) {
        try {
                Document userDoc = userCollection.find(
                        and(eq("email", email), eq("password", password))
                ).first();
            if (userDoc != null) {
                Long mobile = userDoc.getLong("mobile");

                return new User(
                        userDoc.getInteger("user_id"),
                        userDoc.getString("name"),
                        userDoc.getString("email"),
                        userDoc.getString("password"),
                        mobile,
                        null
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null,
                            rs.getObject("mobile") != null ? rs.getLong("mobile") : null,
                            rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean addUser(User user) {
        try {
            // Find max user_id to generate next one since MongoDB doesn't auto-increment
            Document lastUser = userCollection.find()
                    .sort(new Document("user_id", -1))
                    .first();
            int nextId = 1;
            if (lastUser != null) {
                nextId = lastUser.getInteger("user_id") + 1;
            }

            Document newUser = new Document()
                    .append("user_id", nextId)
                    .append("name", user.getName().trim())
                    .append("email", user.getEmail().trim())
                    .append("password", user.getPassword().trim())
                    .append("mobile", user.getMobile())
                    .append("created_at", java.time.LocalDateTime.now().toString());
            userCollection.insertOne(newUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User validateUserForResetPassword(String email, Long mobile) {
        try {
            Document userDoc;

            if (mobile != null) {
                userDoc = userCollection.find(
                        and(eq("email", email), eq("mobile", mobile))
                ).first();
            } else {
                userDoc = userCollection.find(eq("email", email)).first();
            }

            if (userDoc != null) {

                Object mobileObj = userDoc.get("mobile");
                Long userMobile = null;

                if (mobileObj != null) {
                    if (mobileObj instanceof Number) {
                        userMobile = ((Number) mobileObj).longValue();
                    } else {
                        userMobile = Long.parseLong(mobileObj.toString());
                    }
                }

                return new User(
                        userDoc.getInteger("user_id"),
                        userDoc.getString("name"),
                        userDoc.getString("email"),
                        null,
                        userMobile,
                        null
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean validateUserByIdAndPassword(int userId, String password) {
        try {
            Document user = userCollection.find(
                    and(
                            eq("user_id", userId),
                            eq("password", password.trim())
                    )
            ).first();

            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        try {
            Document result = userCollection.findOneAndUpdate(
                    eq("user_id",userId),
                    new Document("$set", new Document("password", newPassword.trim()))
            );
            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User updateProfile(int userId,String name,String mobile) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PROFILE)) {

            stmt.setString(1, name);

            if (mobile != null && !mobile.trim().isEmpty())
                stmt.setLong(2, Long.parseLong(mobile));
            else
                stmt.setNull(2, Types.NUMERIC);

            stmt.setInt(3, userId);

            int updated = stmt.executeUpdate();

            if (updated == 1) {
                return getUserById(userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
