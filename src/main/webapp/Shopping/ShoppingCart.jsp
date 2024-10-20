<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../web-library/Reference.jsp"%>
<%@ include file="../Menu.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
    <style>
        .cart-container {
            margin-top: 50px;
        }
        .cart-table {
            width: 100%;
            margin-bottom: 30px;
        }
        .cart-table th, .cart-table td {
            padding: 15px;
            text-align: center;
        }
        .cart-table img {
            max-width: 100px;
        }
        .total-price {
            text-align: right;
            font-weight: bold;
        }
        .btn-custom {
            background-color: #FFA500;
            color: white;
            border: none;
        }
        .btn-custom:hover {
            background-color: #e69500;
        }
    </style>
</head>
<body>
    <div class="container cart-container" style="margin-top:5rem !important">
        <h2 class="text-center">Shopping Cart</h2>
        <table class="cart-table table table-bordered">
            <thead>
                <tr>
                    <th>Product Image</th>
                    <th>Product Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Remove</th>
                </tr>
            </thead>
            <tbody id="cartTableBody">
                <!-- Products will be dynamically loaded here -->
            </tbody>
        </table>  

        <div class="total-price">
            Total: <span id="totalPrice">$0.00</span>
        </div>

        <div class="mt-4">
            <button class="btn btn-custom" onclick="checkout()">Checkout</button>
            <button class="btn btn-secondary" onclick="goBack()">Back to Shopping</button>
        </div>
    </div>

    <!-- Moved Scripts to the bottom -->
    <script>
        // Function to update the total price
        function updateTotalPrice() {
            let totalPrice = 0;
            document.querySelectorAll(".cart-row").forEach(function (row) {
                const price = parseFloat(row.querySelector(".price").innerText);
                const quantity = parseInt(row.querySelector(".quantity").value);
                totalPrice += price * quantity;
            });
            document.getElementById("totalPrice").innerText = "$" + totalPrice.toFixed(2);
        }

        // Function to update product quantity in the cart
        function updateQuantity(productId, quantity) {
            if (quantity < 1) {
                alert("Quantity must be at least 1.");
                return;
            }
            
            AjaxCall(
                "Shopping/Cart?productId=" + productId + "&quantity=" + quantity,
                "PUT",
                { },
                function (data) {
                    updateTotalPrice();
                },
                function (jqXHR) {
                    alert(jqXHR.responseText);
                }
            );
        }

        // Function to delete a product from the cart
        function deleteProduct(productId, rowId) {
            const confirmDelete = confirm("Are you sure you want to delete this product from the cart?");
            
            if (!confirmDelete) return;

            AjaxCall(
                "Shopping/Cart?productId=" + productId,
                "DELETE",
                null,
                function (data) {
                    document.getElementById(rowId).remove();
                    updateTotalPrice();

                    const remainingRows = document.querySelectorAll(".cart-row").length;
                    if (remainingRows === 0) {
                        const cartTableBody = document.getElementById("cartTableBody");
                        cartTableBody.innerHTML = '<tr><td colspan="5" class="text-center">Your cart is empty. <a href="' + getBaseURL() + 'Home.jsp">Go to shopping</a></td></tr>';
                        document.querySelector(".total-price").style.display = 'none';
                        document.querySelector(".mt-4").style.display = 'none';
                    }
                },
                function (jqXHR) {
                    alert(jqXHR.responseText);
                }
            );
        }

        // Function to load cart products when page loads
       function loadCartProducts() {
    AjaxCall(
        "Shopping/Cart",
        "GET",
        null,
        function (response) {
            const cartItemList = response.data;
            const cartTableBody = document.getElementById("cartTableBody");
            const totalPriceContainer = document.querySelector(".total-price");
            const buttonsContainer = document.querySelector(".mt-4");

            cartTableBody.innerHTML = "";

            if (cartItemList.length === 0) {
                cartTableBody.innerHTML = '<tr><td colspan="5" class="text-center">Your cart is empty. <a href="' + getBaseURL() + 'Home.jsp">Go to shopping</a></td></tr>';
                totalPriceContainer.style.display = 'none';
                buttonsContainer.style.display = 'none';
            } else {
                // Build the entire table rows as a single string first
                let rowsHtml = '';

                cartItemList.forEach(function (cartItem, index) {
                    const product = cartItem.product;
                    const rowId = "productRow" + index;

                    // Build the HTML for each row
                    rowsHtml += 
                        '<tr id="' + rowId + '" class="cart-row">' +
                            '<td><img src="' + getBaseURL() +"Images/"+ product.imageURI + '" alt="' + product.productName + '"></td>' +
                            '<td>' + product.productName + '</td>' +
                            '<td class="price">' + product.price.toFixed(2) + '</td>' +
                            '<td>' +
                            '<input type="number" class="quantity" id="quantity_' + product.productID + '" value="' + cartItem.quantity + '" min="1">' +
                        '</td>' +
                            '<td>' +
                                '<button class="btn btn-danger" onclick="deleteProduct(' + product.productID + ', \'' + rowId + '\')">Delete</button>' +
                            '</td>' +
                        '</tr>';
                });

                // Update the table body in one operation
                cartTableBody.innerHTML = rowsHtml;

                // Attach event listeners after the DOM has been updated
                cartItemList.forEach(function (cartItem) {
                    const product = cartItem.product;
                    document.getElementById("quantity_" + product.productID).addEventListener("change", function(event) {
                        const newQuantity = event.target.value;
                        updateQuantity(product.productID, newQuantity);
                    });
                });

                // Show the total price and buttons container
                totalPriceContainer.style.display = 'block';
                buttonsContainer.style.display = 'block';

                // Update the total price
                updateTotalPrice();
            }
        },
        function (jqXHR) {
            alert("Error loading cart: " + jqXHR.responseText);
        }
    );
}


        // Function to proceed to checkout
        function checkout() {
            AjaxCall(
                "Shopping/CheckOut",
                "POST",
                null,
                function (response) {
                    const orderID = response.data;
                    window.location.href = getBaseURL() + "Shopping/OrderDetails.jsp?orderID=" + orderID;
                },
                function (jqXHR) {
                    alert("Checkout failed: " + jqXHR.responseText);
                }
            );
        }

        // Function to go back to the product list
        function goBack() {
            window.location.href = getBaseURL() + "Home.jsp";
        }

        // Load cart products when the page is ready
        document.addEventListener("DOMContentLoaded", function () {
            loadCartProducts();
        });
    </script>
</body>
</html>
