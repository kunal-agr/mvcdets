<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Forgot Password</title>
        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    </head>

    <body>
    <div class="row">
        <h2 class="text-center">Daily Expense Tracker</h2>
        <hr/>
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">Forgot Password</div>
                <div class="panel-body">
                    <div id="errorMsg" class="alert alert-danger text-center" style="display:none;"></div>
                    <div id="successMsg" class="alert alert-success text-center" style="display:none;"></div>

                    <form id="forgotForm">
                        <div class="form-group">
                            <input class="form-control" name="email" placeholder="Email" type="email" required>
                        </div>
                        <div class="form-group">
                            <input class="form-control" name="mobile" placeholder="Mobile Number" type="text"
                                   pattern="[0-9]{10}" required>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">Verify</button>
                        <hr>
                        <a href="index.jsp" class="btn btn-default btn-block">Back to Login</a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="<c:url value='/js/jquery-1.11.1.min.js'/>"></script>
    <script>
        document.getElementById('forgotForm').addEventListener('submit', async function(e) {
            e.preventDefault();

            const email = this.email.value;
            const mobile = this.mobile.value;

            const response = await fetch('<c:url value="/api/auth/forgot"/>', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, mobile })
            });

            const errorDiv = document.getElementById('errorMsg');
            const successDiv = document.getElementById('successMsg');
            errorDiv.style.display = 'none';
            successDiv.style.display = 'none';

            if (response.ok) {
                window.location.href = 'reset-password.jsp';
            } else {
                const data = await response.json().catch(() => ({}));
                errorDiv.textContent = data.error || "Invalid email or mobile number";
                errorDiv.style.display = 'block';
            }
        });
    </script>
    </body>
</html>
