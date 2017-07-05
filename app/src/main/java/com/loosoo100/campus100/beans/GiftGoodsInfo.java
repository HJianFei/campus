package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * @author yang 礼物说商品信息
 */
public class GiftGoodsInfo {
    // 商品ID
    private String giftgoodsId;
    // 商品编号
    private String giftgoodsSn;
    // 商品名
    private String giftgoodsName;
    // 图片地址
    private String giftgoodsImg;
    // 轮播图片地址
    private String giftgoodsImg01;
    // 轮播图片地址
    private String giftgoodsImg02;
    // 轮播图片地址
    private String giftgoodsImg03;
    // 价格
    private float shopPrice;
    // 市场价
    private float marketPrice;
    // 商品库存
    private int giftgoodsStock;
    // 商品已售
    private int giftgoodsSold;
    // 商品收藏人数
    private int collect;
    // 商品详情图片
    private List<String> giftgoodsdetail;
    // 商品规格
    private List<GiftGoodsAttr> goodsAttr;
    //是否已收藏
    private String collectID;
    //运费
    private float deliverMoney;

    public float getDeliverMoney() {
        return deliverMoney;
    }

    public void setDeliverMoney(float deliverMoney) {
        this.deliverMoney = deliverMoney;
    }

    public String getGiftgoodsName() {
        return giftgoodsName;
    }

    public void setGiftgoodsName(String giftgoodsName) {
        this.giftgoodsName = giftgoodsName;
    }

    public float getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(float shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getGiftgoodsId() {
        return giftgoodsId;
    }

    public void setGiftgoodsId(String giftgoodsId) {
        this.giftgoodsId = giftgoodsId;
    }

    public String getGiftgoodsImg() {
        return giftgoodsImg;
    }

    public void setGiftgoodsImg(String giftgoodsImg) {
        this.giftgoodsImg = giftgoodsImg;
    }

    public String getGiftgoodsImg01() {
        return giftgoodsImg01;
    }

    public void setGiftgoodsImg01(String giftgoodsImg01) {
        this.giftgoodsImg01 = giftgoodsImg01;
    }

    public String getGiftgoodsImg02() {
        return giftgoodsImg02;
    }

    public void setGiftgoodsImg02(String giftgoodsImg02) {
        this.giftgoodsImg02 = giftgoodsImg02;
    }

    public String getGiftgoodsImg03() {
        return giftgoodsImg03;
    }

    public void setGiftgoodsImg03(String giftgoodsImg03) {
        this.giftgoodsImg03 = giftgoodsImg03;
    }

    public float getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getGiftgoodsStock() {
        return giftgoodsStock;
    }

    public void setGiftgoodsStock(int giftgoodsStock) {
        this.giftgoodsStock = giftgoodsStock;
    }

    public int getGiftgoodsSold() {
        return giftgoodsSold;
    }

    public void setGiftgoodsSold(int giftgoodsSold) {
        this.giftgoodsSold = giftgoodsSold;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public List<String> getGiftgoodsdetail() {
        return giftgoodsdetail;
    }

    public void setGiftgoodsdetail(List<String> giftgoodsdetail) {
        this.giftgoodsdetail = giftgoodsdetail;
    }

    public List<GiftGoodsAttr> getGoodsAttr() {
        return goodsAttr;
    }

    public void setGoodsAttr(List<GiftGoodsAttr> goodsAttr) {
        this.goodsAttr = goodsAttr;
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public String getGiftgoodsSn() {
        return giftgoodsSn;
    }

    public void setGiftgoodsSn(String giftgoodsSn) {
        this.giftgoodsSn = giftgoodsSn;
    }

}
