package com.loosoo100.campus100.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HJianFei on 2016/10/18.
 */

public class Groupnumber implements Serializable {

    private String total;
    private int pageSize;
    private int start;
    private int totalPage;
    private int currPage;
    private String uid;
    private int isGranted;
    private int code;
    private List<RootBean> root;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIsGranted() {
        return isGranted;
    }

    public void setIsGranted(int isGranted) {
        this.isGranted = isGranted;
    }

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

    public static class RootBean implements Serializable{
        /**
         * userId : 16112
         * status : 127
         * userName : 骑着蜗牛去旅行
         * userPhotoThums : Upload/thumb/thumb_58213d3b9495d.png
         */

        private String userId;
        private String status;
        private String userName;
        private String userPhotoThums;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
}
