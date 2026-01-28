package com.kagrawal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kagrawal.dao.UserDAO;
import com.kagrawal.dao.UserDAOImpl;
import com.kagrawal.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthController extends HttpServlet {

    private UserDAO userDAO;
    private ObjectMapper mapper;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/login":
                login(req, resp);
                break;
            case "/register":
                register(req, resp);
                break;
            case "/forgot":
                forgotPassword(req, resp);
                break;
            case "/reset":
                resetPassword(req, resp);
                break;
            case "/change-password":
                changePassword(req, resp);
                break;
            case "/logout":
                logout(req,resp);
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // LOGIN/REGISTER remain REST JSON
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
        String email = body.get("email");
        String password = body.get("password");

        User user = userDAO.validateUser(email, password);

        resp.setContentType("application/json");
        Map<String, Object> json = new HashMap<>();

        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.put("error", "Invalid email or password");
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userName", user.getName());
            session.setAttribute("userId", user.getUserId());  // <--- ADD THIS

            resp.setStatus(HttpServletResponse.SC_OK);
            json.put("message", "Login successful");
            json.put("userId", user.getUserId());
            json.put("name", user.getName());
        }

        resp.getWriter().write(mapper.writeValueAsString(json));
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        String mobile = body.get("mobile");

        resp.setContentType("application/json");
        Map<String, Object> json = new HashMap<>();

        User existing = userDAO.validateUser(email, password);
        if (existing != null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json.put("error", "Email already registered");
        } else {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setMobile(mobile != null ? Long.parseLong(mobile) : null);

            boolean created = userDAO.addUser(user);
            if (created) {
                resp.setStatus(HttpServletResponse.SC_OK);
                json.put("message", "Registration successful");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json.put("error", "Failed to register. Try again.");
            }
        }

        resp.getWriter().write(mapper.writeValueAsString(json));
    }

    // ================= FORGOT PASSWORD =================
    private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String mobileParam = req.getParameter("mobile");
        Long mobile = (mobileParam != null && !mobileParam.isEmpty()) ? Long.parseLong(mobileParam) : null;

        User user = userDAO.validateUserForResetPassword(email, mobile);

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("resetUserId", user.getUserId());
            resp.sendRedirect(req.getContextPath() + "/reset-password.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp?error=2");
        }
    }

    // ================= RESET PASSWORD =================
    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("resetUserId") == null) {
            resp.sendRedirect(req.getContextPath() + "/reset-password.jsp");
            return;
        }

        int userId = (int) session.getAttribute("resetUserId");
        String newPassword = req.getParameter("newpassword");

        boolean updated = userDAO.updatePassword(userId, newPassword);

        if (updated) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/reset-password.jsp?error=3");
        }
    }

    // ================= CHANGE PASSWORD =================
    private void changePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        int userId = ((User) session.getAttribute("user")).getUserId();
        String oldPassword = req.getParameter("currentpassword");
        String newPassword = req.getParameter("newpassword");

        boolean valid = userDAO.validateUserByIdAndPassword(userId, oldPassword);

        if (valid && userDAO.updatePassword(userId, newPassword)) {
            resp.sendRedirect(req.getContextPath() + "/change-password.jsp?changed=1");
        } else {
            resp.sendRedirect(req.getContextPath() + "/change-password.jsp?changed=0");
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("{\"message\":\"Logged out\"}");
    }
}
