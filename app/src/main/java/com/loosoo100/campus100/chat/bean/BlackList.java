package com.loosoo100.campus100.chat.bean;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/17.
 */

public class BlackList {

    /**
     * total : 1
     * pageSize : 10
     * start : 0
     * root : [{"uid":"503","black_uid":"453","userName":"小夜","userPhotoThums":"Upload/thumb/thumb_57dec84e57baa.png","ry_token":"sGvJz2w62+KWReywrr0mAOV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLq1nk8iiWt8DfrC7VdIvCHYPhLgH9jGt7w=="}]
     * totalPage : 1
     * currPage : 1
     * code : 200
     */

    private String total;
    private int pageSize;
    private int start;
    private int totalPage;
    private int currPage;
    private int code;
    /**
     * uid : 503
     * black_uid : 453
     * userName : 小夜
     * userPhotoThums : Upload/thumb/thumb_57dec84e57baa.png
     * ry_token : sGvJz2w62+KWReywrr0mAOV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLq1nk8iiWt8DfrC7VdIvCHYPhLgH9jGt7w==
     */

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
        private String uid;
        private String black_uid;
        private String userName;
        private String userPhotoThums;
        private String ry_token;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBlack_uid() {
            return black_uid;
        }

        public void setBlack_uid(String black_uid) {
            this.black_uid = black_uid;
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

        public String getRy_token() {
            return ry_token;
        }

        public void setRy_token(String ry_token) {
            this.ry_token = ry_token;
        }
    }
}
