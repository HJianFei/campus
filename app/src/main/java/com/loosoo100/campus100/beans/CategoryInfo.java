package com.loosoo100.campus100.beans;

import android.graphics.Bitmap;

/**
 * 商品分类信息
 * 
 * @author yang
 * 
 */
public class CategoryInfo {
	// 商品分类图标
	private Bitmap categoryIcon;
	//分类ID
	private String catID;
	// 商品分类名
	private String categoryName;
	// 商品分类是否显示红条
	private boolean isShowRedline;
	// 店铺ID
	private String shopID;

	public Bitmap getCategoryIcon() {
		return categoryIcon;
	}

	public void setCategoryIcon(Bitmap categoryIcon) {
		this.categoryIcon = categoryIcon;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public boolean isShowRedline() {
		return isShowRedline;
	}

	public void setShowRedline(boolean isShowRedline) {
		this.isShowRedline = isShowRedline;
	}

	public String getCatID() {
		return catID;
	}

	public void setCatID(String catID) {
		this.catID = catID;
	}

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

}
