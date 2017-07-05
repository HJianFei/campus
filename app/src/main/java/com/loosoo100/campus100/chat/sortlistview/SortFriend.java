package com.loosoo100.campus100.chat.sortlistview;

import java.io.Serializable;

/**
 * Created by HJianFei on 2016/10/13.
 */

public class SortFriend implements Serializable {

    private int id;
    private String uid;
    private String nickname;
    private String avatar;
    private String letters;

    public SortFriend() {
    }

    public SortFriend(int id, String uid, String nickname, String avatar, String letters) {
        this.id = id;
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.letters = letters;
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

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    @Override
    public String toString() {
        return "SortFriend{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", letters='" + letters + '\'' +
                '}';
    }
}
