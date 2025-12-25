<%@ page import="java.sql.*, javax.servlet.*, javax.servlet.http.*" %>

<%
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("logout.jsp");
    }

    String username = "User";
    if (request.getSession(false).getAttribute("userName") != null) {
        username = session.getAttribute("userName").toString();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Daily Expense Tracker || Datewise Expense Report</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/datepicker3.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">
</head>

<body>

<nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button"
                    class="navbar-toggle collapsed"
                    data-toggle="collapse"
                    data-target="#sidebar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <a class="navbar-brand" href="dashboard.jsp">
                <span>Daily Expense Tracker</span>
            </a>
        </div>
    </div>
</nav>

<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
    <div class="profile-sidebar">
        <div class="profile-userpic">
            <img src="http://placehold.it/50/30a5ff/fff"
                 class="img-responsive"
                 alt="">
        </div>

        <div class="profile-usertitle">
            <div class="profile-usertitle-name">
                <%= username %>
            </div>
            <div class="profile-usertitle-status">
                <span class="indicator label-success"></span>Online
            </div>
        </div>

        <div class="clear"></div>
    </div>

    <div class="divider"></div>

    <ul class="nav menu">
        <li class="active">
            <a href="dashboard.jsp">
                <em class="fa fa-dashboard">&nbsp;</em> Dashboard
            </a>
        </li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-1">
                <em class="fa fa-navicon">&nbsp;</em>Expenses
                <span class="icon pull-right">
                    <em class="fa fa-plus"></em>
                </span>
            </a>

            <ul class="children collapse" id="sub-item-1">
                <li>
                    <a href="add-expense.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span>
                        Add Expenses
                    </a>
                </li>
                <li>
                    <a href="manage-expense.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span>
                        Manage Expenses
                    </a>
                </li>
            </ul>
        </li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-2">
                <em class="fa fa-navicon">&nbsp;</em>Expense Report
                <span class="icon pull-right">
                    <em class="fa fa-plus"></em>
                </span>
            </a>

            <ul class="children collapse" id="sub-item-2">
                <li>
                    <a href="expense-datewise-result.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span>
                        Daywise Expenses
                    </a>
                </li>
                <li>
                    <a href="expense-monthwise-reports.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span>
                        Monthwise Expenses
                    </a>
                </li>
                <li>
                    <a href="expense-yearwise-reports.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span>
                        Yearwise Expenses
                    </a>
                </li>
            </ul>
        </li>

        <li>
            <a href="user-profile.jsp">
                <em class="fa fa-user">&nbsp;</em> Profile
            </a>
        </li>

        <li>
            <a href="change-password.jsp">
                <em class="fa fa-clone">&nbsp;</em> Change Password
            </a>
        </li>

        <li>
            <a href="logout.jsp">
                <em class="fa fa-power-off">&nbsp;</em> Logout
            </a>
        </li>
    </ul>
</div>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="row">
        <ol class="breadcrumb">
            <li>
                <a href="#">
                    <em class="fa fa-home"></em>
                </a>
            </li>
            <li class="active">Datewise Expense Report</li>
        </ol>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Datewise Expense Report
                </div>

                <div class="panel-body">
                    <div class="col-md-12">
                        <form role="form"
                              method="post"
                              action="expense?action=dateexpense"
                              name="bwdatesreport">

                            <div class="form-group">
                                <label>From Date</label>
                                <input type="date"
                                       class="form-control"
                                       id="fromdate"
                                       name="fromdate"
                                       required>
                            </div>

                            <div class="form-group">
                                <label>To Date</label>
                                <input type="date"
                                       class="form-control"
                                       id="todate"
                                       name="todate"
                                       required>
                            </div>

                            <div class="form-group has-success">
                                <button type="submit"
                                        class="btn btn-primary"
                                        name="submit">
                                    Submit
                                </button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-sm-12">
            <p class="back-link">Daily Expense Tracker</p>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/chart.min.js"></script>
<script src="js/chart-data.js"></script>
<script src="js/easypiechart.js"></script>
<script src="js/easypiechart-data.js"></script>
<script src="js/bootstrap-datepicker.js"></script>
<script src="js/custom.js"></script>

</body>
</html>
