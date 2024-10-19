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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Details</title>
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
        .add-review-panel {
            margin-top: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border: 1px solid #e2e6ea;
            border-radius: 5px;
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

            <%-- Check if userData is not null --%>
            <% if (session.getAttribute("userData") != null) { %>
            <!-- Add to Cart & Share Buttons -->
            <button class="btn btn-custom btn-lg me-2" id="addToCartBtn">Add to Cart</button>
            <% } else { %>
            <!-- Message for non-logged-in users -->
            <p>Please <a href="<%=request.getContextPath()%>/User/Login.jsp">log in</a> to add products to your cart.</p>
            <% } %>

            <div class="share-icons mt-5">
                <p>Share this product:</p>
                <a id="whatsappShare" target="_blank"><i class="bi bi-whatsapp"></i> WhatsApp</a>
                <a id="facebookShare" target="_blank"><i class="bi bi-facebook"></i> Facebook</a>
                <a id="twitterShare" target="_blank"><i class="bi bi-twitter"></i> Twitter</a>
            </div>
        </div>
    </div>

    <!-- Add Review Panel -->
    <div class="add-review-panel" id="addReviewPanel" style="display:none;">
        <div id="recentOrderInfo"></div>
        <h4>Leave a Review</h4>
        <div class="form-group">
            <label for="reviewText">Your Review</label>
            <textarea class="form-control" id="reviewText" rows="3" placeholder="Write your review..."></textarea>
        </div>
        <div class="form-group">
            <label for="rating">Your Rating</label>
            <select class="form-control" id="rating">
                <option value="5">★★★★★ (5)</option>
                <option value="4">★★★★☆ (4)</option>
                <option value="3">★★★☆☆ (3)</option>
                <option value="2">★★☆☆☆ (2)</option>
                <option value="1">★☆☆☆☆ (1)</option>
            </select>
        </div>
        <button class="btn btn-custom mt-3" id="submitReviewBtn">Submit Review</button>
    </div>

    <!-- Review Section -->
    <div class="review-section mt-5" id="reviewSection">
        <h3>Reviews:</h3>
        <div id="reviewList"></div>
    </div>
</div>

<!-- Scripts moved to the bottom for optimization -->
<script>
    var orderId = 0;

    $(document).ready(function() {
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');

        if (productId) {
            loadProductDetails(productId);
            generateShareLinks(productId);
        }

        $('#addToCartBtn').click(function() {
        	AddProductToCart(productId);
        });

        $('#submitReviewBtn').click(function() {
            submitReview(productId);
        });
    });

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
    
    function loadProductDetails(productId) {
        AjaxCall(
            "Product/Details",
            "GET",
            { productId: productId },
            function(result) {
                var data = result.data;
                $('#productName').text(data.productName);
                $('#productDescription').text(data.description);
                $('#productPrice').text(data.price);
                $('#productImage').attr('src', getBaseURL() +"Images/"+ data.imageURI);

                // Check if user has recently purchased the product
                if (data.recentOrderOftheProduct) {
                    orderId = data.recentOrderOftheProduct.orderID;
                    $('#recentOrderInfo').html(
                        "You have bought this product recently at " +
                        data.recentOrderOftheProduct.orderPlacedAt +
                        ". (Order #" + data.recentOrderOftheProduct.orderID + "). " +
                        "Do you want to share your comments to help others?"
                    );
                    $('#addReviewPanel').show();
                } else {
                    $('#addReviewPanel').hide();
                }

                // Load reviews
                loadReviews(data.productReviews);
            },
            function(jqXHR) {
                alert(jqXHR.responseText);
            }
        );
    }

    function loadReviews(reviews) {
        const reviewList = $('#reviewList');
        reviewList.empty(); // Clear existing reviews

        if (reviews.length === 0) {
            // If there are no reviews, display a message
            reviewList.append('<p>No reviews yet. Buy and be the first to review!</p>');
        } else {
            reviews.forEach(review => {
                const starRating = generateStars(review.rating);  
                const reviewDate = new Date(review.commentedAt).toLocaleDateString(); 
                const DisplayName = review.displayName;  
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
    }


    function submitReview(productId) {
        const reviewText = $('#reviewText').val();
        const rating = $('#rating').val();

        if (reviewText.trim() === "") {
            alert("Please enter your review.");
            return;
        }

        AjaxCall(
            "Product/Comment",
            "POST",
            { 
                productId: productId,
                orderId: orderId,
                rating: rating,
                comment: reviewText
            },
            function(data) {
                window.location.reload();
            },
            function(jqXHR) {
                alert(jqXHR.responseText);
            }
        );
    }

    function generateStars(rating) {
        let stars = '';
        for (let i = 0; i < 5; i++) {
            stars += i < Math.floor(rating)
                ? '<i class="bi bi-star-fill" style="color: #FFC107;"></i>'  
                : '<i class="bi bi-star" style="color: #FFC107;"></i>';  
        }
        return stars;
    }

    function generateShareLinks(productId) {
        const currentURL = window.location.href;
        const whatsappShareLink = 'https://wa.me/?text=Check out this product: ' + currentURL;
        const facebookShareLink = 'https://www.facebook.com/sharer/sharer.php?u=' + currentURL;
        const twitterShareLink = 'https://twitter.com/intent/tweet?url=' + currentURL + '&text=Check out this product!';

        $('#whatsappShare').attr('href', whatsappShareLink);
        $('#facebookShare').attr('href', facebookShareLink);
        $('#twitterShare').attr('href', twitterShareLink);
    }

</script>
</body>
</html>
