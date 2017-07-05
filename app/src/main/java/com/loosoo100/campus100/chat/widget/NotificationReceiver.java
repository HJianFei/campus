package com.loosoo100.campus100.chat.widget;

import android.content.Context;
import android.content.Intent;

import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by HJianFei on 2016/10/31.
 */

public class NotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        Intent intent;
        String uid = context.getSharedPreferences(UserInfoDB.USERTABLE, context.MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        String cuid = context.getSharedPreferences(UserInfoDB.USERTABLE, context.MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
        if (!uid.equals("") && cuid.equals("")) {
            intent = new Intent(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (uid.equals("") && !cuid.equals("")) {
            intent = new Intent(context, BossHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        return false;
    }
}
