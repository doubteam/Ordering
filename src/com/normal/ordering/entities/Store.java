package com.normal.ordering.entities;

public class Store {

	private String storeAddress;
	private String storeName;
	private String storeIp;
	public Store(String merchantAddress, String merchantName) {
		super();
	//	this.merchantIp=merchantIp;
		this.storeAddress = merchantAddress;
		this.storeName = merchantName;
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
	public String getStoreIp() {
		return storeIp;
	}
	public void setStoreIp(String storeIp) {
		this.storeIp = storeIp;
	}
	
	
	
}
