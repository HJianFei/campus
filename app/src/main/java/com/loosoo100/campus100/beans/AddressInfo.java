package com.loosoo100.campus100.beans;

/**
 * 
 * @author yang 地址信息
 * 
 */
public class AddressInfo {
	// 地址ID
	private String addressId;
	// 用户ID
	private String userId;
	// 联系人姓名
	private String userName;
	// 联系人电话
	private String userPhone;
//	// 邮政编码
//	private String postcode;
	// 联系人地址
	private String address;
//	// 联系人性别(0代表男，1代表女)
//	private String sex;
	// 是否默认地址
	private String isDefault;
	// 学校
	private String school;
	// 学校ID
	private String sid;
	// 省
	private String province;
	// 市
	private String city;
	// 区
	private String area;
	// 省ID
	private String pid;
	// 市ID
	private String cid;
	// 区ID
	private String aid;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

//	public String getPostcode() {
//		return postcode;
//	}
//
//	public void setPostcode(String postcode) {
//		this.postcode = postcode;
//	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public String getSex() {
//		return sex;
//	}
//
//	public void setSex(String sex) {
//		this.sex = sex;
//	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
