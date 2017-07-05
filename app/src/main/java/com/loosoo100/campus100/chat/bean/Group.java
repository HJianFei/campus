package com.loosoo100.campus100.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HJianFei on 2016/10/13.
 */

public class Group implements Serializable {

    /**
     * root : [{"id":"15","name":"嗯","avatar":"","description":"","type":"0"},{"id":"16","name":"测试群","avatar":"","description":"","type":"0"},{"id":"17","name":"测试群2","avatar":"","description":"","type":"0"},{"id":"18","name":"测试群3","avatar":"","description":"","type":"0"}]
     * code : 200
     */

    private int code;
    /**
     * id : 15
     * name : 嗯
     * avatar :
     * description :
     * type : 0
     */

    private List<GroupBean> root;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<GroupBean> getRoot() {
        return root;
    }

    public void setRoot(List<GroupBean> root) {
        this.root = root;
    }

    public static class GroupBean {
        private String id;
        private String name;
        private String avatar;
        private String description;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
