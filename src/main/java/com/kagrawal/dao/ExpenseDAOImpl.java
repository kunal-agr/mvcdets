package com.kagrawal.dao;

import com.google.cloud.firestore.*;
import com.kagrawal.model.Expense;
import com.kagrawal.util.FirebaseUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExpenseDAOImpl implements ExpenseDAO {

    private final Firestore db;

    public ExpenseDAOImpl() {
        this.db = FirebaseUtil.getFirestore();
    }

    @Override
    public boolean addExpense(Expense e) {
        try {
            String docId = String.valueOf(System.currentTimeMillis() / 1000); // unique doc id
            e.setExpenseId(Integer.parseInt(docId)); // optional, for local reference

            // Firestore document map
            db.collection("expenses")
                    .document(docId)
                    .set(new ExpenseFirestore(e))
                    .get();

            return true;
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Expense> getExpensesByUser(int userId) {
        List<Expense> list = new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection("expenses")
                    .whereEqualTo("userId", String.valueOf(userId))
                    .get()
                    .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                list.add(doc.toObject(ExpenseFirestore.class).toExpense());
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteExpense(int expenseId) {
        try {
            db.collection("expenses")
                    .document(String.valueOf(expenseId))
                    .delete()
                    .get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    private BigDecimal getExpenseTotalByUserAndDate(int userId, LocalDate from, LocalDate to) {
        BigDecimal total = BigDecimal.ZERO;
        try {
            QuerySnapshot snapshot = db.collection("expenses")
                    .whereEqualTo("userId", String.valueOf(userId))
                    .get()
                    .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                Expense e = doc.toObject(ExpenseFirestore.class).toExpense();
                if ((e.getExpenseDate().isEqual(from) || e.getExpenseDate().isAfter(from)) &&
                        e.getExpenseDate().isBefore(to.plusDays(1))) {
                    total = total.add(e.getAmount());
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return total;
    }

    @Override
    public BigDecimal getDayWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        return getExpenseTotalByUserAndDate(userId, fdate, tdate);
    }

    @Override
    public BigDecimal getMonthWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        return getExpenseTotalByUserAndDate(userId, fdate, tdate);
    }

    @Override
    public BigDecimal getYearWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        return getExpenseTotalByUserAndDate(userId, fdate, tdate);
    }

    @Override
    public BigDecimal todaysExpense(int userId) {
        LocalDate today = LocalDate.now();
        return getExpenseTotalByUserAndDate(userId, today, today);
    }

    @Override
    public BigDecimal yesterdayExpense(int userId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return getExpenseTotalByUserAndDate(userId, yesterday, yesterday);
    }

    @Override
    public BigDecimal weekExpense(int userId) {
        LocalDate today = LocalDate.now();
        return getExpenseTotalByUserAndDate(userId, today.minusDays(6), today);
    }

    @Override
    public BigDecimal monthExpense(int userId) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        return getExpenseTotalByUserAndDate(userId, start, now);
    }

    @Override
    public BigDecimal yearExpense(int userId) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfYear(1);
        return getExpenseTotalByUserAndDate(userId, start, now);
    }

    @Override
    public BigDecimal totalExpense(int userId) {
        return getExpenseTotalByUserAndDate(userId, LocalDate.of(1970,1,1), LocalDate.now());
    }

    // Inner class to map Expense to Firestore with string userId
    private static class ExpenseFirestore {
        private String expenseId;
        private String userId;
        private LocalDate expenseDate;
        private BigDecimal amount;
        private String category;
        private String description;

        public ExpenseFirestore() {}

        public ExpenseFirestore(Expense e) {
            this.expenseId = String.valueOf(e.getExpenseId());
            this.userId = String.valueOf(e.getUserId());
            this.expenseDate = e.getExpenseDate();
            this.amount = e.getAmount();
            this.category = e.getCategory();
            this.description = e.getDescription();
        }

        public Expense toExpense() {
            Expense e = new Expense();
            e.setExpenseId(Integer.parseInt(expenseId));
            e.setUserId(Integer.parseInt(userId));
            e.setExpenseDate(expenseDate);
            e.setAmount(amount);
            e.setCategory(category);
            e.setDescription(description);
            return e;
        }
    }
}
