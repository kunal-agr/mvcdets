package com.kagrawal.dao;

import com.kagrawal.model.User;
import com.kagrawal.util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserDAOImpl implements UserDAO {

    private MongoCollection<Document> userCollection;

    public UserDAOImpl() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        if (db == null) {
            throw new RuntimeException("❌ MongoDB database is NULL");
        }
        this.userCollection = db.getCollection("users");
    }

    // Helper to read mobile number as Long
    private Long getMobile(Document doc) {
        Object mobileObj = doc.get("mobile");
        if (mobileObj == null)
            return null;
        if (mobileObj instanceof Integer)
            return ((Integer) mobileObj).longValue();
        if (mobileObj instanceof Long)
            return (Long) mobileObj;
        return null;
    }

    @Override
    public User validateUser(String email, String password) {
        try {
            Document userDoc = userCollection.find(
                    and(eq("email", email.trim()), eq("password", password))
            ).first();

            if (userDoc != null) {
                return new User(
                        userDoc.getInteger("user_id"),
                        userDoc.getString("name"),
                        userDoc.getString("email"),
                        userDoc.getString("password"),
                        getMobile(userDoc),
                        userDoc.getDate("createdAt")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(int userId) {
        try {
            Document doc = userCollection.find(eq("user_id", userId)).first();
            if (doc == null) return null;

            return new User(
                    doc.getInteger("user_id"),
                    doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"),
                    getMobile(doc),
                    doc.getDate("createdAt")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addUser(User user) {
        try {
            Document lastUser = userCollection.find()
                    .sort(new Document("user_id", -1))
                    .first();
            int nextId = 1;
            if (lastUser != null) nextId = lastUser.getInteger("user_id") + 1;

            Document newUser = new Document()
                    .append("user_id", nextId)
                    .append("name", user.getName().trim())
                    .append("email", user.getEmail().trim())
                    .append("password", user.getPassword().trim())
                    .append("mobile", user.getMobile())
                    .append("createdAt", new java.util.Date());

            userCollection.insertOne(newUser);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User validateUserForResetPassword(String email, Long mobile) {
        try {
            Document userDoc;

            if (mobile != null) {
                userDoc = userCollection.find(
                        and(eq("email", email.trim()), eq("mobile", mobile))
                ).first();
            } else {
                userDoc = userCollection.find(eq("email", email.trim())).first();
            }

            if (userDoc != null) {
                return new User(
                        userDoc.getInteger("user_id"),
                        userDoc.getString("name"),
                        userDoc.getString("email"),
                        null,
                        getMobile(userDoc),
                        userDoc.getDate("createdAt")
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
                    and(eq("user_id", userId), eq("password", password))
            ).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        try {
            Document result = userCollection.findOneAndUpdate(
                    eq("user_id", userId),
                    new Document("$set", new Document("password", newPassword))
            );
            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User updateProfile(int userId, String name, String mobile) {
        try {
            Document updateFields = new Document();
            updateFields.append("name", name.trim());

            if (mobile != null && !mobile.trim().isEmpty()) {
                updateFields.append("mobile", Long.parseLong(mobile));
            }

            Document result = userCollection.findOneAndUpdate(
                    eq("user_id", userId),
                    new Document("$set", updateFields)
            );

            if (result != null) return getUserById(userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
