package com.loosoo100.campus100.zzboss.beans;

import java.util.List;

/**
 * @author yang 赞助社团基本信息
 */
public class BossCommSupportInfo {
    // 需求ID
    private String id;
    // 需求头像地址
    private String headthumb;
    // 社团名称
    private String commName;
    // 需求名称
    private String demandName;
    // 提供的内容
    private String offer;
    // 社团所属城市
    private String city = "";
    // 社团所属学校
    private String school;
    // 截止时间
    private String overTime;
    // 剩余多少席位
    private int remaindCount;
    // 多少元起(0为免费)
    private String free;
    // 状态
    private int status;
    // 所属学校ID
    private String schoolID;
    // 社团ID
    private String commId;
    // 已筹集的资金
    private float raiseMoney;
    // 需要筹集的资金
    private float needMoney;
    // 内容
    private String letter;
    // 社团头像地址
    private String commHeadShot;
    // 图片集合
    private List<String> pics;
    // 是否已收藏(0没收藏 1已收藏)
    private int collect;
    // 是否已参与(0没参与 1已参与)
    private int canyu;
    // 收藏人数
    private int collectCount;
    // 支持人数
    private int supportCount;
    // 需求分类
    private int classify;
    // 浏览数
    private int readCount;
    // 社团长ID
    private String commUid;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    //企业是否认证
    private int flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadthumb() {
        return headthumb;
    }

    public void setHeadthumb(String headthumb) {
        this.headthumb = headthumb;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public int getRemaindCount() {
        return remaindCount;
    }

    public void setRemaindCount(int remaindCount) {
        this.remaindCount = remaindCount;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public float getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(float needMoney) {
        this.needMoney = needMoney;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getCommHeadShot() {
        return commHeadShot;
    }

    public void setCommHeadShot(String commHeadShot) {
        this.commHeadShot = commHeadShot;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getCanyu() {
        return canyu;
    }

    public void setCanyu(int canyu) {
        this.canyu = canyu;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getCommUid() {
        return commUid;
    }

    public void setCommUid(String commUid) {
        this.commUid = commUid;
    }

    public float getRaiseMoney() {
        return raiseMoney;
    }

    public void setRaiseMoney(float raiseMoney) {
        this.raiseMoney = raiseMoney;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

}
