package com.loosoo100.campus100.zzboss.beans;

/**
 * 公司简介
 *
 * @author yang
 */
public class BossCompanySummaryInfo {
    //公司ID
    private String id;
    // 公司头像
    private String logo;
    // 公司背景图
    private String bgThumb;
    // 公司名
    private String company;
    // 公司网址
    private String host;
    // 公司地址
    private String address;
    // 公司架构
    private String frame;
    // 公司地址
    private String property;
    // 公司规模
    private String size;
    // 公司简介
    private String summary;
    // 公司状态(2已认证 1审核中 0 未认证 -1未认证)
    private int flag;
    // 公司赞助的金额
    private String money;
    // 公司赞助的次数
    private int count;
    //点进来的是否社团长
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getBgThumb() {
        return bgThumb;
    }

    public void setBgThumb(String bgThumb) {
        this.bgThumb = bgThumb;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
