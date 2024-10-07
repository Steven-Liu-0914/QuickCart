package com.quickcart.general;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.io.IOException;

public class Database {
    private Connection connection;
    private boolean conFree = true;

    public Database() {
    	
    	 try {
    	     Class.forName("com.mysql.cj.jdbc.Driver"); // Use com.mysql.jdbc.Driver if you're not on MySQL 8+ yet.
    	     System.out.println("Driver loaded!");
    	 } catch (ClassNotFoundException e) {
    	     throw new IllegalStateException("Cannot find the driver in the classpath!", e);
    	 }
    	 
        try {       	
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (input != null) {
                props.load(input);
                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");
                
                connection = DriverManager.getConnection(url, username, password);
            } else {
                throw new IOException("Configuration file not found");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

 // Method to run SQL queries (e.g., SELECT queries) and return a ResultSet
    public ResultSet runSQL(String sql) {
        try (Statement stmt = connection.createStatement()) {
            // Execute the query and return the result set
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to run a stored procedure with parameters and return a ResultSet
    public ResultSet runSP(String sql, ArrayList<Object> vals) {
        try {
            CallableStatement stmt = connection.prepareCall(sql);
            
            // Set the parameters for the stored procedure
            for (int i = 0; i < vals.size(); i++) {
                if (vals.get(i) instanceof String) {
                    stmt.setString(i + 1, (String) vals.get(i));
                } else if (vals.get(i) instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) vals.get(i));
                } else if (vals.get(i) instanceof Double) {
                    stmt.setDouble(i + 1, (Double) vals.get(i));
                }
                // Add more types if necessary
            }
            
            // Execute the stored procedure and return the result set
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected synchronized Connection getConnection() {
        while (conFree == false) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        
        conFree = false;
        notify();
        
        return connection;
    }
    
    protected synchronized void releaseConnection() {
        while (conFree == true) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        
        conFree = true;
        notify();
    }
    
    public boolean checkEmailExists(String email) {
    
       String sql = "SELECT COUNT(*) FROM User WHERE email = ?";

       try {
           PreparedStatement prepStmt = connection.prepareStatement(sql);
             
            prepStmt.setString(1, email);
            ResultSet resultSet = prepStmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    
    
    public boolean create(String displayName, String password, String email, String phoneNumber)throws NoSuchAlgorithmException {
		boolean status = false;
	try{
		String salt = getSalt();
		String passwordHashedSalt = hashPassword(password, salt); 
		
		String sqlStatement = "INSERT INTO User (DisplayName, PasswordHashedSalt, Salt, Email, PhoneNumber) VALUES (?,?,?,?,?);";
		getConnection();
	
		PreparedStatement prepStmt = connection.prepareStatement(sqlStatement);
		prepStmt.setString(1, displayName);
		prepStmt.setString(2, passwordHashedSalt);
		prepStmt.setString(3, salt);
		prepStmt.setString(4, email);
		prepStmt.setString(5, phoneNumber);
		
		int x = prepStmt.executeUpdate();
		
		if (x==1) {
			status = true;
		}
		
		prepStmt.close();
		releaseConnection();
		
		}catch (SQLException ex) {
			releaseConnection();
			ex.printStackTrace();
	
	}
	return status;
	}
    
    public boolean update(String email, String newPassword)throws NoSuchAlgorithmException {
		boolean status = false;
	try{
		
		String salt = getSalt();
		String passwordHashedSalt = hashPassword(newPassword, salt);
		
		String sqlStatement = "UPDATE User SET PasswordHashedSalt = ?, Salt = ? where Email = ?";
		getConnection();
	
		PreparedStatement prepStmt = connection.prepareStatement(sqlStatement);
		prepStmt.setString(1, passwordHashedSalt);
		prepStmt.setString(2, salt);
		prepStmt.setString(3, email);
		
		int rowsUpdated = prepStmt.executeUpdate();
		
		if (rowsUpdated == 1) {
			status = true;
		}
		
		prepStmt.close();
		releaseConnection();
		
		}catch (SQLException ex) {
			releaseConnection();
			ex.printStackTrace();
	
	}
	return status;
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
