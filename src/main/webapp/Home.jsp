<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="web-library/Reference.jsp"%>
<%@ include file="Menu.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>QuickCart - Home</title>

<!-- External CSS for Styling -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/web-library/CSS/Home.css"> 
</head>

<body>
    <div class="container mt-5">
        <h2 class="header text-center" style="margin-top:80px">Our Fashion Products</h2>

        <!-- Search Bar -->
        <div class="navbar-nav mx-auto search-bar">
            <div class="input-group">
                <select id="categorySelect" class="form-select category-select" style="max-width: 200px;" onchange="filterProducts()">
                    <option value="all">All Categories</option>
                    <option value="electronics">Electronics</option>
                    <option value="clothing">Clothing</option>
                    <option value="books">Books</option>
                </select>
                <input class="form-control" id="searchInput" type="text" placeholder="Search for products..." oninput="filterProducts()">
            </div>
        </div>

        <!-- Product Listing -->
        <div class="row" id="productListing"></div>

        <!-- Modals for login -->
        <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="loginModalLabel">You are not logged in!</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">Log in to add items to your cart.</div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <a href="<%=request.getContextPath()%>/User/Login.jsp" class="btn btn-primary">Login</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
<script>
    // Add product to cart function using the base URL
    function AddProductToCart(_productId) {
        const userData = '<%= session.getAttribute("userData") != null ? "exists" : "null" %>';

        if (userData === "null") {
            const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
            loginModal.show();
        } else {
            AjaxCall(
               "Shopping/Cart",
                "POST",
                { productId: _productId },
                function(data) {
                    alert("Product added to cart successfully!");
                },
                function(jqXHR) {
                    alert(jqXHR.responseText);
                }
            );
        }
    }

    function filterProducts() {
        const searchQuery = document.getElementById('searchInput').value.toLowerCase();
        const selectedCategory = document.getElementById('categorySelect').value;

        // Loop through all products and hide those that don't match the search query or category
        document.querySelectorAll('.product-item').forEach(function (product) {
            const productName = product.getAttribute('data-name').toLowerCase();
            const productCategory = product.getAttribute('data-category').toLowerCase();

            // Check if the product matches the search query and selected category
            if ((selectedCategory === 'all' || selectedCategory === productCategory) &&
                productName.includes(searchQuery)) {
                product.style.display = 'block';
            } else {
                product.style.display = 'none';
            }
        });
    }
    
    // Fetch product listing via AJAX
    function fetchProductListing() {
        AjaxCall(
            "Product/Listing",
            "GET",
            null,
            function(response) {
                const productList = response.data; 
                renderProductList(productList);
            },
            function(jqXHR) {
                alert("Failed to load products: " + jqXHR.responseText);
            }
        );
    }

 // Dynamically render product list
    function renderProductList(products) {
        const productContainer = document.getElementById('productListing');
        productContainer.innerHTML = ''; // Clear previous content

        products.forEach(function(product) {
            const productCard = 
                '<div class="col-md-4 mb-4 product-item" data-name="' + product.productName + '" data-category="' + product.categoryDescription + '">' +
                    '<div class="card">' +
                        '<a href="' + getBaseURL() + 'Product/ProductDetails.jsp?id=' + product.productID + '" class="text-decoration-none text-dark">' +
                            '<img src="'+ getBaseURL() +"Images/"+ product.imageURI + '" class="card-img-top" alt="Product">' +
                            '<div class="card-body">' +
                                '<h5 class="card-title">' + product.productName + '</h5>' +
                                '<p class="card-text">$' + product.price + '</p>' +
                            '</div>' +
                        '</a>' +
                        '<button class="btn btn-primary" type="button" id="addToCartButton" onclick="AddProductToCart(' + product.productID + ')">Add to Cart</button>' +
                    '</div>' +
                '</div>';
            productContainer.innerHTML += productCard;
        });
    }

    // Load product listing when page loads
    window.onload = function() {
        fetchProductListing();
    };
</script>
</body>
</html>


