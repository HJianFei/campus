package com.loosoo100.campus100.anyevent;

/**
 * 礼物盒子订单凑一凑还差多少钱
 * 
 * @author yang
 * 
 */
public class MEventGiftTogNotEnough {
	private int position;
	private float yetMoney;

	public MEventGiftTogNotEnough(int position, float yetMoney) {
		this.position = position;
		this.yetMoney = yetMoney;
	}

	public int getPosition() {
		return position;
	}

	public float getYetMoney() {
		return yetMoney;
	}

}
