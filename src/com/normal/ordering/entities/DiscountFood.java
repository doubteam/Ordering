package com.normal.ordering.entities;

public class DiscountFood{


	private String storeName;
	private String discountText;
	public DiscountFood(String storeName,String discountText){
		this.storeName=storeName;
		this.discountText=discountText;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getDiscountText() {
		return discountText;
	}
	public void setDiscountText(String discountText) {
		this.discountText = discountText;
	}
	
}
