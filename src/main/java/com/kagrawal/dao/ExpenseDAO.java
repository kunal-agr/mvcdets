package com.kagrawal.dao;

import com.kagrawal.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseDAO {
    boolean addExpense(Expense expense);
    List<Expense> getExpensesByUser(int userId);
    void deleteExpense(int expenseId);
    BigDecimal getDayWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate);
    BigDecimal getMonthWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate);
    BigDecimal getYearWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate);
    BigDecimal todaysExpense(int userId);
    BigDecimal yesterdayExpense(int userId);
    BigDecimal weekExpense(int userId);
    BigDecimal monthExpense(int userId);
    BigDecimal yearExpense(int userId);
    BigDecimal totalExpense(int userId);
}
