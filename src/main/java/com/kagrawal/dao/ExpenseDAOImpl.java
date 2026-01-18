package com.kagrawal.dao;

import com.kagrawal.model.Expense;
import com.mongodb.client.*;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class ExpenseDAOImpl implements ExpenseDAO {

    private MongoCollection<Document> expenseCollection;

    public ExpenseDAOImpl() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("mvcdetsdb");
        expenseCollection = db.getCollection("expenses");
    }

    /* ---------------- ADD EXPENSE ---------------- */

    @Override
    public boolean addExpense(Expense e) {
        try {
            Document lastExpense = expenseCollection.find()
                    .sort(new Document("expense_id", -1))
                    .first();

            int nextId = 1;
            if (lastExpense != null) {
                nextId = lastExpense.getInteger("expense_id") + 1;
            }

            Document doc = new Document()
                    .append("expense_id", nextId)
                    .append("user_id", e.getUserId())
                    .append("amount", e.getAmount())
                    .append("category", e.getCategory())
                    .append("description", e.getDescription())
                    .append("createdAt", new Date())
                    .append("expenseDate",
                            Date.from(
                                    e.getExpenseDate()
                                            .atStartOfDay(ZoneId.systemDefault())
                                            .toInstant()
                            )
                    );

            expenseCollection.insertOne(doc);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* ---------------- LIST EXPENSES ---------------- */

    @Override
    public List<Expense> getExpensesByUser(int userId) {
        List<Expense> list = new ArrayList<>();

        try {
            List<Document> docs = expenseCollection.find(eq("user_id", userId))
                    .sort(new Document("expenseDate", -1))
                    .into(new ArrayList<>());

            for (Document d : docs) {
                Expense e = new Expense();
                e.setExpenseId(d.getInteger("expense_id"));
                e.setUserId(d.getInteger("user_id"));
                e.setAmount(BigDecimal.valueOf(((Number) d.get("amount")).doubleValue()));
                e.setCategory(d.getString("category"));
                e.setDescription(d.getString("description"));

                Date date = d.getDate("expenseDate");
                e.setExpenseDate(
                        date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                );

                list.add(e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    /* ---------------- DELETE EXPENSE ---------------- */

    @Override
    public void deleteExpense(int expenseId) {
        expenseCollection.deleteOne(eq("expense_id", expenseId));
    }

    private BigDecimal getExpenseBetweenDates(int userId, LocalDate from, LocalDate to) {
        BigDecimal total = BigDecimal.ZERO;

        try {
            Date start = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Document result = expenseCollection.aggregate(
                    Arrays.asList(
                            new Document("$match",
                                    and(
                                            eq("user_id", userId),
                                            gte("expenseDate", start),
                                            lt("expenseDate", end)
                                    )
                            ),
                            new Document("$group",
                                    new Document("_id", null)
                                            .append("total", new Document("$sum", "$amount")))
                    )
            ).first();

            if (result != null && result.get("total") != null) {
                total = BigDecimal.valueOf(((Number) result.get("total")).doubleValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }


    @Override
    public BigDecimal getDayWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        return getExpenseBetweenDates(userId, fdate, tdate);
    }

    @Override
    public BigDecimal getMonthWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        return getExpenseBetweenDates(userId, fdate, tdate);
    }

    @Override
    public BigDecimal getYearWiseExpenseTotal(int userId, LocalDate fdate, LocalDate tdate) {
        return getExpenseBetweenDates(userId, fdate, tdate);
    }

    /* ---------------- TODAY EXPENSE ---------------- */

    @Override
    public BigDecimal todaysExpense(int userId) {
        return dateRangeSum(userId, LocalDate.now(), LocalDate.now().plusDays(1));
    }

    /* ---------------- YESTERDAY EXPENSE ---------------- */

    @Override
    public BigDecimal yesterdayExpense(int userId) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return dateRangeSum(userId, yesterday, yesterday.plusDays(1));
    }

    /* ---------------- WEEK EXPENSE ---------------- */

    @Override
    public BigDecimal weekExpense(int userId) {
        LocalDate today = LocalDate.now();
        return dateRangeSum(userId, today.minusDays(7), today);
    }

    /* ---------------- MONTH EXPENSE ---------------- */

    @Override
    public BigDecimal monthExpense(int userId) {
        LocalDate now = LocalDate.now();
        return dateRangeSum(
                userId,
                now.withDayOfMonth(1),
                now.plusMonths(1).withDayOfMonth(1)
        );
    }

    /* ---------------- YEAR EXPENSE ---------------- */

    @Override
    public BigDecimal yearExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try {
            int year = LocalDate.now().getYear();

            List<Document> pipeline = Arrays.asList(
                    new Document("$match", new Document("user_id", userId)),
                    new Document("$addFields",
                            new Document("year",
                                    new Document("$year", "$expenseDate"))),
                    new Document("$match", new Document("year", year)),
                    new Document("$group",
                            new Document("_id", null)
                                    .append("total", new Document("$sum", "$amount")))
            );

            Document result = expenseCollection.aggregate(pipeline).first();

            if (result != null) {
                total = BigDecimal.valueOf(((Number) result.get("total")).doubleValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    /* ---------------- TOTAL EXPENSE ---------------- */

    @Override
    public BigDecimal totalExpense(int userId) {
        BigDecimal total = BigDecimal.ZERO;

        try {
            Document result = expenseCollection.aggregate(
                    Arrays.asList(
                            new Document("$match", new Document("user_id", userId)),
                            new Document("$group",
                                    new Document("_id", null)
                                            .append("total", new Document("$sum", "$amount")))
                    )
            ).first();

            if (result != null) {
                total = BigDecimal.valueOf(((Number) result.get("total")).doubleValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    /* ---------------- SHARED DATE RANGE METHOD ---------------- */

    private BigDecimal dateRangeSum(int userId, LocalDate from, LocalDate to) {
        BigDecimal total = BigDecimal.ZERO;

        try {
            Date start = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());

            List<Document> pipeline = Arrays.asList(
                    new Document("$match",
                            and(
                                    eq("user_id", userId),
                                    gte("expenseDate", start),
                                    lt("expenseDate", end)
                            )
                    ),
                    new Document("$group",
                            new Document("_id", null)
                                    .append("total", new Document("$sum", "$amount")))
            );

            Document result = expenseCollection.aggregate(pipeline).first();

            if (result != null) {
                total = BigDecimal.valueOf(((Number) result.get("total")).doubleValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}
