<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker - Reset Password</title>

        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    </head>

    <body>
    <div class="row">
        <h2 align="center">Daily Expense Tracker</h2>
        <hr />

        <div class="col-xs-10 col-xs-offset-1
                    col-sm-8 col-sm-offset-2
                    col-md-4 col-md-offset-4">

            <div class="login-panel panel panel-default">
                <div class="panel-heading">Reset Password</div>

                <div class="panel-body">

                    <div id="errorMsg" class="alert alert-danger text-center" style="display:none;"></div>
                    <div id="successMsg" class="alert alert-success text-center" style="display:none;"></div>

                    <form name="changepassword" id="changepassword">

                        <fieldset>

                            <div class="form-group">
                                <input class="form-control"
                                       placeholder="New Password"
                                       name="newpassword"
                                       type="password"
                                       required>
                            </div>

                            <div class="form-group">
                                <input class="form-control"
                                       placeholder="Confirm Password"
                                       name="confirmpassword"
                                       type="password"
                                       required>
                            </div>

                            <button type="submit"
                                    class="btn btn-primary btn-block">
                                Reset Password
                            </button>

                            <hr>

                            <a href="index.jsp"
                               class="btn btn-success btn-block">
                                Back to Login
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
        document.getElementById('changepassword').addEventListener('submit', async function(e) {
            e.preventDefault();

            const newPass = this.newpassword.value;
            const confirmPass = this.confirmpassword.value;

            const errorDiv = document.getElementById('errorMsg');
            const successDiv = document.getElementById('successMsg');
            errorDiv.style.display = 'none';
            successDiv.style.display = 'none';

            if (newPass !== confirmPass) {
                errorDiv.textContent = "New Password and Confirm Password do not match";
                errorDiv.style.display = 'block';
                return;
            }

            try {
                const response = await fetch('<c:url value="/api/auth/reset"/>', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ password: newPass })
                });

                const data = await response.json().catch(() => ({}));

                if (response.ok) {
                    successDiv.textContent = data.message || "Password has been reset successfully.";
                    successDiv.style.display = 'block';
                    this.reset();
                    setTimeout(() => window.location.href = 'index.jsp', 2000);
                } else if (response.status === 401) {
                    errorDiv.textContent = data.error || "Session expired. Please login again.";
                    errorDiv.style.display = 'block';
                    setTimeout(() => window.location.href = 'index.jsp', 2000);
                } else {
                    errorDiv.textContent = data.error || "Password reset failed. Try again.";
                    errorDiv.style.display = 'block';
                }

            } catch (err) {
                errorDiv.textContent = "Network error. Please try again.";
                errorDiv.style.display = 'block';
            }
        });
    </script>
    </body>
</html>
