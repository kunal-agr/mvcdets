<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Daily Expense Tracker - Login</title>

        <!-- Bootstrap CSS -->
        <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/css/datepicker3.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/css/styles.css" rel="stylesheet">
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
                        String error = request.getParameter("error");
                        if("1".equals(error)) {
                    %>
                        <div class="alert alert-danger text-center">
                            Invalid email or password
                        </div>
                    <%
                        }
                    %>
                        <form role="form" action="login" method="post">
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

                                <a href="#">Forgot Password?</a>

                                <br><br>

                                <button type="submit"
                                        name="login"
                                        class="btn btn-primary btn-block">
                                    Login
                                </button>

                                <hr>

                                <a href="#"
                                   class="btn btn-success btn-block">
                                    Register
                                </a>

                            </fieldset>
                        </form>
                    </div>
                </div>

            </div>
        </div>

        <script src="<%=request.getContextPath()%>/js/jquery-1.11.1.min.js"></script>
        <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    </body>
</html>
