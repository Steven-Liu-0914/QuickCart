<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="web-library/Reference.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
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
        .error { color: red;  
        }
        .container {
            max-width: 400px;
            margin-top: 100px;
        }
        .pointer {
            color: #6c757d; /* Bootstrap primary color */
            cursor: pointer; /* Change cursor to pointer */
        }
    </style>
</head>
<body>

<div class="container">
    <h2 class="text-center">Reset Password</h2>
    <form id="resetForm" action="Reset" method="post">
        
        <div class="form-group">
            <label for="email">Enter your Email:</label>
            <input type="email" class="form-control" id="email" name="email" required>
            <small class="form-text text-muted">Please enter your email used to create this account</small>
        </div>
        
        <div class="form-group">
            <label for="newPassword">New Password:</label>
            <input type="password" class="form-control" id="newPassword" 
                       placeholder="Enter your new password" 
                       pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$" 
                       title="Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit." required>
            <small class="form-text text-muted">Must include at least 8 characters, one uppercase letter, one lowercase letter, and one number.</small>
        	<div class="error" id="passwordError"></div>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" class="form-control" id="confirmPassword" 
                       placeholder="Re-enter password" 
                       pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$" 
                       title="Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit." required>
            <small class="form-text text-muted">Please re-enter the password</small>
        	<div class="error" id="passwordError"></div>
        </div>
        <div class="d-grid gap-2">
            <button type="submit" class="btn btn-primary">Reset Password</button>
        </div>
    </form>
    <div class="mt-3 text-center">
    	<span class="pointer" onclick="window.location.href='Login.jsp';">Back to Login</span>
    </div>
	
	<!-- Custom script for AJAX register -->  
      
    <script>
    
    $(document).ready(function() {
        $('#resetForm').on('submit', function(event) {
            // Clear previous error messages
            $('.error').text('');

            // Check if the form is valid
            if (!this.checkValidity()) {
                event.preventDefault(); // Prevent form submission
                this.classList.add('was-validated'); // Add Bootstrap validation class

                // Show custom error messages
                
                if (!$('#email')[0].checkValidity()) {
                    $('#emailError').text('Please enter a valid email address.');
                }
                if (!$('#newPassword')[0].checkValidity()) {
                    $('#passwordError').text('Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, and one number.');
                }
                if (!$('#confirmPassword')[0].checkValidity()) {
                    $('#passwordError').text('Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, and one number.');
                }
            } else {
                // If the form is valid, prevent default submission for AJAX
                event.preventDefault();
            
            //$("#registerButton").on("click", function() {
            	var email = $("#email").val();
            	var newPassword = $("#newPassword").val();
            	var confirmPassword = $("#confirmPassword").val();
                
                

                AjaxCall(
                    "http://localhost:8080/QuickCart/User/Reset",
                    "POST",
                    { email: email, newPassword: newPassword, confirmPassword: confirmPassword },
                    function(data) {
                        // Handle success
                        window.location.href = "PasswordChangeSuccess.jsp"; // Example redirect
                    },
                    function(jqXHR) {
                        // Handle error
                        alert(jqXHR.responseText); // Show error message
                    });
            	};
            
            });
        });
    
    
    
    </script>

	
</div>