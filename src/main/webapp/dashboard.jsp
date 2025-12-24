<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Daily Expense Tracker - Dashboard</title>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/datepicker3.css'/>">
        <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">

    </head>

    <body>
    <!-- Header -->
    <%@ include file="includes/header.jsp" %>

    <!-- Sidebar -->
    <%@ include file="includes/sidebar.jsp" %>

    <!-- Main Content -->
    <div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">

        <h1 class="page-header">Dashboard</h1>

        <div class="row">
            <!-- Today's Expense -->
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Today's Expense</h4>
                        <div class="easypiechart" data-percent="-1">
                            <span class="percent">-1</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Yesterday's Expense -->
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Yesterday's Expense</h4>
                        <div class="easypiechart" data-percent="-1">
                            <span class="percent">-1</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Weekly Expense -->
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Last 7 Days Expense</h4>
                        <div class="easypiechart" data-percent="-1">
                            <span class="percent">-1</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Monthly Expense -->
            <div class="col-xs-6 col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body easypiechart-panel">
                        <h4>Last 30 Days Expense</h4>
                        <div class="easypiechart" data-percent="-1">
                            <span class="percent">-1</span>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>

    <!-- Footer -->
    <%@ include file="includes/footer.jsp" %>

    <!-- JS Files (MUST BE AT END) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.11.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/easypiechart.js"></script>
    <script src="<%=request.getContextPath()%>/js/easypiechart-data.js"></script>
    </body>
</html>
