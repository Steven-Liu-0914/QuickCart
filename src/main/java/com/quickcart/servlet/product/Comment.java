package com.quickcart.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Response;

/**
 * Servlet implementation class Comment
 */
@WebServlet("/Product/Comment")
public class Comment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Comment() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is logged in
        UserDTO user = (UserDTO) request.getSession().getAttribute("userData");

        if (user == null) {
            // Return error if the user is not logged in
            Response.ResponseError(response, "User not logged in");
            return;
        }

        try {
            // Parse the incoming request data
            int productId = Integer.parseInt(request.getParameter("productId"));
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            double rating = Double.parseDouble(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            // Call ProductManager to insert the review and get the review ID
            ProductManager productManager = new ProductManager();
            int reviewId = productManager.addProductReview(user.getUserId(), productId, orderId, rating, comment);

            if (reviewId > 0) {
                // If the insert was successful, return success response with the new review ID
                Response.ResponseSuccess(response);
            } else {
                // If the insert failed, return an error response
                Response.ResponseError(response, "Failed to add review.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle any errors that occurred
            Response.ResponseError(response, "An error occurred while adding the review.");
        }
    }

}
