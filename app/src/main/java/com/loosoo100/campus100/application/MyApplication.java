package com.loosoo100.campus100.application;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.activities.CompanySummaryActivity;
import com.loosoo100.campus100.chat.ui.MapLocationActivity;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.zzboss.activities.BossCommDetailActivity;
import com.loosoo100.campus100.zzboss.activities.BossCompanySummaryActivity;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.LocationMessage;

public class MyApplication extends MultiDexApplication {
    private static RequestQueue mQueue;
    public static Context applicationContext;
    private static MyApplication instance;
    private String uid = "";
    private String cuid = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(this);
        applicationContext = this;
        instance = this;
        RongIM.init(this);
        //会话界面，点击好友头像，进入好友主页
        initConversationClickListener();
        setInputProvider();

        /**
         * 极光推送
         */
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

    }

    private void setInputProvider() {
        InputProvider.ExtendProvider[] muiltiProvider = {
                new ImageInputProvider(RongContext.getInstance()),
                new LocationInputProvider(RongContext.getInstance()),//地理位置
        };
        RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, muiltiProvider);
        RongIM.resetInputExtensionProvider(Conversation.ConversationType.GROUP, muiltiProvider);
    }

    /**
     * 会话界面点击好友头像，进入好友主页
     */
    private void initConversationClickListener() {

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                        .getString(UserInfoDB.USERID, "");
                cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
                if (!uid.equals("") && userInfo.getUserId().equals(uid)) {
                    Intent intent = new Intent(getApplicationContext(),
                            CampusContactsPersonalActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (!uid.equals("") && !userInfo.getUserId().equals(uid) && !userInfo.getUserId().startsWith("cmp")) {
                    Intent intent = new Intent(getApplicationContext(),
                            CampusContactsFriendActivity.class);
                    intent.putExtra("muid", userInfo.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (!uid.equals("") && !userInfo.getUserId().equals(uid) && userInfo.getUserId().startsWith("cmp")) {
                    Intent intent = new Intent(getApplicationContext(), CompanySummaryActivity.class);
                    intent.putExtra("cuid", userInfo.getUserId().substring(3));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } else if (!cuid.equals("") && userInfo.getUserId().equals("cmp" + cuid)) {

                    Intent intent = new Intent(getApplicationContext(),
                            BossCompanySummaryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (!cuid.equals("") && !userInfo.getUserId().equals("cmp" + cuid)) {

                    Intent intent = new Intent(getApplicationContext(),
                            BossCommDetailActivity.class);
                    intent.putExtra("company", userInfo.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(final Context context, View view, final Message message) {

                // 消息点击事件，判断如果是位置消息就取出Content()跳转到地图activity
                if (message.getContent() instanceof LocationMessage) {
                    Intent intent = new Intent(context, MapLocationActivity.class);
                    intent.putExtra("location", message.getContent());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });

    }

    public static RequestQueue getRequestQueue() {
        return mQueue;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
