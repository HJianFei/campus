package com.loosoo100.campus100.zzboss.chat.bean;

/**
 * Created by HJianFei on 2016/10/25.
 */

public class ConcernInfo {

    /**
     * code : 200
     * data : {"cid":{"company_id":"11","company_nickname":"周星星","company_photoThumb":"Upload/thumb/thumb_57f9e21a66934.png","ry_token":"VyKMTTZNwyvuzwpa41mp2v5I+A/U7QhJXRg3pdEIS8S160+CoHJRI014cTKFF4c3TfHJv3+QluEktHjf5db5/g=="},"uid":{"userId":"26","userName":"pangpang","userPhotoThums":null,"ry_token":"1lW3G90pasKGxg9M6j8KouV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLt318kQ77tJGcS1d2Vp2YoFWVeueiKgOow=="}}
     */

    private int code;
    /**
     * cid : {"company_id":"11","company_nickname":"周星星","company_photoThumb":"Upload/thumb/thumb_57f9e21a66934.png","ry_token":"VyKMTTZNwyvuzwpa41mp2v5I+A/U7QhJXRg3pdEIS8S160+CoHJRI014cTKFF4c3TfHJv3+QluEktHjf5db5/g=="}
     * uid : {"userId":"26","userName":"pangpang","userPhotoThums":null,"ry_token":"1lW3G90pasKGxg9M6j8KouV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLt318kQ77tJGcS1d2Vp2YoFWVeueiKgOow=="}
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * company_id : 11
         * company_nickname : 周星星
         * company_photoThumb : Upload/thumb/thumb_57f9e21a66934.png
         * ry_token : VyKMTTZNwyvuzwpa41mp2v5I+A/U7QhJXRg3pdEIS8S160+CoHJRI014cTKFF4c3TfHJv3+QluEktHjf5db5/g==
         */

        private CidBean cid;
        /**
         * userId : 26
         * userName : pangpang
         * userPhotoThums : null
         * ry_token : 1lW3G90pasKGxg9M6j8KouV7sFqG1h5Fxab8uZ7BuMSLSm5Cx7OnLt318kQ77tJGcS1d2Vp2YoFWVeueiKgOow==
         */

        private UidBean uid;

        public CidBean getCid() {
            return cid;
        }

        public void setCid(CidBean cid) {
            this.cid = cid;
        }

        public UidBean getUid() {
            return uid;
        }

        public void setUid(UidBean uid) {
            this.uid = uid;
        }

        public static class CidBean {
            private String company_id;
            private String company_nickname;
            private String company_photoThumb;
            private String ry_token;

            public String getCompany_id() {
                return company_id;
            }

            public void setCompany_id(String company_id) {
                this.company_id = company_id;
            }

            public String getCompany_nickname() {
                return company_nickname;
            }

            public void setCompany_nickname(String company_nickname) {
                this.company_nickname = company_nickname;
            }

            public String getCompany_photoThumb() {
                return company_photoThumb;
            }

            public void setCompany_photoThumb(String company_photoThumb) {
                this.company_photoThumb = company_photoThumb;
            }

            public String getRy_token() {
                return ry_token;
            }

            public void setRy_token(String ry_token) {
                this.ry_token = ry_token;
            }

            @Override
            public String toString() {
                return "CidBean{" +
                        "company_id='" + company_id + '\'' +
                        ", company_nickname='" + company_nickname + '\'' +
                        ", company_photoThumb='" + company_photoThumb + '\'' +
                        ", ry_token='" + ry_token + '\'' +
                        '}';
            }
        }

        public static class UidBean {
            private String userId;
            private String userName;
            private Object userPhotoThums;
            private String ry_token;

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

            @Override
            public String toString() {
                return "UidBean{" +
                        "userId='" + userId + '\'' +
                        ", userName='" + userName + '\'' +
                        ", userPhotoThums=" + userPhotoThums +
                        ", ry_token='" + ry_token + '\'' +
                        '}';
            }
        }
    }
}
