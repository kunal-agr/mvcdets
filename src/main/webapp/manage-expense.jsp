<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, com.kagrawal.model.Expense" %>

<%
    Integer uid = null;

    if (session != null) {
        if (session.getAttribute("userId") != null) {
            uid = (Integer) session.getAttribute("userId");
        } else if (session.getAttribute("userid") != null) {
            uid = (Integer) session.getAttribute("userid");
        }
    }

    if (uid == null) {
        response.sendRedirect("logout.jsp");
        return;
    }
     if (request.getAttribute("expenses") == null) {
            response.sendRedirect("expense?action=manage");
            return;
     }
    List<Expense> list = (List<Expense>) request.getAttribute("expenses");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Manage Expense</title>
        <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body>
    <div class="container" style="margin-top:50px;">
        <h3>Manage Expenses</h3>

        <table class="table table-bordered">
            <thead>
            <tr>
                <th>#</th>
                <th>Description</th>
                <th>Amount</th>
                <th>Date</th>
                <th>Category</th>
                <th>Action</th>
            </tr>
            </thead>

            <tbody>
            <%
                if (list != null && !list.isEmpty()) {
                    int i = 1;
                    for (Expense e : list) {
            %>
            <tr>
                <td><%= i++ %></td>
                <td><%= e.getDescription() %></td>
                <td>₹ <%= e.getAmount() %></td>
                <td><%= e.getExpenseDate() %></td>
                <td><%= e.getCategory() %></td>
                <td>
                    <a href="expense?action=delete&expenseId=<%= e.getExpenseId() %>"
                       onclick="return confirm('Delete this expense?')"
                       class="btn btn-danger btn-xs">
                        Delete
                    </a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="6" style="text-align:center;">No expenses found</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <p>Total records: <%= list != null ? list.size() : 0 %></p>
        <a href="dashboard.jsp" class="btn btn-secondary">
                        ← Back to Dashboard
                    </a>
    </div>
    </body>
</html>
