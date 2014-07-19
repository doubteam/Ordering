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
	public String getMerchantAddress() {
		return storeAddress;
	}
	public void setMerchantAddress(String merchantAddress) {
		this.storeAddress = merchantAddress;
	}
	public String getMerchantName() {
		return storeName;
	}
	public void setMerchantName(String merchantName) {
		this.storeName = merchantName;
	}
	public String getMerchantIp() {
		return storeIp;
	}
	public void setMerchantIp(String merchantIp) {
		this.storeIp = merchantIp;
	}
	
	
	
}
