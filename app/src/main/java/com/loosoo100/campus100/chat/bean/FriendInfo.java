package com.loosoo100.campus100.chat.bean;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/14.
 */

public class FriendInfo {


    /**
     * root : [{"uid":"495","userName":"Mark_A","userPhotoThums":"Upload/thumb/thumb_57ca80a0c87d5.png","ry_token":"h4ooqXujK7G6xYD6BnRXxv5I+A/U7QhJXRg3pdEIS8TqIJnCROIDdKswQhMnL0ThqJyx4U00fSU="},{"uid":"503","userName":"骑着蜗牛去旅行","userPhotoThums":"Upload/thumb/thumb_58008dc8edb06.png","ry_token":"sXzGrfs08PokL2SpqRSDkMku0Rr3+MDT/BDEE3eZRq1W6s62KaJ3iLXcrs6QgkRzbgC0jEA+8rd0//aAm3ziHg=="}]
     * code : 200
     */

    private int code;
    /**
     * uid : 495
     * userName : Mark_A
     * userPhotoThums : Upload/thumb/thumb_57ca80a0c87d5.png
     * ry_token : h4ooqXujK7G6xYD6BnRXxv5I+A/U7QhJXRg3pdEIS8TqIJnCROIDdKswQhMnL0ThqJyx4U00fSU=
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
        private String uid;
        private String userName;
        private String userPhotoThums;
        private String ry_token;

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

        public String getRy_token() {
            return ry_token;
        }

        public void setRy_token(String ry_token) {
            this.ry_token = ry_token;
        }

        @Override
        public String toString() {
            return "RootBean{" +
                    "uid='" + uid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userPhotoThums='" + userPhotoThums + '\'' +
                    ", ry_token='" + ry_token + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "code=" + code +
                ", root=" + root +
                '}';
    }
}
