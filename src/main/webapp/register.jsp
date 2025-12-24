<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker - Register</title>

       <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
       <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
       <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">

        <script>
            function checkpass() {
                if (document.signup.password.value !==
                    document.signup.repeatpassword.value) {

                    alert("Password and Repeat Password do not match");
                    document.signup.repeatpassword.focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>

    <body>
        <div class="row">
            <h2 align="center">Daily Expense Tracker</h2>
            <hr/>

            <div class="col-xs-10 col-xs-offset-1
                        col-sm-8 col-sm-offset-2
                        col-md-4 col-md-offset-4">

                <div class="login-panel panel panel-default">
                    <div class="panel-heading">Sign Up</div>
                    <div class="panel-body">
                        <form name="signup"
                              method="post"
                              action="user?action=register"
                              onsubmit="return checkpass();">

                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control"
                                           placeholder="Full Name"
                                           name="name"
                                           type="text"
                                           required>
                                </div>

                                <div class="form-group">
                                    <input class="form-control"
                                           placeholder="E-mail"
                                           name="email"
                                           type="email"
                                           required>
                                </div>

                                <div class="form-group">
                                    <input class="form-control"
                                           max = 10
                                           min = 10
                                           placeholder="Mobile Number"
                                           name="mobile"
                                           type="text"
                                           required>
                                </div>

                                <div class="form-group">
                                    <input class="form-control"
                                           placeholder="Password"
                                           name="password"
                                           type="password"
                                           required>
                                </div>

                                <div class="form-group">
                                    <input class="form-control"
                                           placeholder="Repeat Password"
                                           name="repeatpassword"
                                           type="password"
                                           required>
                                </div>

                                <button type="submit"
                                        class="btn btn-primary btn-block">
                                    Register
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
