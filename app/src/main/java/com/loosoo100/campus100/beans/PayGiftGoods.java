package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 礼物说下单商品信息
 * 
 */
public class PayGiftGoods {
	// 下单的商品Id
	private String goodsId;
	// 下单的商品编号
	private String giftgoodsSn;
	// 商品数量
	private int goodsNums;
	// 商品价格
	private float goodsPrice;
	// 商品属性ID
	private String goodsAttrId;
	// 商品属性名字
	private String goodsAttrName;
	// 商品名
	private String goodsName;
	// 商品缩略图
	private String goodsThums;
	// 商品库存ID
	private String skuId;

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

	public String getGoodsAttrId() {
		return goodsAttrId;
	}

	public void setGoodsAttrId(String goodsAttrId) {
		this.goodsAttrId = goodsAttrId;
	}

	public String getGoodsAttrName() {
		return goodsAttrName;
	}

	public void setGoodsAttrName(String goodsAttrName) {
		this.goodsAttrName = goodsAttrName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsThums() {
		return goodsThums;
	}

	public void setGoodsThums(String goodsThums) {
		this.goodsThums = goodsThums;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getGiftgoodsSn() {
		return giftgoodsSn;
	}

	public void setGiftgoodsSn(String giftgoodsSn) {
		this.giftgoodsSn = giftgoodsSn;
	}

}
