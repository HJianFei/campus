package com.loosoo100.campus100.zzboss.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.LoginActivity;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.beans.VersionInfo;
import com.loosoo100.campus100.chat.bean.Company_Info;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.chat.widget.DragPointView;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.AppUpdateDialog;
import com.loosoo100.campus100.zzboss.beans.BossCompanyInfo;
import com.loosoo100.campus100.zzboss.fragments.BossHomeFragment;
import com.loosoo100.campus100.zzboss.fragments.BossMessageFragment;
import com.loosoo100.campus100.zzboss.fragments.BossPersonalFragment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

/**
 * @author yang 主activity
 */
public class BossHomeActivity extends FragmentActivity implements OnClickListener, DragPointView.OnDragListencer {
    @ViewInject(R.id.rl_home)
    private RelativeLayout rl_home; // 底部首页布局
    @ViewInject(R.id.rl_message)
    private RelativeLayout rl_message; // 底部消息布局
    @ViewInject(R.id.rl_personal)
    private RelativeLayout rl_personal; // 底部我的布局

    @ViewInject(R.id.iv_home)
    private ImageView iv_home; // 底部首页图标
    @ViewInject(R.id.iv_message)
    private ImageView iv_message; // 底部消息图标
    @ViewInject(R.id.iv_personal)
    private ImageView iv_personal; // 底部我的图标
    @ViewInject(R.id.iv_circle)
    private ImageView iv_circle; // 我的引导红点

    private boolean isFirstExit = true; // 记录是否第一次按返回键

    private int fragmentIndex = 1; // 记录上次fragment的序号

    private String cuid = "";
    private BossCompanyInfo companyInfo = null;
    private VersionInfo versionInfo = null; // 判断版本

    private BossHomeFragment homeFragment;
    private BossMessageFragment messageFragment;
    private BossPersonalFragment personalFragment;
    private ScaleAnimation scaleAnimation; // 底部按钮动画

    private int versionCode = -1;

    //消息未读
    @ViewInject(R.id.unread_num)
    private DragPointView unread_num;
    //加载对话框
    private SweetAlertDialog mDialog;

    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    private RemoteViews remoteView;

