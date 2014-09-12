package com.normal.ordering.entities;

import java.util.ArrayList;


public class JsonBean {
	private String storeId;
	private boolean isBooking;
	private String userName;
	private ArrayList<Food> myOrderingList;
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public boolean isBooking() {
		return isBooking;
	}
	public void setBooking(boolean isBooking) {
		this.isBooking = isBooking;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ArrayList<Food> getMyOrderingList() {
		return myOrderingList;
	}
	public void setMyOrderingList(ArrayList<Food> myOrderingList) {
		this.myOrderingList = myOrderingList;
	}
	
	
}
