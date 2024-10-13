<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../web-library/Reference.jsp"%>
<%@ include file="../Menu.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cart Page</title>
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
</style>
</head>
<body>
	<div class="container mt-5">
	
			<!-- %response.sendRedirect("DisplayCart");%-->
		<h2 class="text-center mb-4">List of items in cart</h2>

		<div class="row">
			<!-- Product Card 1 -->
			<div class="col-md-6">
				<h4></h4>
				<div class="card">
					<a href="product-details.jsp?id=3"
						class="text-decoration-none text-dark"> 
						<div class="card-body">
							<h5 class="card-title">Product 1</h5>
													
							<p class="card-text">$39.99</p>
							<input type="checkbox">
						</div>
					</a>
				</div>
			</div>
		</div>
		
		<div class="checkout">
			<!-- Product Card 1 -->
			<div class="col-md-3 ">
				<h4></h4>
				<div class="card">
					<a href="product-details.jsp?id=3"
						class="text-decoration-none text-dark"> <img
						src="https://via.placeholder.com/100" class="card-img-top"
						alt="Product 3">
						<div class="card-body">
							<h5 class="card-title">List Of Items Selected</h5>
													
							<p class="card-text">$39.99</p>
							<input type="checkbox">
						</div>
					</a>
				</div>
			</div>
		</div>		
		<div>
		<div class="col-md-6">
		
		</div>
	</div>

</body>
</html>