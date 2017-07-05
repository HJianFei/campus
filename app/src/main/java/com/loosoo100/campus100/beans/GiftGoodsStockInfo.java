package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 商品各规格库存信息
 * 
 */
public class GiftGoodsStockInfo {
	// 属性ID
	private String id;
	// 属性编号
	private String attrSymbol;
	// 价格
	private float price;
	// 库存
	private int stock;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttrSymbol() {
		return attrSymbol;
	}

	public void setAttrSymbol(String attrSymbol) {
		this.attrSymbol = attrSymbol;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

}
