<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.kagrawal.model.Expense" %>
<%
    Integer userId = null;
    String username = "User";

    if (session != null) {
        if (session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");
        }
        if (session.getAttribute("userName") != null) {
            username = session.getAttribute("userName").toString();
        }
    }

    if (userId == null) {
        response.sendRedirect("logout.jsp");
        return;
    }

    if (request.getAttribute("expenses") == null) {
        RequestDispatcher rd = request.getRequestDispatcher("expense?action=manage");
        rd.forward(request, response);
        return;
    }

    List<Expense> list = (List<Expense>) request.getAttribute("expenses");
    String msg = request.getParameter("msg");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker || Manage Expenses</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link href="css/styles.css" rel="stylesheet">
    </head>

    <body>

    <nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="dashboard.jsp"><span>Daily Expense Tracker</span></a>
            </div>
        </div>
    </nav>

    <div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
        <div class="profile-sidebar">
            <div class="profile-userpic">
                <img src="http://placehold.it/50/30a5ff/fff" class="img-responsive" alt="">
            </div>
            <div class="profile-usertitle">
                <div class="profile-usertitle-name"><%= username %></div>
                <div class="profile-usertitle-status"><span class="indicator label-success"></span>Online</div>
            </div>
            <div class="clear"></div>
        </div>

        <div class="divider"></div>

        <ul class="nav menu">
            <li><a href="dashboard.jsp"><em class="fa fa-dashboard">&nbsp;</em> Dashboard</a></li>

            <li class="parent active">
                <a data-toggle="collapse" href="#sub-item-1" class="collapsed">
                    <em class="fa fa-navicon">&nbsp;</em>Expenses
                    <span data-toggle="collapse" href="#sub-item-1" class="icon pull-right">
                        <em class="fa fa-plus"></em>
                    </span>
                </a>
                <ul class="children collapse in" id="sub-item-1">
                    <li><a href="add-expense.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Add Expenses</a></li>
                    <li class="active"><a href="manage-expense.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Manage Expenses</a></li>
                </ul>
            </li>

            <li class="parent">
                <a data-toggle="collapse" href="#sub-item-2" class="collapsed">
                    <em class="fa fa-navicon">&nbsp;</em>Expense Report
                    <span data-toggle="collapse" href="#sub-item-2" class="icon pull-right">
                        <em class="fa fa-plus"></em>
                    </span>
                </a>
                <ul class="children collapse" id="sub-item-2">
                    <li><a href="expense-datewise-reports.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Daywise Expenses</a></li>
                    <li><a href="expense-monthwise-reports.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Monthwise Expenses</a></li>
                    <li><a href="expense-yearwise-reports.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Yearwise Expenses</a></li>
                </ul>
            </li>

            <li><a href="user-profile.jsp"><em class="fa fa-user">&nbsp;</em> Profile</a></li>
            <li><a href="change-password.jsp"><em class="fa fa-clone">&nbsp;</em> Change Password</a></li>
            <li><a href="logout.jsp"><em class="fa fa-power-off">&nbsp;</em> Logout</a></li>
        </ul>
    </div>

    <div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
        <div class="row">
            <ol class="breadcrumb">
                <li><a href="dashboard.jsp"><em class="fa fa-home"></em></a></li>
                <li class="active">Manage Expenses</li>
            </ol>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">Manage Expenses</div>
            <div class="panel-body">
                <% if (msg != null) { %>
                    <div class="alert alert-info text-center"><%= msg %></div>
                <% } %>

                <div class="table-responsive">
                    <table class="table table-bordered table-striped">
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
                                <td><%= e.getAmount() %></td>
                                <td><%= e.getExpenseDate() %></td>
                                <td>
                                    <a href="expense?action=delete&expenseId=<%= e.getExpenseId() %>"
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('Do you really want to delete this record?')">
                                        <em class="fa fa-trash"></em>
                                    </a>
                                </td>
                            </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="5" class="text-center">No expenses found.</td>
                            </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%@ include file="includes/footer.jsp" %>
    </div>

    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/custom.js"></script>

    </body>
</html>