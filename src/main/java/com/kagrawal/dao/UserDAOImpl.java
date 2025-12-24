package com.kagrawal.dao;

import com.kagrawal.model.User;
import com.kagrawal.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class UserDAOImpl implements UserDAO {

    private static final String VALIDATE_USER =
            "SELECT user_id, name, email, password, mobile " +
                    "FROM tbluser WHERE email = ? AND password = ?";

    private static final String SELECT_BY_ID =
            "SELECT user_id, name, email, mobile " +
                    "FROM tbluser WHERE user_id = ?";

    private static final String INSERT_USER =
            "INSERT INTO tbluser (name, email, mobile, password) VALUES (?, ?, ?, ?)";

    private static final String VALIDATE_FOR_RESET =
            "SELECT user_id, name, email, mobile FROM tbluser WHERE email = ? AND mobile = ?";

    private static final String UPDATE_PASS =
            "UPDATE tbluser SET password = ? WHERE user_id = ?";

    @Override
    public User validateUser(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VALIDATE_USER)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Long mobile = rs.getObject("mobile") != null
                            ? rs.getLong("mobile")
                            : null;

                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            mobile,
                            null
                    );
                }
            }
        } catch (SQLException e) {
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

                    Long mobile = rs.getObject("mobile") != null
                            ? rs.getLong("mobile")
                            : null;

                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null,
                            mobile,
                            null
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
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());

            if (user.getMobile() != null)
                stmt.setLong(3, user.getMobile());
            else
                stmt.setNull(3, Types.NUMERIC);

            stmt.setString(4, user.getPassword());

            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User validateUserForResetPassword(String email, Long mobile) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VALIDATE_FOR_RESET)) {

            stmt.setString(1, email);

            if (mobile != null)
                stmt.setLong(2, mobile);
            else
                stmt.setNull(2, Types.NUMERIC);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null,
                            rs.getLong("mobile"),
                            null
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        boolean status = false;
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PASS)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2,userId);

            status = stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

}
