<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../web-library/Reference.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Success: Register</title>
    <!-- Link to General CSS already included in Reference.jsp -->
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card text-center">
            <div class="card-body">
                <h1 class="card-title">Registration Successful!</h1>
                <p class="card-text">Click to start shopping!</p>
                <a href="<%=request.getContextPath()%>/Home.jsp" class="btn btn-primary">Start Shopping</a>
            </div>
        </div>
    </div>
</body>
</html>
