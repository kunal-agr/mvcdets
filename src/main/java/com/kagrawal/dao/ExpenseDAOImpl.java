package com.kagrawal.dao;

import com.kagrawal.model.Expense;
import com.kagrawal.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAOImpl implements ExpenseDAO {
    private static final String INSERT_EXPENSE = "INSERT INTO tblexpense (user_id, expense_date, amount, category, description) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_USER = "SELECT * FROM tblexpense WHERE user_id = ? ORDER BY expense_date DESC";
    private static final String DELETE_EXPENSE = "DELETE FROM tblexpense WHERE expense_id = ?";
    private static final String DAY_WISE =
            "SELECT COALESCE(SUM(amount), 0) " +
                    "FROM tblexpense " +
                    "WHERE user_id = ? AND expense_date BETWEEN ? AND ?";
    private static final String MONTH_WISE =
            "SELECT COALESCE(SUM(amount), 0) " +
                    "FROM tblexpense " +
                    "WHERE user_id = ? AND expense_date BETWEEN ? AND ?";

    @Override
    public boolean addExpense(Expense e) {
        boolean status = false;

        try(Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_EXPENSE)) {

            stmt.setInt(1, e.getUserId());
            stmt.setDate(2, java.sql.Date.valueOf(e.getExpenseDate()));
            stmt.setBigDecimal(3, e.getAmount());
            stmt.setString(4, e.getCategory());
            stmt.setString(5, e.getDescription());

            status = stmt.executeUpdate() > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public List<Expense> getExpensesByUser(int userId) {
        List<Expense> expensesList = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_BY_USER)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Expense expense = new Expense();

                    expense.setExpenseId(rs.getInt("expense_id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setCategory(rs.getString("category"));
                    expense.setDescription(rs.getString("description"));

                    expensesList.add(expense);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return expensesList;
    }

    @Override
    public void deleteExpense(int expenseId) {
        try(Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_EXPENSE)) {
            stmt.setInt(1, expenseId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal getDayWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(DAY_WISE)) {

            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(fdate));
            stmt.setDate(3, Date.valueOf(tdate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    @Override
    public BigDecimal getMonthWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(MONTH_WISE)) {

            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(fdate));
            stmt.setDate(3, Date.valueOf(tdate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getBigDecimal(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}
