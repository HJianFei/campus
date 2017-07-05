package com.loosoo100.campus100.beans;

import android.graphics.Bitmap;

/**
 * 
 * @author yang 礼物说订单信息
 * 
 */
public class GiftOrderInfo {
	// 订单状态 0待付款 1发货
	private int isPay;
	// 订单状态
	private int status;
	// 订单ID
	private String orderID;
	// 订单编号
	private String orderNO;
	// 0是自己要 1是送给她
	private int needType;
	// 订单时间
	private String time;
	// 订单图标
	private Bitmap bitmap;
	// 收件人名字
	private String userName;
	// 收件人ID
	private String userID;
	// 订单颜色
	private String color;
	// 地址
	private String userAddress;
	// 电话
	private String userPhone;
	// 订单价格
	private float needPrice;
	// 运费
	private float deliverMoney;
	// 订单还差多少元
	private float priceNotEnough;
	// 快递编号
	private String logisticsComs;
	// 快递单号
	private String logisticsNum;
	// 快递名
	private String logisticsName;
	// 分享备注
	private String remarkShare;
	// 商品
	private GiftOrderGoodsInfo giftOrderGoodsInfo;

	public String getLogisticsComs() {
		return logisticsComs;
	}

	public void setLogisticsComs(String logisticsComs) {
		this.logisticsComs = logisticsComs;
	}

	public String getLogisticsNum() {
		return logisticsNum;
	}

	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public int getNeedType() {
		return needType;
	}

	public void setNeedType(int needType) {
		this.needType = needType;
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

	public float getDeliverMoney() {
		return deliverMoney;
	}

	public void setDeliverMoney(float deliverMoney) {
		this.deliverMoney = deliverMoney;
	}

	public float getPriceNotEnough() {
		return priceNotEnough;
	}

	public void setPriceNotEnough(float priceNotEnough) {
		this.priceNotEnough = priceNotEnough;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


	public float getNeedPrice() {
		return needPrice;
	}

	public void setNeedPrice(float needPrice) {
		this.needPrice = needPrice;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public GiftOrderGoodsInfo getGiftOrderGoodsInfo() {
		return giftOrderGoodsInfo;
	}

	public void setGiftOrderGoodsInfo(GiftOrderGoodsInfo giftOrderGoodsInfo) {
		this.giftOrderGoodsInfo = giftOrderGoodsInfo;
	}

	public String getRemarkShare() {
		return remarkShare;
	}

	public void setRemarkShare(String remarkShare) {
		this.remarkShare = remarkShare;
	}

}
