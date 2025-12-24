<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.kagrawal.model.Expense" %>

<%
    // Session check
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 🔥 CALL SERVLET ONLY IF DATA NOT PRESENT
    if (request.getAttribute("expenses") == null) {
        RequestDispatcher rd = request.getRequestDispatcher("expense?action=manage");
        rd.forward(request, response);
        return; // VERY IMPORTANT
    }

    // Data already loaded by servlet
    List<Expense> list = (List<Expense>) request.getAttribute("expenses");
    String name = (String) session.getAttribute("userName");
    String msg = request.getParameter("msg");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Daily Expense Tracker || Manage Expense</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
</head>

<body>

<!-- ===== NAVBAR ===== -->
<nav class="navbar navbar-custom navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#sidebar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="dashboard.jsp">Daily Expense Tracker</a>
        </div>
    </div>
</nav>

<!-- ===== SIDEBAR ===== -->
<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">

    <div class="profile-sidebar">
        <div class="profile-userpic">
            <img src="http://placehold.it/50/30a5ff/fff" class="img-responsive">
        </div>
        <div class="profile-usertitle">
            <div class="profile-usertitle-name"><%= name %></div>
            <div class="profile-usertitle-status">
                <span class="indicator label-success"></span> Online
            </div>
        </div>
    </div>

    <ul class="nav menu">
        <li><a href="dashboard.jsp"><em class="fa fa-dashboard"></em> Dashboard</a></li>

        <li class="parent active">
            <a data-toggle="collapse" href="#sub-item-1">
                <em class="fa fa-navicon"></em> Expenses
                <span class="icon pull-right"><em class="fa fa-minus"></em></span>
            </a>
            <ul class="children collapse in" id="sub-item-1">
                <li><a href="add-expense.jsp">Add Expenses</a></li>
                <li class="active"><a href="manage-expense.jsp">Manage Expenses</a></li>
            </ul>
        </li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-2">
                <em class="fa fa-navicon"></em> Expense Report
                <span class="icon pull-right"><em class="fa fa-plus"></em></span>
            </a>
            <ul class="children collapse" id="sub-item-2">
                <li><a href="expense-datewise-reports.jsp">Daywise</a></li>
                <li><a href="expense-monthwise-reports.jsp">Monthwise</a></li>
                <li><a href="expense-yearwise-reports.jsp">Yearwise</a></li>
            </ul>
        </li>

        <li><a href="logout.jsp"><em class="fa fa-power-off"></em> Logout</a></li>
    </ul>
</div>

<!-- ===== MAIN CONTENT ===== -->
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

    <div class="panel panel-default" style="margin-top:60px;">
        <div class="panel-heading">Manage Expenses</div>

        <div class="panel-body">

            <p style="color:red;text-align:center;">
                <%= msg != null ? msg : "" %>
            </p>

            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Description</th>
                        <th>Amount</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                    </thead>

                    <tbody>
                    <%
                        if (list != null && !list.isEmpty()) {
                            int i = 1;
                            for (Expense e : list) {
                    %>
                        <tr>
                            <td><%= i++ %></td>
                            <td><%= e.getDescription() %></td>
                            <td>₹ <%= e.getAmount() %></td>
                            <td><%= e.getExpenseDate() %></td>
                            <td>
                                <a href="expense?action=delete&expenseId=<%= e.getExpenseId() %>"
                                   onclick="return confirm('Delete this expense?')">
                                    Delete
                                </a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="5" class="text-center">No expenses found</td>
                        </tr>
                    <%
                        }
                    %>
                    </tbody>

                </table>
            </div>
        </div>
    </div>
</div>

<!-- JS (LOAD ONCE) -->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/custom.js"></script>

</body>
</html>
