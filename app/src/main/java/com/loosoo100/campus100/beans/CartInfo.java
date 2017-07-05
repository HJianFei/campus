package com.loosoo100.campus100.beans;


/**
 * 
 * @author yang
 * 添加到购物车的商品信息
 *
 */
public class CartInfo {
	//商品名
	public static String goodsName;
	//商品数量
	public static int goodsCount;
	//总商品数量
	public static int goodsCounttotal;
	//商品价格
	public static float goodsPrice;
	//购物车总价
	public static float goodsPricetotal;
	
	
	public static void resetData(){
		goodsName="";
		goodsCount=0;
		goodsCounttotal=0;
		goodsPrice=0;
		goodsPricetotal=0;
	}
	
	
	
}
