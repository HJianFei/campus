package com.loosoo100.campus100.chat.bean;

/**
 * Created by HJianFei on 2016/10/31.
 */

public class Community_Info {

    /**
     * community_id : 4762
     * community_name : Android程序猿
     * community_logothumb : http://ocaxbuib5.bkt.clouddn.com/community_2016-10-26_5810379fb33b4.png/thumb
     * code : 200
     */

    private String community_id;
    private String community_name;
    private String community_logothumb;
    private int code;

    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String getCommunity_logothumb() {
        return community_logothumb;
    }

    public void setCommunity_logothumb(String community_logothumb) {
        this.community_logothumb = community_logothumb;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
