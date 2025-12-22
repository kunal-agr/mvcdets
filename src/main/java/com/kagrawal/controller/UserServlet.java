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
    String name = null;
    String email = null;
    String password = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try{
            switch (action) {
                case"login":loginUser(req, resp);
                    break;

                case"register":registerUser(req, resp);
                    break;
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("rootCause", e.getCause());
            req.setAttribute("errorException", e);
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    void loginUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = null;
        email = req.getParameter("email");
        password = req.getParameter("password");

        user = userDAO.validateUser(email, password);

        if (user != null) {
            req.getSession(true).setAttribute("user",user);
            resp.sendRedirect("dashboard.jsp");
        } else {
            resp.sendRedirect("index.jsp?error=1");
        }
    }

    void registerUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        name = req.getParameter("name");
        email = req.getParameter("email");
        password = req.getParameter("password");

        boolean result = userDAO.addUser(new User(name, email, password));
        if (result)
            resp.sendRedirect("index.jsp?success=1");
        else
            resp.sendRedirect("index.jsp?success=0");
    }
}
