package com.loosoo100.campus100.zzboss.chat.fragment;

/**
 * Created by HJianFei on 2016/10/26.
 */

public class SortFollows {
    private String cmpId;
    private String cmmId;
    private String uid;
    private String userName;
    private String userPhotoThums;
    private String school_name;
    private String ry_token;
    private String Letters;

    public SortFollows() {
    }

    public SortFollows(String cmpId, String cmmId, String uid, String userName, String userPhotoThums, String school_name, String ry_token, String letters) {
        this.cmpId = cmpId;
        this.cmmId = cmmId;
        this.uid = uid;
        this.userName = userName;
        this.userPhotoThums = userPhotoThums;
        this.school_name = school_name;
        this.ry_token = ry_token;
        Letters = letters;
    }

    public String getLetters() {
        return Letters;
    }

    public void setLetters(String letters) {
        Letters = letters;
    }

    public String getCmpId() {
        return cmpId;
    }

    public void setCmpId(String cmpId) {
        this.cmpId = cmpId;
    }

    public String getCmmId() {
        return cmmId;
    }

    public void setCmmId(String cmmId) {
        this.cmmId = cmmId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getRy_token() {
        return ry_token;
    }

    public void setRy_token(String ry_token) {
        this.ry_token = ry_token;
    }

    @Override
    public String toString() {
        return "SortFollows{" +
                "cmpId='" + cmpId + '\'' +
                ", cmmId='" + cmmId + '\'' +
                ", uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhotoThums='" + userPhotoThums + '\'' +
                ", school_name='" + school_name + '\'' +
                ", ry_token='" + ry_token + '\'' +
                '}';
    }
}
