<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.quickcart.data.models.*,java.util.*"%>

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
#checkout{
	width: 40%;
	height: 15%;
	margin-bottom: 20px;
	white-space: nowrap;
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

<script>

function calculateCost(price){
	const checkbox = event.target;
	const cost =  document.getElementById("cost");
	
	const currentPrice = parseFloat(cost.textContent);
	const productPrice = parseFloat(price);
	var totalprice = 0.00;

	if(checkbox.checked){
		totalprice = currentPrice + productPrice;
	}
	else{
		totalprice = currentPrice - productPrice;
	}
	console.log(totalprice);

	document.getElementById("cost").textContent = totalprice.toFixed(2);
	
	
}
	
</script>
</head>
<body>
	<div class="container mt-5">

		<%
		if (session.getAttribute("cart-redirected") == null) {
			// Redirect to the DisplayCart to set the cart list
			response.sendRedirect("../Cart/DisplayCart");
			return; // Prevent further processing
		}
		%>
		<!-- %response.sendRedirect("DisplayCart");%-->
		<h2 class="text-center mb-4">List of items in cart</h2>

		<div class="row">
			<div class="col-md-9 mb-4">
				<ol class="list-group list-group-numbered ">
					<%
					List<Product> cartList = (List<Product>) session.getAttribute("cartList");

					for (Product p : cartList) {
					%>
					<li
						class="list-group-item  d-flex justify-content-between align-items-start border p-2">
						<h6><%=p.getProductName()%></h6> <span> $<%=p.getPrice()%></span>

						<input class="form-check-input me-1 checkbox" type="checkbox"
						value="" aria-label="..."
						onclick="calculateCost(<%=p.getPrice()%>)">
					</li>

					<%
					}
					%>
				</ol>
			</div>
			<div class="col-md-3 mb-4">
				<div class="card">
					<h3 class="card-title">Summary</h3>
					<h5 class="card-text">
						Total Cost: $<span id="cost">0.00</span>
					</h5>
									<button class="btn btn-primary" type="button" id="checkout"
						>Checkout</button>
				</div>

			</div>
		</div>

	</div>
</body>
</html>