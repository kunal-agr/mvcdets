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
    private static final String YEAR_WISE =
            "SELECT COALESCE(SUM(amount), 0) " +
                    "FROM tblexpense " +
                    "WHERE user_id = ? AND expense_date BETWEEN ? AND ?";
    private static final String TODAY_EXPENSE = "SELECT COALESCE(SUM(amount), 0) AS todays_expense " +
            "FROM tblexpense " +
            "WHERE user_id = ? " +
            "AND expense_date = CURRENT_DATE";
    private static final String YESTERDAY_EXPENSE = "SELECT COALESCE(SUM(amount), 0) AS yesterdays_expense " +
            "FROM tblexpense " +
            "WHERE user_id = ? " +
            "AND expense_date = CURRENT_DATE - INTERVAL '1 day'";

    private static final String WEEK_EXPENSE =
            "SELECT COALESCE(SUM(amount), 0) AS last_7_days_expense " +
                    "FROM tblexpense " +
                    "WHERE user_id = ? " +
                    "AND expense_date >= CURRENT_DATE - INTERVAL '7 day' " +
                    "AND expense_date < CURRENT_DATE";

    private static final String MONTH_EXPENSE = "SELECT COALESCE(SUM(amount), 0) AS month_expense " +
            "FROM tblexpense " +
            "WHERE user_id = ? " +
            "AND expense_date >= date_trunc('month', CURRENT_DATE) " +
            "AND expense_date < date_trunc('month', CURRENT_DATE + INTERVAL '1 month')";

    private static final String YEAR_EXPENSE = "SELECT COALESCE(SUM(amount), 0) AS year_expense " +
            "FROM tblexpense " +
            "WHERE user_id = ? " +
            "AND expense_date >= date_trunc('year', CURRENT_DATE) " +
            "AND expense_date < date_trunc('year', CURRENT_DATE + INTERVAL '1 year')";

    private static final String TOTAL_EXPENSE =
            "SELECT COALESCE(SUM(amount), 0) AS lifetime_expense " +
                    "FROM tblexpense " +
                    "WHERE user_id = ?";

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

    @Override
    public BigDecimal getYearWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(YEAR_WISE)) {

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
    public BigDecimal todaysExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(TODAY_EXPENSE)) {

            stmt.setInt(1, userId);

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
    public BigDecimal yesterdayExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(YESTERDAY_EXPENSE)) {

            stmt.setInt(1, userId);

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
    public BigDecimal weekExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(WEEK_EXPENSE)) {

            stmt.setInt(1, userId);

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
    public BigDecimal monthExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(MONTH_EXPENSE)) {

            stmt.setInt(1, userId);

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
    public BigDecimal yearExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(YEAR_EXPENSE)) {

            stmt.setInt(1, userId);

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
    public BigDecimal totalExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(TOTAL_EXPENSE)) {

            stmt.setInt(1, userId);

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
