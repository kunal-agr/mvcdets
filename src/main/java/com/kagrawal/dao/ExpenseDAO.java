package com.kagrawal.dao;

import com.kagrawal.model.Expense;

import java.util.List;

public interface ExpenseDAO {
    boolean addExpense(Expense expense);
    List<Expense> getExpensesByUser(int userId);
}
