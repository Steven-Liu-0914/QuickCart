package com.quickcart.data.models;

public class CartItem {
    private int quantity;    // Quantity of the product
    private ProductDTO product;    // The associated product

    public CartItem() {
    }

    public CartItem(int quantity, ProductDTO product) {
        this.quantity = quantity;
        this.product = product;
    }

    // Getters and Setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
