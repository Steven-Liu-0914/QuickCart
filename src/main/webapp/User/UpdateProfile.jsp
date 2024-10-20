<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../web-library/Reference.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Profile</title>
    <!-- Styles moved to General.css -->
</head>

<body>
<div class="container mt-5">
    <h2>Update Profile</h2>
    <form id="updateProfile" action="UpdateProfile" method="POST">
        <!-- Display Name -->
        <div class="form-group">
            <label for="newDisplayName">Display Name</label>
            <input type="text" class="form-control" id="newDisplayName" pattern="^[A-Za-z0-9 ]{3,15}$" value="${userData.displayName}" required>
            <small class="form-text text-muted">Allowed: 3-20 characters (letters, numbers, and spaces).</small>
            <div class="error" id="displayNameError"></div>
        </div>
        
        <!-- Phone Number -->
        <div class="form-group">
            <label for="newPhoneNumber">Phone Number</label>
            <input type="tel" class="form-control" id="newPhoneNumber" 
                    placeholder="Enter your mobile number"
                    pattern="(\d{8})" 
                    title="Please enter a valid phone number." value="${userData.phoneNumber}" required>         
            <small class="form-text text-muted">Format: e.g., 98745632</small>
            <div class="error" id="phoneNumberError"></div>
        </div>
        
        <!-- New Password (Optional) -->
        <div class="form-group mt-4">
            <label for="newPassword">New Password (Optional)</label>
            <input type="password" class="form-control" id="newPassword" placeholder="Enter your new password">
            <small class="form-text text-muted">Password must contain at least 8 characters, including uppercase, lowercase, and numbers.</small>
            <div class="error" id="passwordError"></div>
        </div>

        <!-- Confirm Password -->
        <div class="form-group mt-2">
            <label for="confirmPassword">Confirm Password</label>
            <input type="password" class="form-control" id="confirmPassword" placeholder="Confirm new password">
            <div class="error" id="confirmPasswordError"></div>
        </div>

        <!-- Submit and Back Buttons -->
        <button type="submit" class="btn btn-primary mt-4">Update Profile</button>
        <button id="backButton" type="button" class="btn btn-secondary mt-4" onclick="window.history.back();">Back</button>
    </form>
</div>

<!-- Script for validation and AJAX submission -->
<script>
$(document).ready(function() {
    $('#updateProfile').on('submit', async function(event) {  // Mark the function as async
        // Clear previous error messages
        $('.error').text('');

        // Collect updated values
        var email = "${userData.email}";
        var newDisplayName = $("#newDisplayName").val();
        var newPhoneNumber = $("#newPhoneNumber").val();
        var newPassword = $("#newPassword").val();
        var confirmPassword = $("#confirmPassword").val();

        // Basic form validation
        if (!newDisplayName || !newPhoneNumber) {
            event.preventDefault();
            return; // Stop form submission
        }

        // Validate password if a new password is provided
        if (newPassword) {
            if (newPassword.length < 8 || !newPassword.match(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/)) {
                $('#passwordError').text('Password must be at least 8 characters long, contain an uppercase letter, a lowercase letter, and a digit.');
                event.preventDefault();
                return;
            }
            if (newPassword !== confirmPassword) {
                $('#confirmPasswordError').text('Passwords do not match.');
                event.preventDefault();
                return;
            }
        }

        // Prevent default submission for AJAX
        event.preventDefault();

        // Encrypt the password if a new password is provided
        let encryptedPassword = null;
        if (newPassword) {
            encryptedPassword = await encryptPassword(newPassword);  // Use await to encrypt the password
        }

        // AJAX call for updating profile
        AjaxCall(
            "User/UpdateProfile",
            "POST",
            {                    
                newDisplayName: newDisplayName, 
                newPhoneNumber: newPhoneNumber, 
                newPassword: encryptedPassword // Pass encrypted password
            },
            function(data) {
                // Handle success
                window.location.href = getBaseURL() + "User/UpdateProfileSuccess.jsp";
            },
            function(jqXHR) {
                // Handle error
                alert(jqXHR.responseText); // Show error message
            }
        );
    });
});
</script>
</body>     
</html>
