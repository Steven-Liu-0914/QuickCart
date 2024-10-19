package com.quickcart.servlet.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.quickcart.data.models.OrderDTO;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Response;

/**
 * Servlet implementation class OrderHistory
 */
@WebServlet("/Order/History")
public class OrderHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 UserDTO user = (UserDTO) request.getSession().getAttribute("userData");

		    if (user == null) {
		        Response.ResponseError(response, "User not logged in");
		        return;
		    }

		    try {
		        OrderManager orderManager = new OrderManager();
		        List<OrderDTO> orderList = orderManager.getOrderHistoryForUser(user.getUserId());

		        Response.ResponseSuccess(response, orderList);
		    } catch (Exception e) {
		        e.printStackTrace();
		        Response.ResponseError(response, "Error retrieving order history");
		    }
	}

	
}
