package com.quickcart.servlet.product;

import java.sql.ResultSet;
import java.util.ArrayList;

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
}
