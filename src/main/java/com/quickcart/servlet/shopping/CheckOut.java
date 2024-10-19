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
 * Servlet implementation class CheckOut
 */
@WebServlet("/Shopping/CheckOut")
public class CheckOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckOut() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            // Get the cart items to calculate total price
            List<CartItem> cartItems = cartManager.getCartItemsInCart(cartId);
            if (cartItems.isEmpty()) {
                Response.ResponseError(response, "Cart is empty. Cannot proceed to checkout.");
                return;
            }

            // Calculate total amount
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getProduct().getPrice() * item.getQuantity();
            }

            // Create the order and get the OrderID
            int orderID = cartManager.createOrderForUser(user.getUserId(), cartItems, totalAmount);

            // Clear the cart after placing the order
            cartManager.clearCart(cartId);

            // Redirect to the order details page
            Response.ResponseSuccess(response, Integer.toString(orderID));

        } catch (Exception e) {
            e.printStackTrace();
            Response.ResponseError(response, "An error occurred while processing the order.");
        }
    }

}
