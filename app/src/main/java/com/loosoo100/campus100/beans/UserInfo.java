package com.loosoo100.campus100.beans;

public class UserInfo {
    // 手机号
    private String phone;
    // 用户ID
    private String uid;
    // 姓名
    private String name;
    // 昵称
    private String nickName;
    // 头像
    private String headShot;
    // 性别
    private String sex;
    // 学校
    private String school;
    // 学校ID
    private String schoolID;
    // 学生证号
    private String stuNo;
    // 钱包余额
    private String money;
    // weixin
    private String weixin;
    // 积分
    private int point;
    //用户是否存在
    private int status;
    //是否企业(1是企业)
    private int org;
    //推荐人是否有效
    private int agent_status;

    public int getAgent_status() {
        return agent_status;
    }

    public void setAgent_status(int agent_status) {
        this.agent_status = agent_status;
    }

    public String getHeadShot() {
        return headShot;
    }

    public void setHeadShot(String headShot) {
        this.headShot = headShot;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public int getOrg() {
        return org;
    }

    public void setOrg(int org) {
        this.org = org;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
