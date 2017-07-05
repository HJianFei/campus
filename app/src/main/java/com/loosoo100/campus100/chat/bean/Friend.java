package com.loosoo100.campus100.chat.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by HJianFei on 2016/10/12.
 */

@DatabaseTable(tableName = "tb_friend")
public class Friend implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "uid")
    private String uid;
    @DatabaseField(columnName = "nickname")
    private String nickname;
    @DatabaseField(columnName = "avatar")
    private String avatar;

    public Friend(int id, String uid, String nickname, String avatar) {
        this.id = id;
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public Friend() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
