package com.quickcart.servlet.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.quickcart.data.models.OrderDTO;
import com.quickcart.general.Response;

/**
 * Servlet implementation class Order
 */
@WebServlet("/Order/Details")
public class Order extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Order() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String orderIdParam = request.getParameter("orderID");
	        if (orderIdParam == null) {
	            Response.ResponseError(response, "Order ID is missing.");
	            return;
	        }

	        try {
	            int orderID = Integer.parseInt(orderIdParam);

	            // Use OrderManager to retrieve order details
	            OrderManager orderManager = new OrderManager();
	            OrderDTO order = orderManager.getOrderDetails(orderID);

	            if (order == null) {
	                Response.ResponseError(response, "Order not found.");
	            } else {
	                Response.ResponseSuccess(response, order);  // Return the order details as JSON
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            Response.ResponseError(response, "An error occurred while retrieving the order details.");
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
