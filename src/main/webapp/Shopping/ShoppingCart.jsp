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
    <script>
        // Function to get the base URL
        function getBaseURL() {
            const url = window.location.origin;
            const path = window.location.pathname.split('/');
            return url + '/' + path[1] + '/';
        }

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
                getBaseURL() + "Shopping/Cart?productId="+productId+"&quantity="+quantity,
                "PUT",
                { },
                function (data) {
                    updateTotalPrice(); // Recalculate total price after updating quantity
                },
                function (jqXHR) {
                    alert(jqXHR.responseText);
                }
            );
        }

     // Function to delete a product from the cart
        function deleteProduct(productId, rowId) {
            // Show confirmation dialog before deletion
            const confirmDelete = confirm("Are you sure you want to delete this product from the cart?");
            
            if (!confirmDelete) {
                return; // If user cancels, stop the deletion process
            }

            // Proceed with the AJAX DELETE request if confirmed
            AjaxCall(
                getBaseURL() + "Shopping/Cart?productId=" + productId, // Correct URL format with query parameter
                "DELETE", // Use DELETE method
                null,     // No body needed for DELETE
                function (data) {
                    document.getElementById(rowId).remove(); // Remove the row from the table
                    updateTotalPrice(); // Recalculate total price after deleting

                    // Check if there are any remaining rows in the cart
                    const remainingRows = document.querySelectorAll(".cart-row").length;
                    if (remainingRows === 0) {
                        // If no products are left, show the "cart is empty" message
                        const cartTableBody = document.getElementById("cartTableBody");
                        cartTableBody.innerHTML = '<tr><td colspan="5" class="text-center">Your cart is empty. <a href="' + getBaseURL() + 'Home.jsp">Go to shopping</a></td></tr>';
                        
                        // Hide the total price and buttons
                        document.querySelector(".total-price").style.display = 'none';
                        document.querySelector(".mt-4").style.display = 'none';
                    }
                },
                function (jqXHR) {
                    alert(jqXHR.responseText); // Show error message if any
                }
            );
        }


        // Function to load cart products when page loads
      function loadCartProducts() {
    AjaxCall(
        getBaseURL() + "Shopping/Cart",
        "GET",
        null,
        function (response) {
            const cartItemList = response.data; // CartItem list
            const cartTableBody = document.getElementById("cartTableBody");
            const cartContainer = document.querySelector(".cart-container");
            const totalPriceContainer = document.querySelector(".total-price");
            const buttonsContainer = document.querySelector(".mt-4");

            cartTableBody.innerHTML = ""; // Clear existing rows

            if (cartItemList.length === 0) {
                // If the cart is empty, show a message and hide the table, total price, and buttons
                cartTableBody.innerHTML = '<tr><td colspan="5" class="text-center">Your cart is empty. <a href="' + getBaseURL() + 'Home.jsp">Go to shopping</a></td></tr>';
                totalPriceContainer.style.display = 'none'; // Hide the total price section
                buttonsContainer.style.display = 'none';    // Hide the buttons
            } else {
                cartItemList.forEach(function (cartItem, index) {
                    const product = cartItem.product;
                    const rowId = "productRow" + index;
                    const rowHtml = 
                        '<tr id="' + rowId + '" class="cart-row">' +
                            '<td><img src="' + product.imageURI + '" alt="' + product.productName + '"></td>' +
                            '<td>' + product.productName + '</td>' +
                            '<td class="price">' + product.price.toFixed(2) + '</td>' +
                            '<td>' +
                            '<input type="number" class="quantity" id="quantity_' + product.productID + '" value="' + cartItem.quantity + '" min="1">' +
                        '</td>' +
                            '<td>' +
                                '<button class="btn btn-danger" onclick="deleteProduct(' + product.productID + ', \'' + rowId + '\')">Delete</button>' +
                            '</td>' +
                        '</tr>';
                    cartTableBody.innerHTML += rowHtml;

                    document.getElementById("quantity_" + product.productID).addEventListener("change", function(event) {
                        const newQuantity = event.target.value; // Get the new quantity
                        updateQuantity(product.productID, newQuantity); // Call the updateQuantity function
                    });
                });

                totalPriceContainer.style.display = 'block'; // Show the total price section
                buttonsContainer.style.display = 'block';    // Show the buttons
                updateTotalPrice(); // Calculate total price on page load
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
        getBaseURL() + "Shopping/CheckOut",  // Call the Checkout servlet via POST
        "POST",
        null,  // No body needed for this request
        function (response) {
            // Assuming the response contains the orderID, redirect to the Order Details page
            const orderID = response.data;
            window.location.href = getBaseURL() + "Shopping/OrderDetails.jsp?orderID=" + orderID;
        },
        function (jqXHR) {
            // Show error message if checkout fails
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
</head>
<body>
    <div class="container cart-container">
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
</body>
</html>
