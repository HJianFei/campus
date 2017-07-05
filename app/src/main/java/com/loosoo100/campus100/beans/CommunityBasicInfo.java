package com.loosoo100.campus100.beans;

import java.util.List;

import android.graphics.Bitmap;

import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

/**
 * @author yang 社团基本信息
 */
public class CommunityBasicInfo {
    // 社团ID
    private String id;
    // 社团ID2
    private String id2;
    // 社团图标
    private Bitmap bitmap;
    // 社团名称
    private String communityName;
    // 社团所属学校
    private String school;
    // 社团所属学校ID
    private String sid;
    // 社团口号
    private String slogan;
    // 社团公告
    private String notice;
    // 社团简介
    private String summary;
    // 创建人
    private String name;
    // 创建人ID
    private String userId;
    // 创建时间
    private String time;
    // 社团成员
    private List<CommunityMemberInfo> memberList;
    // 社团近七天资金
    private List<CommunityMoney> moneyList;
    // 职位
    private String job;
    // 拥有资金
    private float money;
    // 头像地址
    private String headthumb;
    // 背景图片
    private String commBg;
    // 社团类型
    private String type;
    // 社团结构
    private String dep;
    //社团活动
    private List<CommunityActivityInfo> activityInfos;
    //我的所有社团列表
    private List<CommunityBasicInfo> myCommList;
    //用户是否申请中或已加入社团	1可加入  0正在申请  -1已经加入
    private String status;
    //用户是否申请中或已加入社团	0可加入  1可退出  -1审核中 2社长身份
    private String in;
    //社团所属城市
    private String city;
    //是否关注社团,0代表没关注 ，1代表没关注
    private String cang;
    //社团成员数
    private int memCount;
    //社团需求
    private List<BossCommSupportInfo> needInfos;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    //企业是否认证
    private int flag;

    public List<BossCommSupportInfo> getNeedInfos() {
        return needInfos;
    }

    public void setNeedInfos(List<BossCommSupportInfo> needInfos) {
        this.needInfos = needInfos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<CommunityMemberInfo> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<CommunityMemberInfo> memberList) {
        this.memberList = memberList;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getHeadthumb() {
        return headthumb;
    }

    public void setHeadthumb(String headthumb) {
        this.headthumb = headthumb;
    }

    public List<CommunityActivityInfo> getActivityInfos() {
        return activityInfos;
    }

    public void setActivityInfos(List<CommunityActivityInfo> activityInfos) {
        this.activityInfos = activityInfos;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CommunityBasicInfo> getMyCommList() {
        return myCommList;
    }

    public void setMyCommList(List<CommunityBasicInfo> myCommList) {
        this.myCommList = myCommList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommunityMoney> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<CommunityMoney> moneyList) {
        this.moneyList = moneyList;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCang() {
        return cang;
    }

    public void setCang(String cang) {
        this.cang = cang;
    }

    public String getCommBg() {
        return commBg;
    }

    public void setCommBg(String commBg) {
        this.commBg = commBg;
    }

    public int getMemCount() {
        return memCount;
    }

    public void setMemCount(int memCount) {
        this.memCount = memCount;
    }


}
