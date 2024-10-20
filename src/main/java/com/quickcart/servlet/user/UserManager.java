package com.quickcart.servlet.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

import com.quickcart.data.models.UserDTO;
import com.quickcart.general.Database;

public class UserManager {

	Database db = new Database();

	public boolean checkEmailExists(String email) {
	    String sql = "CALL sp_check_email_exists(?)"; // Using stored procedure
	    ArrayList<Object> prepStmt = new ArrayList<>();
	    prepStmt.add(email);

	    ResultSet resultSet = null; 
	    try {
	        resultSet = db.runSP(sql, prepStmt); // Get the ResultSet from the stored procedure

	        if (resultSet != null && resultSet.next()) {
	            return resultSet.getInt(1) > 0; // Check if email exists
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Always close the ResultSet in the finally block
	        if (resultSet != null) {
	            try {
	                resultSet.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return false;
	}

	
	 public UserDTO getUserById(int userId) throws SQLException {
	        Database db = new Database();
	        ArrayList<Object> vals = new ArrayList<>();
	        vals.add(userId);

	        ResultSet rs = db.runSP("{CALL sp_user_get_by_id(?)}", vals);
	        UserDTO user = null;

	        if (rs.next()) {
	            user = new UserDTO();
	            user.setUserId(rs.getInt("UserID"));
	            user.setDisplayName(rs.getString("DisplayName"));
	            user.setEmail(rs.getString("Email"));
	            user.setPhoneNumber(rs.getString("PhoneNumber"));
	            user.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
	        }

	        return user;
	    }

	  
	public ResultSet getUserByEmail(String email) {
		ArrayList<Object> vals = new ArrayList<>();
		vals.add(email);
		ResultSet rs = db.runSP("{CALL sp_user_get_by_email(?)}", vals);
		return rs;

	}

	public Boolean createUser(String displayName, String password, String email, String phoneNumber) {
	    try {
	        ArrayList<Object> prepStmt = new ArrayList<>();

	        // Generate salt and hash the password with salt
	        String salt = getSalt();
	        String passwordHashedSalt = hashPassword(password, salt);

	        String sql = "CALL sp_create_user(?, ?, ?, ?, ?, ?)";  // The last '?' is for the output parameter (new user ID)

	        // Adding input parameters for display name, password hash, salt, email, and phone number
	        prepStmt.add(displayName);
	        prepStmt.add(passwordHashedSalt);
	        prepStmt.add(salt);
	        prepStmt.add(email);
	        prepStmt.add(phoneNumber);

	        // Run stored procedure and capture the output parameter (new UserID)
	        int newUserId = db.runSPWithUpdate(sql, prepStmt, 6);  // The 6th parameter is the output

	        return newUserId >0;  // Return the new user ID

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;  // Return -1 or any error indicator
	    }
	}



	
	public boolean updateUserProfile(int userId, String displayName, String phoneNumber, String newPassword, String salt) {
	    try {
	        ArrayList<Object> prepStmt = new ArrayList<>();

	        // Prepare the stored procedure call
	        String sql = "CALL sp_update_user_profile_and_password(?, ?, ?, ?, ?, ?)";

	        // Adding parameters for user ID, display name, and phone number
	        prepStmt.add(userId);         
	        prepStmt.add(displayName);    
	        prepStmt.add(phoneNumber);

	        // If no new password is provided, pass null for both the password and salt
	        if (newPassword == null || newPassword.isEmpty()) {
	            prepStmt.add(null);    // No password update
	            prepStmt.add(null);    // No salt update
	        } else {
	            prepStmt.add(newPassword);  // Hashed password
	            prepStmt.add(salt);         // Generated salt
	        }

	        // Use runSP for executing the stored procedure
	        int Status = db.runSPWithUpdate(sql, prepStmt, 6);  // Use runSP if no OUT parameter is needed
	        
	        return Status>0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	public String hashPassword(String password, String salt) {
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

	public String getSalt() {
		Random r = new SecureRandom();
		byte[] saltBytes = new byte[32];
		r.nextBytes(saltBytes);
		return Base64.getEncoder().encodeToString(saltBytes);
	}
}
