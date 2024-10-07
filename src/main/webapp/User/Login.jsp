<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../web-library/Reference.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login - QuickCart</title>
</head>
<body>
	<div class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-4">
				<h2 class="text-center">Login</h2>
				<div class="card p-4">
					<form id="loginForm">
						<div class="mb-3">
							<label for="email" class="form-label">Email address</label> 
							<input
								type="email" class="form-control" id="email"
								placeholder="Enter email" required>
						</div>
						<div class="mb-3">
							<label for="password" class="form-label">Password</label> 
							<input
								type="password" class="form-control" id="password"
								placeholder="Enter password" required>
						</div>
						<div class="d-grid gap-2">
							<button id="loginButton" type="button" class="btn btn-primary">Login</button>
						</div>
						<div class="d-grid gap-2 mt-2">
							<button type="button" class="btn btn-secondary"
								onclick="window.location.href='Register.jsp';">Register</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<!-- Custom script for AJAX login -->
	<script>
		$("#loginButton").on("click", function() {
			var userEmail = $("#email").val();
			var password = $("#password").val();

			AjaxCall("http://localhost:8080/QuickCart/User/Login", "POST", {
				email : userEmail,
				password : password
			}, function(data) {
				// Handle success
				window.location.href = "../Home.jsp"; // Example redirect
			}, function(jqXHR) {
				// Handle error
				alert(jqXHR.responseText); // Show error message
			});
		});
	</script>
</body>
</html>
