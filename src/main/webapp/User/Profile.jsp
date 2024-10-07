<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../web-library/Reference.jsp" %>    
<%@ include file="../Menu.jsp" %> 
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
</head>
<body>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-4 text-center">
            <img src="https://via.placeholder.com/150" alt="Profile Picture" class="img-fluid rounded-circle">
            <h2 class="mt-3">John Doe</h2>
            <p class="text-muted">User ID: 12345</p>
            <button class="btn btn-primary">Edit Profile</button>
        </div>
        <div class="col-md-8">
            <h3>User Information</h3>
            <ul class="list-group">
                <li class="list-group-item"><strong>Email:</strong> john.doe@example.com</li>
                <li class="list-group-item"><strong>Phone Number:</strong> +1234567890</li>
                <li class="list-group-item"><strong>Joined At:</strong> January 1, 2022</li>
            </ul>
        </div>
    </div>
</div>

</body>
</html>
