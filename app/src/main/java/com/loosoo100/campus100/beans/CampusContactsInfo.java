package com.loosoo100.campus100.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yang 校园圈信息
 * 
 */
public class CampusContactsInfo {
	// 圈圈ID
	private String id;
	// 圈圈用户ID
	private String userId;
	// 头像
	private String headShot;
	// 性别
	private String sex;
	// 用户名
	private String name;
	// 发表的内容
	private String content;
	// 发表所在地址
	private String location;
	// 发表时间
	private long time;
	// 发表时间月
	private String month;
	// 发表时间日
	private String day;
	// 赞的人数
	private int likeCount;
	// 是否已赞
	private int isLiked;
	// 权限
	private int permission;
	// 是否显示评论和赞的视图
	private boolean isShowAppraise;
	// 评论是否显示完整
	private boolean isShowAll;
	// 评论列表
	private List<CampusContactsReplyInfo> appraiseList;
	// 评论列表(废弃的)
	private List<Map<String, Object>> appraiseListMap;
	// 所在学校
	private String school;
	// 图片集小
	private List<String> picListThum;
	// 图片集大
	private ArrayList<String> picList;
	//学校ID
	private String sid;

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

	public List<CampusContactsReplyInfo> getAppraiseList() {
		return appraiseList;
	}

	public void setAppraiseList(List<CampusContactsReplyInfo> appraiseList) {
		this.appraiseList = appraiseList;
	}

	public boolean isShowAll() {
		return isShowAll;
	}

	public void setShowAll(boolean isShowAll) {
		this.isShowAll = isShowAll;
	}

	public boolean isShowAppraise() {
		return isShowAppraise;
	}

	public void setShowAppraise(boolean isShowAppraise) {
		this.isShowAppraise = isShowAppraise;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public ArrayList<String> getPicList() {
		return picList;
	}

	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}

	public String getHeadShot() {
		return headShot;
	}

	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}

	public List<String> getPicListThum() {
		return picListThum;
	}

	public void setPicListThum(List<String> picListThum) {
		this.picListThum = picListThum;
	}

	public List<Map<String, Object>> getAppraiseListMap() {
		return appraiseListMap;
	}

	public void setAppraiseListMap(List<Map<String, Object>> appraiseListMap) {
		this.appraiseListMap = appraiseListMap;
	}

	public int getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(int isLiked) {
		this.isLiked = isLiked;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
