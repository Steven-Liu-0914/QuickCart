<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../web-library/Reference.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Success: Password Reset</title>
    <style>
        /* Custom theme color: Bright Orange (#FFA500) */
        .btn-primary {
            background-color: #FFA500;
            border-color: #FFA500;
        }
        .btn-primary:hover {
            background-color: #e69500;
            border-color: #e69500;
        }
        .card-title {
            color: #FFA500;
        }
        /* Footer styling */
        footer {
            background-color: #FFA500;
            color: white;
            padding: 10px 0;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card text-center">
            <div class="card-body">
                <h1 class="card-title">You have successfully update your password!</h1>
                <p class="card-text">Please re-login to verify</p>
                <a href="../Home.jsp" class="btn btn-primary">Log In Again</a>
            </div>
        </div>
    </div>
</html>