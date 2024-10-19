package com.quickcart.general;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.io.InputStream;
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
    public ResultSet getSQL(String sql, ArrayList<Object> prepStmt) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql); // Do not use try-with-resources here
            // Loop through the list of values and set them to the PreparedStatement
            for (int i = 0; i < prepStmt.size(); i++) {
                pstmt.setObject(i + 1, prepStmt.get(i));  // i+1 because JDBC parameter indices start from 1
            }

            // Execute the query and return the result set
            return pstmt.executeQuery(); // Return ResultSet to caller
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public void updateSQL(String sql, ArrayList<Object> prepStmt) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            // Loop through the list of values and set them to the PreparedStatement
            for (int i = 0; i < prepStmt.size(); i++) {
                pstmt.setObject(i + 1, prepStmt.get(i));  // i+1 because JDBC parameter indices start from 1
            }
            
            // Execute the query and return the result set
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
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

    public int runSPWithUpdate(String sql, ArrayList<Object> vals, int outputParamIndex) throws SQLException {
        int outputValue = -1;
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

            // Register output parameter for stored procedures that have OUT parameters
            stmt.registerOutParameter(outputParamIndex, java.sql.Types.INTEGER);

            // Execute the stored procedure (use executeUpdate for non-select queries)
            stmt.executeUpdate();

            // Get the output parameter value (e.g., orderID)
            outputValue = stmt.getInt(outputParamIndex);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error executing stored procedure: " + e.getMessage());
        }

        return outputValue;  // Return the output parameter value (e.g., orderID)
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
    
    
	
}
