<%@ include file="../web-library/Reference.jsp"%>
<%@ include file="../Menu.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details</title>
    <style>
        .order-container {
            margin-top: 50px;
        }
        .order-table {
            width: 100%;
            margin-bottom: 30px;
        }
        .order-table th, .order-table td {
            padding: 15px;
            text-align: center;
        }
        .order-summary {
            text-align: right;
            font-weight: bold;
        }
        .order-total {
            font-size: 1.2em;
        }
        
        .btn-primary {
	background-color: #FFA500;
	border-color: #FFA500;
}

.btn-primary:hover {
	background-color: #e69500;
	border-color: #e69500;
}
    </style>

    <script>
        // Function to get the base URL
        function getBaseURL() {
            const url = window.location.origin;
            const path = window.location.pathname.split('/');
            return url + '/' + path[1] + '/';
        }
        
        // JavaScript function to redirect to Order History page
        function viewOrderHistory() {
            window.location.href = getBaseURL() + "ViewOrder.jsp"; // Replace with actual OrderHistory page URL
        }

        // Function to load order details
        function loadOrderDetails(orderID) {
            AjaxCall(
                getBaseURL() + "Order/Details?orderID=" + orderID, // API endpoint for fetching order details
                "GET",
                null,
                function (response) {
                    const order = response.data;
                    document.getElementById("orderID").innerText = order.orderID;
                    document.getElementById("orderUser").innerText = order.user.displayName;
                    document.getElementById("orderPlacedAt").innerText = order.orderPlacedAt;
                    document.getElementById("orderTotal").innerText = "$" + order.totalAmount.toFixed(2);

                    // Load order items into the table
                    const orderItems = order.orderItems;
                    const orderTableBody = document.getElementById("orderTableBody");
                    orderTableBody.innerHTML = ""; // Clear existing rows

                    orderItems.forEach(function (item) {
                        const rowHtml = 
                            '<tr>' +
                                '<td>' + item.product.productName + '</td>' +
                                '<td>' + item.quantity + '</td>' +
                                '<td>$' + item.price.toFixed(2) + '</td>' +
                                '<td>$' + (item.price * item.quantity).toFixed(2) + '</td>' +
                            '</tr>';
                        orderTableBody.innerHTML += rowHtml;
                    });
                },
                function (jqXHR) {
                    alert("Failed to load order details: " + jqXHR.responseText);
                }
            );
        }

        // Function to get orderID from the URL query string
        function getOrderIDFromURL() {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get('orderID');
        }

        // Load order details when the page is ready
        document.addEventListener("DOMContentLoaded", function () {
            const orderID = getOrderIDFromURL();
            if (orderID) {
                loadOrderDetails(orderID);
            } else {
                alert("Order ID not found.");
            }
        });
    </script>
</head>
<body>
    <div class="container order-container" style="margin-top:80px">
        <h2 class="text-center">Order Details</h2>

        <div>
            <strong>Order ID:</strong> <span id="orderID"></span><br>
            <strong>User:</strong> <span id="orderUser"></span><br>
            <strong>Order Placed At:</strong> <span id="orderPlacedAt"></span><br>
        </div>

        <table class="order-table table table-bordered">
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody id="orderTableBody">
                <!-- Order items will be dynamically loaded here -->
            </tbody>
        </table>

        <div class="order-summary">
            <span class="order-total">Total: <span id="orderTotal">$0.00</span></span>
        </div>
        
         <!-- View Order History Button -->
        <div class="text-left mt-4">
           <a href="ViewOrder.jsp" class="btn btn-primary">View All Order History</a>
           <a href="Home.jsp" class="btn btn-primary">Continue Shopping</a>
        </div>
    </div>
</body>
</html>
