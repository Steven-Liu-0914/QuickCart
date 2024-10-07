<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="web-library/Reference.jsp" %>
<%@ include file="Menu.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - QuickCart</title>
</head>

<body>
    <div class="container mt-5">
        <h1 class="mb-4">User Profile</h1>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Profile Information</h5>
                <p><strong>User ID:</strong> ${userId}</p>
                <p><strong>Display Name:</strong> ${displayName}</p>
                <p><strong>Email:</strong> ${email}</p>
                <p><strong>Phone Number:</strong> ${phoneNumber}</p>
                <p><strong>Created At:</strong> ${createdAt}</p>
            </div>
        </div>
    </div>
    
</body>
</html>