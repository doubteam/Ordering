package com.normal.ordering.entities;

public class DiscountFood {
	// 商店名称
	private String storeName;
	// 促销文本
	private String discountText;
	
	private String storeId;

	public DiscountFood(String storeName, String discountText,String storeId) {
		this.storeName = storeName;
		this.discountText = discountText;
		this.storeId=storeId;
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	
}
