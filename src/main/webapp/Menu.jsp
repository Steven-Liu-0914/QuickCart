<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.quickcart.data.models.UserDTO"%>
<%@ include file="web-library/Reference.jsp"%>



<nav class="navbar navbar-expand-lg navbar-light fixed-top"
	style="background-color: #FFA500;">
	<div class="container">
		<a class="navbar-brand text-white" href="Home.jsp"><b>QuickCart</b></a>


		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav" aria-controls="navbarNav"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarNav">

			
			<ul class="navbar-nav ms-auto">
				<%
				// Check if userData exists in the session
				UserDTO userName = (UserDTO) session.getAttribute("userData");

				String requestURI = request.getRequestURI();
				if (userName != null) {
					if (requestURI.endsWith("ShoppingCart.jsp")) {
				%>
				<li class="nav-item"><a class="nav-link text-white"
					href="../Home.jsp"><b>Home</b></a></li>

				<%
				} else if (requestURI.endsWith("Home.jsp")) {
				%>

				<li class="nav-item"><a class="nav-link text-white"
					href="Shopping/ShoppingCart.jsp"><b>Cart
					</b></a></li>
				<%
				}
				%>


				<li class="nav-item"><a class="nav-link text-white"
					id="profile" href="Profile"><b><%=userName.getDisplayName()%></b></a>
				</li>

				<li class="nav-item"><a class="nav-link text-white" id="logout"
					href="Logout"><b>Log Out</b></a></li>

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
