package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventGuide;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;
import com.loosoo100.campus100.zzboss.activities.BossRegister02Activity;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 登录activity
 */
public class LoginActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_login_bg)
    private ImageView iv_login_bg;
    @ViewInject(R.id.et_account)
    private EditText et_account;
    @ViewInject(R.id.et_password)
    private EditText et_password;
    @ViewInject(R.id.btn_login)
    private Button btn_login;
    @ViewInject(R.id.btn_register)
    private Button btn_register;
    @ViewInject(R.id.btn_forget)
    private Button btn_forget; // 忘记密码
    @ViewInject(R.id.ll_argeement)
    private LinearLayout ll_argeement; // 条款
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 加载动画

    @ViewInject(R.id.ll_student01)
    private LinearLayout ll_student01;
    @ViewInject(R.id.ll_student02)
    private LinearLayout ll_student02;
    @ViewInject(R.id.ll_bussiness01)
    private LinearLayout ll_bussiness01;
    @ViewInject(R.id.ll_bussiness02)
    private LinearLayout ll_bussiness02;
    @ViewInject(R.id.rl_dialog)
    private RelativeLayout rl_dialog;
    @ViewInject(R.id.btn_student)
    private Button btn_student;
    @ViewInject(R.id.btn_boss)
    private Button btn_boss;
    @ViewInject(R.id.rl_dialog02)
    private RelativeLayout rl_dialog02;
    @ViewInject(R.id.btn_student02)
    private Button btn_student02;
    @ViewInject(R.id.btn_boss02)
    private Button btn_boss02;

    private String account = "";
    private String password = "";
    private String status = "";
    private String uid = "";
    private String sid = "";
    private String sname = "";

    // 企业版
    private String cuid = "";
    private String company = "";
    private String nickname = "";
    private String money = "";
    private String point = "";

    private LoginActivity activity;
    private UserInfo userInfo = null;
    private UserInfo userInfo2 = null;

    private boolean isFirstExit = true;

    private boolean boss = false; // 选择学生版或者企业版

    private Handler handlerReg = new Handler() {

        public void handleMessage(android.os.Message msg) {
            // 保存用户ID
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.USERID, uid);
            // 保存用户手机号
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.PHONE, account);
            // 基本信息的学校
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.SCHOOL, sname);
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.SCHOOL_ID, sid);
            // 保存照片墙学校
            UserInfoDB.setUserInfo(activity, UserInfoDB.SCHOOL_PICTURE, sname);
            UserInfoDB.setUserInfo(activity, UserInfoDB.SCHOOL_ID_PICTURE, sid);
            // 保存社团学校
            UserInfoDB.setUserInfo(activity, UserInfoDB.SCHOOL_COMM, sname);
            UserInfoDB.setUserInfo(activity, UserInfoDB.SCHOOL_ID_COMM, sid);
            // 保存小卖部学校
            UserInfoDB.setUserInfo(activity, UserInfoDB.SCHOOL_STORE, sname);
            UserInfoDB.setUserInfo(activity, UserInfoDB.SCHOOL_ID_STORE, sid);
            if (userInfo != null) {
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.NAME, userInfo.getName());
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.SEX, userInfo.getSex());
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.STUDENT_ID, userInfo.getStuNo());
            }
            if (userInfo2 != null) {
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.MONEY, userInfo2.getMoney() + "");
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.POINT, userInfo2.getPoint() + "");
            }
            progress_update.setVisibility(View.GONE);
            if (sid.equals("") || sid.equals("null")) {
                Intent intent = new Intent(LoginActivity.this, Register02Activity.class);
                startActivity(intent);
                finish();
            } else {
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.ORG, "0");
                ToastUtil.showToast(LoginActivity.this, "登录成功");
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        ;
    };

    private Handler handlerRegBoss = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 保存公司ID
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.CUSERID, cuid);
            // 保存公司名
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.COMPANY, company);
            // 保存用户手机号
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.PHONE, account);
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.NICK_NAME, nickname);
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.MONEY, money);
            UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.POINT, point);
            if (company.equals("") || company.equals("null")) {
                Intent intent = new Intent(LoginActivity.this, BossRegister02Activity.class);
                startActivity(intent);
                finish();
            } else {
                UserInfoDB.setUserInfo(LoginActivity.this, UserInfoDB.ORG, "1");
                ToastUtil.showToast(LoginActivity.this, "登录成功");
                Intent intent = new Intent(LoginActivity.this, BossHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EventBus.getDefault().register(this);
        activity = LoginActivity.this;
        iv_login_bg.setImageBitmap(GetData.getBitMap(this, R.drawable.login_bg));
        initView();

    }

    private void initView() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        ll_argeement.setOnClickListener(this);
        ll_student01.setOnClickListener(this);
        ll_student02.setOnClickListener(this);
        ll_bussiness01.setOnClickListener(this);
        ll_bussiness02.setOnClickListener(this);
        btn_student.setOnClickListener(this);
        btn_boss.setOnClickListener(this);
        btn_student02.setOnClickListener(this);
        btn_boss02.setOnClickListener(this);

        //TODO
//		et_account.setText("11511500002");
//		et_password.setText("5845202");
//		et_account.setText("15913135783");
//		et_password.setText("123456");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_student01:
                //TODO
//			et_account.setText("11511500002");
//			et_password.setText("584520");
//			et_account.setText("15913135783");
//			et_password.setText("123456");
                ll_student02.setVisibility(View.GONE);
                ll_student01.setVisibility(View.VISIBLE);
                ll_bussiness02.setVisibility(View.GONE);
                ll_bussiness01.setVisibility(View.VISIBLE);
                boss = false;
                break;

            case R.id.ll_student02:
                //TODO
//			et_account.setText("11511500002");
//			et_password.setText("584520");
//			et_account.setText("15913135783");
//			et_password.setText("123456");
                ll_student02.setVisibility(View.GONE);
                ll_student01.setVisibility(View.VISIBLE);
                ll_bussiness02.setVisibility(View.GONE);
                ll_bussiness01.setVisibility(View.VISIBLE);
                boss = false;
                break;

            case R.id.ll_bussiness01:
                //TODO
//			et_account.setText("15913135783");
//			et_password.setText("123456");
                ll_bussiness01.setVisibility(View.GONE);
                ll_bussiness02.setVisibility(View.VISIBLE);
                ll_student01.setVisibility(View.GONE);
                ll_student02.setVisibility(View.VISIBLE);
                boss = true;
                break;

            case R.id.ll_bussiness02:
                //TODO
//			et_account.setText("15913135783");
//			et_password.setText("123456");
                ll_bussiness01.setVisibility(View.GONE);
                ll_bussiness02.setVisibility(View.VISIBLE);
                ll_student01.setVisibility(View.GONE);
                ll_student02.setVisibility(View.VISIBLE);
                boss = true;
                break;

            // 登录
            case R.id.btn_login:
                account = et_account.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (account.equals("") || password.equals("")) {
                    ToastUtil.showToast(LoginActivity.this, "请将信息填写完整");
                    return;
                }
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        if (boss) {
                            postBossDataXutils(MyConfig.URL_POST_LOGIN_BOSS);
                        } else {
                            postDataXutils(MyConfig.URL_POST_LOGIN);
                        }
                    }

                    ;
                }.start();
                break;

            // 注册
            case R.id.btn_register:
                rl_dialog02.setVisibility(View.GONE);
                if (rl_dialog.getVisibility() == View.VISIBLE) {
                    rl_dialog.setVisibility(View.GONE);
                    rl_dialog.startAnimation(MyAnimation.getScaleAnimationToLeftBottom2());
                } else {
                    rl_dialog.setVisibility(View.VISIBLE);
                    rl_dialog.startAnimation(MyAnimation.getScaleAnimationToRightTop2());
                }
                break;

            case R.id.btn_student:
                Intent intentStu = new Intent(this, Register01Activity.class);
                intentStu.putExtra("org", "0");
                startActivity(intentStu);
                rl_dialog.setVisibility(View.GONE);
                rl_dialog02.setVisibility(View.GONE);
                break;

            case R.id.btn_boss:
                Intent intentBoss = new Intent(this, Register01Activity.class);
                intentBoss.putExtra("org", "1");
                startActivity(intentBoss);
                rl_dialog.setVisibility(View.GONE);
                rl_dialog02.setVisibility(View.GONE);
                break;

            // 忘记密码
            case R.id.btn_forget:
                rl_dialog.setVisibility(View.GONE);
                if (rl_dialog02.getVisibility() == View.VISIBLE) {
                    rl_dialog02.setVisibility(View.GONE);
                    rl_dialog02.startAnimation(MyAnimation.getScaleAnimationToRightBottom());
                } else {
                    rl_dialog02.setVisibility(View.VISIBLE);
                    rl_dialog02.startAnimation(MyAnimation.getScaleAnimationToLeftTop());
                }
                break;


            case R.id.btn_student02:
                Intent intentStu02 = new Intent(this, ForgetPWActivity.class);
                intentStu02.putExtra("boss", false);
                startActivity(intentStu02);
                rl_dialog02.setVisibility(View.GONE);
                rl_dialog.setVisibility(View.GONE);
                break;

            case R.id.btn_boss02:
                Intent intentBoss02 = new Intent(this, ForgetPWActivity.class);
                intentBoss02.putExtra("boss", true);
                startActivity(intentBoss02);
                rl_dialog02.setVisibility(View.GONE);
                rl_dialog.setVisibility(View.GONE);
                break;

            case R.id.ll_argeement:
                Intent intent3 = new Intent(this, ServiceActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private void postDataXutils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userPhone", account);
        params.addBodyParameter("loginPwd", password);
        params.addBodyParameter("imei", MyUtils.getIMEI(this));
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.d("responseInfo", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    status = jsonObject.optString("status", "");
                    uid = jsonObject.optString("userId", "");
                    sid = jsonObject.optString("school_shopid", "");
                    sname = jsonObject.optString("school_name", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress_update.setVisibility(View.GONE);
                if (status.equals("-1")) {
                    ToastUtil.showToast(LoginActivity.this, "账号不存在");
                } else if (status.equals("0")) {
                    ToastUtil.showToast(LoginActivity.this, "账号或密码错误");
                } else {
                    progress_update.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {
                            userInfo = GetData.getUserBasicInfo(MyConfig.URL_JSON_USERINFO_BASIC + uid);
                            userInfo2 = GetData.getUserInfo(MyConfig.URL_JSON_USERINFO + uid);
                            if (!isDestroyed()) {
                                handlerReg.sendEmptyMessage(0);
                            }
                        }

                        ;
                    }.start();
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.d("error", arg1.toString());
                ToastUtil.showToast(LoginActivity.this, "登录失败");
                progress_update.setVisibility(View.GONE);
            }
        });
    }

    private void postBossDataXutils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("companyPhone", account);
        params.addBodyParameter("loginPwd", password);
        params.addBodyParameter("imei", MyUtils.getIMEI(this));
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.d("responseInfo", responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    status = jsonObject.optString("status", "");
                    cuid = jsonObject.optString("company_id", "");
                    company = jsonObject.optString("company_name", "");
                    nickname = jsonObject.optString("company_nickname", "");
                    money = jsonObject.optString("company_money", "");
                    point = jsonObject.optString("company_score", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress_update.setVisibility(View.GONE);
                if (status.equals("-1")) {
                    ToastUtil.showToast(LoginActivity.this, "账号不存在");
                } else if (status.equals("0")) {
                    ToastUtil.showToast(LoginActivity.this, "账号或密码错误");
                } else {
                    if (!isDestroyed()) {
                        handlerRegBoss.sendEmptyMessage(0);
                    }
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.d("error", arg1.toString());
                ToastUtil.showToast(LoginActivity.this, "登录失败");
                progress_update.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 监听手机按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_dialog.getVisibility() == View.VISIBLE) {
                rl_dialog.setVisibility(View.GONE);
                rl_dialog.startAnimation(MyAnimation.getScaleAnimationToLeftBottom2());
                return true;
            }
            if (rl_dialog02.getVisibility() == View.VISIBLE) {
                rl_dialog02.setVisibility(View.GONE);
                rl_dialog02.startAnimation(MyAnimation.getScaleAnimationToRightBottom());
                return true;
            }
            // 判断2秒内是否按了两次返回键，如果是则退出应用，否则提示用户再按一次退出程序
            if (isFirstExit) {
                ToastUtil.showToast(LoginActivity.this, "再按一次退出程序");
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

    public void onEventMainThread(MEventGuide event) {
        if (event.isFinish()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
