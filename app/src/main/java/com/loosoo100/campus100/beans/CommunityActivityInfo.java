package com.loosoo100.campus100.beans;

import java.util.List;

import android.graphics.Bitmap;

/**
 * @author yang 社团活动信息
 */
public class CommunityActivityInfo {
    // 活动ID
    private String id;
    // 活动状态
    private int status;
    // 活动名称
    private String activityName;
    // 创建人
    private String name;
    // 所属学校
    private String school;
    // 所属学校ID
    private String schoolID;
    // 社团ID
    private String commId;
    // 社团名称
    private String communityName;
    // 图片
    private Bitmap bitmap;
    // 需要筹集的资金
    private float needMoney;
    // 已筹集的资金
    private float raisedMoney;
    // 支持的人数
    private int supportCount;
    // 剩余天数
    private int remainDay;
    // 我支持的金额
    private float mySupportMoney;
    // 发起的日期
    private String date;
    // 活动头像地址
    private String actiHeadShot;
    // 内容
    private String acti_letter;
    // 社团头像地址
    private String commHeadShot;
    // 活动图片集合
    private List<String> acti_pics;
    //是否已收藏(0没收藏 1已收藏)
    private int collect;
    //是否已参与(0没参与 1已参与)
    private int canyu;
    //收藏人数
    private int collectCount;
    //活动分类,是否显示进度条(=0众筹    =1非众筹)
    private int classify;
    //所属城市
    private String city;
    //多少元起(0为免费)
    private String free;
    //浏览数
    private int readCount;
    //社团长ID
    private String commUid;
    //活动开始时间
    private long startTime;
    //活动结束时间
    private long endTime;
    //举办地址
    private String address;
    //举办的类型（本校或全国）
    private String type;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    //企业是否认证
    private int flag;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getActiHeadShot() {
        return actiHeadShot;
    }

    public void setActiHeadShot(String actiHeadShot) {
        this.actiHeadShot = actiHeadShot;
    }

    public String getCommHeadShot() {
        return commHeadShot;
    }

    public void setCommHeadShot(String commHeadShot) {
        this.commHeadShot = commHeadShot;
    }

    public int getRemainDay() {
        return remainDay;
    }

    public void setRemainDay(int remainDay) {
        this.remainDay = remainDay;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(float needMoney) {
        this.needMoney = needMoney;
    }

    public float getRaisedMoney() {
        return raisedMoney;
    }

    public void setRaisedMoney(float raisedMoney) {
        this.raisedMoney = raisedMoney;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public float getMySupportMoney() {
        return mySupportMoney;
    }

    public void setMySupportMoney(float mySupportMoney) {
        this.mySupportMoney = mySupportMoney;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public List<String> getActi_pics() {
        return acti_pics;
    }

    public void setActi_pics(List<String> acti_pics) {
        this.acti_pics = acti_pics;
    }

    public String getActi_letter() {
        return acti_letter;
    }

    public void setActi_letter(String acti_letter) {
        this.acti_letter = acti_letter;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
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

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public int getCanyu() {
        return canyu;
    }

    public void setCanyu(int canyu) {
        this.canyu = canyu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
