package com.quickcart.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Database;
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

		// Use a PrintWriter to send the JSON response
		PrintWriter out = response.getWriter();

		if (userEmail != null && password != null && !userEmail.isEmpty() && !password.isEmpty()) {
			Database db = new Database();
			UserDTO user = null;

			try {				
				// Call stored procedure to verify login
				ArrayList<Object> vals = new ArrayList<>();
				vals.add(userEmail);
				ResultSet rs = db.runSP("{CALL sp_user_verify_login(?)}", vals);

				if (rs.next()) {
					// Get data from result set
					String passwordHash = rs.getString("Password_Hash");
					String passwordSalt = rs.getString("Password_Salt");
					String displayName = rs.getString("DisplayName");
					String phoneNumber = rs.getString("PhoneNumber");
					LocalDateTime createdAt = rs.getTimestamp("CreatedAt").toLocalDateTime();

					// Hash the input password with the salt
					String hashedInputPassword = hashPassword(password, passwordSalt); // Implement this method

					// Check if the hashed password matches
					if (hashedInputPassword.equals(passwordHash)) {
						// Map values to UserDTO
						user = new UserDTO();
						user.setEmail(userEmail);
						user.setDisplayName(displayName);
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
				Response.ResponseError(response, "Database Error - "+ e.getMessage());
			}
		} else {
			// Missing user or password
			Response.ResponseError(response, "Email and password are required");	
		}

		out.flush();
		out.close();
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{		
		 // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.setStatus(HttpServletResponse.SC_OK);
	}
	
	
	private String hashPassword(String password, String salt) {
		try {
			// Combine password and salt
			String combined = password + salt;

			// Create a MessageDigest instance for SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Hash the combined string
			byte[] hashBytes = digest.digest(combined.getBytes());

			// Convert byte array to hexadecimal string
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0'); // Add leading zero for single digit
				}
				hexString.append(hex);
			}

			return hexString.toString(); // Return the hashed password
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hashing algorithm not found", e);
		}
	}
}
