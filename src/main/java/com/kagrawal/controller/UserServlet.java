package com.kagrawal.controller;

import com.kagrawal.dao.UserDAOImpl;
import com.kagrawal.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case"login":User user = null;
                        String email = req.getParameter("email");
                        String password = req.getParameter("password");

                        user = userDAO.validateUser(email, password);

                        if (user != null) {
                            req.getSession(true).setAttribute("user",user);
                            resp.sendRedirect("dashboard.jsp");
                        } else {
                            resp.sendRedirect("index.jsp?error=1");
                        }
                        break;
        }
    }
}
