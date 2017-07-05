package com.loosoo100.campus100.chat.bean;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/28.
 */

public class SearchInfo {

    /**
     * code : 200
     * root : [{"school_name":"广州大学松田学院","userId":"13136","userName":"安尿","userPhone":"15107224957","userPhotoThums":"","userSex":"1"},{"school_name":"广东理工职业学院（白云校区）","userId":"15512","userName":"可爱又能嫖","userPhone":"15107319561","userPhotoThums":"","userSex":"0"},{"school_name":"广东海洋大学霞山校区","userId":"14478","userName":"千画i","userPhone":"15107942753","userPhotoThums":"","userSex":"0"}]
     */

    private int code;
    /**
     * school_name : 广州大学松田学院
     * userId : 13136
     * userName : 安尿
     * userPhone : 15107224957
     * userPhotoThums :
     * userSex : 1
     */

    private List<RootBean> root;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<RootBean> getRoot() {
        return root;
    }

    public void setRoot(List<RootBean> root) {
        this.root = root;
    }

    public static class RootBean {
        private String school_name;
        private String userId;
        private String userName;
        private String userPhone;
        private String userPhotoThums;
        private String userSex;

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
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

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserPhotoThums() {
            return userPhotoThums;
        }

        public void setUserPhotoThums(String userPhotoThums) {
            this.userPhotoThums = userPhotoThums;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }
    }
}
