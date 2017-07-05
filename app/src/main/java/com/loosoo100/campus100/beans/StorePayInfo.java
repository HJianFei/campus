package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * 
 * @author yang 礼物说支付信息
 * 
 */
public class StorePayInfo {
	private float totalMoney;
	private int isPay;
	private int needType;
	// 用户Id
	private String userId;
	private String addressId;
	private String userName;
	private String userAddress;
	private String userPhone;

	private List<PayStoreGoods> list;

	public float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public List<PayStoreGoods> getList() {
		return list;
	}

	public void setList(List<PayStoreGoods> list) {
		this.list = list;
	}

	public int getNeedType() {
		return needType;
	}

	public void setNeedType(int needType) {
		this.needType = needType;
	}

}
