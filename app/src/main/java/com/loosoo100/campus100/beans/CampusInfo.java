package com.loosoo100.campus100.beans;

import android.graphics.Bitmap;

/**
 * 
 * @author yang
 *	搜索的学校信息
 */
public class CampusInfo {

	//学校ID
	private String campusID;
	//学校名字
	private String campusName;
	//学校小图标地址
	private String school_picthumb;
	//学校大图标地址
	private String school_pic;
	//学校图标
	private Bitmap campusIcon;
	
	
	public String getSchool_picthumb() {
		return school_picthumb;
	}
	public void setSchool_picthumb(String school_picthumb) {
		this.school_picthumb = school_picthumb;
	}
	public String getSchool_pic() {
		return school_pic;
	}
	public void setSchool_pic(String school_pic) {
		this.school_pic = school_pic;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public Bitmap getCampusIcon() {
		return campusIcon;
	}
	public void setCampusIcon(Bitmap campusIcon) {
		this.campusIcon = campusIcon;
	}
	public String getCampusID() {
		return campusID;
	}
	public void setCampusID(String campusID) {
		this.campusID = campusID;
	}
	
	
}
