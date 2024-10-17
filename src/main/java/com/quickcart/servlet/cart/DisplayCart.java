package com.quickcart.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jakartaee.commons.lang3.tuple.Pair;

import com.quickcart.data.models.ProductDTO;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Database;

/**
 * Servlet implementation class DisplayCart
 */
@WebServlet("/Cart/DisplayCart")
public class DisplayCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession(false); // Get existing session, do not create if it doesn't exist
		UserDTO user = (UserDTO) session.getAttribute("userData");
		System.out.println("Reached display servelet");
		Database db = new Database();
		List<ProductDTO> cartList = new ArrayList<>();
		
		String sql = "select p.productID,p.ProductName,p.Description,p.Price,p.ImageURL,p.CategoryID from CartItem i inner join Cart c on i.CartID = c.CartID\n"
				+ "inner join Product p on p.ProductID = i.ProductID where c.userID = ?;";
		ArrayList<Object> prepStmt = new ArrayList<>();
		
		ResultSet rs = null; // Declare ResultSet outside try block
		prepStmt.add(user.getUserId());
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
				cartList.add(product);   
			}
			
			System.out.println(cartList.size());

	        request.getSession().setAttribute("cartList", cartList);
	        request.getSession().setAttribute("cart-redirected",true);
	        request.getRequestDispatcher("../Shopping/ShoppingCart.jsp").forward(request, response);

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
