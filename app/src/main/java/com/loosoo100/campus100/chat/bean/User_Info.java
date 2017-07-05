package com.loosoo100.campus100.chat.bean;

/**
 * Created by HJianFei on 2016/10/14.
 */

public class User_Info {


    /**
     * code : 200
     * ry_token : bxmCT0n94IxgicF14glRyMC3OPZ4MLvN/1J8VQFDbBQGcAWzC0N4eoQFKenKEJ4f+rXDeLzuyNif6G5VPS3kcw==
     * school_name : 广州医科大学
     * school_shopid : 5
     * userId : 16112
     * userName : 骑着蜗牛去旅行
     * userPhotoThums : Upload/thumb/thumb_580ce3943d03e.png
     */

    private int code;
    private String ry_token;
    private String school_name;
    private String school_shopid;
    private String userId;
    private String userName;
    private String userPhotoThums;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRy_token() {
        return ry_token;
    }

    public void setRy_token(String ry_token) {
        this.ry_token = ry_token;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_shopid() {
        return school_shopid;
    }

    public void setSchool_shopid(String school_shopid) {
        this.school_shopid = school_shopid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhotoThums() {
        return userPhotoThums;
    }

    public void setUserPhotoThums(String userPhotoThums) {
        this.userPhotoThums = userPhotoThums;
    }
}
