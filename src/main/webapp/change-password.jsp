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
            response.sendRedirect("logout.jsp");
            return;
        }

        String msg = "";
        if (request.getParameter("changed") != null) {
            if ("0".equals(request.getParameter("changed")))
                msg = "Your password has not been changed.";
            else
                msg = "Your password has been successfully changed.";
        }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Change Password</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/styles.css" rel="stylesheet">
        <link href="css/font-awesome.min.css" rel="stylesheet">

        <script type="text/javascript">
            function checkpass() {
                if(document.changepassword.newpassword.value != document.changepassword.confirmpassword.value) {
                    alert('New Password and Confirm Password do not match');
                    document.changepassword.confirmpassword.focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>

    <body>
        <nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar-collapse"><span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span></button>
                    <a class="navbar-brand" href="dashboard.jsp"><span>Daily Expense Tracker</span></a>
                </div>

            </div>
        </nav>

        <div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
            <div class="profile-sidebar">
                <div class="profile-userpic">
                </div>
                <div class="profile-usertitle">
                    <div class="profile-usertitle-name"><%= username %></div>
                    <div class="profile-usertitle-status"><span class="indicator label-success"></span>Online</div>
                </div>
                <div class="clear"></div>
            </div>
            <div class="divider"></div>

            <ul class="nav menu">
                <li class="active"><a href="dashboard.jsp"><em class="fa fa-dashboard">&nbsp;</em> Dashboard</a></li>
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
                <li><a href="logout.jsp"><em class="fa fa-power-off">&nbsp;</em> Logout</a></li>
            </ul>
        </div>


        <div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">Change Password</div>
                <div class="panel-body">
                    <p style="font-size:16px; color:red;" align="center"><%= msg %></p>
                    <form role="form" method="post" action="user?action=changePass" name="changepassword" onsubmit="return checkpass();">
                        <div class="form-group">
                            <label>Current Password</label>
                            <input type="password" name="currentpassword" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>New Password</label>
                            <input type="password" name="newpassword" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Confirm Password</label>
                            <input type="password" name="confirmpassword" class="form-control" required>
                        </div>
                        <div class="form-group has-success">
                            <button type="submit" class="btn btn-primary" name="submit">Change</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-sm-12">
            <p class="back-link">Daily Expense Tracker</p>
        </div>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    </body>
</html>

