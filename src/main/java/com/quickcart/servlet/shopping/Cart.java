package com.quickcart.servlet.shopping;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.quickcart.data.models.CartItem;

import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Response;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Shopping/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		   // Get the User Session
        UserDTO user = (UserDTO) request.getSession().getAttribute("userData");

        if (user == null) {
            // If no user session, return error response
            Response.ResponseError(response, "User not logged in");
            return;
        }

        try {
            // Get ProductID from the request (sent through URL or body)
            int productId = Integer.parseInt(request.getParameter("productId"));

            // Get the unique Cart ID for the user (if not exists, create a new cart)
            CartManager cartManager = new CartManager();
            int cartId = cartManager.getOrCreateCartIdForUser(user.getUserId());

            // Insert the product into the Cart
            boolean success = cartManager.addProductToCart(cartId, productId);

            // If successful, return a success response
            if (success) {
                Response.ResponseSuccess(response);
            } else {
                Response.ResponseError(response, "Failed to add product to cart");
            }
        } catch (Exception e) {
            // Handle any other errors
            e.printStackTrace();
            Response.ResponseError(response, "An error occurred while adding the product to cart");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Get the User Session
	    UserDTO user = (UserDTO) request.getSession().getAttribute("userData");

	    if (user == null) {
	        // If no user session, return error response
	        Response.ResponseError(response, "User not logged in");
	        return;
	    }

	    try {
	        // Get Cart ID for the user
	        CartManager cartManager = new CartManager();
	        int cartId = cartManager.getOrCreateCartIdForUser(user.getUserId());

	        // Get list of CartItem in the user's cart
	        List<CartItem> cartItemList = cartManager.getCartItemsInCart(cartId);

	        // Return the cart item list as a success response
	        Response.ResponseSuccess(response, cartItemList);

	    } catch (Exception e) {
	        // Handle any other errors
	        e.printStackTrace();
	        Response.ResponseError(response, "An error occurred while retrieving the cart items");
	    }
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Parse product ID and quantity from the request
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Get user session to retrieve user information
            UserDTO user = (UserDTO) request.getSession().getAttribute("userData");

            if (user == null) {
                // Return error if user is not logged in
                Response.ResponseError(response, "User not logged in");
                return;
            }

            // Retrieve the CartID for the logged-in user
            CartManager cartManager = new CartManager();
            int cartId = cartManager.getOrCreateCartIdForUser(user.getUserId());

            // Call CartManager to update the quantity
            boolean success = cartManager.updateProductQuantityInCart(cartId, productId, quantity);

            if (success) {
                // Send success response
                Response.ResponseSuccess(response);
            } else {
                // Send error if updating failed
                Response.ResponseError(response, "Failed to update product quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response.ResponseError(response, "An error occurred while updating the product quantity");
        }
    }
	
	// Handle DELETE request to remove a product from the cart
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get the productId from query parameters
            int productId = Integer.parseInt(request.getParameter("productId"));

            // Get user session
            UserDTO user = (UserDTO) request.getSession().getAttribute("userData");

            if (user == null) {
                // If user is not logged in, return error response
                Response.ResponseError(response, "User not logged in");
                return;
            }

            // Get Cart ID for the user
            CartManager cartManager = new CartManager();
            int cartId = cartManager.getOrCreateCartIdForUser(user.getUserId());

            // Call the CartManager to delete the product from the cart
            boolean success = cartManager.deleteProductFromCart(cartId, productId);

            if (success) {
                // Return success response if deletion was successful
                Response.ResponseSuccess(response);
            } else {
                // Return error response if deletion failed
                Response.ResponseError(response, "Failed to delete product from the cart");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response.ResponseError(response, "An error occurred while deleting the product from the cart");
        }
    }

}
