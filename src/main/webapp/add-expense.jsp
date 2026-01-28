<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
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
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Daily Expense Tracker || Add Expense</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/datepicker3.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
</head>

<body>

<nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="dashboard.jsp">
                <span>Daily Expense Tracker</span>
            </a>
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
            <ul class="children collapse in" id="sub-item-1">
                <li class="active">
                    <a href="add-expense.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span> Add Expenses
                    </a>
                </li>
                <li>
                    <a href="manage-expense.jsp">
                        <span class="fa fa-arrow-right">&nbsp;</span> Manage Expenses
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
            <li class="active">Expense</li>
        </ol>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">Expense</div>
                <div class="panel-body">

                    <div id="msg"></div>

                    <form id="expenseForm">
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

                </div>
            </div>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<!-- 🔒 EXISTING EXPENSE REST LOGIC (UNCHANGED) -->
<script>
document.getElementById('expenseForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const expense = {
        expenseDate: formData.get('dateExpense'),
        description: formData.get('item'),
        amount: parseFloat(formData.get('amount')),
        category: formData.get('category')
    };

    try {
        const res = await fetch('<%=request.getContextPath()%>/api/expenses', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(expense)
        });

        const data = await res.json();

        if (res.ok) {
            msg.innerHTML = `<div class="alert alert-success">${data.message}</div>`;
            this.reset();
        } else {
            msg.innerHTML = `<div class="alert alert-danger">${data.error}</div>`;
        }
    } catch (err) {
        msg.innerHTML = `<div class="alert alert-danger">Server error</div>`;
    }
});
</script>

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
