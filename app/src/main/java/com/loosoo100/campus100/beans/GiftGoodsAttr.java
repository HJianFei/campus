package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * 
 * @author yang
 * 商品规格属性
 *
 */
public class GiftGoodsAttr {
	// 属性名
	private String attrName;
	// 属性列表
	private List<GiftGoodsAttrVal> list;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public List<GiftGoodsAttrVal> getList() {
		return list;
	}

	public void setList(List<GiftGoodsAttrVal> list) {
		this.list = list;
	}
}
