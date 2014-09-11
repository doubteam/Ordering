package com.normal.ordering.entities;


public class TheDishes{
	
	
	private String goodsName;
	private String price;
	private String number;
	private String disheId;
	
	public TheDishes(String goodsName, String price,String dishesId) {
		super();
		this.goodsName = goodsName;
		this.price = price;
		this.disheId=dishesId;
	}
	
	public TheDishes(String goodsName, String price, String number,String dishesId) {
		super();
		this.goodsName = goodsName;
		this.price = price;
		this.number = number;
		this.disheId=dishesId;
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

	public String getDisheId() {
		return disheId;
	}

	public void setDisheId(String disheId) {
		this.disheId = disheId;
	}	
	
}
