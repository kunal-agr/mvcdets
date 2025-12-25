package com.kagrawal.controller;

import com.kagrawal.dao.ExpenseDAO;
import com.kagrawal.dao.ExpenseDAOImpl;
import com.kagrawal.dao.UserDAOImpl;
import com.kagrawal.model.Expense;
import com.kagrawal.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/expense")
public class ExpenseServlet extends HttpServlet{
    private final ExpenseDAOImpl expenseDAO = new ExpenseDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action){
            case "add":addExpense(req,resp);
                break;
            case "manage":manageExpense(req,resp);
                break;
            case "delete":deleteExpense(req,resp);
                break;
            case "dateexpense":dateWiseExpense(req,resp);
                break;
            case "monthexpense":monthWiseExpense(req,resp);
                break;
            case "yearexpense":yearWiseExpense(req,resp);
                break;
            case "allExpense":allExpenses(req,resp);
                break;
            default: getProfileUserId(req,resp);
        }
    }

    private void getProfileUserId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = (req.getSession(false).getAttribute("userId") != null)
                ? (int) req.getSession(false).getAttribute("userId")
                : -1;

        resp.sendRedirect("dashboard.jsp?user_id=" + userId);
    }

    private void addExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = Integer.parseInt(session.getAttribute("userId").toString());

        LocalDate expenseDate = LocalDate.parse(req.getParameter("dateExpense"));
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(req.getParameter("amount")));
        String category = req.getParameter("category");
        String description = req.getParameter("item");

        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setExpenseDate(expenseDate);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDescription(description);

        boolean status = expenseDAO.addExpense(expense);
        if (status) {
            resp.sendRedirect("add-expense.jsp?msg=Expense added");
        } else {
            resp.sendRedirect("add-expense.jsp?msg=Expense not added");
        }
    }

    private void manageExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        List<Expense> expenseList = expenseDAO.getExpensesByUser(userId);

        req.setAttribute("expenses", expenseList);

        RequestDispatcher rd = req.getRequestDispatcher("manage-expense.jsp");
        rd.forward(req, resp);
    }

    private void deleteExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int expenseId = Integer.parseInt(req.getParameter("expenseId"));
        expenseDAO.deleteExpense(expenseId);
        resp.sendRedirect("expense?action=manage");
    }

    private void dateWiseExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        LocalDate fromDate = LocalDate.parse(req.getParameter("fromdate"));
        LocalDate toDate = LocalDate.parse(req.getParameter("todate"));

        BigDecimal grandTotal = expenseDAO.getDayWiseExpenseTotal(userId, fromDate, toDate);
        req.setAttribute("grandTotal", grandTotal);
        req.setAttribute("fromDate", fromDate);
        req.setAttribute("toDate", toDate);

        RequestDispatcher rd = req.getRequestDispatcher("expense-datewise-result.jsp");
        rd.forward(req, resp);
    }

    private void monthWiseExpense(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        LocalDate fromDate = LocalDate.parse(req.getParameter("fromdate"));
        LocalDate toDate   = LocalDate.parse(req.getParameter("todate"));

        BigDecimal grandTotal =
                expenseDAO.getMonthWiseExpenseTotal(userId, fromDate, toDate);

        req.setAttribute("grandTotal", grandTotal);

        req.setAttribute("fromMonth", fromDate.toString());
        req.setAttribute("toMonth", toDate.toString());

        RequestDispatcher rd = req.getRequestDispatcher("expense-monthwise-result.jsp");
        rd.forward(req, resp);
    }

    private void yearWiseExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        LocalDate fromDate = LocalDate.parse(req.getParameter("fromYear"));
        LocalDate toDate   = LocalDate.parse(req.getParameter("toYear"));

        BigDecimal grandTotal = expenseDAO.getYearWiseExpenseTotal(userId, fromDate, toDate);

        req.setAttribute("grandTotal", grandTotal);

        req.setAttribute("fromYear", fromDate.toString());
        req.setAttribute("toYear", toDate.toString());

        RequestDispatcher rd = req.getRequestDispatcher("expense-yearwise-result.jsp");
        rd.forward(req, resp);
    }

    private void allExpenses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        BigDecimal totalTodayExpense = expenseDAO.todaysExpense(userId);
        BigDecimal yesterdayExpense = expenseDAO.yesterdayExpense(userId);
        BigDecimal weekExpense = expenseDAO.weekExpense(userId);
        BigDecimal monthExpense = expenseDAO.monthExpense(userId);
        BigDecimal yearExpense = expenseDAO.yearExpense(userId);
        BigDecimal totalExpense = expenseDAO.totalExpense(userId);

        req.setAttribute("totalTodayExpense", totalTodayExpense);
        req.setAttribute("yesterdayExpense", yesterdayExpense);
        req.setAttribute("weekExpense", weekExpense);
        req.setAttribute("monthExpense", monthExpense);
        req.setAttribute("yearExpense", yearExpense);
        req.setAttribute("totalExpense", totalExpense);

        req.setAttribute("allExpenses", true);

        RequestDispatcher rd = req.getRequestDispatcher("dashboard.jsp");
        rd.forward(req, resp);
    }
}
