package com.kagrawal.dao;

import com.kagrawal.model.User;
import com.kagrawal.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    private static final String VALIDATE_USER =
            "SELECT user_id, name, email, password " +
                    "FROM tbluser " +
                    "WHERE email = ? AND password = ?";

    private static final String SELECT_BY_ID =
            "SELECT user_id, name, email " +
                    "FROM tbluser " +
                    "WHERE user_id = ?";

    private static final String INSERT_USER =
            "INSERT INTO tbluser (name, email, password) " +
                    "VALUES (?, ?, ?)";

    @Override
    public User validateUser(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VALIDATE_USER)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // later -> throw DAOException
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
                            null
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // later -> throw DAOException
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // later -> throw DAOException
        }
        return false;
    }
}
