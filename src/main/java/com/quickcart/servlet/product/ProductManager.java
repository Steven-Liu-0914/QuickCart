package com.quickcart.servlet.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.quickcart.data.models.OrderDTO;
import com.quickcart.general.Database;


public class ProductManager {
	Database db = new Database();
	
	public ResultSet getPrductDetailsById(int ProductId) {
		ArrayList<Object> vals = new ArrayList<>();
		vals.add(ProductId);
		ResultSet rs = db.runSP("{CALL sp_product_get_details_by_id(?)}", vals);
		return rs;
	}
	
	public ResultSet getPrductReviewListById(int ProductId) {
		ArrayList<Object> vals = new ArrayList<>();
		vals.add(ProductId);
		ResultSet rs = db.runSP("{CALL sp_product_review_list_by_product_id(?)}", vals);
		return rs;
	}
	
	public ResultSet getProductListing() 
	{
		ArrayList<Object> vals = new ArrayList<>();
		ResultSet rs = db.runSP("{CALL sp_product_get_listing()}", vals);
		return rs;
		
	}
	
	public OrderDTO getRecentOrderForProduct(int userId, int productId) throws SQLException {
        Database db = new Database();
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(userId); // Pass the userID
        vals.add(productId); // Pass the productID

        ResultSet rs = db.runSP("{CALL sp_recent_order_for_product(?, ?)}", vals);
        OrderDTO recentOrder = null;

        if (rs != null && rs.next()) {
            recentOrder = new OrderDTO();
            recentOrder.setOrderID(rs.getInt("OrderID"));
            recentOrder.setOrderPlacedAt(rs.getString("OrderPlacedAt"));    
        }

        return recentOrder;
    }
	
	
	public int addProductReview(int userId, int productId, int orderId, double rating, String comment) throws SQLException {
	    Database db = new Database();
	    ArrayList<Object> vals = new ArrayList<>();
	    vals.add(userId);
	    vals.add(productId);
	    vals.add(orderId);
	    vals.add(rating);
	    vals.add(comment);

	    // Call the stored procedure to insert a review, expecting the reviewID as the output
	    int reviewId = db.runSPWithUpdate("{CALL sp_review_insert(?, ?, ?, ?, ?, ?)}", vals, 6); // Assuming 6th parameter is the OUT parameter for reviewID

	    return reviewId;
	}


}
