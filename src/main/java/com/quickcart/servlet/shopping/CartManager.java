package com.quickcart.servlet.shopping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.quickcart.data.models.CartItem;
import com.quickcart.data.models.ProductDTO;
import com.quickcart.general.Database;

public class CartManager {

    private Database db;

    public CartManager() {
        db = new Database(); // Assuming the Database class handles connection pooling and execution of stored procedures.
    }

    // Get or create cart for the user
    public int getOrCreateCartIdForUser(int userId) throws SQLException {
        int cartId = -1;

        // First, try to get the CartID using the get stored procedure
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(userId);
        
        ResultSet rs = null;

        try {
            // Call the stored procedure to get CartID
            rs = db.runSP("{CALL sp_cart_get_by_user_id(?)}", vals);
            if (rs.next()) {
                // If a CartID exists, return it
                cartId = rs.getInt("CartID");
            }
        } finally {
            if (rs != null) rs.close();
        }

        // If no cart found, create a new one
        if (cartId == -1) {
            // Call the stored procedure to create a new cart
            ArrayList<Object> createVals = new ArrayList<>();
            createVals.add(userId);

            ResultSet rsCreate = null;
            try {
                rsCreate = db.runSP("{CALL sp_cart_create_for_user(?)}", createVals);
                if (rsCreate.next()) {
                    cartId = rsCreate.getInt("CartID");
                }
            } finally {
                if (rsCreate != null) rsCreate.close();
            }
        }

        return cartId;
    }

    // Add a product to the user's cart
    public boolean addProductToCart(int cartId, int productId) throws SQLException {
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(cartId);
        vals.add(productId);

        // Run the stored procedure
        db.runSP("{CALL sp_cartitem_add_product(?, ?)}", vals);

        // No result set expected, we assume the insert is successful
        return true;
    }

    // Get all products in the user's cart
    public List<CartItem> getCartItemsInCart(int cartId) throws SQLException {
        List<CartItem> cartItemList = new ArrayList<>();
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(cartId);

        // Call the stored procedure to get cart items
        ResultSet rs = db.runSP("{CALL sp_cart_get_products(?)}", vals);

        while (rs.next()) {
            // Create a new ProductDTO object
            ProductDTO product = new ProductDTO();
            product.setProductID(rs.getInt("ProductID"));
            product.setProductName(rs.getString("ProductName"));
            product.setDescription(rs.getString("Description"));
            product.setPrice(rs.getDouble("Price"));
            product.setImageURI(rs.getString("ImageURL"));

            // Get the quantity from the result set
            int quantity = rs.getInt("Quantity");

            // Create a new CartItem object
            CartItem cartItem = new CartItem(quantity, product);
            cartItemList.add(cartItem);
        }

        return cartItemList;
    }
    
    public boolean updateProductQuantityInCart(int cartId, int productId, int quantity) throws SQLException {
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(cartId);
        vals.add(productId);
        vals.add(quantity);

        // Call the stored procedure to update the quantity
        db.runSP("{CALL sp_cartitem_update_quantity(?, ?, ?)}", vals);

        // If no exceptions are thrown, assume the update is successful
        return true;
    }
    
    public boolean deleteProductFromCart(int cartId, int productId) throws SQLException {
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(cartId);
        vals.add(productId);

        // Call the stored procedure to delete the product from the cart
        db.runSP("{CALL sp_cartitem_delete(?, ?)}", vals);

        // If no exceptions are thrown, assume the deletion was successful
        return true;
    }
    
    
    // Method to clear the cart
    public void clearCart(int cartId) throws SQLException {
        Database db = new Database();
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(cartId);

        db.runSP("{CALL sp_cart_clear(?)}", vals);
    }
    
 // Method to create an order and add order items for the user
    public int createOrderForUser(int userId, List<CartItem> cartItems, double totalAmount) throws SQLException {
        Database db = new Database();
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(userId);
        vals.add(totalAmount);

        // Create an order and get the generated OrderID      
        int orderId = db.runSPWithUpdate("{CALL sp_order_create(?, ?, ?)}", vals, 3);
     
        // Insert each cart item as an order item
        for (CartItem item : cartItems) {
            ArrayList<Object> itemVals = new ArrayList<>();
            itemVals.add(orderId);
            itemVals.add(item.getProduct().getProductID());
            itemVals.add(item.getQuantity());
            itemVals.add(item.getProduct().getPrice());

            db.runSP("{CALL sp_orderitem_create(?, ?, ?, ?)}", itemVals);
        }

        return orderId; // Return the order ID for redirecting to the order details page
    }
}
