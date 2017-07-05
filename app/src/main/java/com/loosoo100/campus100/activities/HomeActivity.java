package com.loosoo100.campus100.activities;

import android.app.Dialog;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventAddFriendCount;
import com.loosoo100.campus100.anyevent.MEventCampusNoRead;
import com.loosoo100.campus100.anyevent.MEventCampusNoReadFriend;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.anyevent.MEventMessageNoRead;
import com.loosoo100.campus100.anyevent.MEventStoreCart;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.beans.VersionInfo;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.User_Info;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.chat.ui.NewFriendListActivity;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.chat.widget.DragPointView;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.fragments.FoundFragment;
import com.loosoo100.campus100.fragments.HomeFragment;
import com.loosoo100.campus100.fragments.MessageFragment;
import com.loosoo100.campus100.fragments.PersonalFragment;
import com.loosoo100.campus100.fragments.StoreFragment;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.AppUpdateDialog;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class HomeActivity extends FragmentActivity implements OnClickListener, DragPointView.OnDragListencer {
    @ViewInject(R.id.rl_home)
    private RelativeLayout rl_home; // 底部首页布局
    @ViewInject(R.id.rl_community)
    private RelativeLayout rl_community; // 底部社团布局
    @ViewInject(R.id.rl_store)
    private RelativeLayout rl_store; // 底部小卖部布局
    @ViewInject(R.id.rl_found)
    private RelativeLayout rl_found; // 底部发现布局
    @ViewInject(R.id.rl_personal)
    private RelativeLayout rl_personal; // 底部我的布局

    @ViewInject(R.id.root_below)
    public static RelativeLayout root_below; // 底部所有按钮布局
    @ViewInject(R.id.iv_below)
    public static ImageView iv_below; // 底部所有按钮布局阴影部分

    @ViewInject(R.id.iv_home)
    private ImageView iv_home; // 底部首页图标
    @ViewInject(R.id.iv_community)
    private ImageView iv_community; // 底部社团图标
    @ViewInject(R.id.iv_store)
    private ImageView iv_store; // 底部小卖部图标
    @ViewInject(R.id.iv_found)
    private ImageView iv_found; // 底部发现图标
    @ViewInject(R.id.iv_personal)
    private ImageView iv_personal; // 底部我的图标
    @ViewInject(R.id.tv_count)
    private TextView tv_count; // 发现的条数
    @ViewInject(R.id.iv_circle)
    private ImageView iv_circle; // 我的引导红点

    private boolean isFirstExit = true; // 记录是否第一次按返回键

    private int fragmentIndex = 1; // 记录上次fragment的序号

    private String uid = "";
    private String sid = "";
    private UserInfo userInfo = null;
    private UserInfo userInfo2 = null;
    private VersionInfo versionInfo = null; // 判断版本

    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private StoreFragment storeFragment;
    private FoundFragment foundFragment;
    private PersonalFragment personalFragment;
    private ScaleAnimation scaleAnimation; // 底部按钮动画
    private ScaleAnimation storeAnimation; // 小卖部动画
    private int cuentIndex = 0;
    private int versionCode = -1;
    private int noReadCount = 0;
    private int noReadCountMes = 0;
    private boolean loading = false;
    //保存本地数据库（好友列表）
    private FriendDao mFriendDao;
    @ViewInject(R.id.unread_num)
    private DragPointView unread_num;
    private SweetAlertDialog mDialog;

    private Dialog dialog;
    private String ed_name = "";

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
            iv_community.startAnimation(scaleAnimation);
            resetBelowButtonColor();
            iv_community.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.message_select));
        }

        ;
    };

    private Handler handler03 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            iv_store.startAnimation(storeAnimation);
            resetBelowButtonColor();
            iv_store.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.store_select));
        }

        ;
    };

    private Handler handler04 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            iv_found.startAnimation(scaleAnimation);
            resetBelowButtonColor();
            iv_found.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.found_select));
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
            if (userInfo != null) {
                String school = userInfo.getSchool();
                String schoolID = userInfo.getSchoolID();

                UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.NAME,
                        userInfo.getName());
                UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.SEX,
                        userInfo.getSex());
                UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.WEIXIN,
                        userInfo.getWeixin());
                UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.SCHOOL,
                        school);
                UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.SCHOOL_ID,
                        schoolID);
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.STUDENT_ID, userInfo.getStuNo());
                // 保存照片墙学校
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.SCHOOL_PICTURE, school);
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.SCHOOL_ID_PICTURE, schoolID);
                // 保存社团学校
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.SCHOOL_COMM, school);
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.SCHOOL_ID_COMM, schoolID);
                // 保存小卖部学校
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.SCHOOL_STORE, school);
                UserInfoDB.setUserInfo(HomeActivity.this,
                        UserInfoDB.SCHOOL_ID_STORE, schoolID);
            }
            if (MyConfig.isCheckVersion) {
                MyConfig.isCheckVersion = false;
                return;
            }
            // 判断版本号
            if (versionInfo != null && versionCode != -1
                    && versionCode != versionInfo.getVersion()) {
                AppUpdateDialog.Builder builderSure = new AppUpdateDialog.Builder(
                        HomeActivity.this);
                builderSure.setMessage(versionInfo.getDescription());
                if (versionInfo.getStatus() == 1) {
                    builderSure.setPositiveButton("下载更新",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    MyConfig.isCheckVersion = true;
                                    remoteView = new RemoteViews(
                                            HomeActivity.this.getPackageName(),
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
                            HomeActivity.this.finish();
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
                                            HomeActivity.this.getPackageName(),
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

        ;
    };

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (noReadCount > 0) {
                EventBus.getDefault().post(new MEventAddFriendCount(noReadCount));
                tv_count.setText(noReadCount + "");
                tv_count.setVisibility(View.VISIBLE);
            } else {
                EventBus.getDefault().post(new MEventAddFriendCount(0));
                tv_count.setVisibility(View.GONE);
            }
            if (noReadCountMes > 0) {
                iv_circle.setVisibility(View.VISIBLE);
            } else {
                iv_circle.setVisibility(View.GONE);
            }
            loading = false;
        }

    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (userInfo2 == null) {
                return;
            }
            // 保存用户余额
            UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.MONEY,
                    userInfo2.getMoney() + "");
            UserInfoDB.setUserInfo(HomeActivity.this, UserInfoDB.POINT,
                    userInfo2.getPoint() + "");
            if (userInfo2.getStatus() == -1) {
                // 停止推送
                JPushInterface.setAlias(HomeActivity.this, "", null);
                // 清除本地用户
                UserInfoDB.clearUserInfo(HomeActivity.this);
                EventBus.getDefault().post(new MEventHome(true));
                ToastUtil.showToast(HomeActivity.this, "账号不存在");
                Intent intent = new Intent(HomeActivity.this,
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
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        mFriendDao = new FriendDao(this);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("数据加载中");
        mDialog.show();
        //获取用户信息
        getUser_Info(uid);
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");

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

        if (uid == null || uid.equals("")) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // 极光推送设置别名为用户ID
        JPushInterface.setAlias(this, MyUtils.getIMEI(this) + uid, null);

        new Thread() {
            public void run() {
                userInfo2 = GetData.getUserInfo(MyConfig.URL_JSON_USERINFO
                        + uid);
                if (!isDestroyed()) {
                    handler2.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        initView();

        if (!uid.equals("")) {
            new Thread() {
                public void run() {
                    userInfo = GetData
                            .getUserBasicInfo(MyConfig.URL_JSON_USERINFO_BASIC
                                    + uid);
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
        storeAnimation = MyAnimation.getScaleAnimationStore();

        homeFragment = new HomeFragment();
        messageFragment = new MessageFragment();
        storeFragment = new StoreFragment();
        foundFragment = new FoundFragment();
        personalFragment = new PersonalFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, homeFragment);
        ft.commit();
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            reconnect();
        }
        initData();

    }

    /**
     * 获取用户信息
     *
     * @param uid
     */

    private void getUser_Info(final String uid) {
        LogUtils.d("onResponse", uid);
        GsonRequest<User_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_USER_INFO + "?userId=" + uid, User_Info.class, new Response.Listener<User_Info>() {
            @Override
            public void onResponse(User_Info user_info) {
                LogUtils.d("onResponse", user_info.getRy_token());
                SharedPreferencesUtils.setParam(HomeActivity.this, "token", user_info.getRy_token());
                FriendDao friendDao = new FriendDao(HomeActivity.this);
                friendDao.deleteAll();
                Friend friend = new Friend();
                friend.setUid(uid);
                friend.setNickname(user_info.getUserName());
                friend.setAvatar(MyConfig.PIC_AVATAR + user_info.getUserPhotoThums());
                //添加学生学校信息
                SharedPreferencesUtils.setParam(HomeActivity.this, "school_name", user_info.getSchool_name());
                SharedPreferencesUtils.setParam(HomeActivity.this, "user_name", user_info.getUserName());
                SharedPreferencesUtils.setParam(HomeActivity.this, "user_avatar", MyConfig.PIC_AVATAR + user_info.getUserPhotoThums());
                SharedPreferencesUtils.setParam(HomeActivity.this, "school_id", user_info.getSchool_shopid());
                friendDao.addFriend(friend);
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
                String cacheToken = (String) SharedPreferencesUtils.getParam(HomeActivity.this, "token", "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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
                            startActivity(new Intent(HomeActivity.this, NewFriendListActivity.class));
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
        String token = (String) SharedPreferencesUtils.getParam(HomeActivity.this, "token", "");
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
        GsonRequest<User_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_TOKEN_FROM_RONGYUN + "?userId=" + uid, User_Info.class, new Response.Listener<User_Info>() {
            @Override
            public void onResponse(User_Info user_info) {
                LogUtils.d("onResponse", user_info.toString());
                SharedPreferencesUtils.setParam(HomeActivity.this, "token", user_info.getRy_token() + "");
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
        rl_community.setOnClickListener(this);
        rl_store.setOnClickListener(this);
        rl_found.setOnClickListener(this);
        rl_personal.setOnClickListener(this);
        unread_num.setOnClickListener(this);
        unread_num.setDragListencer(this);
    }

    /**
     * 更改fargment
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (!loading) {
            loading = true;
            new Thread() {
                public void run() {
                    noReadCount = getNoReadCount(MyConfig.URL_JSON_CAMPUS_NOREAD_COUNT
                            + uid);
                    if (!isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }
            }.start();
        }
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
                        switchFragment(storeFragment, homeFragment);
                        break;
                    case 4:
                        switchFragment(foundFragment, homeFragment);
                        break;
                    case 5:
                        switchFragment(personalFragment, homeFragment);
                        break;

                }
                fragmentIndex = 1;
                break;

            case R.id.rl_community:
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
                        switchFragment(storeFragment, messageFragment);
                        break;
                    case 4:
                        switchFragment(foundFragment, messageFragment);
                        break;
                    case 5:
                        switchFragment(personalFragment, messageFragment);
                        break;

                }
                fragmentIndex = 2;
                break;

            case R.id.rl_store:
                new Thread() {
                    public void run() {
                        handler03.sendEmptyMessage(0);
                    }

                    ;
                }.start();
                switch (fragmentIndex) {
                    case 1:
                        switchFragment(homeFragment, storeFragment);
                        break;
                    case 2:
                        switchFragment(messageFragment, storeFragment);
                        break;
                    case 3:
                        break;
                    case 4:
                        switchFragment(foundFragment, storeFragment);
                        break;
                    case 5:
                        switchFragment(personalFragment, storeFragment);
                        break;

                }
                fragmentIndex = 3;
                break;

            case R.id.rl_found:
                new Thread() {
                    public void run() {
                        handler04.sendEmptyMessage(0);
                    }

                    ;
                }.start();
                switch (fragmentIndex) {
                    case 1:
                        switchFragment(homeFragment, foundFragment);
                        break;
                    case 2:
                        switchFragment(messageFragment, foundFragment);
                        break;
                    case 3:
                        switchFragment(storeFragment, foundFragment);
                        break;
                    case 4:
                        break;
                    case 5:
                        switchFragment(personalFragment, foundFragment);
                        break;

                }
                fragmentIndex = 4;
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
                        switchFragment(storeFragment, personalFragment);
                        break;
                    case 4:
                        switchFragment(foundFragment, personalFragment);
                        break;
                    case 5:
                        break;
                }
                fragmentIndex = 5;
                break;
        }
    }

    /**
     * 底部按钮背景还原
     */
    private void resetBelowButtonColor() {
        iv_home.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.recommend));
        iv_community.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.message));
        iv_store.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.store));
        iv_found.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.found));
        iv_personal.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.personal));
    }

    /**
     * 监听手机按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断当前页面是否是小卖部
            if (fragmentIndex == 3) {
                // 判断购物车界面是否显示状态，是则按返回键时设置购物车界面隐藏而不是退出应用
                if (StoreFragment.rl_cart_page.getVisibility() == View.VISIBLE) {
                    StoreFragment.rl_cart_page.setVisibility(View.GONE);
                    return true;
                }
                // 判断商家界面是否显示状态，是则按返回键时设置商家界面隐藏而不是退出应用
                if (StoreFragment.ll_activity_business.getVisibility() == View.VISIBLE) {
                    StoreFragment.ll_activity_business.setVisibility(View.GONE);
                    iv_below.setVisibility(View.VISIBLE);
                    root_below.setVisibility(View.VISIBLE);
                    return true;
                }
                // 判断商品详情界面是否显示状态，是则按返回键时设置商品详情界面隐藏而不是退出应用
                if (StoreFragment.rl_activity_goods.getVisibility() == View.VISIBLE) {
                    StoreFragment.rl_activity_goods.setVisibility(View.GONE);
                    iv_below.setVisibility(View.VISIBLE);
                    root_below.setVisibility(View.VISIBLE);
                    return true;
                }
                // 判断2秒内是否按了两次返回键，如果是则退出应用，否则提示用户再按一次退出程序
                else if (isFirstExit) {
//                    ToastUtil.showToast(HomeActivity.this, "再按一次退出程序");
//                    isFirstExit = false;
//                    new Thread() {
//                        public void run() {
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            isFirstExit = true;
//                        }
//
//                        ;
//                    }.start();
//                    return true;
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        moveTaskToBack(false);
                        return true;
                    }
                }
            }

            // 判断2秒内是否按了两次返回键，如果是则退出应用，否则提示用户再按一次退出程序
            else if (isFirstExit) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    moveTaskToBack(false);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // 扫描加入社团
            if (requestCode == MyConfig.QRCODE) {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                boolean contains = result.contains(",");
                if (result.startsWith("myQRCode")) {
                    String[] split = result.split(">");
                    addToFriend(split);
                } else if (contains) {
                    if (result.split(",")[0].equals(sid)) {
                        joinCommunity(result.split(",")[1]);
                    } else {
                        ToastUtil.showToast(HomeActivity.this, "不是本校成员不能加入");
                    }

                } else {
                    ToastUtil.showToast(HomeActivity.this, "社团不存在");

                }
            }
            // 选择社团学校
            else if (requestCode == MyConfig.SCHOOL_CODE_COMMUNITY) {
                // CommunityFragment.btn_campusName.setText(data.getExtras()
                // .getString(MyConfig.SCHOOL_SEARCH));
                String school = data.getExtras().getString(
                        MyConfig.SCHOOL_SEARCH);
                String schoolID = data.getExtras().getString(
                        MyConfig.SCHOOL_SEARCH_ID);
                // 保存社团学校
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_COMM, school);
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_COMM,
                        schoolID);
            }
            // 选择小卖部学校
            else if (requestCode == MyConfig.SCHOOL_CODE_STORE) {
                // StoreFragment.btn_campusName.setText(data.getExtras()
                // .getString(MyConfig.SCHOOL_SEARCH));
                String school = data.getExtras().getString(
                        MyConfig.SCHOOL_SEARCH);
                String schoolID = data.getExtras().getString(
                        MyConfig.SCHOOL_SEARCH_ID);
                // 保存小卖部学校
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_STORE, school);
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_STORE,
                        schoolID);
//                StoreFragment.isChange = true;
                EventBus.getDefault().post(new MEventStoreCart(true));
            }
            // 选择所有学校
            else if (requestCode == MyConfig.SCHOOL_CODE_HOME) {
                String school = data.getExtras().getString(
                        MyConfig.SCHOOL_SEARCH);
                String schoolID = data.getExtras().getString(
                        MyConfig.SCHOOL_SEARCH_ID);
                HomeFragment.tv_location01.setText(school);
                HomeFragment.tv_location02.setText(school);
                // 保存照片墙学校
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_PICTURE, school);
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_PICTURE,
                        schoolID);
                // 保存社团学校
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_COMM, school);
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_COMM,
                        schoolID);
                // 保存小卖部学校
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_STORE, school);
                UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_STORE,
                        schoolID);
//                StoreFragment.isChange = true;
                EventBus.getDefault().post(new MEventStoreCart(true));
            }
        }
    }

    private void addToFriend(final String[] result) {

        dialog = new Dialog(this, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.add_friend_layout, null);
        dialog.setContentView(viewDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(viewDialog, params);
        CircleView iv_avatar = (CircleView) viewDialog.findViewById(R.id.iv_avatar);
        Glide.with(this).load(result[2]).error(R.drawable.default_useravatar).into(iv_avatar);
        TextView tv_name = (TextView) viewDialog.findViewById(R.id.tv_name);
        tv_name.setText(result[3]);
        final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
        TextView btn_ok = (TextView) viewDialog.findViewById(R.id.btn_ok);
        TextView btn_cancel = (TextView) viewDialog.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_name = ed_description.getText().toString().trim();
                dialog.dismiss();
                sendRequest(ed_name, result[1]);

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialog) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void sendRequest(final String msg, final String result) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.ADD_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToastUtil.showToast(HomeActivity.this, "请求发送成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                ToastUtil.showToast(HomeActivity.this, "请求失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("to_uid", result);
                map.put("msg", msg);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
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
                MyUtils.installAPK(HomeActivity.this, target);
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
                ToastUtil.showToast(HomeActivity.this, "下载失败");
            }
        });
    }

    /**
     * 获取校园圈没读的条数
     */
    private int getNoReadCount(String urlString) {
        InputStream is = null;
        int count = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            count = jsonObject.optInt("nums", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    private String readStream(InputStream is) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void joinCommunity(String comid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userid", uid);
        params.addBodyParameter("comnid", comid);
        params.addBodyParameter("sid", sid);
        httpUtils.send(HttpRequest.HttpMethod.POST,
                MyConfig.URL_POST_COMMUNITY_JOIN, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(HomeActivity.this, "提交成功,请耐心等待审核");
                        } else if (result.equals("-1")) {
                            CustomDialog.Builder builder = new CustomDialog.Builder(
                                    HomeActivity.this);
                            builder.setMessage("请先完善个人资料");
                            builder.setPositiveButton("是",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            Intent intent = new Intent(
                                                    HomeActivity.this,
                                                    BasicInfoActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            builder.setNegativeButton("否", null);
                            builder.create().show();
                        } else {
                            ToastUtil.showToast(HomeActivity.this, "提交失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(HomeActivity.this, "提交失败");
                    }
                });

    }

    @Override
    protected void onResume() {
        if (!loading) {
            loading = true;
            new Thread() {
                public void run() {
                    noReadCount = getNoReadCount(MyConfig.URL_JSON_CAMPUS_NOREAD_COUNT
                            + uid);
                    noReadCountMes = GetData.getNoReadCount(MyConfig.URL_JSON_MESSAGE_NOREAD
                            + uid);
                    if (!isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
        super.onResume();
    }

    public void onEventMainThread(MEventHome event) {
        if (event.isFinish()) {
            this.finish();
        }
    }


    /**
     * 红点是否显示
     *
     * @param event
     */
    public void onEventMainThread(MEventCampusNoRead event) {
        if (event.isChange()) {
            if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                    .getString(UserInfoDB.NOREAD_MESSAGE_COUNT,
                            "0").equals("0")) {
                iv_circle.setVisibility(View.VISIBLE);
            } else {
                iv_circle.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 红点是否显示
     *
     * @param event
     */
    public void onEventMainThread(MEventCampusNoReadFriend event) {
        if (event.isChange()) {
            if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                    .getString(UserInfoDB.NOREAD_MESSAGE_COUNT,
                            "0").equals("0")) {
                iv_circle.setVisibility(View.VISIBLE);
            } else {
                iv_circle.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 红点是否显示
     */
    public void onEventMainThread(MEventMessageNoRead event) {
        if (event.isChange()) {
            if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                    .getString(UserInfoDB.NOREAD_MESSAGE_COUNT,
                            "0").equals("0")) {
                iv_circle.setVisibility(View.VISIBLE);
            } else {
                iv_circle.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        //退出融云账号
//        RongIM.getInstance().disconnect();
        root_below = null;
        iv_below = null;

        super.onDestroy();
    }

    @Override
    public void onDragOut() {
        unread_num.setVisibility(View.GONE);
        ToastUtil.showToast(HomeActivity.this, "清除成功");
        List<Conversation> conversations = RongIM.getInstance().getConversationList();
        if (conversations != null && conversations.size() > 0) {
            for (Conversation c : conversations) {
                RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
            }
        }
    }
}
