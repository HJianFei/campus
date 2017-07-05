package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * 
 * @author yang 小卖部订单信息
 */
public class StoreOrderInfo {
	// 订单状态
	private int orderStatus;
	// 1付款 0没付款
	private String isPay;
	// 退款状态 0 没有申请退款 1退款中 2退款完成 3拒绝退款
	private String isRefund;
	// 0钱包 1 微信 2支付宝
	private String payType;
	// 订单ID
	private String orderId;
	// 订单号
	private String orderNo;
	// 用户ID
	private String userId;
	// 商家ID
	private String shopid;
	// 商家图标
	private String shopImg;
	// 下单时间
	private String createTime;
	// 签收时间
	private String finishTime;
	// 总价(加运费)
	private float needPay;
	// 订单价格
	private float totalMoney;
	// 配送费
	private float deliverMoney;
	// 配送人
	private String deliverMember;
	// 配送方式0商家配送1到店自取
	private String isSelf;
	// 配送人
	private String deliverPhone;
	// 联系人
	private String userName;
	// 联系电话
	private String userPhone;
	// 收货地址
	private String userAddress;
	// 商家电话
	private String shopPhone;
	// 学校名字
	private String shopName;
	// 是否已评价 0没评价1已评价
	private String isAppraise;
	// 评分
	private int star;
	// 商品数量
	private int count;

	public String getIsAppraise() {
		return isAppraise;
	}

	public void setIsAppraise(String isAppraise) {
		this.isAppraise = isAppraise;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	// 商品列表
	private List<GoodsInfo> list;

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public String getDeliverMember() {
		return deliverMember;
	}

	public void setDeliverMember(String deliverMember) {
		this.deliverMember = deliverMember;
	}

	public String getDeliverPhone() {
		return deliverPhone;
	}

	public void setDeliverPhone(String deliverPhone) {
		this.deliverPhone = deliverPhone;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public float getDeliverMoney() {
		return deliverMoney;
	}

	public void setDeliverMoney(float deliverMoney) {
		this.deliverMoney = deliverMoney;
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

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getShopPhone() {
		return shopPhone;
	}

	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public List<GoodsInfo> getList() {
		return list;
	}

	public void setList(List<GoodsInfo> list) {
		this.list = list;
	}

	public String getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(String isSelf) {
		this.isSelf = isSelf;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public float getNeedPay() {
		return needPay;
	}

	public void setNeedPay(float needPay) {
		this.needPay = needPay;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

}
