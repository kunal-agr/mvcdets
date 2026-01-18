package com.kagrawal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int expenseId;
    private int userId;
    private LocalDate expenseDate;
    private BigDecimal amount;
    private String category;
    private String description;

    public Expense() {
    }

    public Expense(int expenseId, int userId, LocalDate expenseDate,
                   BigDecimal amount, String category, String description) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }


    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
