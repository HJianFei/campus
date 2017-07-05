package com.loosoo100.campus100.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.activities.LoginActivity;
import com.loosoo100.campus100.activities.MessageActivity;
import com.loosoo100.campus100.anyevent.MEventAddFriendNoRead;
import com.loosoo100.campus100.anyevent.MEventCampusNoRead;
import com.loosoo100.campus100.anyevent.MEventCampusNoReadFriend;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.anyevent.MEventMessageNoRead;
import com.loosoo100.campus100.chat.ui.NewFriendListActivity;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;
import com.ta.utdid2.android.utils.StringUtils;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class JPushReceiver extends BroadcastReceiver {

    private String uid = "";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        this.mContext = context;
        uid = context.getSharedPreferences(UserInfoDB.USERTABLE, context.MODE_PRIVATE).getString(UserInfoDB.USERID, "");

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // 逻辑代码
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            processCustomMessage(mContext, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //通知获取系统消息
            new Thread() {
                public void run() {
                    int count = GetData.getNoReadCount(MyConfig.URL_JSON_MESSAGE_NOREAD + uid);
                    UserInfoDB.setUserInfo(mContext, UserInfoDB.NOREAD_MESSAGE_COUNT, count + "");
                    EventBus.getDefault().post(new MEventMessageNoRead(true));
                }
            }.start();

            if (bundle.getString(JPushInterface.EXTRA_ALERT).contains("不同设备登录了")) {
                if (context.getSharedPreferences(UserInfoDB.USERTABLE, context.MODE_PRIVATE)
                        .getString(UserInfoDB.ORG, "").equals("0")) {
                    Intent i = new Intent(context, HomeActivity.class); //
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, BossHomeActivity.class); //
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                Intent intent2 = new Intent(context, LoginActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
                EventBus.getDefault().post(new MEventHome(true));
                UserInfoDB.clearUserInfo(context);

                ToastUtil.showToast(context, "您的账号在其它设备登录了");

            }
            if (bundle.getString(JPushInterface.EXTRA_ALERT).contains("暗恋")) {
                new Thread() {
                    public void run() {
                        int count = GetData.getCampusContactsNoRead(MyConfig.URL_JSON_CAMPUS_NOREAD + uid);
                        UserInfoDB.setUserInfo(mContext, UserInfoDB.CAMPUS_NOREAD_LOVE, count + "");
//                        UserInfoDB.setUserInfo(mContext, UserInfoDB.CAMPUS_NOREAD_HOME_LOVE, count + "");
                        EventBus.getDefault().post(new MEventCampusNoRead(true));
                    }
                }.start();
            } else if (bundle.getString(JPushInterface.EXTRA_ALERT).contains("互换")) {
                new Thread() {
                    public void run() {
                        int count = GetData.getCampusContactsNoRead(MyConfig.URL_JSON_CAMPUS_NOREAD_FRIEND + uid);
                        UserInfoDB.setUserInfo(mContext, UserInfoDB.CAMPUS_NOREAD_FRIEND, count + "");
                        UserInfoDB.setUserInfo(mContext, UserInfoDB.CAMPUS_NOREAD_FRIEND_DETAIL, count + "");
//                        UserInfoDB.setUserInfo(mContext, UserInfoDB.CAMPUS_NOREAD_HOME_FRIEND, count + "");
                        EventBus.getDefault().post(new MEventCampusNoReadFriend(true));
                    }

                    ;
                }.start();
            } else if (bundle.getString(JPushInterface.EXTRA_ALERT).contains("请求添加您为好友")) {
                EventBus.getDefault().post(new MEventAddFriendNoRead(true));
            }

            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

//            LogUtils.d("onResponse", "用户点击打开了通知");
//            openNotification(context, bundle);
// String title = "";
            // 在这里可以自己写代码去定义用户点击后的行为
            //除了设备登录，其它全部跳转到消息列表界面
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogUtils.d("responseInfo", type);
            if (!uid.equals("") && content.contains("请求添加您为好友")) {
                Intent i = new Intent(context, NewFriendListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else if (!uid.equals("") && !content.contains("不同设备登录了")) {
                Intent i = new Intent(context, MessageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }

        }
    }

//    private void openNotification(Context context, Bundle bundle) {
//        String string = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        LogUtils.d("onResponse", string);
//
//    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.d("responseInfo", "收到自定义消息**title->" + title + "**message->" + message + "**extras->" + extras);
        if (StringUtils.isEmpty(title)) {
            return;
        }

    }
}
