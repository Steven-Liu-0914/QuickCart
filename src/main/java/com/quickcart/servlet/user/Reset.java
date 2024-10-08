package com.quickcart.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.quickcart.general.Response;

/**
 * Servlet implementation class Reset
 */
@WebServlet("/User/Reset")
public class Reset extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Reset() {
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
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String email = request.getParameter("email");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		PrintWriter out = response.getWriter();

		boolean result = false;
		boolean exists = false;

		if (email != null && newPassword != null && confirmPassword != null && !email.isEmpty()
				&& !newPassword.isEmpty() && !confirmPassword.isEmpty() && newPassword.equals(confirmPassword)) {

			try {

				exists = userManager.checkEmailExists(email);
				if (exists == true) {

					result = userManager.updateUser(email, newPassword);
					if (result) {
						Response.ResponseSuccess(response);

					}
				} else {
					// Email do not exist
					Response.ResponseError(response, "Invalid email");

				}
			} catch (Exception e) {
				e.printStackTrace();
				Response.ResponseError(response, "Database Error");
			}

		} else {
			Response.ResponseError(response, "Password does not match");
		}
		out.flush();
		out.close();
	}
}