package com.quickcart.servlet;

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

import com.quickcart.data.models.ProductDTO;
import com.quickcart.general.Database;

/**
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Reached home servelet");
		Database db = new Database();
		List<ProductDTO> productList = new ArrayList<>();
		
		String sql = "Select * From Product;";
		ArrayList<Object> prepStmt = new ArrayList<>();

		ResultSet rs = null; // Declare ResultSet outside try block
		try {
			rs = db.getSQL(sql, prepStmt); // Get the ResultSet
			while(rs.next()) {
				int productID = rs.getInt("ProductID");
				String productName = rs.getString("ProductName");
				String description = rs.getString("Description");
				Double price = rs.getDouble("Price");
				String imageURI = rs.getString("ImageURL");
				int categoryID = rs.getInt("CategoryID");

				// Instantiate a Product object
				ProductDTO product = new ProductDTO(productID, productName, description, price, imageURI, categoryID);
				productList.add(product);        	
			}
			System.out.println("Loaded "+ productList.size() +" products from the database.");
	        request.getSession().setAttribute("dataList", productList);
	        request.getSession().setAttribute("redirected",true);
	        request.getRequestDispatcher("Home.jsp").forward(request, response);


			// Respond with success message		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			if(rs!=null)
			{
		        try {
		            rs.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
			}
			
		}

	}

}
