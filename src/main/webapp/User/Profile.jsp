<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../web-library/Reference.jsp" %>    
<%@ include file="../Menu.jsp" %> 
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - QuickCart</title>
    <style>
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
                
                <!-- Edit Profile Button -->
                <a href="UpdateProfile" class="btn btn-primary mt-3">Edit Profile</a>

                <!-- View Order History Button -->
                <a href="OrderHistory.jsp" class="btn btn-primary mt-3">View Order History</a>
            </div>
        </div>
    </div>
</body>
</html>
