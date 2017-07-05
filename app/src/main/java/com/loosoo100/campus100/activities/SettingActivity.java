package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.beans.VersionInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.DataCleanManager;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.AppUpdateDialog;
import com.loosoo100.campus100.view.CustomDialog;

import java.io.File;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * @author yang 设置activity
 */
public class SettingActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;

    @ViewInject(R.id.ll_service)
    private LinearLayout ll_service; // 服务条款

    @ViewInject(R.id.ll_clear)
    private LinearLayout ll_clear; // 清除缓存
    // @ViewInject(R.id.tv_size)
    // private TextView tv_size; // 缓存大小

    @ViewInject(R.id.ll_update)
    private LinearLayout ll_update; // 检测更新

    @ViewInject(R.id.ll_about)
    private LinearLayout ll_about; // 关于我们

    @ViewInject(R.id.btn_exit)
    private Button btn_exit; // 退出登录

    private Intent intent = new Intent();

    private VersionInfo versionInfo = null; // 判断版本
    private int versionCode = -1;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    private RemoteViews remoteView;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (versionInfo == null) {
                ToastUtil.showToast(SettingActivity.this,"请检查网络");
                return;
            }
            // 判断版本号
            if (versionInfo != null && versionCode != -1
                    && versionCode != versionInfo.getVersion()) {
                AppUpdateDialog.Builder builderSure = new AppUpdateDialog.Builder(
                        SettingActivity.this);
                builderSure.setMessage(versionInfo.getDescription());
                    builderSure.setPositiveButton("下载更新",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    MyConfig.isCheckVersion = true;
                                    remoteView = new RemoteViews(
                                            SettingActivity.this.getPackageName(),
                                            R.layout.notification);
                                    remoteView.setImageViewResource(R.id.iv_icon,
                                            R.drawable.app_logo);
                                    remoteView.setTextViewText(R.id.tv_percent,
                                            "0%");
                                    remoteView.setProgressBar(R.id.progressBar,
                                            100, 0, false);
                                    // 设置通知栏显示内容
                                    mNotification.icon = R.drawable.app_logo;
                                    mNotification.tickerText = "开始下载";
                                    mNotification.contentView = remoteView;
                                    mNotification.flags = Notification.FLAG_NO_CLEAR;
                                    // 发出通知
                                    mNotificationManager.notify(0, mNotification);
                                    new Thread() {
                                        public void run() {
                                            download(versionInfo.getUrl(),
                                                    MyUtils.getInnerSDCardPath()
                                                            + "/campus100.apk");
                                        }

                                        ;
                                    }.start();
                                }
                            });
                builderSure.create().show();
            }else {
                ToastUtil.showToast(SettingActivity.this,"已是最新版本");
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_clear.setOnClickListener(this);
        ll_update.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        // tv_size.setText(DataCleanManager.getCacheSize(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            // 服务条款
            case R.id.ll_service:
                Intent intentService = new Intent(this, ServiceActivity.class);
                startActivity(intentService);
                break;

            // 清除缓存
            case R.id.ll_clear:
                DataCleanManager.cleanApplicationData(this);
                // tv_size.setText(DataCleanManager.getCacheSize(this));
                break;

            // 检测更新
            case R.id.ll_update:
                if (MyConfig.isCheckVersion) {
                    ToastUtil.showToast(SettingActivity.this,"请稍候...");
                    return;
                }
                new Thread() {
                    public void run() {
                        versionInfo = GetData.getvVersionInfo(MyConfig.URL_JSON_VERSION);
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            // 关于我们
            case R.id.ll_about:
                Intent intent2 = new Intent(this, AboutUsActivity.class);
                startActivity(intent2);
                break;

            // 退出登录
            case R.id.btn_exit:
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否确认退出账号");
                builderDel.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 停止推送
                        JPushInterface.setAlias(SettingActivity.this, "", null);
                        // 清除本地用户
                        UserInfoDB.clearUserInfo(SettingActivity.this);
                        EventBus.getDefault().post(new MEventHome(true));
                        //退出融云账号
                        SharedPreferencesUtils.setParam(SettingActivity.this, "token", "");
                        //删除缓存
                        SharedPreferencesUtils.setParam(SettingActivity.this, "school_name", "");
                        SharedPreferencesUtils.setParam(SettingActivity.this, "user_name", "");
                        SharedPreferencesUtils.setParam(SettingActivity.this, "user_avatar", "");
                        SharedPreferencesUtils.setParam(SettingActivity.this, "school_id", "");
                        SharedPreferencesUtils.setParam(SettingActivity.this, "companyID", "");
                        SharedPreferencesUtils.setParam(SettingActivity.this, "company_name", "");
                        SharedPreferencesUtils.setParam(SettingActivity.this, "company_avatar", "");
                        RongIM.getInstance().logout();
                        ToastUtil.showToast(SettingActivity.this,"您已退出账号");
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();

                break;

        }
    }

    /**
     * 下载新版本的apk
     *
     * @param url
     * @param target
     */
    private void download(String url, final String target) {
        File file = new File(target);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HttpUtils http = new HttpUtils();
        http.download(url, target, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                LogUtils.d("responseInfo",arg0.toString());
                MyConfig.isCheckVersion = false;
                // 取消通知栏通知
                mNotificationManager.cancel(0);
                // 跳到安装界面
                MyUtils.installAPK(SettingActivity.this, target);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                remoteView.setProgressBar(R.id.progressBar, 100, (int) ((float) current / total * 100), false);
                remoteView.setTextViewText(R.id.tv_percent, (int) ((float) current / total * 100) + "%");
                mNotificationManager.notify(0, mNotification);
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.d("error",arg1.toString());
                MyConfig.isCheckVersion = false;
                mNotificationManager.cancel(0);
                ToastUtil.showToast(SettingActivity.this,"下载失败");
            }
        });

    }
}
