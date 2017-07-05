package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 校园超市下单商品信息
 * 
 */
public class PayStoreGoods {
	// 下单的商品Id
	private String goodsId;
	// 商品数量
	private int goodsNums;
	// 商品价格
	private float goodsPrice;
	// 商品缩略图
	private String goodsThums;
	// 商品名
	private String goodsName;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsNums() {
		return goodsNums;
	}

	public void setGoodsNums(int goodsNums) {
		this.goodsNums = goodsNums;
	}

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsThums() {
		return goodsThums;
	}

	public void setGoodsThums(String goodsThums) {
		this.goodsThums = goodsThums;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


}
