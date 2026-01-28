package com.kagrawal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kagrawal.dao.ExpenseDAO;
import com.kagrawal.dao.ExpenseDAOImpl;
import com.kagrawal.model.Expense;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/expenses/*")
public class ExpenseController extends HttpServlet {

    private ExpenseDAO expenseDAO;
    private ObjectMapper mapper;

    @Override
    public void init() {
        expenseDAO = new ExpenseDAOImpl();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            List<Expense> expenses = expenseDAO.getExpensesByUser(userId);
            resp.getWriter().write(mapper.writeValueAsString(expenses));

        } else if (path.equals("/summary")) {
            Map<String, BigDecimal> summary = new HashMap<>();
            summary.put("today", expenseDAO.todaysExpense(userId));
            summary.put("yesterday", expenseDAO.yesterdayExpense(userId));
            summary.put("week", expenseDAO.weekExpense(userId));
            summary.put("month", expenseDAO.monthExpense(userId));
            summary.put("year", expenseDAO.yearExpense(userId));
            summary.put("total", expenseDAO.totalExpense(userId));
            resp.getWriter().write(mapper.writeValueAsString(summary));

        } else if (path.equals("/date")) {
            LocalDate from = LocalDate.parse(req.getParameter("fromdate"));
            LocalDate to = LocalDate.parse(req.getParameter("todate"));
            BigDecimal total = expenseDAO.getDayWiseExpenseTotal(userId, from, to);
            Map<String, Object> res = new HashMap<>();
            res.put("fromDate", from);
            res.put("toDate", to);
            res.put("total", total);
            resp.getWriter().write(mapper.writeValueAsString(res));

        } else if (path.equals("/month")) {
            LocalDate from = LocalDate.parse(req.getParameter("fromdate"));
            LocalDate to = LocalDate.parse(req.getParameter("todate"));
            BigDecimal total = expenseDAO.getMonthWiseExpenseTotal(userId, from, to);
            Map<String, Object> res = new HashMap<>();
            res.put("fromMonth", from);
            res.put("toMonth", to);
            res.put("total", total);
            resp.getWriter().write(mapper.writeValueAsString(res));

        } else if (path.equals("/year")) {
            LocalDate from = LocalDate.parse(req.getParameter("fromdate"));
            LocalDate to = LocalDate.parse(req.getParameter("todate"));

            BigDecimal total = expenseDAO.getYearWiseExpenseTotal(userId, from, to);

            Map<String, Object> res = new HashMap<>();
            res.put("fromYear", from);
            res.put("toYear", to);
            res.put("total", total);

            resp.getWriter().write(mapper.writeValueAsString(res));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            Expense expense = mapper.readValue(req.getInputStream(), Expense.class);
            expense.setUserId(userId);

            boolean status = expenseDAO.addExpense(expense);

            if (status) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"message\":\"Expense added\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Failed to add expense\"}");
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // <-- prints real error in server console
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        String path = req.getPathInfo(); // e.g., /123
        if (path != null && path.length() > 1) {
            int expenseId = Integer.parseInt(path.substring(1));
            expenseDAO.deleteExpense(expenseId);
            resp.getWriter().write("{\"message\":\"Expense deleted\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Expense ID required\"}");
        }
    }
}
