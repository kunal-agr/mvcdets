package com.kagrawal.controller;

import com.kagrawal.dao.UserDAOImpl;
import com.kagrawal.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if ("login".equals(action)) {
                loginUser(req, resp);
            } else if ("register".equals(action)) {
                registerUser(req, resp);
            } else if ("forgot".equals(action)) {
                resetPassword(req,resp);
            } else if("reset".equals(action)) {
                updatePassword(req,resp);
            } else if("changePass".equals(action)) {
                changePassword(req,resp);
            } else if("profile".equals(action)) {
              userProfile(req,resp);
            } else {
                resp.sendRedirect("index.jsp");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("rootCause", e.getCause());
            req.setAttribute("errorException", e);
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void loginUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userDAO.validateUser(email, password);

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getName());
            resp.sendRedirect("dashboard.jsp");
        } else {
            resp.sendRedirect("index.jsp?error=1");
        }
    }

    private void registerUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        String mobileParam = req.getParameter("mobile");
        Long mobile = null;

        if (mobileParam != null && !mobileParam.trim().isEmpty()) {
            mobile = Long.parseLong(mobileParam);
        }

        User user = new User(name, email, password, mobile);

        boolean result = userDAO.addUser(user);

        if (result)
            resp.sendRedirect("index.jsp?success=1");
        else
            resp.sendRedirect("index.jsp?success=0");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String email = req.getParameter("email");
        String mobileParam = req.getParameter("mobile");

        Long mobile = null;
        if (mobileParam != null && !mobileParam.trim().isEmpty()) {
            mobile = Long.parseLong(mobileParam);
        }

        User user = userDAO.validateUserForResetPassword(email, mobile);

        if (user != null) {
            req.getSession(true).setAttribute("resetUserId", user.getUserId());
            req.getSession().setAttribute("resetUserId", user.getUserId());
            resp.sendRedirect("reset-password.jsp");
        } else {
            resp.sendRedirect("index.jsp?error=2");
        }
    }

    private void updatePassword(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        Integer userId = (Integer) session.getAttribute("resetUserId");

        if (userId == null) {
            resp.sendRedirect("resetPassword.jsp");
            return;
        }

        String password = req.getParameter("newpassword");
        boolean result = userDAO.updatePassword(userId,password);

        if (result) {
            session.invalidate();
            resp.sendRedirect("index.jsp");
        } else {
            resp.sendRedirect("index.jsp?error=3");
        }
    }

    private void changePassword(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        int userId = (int) session.getAttribute("userId");

        String currentPassword = req.getParameter("currentpassword");
        String newPassword = req.getParameter("newpassword");

        boolean valid = userDAO.validateUserByIdAndPassword(userId, currentPassword);

        if (!valid) {
            resp.sendRedirect("change-password.jsp?changed=0");
            return;
        }

        boolean result = userDAO.updatePassword(userId, newPassword);

        if (result) {
            resp.sendRedirect("change-password.jsp?changed=1");
        } else {
            resp.sendRedirect("change-password.jsp?changed=0");
        }
    }

    private void userProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        int userId = (int) session.getAttribute("userId");

        User user = userDAO.getUserById(userId);

        req.setAttribute("user", user);
        req.getRequestDispatcher("user-profile.jsp").forward(req, resp);
    }
}
