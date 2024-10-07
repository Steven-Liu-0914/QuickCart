<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.quickcart.data.models.UserDTO"%>
<%@ include file="web-library/Reference.jsp"%>

<script>
function logout() {
    // Clear userData from sessionStorage (or localStorage)
    sessionStorage.removeItem("userData"); // If you used sessionStorage to store user data
    // or
    localStorage.removeItem("userData"); // If you used localStorage

    // Refresh the page
    location.reload();
}
</script>

<nav class="navbar navbar-expand-lg navbar-light"
	style="background-color: #FFA500;">
	<div class="container">
		<a class="navbar-brand text-white" href="#"><b>QuickCart</b></a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ms-auto">
				<%
				// Check if userData exists in the session
				UserDTO userData = (UserDTO) session.getAttribute("userData");
				if (userData != null) {
				%>
				<li class="nav-item"><a class="nav-link text-white"
					href="cart.jsp"><b>My Cart</b></a></li>
				<li class="nav-item"><a class="nav-link text-white" href="#"><b><%=userData.getDisplayName()%></b></a>
				</li>
				<li class="nav-item"><a class="nav-link" style="color:tomato" href="#" onclick="logout()"><b>Log
							Out</b></a></li>
				<%
				} else {
				%>
				<li class="nav-item"><a class="nav-link text-white"
					href="User/Login.jsp"><b>Login</b></a></li>
				<%
				}
				%>
			</ul>
		</div>
	</div>
</nav>