    private Handler handler01 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            iv_home.startAnimation(scaleAnimation);
            resetBelowButtonColor();
            iv_home.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.recommend_select));
        }

        ;
    };

    private Handler handler02 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            iv_message.startAnimation(scaleAnimation);
            resetBelowButtonColor();
            iv_message.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_select));
        }

        ;
    };

    private Handler handler05 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            iv_personal.startAnimation(scaleAnimation);
            resetBelowButtonColor();
            iv_personal.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.personal_select));
        }

        ;
    };

    private Handler handlerInfo = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (MyConfig.isCheckVersion) {
                MyConfig.isCheckVersion = false;
                return;
            }
            // 判断版本号
            if (versionInfo != null && versionCode != -1
                    && versionCode != versionInfo.getVersion()) {
                AppUpdateDialog.Builder builderSure = new AppUpdateDialog.Builder(
                        BossHomeActivity.this);
                builderSure.setMessage(versionInfo.getDescription());
                if (versionInfo.getStatus()==1){
                    builderSure.setPositiveButton("下载更新",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    MyConfig.isCheckVersion = true;
                                    remoteView = new RemoteViews(
                                            BossHomeActivity.this.getPackageName(),
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
                        BossHomeActivity.this.finish();
                    }
                });
                }else{
                    builderSure.setPositiveButton("下载更新",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    MyConfig.isCheckVersion = true;
                                    remoteView = new RemoteViews(
                                            BossHomeActivity.this.getPackageName(),
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

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (companyInfo == null) {
                return;
            }
            if (companyInfo.getStatus() == -1) {
                // 停止推送
                JPushInterface.setAlias(BossHomeActivity.this, "", null);
                // 清除本地用户
                UserInfoDB.clearUserInfo(BossHomeActivity.this);
                EventBus.getDefault().post(new MEventHome(true));
                ToastUtil.showToast(BossHomeActivity.this, "账号不存在");
                Intent intent = new Intent(BossHomeActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_main);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("数据加载中");
        mDialog.show();
        //获取企业信息
        getCompany_Info(cuid);
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

        if (cuid == null || cuid.equals("")) {
            Intent intent = new Intent(BossHomeActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // 极光推送设置别名为用户ID
        JPushInterface.setAlias(this, "org" + MyUtils.getIMEI(this) + cuid,
                null);

        new Thread() {
            public void run() {
                companyInfo = GetData
                        .getBossUserInfo(MyConfig.URL_JSON_COMPANYINFO + cuid);
                if (!isDestroyed()) {
                    handler2.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        initView();

        if (!cuid.equals("")) {
            new Thread() {
                public void run() {
                    versionInfo = GetData
                            .getvVersionInfo(MyConfig.URL_JSON_VERSION);
                    if (!isDestroyed()) {
                        handlerInfo.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }

        scaleAnimation = MyAnimation.getScaleAnimationDown();

        homeFragment = new BossHomeFragment();
        messageFragment = new BossMessageFragment();
        personalFragment = new BossPersonalFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, homeFragment);
        ft.commit();
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            reconnect();
        }
        initData();

    }

    private void getCompany_Info(final String cuid) {
        GsonRequest<Company_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_COMPANY_INFO + "?companyId=" + cuid, Company_Info.class, new Response.Listener<Company_Info>() {
            @Override
            public void onResponse(Company_Info company_info) {
                SharedPreferencesUtils.setParam(BossHomeActivity.this, "token", company_info.getRy_token());
                SharedPreferencesUtils.setParam(BossHomeActivity.this, "companyID", "cmp" + company_info.getCompany_id());
                SharedPreferencesUtils.setParam(BossHomeActivity.this, "company_name", company_info.getCompany_name());
                SharedPreferencesUtils.setParam(BossHomeActivity.this, "company_avatar", MyConfig.PIC_AVATAR + company_info.getCompany_logo());
                if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                    reconnect();
                }
                mDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
                LogUtils.d("volleyError", volleyError.toString());


            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);

    }

    protected void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
            }
        }, 500);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                String cacheToken = (String) SharedPreferencesUtils.getParam(BossHomeActivity.this, "token", "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(BossHomeActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                reconnect();

                            }

                            @Override
                            public void onSuccess(String s) {
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                    }
                }
            }
        }
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");
            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
//                            startActivity(new Intent(HomeActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count == 0) {
                unread_num.setVisibility(View.GONE);
            } else if (count > 0 && count < 100) {
                unread_num.setVisibility(View.VISIBLE);
                unread_num.setText(count + "");
            } else {
                unread_num.setVisibility(View.VISIBLE);
                unread_num.setText("99+");
            }
        }
    };

    private void reconnect() {
        String token = (String) SharedPreferencesUtils.getParam(BossHomeActivity.this, "token", "");
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtils.d("onResponse", "融云token过期");
                refreshToken();

            }

            @Override
            public void onSuccess(String s) {
                LogUtils.d("onResponse", "sss" + s);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                LogUtils.d("onResponse", e.toString());
                refreshToken();

            }
        });

    }

    private void refreshToken() {
        GsonRequest<Company_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_TOKEN_FROM_RONGYUN + "?companyId=" + cuid, Company_Info.class, new Response.Listener<Company_Info>() {
            @Override
            public void onResponse(Company_Info company_info) {
                SharedPreferencesUtils.setParam(BossHomeActivity.this, "token", company_info.getRy_token() + "");
                reconnect();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void initView() {
        rl_home.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_personal.setOnClickListener(this);
        unread_num.setOnClickListener(this);
        unread_num.setDragListencer(this);
    }

    /**
     * 更改fargment
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from == null || to == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!to.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.fl_container, to).commit();
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
    }

    /**
     * 监听单击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                new Thread() {
                    public void run() {
                        handler01.sendEmptyMessage(0);
                    }

                    ;
                }.start();
                switch (fragmentIndex) {
                    case 1:
                        break;
                    case 2:
                        switchFragment(messageFragment, homeFragment);
                        break;
                    case 3:
                        switchFragment(personalFragment, homeFragment);
                        break;

                }
                fragmentIndex = 1;
                break;

            case R.id.rl_message:
                new Thread() {
                    public void run() {
                        handler02.sendEmptyMessage(0);
                    }

                    ;
                }.start();
                switch (fragmentIndex) {
                    case 1:
                        switchFragment(homeFragment, messageFragment);
                        break;
                    case 2:
                        break;
                    case 3:
                        switchFragment(personalFragment, messageFragment);
                        break;

                }
                fragmentIndex = 2;
                break;

            case R.id.rl_personal:
                new Thread() {
                    public void run() {
                        handler05.sendEmptyMessage(0);
                    }

                    ;
                }.start();
                switch (fragmentIndex) {
                    case 1:
                        switchFragment(homeFragment, personalFragment);
                        break;
                    case 2:
                        switchFragment(messageFragment, personalFragment);
                        break;
                    case 3:
                        break;
                }
                fragmentIndex = 3;
                break;
        }
    }

    /**
     * 底部按钮背景还原
     */
    private void resetBelowButtonColor() {
        iv_home.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.recommend));
        iv_message.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.message));
        iv_personal.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.personal));
    }

    /**
     * 监听手机按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断2秒内是否按了两次返回键，如果是则退出应用，否则提示用户再按一次退出程序
            if (isFirstExit) {
                ToastUtil.showToast(BossHomeActivity.this, "再按一次退出程序");
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
                MyUtils.installAPK(BossHomeActivity.this, target);
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
                ToastUtil.showToast(BossHomeActivity.this, "下载失败");
            }
        });
    }

    public void onEventMainThread(MEventHome event) {
        if (event.isFinish()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        //退出融云账号
//        RongIM.getInstance().logout();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDragOut() {
        unread_num.setVisibility(View.GONE);
        ToastUtil.showToast(BossHomeActivity.this, "清除成功");
        List<Conversation> conversations = RongIM.getInstance().getConversationList();
        if (conversations != null && conversations.size() > 0) {
            for (Conversation c : conversations) {
                RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
            }
        }
    }
}
