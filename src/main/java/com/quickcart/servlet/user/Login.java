package com.quickcart.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Response;

/**
 * Servlet implementation class Login
 */

@WebServlet("/User/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Set response content type to JSON
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String userEmail = request.getParameter("email");
		String password = request.getParameter("password");
		UserManager userManager = new UserManager();

		// Use a PrintWriter to send the JSON response
		PrintWriter out = response.getWriter();

		if (userEmail != null && password != null && !userEmail.isEmpty() && !password.isEmpty()) {
			UserDTO user = null;

			try {

				// Call stored procedure to verify login
				ResultSet rs = userManager.getUserByEmail(userEmail);

				if (rs.next()) {
					// Get data from result set
					Integer userId = rs.getInt("UserID");
					String displayName = rs.getString("DisplayName");
					String passwordHash = rs.getString("PasswordHashedSalt");
					String passwordSalt = rs.getString("Salt");
					String email = rs.getString("Email");
					String phoneNumber = rs.getString("PhoneNumber");
					LocalDateTime createdAt = rs.getTimestamp("CreatedAt").toLocalDateTime();

					// Hash the input password with the salt
					String hashedInputPassword = userManager.hashPassword(password, passwordSalt); // Implement this method

					// Check if the hashed password matches
					if (hashedInputPassword.equals(passwordHash)) {
						// Map values to UserDTO
						user = new UserDTO();
						user.setUserId(userId);
						user.setDisplayName(displayName);
						user.setEmail(email);
						user.setPhoneNumber(phoneNumber);
						user.setCreatedAt(createdAt);

						// Add userDTO to session
						HttpSession session = request.getSession();
						session.setAttribute("userData", user);

						// Respond with success message
						Response.ResponseSuccess(response);

					} else {
						// Invalid password
						Response.ResponseError(response, "Invalid password");

					}
				} else {
					// User not found
					Response.ResponseError(response, "User not found");

				}
			} catch (SQLException e) {
				e.printStackTrace();
				Response.ResponseError(response, "Database error");

			}
		} else {
			// Missing user or password
			Response.ResponseError(response, "Email and password are required");

		}

		out.flush();
		out.close();
	}

}
