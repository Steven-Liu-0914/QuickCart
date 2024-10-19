package com.quickcart.data.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewDTO {
	
	private int ReviewID;
	private int UserID;
	private int ProductID;
	private int OrderID;
	private double Rating;
	private String Comment;
	private String CommentedAt;
	private String DisplayName;
	
	public ReviewDTO() {}

	public ReviewDTO(int reviewID, int userID, int productID, int orderID, double rating, String comment,
			String commentedAt) {
		this.ReviewID = reviewID;
		this.UserID = userID;
		this.ProductID = productID;
		this.OrderID = orderID;
		this.Rating = rating;
		this.Comment = comment;
		this.CommentedAt = commentedAt;
	}

	public int getReviewID() {
		return ReviewID;
	}

	public void setReviewID(int reviewID) {
		ReviewID = reviewID;
	}
	
	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		this.DisplayName = displayName;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}

	public int getOrderID() {
		return OrderID;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public double getRating() {
		return Rating;
	}

	public void setRating(double rating) {
		Rating = rating;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public String getCommentedAt() {
		return CommentedAt;
	}

	public void setCommentedAt(LocalDateTime commentedAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedCommentedAt = commentedAt.format(formatter);
		
		this.CommentedAt = formattedCommentedAt;

	}
}