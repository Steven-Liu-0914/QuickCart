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
import java.util.ArrayList;

import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Database;

/**
 * Servlet implementation class Register
 */
@WebServlet("/User/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set response content type to JSON
		
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
		String displayName = request.getParameter("displayName");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        boolean result = false ;
        boolean exists = false;
        
     // Use a PrintWriter to send the JSON response
        PrintWriter out = response.getWriter();
        
        try {
        	
        	
        	Database db = new Database();
        	exists = db.checkEmailExists(email);
        	if (exists != true) {
        		result = db.create(displayName,password,email,phoneNumber);	
        		UserDTO user = null;
	        	if (result) {
	        		try {
	                    // Call stored procedure to retrieve values
	        			ArrayList<Object> vals = new ArrayList<>();
	                    vals.add(email);
	                    ResultSet rs = db.runSP("{CALL getUserByEmail(?)}", vals);
	                    
	                    if (rs.next()) {
	                        // Get data from result set
	                    	Integer userId = rs.getInt("UserID");
	                        LocalDateTime createdAt = rs.getTimestamp("CreatedAt").toLocalDateTime();
	        		
			           	 	user = new UserDTO();
			           	 	user.setUserId(userId);
			                user.setEmail(email);
			                user.setDisplayName(displayName);
			                user.setPhoneNumber(phoneNumber);
			                user.setCreatedAt(createdAt);
			
			                // Add userDTO to session
			                HttpSession session = request.getSession();
			                session.setAttribute("userData", user);
			
			                // Respond with success message
			                out.write("{\"success\": true, \"message\": \"Registration successful\"}");
	                    }else {
	                    // User not found
	                    out.write("{\"success\": false, \"message\": \"User not found\"}");
	                    }
	        		} catch (SQLException e) {
	        		e.printStackTrace();
	        		out.write("{\"success\": false, \"message\": \"Database error\"}");
	        		}
	        	} else { 
	           	response.sendRedirect("Register.jsp");
	           	return;
	           }
        	}
	        else {
	        	out.write("{\"success\": false, \"message\": \"Email already exists in database\"}");
	        }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        out.flush();
        out.close();
	}
	
	   
}


