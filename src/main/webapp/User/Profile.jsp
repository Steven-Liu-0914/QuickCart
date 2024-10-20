<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../web-library/Reference.jsp" %>    
<%@ include file="../Menu.jsp" %> 

<%
    // Get userData from session
    UserDTO user = (UserDTO) session.getAttribute("userData");
%>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile - QuickCart</title>
</head>

<body>
    <div class="container mt-5" style="margin-top: 5rem !important">
        <h1 class="mb-4">User Profile</h1>

        <%
            if (user == null) {
                // If userData is null, prompt the user to log in
        %>
                <div class="alert alert-warning" role="alert">
                    Please <a href="<%=request.getContextPath()%>/User/Login.jsp" class="alert-link">log in</a> to view your profile.
                </div>
        <%
            } else {
                // If userData is present, show the profile information
        %>
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Profile Information</h5>

                        <!-- Display user's profile data -->
                        <p><strong>User ID:</strong> <%= user.getUserId() %></p>
                        <p><strong>Display Name:</strong> <%= user.getDisplayName() %></p>
                        <p><strong>Email:</strong> <%= user.getEmail() %></p>
                        <p><strong>Phone Number:</strong> <%= user.getPhoneNumber() %></p>
                        <p ><strong>Created At:</strong><span id="createdAt"><%= user.getCreatedAt() %></span></p>
                        
                        <!-- Edit Profile Button -->
                        <a href="<%=request.getContextPath()%>/User/UpdateProfile.jsp" class="btn btn-primary mt-3">Edit Profile</a>

                        <!-- View Order History Button -->
                        <a href="<%=request.getContextPath()%>/Shopping/ViewOrder.jsp" class="btn btn-primary mt-3">View Order History</a>
                    </div>
                </div>
        <%
            }
        %>
    </div>
    
    <script>
      document.addEventListener("DOMContentLoaded", function() {
        // Get the createdAt value from the JSP
        const createdAtValue = document.getElementById("createdAt").innerText;

        // Format the date
        const formattedDate = formatDateToSGTime(createdAtValue);

        // Update the content with the formatted date
        document.getElementById("createdAt").innerText = formattedDate;
    });
</script>
</body>
</html>
