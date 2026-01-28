<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.math.BigDecimal" %>
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
        response.sendRedirect("index.jsp");
        return;
    }
    if (request.getAttribute("allExpenses") == null) {
        RequestDispatcher rd = request.getRequestDispatcher("expense?action=allExpense");
        rd.forward(request, response);
        return;
    }

    BigDecimal todaysExpense = (BigDecimal) request.getAttribute("totalTodayExpense");
    if (todaysExpense == null)
        todaysExpense = BigDecimal.ZERO;

    BigDecimal yesterdayExpense = (BigDecimal) request.getAttribute("yesterdayExpense");
    if (yesterdayExpense == null)
        yesterdayExpense = BigDecimal.ZERO;

    BigDecimal weekExpense = (BigDecimal) request.getAttribute("weekExpense");
    if (weekExpense == null)
        weekExpense = BigDecimal.ZERO;

    BigDecimal monthExpense = (BigDecimal) request.getAttribute("monthExpense");
    if (monthExpense == null)
        monthExpense = BigDecimal.ZERO;

    BigDecimal yearExpense = (BigDecimal) request.getAttribute("yearExpense");
    if (yearExpense == null)
        yearExpense = BigDecimal.ZERO;

    BigDecimal totalExpense = (BigDecimal) request.getAttribute("totalExpense");
    if (totalExpense == null)
        totalExpense = BigDecimal.ZERO;
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker - Dashboard</title>

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
                <a class="navbar-brand" href="dashboard.jsp">
                    <span>Daily Expense Tracker</span>
                </a>
            </div>
        </div>
    </nav>

    <div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
        <div class="profile-sidebar">
            <div class="profile-userpic">
            </div>
            <div class="profile-usertitle">
                <div class="profile-usertitle-name"><%= username %></div>
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
                    <span data-toggle="collapse" href="#sub-item-1" class="icon pull-right">
                        <em class="fa fa-plus"></em>
                    </span>
                </a>
                <ul class="children collapse" id="sub-item-1">
                    <li><a href="add-expense.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Add Expenses</a></li>
                    <li><a href="manage-expense.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Manage Expenses</a></li>
                </ul>
            </li>

            <li class="parent">
                <a data-toggle="collapse" href="#sub-item-2">
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
            <li>
                <a href="#" onclick="logout()">
                    <em class="fa fa-power-off">&nbsp;</em> Logout
                </a>
            </li>
        </ul>
    </div>

    <div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

        <div class="row">
            <ol class="breadcrumb">
                <li><a href="#"><em class="fa fa-home"></em></a></li>
                <li class="active">Dashboard</li>
            </ol>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Dashboard</h1>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Today's Expense</h4>
                        <div class="easypiechart" id="easypiechart-blue" data-percent="100">
                            <span class="percent"><%= todaysExpense %></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Yesterday's Expense</h4>
                        <div class="easypiechart" id="easypiechart-orange" data-percent="100">
                            <span class="percent"><%= yesterdayExpense %></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Last 7 Days Expense</h4>
                        <div class="easypiechart" id="easypiechart-teal" data-percent="100">
                            <span class="percent"><%= weekExpense %></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Last 30 Days Expense</h4>
                        <div class="easypiechart" id="easypiechart-red" data-percent="100">
                            <span class="percent"><%= monthExpense %></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Current Year Expenses</h4>
                        <div class="easypiechart" id="easypiechart-red" data-percent="100">
                            <span class="percent"><%= yearExpense %></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Total Expenses</h4>
                        <div class="easypiechart" id="easypiechart-red" data-percent="100">
                            <span class="percent"><%= totalExpense %></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="includes/footer.jsp" %>
    </div>

    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/easypiechart.js"></script>
    <script src="js/easypiechart-data.js"></script>
    <script src="js/custom.js"></script>
    <script>
        async function logout() {
            try {
                await fetch('<%=request.getContextPath()%>/api/auth/logout');
                window.location.href = 'index.jsp';
            } catch (e) {
                alert('Logout failed');
            }
        }
    </script>
    </body>
</html>