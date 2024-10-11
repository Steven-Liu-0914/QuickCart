package com.quickcart.servlet.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.quickcart.data.models.Product;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Database;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Database db = new Database();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Cart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false); // Get existing session, do not create if it doesn't exist
		UserDTO user = (UserDTO) session.getAttribute("userData");
		String productId = request.getParameter("productId");

		if (user == null) {
			response.sendRedirect("Login.jsp"); // Redirect to login page
			return;
		}
		int cartId = returnCartId(user.getUserId());
		System.out.println(cartId);
		response.sendRedirect("Home.jsp");

	}

	public void addUserToCart(int userId, int cartId) {

		String sql = "Insert into Cart Values(?,?)";
		System.out.println(sql);
		ArrayList<Object> prepStmt = new ArrayList<>();
		ResultSet rs = null;

		prepStmt.add(cartId);
		prepStmt.add(userId);

		try {
			db.updateSQL(sql, prepStmt);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int returnCartId(int userId) {
		String sql = "SELECT * FROM Cart WHERE UserID = ?";
		String maxCountSql = "SELECT Max(cartid) FROM Cart;";

		ArrayList<Object> prepStmt = new ArrayList<>();
		ArrayList<Object> prepStmtForMaxCount = new ArrayList<>();

		prepStmt.add(userId);

		ResultSet rs = null;
		ResultSet countRs = null;
		int cartId = 0;

		try {
			rs = db.getSQL(sql, prepStmt);
			countRs = db.getSQL(maxCountSql, prepStmtForMaxCount);

			while (countRs.next()) {
				cartId = countRs.getInt(1);
			}
			if (!rs.next()) {
				System.out.println("User not in cart");
				if (!countRs.next()) {
					cartId = 1;
					System.out.println("Cart is empty");

				} else {
					while (countRs.next()) {
						cartId = countRs.getInt(1);
						System.out.println("Cart is not empty");

					}

				}			
				addUserToCart(userId, cartId);


			} else {
				while (rs.next()) {
					cartId = rs.getInt("CartId");
					System.out.println(cartId);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Always close the ResultSet in the finally block
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return cartId;
	}

}
