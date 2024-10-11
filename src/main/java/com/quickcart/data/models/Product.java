package com.quickcart.data.models;

public class Product {
		
	private int ProductID;
	private String ProductName;
	private String Description;
	private Double Price;
	private String imageURI;
	private int CategoryID;
	
	public Product() {}
	
	public Product(int productID, String productName, String description, Double price, String imageURI,
			int categoryID) {
		this.ProductID = productID;
		this.ProductName = productName;
		this.Description = description;
		this.Price = price;
		this.imageURI = imageURI;
		this.CategoryID = categoryID;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		this.ProductID = productID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Double getPrice() {
		return Price;
	}

	public void setPrice(Double price) {
		Price = price;
	}

	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}

	public int getCategoryID() {
		return CategoryID;
	}

	public void setCategoryID(int categoryID) {
		CategoryID = categoryID;
	}

	
}