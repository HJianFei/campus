package com.loosoo100.campus100.beans;


/**
 * 
 * @author yang 礼物说订单商品信息
 * 
 */
public class GiftOrderGoodsInfo {
	// 商品ID
	private String goodsId;
	// 商品属性ID
	private String goodsAttrId;
	// 商品名
	private String goodsName;
	// 商品属性名
	private String goodsAttrName;
	// 图片地址
	private String goodsThums;
	// 价格
	private float goodsPrice;
	// 商品数量
	private int goodsNums;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsAttrId() {
		return goodsAttrId;
	}

	public void setGoodsAttrId(String goodsAttrId) {
		this.goodsAttrId = goodsAttrId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsAttrName() {
		return goodsAttrName;
	}

	public void setGoodsAttrName(String goodsAttrName) {
		this.goodsAttrName = goodsAttrName;
	}

	public String getGoodsThums() {
		return goodsThums;
	}

	public void setGoodsThums(String goodsThums) {
		this.goodsThums = goodsThums;
	}

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsNums() {
		return goodsNums;
	}

	public void setGoodsNums(int goodsNums) {
		this.goodsNums = goodsNums;
	}

}
