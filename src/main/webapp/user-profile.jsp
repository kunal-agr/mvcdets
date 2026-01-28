<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Daily Expense Tracker || User Profile</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/datepicker3.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#sidebar-collapse">
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

<!-- SIDEBAR -->
<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
    <div class="profile-sidebar">
        <div class="profile-userpic">
            <img src="http://placehold.it/50/30a5ff/fff" class="img-responsive">
        </div>
        <div class="profile-usertitle">
            <div class="profile-usertitle-name" id="sidebarUsername">User</div>
            <div class="profile-usertitle-status">
                <span class="indicator label-success"></span>Online
            </div>
        </div>
        <div class="clear"></div>
    </div>

    <div class="divider"></div>

    <ul class="nav menu">
        <li>
            <a href="dashboard.jsp">
                <em class="fa fa-dashboard">&nbsp;</em> Dashboard
            </a>
        </li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-1">
                <em class="fa fa-navicon">&nbsp;</em>Expenses
                <span class="icon pull-right"><em class="fa fa-plus"></em></span>
            </a>
            <ul class="children collapse" id="sub-item-1">
                <li><a href="add-expense.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Add Expenses</a></li>
                <li><a href="manage-expense.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Manage Expenses</a></li>
            </ul>
        </li>

        <li class="parent">
            <a data-toggle="collapse" href="#sub-item-2">
                <em class="fa fa-navicon">&nbsp;</em>Expense Report
                <span class="icon pull-right"><em class="fa fa-plus"></em></span>
            </a>
            <ul class="children collapse" id="sub-item-2">
                <li><a href="expense-datewise-reports.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Daywise Expenses</a></li>
                <li><a href="expense-monthwise-reports.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Monthwise Expenses</a></li>
                <li><a href="expense-yearwise-reports.jsp"><span class="fa fa-arrow-right">&nbsp;</span> Yearwise Expenses</a></li>
            </ul>
        </li>

        <li class="active">
            <a href="user-profile.jsp">
                <em class="fa fa-user">&nbsp;</em> Profile
            </a>
        </li>

        <li><a href="change-password.jsp"><em class="fa fa-clone">&nbsp;</em> Change Password</a></li>
        <li>
            <a href="#" onclick="logout()">
                <em class="fa fa-power-off">&nbsp;</em> Logout
            </a>
        </li>
    </ul>
</div>

<!-- MAIN -->
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

    <div class="row">
        <ol class="breadcrumb">
            <li><a href="#"><em class="fa fa-home"></em></a></li>
            <li class="active">Profile</li>
        </ol>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">Profile</div>
        <div class="panel-body">

            <p id="msg" style="font-size:16px; color:red" align="center"></p>

            <!-- SAME MVC FORM UI -->
            <form id="profileForm">

                <div class="form-group">
                    <label>Full Name</label>
                    <input class="form-control" id="fullname" required>
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input class="form-control" id="email" readonly>
                </div>

                <div class="form-group">
                    <label>Mobile Number</label>
                    <input class="form-control" id="mobile" maxlength="10" required>
                </div>

                <div class="form-group">
                    <label>Registration Date</label>
                    <input class="form-control" id="createdAt" readonly>
                </div>

                <button type="submit" class="btn btn-primary">Update</button>
            </form>

        </div>
    </div>

    <%@ include file="includes/footer.jsp" %>
</div>

<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<!-- 🔥 REST JS – UNTOUCHED -->
<script>
    async function loadProfile() {
        const res = await fetch('api/user/profile');

        if (res.status === 401) {
            window.location.href = 'index.jsp';
            return;
        }

        const { user } = await res.json();

        fullname.value = user.name;
        email.value = user.email;
        mobile.value = user.mobile;
        sidebarUsername.innerText = user.name;

        let raw = user.createdAt;
        let date;

        if (!isNaN(raw)) {
            date = new Date(Number(raw));
        } else {
            date = new Date(raw.replace(" ", "T"));
        }

        createdAt.value = date.toLocaleDateString("en-IN", {
            day: "2-digit",
            month: "long",
            year: "numeric"
        });
    }

    profileForm.addEventListener('submit', async e => {
        e.preventDefault();

        const res = await fetch('api/user/profile', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                fullname: fullname.value,
                contactnumber: mobile.value
            })
        });

        const data = await res.json();
        msg.innerText = res.ok ? data.message : data.error;
    });

    loadProfile();
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
