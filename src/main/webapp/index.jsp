<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker - Login</title>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    </head>

    <body>
        <div class="row">
            <h2 class="text-center">Daily Expense Tracker</h2>
            <hr />

            <div class="col-xs-10 col-xs-offset-1
                        col-sm-8 col-sm-offset-2
                        col-md-4 col-md-offset-4">

                <div class="login-panel panel panel-default">
                    <div class="panel-heading">Log in</div>

                    <div class="panel-body">
                    <%
                        String error = (String) request.getParameter("error");
                        String success = (String) request.getParameter("success");
                        if("1".equals(error)) {
                    %>
                        <div class="alert alert-danger text-center">
                            Invalid email or password
                        </div>
                    <%
                        } if("2".equals(error)) {
                    %>
                        <div class="alert alert-danger text-center">
                            Invalid email or mobile
                        </div>
                    <%
                        } if ("3".equals(error)) {
                    %>
                        <div class="alert alert-danger text-center">
                            Password cannot be reset
                        </div>
                    <%
                        } if("1".equals(success)) {
                    %>
                        <div class="alert alert-success text-center">
                            You have registered successfully
                        </div>
                    <%
                        }
                    %>
                        <form role="form" action="user?action=login" method="post">
                            <fieldset>

                                <div class="form-group">
                                    <input class="form-control"
                                           placeholder="E-mail"
                                           name="email"
                                           type="email"
                                           autofocus
                                           required>
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
                                        name="login"
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
        <script src="<c:url value='/js/easypiechart.js'/>"></script>
        <script src="<c:url value='/js/easypiechart-data.js'/>"></script>
    </body>
</html>