package com.quickcart.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import com.quickcart.data.models.UserDTO;
import com.quickcart.general.AESDecryptor;
import com.quickcart.general.Response;

/**
 * Servlet implementation class UpdateProfile
 */
@WebServlet("/User/UpdateProfile")
public class UpdateProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserManager userManager = new UserManager();

		// Retrieve parameters from the request
		String newDisplayName = request.getParameter("newDisplayName");
		String newPhoneNumber = request.getParameter("newPhoneNumber");
		String newPassword = request.getParameter("newPassword"); // New Password

		try {
			if(newPassword!=null&&!newPassword.isEmpty()) {
			newPassword = AESDecryptor.decryptPassword(newPassword);}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		boolean result = false;

		// Retrieve user data from the session
		HttpSession session = request.getSession(false);
		UserDTO userData = (UserDTO) session.getAttribute("userData");

		if (userData != null && newDisplayName != null && newPhoneNumber != null && !newDisplayName.isEmpty()
				&& !newPhoneNumber.isEmpty()) {
			try {
				// Get userId from session
				int userId = userData.getUserId();

				// Generate new salt and hash password if a new password is provided
				String salt = null;
				String passwordHashedSalt = null;
				if (newPassword != null && !newPassword.isEmpty()) {
					salt = userManager.getSalt();
					passwordHashedSalt = userManager.hashPassword(newPassword, salt);
				}

				// Update user profile
				result = userManager.updateUserProfile(userId, newDisplayName, newPhoneNumber, passwordHashedSalt,
						salt);

				if (result) {
					// Fetch updated user details from DB
					UserDTO user = userManager.getUserById(userId);

					// Add updated user data to session
					session.setAttribute("userData", user);

					// Send success response
					Response.ResponseSuccess(response);

				} else {
					Response.ResponseError(response, "Failed to update profile.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Response.ResponseError(response, "Database Error");
			}
		} else {
			// If required fields are missing
			Response.ResponseError(response, "Invalid request: Missing profile data.");
		}

		out.flush();
		out.close();
	}

}
