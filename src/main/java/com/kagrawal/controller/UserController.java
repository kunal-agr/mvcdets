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

@WebServlet("/api/user/*")
public class UserController extends HttpServlet {

    private UserDAO userDAO;
    private ObjectMapper mapper;

    @Override
    public void init() {
        userDAO = new UserDAOImpl();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (path) {
            case "/profile":
                getProfile(req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
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
            case "/profile":
                updateProfile(req, resp);
                break;
            case "/change-password":
                changePassword(req, resp);
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession(false);
        resp.setContentType("application/json");

        Map<String, Object> json = new HashMap<>();

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.put("error", "Unauthorized");
        } else {
            User sessionUser = (User) session.getAttribute("user");

            // 🔥 fetch again from DB to get created_at
            User dbUser = userDAO.getUserById(sessionUser.getUserId());

            json.put("user", dbUser);
        }

        resp.getWriter().write(mapper.writeValueAsString(json));
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        resp.setContentType("application/json");
        Map<String, Object> json = new HashMap<>();

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.put("error", "Unauthorized");
        } else {
            User sessionUser = (User) session.getAttribute("user");

            Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
            String name = body.get("fullname");
            String mobile = body.get("contactnumber");

            User updatedUser = userDAO.updateProfile(sessionUser.getUserId(), name, mobile);

            if (updatedUser != null) {
                session.setAttribute("user", updatedUser); // update session
                resp.setStatus(HttpServletResponse.SC_OK);
                json.put("message", "Profile updated successfully");
                json.put("user", updatedUser);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json.put("error", "Profile update failed");
            }
        }

        resp.getWriter().write(mapper.writeValueAsString(json));
    }

    private void changePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        resp.setContentType("application/json");
        Map<String, Object> json = new HashMap<>();

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.put("error", "Unauthorized");
        } else {
            User user = (User) session.getAttribute("user");
            Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);

            String currentPassword = body.get("currentpassword");
            String newPassword = body.get("newpassword");

            boolean valid = userDAO.validateUserByIdAndPassword(user.getUserId(), currentPassword);

            if (!valid) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.put("error", "Current password is incorrect.");
            } else {
                boolean updated = userDAO.updatePassword(user.getUserId(), newPassword);
                if (updated) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    json.put("message", "Password successfully changed.");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    json.put("error", "Failed to update password.");
                }
            }
        }

        resp.getWriter().write(mapper.writeValueAsString(json));
    }
}