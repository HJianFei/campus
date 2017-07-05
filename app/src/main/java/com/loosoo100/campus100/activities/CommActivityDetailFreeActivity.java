package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.loosoo100.campus100.anyevent.MEventCollectActiChange;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.anyevent.MEventSecondPage;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.imitateTB.MyScrollView;
import com.loosoo100.campus100.zzboss.activities.CommSupportDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

import static com.loosoo100.campus100.utils.GetData.getBossCommActiInfo;

/**
 * @author yang 社团活动详情-免费
 */
public class CommActivityDetailFreeActivity extends Activity implements
        OnClickListener, OnTouchListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.iv_empty2)
    private ImageView iv_empty2; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;

    @ViewInject(R.id.iv_picture)
    private ImageView iv_picture; // 顶部图片
    @ViewInject(R.id.tv_activityName)
    private TextView tv_activityName; // 活动名称
    @ViewInject(R.id.tv_readCount)
    private TextView tv_readCount; // 浏览次数
    @ViewInject(R.id.tv_collectCount)
    private TextView tv_collectCount; // 收藏人数
    @ViewInject(R.id.tv_remainDay)
    private TextView tv_remainDay; // 剩余天数
    @ViewInject(R.id.tv_time)
    private TextView tv_time; // 起始时间
    @ViewInject(R.id.tv_address)
    private TextView tv_address; // 举办地址
    @ViewInject(R.id.tv_money)
    private TextView tv_money; // 收费金额
    @ViewInject(R.id.tv_personCount)
    private TextView tv_personCount; // 剩余多少人
    @ViewInject(R.id.ll_comm)
    private LinearLayout ll_comm; // 社团布局
    @ViewInject(R.id.tv_communityName)
    private TextView tv_communityName; // 社团名称
    @ViewInject(R.id.tv_id)
    private TextView tv_id; // 社团号
    @ViewInject(R.id.tv_city_school)
    private TextView tv_city_school; // 所属城市和学校
    @ViewInject(R.id.cv_headShot)
    private CircleView cv_headShot; // 社团头像
    @ViewInject(R.id.ll_payDetail)
    private LinearLayout ll_payDetail; // 打赏情况
    @ViewInject(R.id.ll_collect)
    private LinearLayout ll_collect; // 收藏布局
    @ViewInject(R.id.iv_collect)
    private ImageView iv_collect; // 收藏按钮
    @ViewInject(R.id.tv_over)
    private TextView tv_over; // 已结束
    @ViewInject(R.id.btn_join)
    private Button btn_join; // 立即参与按钮
    @ViewInject(R.id.btn_pay)
    private Button btn_pay; // 打赏按钮
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update_blackbg)
    private RelativeLayout progress_update_blackbg; // 加载动画
    @ViewInject(R.id.ll_image)
    private LinearLayout ll_image; // 图片详情
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more; // 更多
    @ViewInject(R.id.rl_popupwindow)
    private RelativeLayout rl_popupwindow; // 弹出菜单项
    @ViewInject(R.id.btn_report)
    private Button btn_report; // 举报活动
    @ViewInject(R.id.dialog_report)
    private RelativeLayout dialog_report; // 举报界面
    @ViewInject(R.id.btn01)
    private Button btn01;
    @ViewInject(R.id.btn02)
    private Button btn02;
    @ViewInject(R.id.btn03)
    private Button btn03;
    @ViewInject(R.id.btn04)
    private Button btn04;
    @ViewInject(R.id.btn05)
    private Button btn05;
    @ViewInject(R.id.webView)
    private WebView webView;
    @ViewInject(R.id.myScrollView01)
    private MyScrollView myScrollView01;
    @ViewInject(R.id.myScrollView02)
    private MyScrollView myScrollView02;

    private String aid = ""; // 传进来的活动ID

    private CommunityActivityInfo communityActivityInfo;

    private String content = "";
    private Dialog dialog;
    private EditText editText;
    private ScaleAnimation animation; // 收藏按钮点击动画

    private boolean isFirstShow = true;

    private String uid = "";
    private String msid = ""; // 活动所在学校ID
    private String sid = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            progress.setVisibility(View.GONE);
            progress_update_blackbg.setVisibility(View.GONE);
        }

        ;
    };

    private Handler handlerPay = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Intent intentPay = new Intent(CommActivityDetailFreeActivity.this,
                    PayActiActivity.class);
            intentPay.putExtra("money",
                    Float.valueOf(communityActivityInfo.getFree()));
            intentPay.putExtra("aid", aid);
            intentPay.putExtra("cid", communityActivityInfo.getCommId());
            intentPay.putExtra("tip", "0");
            startActivityForResult(intentPay, 1);
        }

        ;
    };

    private Handler handlerPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (Float.valueOf(communityActivityInfo.getFree()) == 0) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(
                        CommActivityDetailFreeActivity.this);
                builderDel.setMessage("是否确认参与");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                progress_update_blackbg
                                        .setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postDataFree(MyConfig.URL_POST_COMM_ACTI_FREE_JOIN);
                                    }

                                    ;
                                }.start();
                            }
                        });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
            } else {
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        isHavePayPW(MyConfig.URL_POST_ISSETTING_PAYPW);
                    }

                    ;
                }.start();
            }
        }

        ;
    };
    private Handler handlerNoPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CustomDialog.Builder builder = new CustomDialog.Builder(
                    CommActivityDetailFreeActivity.this);
            builder.setMessage("请先完善个人资料");
            builder.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    CommActivityDetailFreeActivity.this,
                                    BasicInfoActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("否", null);
            builder.create().show();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_activity_detail_free);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setStatusBarHeight(this, iv_empty2);
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        EventBus.getDefault().register(this);

        aid = getIntent().getExtras().getString("aid");

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");

        progress.setVisibility(View.VISIBLE);
        animation = MyAnimation.getScaleAnimationLocation();
        rl_back.setOnClickListener(this);

        new Thread() {
            public void run() {
                communityActivityInfo =
                        getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                + aid + "&uid=" + uid);
                if (communityActivityInfo != null && !isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initView() {
        ll_payDetail.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);
        btn05.setOnClickListener(this);
        ll_comm.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_join.setOnClickListener(this);

        myScrollView01.setOnTouchListener(this);
        myScrollView02.setOnTouchListener(this);

        msid = communityActivityInfo.getSchoolID();

        if (communityActivityInfo.getStatus() == 1) {
            tv_over.setVisibility(View.VISIBLE);
            btn_join.setVisibility(View.GONE);
            btn_pay.setVisibility(View.GONE);
        } else {
            tv_over.setVisibility(View.GONE);
            btn_join.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.VISIBLE);
        }

        if (communityActivityInfo.getCanyu() == 1) {
            btn_join.setText("已参与");
            btn_join.setClickable(false);
        } else {
            btn_join.setText("立即参与");
            btn_join.setClickable(true);
        }

        if (communityActivityInfo.getType().equals("0")) {
            if (!communityActivityInfo.getSchoolID().equals(sid)) {
                btn_join.setClickable(false);
                btn_join.setText("限本校参与");
                btn_join.setBackgroundColor(getResources().getColor(
                        R.color.gray_a1a1a1));
            }
        }

        if (communityActivityInfo.getCommUid().equals(uid)) {
            btn_report.setText("报名人员");
        }

        if (communityActivityInfo.getCollect() == 0) {
            iv_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect));
        } else {
            iv_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_red));
        }

        if (Float.valueOf(communityActivityInfo.getFree()) == 0) {
            tv_money.setText("免费");
            tv_personCount
                    .setText((int) (communityActivityInfo.getNeedMoney() - communityActivityInfo
                            .getRaisedMoney()) + "人");
        } else {
            tv_money.setText("￥" + communityActivityInfo.getFree());
            tv_personCount
                    .setText((int) (communityActivityInfo.getNeedMoney() - communityActivityInfo
                            .getRaisedMoney()) + "票");
        }

        tv_activityName.setText(communityActivityInfo.getActivityName());
        tv_readCount.setText("" + communityActivityInfo.getReadCount());
        tv_collectCount.setText("" + communityActivityInfo.getCollectCount());
        tv_remainDay.setText(communityActivityInfo.getRemainDay() + "天");
        tv_time.setText(MyUtils.getSqlDateYMD(communityActivityInfo
                .getStartTime())
                + " — "
                + MyUtils.getSqlDateYMD(communityActivityInfo.getEndTime()));
        tv_address.setText(communityActivityInfo.getAddress());
        tv_id.setText(communityActivityInfo.getCommId() + "");
        tv_city_school.setText(communityActivityInfo.getCity() + "-"
                + communityActivityInfo.getSchool());
        tv_communityName.setText(communityActivityInfo.getCommunityName());

        if (!communityActivityInfo.getActi_letter().equals("")) {
            webView.loadDataWithBaseURL(null,
                    communityActivityInfo.getActi_letter(), "text/html",
                    "utf-8", null);
            webView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.GONE);
        }

        if (!communityActivityInfo.getActiHeadShot().equals("")
                && !communityActivityInfo.getActiHeadShot().equals("null")) {
            Glide.with(this).load(communityActivityInfo.getActiHeadShot())
                    .into(iv_picture);
        }
        if (!communityActivityInfo.getCommHeadShot().equals("")
                && !communityActivityInfo.getCommHeadShot().equals("null")) {
            Glide.with(this).load(communityActivityInfo.getCommHeadShot())
                    .into(cv_headShot);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.ll_payDetail:
                Intent intent2 = new Intent(this, CommSupportDetailActivity.class);
                intent2.putExtra("aid", aid);
                startActivity(intent2);
                break;

            case R.id.btn_join:
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postIsDataPerfect(MyConfig.URL_POST_IS_INFO_PERFECT, 1);
                    }

                    ;
                }.start();
                break;

            case R.id.btn_pay:
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postIsDataPerfect(MyConfig.URL_POST_IS_INFO_PERFECT, 2);
                    }

                    ;
                }.start();
                break;

            // 收藏按钮
            case R.id.ll_collect:
                ll_collect.setClickable(false);
                iv_collect.startAnimation(animation);
                if (communityActivityInfo.getCollect() == 0) {
                    new Thread() {
                        public void run() {
                            postCollect(MyConfig.URL_POST_ACTI_COLLECT_BOSS);
                        }

                        ;
                    }.start();
                } else {
                    new Thread() {
                        public void run() {
                            postCollectCancel(MyConfig.URL_POST_ACTI_COLLECT_CANCEL_BOSS);
                        }

                        ;
                    }.start();
                }
                break;

            // 社团
            case R.id.ll_comm:
                Intent intentComm = new Intent(this, CommDetailActivity.class);
                intentComm.putExtra("id", communityActivityInfo.getCommId());
                startActivity(intentComm);
                break;

            // 右上角更多
            case R.id.rl_more:
                if (rl_popupwindow.getVisibility() == View.GONE) {
                    rl_popupwindow.setVisibility(View.VISIBLE);
                } else {
                    rl_popupwindow.setVisibility(View.GONE);
                }
                break;

            // 右上角举报活动
            case R.id.btn_report:
                rl_popupwindow.setVisibility(View.GONE);
                if (communityActivityInfo != null
                        && communityActivityInfo.getCommUid().equals(uid)) {
                    Intent intent3 = new Intent(this, CommActiMemJoinActivity.class);
                    intent3.putExtra("aid", aid);
                    startActivity(intent3);
                } else {
                    dialog_report.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn01:
                dialog_report.setVisibility(View.GONE);
                content = "虚假活动";
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn02:
                dialog_report.setVisibility(View.GONE);
                content = "淫秽色情";
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn03:
                dialog_report.setVisibility(View.GONE);
                content = "广告骚扰";
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn04:
                dialog_report.setVisibility(View.GONE);
                content = "恶意言论";
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn05:
                dialog_report.setVisibility(View.GONE);
                dialog = new Dialog(this, R.style.MyDialog);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.dialog_report_other, null);
                dialog.setContentView(view);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                dialog.setContentView(view, params);
                editText = (EditText) view.findViewById(R.id.et_reason);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

                btn_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        content = editText.getText().toString().trim();
                        if (content.equals("")) {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "请输入举报内容");
                            return;
                        }
                        dialog.dismiss();
                        progress_update_blackbg.setVisibility(View.VISIBLE);
                        new Thread() {
                            public void run() {
                                doPostReport(MyConfig.URL_POST_REPORT);
                            }

                            ;
                        }.start();
                    }
                });
                dialog.show();
                // 显示软键盘
                MyUtils.showSoftInput(this, editText);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            progress_update_blackbg.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    postData(MyConfig.URL_POST_COMMUNITY_ACTIVITY_SUPPORT);
                }

                ;
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 判断是否设置过支付密码
     *
     * @param uploadHost
     */
    private void isHavePayPW(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("-4")) {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "请先设置支付密码");
                            Intent intent = new Intent(
                                    CommActivityDetailFreeActivity.this,
                                    PayPWUpdateActivity.class);
                            startActivity(intent);
                            return;
                        }
                        if (!isDestroyed()) {
                            handlerPay.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 举报
     *
     * @param uploadHost
     */
    private void doPostReport(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "4");
        params.addBodyParameter("rid", aid);
        params.addBodyParameter("sid", msid);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("content", content);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        } else if (result.equals("-1")) {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "您已举报过了");
                        } else {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "举报成功");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 提交数据,提交支付金额
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", aid);
        params.addBodyParameter("cid", communityActivityInfo.getCommId());
        params.addBodyParameter("classifyid",
                communityActivityInfo.getClassify() + "");
        params.addBodyParameter("money", communityActivityInfo.getFree());
        params.addBodyParameter("userId", uid);
        params.addBodyParameter("tip", "0");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            EventBus.getDefault().post(
                                    new MEventCommActiPay(true));
                            new Thread() {
                                public void run() {
                                    communityActivityInfo =
                                            getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                                    + aid + "&uid=" + uid);
                                    if (communityActivityInfo != null
                                            && !isDestroyed()) {
                                        handler.sendEmptyMessage(0);
                                    }
                                }

                                ;
                            }.start();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        progress_update_blackbg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 提交数据,免费参加
     */
    private void postDataFree(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", aid);
        params.addBodyParameter("cid", communityActivityInfo.getCommId());
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("type", "0");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            new Thread() {
                                public void run() {
                                    communityActivityInfo =
                                            getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                                    + aid + "&uid=" + uid);
                                    if (communityActivityInfo != null
                                            && !isDestroyed()) {
                                        handler.sendEmptyMessage(0);
                                    }
                                }

                                ;
                            }.start();
                        } else {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        progress_update_blackbg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 是否完善资料
     */
    private void postIsDataPerfect(String uploadHost, final int type) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "0");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("cid", communityActivityInfo.getCommId());
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (type == 1) {
                            if (result.equals("1") || result.equals("2")) {
                                if (!isDestroyed()) {
                                    handlerPerfect.sendEmptyMessage(0);
                                }
                            } else if (result.equals("-2")) {
                                ToastUtil.showToast(CommActivityDetailFreeActivity.this, "您权限不足");
                            } else {
                                if (!isDestroyed()) {
                                    handlerNoPerfect.sendEmptyMessage(0);
                                }
                            }
                        } else if (type == 2) {
                            if (result.equals("1") || result.equals("2")) {
                                Intent intent = new Intent(CommActivityDetailFreeActivity.this, CommSupportPayActivity.class);
                                intent.putExtra("aid", aid);
                                intent.putExtra("cid", communityActivityInfo.getCommId());
                                intent.putExtra("classifyid", communityActivityInfo.getClassify()
                                        + "");
                                intent.putExtra("tip", "1");
                                startActivity(intent);
                            } else if (result.equals("-2")) {
                                ToastUtil.showToast(CommActivityDetailFreeActivity.this, "您权限不足");
                            } else {
                                if (!isDestroyed()) {
                                    handlerNoPerfect.sendEmptyMessage(0);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 提交数据,收藏
     */
    private void postCollect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", aid);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
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
                            iv_collect.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.icon_collect_red));
                            communityActivityInfo.setCollect(1);
                            communityActivityInfo
                                    .setCollectCount(communityActivityInfo
                                            .getCollectCount() + 1);
                            tv_collectCount.setText(communityActivityInfo
                                    .getCollectCount() + "");
                            EventBus.getDefault().post(
                                    new MEventCollectActiChange(true));
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        ll_collect.setClickable(true);
                    }
                });
    }

    /**
     * 提交数据,取消收藏
     */
    private void postCollectCancel(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", aid);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
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
                            iv_collect.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.icon_collect));
                            communityActivityInfo.setCollect(0);
                            communityActivityInfo
                                    .setCollectCount(communityActivityInfo
                                            .getCollectCount() - 1);
                            tv_collectCount.setText(communityActivityInfo
                                    .getCollectCount() + "");
                            EventBus.getDefault().post(
                                    new MEventCollectActiChange(true));
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(CommActivityDetailFreeActivity.this, "操作失败");
                        ll_collect.setClickable(true);
                    }
                });
    }

    public void onEventMainThread(MEventSecondPage event) {
        if (event.isShow() && isFirstShow) {
            isFirstShow = false;
            for (int i = 0; i < communityActivityInfo.getActi_pics().size(); i++) {
                ImageView imageView = new ImageView(this);
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                imageView.setAdjustViewBounds(true);
                Glide.with(this)
                        .load(communityActivityInfo.getActi_pics().get(i))
                        .dontAnimate().placeholder(R.drawable.imgloading) // 设置占位图
                        .into(imageView);
                ll_image.addView(imageView);
            }
        }
    }

    public void onEventMainThread(MEventCommActiPay event) {
        if (event.isChange()) {
            progress_update_blackbg.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    communityActivityInfo =
                            getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                    + aid + "&uid=" + uid);
                    if (communityActivityInfo != null && !isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_popupwindow.getVisibility() == View.VISIBLE) {
                rl_popupwindow.setVisibility(View.GONE);
                return true;
            }
            if (dialog_report.getVisibility() == View.VISIBLE) {
                dialog_report.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (rl_popupwindow.getVisibility() == View.VISIBLE) {
            rl_popupwindow.setVisibility(View.GONE);
        }
        if (dialog_report.getVisibility() == View.VISIBLE) {
            dialog_report.setVisibility(View.GONE);
        }
        super.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (rl_popupwindow.getVisibility() == View.VISIBLE) {
                rl_popupwindow.setVisibility(View.GONE);
                return true;
            }
            if (dialog_report.getVisibility() == View.VISIBLE) {
                dialog_report.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }

}
