package com.quickcart.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quickcart.data.models.OrderDTO;
import com.quickcart.data.models.ProductDTO;
import com.quickcart.data.models.ReviewDTO;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Response;


/**
 * Servlet implementation class ProductDetails
 */
@WebServlet("/Product/Details")
public class Product extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Product() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		  // Get productId from request parameter
	    String productId = request.getParameter("productId");

	    try {
	        // Initialize the product manager
	        ProductManager productManager = new ProductManager();

	        // Fetch product details using sp_product_get_details_by_id
	        ResultSet productRs = productManager.getPrductDetailsById(Integer.parseInt(productId));
	        
	        // Initialize the ProductDTO
	        ProductDTO product = new ProductDTO();

	        if (productRs.next()) {
	            // Populate the ProductDTO with details from the first stored procedure
	            product.setProductID(productRs.getInt("ProductID"));
	            product.setProductName(productRs.getString("ProductName"));
	            product.setDescription(productRs.getString("Description"));
	            product.setPrice(productRs.getDouble("Price"));
	            product.setImageURI(productRs.getString("ImageURL"));
	            product.setCategoryDescription(productRs.getString("CategoryDescription"));
	            
	            
	        } else {
	            // If no product details found, handle appropriately (e.g., return error response)
	        	Response.ResponseError(response, "Product not found.");
	            return;
	        }

	        // Fetch reviews using sp_product_review_list_by_product_id
	        ResultSet reviewRs = productManager.getPrductReviewListById(Integer.parseInt(productId));
	        List<ReviewDTO> reviews = new ArrayList<>();
	        UserDTO user = (UserDTO) request.getSession().getAttribute("userData");
	        if(user!=null) {
	        OrderDTO recentOrder = productManager.getRecentOrderForProduct(Integer.parseInt(productId), user.getUserId());
	        if (recentOrder != null) {
	            product.setRecentOrderOftheProduct(recentOrder);
	        }}
	        
	        if(reviewRs!=null) 
	        {
	        while (reviewRs.next()) {
	            // Populate each ReviewDTO and add to the reviews list
	            ReviewDTO review = new ReviewDTO();
	            review.setReviewID(reviewRs.getInt("ReviewID"));
	            review.setUserID(reviewRs.getInt("UserID"));
	         
	            review.setDisplayName(reviewRs.getString("DisplayName"));
	            review.setProductID(reviewRs.getInt("ProductID")); // Same as productId
	            review.setOrderID(reviewRs.getInt("OrderID"));
	            review.setRating(reviewRs.getDouble("Rating"));
	            review.setComment(reviewRs.getString("Comment"));
	            review.setCommentedAt(reviewRs.getTimestamp("CommentedAt").toLocalDateTime());

	            reviews.add(review);
	        }
	        }

	        // Set reviews list to ProductDTO
	        product.setProductReviews(reviews);
	        
			Response.ResponseSuccess(response,product);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle error
	        Response.ResponseError(response, "Error retrieving product details.");
	    }
        
	}

}
