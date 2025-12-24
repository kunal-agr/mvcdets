package com.kagrawal.dao;

import com.kagrawal.model.Expense;
import com.kagrawal.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAOImpl implements ExpenseDAO {
    private static final String INSERT_EXPENSE = "INSERT INTO tblexpense (user_id, expense_date, amount, category, description) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_USER = "SELECT * FROM tblexpense WHERE user_id = ? ORDER BY expense_date DESC";
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

}
