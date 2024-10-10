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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
    	HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("userData"); // Get user from session
        
        if (user == null) {
        	response.sendRedirect("User/Login.jsp"); // Redirect if no user is logged in
            return;
        }
        
        request.setAttribute("userData", user); // Set user as a request attribute
        request.getRequestDispatcher("User/UpdateProfile.jsp").forward(request, response);
    }// Forward to JSP
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserManager userManager = new UserManager();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String email = request.getParameter("email");
		String newDisplayName = request.getParameter("newDisplayName");
		String newPhoneNumber = request.getParameter("newPhoneNumber");

		PrintWriter out = response.getWriter();

		boolean result = false;
		boolean exists = false;

		if (newDisplayName != null && newPhoneNumber != null  && !newDisplayName.isEmpty() && !newPhoneNumber.isEmpty()) {

			try {

				exists = userManager.checkEmailExists(email);
				if (exists == true) {

					result = userManager.updateUserProfile(email, newDisplayName,newPhoneNumber);
					if (result) {
						HttpSession session = request.getSession();
						session.invalidate();
						Response.ResponseSuccess(response);

					}
				} else {
					// Email do not exist
					Response.ResponseError(response, "Error fetching email address");

				}
			} catch (Exception e) {
				e.printStackTrace();
				Response.ResponseError(response, "Database Error");
			}

		} else {
			Response.ResponseError(response, "Invalid request");
		}
		out.flush();
		out.close();
	}
	

}
