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
	height: 400px;
}

.header {
	margin-top: 100px;
}

#addToCartButton {
	width: 25%;
	height: 15%;
	margin-bottom: 20px;
	margin-left: 4%;
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
    function AddProductToCart(_productId) {
       
		
        AjaxCall(
            "http://localhost:8080/QuickCart/Cart/AddToCart",
            "POST",
            { productId:_productId},
            function(data) {
                // Handle success
               const loginModal = new bootstrap.Modal(document.getElementById('addedToCartModal'));
               loginModal.show();
               //alert('Add Product to Cart Successfully!'+" your id is "+_productId);
            },
            function(jqXHR) {
                // Handle error
                if(jqXHR.responseText == "User not logged in"){
                    const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                    loginModal.show();
                }else{
                    alert(jqXHR.responseText); // Show error message

                }
            }
        );
    };
   
    </script>
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
		<div class="navbar-nav mx-auto ">
			<form class="d-flex">
				<input class="form-control" type="search" placeholder="Search"
					aria-label="Search" style="width: 600px;">
				<button class="btn me-3" type="submit">
					<i class="bi bi-search"></i>
				</button>
			</form>
		</div>

		<div class="row">
			<%
			List<Product> productList = (List<Product>) session.getAttribute("dataList");

			for (Product p : productList) {
				//p.setImageURI(imgPath);
			%>
			<div class="col-md-4 mb-4">
				<div class="card">
					<a href="product-details.jsp?id=<%=p.getProductID()%>"
						class="text-decoration-none text-dark"> <img
						src="<%=p.getImageURI()%>" class="card-img-top" alt="Product">
						<div class="card-body">

							<h5 class="card-title"><%=p.getProductName()%></h5>
							<p class="card-text">
								$<%=p.getPrice()%></p>
						</div>
					</a>
					<button class="btn btn-primary" type="button" id="addToCartButton"
						onclick="AddProductToCart(<%=p.getProductID()%>)">Add to
						Cart</button>
				</div>

			</div>
			<%
			}
			%>

		</div>

		<div class="modal fade" id="loginModal" tabindex="-1"
			aria-labelledby="loginModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="loginModalLabel">You are not
							logged in!</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body">Log in to add items to your cart.</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Close</button>
						<a href="User/Login.jsp" class="btn btn-primary">Login</a>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="addedToCartModal" tabindex="-1"
			aria-labelledby="addedToCartModal" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="addedToCartModal">Added To Cart!</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					
				</div>
			</div>
		</div>
	</div>
</body>




</html>
