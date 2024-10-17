<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.quickcart.data.models.*,java.util.*"%>
<%@ include file="../web-library/Reference.jsp"%>
<%@ include file="../Menu.jsp"%>

<%
    // Check if the user is logged in
    UserDTO user = (UserDTO) session.getAttribute("userData");
    boolean isLoggedIn = user != null;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Product Details</title>
    <!-- Bootstrap CSS -->
    <style>
        .btn-custom {
            background-color: #FFA500;
            color: white;
            border: none;
        }
        .btn-custom:hover {
            background-color: #e69500;
            color: white;
        }
        .product-image img {
            max-width: 100%;
            height: auto;
        }
        .review-section {
            margin-top: 30px;
        }
        .share-icons a {
            color: #FFA500;
            margin-right: 10px;
            font-size: 1.3em;
            text-decoration: none;
        }
      
        .share-icons i {
            margin-right: 3px;
        }
        .alert-warning {
            background-color: #fff3cd;
            color: #856404;
            border-color: #ffeeba;
        }
        .form-control, .btn {
            border-radius: 0.25rem;
        }
  
    </style>
</head>
<body>

<div class="container mt-5" style="margin-top:5rem !important">
    <div class="row">
        <!-- Product Image -->
        <div class="col-md-5 product-image">
            <img id="productImage" class="img-fluid rounded" alt="Product Image">
        </div>

        <!-- Product Details -->
        <div class="col-md-7">
            <h1 id="productName"></h1>
            <p id="productDescription"></p>
            <h2>Price: $<span id="productPrice"></span></h2>

            <!-- Add to Cart & Buy Now Buttons -->
            <button class="btn btn-custom btn-lg me-2" id="addToCartBtn">Add to Cart</button>
            <button class="btn btn-custom btn-lg" id="buyNowBtn">Buy Now</button>

            <!-- Share buttons -->
            <div class="share-icons mt-5">
                <p>Share this product:</p>
                <a id="whatsappShare" target="_blank"><i class="bi bi-whatsapp"></i> WhatsApp</a>
                <a id="facebookShare" target="_blank"><i class="bi bi-facebook"></i> Facebook</a>
                <a id="twitterShare" target="_blank"><i class="bi bi-twitter"></i> Twitter</a>
            </div>
        </div>
    </div>

     
    <!-- Review section -->
    <div class="review-section mt-5" id="reviewSection">
        <h3>Reviews:</h3>
        <div id="reviewList"></div>
    </div>
</div>

<!-- jQuery (for easy AJAX) -->

<script>
    $(document).ready(function() {
        // Get the product ID from the query parameter
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');

        if (productId) {
            // Load product details via AJAX
            loadProductDetails(productId);
            generateShareLinks(productId);  // Update share links
        }

        // Add to Cart button
        $('#addToCartBtn').click(function() {
            addToCart(productId);
        });

        // Buy Now button
        $('#buyNowBtn').click(function() {
            buyNow(productId);
        });
    });

    // Function to load product details
    function loadProductDetails(productId) {
        AjaxCall(
            "http://localhost:8080/QuickCart/Product/Details",
            "GET",
            { productId: productId },
            function(result) {
                var data = result.data;
                $('#productName').text(data.productName);
                $('#productDescription').text(data.description);
                $('#productPrice').text(data.price);
                $('#productImage').attr('src', data.imageURI);

                // Load reviews
                loadReviews(data.productReviews);
            },
            function(jqXHR) {
                if (jqXHR.responseText == "Product not found.") {
                    alert("Product not found.");
                } else {
                    alert(jqXHR.responseText);
                }
            }
        );
    }

    // Function to load reviews into the review section
   function loadReviews(reviews) {
    const reviewList = $('#reviewList');
    reviewList.empty(); // 清空现有的评论列表
    reviews.forEach(review => {
        const starRating = generateStars(review.rating);  // Helper function to generate star HTML
        const reviewDate = new Date(review.commentedAt).toLocaleDateString();  // 使用JS格式化日期
        const DisplayName = review.displayName || 'Anonymous';  // 显示用户的名称
        const ratingNum = review.rating;
        const Comment = review.comment;
        
        const reviewHtml = 
            '<div class="review-item mb-4 p-3" style="border-bottom: 1px solid #ddd;">' +
            '<div class="d-flex align-items-center mb-2">' +
            '<strong>' + DisplayName + '</strong> <span class="badge bg-success ms-2">Verified Purchase</span>' +
            '</div>' +
            '<div>' + starRating + ' (' + ratingNum + '/5)</div>' +
            '<p>' + Comment + '</p>' +
            '<small class="text-muted">' + reviewDate + '</small>' +
            '</div>';
        
        reviewList.append(reviewHtml);
    });
}


    // Helper function to generate star rating HTML
    function generateStars(rating) {
        let stars = '';
        for (let i = 0; i < 5; i++) {
            stars += i < Math.floor(rating)
                ? '<i class="bi bi-star-fill" style="color: #FFC107;"></i>'  // Full star
                : '<i class="bi bi-star" style="color: #FFC107;"></i>';  // Empty star
        }
        return stars;
    }

    function generateShareLinks(productId) {
        const currentURL = window.location.href; // Get the current page URL

        // Dynamically generate the share links
        const whatsappShareLink = 'https://wa.me/?text=Check out this product: ' + currentURL;
        const facebookShareLink = 'https://www.facebook.com/sharer/sharer.php?u=' + currentURL;
        const twitterShareLink = 'https://twitter.com/intent/tweet?url=' + currentURL + '&text=Check out this product!';

        // Apply the generated links to the share buttons
        $('#whatsappShare').attr('href', whatsappShareLink);
        $('#facebookShare').attr('href', facebookShareLink);
        $('#twitterShare').attr('href', twitterShareLink);
    }


    // Add to cart AJAX call
    function addToCart(productId) {
        AjaxCall(
            "http://localhost:8080/QuickCart/Cart/AddToCart",
            "POST",
            { productId: productId },
            function(data) {
                const addedToCartModal = new bootstrap.Modal(document.getElementById('addedToCartModal'));
                addedToCartModal.show();
            },
            function(jqXHR) {
                if (jqXHR.responseText == "User not logged in") {
                    const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                    loginModal.show();
                } else {
                    alert(jqXHR.responseText);
                }
            }
        );
    }

    // Buy now AJAX call
    function buyNow(productId) {}
    
    </script>
       
