package com.loosoo100.campus100.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HJianFei on 2016/10/14.
 */

public class Friend_Reqs_List {


    /**
     * total : 1
     * pageSize : 10
     * start : 0
     * root : [{"to_uid":"167","uid":"26","userName":"pangpang","userPhotoThums":null,"ry_token":"1lW3G90pasKGxg9M6j8KouV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLt318kQ77tJGcS1d2Vp2YoFWVeueiKgOow==","msg":["","","","123"],"status":"1"}]
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
     * to_uid : 167
     * uid : 26
     * userName : pangpang
     * userPhotoThums : null
     * ry_token : 1lW3G90pasKGxg9M6j8KouV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLt318kQ77tJGcS1d2Vp2YoFWVeueiKgOow==
     * msg : ["","","","123"]
     * status : 1
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

    public static class RootBean implements Serializable{
        private String to_uid;
        private String uid;
        private String userName;
        private Object userPhotoThums;
        private String ry_token;
        private String status;
        private List<String> msg;

        public String getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(String to_uid) {
            this.to_uid = to_uid;
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

        public Object getUserPhotoThums() {
            return userPhotoThums;
        }

        public void setUserPhotoThums(Object userPhotoThums) {
            this.userPhotoThums = userPhotoThums;
        }

        public String getRy_token() {
            return ry_token;
        }

        public void setRy_token(String ry_token) {
            this.ry_token = ry_token;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<String> getMsg() {
            return msg;
        }

        public void setMsg(List<String> msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "RootBean{" +
                    "to_uid='" + to_uid + '\'' +
                    ", uid='" + uid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userPhotoThums=" + userPhotoThums +
                    ", ry_token='" + ry_token + '\'' +
                    ", status='" + status + '\'' +
                    ", msg=" + msg +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Friend_Reqs_List{" +
                "total='" + total + '\'' +
                ", pageSize=" + pageSize +
                ", start=" + start +
                ", totalPage=" + totalPage +
                ", currPage=" + currPage +
                ", code=" + code +
                ", root=" + root +
                '}';
    }
}
