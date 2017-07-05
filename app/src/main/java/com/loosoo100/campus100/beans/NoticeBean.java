package com.loosoo100.campus100.beans;

import java.util.List;

/**
 * Created by HJianFei on 2016/10/8.
 */

public class NoticeBean {

	 /**
     * list : [{"chat_id":"209","userPhoto":"http://192.168.199.140/App/Upload/touxiang/2016-09-17/57dcb73407096.png","userName":"Jay"},{"chat_id":"254","userPhoto":"http://192.168.199.140/App/Upload/touxiang/2016-09-17/57dcb73407096.png","userName":"Jay"}]
     * status : 1
     */

    private int status;
    /**
     * chat_id : 209
     * userPhoto : http://192.168.199.140/App/Upload/touxiang/2016-09-17/57dcb73407096.png
     * userName : Jay
     */

    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String chat_id;
        private String userPhoto;
        private String userName;

        public String getChat_id() {
            return chat_id;
        }

        public void setChat_id(String chat_id) {
            this.chat_id = chat_id;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

	@Override
	public String toString() {
		return "NoticeBean [status=" + status + ", list=" + list + "]";
	}
    
}
