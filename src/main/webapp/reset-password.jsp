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

        <script>
            function checkpass() {
                if (document.changepassword.newpassword.value !== document.changepassword.confirmpassword.value) {
                    alert("New Password and Confirm Password do not match");
                    document.changepassword.confirmpassword.focus();
                    return false;
                }
                return true;
            }
        </script>
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

                    <%
                        String error = request.getParameter("error");
                        if ("1".equals(error)) {
                    %>
                        <div class="alert alert-danger text-center">
                            Password update failed
                        </div>
                    <%
                        }
                    %>

                    <form name="changepassword" method="post" action="user?action=reset" onsubmit="return checkpass();">

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
        <script src="<c:url value='/js/easypiechart.js'/>"></script>
        <script src="<c:url value='/js/easypiechart-data.js'/>"></script>
    </body>
</html>
