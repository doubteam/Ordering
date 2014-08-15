package com.normal.ordering.entities;

public class Store {

	private String storeAddress;
	private String storeName;
	private String storeId;
	public Store(String storeAddress, String storeName,String storeId) {
		super();
		this.storeId=storeId;
		this.storeAddress = storeAddress;
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreIp(String storeId) {
		this.storeId = storeId;
	}
	
	
	
}
