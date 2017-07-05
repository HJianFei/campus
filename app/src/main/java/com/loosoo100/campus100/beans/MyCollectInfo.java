package com.loosoo100.campus100.beans;

/**
 * 我的收藏
 * 
 * @author Administrator
 * 
 */
public class MyCollectInfo {
	//ID
	private String id;
	// 图标
	private String picUrl;
	// 名字
	private String name;
	// 活动分类（是否筹集）
	private int classify;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getClassify() {
		return classify;
	}

	public void setClassify(int classify) {
		this.classify = classify;
	}

}
