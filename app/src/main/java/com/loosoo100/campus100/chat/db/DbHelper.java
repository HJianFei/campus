package com.loosoo100.campus100.chat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.loosoo100.campus100.chat.bean.Friend;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by HJianFei on 2016/10/12.
 */

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/campus100/campus100.db";
    private static final int DB_VERSION = 1;
    private HashMap<String, Dao> mDaos = new HashMap<>();

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static DbHelper mInstance;

    public synchronized static DbHelper getmInstace(Context context) {
        if (mInstance == null) {
            synchronized (DbHelper.class) {
                if (mInstance == null) {
                    mInstance = new DbHelper(context);
                }
            }
        }
        return mInstance;
    }

    public synchronized Dao getDao(Class clz) throws SQLException {
        Dao dao = null;
        String className = clz.getSimpleName();
        if (mDaos.containsKey(className)) {
            dao = mDaos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clz);
            mDaos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : mDaos.keySet()) {
            Dao dao = mDaos.get(key);
            dao = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Friend.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Friend.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
