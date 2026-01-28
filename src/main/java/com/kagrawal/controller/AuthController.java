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
                break;
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

    private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
        String email = body.get("email");
        String mobileParam = body.get("mobile");
        Long mobile = (mobileParam != null && !mobileParam.isEmpty()) ? Long.parseLong(mobileParam) : null;

        User user = userDAO.validateUserForResetPassword(email, mobile);

        if (user != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("userId", user.getUserId());
            json.put("message", "User verified. Proceed to reset password.");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(json));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Invalid email or mobile.\"}");
        }
    }
    // ================= RESET PASSWORD =================
    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        // Read JSON body
        Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
        String newPassword = body.get("password");
        int userId = Integer.parseInt(body.get("userId")); // receive from JS

        boolean updated = userDAO.updatePassword(userId, newPassword);

        if (updated) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Password updated successfully.\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Failed to update password.\"}");
        }
    }

    // ================= CHANGE PASSWORD =================
    private void changePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
        int userId = Integer.parseInt(body.get("userId"));  // get from JSON instead of session
        String oldPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        Map<String, Object> json = new HashMap<>();

        boolean valid = userDAO.validateUserByIdAndPassword(userId, oldPassword);

        if (valid && userDAO.updatePassword(userId, newPassword)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            json.put("message", "Password changed successfully.");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            json.put("error", "Invalid userId or current password.");
        }

        resp.getWriter().write(mapper.writeValueAsString(json));
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