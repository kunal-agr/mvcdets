<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("logout.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker - Login</title>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    </head>
    <body>

    <div class="container mt-5">
        <h3>Add Expense</h3>

        <%
            String msg = (String) request.getParameter("msg");
            if (msg != null) {
        %>
            <div class="alert alert-info"><%= msg %></div>
        <%
                session.removeAttribute("msg");
            }
        %>

        <form method="post" action="expense">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label>Date of Expense</label>
                <input class="form-control" type="date" name="dateExpense" required>
            </div>

            <div class="form-group">
                <label>Item</label>
                <input type="text" class="form-control" name="item" required>
            </div>

            <div class="form-group">
                <label>Cost of Item</label>
                <input class="form-control" type="number" step="0.01" name="amount" required>
            </div>

            <div class="form-group">
                <label>Category</label>
                <select name="category" class="form-control">
                    <option>Food</option>
                    <option>Transport</option>
                    <option>Shopping</option>
                    <option>Rent</option>
                    <option>Other</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Add</button>
        </form>
        <div class="mt-3">
            <a href="dashboard.jsp" class="btn btn-secondary">
                ← Back to Dashboard
            </a>
        </div>
    </div>

    </body>

    <script src="<c:url value='/js/jquery-1.11.1.min.js'/>"></script>
    <script src="<c:url value='/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/js/easypiechart.js'/>"></script>
    <script src="<c:url value='/js/easypiechart-data.js'/>"></script>
</html>
