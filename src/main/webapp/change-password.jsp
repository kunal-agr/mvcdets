<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    int userId = ((com.kagrawal.model.User) session.getAttribute("user")).getUserId();
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
</head>

<body>
<nav class="navbar navbar-custom navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="dashboard.jsp"><span>Daily Expense Tracker</span></a>
        </div>
    </div>
</nav>

<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
    <ul class="nav menu">
        <li class="active"><a href="dashboard.jsp"><em class="fa fa-dashboard">&nbsp;</em> Dashboard</a></li>
        <li><a href="change-password.jsp"><em class="fa fa-clone">&nbsp;</em> Change Password</a></li>
        <li>
            <a href="#" onclick="logout()"><em class="fa fa-power-off">&nbsp;</em> Logout</a>
        </li>
    </ul>
</div>

<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="panel panel-default">
        <div class="panel-heading">Change Password</div>
        <div class="panel-body">
            <form name="changepassword">
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
                <button type="submit" class="btn btn-primary">Change</button>
            </form>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
    function checkpass() {
        const form = document.changepassword;
        if (form.newpassword.value !== form.confirmpassword.value) {
            alert('New Password and Confirm Password do not match');
            form.confirmpassword.focus();
            return false;
        }
        return true;
    }

    async function submitForm(e) {
        e.preventDefault();
        if (!checkpass()) return;

        const form = document.changepassword;

        const res = await fetch('<%=request.getContextPath()%>/api/auth/change-password', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId: "<%=userId%>",
                currentPassword: form.currentpassword.value,
                newPassword: form.newpassword.value
            })
        });

        const data = await res.json();
        alert(res.ok ? data.message : data.error);
        if (res.ok) form.reset();
    }

    window.addEventListener('DOMContentLoaded', () => {
        document.changepassword.addEventListener('submit', submitForm);
    });

    async function logout() {
        try {
            await fetch('<%=request.getContextPath()%>/api/auth/logout', { method: 'POST' });
            window.location.href = 'index.jsp';
        } catch (e) {
            alert('Logout failed');
        }
    }
</script>
</body>
</html>
