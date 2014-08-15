package com.normal.ordering.entities;


public class TheDishes{
	
	
	private String goodsName;
	private String price;
	private String number;
	
	public TheDishes(String goodsName, String price) {
		super();
		this.goodsName = goodsName;
		this.price = price;
	}
	
	public TheDishes(String goodsName, String price, String number) {
		super();
		this.goodsName = goodsName;
		this.price = price;
		this.number = number;
	}
	public String getGoodsName() {
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
