package com.quickcart.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

import com.quickcart.data.models.UserDTO;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/User/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // Get existing session, do not create if it doesn't exist
        
        if (session != null) {
            // Retrieve user data from session
            UserDTO user = (UserDTO) session.getAttribute("userData");
            
            // Check if user data is available
            if (user != null) {
            	Integer userId = user.getUserId();
            	String displayName = user.getDisplayName();
                String email = user.getEmail();
                String phoneNumber = user.getPhoneNumber();
                LocalDateTime createdAt = user.getCreatedAt();
                
                // Define a formatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                // Format the createdAt 
                String formattedCreatedAt = createdAt.format(formatter);
                
                // Set user data as request attributes
                request.setAttribute("userId", userId);
                request.setAttribute("displayName", displayName);
                request.setAttribute("email", email);
                request.setAttribute("phoneNumber", phoneNumber);
                request.setAttribute("createdAt", formattedCreatedAt);
                
                // Forward to the JSP page
                request.getRequestDispatcher("Profile.jsp").forward(request, response);
            } else {
                response.sendRedirect("Login.jsp"); // Redirect to login if session is valid but no user data
            }
        } else {
            response.sendRedirect("Login.jsp"); // Redirect to login if no session exists
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
