package com.loosoo100.campus100.chat.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.loosoo100.campus100.chat.bean.Friend;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by HJianFei on 2016/10/12.
 */

public class FriendDao {

    private Context mContext;
    private DbHelper mDbHelper;
    private Dao<Friend, Integer> mFriendDao;

    public FriendDao(Context context) {
        mContext = context;
        mDbHelper = DbHelper.getmInstace(mContext);
        try {
            mFriendDao = mDbHelper.getDao(Friend.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据
     *
     * @param friend
     */
    public void addFriend(Friend friend) {
        try {
            mFriendDao.create(friend);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过ID查询
     *
     * @return
     * @paam id
     */
    public Friend queryFriendById(int id) {
        try {
            Friend friend = mFriendDao.queryForId(id);
            return friend;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有的的的数据
     *
     * @return
     */
    public List<Friend> queryFriendAll() {
        try {
            List<Friend> friends = mFriendDao.queryForAll();
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 匹配查询
     * SELECT * FROM tb_friend  WHERE uid='' AND nickname = '';
     *
     * @param uid
     * @return
     */
    public List<Friend> queryWhereEq(String uid) {
        QueryBuilder queryBuilder = mFriendDao.queryBuilder();
        try {
            List<Friend> friendList = queryBuilder.where().notIn("uid", uid).query();
            return friendList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 匹配查询
     * SELECT * FROM tb_friend  WHERE uid='' AND nickname = '';
     *
     * @param uid
     * @return
     */
    public Friend queryWhereById(String uid) {
        QueryBuilder queryBuilder = mFriendDao.queryBuilder();
        try {
            List<Friend> friendList = queryBuilder.where().eq("uid", uid).query();
            return friendList.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//    /**
//     * like查询
//     * @param name
//     *
//     * @param sex
//     * @return
//     */
//    public  List<User> queryWhereLike(String name,String sex){
//        QueryBuilder queryBuilder = mUserDao.queryBuilder();
//        try {
//            List<User> userList = queryBuilder.where().eq("sex",sex).query();
//            return userList;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 删除name = name的数据,
     *
     * @param uid
     */
    public void delete(String uid) {
        DeleteBuilder deleteBuilder = mFriendDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("uid", uid);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        DeleteBuilder deleteBuilder = mFriendDao.deleteBuilder();
        try {
            deleteBuilder.where().isNotNull("id");
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 更新数据
//     * @param name
//     * @param sex
//     */
//    public void update(String nickname,String sex){
//        UpdateBuilder updateBuilder = mFriendDao.updateBuilder();
//        try {
//            updateBuilder.updateColumnValue("nickname",nickname).where().eq("",sex);
//            updateBuilder.update();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}
