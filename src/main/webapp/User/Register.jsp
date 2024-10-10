<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../web-library/Reference.jsp" %>    
<%@ include file="../Menu.jsp" %> 

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - QuickCart</title>
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
           
        .error { color: red; }
        
    </style>
</head>
<body>
    <div class="container mt-5">
        <h2 class="text-center">Registration Page</h2>
        	<form id="registerForm" action="Register" method="post" novalidate>
            <div class="form-group">
                <label for="displayName">Display Name:</label>
                 <input type="text" class="form-control" id="displayName" 
                       placeholder="Enter your display name" 
                       pattern="^[A-Za-z0-9 ]{3,15}$" 
                       title="Display name must be 3-20 characters long and can only contain letters, numbers, and spaces." required>
                <small class="form-text text-muted">Allowed: 3-20 characters (letters, numbers, and spaces).</small>
                <div class="error" id="displayNameError"></div>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" class="form-control" id="password" 
                       placeholder="Enter your password" 
                       pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$" 
                       title="Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit." required>
                <small class="form-text text-muted">Must include at least 8 characters, one uppercase letter, one lowercase letter, and one number.</small>
            	<div class="error" id="passwordError"></div>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" class="form-control" id="email" 
				       placeholder="Enter your email address" 
				       pattern="^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" 
				       title="Please enter a valid email address." required>
				       <div class="error" id="emailError"></div>
            </div>
            <div class="form-group">
                <label for="phoneNumber">Phone Number:</label>
                <input type="tel" class="form-control" id="phoneNumber" 
                       placeholder="Enter your mobile number"
                       pattern="(\d{8})" 
                       title="Please enter a valid phone number." required>
                <small class="form-text text-muted">Format: e.g. 98745632</small>
                <div class="error" id="phoneNumberError"></div>
            </div>
            <button id="registerButton" type="submit" class="btn btn-primary">Register</button>
            <button id="backButton" type="button" class="btn btn-secondary" onclick="window.history.back();">Back</button>
        	</form>
    </div>
    

    <!-- Custom script for AJAX register -->  
      
    <script>
    
    $(document).ready(function() {
        $('#registerForm').on('submit', function(event) {
            // Clear previous error messages
            $('.error').text('');

            // Check if the form is valid
            if (!this.checkValidity()) {
                event.preventDefault(); // Prevent form submission
                this.classList.add('was-validated'); // Add Bootstrap validation class

                // Show custom error messages
                if (!$('#displayName')[0].checkValidity()) {
                    $('#displayNameError').text('Display name must be 3-20 characters long and can only contain letters, numbers, and spaces.');
                }
                if (!$('#email')[0].checkValidity()) {
                    $('#emailError').text('Please enter a valid email address.');
                }
                if (!$('#password')[0].checkValidity()) {
                    $('#passwordError').text('Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, and one number.');
                }
                if (!$('#phoneNumber')[0].checkValidity()) {
                    $('#phoneNumberError').text('Phone number must be 8 digits.');
                }
            } else {
                // If the form is valid, prevent default submission for AJAX
                event.preventDefault();
            
            //$("#registerButton").on("click", function() {
            	var displayName = $("#displayName").val();
            	var password = $("#password").val();
            	//var confirmPassword = $("#confirmPassword").val();
                var email = $("#email").val();
                var phoneNumber = $("#phoneNumber").val();

                AjaxCall(
                    "http://localhost:8080/QuickCart/User/Register",
                    "POST",
                    { displayName: displayName, password: password, email: email, phoneNumber: phoneNumber },
                    function(data) {
                        // Handle success
                        window.location.href = "RegisterSuccess.jsp"; // Example redirect
                    },
                    function(jqXHR) {
                        // Handle error
                        alert(jqXHR.responseText); // Show error message
                    });
            	};
            
            });
        });
    
    
    
    </script>

 </body>
 </html>
