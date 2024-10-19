package com.quickcart.servlet.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.quickcart.data.models.ProductDTO;
import com.quickcart.general.Response;

/**
 * Servlet implementation class ProductListing
 */
@WebServlet("/Product/Listing")
public class ProductListing extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductListing() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductManager productManager = new ProductManager();
        List<ProductDTO> productList = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = productManager.getProductListing();

            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                Double price = rs.getDouble("Price");
                String imageURI = rs.getString("ImageURL");
                int categoryID = rs.getInt("CategoryID");
                String categoryDescription = rs.getString("CategoryDescription");

                // Instantiate a ProductDTO object
                ProductDTO product = new ProductDTO(productID, productName, description, price, imageURI, categoryID,categoryDescription);
                productList.add(product);
            }

            // Success response
            Response.ResponseSuccess(response, productList);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception and send error response
            Response.ResponseError(response, "Error retrieving product listing.");
        } finally {
            // Ensure that the ResultSet is closed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle exception during resource closing
                    Response.ResponseError(response, "Error closing database resources.");
                }
            }
        }
    }
}
