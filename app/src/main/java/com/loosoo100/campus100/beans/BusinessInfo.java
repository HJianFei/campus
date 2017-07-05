package com.loosoo100.campus100.beans;

/**
 * 商家信息
 * 
 * @author yang 商家信息
 * 
 */
public class BusinessInfo {
	// 商家名称
	public static String shopName;
	// 营业开始时间
	public static String startTime;
	// 营业结束时间
	public static String endTime;
	// 店铺ID
	public static String shopID;
	// 最低多少元起送
	public static float sendAtLeastMoney;
	// 配送费
	public static float expressFee;
	// 商家电话
	public static String phone;
	// 平均配送时间
	public static String deliverTime;
	// 商铺地址
	public static String address;

	public static void resetData() {
		shopName = "";
		startTime = "";
		endTime = "";
		shopID = "";
		sendAtLeastMoney = 0;
		expressFee = 0;
		phone = "";
		deliverTime = "";
		address = "";
	}

}
