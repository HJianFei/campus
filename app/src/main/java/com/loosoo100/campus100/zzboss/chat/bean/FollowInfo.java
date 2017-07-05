package com.loosoo100.campus100.zzboss.chat.bean;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/25.
 */

public class FollowInfo {


    /**
     * root : [{"cmpId":"116","cmmId":"4561","uid":"442","userName":"乒乓球联盟","userPhotoThums":"http://ocaxbuib5.bkt.clouddn.com/community_2016-09-19_57df60b77d1e0.png/thumb","school_name":"广东金融学院","ry_token":"pnPUt8MqXBmLjvHSFxlRdpwPYZuyJAtRdGBYTb3ixA9tFayCD+J60ebwT1YNtPXnYitI92NGJOj0DJdBVqk+jw=="},{"cmpId":"116","cmmId":"4563","uid":"650","userName":"篮球俱乐部","userPhotoThums":"http://ocaxbuib5.bkt.clouddn.com/community_2016-09-19_57df6251532b8.png/thumb","school_name":"广东金融学院","ry_token":"N3Pz6YbplpfgqhD6x0BygJNETO5+aSAH4Em/YyNssOPFhahPh0QaEJDltGpsDV4SSzQX3etQPlN6gkPPdfOroQ=="},{"cmpId":"116","cmmId":"4567","uid":"844","userName":"广州大学纺织服装学院社团联合会","userPhotoThums":"http://ocaxbuib5.bkt.clouddn.com/community_2016-09-27_57ea95e6054a6.png/thumb","school_name":"广州大学纺织服装学院","ry_token":"ExaM1SxJ/fxENWcHP4SGApwPYZuyJAtRdGBYTb3ixA9tFayCD+J60XBinTTZo78Xszq8INkXxX/0DJdBVqk+jw=="},{"cmpId":"116","cmmId":"4588","uid":"6729","userName":"校外联中心","userPhotoThums":"http://ocaxbuib5.bkt.clouddn.com/community_2016-10-12_57fe0269d58f8.png/thumb","school_name":"广东外语外贸大学南国商学院","ry_token":"R8bf5I3qn9i+IEhgBUQtJJNETO5+aSAH4Em/YyNssOPFhahPh0QaEAn/O52jHz7W6NiLFgAoX7yIw2UU+S6szQ=="},{"cmpId":"116","cmmId":"4612","uid":"417","userName":"健美操协会","userPhotoThums":"http://ocaxbuib5.bkt.clouddn.com/community_2016-10-14_58006f376ce41.jpeg/thumb","school_name":"广东岭南职业技术学院","ry_token":"oOkEs4lcmCW7HUoTT4nBJpwPYZuyJAtRdGBYTb3ixA9tFayCD+J60blRwb4JbpmBBLvs0spHyvT0DJdBVqk+jw=="}]
     * code : 200
     */

    private int code;
    /**
     * cmpId : 116
     * cmmId : 4561
     * uid : 442
     * userName : 乒乓球联盟
     * userPhotoThums : http://ocaxbuib5.bkt.clouddn.com/community_2016-09-19_57df60b77d1e0.png/thumb
     * school_name : 广东金融学院
     * ry_token : pnPUt8MqXBmLjvHSFxlRdpwPYZuyJAtRdGBYTb3ixA9tFayCD+J60ebwT1YNtPXnYitI92NGJOj0DJdBVqk+jw==
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
        private String cmpId;
        private String cmmId;
        private String uid;
        private String userName;
        private String userPhotoThums;
        private String school_name;
        private String ry_token;

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
    }
}
