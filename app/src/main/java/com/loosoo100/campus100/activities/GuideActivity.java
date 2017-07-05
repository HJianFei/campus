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
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.beans.VersionInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.AppUpdateDialog;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yang 开机页面activity
 */
public class GuideActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.vp)
    private ViewPager vp;
    private List<View> viewList = new ArrayList<View>();

    private View guide_01;
    private View guide_02;
    private View guide_03;
    private View guide_04;
    // private View guide_login;

    private ImageView iv01;
    private ImageView iv02;
    private ImageView iv03;
    private ImageView iv04;
    private Button btn_in;
    // private ImageView iv_login_bg;

    // 记录2秒内是否第一次按下返回键
    private boolean isFirstExit = true;

    private VersionInfo versionInfo = null; // 判断版本
    private int versionCode = -1;

    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    private RemoteViews remoteView;

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
                // 判断版本号
                if (versionInfo != null && versionCode != -1
                        && versionCode != versionInfo.getVersion()) {
                    AppUpdateDialog.Builder builderSure = new AppUpdateDialog.Builder(
                            GuideActivity.this);
                    builderSure.setMessage(versionInfo.getDescription());
                    if (versionInfo.getStatus() == 1) {
                        builderSure.setPositiveButton("下载更新",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        MyConfig.isCheckVersion = true;
                                        remoteView = new RemoteViews(
                                                GuideActivity.this.getPackageName(),
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
                        builderSure.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GuideActivity.this.finish();
                            }
                        });
                    } else {
                        builderSure.setPositiveButton("下载更新",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        MyConfig.isCheckVersion = true;
                                        remoteView = new RemoteViews(
                                                GuideActivity.this.getPackageName(),
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
                    }
                    builderSure.create().show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewUtils.inject(this);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(),
                    0);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();

        new Thread() {
            public void run() {
                versionInfo = GetData
                        .getvVersionInfo(MyConfig.URL_JSON_VERSION);
                handler2.sendEmptyMessage(0);
            }

            ;
        }.start();

        LayoutInflater inflater = LayoutInflater.from(this);

        guide_01 = inflater.inflate(R.layout.guide_01, null);
        guide_02 = inflater.inflate(R.layout.guide_02, null);
        guide_03 = inflater.inflate(R.layout.guide_03, null);
        guide_04 = inflater.inflate(R.layout.guide_04, null);
        // guide_login = inflater.inflate(R.layout.guide_login, null);

        iv01 = (ImageView) guide_01.findViewById(R.id.iv01);
        iv02 = (ImageView) guide_02.findViewById(R.id.iv02);
        iv03 = (ImageView) guide_03.findViewById(R.id.iv03);
        iv04 = (ImageView) guide_04.findViewById(R.id.iv04);
        btn_in = (Button) guide_04.findViewById(R.id.btn_in);
        btn_in.setOnClickListener(this);
        // iv_login_bg = (ImageView) guide_login.findViewById(R.id.iv_login_bg);

        iv01.setImageBitmap(GetData.getBitMap(this, R.drawable.guide01));
        iv02.setImageBitmap(GetData.getBitMap(this, R.drawable.guide02));
        iv03.setImageBitmap(GetData.getBitMap(this, R.drawable.guide03));
        iv04.setImageBitmap(GetData.getBitMap(this, R.drawable.guide04));
        // iv_login_bg
        // .setImageBitmap(GetData.getBitMap(this, R.drawable.login_bg));

        viewList.add(guide_01);
        viewList.add(guide_02);
        viewList.add(guide_03);
        viewList.add(guide_04);
        // viewList.add(guide_login);
        vp.setAdapter(new ViewPagerAdapter(viewList));

    }

    /**
     * 监听手机按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断2秒内是否按了两次返回键，如果是则退出应用，否则提示用户再按一次退出程序
            if (isFirstExit) {
                ToastUtil.showToast(GuideActivity.this, "再按一次退出程序");
                isFirstExit = false;
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isFirstExit = true;
                    }

                    ;
                }.start();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_in:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
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
                LogUtils.d("responseInfo", arg0.toString());
                MyConfig.isCheckVersion = false;
                // 取消通知栏通知
                mNotificationManager.cancel(0);
                // 跳到安装界面
                MyUtils.installAPK(GuideActivity.this, target);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                remoteView.setProgressBar(R.id.progressBar, 100,
                        (int) ((float) current / total * 100), false);
                remoteView.setTextViewText(R.id.tv_percent,
                        (int) ((float) current / total * 100) + "%");
                mNotificationManager.notify(0, mNotification);
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.d("error", arg1.toString());
                MyConfig.isCheckVersion = false;
                mNotificationManager.cancel(0);
                ToastUtil.showToast(GuideActivity.this, "下载失败");
            }
        });
    }
}
