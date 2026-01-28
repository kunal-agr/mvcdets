package com.kagrawal.dao;

import com.google.cloud.firestore.*;
import com.kagrawal.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
            int expenseId = (int) (System.currentTimeMillis() / 1000);
            e.setExpenseId(expenseId);

            db.collection("expenses")
                    .document(String.valueOf(expenseId))
                    .set(toFirestoreObject(e))
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
                    .whereEqualTo("userId", userId)   // NUMBER match
                    .get()
                    .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                list.add(fromFirestore(doc));
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

    /* =========================
       TOTAL CALCULATION HELPERS
       ========================= */

    private BigDecimal getExpenseTotalByUserAndDate(int userId, LocalDate from, LocalDate to) {
        BigDecimal total = BigDecimal.ZERO;

        try {
            QuerySnapshot snapshot = db.collection("expenses")
                    .whereEqualTo("userId", userId)
                    .get()
                    .get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                Expense e = fromFirestore(doc);

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

    /* =========================
       INTERFACE METHODS
       ========================= */

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
        return getExpenseTotalByUserAndDate(userId, now.withDayOfMonth(1), now);
    }

    @Override
    public BigDecimal yearExpense(int userId) {
        LocalDate now = LocalDate.now();
        return getExpenseTotalByUserAndDate(userId, now.withDayOfYear(1), now);
    }

    @Override
    public BigDecimal totalExpense(int userId) {
        return getExpenseTotalByUserAndDate(
                userId,
                LocalDate.of(1970, 1, 1),
                LocalDate.now()
        );
    }

    /* =========================
       FIRESTORE MAPPERS
       ========================= */

    private Expense fromFirestore(DocumentSnapshot doc) {
        Expense e = new Expense();

        e.setExpenseId(doc.getLong("expenseId").intValue());
        e.setUserId(doc.getLong("userId").intValue());

        String isoDate = doc.getString("expenseDate");
        e.setExpenseDate(OffsetDateTime.parse(isoDate).toLocalDate());

        e.setAmount(new BigDecimal(doc.get("amount").toString()));
        e.setCategory(doc.getString("category"));
        e.setDescription(doc.getString("description"));

        return e;
    }

    private Object toFirestoreObject(Expense e) {
        return new Object() {
            public final int expenseId = e.getExpenseId();
            public final int userId = e.getUserId();
            public final String expenseDate = e.getExpenseDate().toString() + "T00:00:00.000Z";
            public final BigDecimal amount = e.getAmount();
            public final String category = e.getCategory();
            public final String description = e.getDescription();
            public final String createdAt = OffsetDateTime.now().toString();
        };
    }
}
