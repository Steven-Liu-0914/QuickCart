<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="web-library/Reference.jsp" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Profile</title>
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
        
    </style>
</head>

<body>
<div class="container mt-5">
    <h2>Update Profile</h2>
    <form id="updateProfile" action="UpdateProfile" method="POST">
        <div class="form-group">
            <label for="newDisplayName">Display Name</label>
            <input type="text" class="form-control" id="newDisplayName" pattern="^[A-Za-z0-9 ]{3,15}$" value="${userData.displayName}" required>
        	<small class="form-text text-muted">Allowed: 3-20 characters (letters, numbers, and spaces).</small>
        	<div class="error" id="displayNameError"></div>
        </div>
        <div class="form-group">
            <label for="newPhoneNumber">Phone Number</label>
            <input type="text"class="form-control" id="newPhoneNumber" pattern="(\d{8})" value="${userData.phoneNumber}" required>
        	<small class="form-text text-muted">Format: e.g. 98745632</small>
        	<div class="error" id="phoneNumberError"></div> 
        </div>
        <button type="submit" class="btn btn-primary">Update Profile</button>
        <button id="backButton" type="button" class="btn btn-secondary" onclick="window.history.back();">Back</button>
    </form>
</div>

<script>
    
    $(document).ready(function() {
        $('#updateProfile').on('submit', function(event) {
            // Clear previous error messages
            $('.error').text('');

            // Check if the form is valid
            if (!this.checkValidity()) {
                event.preventDefault(); // Prevent form submission
                this.classList.add('was-validated'); // Add Bootstrap validation class

                // Show custom error messages
                
                if (!$('#newDisplayName')[0].checkValidity()) {
                    $('#displayNameError').text('Please enter a valid display name.');
                }
                if (!$('#newPhoneNumber')[0].checkValidity()) {
                    $('#phoneNumberError').text('Phone number must be 8 digits');
                }
                
            } else {
                // If the form is valid, prevent default submission for AJAX
                event.preventDefault();
            
          		var email = "${userData.email}";
            	var newDisplayName = $("#newDisplayName").val();
            	var newPhoneNumber = $("#newPhoneNumber").val();
                
                

                AjaxCall(
                    "http://localhost:8080/QuickCart/User/UpdateProfile",
                    "POST",
                    { email: email, newDisplayName: newDisplayName, newPhoneNumber: newPhoneNumber },
                    function(data) {
                        // Handle success
                        window.location.href = "UpdateProfileSucess.jsp"; // Example redirect
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
