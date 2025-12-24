<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Sidebar -->
<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">

    <!-- User Profile Section -->
    <div class="profile-sidebar">
        <div class="profile-userpic">
            <img src="http://placehold.it/50/30a5ff/fff" class="img-responsive" alt="">
        </div>
        <%
            String username = "User";

            if (request.getSession(false).getAttribute("userName") != null) {
                username = session.getAttribute("userName").toString();
            }
        %>

        <!-- Username (will be set from session later) -->
        <div class="profile-usertitle">
            <div class="profile-usertitle-name"><%=username%></div>
            <div class="profile-usertitle-status">
                <span class="indicator label-success"></span>Online
            </div>
        </div>
    </div>

    <div class="divider"></div>

    <!-- Sidebar Menu -->
    <ul class="nav menu">
        <li class="active"><a href="dashboard.jsp">Dashboard</a></li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-1">Expenses</a>
            <ul class="children collapse" id="sub-item-1">
                <li><a href="add-expense.jsp">Add Expense</a></li>
                <li><a href="manage-expense.jsp">Manage Expense</a></li>
            </ul>
        </li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-2">Expense Reports</a>
            <ul class="children collapse" id="sub-item-2">
                <li><a href="expense-datewise-reports.jsp">Daywise</a></li>
                <li><a href="expense-monthwise-reports.jsp">Monthwise</a></li>
                <li><a href="expense-yearwise-reports.jsp">Yearwise</a></li>
            </ul>
        </li>

        <li><a href="user-profile.jsp">Profile</a></li>
        <li><a href="change-password.jsp">Change Password</a></li>
        <li><a href="logout.jsp">Logout</a></li>
    </ul>
</div>
