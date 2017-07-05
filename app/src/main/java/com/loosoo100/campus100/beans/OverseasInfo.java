package com.loosoo100.campus100.beans;

import java.util.List;

import android.graphics.Bitmap;

/**
 * 
 * @author yang 海归学校信息
 * 
 */
public class OverseasInfo {
	// 大学ID
	private String schoolID;
	// 大学名称
	private String schoolName;
	// 大学英文名称
	private String schoolNameENG;
	// 大约费用
	private String fee;
	// 收藏人数
	private long collectCount;
	// 入学难度系数
	private int enterSchoolDifficulty;
	// 学生满意度
	private int satisfaction;
	// 毕业生就业前景
	private int future;
	// 校园100评估
	private int ourAppraise;
	// 学校图片
	private String picUrl;
	// 学校图片
	private String picUrl01;
	// 学校图片
	private String picUrl02;
	// 学校图片
	private String picUrl03;
	// 学校图片
	private Bitmap schoolPicture;
	// 学校地址(国家)
	private String location;
	// 是否已收藏
	private int status; // 1已收藏 0没收藏
	// 图片集(详情)
	private List<String> picList;
	//详情文本
	private String overLetter;

	public String getPicUrl01() {
		return picUrl01;
	}

	public void setPicUrl01(String picUrl01) {
		this.picUrl01 = picUrl01;
	}

	public String getPicUrl02() {
		return picUrl02;
	}

	public void setPicUrl02(String picUrl02) {
		this.picUrl02 = picUrl02;
	}

	public String getPicUrl03() {
		return picUrl03;
	}

	public void setPicUrl03(String picUrl03) {
		this.picUrl03 = picUrl03;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public int getEnterSchoolDifficulty() {
		return enterSchoolDifficulty;
	}

	public void setEnterSchoolDifficulty(int enterSchoolDifficulty) {
		this.enterSchoolDifficulty = enterSchoolDifficulty;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

	public int getFuture() {
		return future;
	}

	public void setFuture(int future) {
		this.future = future;
	}

	public int getOurAppraise() {
		return ourAppraise;
	}

	public void setOurAppraise(int ourAppraise) {
		this.ourAppraise = ourAppraise;
	}

	public Bitmap getSchoolPicture() {
		return schoolPicture;
	}

	public void setSchoolPicture(Bitmap schoolPicture) {
		this.schoolPicture = schoolPicture;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSchoolNameENG() {
		return schoolNameENG;
	}

	public void setSchoolNameENG(String schoolNameENG) {
		this.schoolNameENG = schoolNameENG;
	}

	public long getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(long collectCount) {
		this.collectCount = collectCount;
	}

	public String getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public String getOverLetter() {
		return overLetter;
	}

	public void setOverLetter(String overLetter) {
		this.overLetter = overLetter;
	}

}
