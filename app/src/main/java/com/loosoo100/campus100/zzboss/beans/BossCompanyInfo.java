package com.loosoo100.campus100.zzboss.beans;

/**
 * 公司信息
 *
 * @author yang
 */
public class BossCompanyInfo {
    // 公司ID
    private String cuid;
    // 公司名
    private String company;
    // 手机号
    private String phone;
    // 姓名
    private String name;
    // 昵称
    private String nickName;
    // 头像
    private String headShot;
    // 性别
    private String sex;
    // 钱包余额
    private float money;
    // 企业赞助金额
    private float zanMoney;
    // 积分
    private int point;
    // qq
    private String qq;
    // 微信
    private String weixin;
    // 公司是否存在
    private int status;
    // 公司状态(2已认证 1审核中 0 未认证 -1未认证)
    private int flag;



    public float getZanMoney() {
        return zanMoney;
    }

    public void setZanMoney(float zanMoney) {
        this.zanMoney = zanMoney;
    }
    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadShot() {
        return headShot;
    }

    public void setHeadShot(String headShot) {
        this.headShot = headShot;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
