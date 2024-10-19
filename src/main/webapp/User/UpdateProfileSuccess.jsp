<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../web-library/Reference.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Success: Update Profile</title>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card text-center">
            <div class="card-body">
                <h1 class="card-title">You have successfully updated your profile!</h1>              
                <a href="<%=request.getContextPath()%>/Home.jsp" class="btn btn-primary">Continue Shopping</a>
            </div>
        </div>
    </div>
</html>
