package com.quickcart.data.models;

public class OrderItemDTO {

    private ProductDTO product;
    private int quantity;
    private double price;

    public OrderItemDTO() {}

    public OrderItemDTO(ProductDTO product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
