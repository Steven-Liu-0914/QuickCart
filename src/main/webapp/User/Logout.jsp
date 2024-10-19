<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../web-library/Reference.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logout</title>
    <script>

        // Function to log the user out on page load
        function logoutUser() {
            AjaxCall(
                "User/Logout",  // Call the Logout endpoint
                "POST",
                null,
                function (response) {
                    console.log("Logged out successfully");
                    // Display logout message after success
                },
                function (jqXHR) {
                    alert("Failed to log out: " + jqXHR.responseText);
                }
            );
        }

        // Call logoutUser on page load
        document.addEventListener("DOMContentLoaded", function () {
            logoutUser();
        });
    </script>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card text-center">
            <div class="card-body">
                <h1 class="card-title">You have successfully logged out!</h1>
                <p class="card-text">Thank you for shopping at QuickCart! We hope to see you again soon!</p>
                <a href="<%=request.getContextPath()%>/Home.jsp" class="btn btn-primary">Shop Again</a>
            </div>
        </div>
    </div>
</body>
</html>
