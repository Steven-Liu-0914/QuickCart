package com.quickcart.servlet.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import com.quickcart.general.Database;

public class UserManager {

	Database db = new Database();

	public boolean checkEmailExists(String email) {
		String sql = "SELECT COUNT(*) FROM User WHERE email = ?";
		ArrayList<Object> prepStmt = new ArrayList<>();
		prepStmt.add(email);

		ResultSet resultSet = null; // Declare ResultSet outside try block
		try {
			resultSet = db.getSQL(sql, prepStmt); // Get the ResultSet

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

	public ResultSet getUserByEmail(String email) {
		ArrayList<Object> vals = new ArrayList<>();
		vals.add(email);
		ResultSet rs = db.runSP("{CALL getUserByEmail(?)}", vals);
		return rs;

	}

	public boolean createUser(String displayName, String password, String email, String phoneNumber) {
		try {
			ArrayList<Object> prepStmt = new ArrayList<>();
			String salt = getSalt();
			String passwordHashedSalt = hashPassword(password, salt);

			String sql = "INSERT INTO User (DisplayName, PasswordHashedSalt, Salt, Email, PhoneNumber) VALUES (?,?,?,?,?)";

			prepStmt.add(displayName);
			prepStmt.add(passwordHashedSalt);
			prepStmt.add(salt);
			prepStmt.add(email);
			prepStmt.add(phoneNumber);

			db.updateSQL(sql, prepStmt);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateUser(String email, String newPassword) {
		try {
			ArrayList<Object> prepStmt = new ArrayList<>();
			String salt = getSalt();
			String passwordHashedSalt = hashPassword(newPassword, salt);

			String sql = "UPDATE User SET PasswordHashedSalt = ?, Salt = ? WHERE Email = ?";
			prepStmt.add(passwordHashedSalt);
			prepStmt.add(salt);
			prepStmt.add(email);

			// Use runSQL for the update statement
			db.updateSQL(sql, prepStmt);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean updateUserProfile(String email, String displayName, String phoneNumber) {
		try {
			ArrayList<Object> prepStmt = new ArrayList<>();
			
			

			String sql = "UPDATE User SET DisplayName = ?, PhoneNumber = ? WHERE Email = ?";
			prepStmt.add(displayName);
			prepStmt.add(phoneNumber);
			prepStmt.add(email);

			// Use runSQL for the update statement
			db.updateSQL(sql, prepStmt);
			return true;
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

	private static String getSalt() {
		Random r = new SecureRandom();
		byte[] saltBytes = new byte[32];
		r.nextBytes(saltBytes);
		return Base64.getEncoder().encodeToString(saltBytes);
	}
}
