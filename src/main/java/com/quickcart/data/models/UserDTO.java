package com.quickcart.data.models;

import java.time.LocalDateTime;

public class UserDTO {
	private int userId; // User ID
	private String displayName; // Display name
	private String passwordHash; // Hashed password
	private String passwordSalt; // Salt used for hashing
	private String email; // User's email
	private String phoneNumber; // User's phone number
	private LocalDateTime createdAt; // Timestamp of record creation

	// Constructors
	public UserDTO() {
	}

	public UserDTO(int userId, String displayName) {
		this.userId = userId;
		this.displayName = displayName;
	}
	
	public UserDTO(int userId, String displayName, String passwordHash, String passwordSalt, String email,
			String phoneNumber, LocalDateTime createdAt) {
		this.userId = userId;
		this.displayName = displayName;
		this.passwordHash = passwordHash;
		this.passwordSalt = passwordSalt;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.createdAt = createdAt;
	}

	// Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}