package com.loosoo100.campus100.card;

/**
 * 照片墙卡片信息
 * 
 */
public class PictureWallInfo {
	// 照片墙ID
	private String id;
	// 用户ID
	private String uid;
	// 用户所在学校ID
	private String sid;
	// 愿望
	private String dream;
	// 支持的人数
	private int supportCount;
	// 踩的人数
	private int opposeCount;
	// 姓名
	private String name;
	// 学校
	private String school;
	// 照片
	private String picture;
	// 头像
	private String headShot;
	// 性别
	private String userSex;
	// 发布时间
	private String time;
	//本人是否赞过或踩过
	private String action;
	//发布月份
	private String month;
	//发布日
	private String day;
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDream() {
		return dream;
	}

	public void setDream(String dream) {
		this.dream = dream;
	}

	public int getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}

	public int getOpposeCount() {
		return opposeCount;
	}

	public void setOpposeCount(int opposeCount) {
		this.opposeCount = opposeCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getHeadShot() {
		return headShot;
	}

	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
