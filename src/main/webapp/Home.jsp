<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.quickcart.data.models.*,java.util.*"%>
<%@ include file="web-library/Reference.jsp"%>
<%@ include file="Menu.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>QuickCart - Home</title>
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

.card-text {
	margin-top: 5px;
	margin-bottom: 10px;
}

.card:hover {
	background-color: #FFF7D1;
}

.card-img-top {
	border-radius: 5px;
	height:400px;
}

.header {
	margin-top: 100px;
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
	<!-- Include the menu -->
	<div class="container mt-5">
		<h2 class="header text-center">Our Featured Products</h2>

		<%
		if (session.getAttribute("redirected") == null) {
			// Redirect to the ProductServlet to set the product list
			response.sendRedirect("Home");
			return; // Prevent further processing
		}
		%>
		<div class="row">
			<%
			List<Product> productList = (List<Product>) session.getAttribute("dataList");

			for (Product p : productList) {
				//p.setImageURI(imgPath);
				int productId = p.getProductID();
			%>
			<div class="col-md-4 mb-4">
				<div class="card">
					<a href="product-details.jsp?id=<%=productId%>"
						class="text-decoration-none text-dark"> <img
						src="<%=p.getImageURI()%>" class="card-img-top" alt="Product">
						<div class="card-body">

							<h5 class="card-title"><%=p.getProductName()%></h5>
							<p class="card-text"><%=p.getPrice()%></p>
							<form method="post" action="Cart" style="display: inline">
								<input name="productId" value="<%=p.getProductID()%>"
									type="hidden">
								<button class="btn btn-primary" type="submit">Add to
									Cart</button>

							</form>

						</div>
					</a>
				</div>

			</div>
			<%
			}
			%>

		</div>

	</div>
</body>




</html>
