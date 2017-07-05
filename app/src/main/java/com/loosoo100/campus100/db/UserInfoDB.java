package com.loosoo100.campus100.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author yang 用户信息表
 */
public class UserInfoDB {
    //用户表
    public static final String USERTABLE = "userInfo";
    //个人版或者企业版,0个人,1企业
    public static final String ORG = "org";
    //昵称
    public static final String NICK_NAME = "nickName";
    //用户ID
    public static final String USERID = "userID";
    //企业ID
    public static final String CUSERID = "cuserID";
    //企业名
    public static final String COMPANY = "company";
    //姓名
    public static final String NAME = "name";
    //手机号
    public static final String PHONE = "phone";
    //性别
    public static final String SEX = "sex";
    //身份证号
    public static final String ID = "id";
    //QQ号
//	public static final String QQ = "qq";
    //微信号
    public static final String WEIXIN = "weixin";
    //年级
    public static final String GRADE = "grade";
    //学校
    public static final String SCHOOL = "school";
    //学校id
    public static final String SCHOOL_ID = "schoolId";
    //学生证号
    public static final String STUDENT_ID = "studentID";
    //头像
    public static final String HEADSHOT = "headShot";
    //金额
    public static final String MONEY = "money";
    //积分
    public static final String POINT = "point";
    //照片墙选择学校
    public static final String SCHOOL_PICTURE = "school_picture";
    //照片墙选择学校ID
    public static final String SCHOOL_ID_PICTURE = "schoolId_picture";
    //	//照片墙是否首次进入，是则显示手指引导动画
//	public static final String PICTURE_FIRST = "picture_first";
    //社团选择学校
    public static final String SCHOOL_COMM = "school_comm";
    //社团选择学校ID
    public static final String SCHOOL_ID_COMM = "schoolId_comm";
    //小卖部选择学校
    public static final String SCHOOL_STORE = "school_store";
    //小卖部选择学校ID
    public static final String SCHOOL_ID_STORE = "schoolId_store";
    //校园圈查看过最新一条圈圈的发表时间
    public static final String CAMPUS_TIME = "campus_time";
    //校园圈我的友友未读消息
    public static final String CAMPUS_NOREAD_FRIEND = "campus_noread_friend";
    //校园圈我的友友未读消息
    public static final String CAMPUS_NOREAD_FRIEND_DETAIL = "campus_noread_friend_detail";
    //校园圈我的暗恋的人未读消息
    public static final String CAMPUS_NOREAD_LOVE = "campus_noread_love";
    //校园圈我的和友友首页未读消息
//    public static final String CAMPUS_NOREAD_HOME_FRIEND = "campus_noread_home_friend";
    //校园圈我的暗恋的人首页未读消息
//    public static final String CAMPUS_NOREAD_HOME_LOVE = "campus_noread_home_love";
    //系统未读消息
    public static final String NOREAD_MESSAGE_COUNT = "noread_message_count";

    // 课程表单双周
    public static final String COURSE = "course";

    /**
     * 设置用户信息
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setUserInfo(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(USERTABLE,
                context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 清除用户信息
     *
     * @param context
     */
    public static void clearUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USERTABLE,
                context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString(NICK_NAME, "");
        edit.putString(ORG, "");
        edit.putString(CUSERID, "");
        edit.putString(COMPANY, "");
        edit.putString(PHONE, "");
        edit.putString(USERID, "");
        edit.putString(NAME, "");
        edit.putString(SEX, "1");
        edit.putString(ID, "");
//		edit.putString(QQ, "");
        edit.putString(WEIXIN, "");
        edit.putString(GRADE, "");
        edit.putString(SCHOOL, "");
        edit.putString(SCHOOL_ID, "");
        edit.putString(STUDENT_ID, "");
        edit.putString(HEADSHOT, "");
        edit.putString(MONEY, "0");
        edit.putString(POINT, "0");
        edit.putString(SCHOOL_PICTURE, "");
        edit.putString(SCHOOL_ID_PICTURE, "");
        edit.putString(SCHOOL_COMM, "");
        edit.putString(SCHOOL_ID_COMM, "");
        edit.putString(SCHOOL_STORE, "");
        edit.putString(SCHOOL_ID_STORE, "");
        edit.putString(CAMPUS_TIME, "0");
        edit.putString(CAMPUS_NOREAD_FRIEND, "0");
        edit.putString(CAMPUS_NOREAD_FRIEND_DETAIL, "0");
        edit.putString(CAMPUS_NOREAD_LOVE, "0");
//        edit.putString(CAMPUS_NOREAD_HOME_LOVE, "0");
//        edit.putString(CAMPUS_NOREAD_HOME_FRIEND, "0");
//        edit.putString(NOREAD_MESSAGE_COUNT, "0");
        edit.commit();
    }

}
