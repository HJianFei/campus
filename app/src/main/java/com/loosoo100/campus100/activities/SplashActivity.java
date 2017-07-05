package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.chat.bean.Company_Info;
import com.loosoo100.campus100.chat.bean.User_Info;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyLocationUtil;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;
import com.loosoo100.campus100.zzboss.activities.BossRegister02Activity;
import com.loosoo100.campus100.zzboss.beans.BossCompanyInfo;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * @author yang 欢迎页面activity
 */
public class SplashActivity extends Activity {

    private Intent intent = new Intent();
    private LinearLayout root;
    private String uid = "";
    private String cuid = ""; // 公司ID
    private String sid = "";
    private String sid2 = "";
    private UserInfo userInfo = null;
    private UserInfo userInfo2 = null;
    private BossCompanyInfo companyInfo = null;
    private String company = "";
    private String token = "";

    private int second = 0; // 5秒后直接跳转

    private ImageView image;
    private MyLocationUtil myLocationUtil;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (userInfo != null) {
                String school = userInfo.getSchool();
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.NAME, userInfo.getName());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SEX, userInfo.getSex());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.WEIXIN, userInfo.getWeixin());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.STUDENT_ID, userInfo.getStuNo());
                // 基本信息的学校
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL, school);
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_ID, userInfo.getSchoolID());
                // 保存照片墙学校
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_PICTURE, school);
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_ID_PICTURE, userInfo.getSchoolID());
                // 保存社团学校
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_COMM, school);
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_ID_COMM, userInfo.getSchoolID());
                // 保存小卖部学校
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_STORE, school);
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SCHOOL_ID_STORE, userInfo.getSchoolID());
            }
            if (userInfo2 != null) {
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.MONEY, userInfo2.getMoney() + "");
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.POINT, userInfo2.getPoint() + "");
            }

            if (userInfo != null) {
                sid2 = userInfo.getSchoolID();
            }
            if ((sid2.equals("") && sid.equals("")) || (sid2.equals("null") && sid.equals(""))
                    || (sid2.equals("") && sid.equals("null")) || (sid2.equals("null") && sid.equals("null"))) {
                intent.setClass(SplashActivity.this, Register02Activity.class);
            } else {
                intent.setClass(SplashActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            SplashActivity.this.finish();
        }
    };
    private Handler handlerBoss = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String company2 = "";
            if (companyInfo != null) {
                company2 = companyInfo.getCompany();
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.NICK_NAME, companyInfo.getName());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.COMPANY, companyInfo.getCompany());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.SEX, companyInfo.getSex());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.WEIXIN, companyInfo.getWeixin());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.NICK_NAME, companyInfo.getNickName());
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.MONEY, companyInfo.getMoney() + "");
                UserInfoDB.setUserInfo(SplashActivity.this, UserInfoDB.POINT, companyInfo.getPoint() + "");
            }

            if (company.equals("") && company2.equals("")) {
                intent.setClass(SplashActivity.this, BossRegister02Activity.class);
            } else {
                intent.setClass(SplashActivity.this, BossHomeActivity.class);
            }
            startActivity(intent);
            SplashActivity.this.finish();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        myLocationUtil = new MyLocationUtil(this);

        root = (LinearLayout) findViewById(R.id.root);
        root.startAnimation(MyAnimation.getAlphaAnimation(500));

        image = (ImageView) findViewById(R.id.image);
        image.setImageBitmap(GetData.getBitMap(this, R.drawable.action));

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");
        company = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.COMPANY, "");
        token = (String) SharedPreferencesUtils.getParam(SplashActivity.this, "token", "");

        //判断融云是否登录
        if (RongIM.getInstance() != null && RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {

            //登录融云账号
            if (!uid.equals("") && !token.equals("") && cuid.equals("")) {
                LogUtils.d("onResponse", "学dd生");
                //连接融云服务器(学生)
                connect(uid);
            } else if (!cuid.equals("") && !token.equals("") && uid.equals("")) {
                //连接融云服务器(企业)
                LogUtils.d("onResponse", "企dd业");
                connect(cuid);
            }
        }
        if (uid.equals("") && cuid.equals("")) {
            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    intent.setClass(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }.start();

        } else {
            if (!uid.equals("")) {
                // 极光推送设置别名为用户ID
                JPushInterface.setAlias(this, uid, null);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            long time01 = System.currentTimeMillis();
                            userInfo = GetData.getUserBasicInfo(MyConfig.URL_JSON_USERINFO_BASIC + uid);
                            userInfo2 = GetData.getUserInfo(MyConfig.URL_JSON_USERINFO + uid);
                            long time02 = System.currentTimeMillis();
                            if (time02 - time01 < 1000) {
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isDestroyed() && second < 4) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }.start();
                new Thread() {
                    public void run() {
                        for (int i = 0; i < 5; i++) {
                            SystemClock.sleep(1000);
                            second++;
                        }
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
            } else {
                // 极光推送设置别名为用户ID
                JPushInterface.setAlias(this, cuid, null);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            long time01 = System.currentTimeMillis();
                            companyInfo = GetData.getBossUserInfo(MyConfig.URL_JSON_COMPANYINFO + cuid);
                            long time02 = System.currentTimeMillis();
                            if (time02 - time01 < 1000) {
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!isDestroyed() && second < 4) {
                            handlerBoss.sendEmptyMessage(0);
                        }
                    }
                }.start();
                new Thread() {
                    public void run() {
                        for (int i = 0; i < 5; i++) {
                            SystemClock.sleep(1000);
                            second++;
                        }
                        if (!isDestroyed()) {
                            handlerBoss.sendEmptyMessage(0);
                        }
                    }
                }.start();
            }

        }

    }

    /**
     * 建立与融云服务器的连接
     */

    private void connect(final String user_id) {
        /**
         * IMKit SDK调用第二步,建立与服务器的连接
         */
        String mToken = (String) SharedPreferencesUtils.getParam(SplashActivity.this, "token", "");
        RongIM.connect(mToken, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {
                LogUtils.d("TokenIncorrect", "融云token过期");
                refreshToken(user_id);

            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override
            public void onSuccess(String userid) {

                Log.d("LoginActivity", "--onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("LoginActivity", "--onError" + errorCode);
                refreshToken(user_id);
            }
        });
    }

    private void refreshToken(String user_id) {
        //获取学生信息
        if (cuid.equals("") && !uid.equals("")) {
            LogUtils.d("onResponse", "学生");
            GsonRequest<User_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_TOKEN_FROM_RONGYUN + "?userId=" + user_id, User_Info.class, new Response.Listener<User_Info>() {
                @Override
                public void onResponse(User_Info user_info) {
                    LogUtils.d("onResponse", user_info.toString());
                    SharedPreferencesUtils.setParam(SplashActivity.this, "token", user_info.getRy_token() + "");
                    connect(uid);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MyApplication.getRequestQueue().add(gsonRequest);
            //获取企业信息
        } else if (!cuid.equals("") && uid.equals("")) {
            LogUtils.d("onResponse", "企业");
            GsonRequest<Company_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_TOKEN_FROM_RONGYUN + "?companyId=" + user_id, Company_Info.class, new Response.Listener<Company_Info>() {
                @Override
                public void onResponse(Company_Info company_info) {
                    LogUtils.d("onResponse", company_info.toString());
                    SharedPreferencesUtils.setParam(SplashActivity.this, "token", company_info.getRy_token() + "");
                    connect(cuid);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MyApplication.getRequestQueue().add(gsonRequest);
        }

    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myLocationUtil.stop();
        super.onDestroy();
    }
}
