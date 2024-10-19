package com.quickcart.servlet.order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quickcart.data.models.OrderDTO;
import com.quickcart.data.models.OrderItemDTO;
import com.quickcart.data.models.ProductDTO;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Database;
public class OrderManager {
	 public OrderDTO getOrderDetails(int orderID) throws SQLException {
	        Database db = new Database();
	        ArrayList<Object> vals = new ArrayList<>();
	        vals.add(orderID);

	        ResultSet rs = db.runSP("{CALL sp_order_get_details(?)}", vals);
	        OrderDTO order = null;
	        if (rs.next()) {
	            order = new OrderDTO();
	            order.setOrderID(rs.getInt("OrderID"));
	            order.setUser(new UserDTO(rs.getInt("UserID"), rs.getString("DisplayName")));
	            order.setOrderPlacedAt(rs.getTimestamp("OrderPlacedAt").toString());
	            order.setTotalAmount(rs.getDouble("TotalAmount"));

	            // Retrieve order items
	            List<OrderItemDTO> orderItems = getOrderItems(orderID);
	            order.setOrderItems(orderItems);
	        }

	        return order;
	    }

	    private List<OrderItemDTO> getOrderItems(int orderID) throws SQLException {
	        Database db = new Database();
	        ArrayList<Object> vals = new ArrayList<>();
	        vals.add(orderID);

	        ResultSet rs = db.runSP("{CALL sp_orderitem_get_by_orderid(?)}", vals);
	        List<OrderItemDTO> orderItems = new ArrayList<>();

	        while (rs.next()) {
	            ProductDTO product = new ProductDTO(
	                rs.getInt("ProductID"),
	                rs.getString("ProductName"),
	                rs.getString("Description"),
	                rs.getDouble("Price"),
	                rs.getString("ImageURL"),
	                rs.getInt("CategoryID"),
	                rs.getString("CategoryDescription")
	            );

	            OrderItemDTO orderItem = new OrderItemDTO();
	            orderItem.setProduct(product);
	            orderItem.setQuantity(rs.getInt("Quantity"));
	            orderItem.setPrice(rs.getDouble("Price"));

	            orderItems.add(orderItem);
	        }

	        return orderItems;
	    }
	    
	    public List<OrderDTO> getOrderHistoryForUser(int userId) throws SQLException {
	        Database db = new Database();
	        ArrayList<Object> vals = new ArrayList<>();
	        vals.add(userId);

	        // Call the stored procedure to get the user's order history
	        ResultSet rs = db.runSP("{CALL sp_order_get_history_by_userid(?)}", vals);
	        List<OrderDTO> orderList = new ArrayList<>();

	        while (rs.next()) {
	            // Create OrderDTO object and populate it with data from the result set
	            OrderDTO order = new OrderDTO();
	            order.setOrderID(rs.getInt("OrderID"));
	            order.setOrderPlacedAt(rs.getString("OrderPlacedAt"));
	            order.setTotalAmount(rs.getDouble("TotalAmount"));

	            // Add the order to the list
	            orderList.add(order);
	        }

	        return orderList; // Return the list of orders
	    }
}
