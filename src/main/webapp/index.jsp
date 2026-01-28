<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Daily Expense Tracker - Login</title>

    <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>

<body>

<div class="row">
    <h2 class="text-center">Daily Expense Tracker</h2>
    <hr/>

    <div class="col-xs-10 col-xs-offset-1
                col-sm-8 col-sm-offset-2
                col-md-4 col-md-offset-4">

        <div class="login-panel panel panel-default">
            <div class="panel-heading">Log in</div>

            <div class="panel-body">

                <div id="error-msg" class="alert alert-danger text-center" style="display:none;"></div>

                <form onsubmit="event.preventDefault(); loginUser();">
                    <fieldset>

                        <div class="form-group">
                            <input class="form-control"
                                   placeholder="E-mail"
                                   name="email"
                                   type="email"
                                   required autofocus>
                        </div>

                        <div class="form-group">
                            <input class="form-control"
                                   placeholder="Password"
                                   name="password"
                                   type="password"
                                   required>
                        </div>

                        <a href="forgot-password.jsp">Forgot Password?</a>
                        <br><br>

                        <button type="submit"
                                class="btn btn-primary btn-block">
                            Login
                        </button>

                        <hr>

                        <a href="register.jsp"
                           class="btn btn-success btn-block">
                            Register
                        </a>

                    </fieldset>
                </form>

            </div>
        </div>
    </div>
</div>

<script src="<c:url value='/js/jquery-1.11.1.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>

<script>
function loginUser() {
    const email = document.querySelector('input[name="email"]').value;
    const password = document.querySelector('input[name="password"]').value;
    const errorDiv = document.getElementById('error-msg');

    errorDiv.style.display = 'none'; // hide previous errors

    fetch('<%=request.getContextPath()%>/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include', // send cookies for session
        body: JSON.stringify({ email, password })
    })
    .then(res => res.json().then(data => ({ status: res.status, body: data })))
    .then(obj => {
        if (obj.status === 200) {
            window.location.href = '<%=request.getContextPath()%>/dashboard.jsp';
        } else if (obj.status === 401) {
            errorDiv.style.display = 'block';
            errorDiv.innerText = obj.body.error || 'Invalid email or password';
        } else {
            errorDiv.style.display = 'block';
            errorDiv.innerText = 'Something went wrong. Try again.';
        }
    })
    .catch(err => {
        errorDiv.style.display = 'block';
        errorDiv.innerText = 'Network error. Try again.';
    });
}
</script>

</body>
</html>
