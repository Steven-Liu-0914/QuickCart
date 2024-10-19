<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../web-library/Reference.jsp"%>
<%@ include file="../Menu.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History</title>
    <style>
        .order-history-container {
            margin-top: 80px;
        }
        .order-history-table {
            width: 100%;
            margin-bottom: 30px;
        }
        .order-history-table th, .order-history-table td {
            padding: 15px;
            text-align: center;
        }
        .btn-view {
            background-color: #FFA500;
            color: white;
            border: none;
        }
        .btn-view:hover {
            background-color: #e69500;
        }
    </style>
</head>
<body>
    <div class="container order-history-container">
        <h2 class="text-center">Order History</h2>
        
        <table class="order-history-table table table-bordered">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="orderHistoryTableBody">
                <!-- Order history will be dynamically loaded here -->
            </tbody>
        </table>
    </div>

    <script>
        // Function to get the base URL
        function getBaseURL() {
            const url = window.location.origin;
            const path = window.location.pathname.split('/');
            return url + '/' + path[1] + '/';
        }

        // Function to load order history when page loads
        function loadOrderHistory() {
            AjaxCall(
                getBaseURL() + "Order/History",  // Update with correct API endpoint
                "GET",
                null,
                function (response) {
                    const orderList = response.data;  // Assuming response contains a list of orders
                    const orderHistoryTableBody = document.getElementById("orderHistoryTableBody");

                    orderHistoryTableBody.innerHTML = "";  // Clear existing rows

                    orderList.forEach(function (order) {
                        const rowHtml = 
                            '<tr>' +
                                '<td>' + order.orderID + '</td>' +
                                '<td>' + order.orderPlacedAt + '</td>' +
                                '<td>$' + order.totalAmount.toFixed(2) + '</td>' +
                                '<td><button class="btn btn-view" onclick="viewOrderDetails(' + order.orderID + ')">View</button></td>' +
                            '</tr>';
                        orderHistoryTableBody.innerHTML += rowHtml;
                    });
                },
                function (jqXHR) {
                    alert("Error loading order history: " + jqXHR.responseText);
                }
            );
        }

        // Function to view order details
        function viewOrderDetails(orderID) {
            window.location.href = getBaseURL() + "Shopping/OrderDetails.jsp?orderID=" + orderID;
        }

        // Load order history when the page is ready
        document.addEventListener("DOMContentLoaded", function () {
            loadOrderHistory();
        });
    </script>
</body>
</html>
