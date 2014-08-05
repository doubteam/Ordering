package com.normal.ordering.entities;


public class TheDishes{
	
	
	private String goodsName;
	private String price;
	private String number;
	
	public TheDishes(String storeName, String price) {
		super();
		this.goodsName = storeName;
		this.price = price;
	}
	
	public TheDishes(String storeName, String price, String number) {
		super();
		this.goodsName = storeName;
		this.price = price;
		this.number = number;
	}
	public String getStoreName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
