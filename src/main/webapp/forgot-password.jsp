<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Forgot Password</title>

        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
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

                    <%
                        if ("1".equals(request.getParameter("error"))) {
                    %>
                        <div class="alert alert-danger text-center">
                            Invalid email or mobile number
                        </div>
                    <%
                        }
                    %>

                    <form action="user?action=forgot" method="post">
                        <div class="form-group">
                            <input class="form-control"
                                   name="email"
                                   placeholder="Email"
                                   type="email"
                                   required>
                        </div>

                        <div class="form-group">
                            <input class="form-control"
                                   name="mobile"
                                   placeholder="Mobile Number"
                                   type="text"
                                   pattern="[0-9]{10}"
                                   required>
                        </div>

                        <button class="btn btn-primary btn-block">
                            Verify
                        </button>

                        <hr>
                        <a href="index.jsp" class="btn btn-default btn-block">
                            Back to Login
                        </a>
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
