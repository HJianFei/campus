package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 礼物说分类信息
 * 
 */
public class GiftCategoryInfo {
	// 一级类目名
	private String categoryFirst;
	// 二级类目名
	private String categorySecond;
	// 三级类目名
	private String categoryThird;
	// 三级类目ID
	private String id;
	// 三级类目图片地址
	private String picUrl;

	public String getCategoryFirst() {
		return categoryFirst;
	}

	public void setCategoryFirst(String categoryFirst) {
		this.categoryFirst = categoryFirst;
	}

	public String getCategorySecond() {
		return categorySecond;
	}

	public void setCategorySecond(String categorySecond) {
		this.categorySecond = categorySecond;
	}

	public String getCategoryThird() {
		return categoryThird;
	}

	public void setCategoryThird(String categoryThird) {
		this.categoryThird = categoryThird;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
