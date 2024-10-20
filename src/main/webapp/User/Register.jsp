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
    <!-- Link to General CSS already included in Reference.jsp -->
</head>

<body>
    <div class="container mt-5" style="margin-top:5rem !important">
        <h2 class="text-center">Registration Page</h2>
        <form id="registerForm" action="Register" method="post" novalidate>
            <!-- Display Name -->
            <div class="form-group">
                <label for="displayName">Display Name:</label>
                <input type="text" class="form-control" id="displayName" 
                    placeholder="Enter your display name" 
                    pattern="^[A-Za-z0-9 ]{3,20}$" 
                    title="Display name must be 3-20 characters long and can only contain letters, numbers, and spaces." required>
                <small class="form-text text-muted">Allowed: 3-20 characters (letters, numbers, and spaces).</small>
                <div class="error" id="displayNameError"></div>
            </div>

            <!-- Password -->
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" class="form-control" id="password" 
                    placeholder="Enter your password" 
                    pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$" 
                    title="Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit." required>
                <small class="form-text text-muted">Must include at least 8 characters, one uppercase letter, one lowercase letter, and one number.</small>
                <div class="error" id="passwordError"></div>
            </div>

            <!-- Confirm Password -->
            <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" class="form-control" id="confirmPassword" 
                    placeholder="Confirm your password" required>
                <div class="error" id="confirmPasswordError"></div>
            </div>

            <!-- Email -->
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" class="form-control" id="email" 
                    placeholder="Enter your email address" 
                    pattern="^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" 
                    title="Please enter a valid email address." required>
                <div class="error" id="emailError"></div>
            </div>

            <!-- Phone Number -->
            <div class="form-group">
                <label for="phoneNumber">Phone Number:</label>
                <input type="tel" class="form-control" id="phoneNumber" 
                    placeholder="Enter your mobile number"
                    pattern="(\d{8})" 
                    title="Please enter a valid phone number." required>
                <small class="form-text text-muted">Format: e.g. 98745632</small>
                <div class="error" id="phoneNumberError"></div>
            </div>

            <!-- Submit and Back Buttons -->
            <button id="registerButton" type="submit" class="btn btn-primary">Register</button>
            <button id="backButton" type="button" class="btn btn-secondary" onclick="window.history.back();">Back</button>
        </form>
    </div>

    <!-- Custom script for AJAX register and password matching validation -->
    <script>
    $(document).ready(function() {
        $('#registerForm').on('submit', async function(event) {  // Mark function as async
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
                // Check if passwords match
                const password = $('#password').val();
                const confirmPassword = $('#confirmPassword').val();

                if (password !== confirmPassword) {
                    event.preventDefault(); // Prevent form submission
                    $('#confirmPasswordError').text('Passwords do not match.'); // Show error message
                    return;  // Stop here if passwords don't match
                }

                // If the form is valid, prevent default submission for AJAX
                event.preventDefault();

                // Collect form data
                var displayName = $("#displayName").val();
                var email = $("#email").val();
                var phoneNumber = $("#phoneNumber").val();

                // Wait for the password to be encrypted before making the AJAX call
                var encryptedPassword = await encryptPassword(password);  // Use await

                // Proceed with the AJAX call
                AjaxCall(
                    "User/Register",
                    "POST",
                    { displayName: displayName, password: encryptedPassword, email: email, phoneNumber: phoneNumber },
                    function(data) {
                        // Handle success
                        window.location.href = getBaseURL() + "User/RegisterSuccess.jsp"; 
                    },
                    function(jqXHR) {
                        // Handle error
                        alert(jqXHR.responseText); // Show error message
                    }
                );
            }
        });
    });

    </script>
</body>
</html>
