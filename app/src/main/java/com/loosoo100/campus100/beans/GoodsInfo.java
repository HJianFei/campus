package com.loosoo100.campus100.beans;

import java.util.List;

import android.graphics.Bitmap;

/**
 * 
 * @author yang 商品信息
 * 
 */
public class GoodsInfo {
	// // 商品所属类目
	// private String category;
	// 类目ID
	private String catID;
	// 商品分类是否显示红条
	private boolean isShowRedline;
	// 商品名称
	private String goodsName;
	// 商品图标
	private Bitmap goodsIcon;
	// 商品图标集
	private List<Bitmap> goodsIconList;
	// 商品原价
	private float originalPrice;
	// 商品现价
	private float currentPrice;
	// 商品数量
	private int count;
	// 商品单位
	private String unit;
	// 商品正常状态或添加状态
	private boolean isAdding = false;
	// 商品是否已经添加收藏
	private boolean isCollected = false;
	// 配送费
	private float expressFee;
	// 已售出
	private int sold;
	// 收藏人数
	private int collection;
	// 商品库存
	private int stock;
	// 商品规格
	private List<String> standard;
	// 商品规格
	private List<String> colors;
	// 商品缩略图
	private String goodsIconUrl;
	// 商品详情图1
	private String goodsDetailUrl01;
	// 商品详情图2
	private String goodsDetailUrl02;
	// 商品详情图3
	private String goodsDetailUrl03;
	// 商品ID
	private String goodsID;

	public String getGoodsDetailUrl01() {
		return goodsDetailUrl01;
	}

	public void setGoodsDetailUrl01(String goodsDetailUrl01) {
		this.goodsDetailUrl01 = goodsDetailUrl01;
	}

	public String getGoodsDetailUrl02() {
		return goodsDetailUrl02;
	}

	public void setGoodsDetailUrl02(String goodsDetailUrl02) {
		this.goodsDetailUrl02 = goodsDetailUrl02;
	}

	public String getGoodsDetailUrl03() {
		return goodsDetailUrl03;
	}

	public void setGoodsDetailUrl03(String goodsDetailUrl03) {
		this.goodsDetailUrl03 = goodsDetailUrl03;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public List<Bitmap> getGoodsIconList() {
		return goodsIconList;
	}

	public void setGoodsIconList(List<Bitmap> goodsIconList) {
		this.goodsIconList = goodsIconList;
	}

	public float getExpressFee() {
		return expressFee;
	}

	public void setExpressFee(float expressFee) {
		this.expressFee = expressFee;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public int getCollection() {
		return collection;
	}

	public void setCollection(int collection) {
		this.collection = collection;
	}

	public List<String> getStandard() {
		return standard;
	}

	public void setStandard(List<String> standard) {
		this.standard = standard;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isAdding() {
		return isAdding;
	}

	public void setAdding(boolean isAdding) {
		this.isAdding = isAdding;
	}

	public boolean isShowRedline() {
		return isShowRedline;
	}

	public void setShowRedline(boolean isShowRedline) {
		this.isShowRedline = isShowRedline;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	// public String getCategory() {
	// return category;
	// }
	//
	// public void setCategory(String category) {
	// this.category = category;
	// }

	public Bitmap getGoodsIcon() {
		return goodsIcon;
	}

	public void setGoodsIcon(Bitmap goodsIcon) {
		this.goodsIcon = goodsIcon;
	}

	public float getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(float originalPrice) {
		this.originalPrice = originalPrice;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getGoodsIconUrl() {
		return goodsIconUrl;
	}

	public void setGoodsIconUrl(String goodsIconUrl) {
		this.goodsIconUrl = goodsIconUrl;
	}

	public String getGoodsID() {
		return goodsID;
	}

	public void setGoodsID(String goodsID) {
		this.goodsID = goodsID;
	}

	public String getCatID() {
		return catID;
	}

	public void setCatID(String catID) {
		this.catID = catID;
	}

}
