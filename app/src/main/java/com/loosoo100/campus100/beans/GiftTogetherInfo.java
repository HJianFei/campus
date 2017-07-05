package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * 礼物盒子凑一凑详情
 * 
 * @author yang
 * 
 */
public class GiftTogetherInfo {
	// 还差多少元
	private float moneyNotEnough;
	// 总共需要多少元
	private float needPay;
	// 凑一凑的小伙伴信息
	private List<GiftTogetherFriendInfo> list;

	public float getMoneyNotEnough() {
		return moneyNotEnough;
	}

	public void setMoneyNotEnough(float moneyNotEnough) {
		this.moneyNotEnough = moneyNotEnough;
	}

	public float getNeedPay() {
		return needPay;
	}

	public void setNeedPay(float needPay) {
		this.needPay = needPay;
	}

	public List<GiftTogetherFriendInfo> getList() {
		return list;
	}

	public void setList(List<GiftTogetherFriendInfo> list) {
		this.list = list;
	}

}
