package com.quickcart.data.models;

import java.util.List;

public class OrderDTO {
    
    private int orderID;
    private UserDTO user;  // User who placed the order
    private String orderPlacedAt;  // Timestamp of when the order was placed
    private double totalAmount;  // Total amount for the order
    private List<OrderItemDTO> orderItems;  // List of items in the order

    // Constructors
    public OrderDTO() {}

    public OrderDTO(int orderID, UserDTO user, String orderPlacedAt, double totalAmount, List<OrderItemDTO> orderItems) {
        this.orderID = orderID;
        this.user = user;
        this.orderPlacedAt = orderPlacedAt;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getOrderPlacedAt() {
        return orderPlacedAt;
    }

    public void setOrderPlacedAt(String orderPlacedAt) {
        this.orderPlacedAt = orderPlacedAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
