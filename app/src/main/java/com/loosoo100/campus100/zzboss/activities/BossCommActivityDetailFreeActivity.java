package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.Button;
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
import com.loosoo100.campus100.activities.PayPWUpdateActivity;
import com.loosoo100.campus100.anyevent.MEventCollectActiChange;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.anyevent.MEventSecondPage;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.imitateTB.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

import static com.loosoo100.campus100.activities.MyCommunityActivity.communityBasicInfos;

/**
 * @author yang 社团活动详情-免费
 */
public class BossCommActivityDetailFreeActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
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
    @ViewInject(R.id.ll_chat)
    private LinearLayout ll_chat; // 联系主办社团
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
    @ViewInject(R.id.webView)
    private WebView webView;
    @ViewInject(R.id.myScrollView01)
    private MyScrollView myScrollView01;
    @ViewInject(R.id.myScrollView02)
    private MyScrollView myScrollView02;

    private String aid = ""; // 传进来的活动ID

    private CommunityActivityInfo communityActivityInfo;

    private ScaleAnimation animation; // 收藏按钮点击动画

    private boolean isFirstShow = true;

    private String cuid = "";

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
            Intent intentPay = new Intent(
                    BossCommActivityDetailFreeActivity.this,
                    BossPayActivity.class);
            intentPay.putExtra("money",
                    Float.valueOf(communityActivityInfo.getFree()));
            intentPay.putExtra("aid", aid);
            intentPay.putExtra("cid", communityActivityInfo.getCommId());
            intentPay.putExtra("type", "act1");
            startActivityForResult(intentPay, 1);
        }

        ;
    };

    private Handler handlerPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (Float.valueOf(communityActivityInfo.getFree()) == 0) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(
                        BossCommActivityDetailFreeActivity.this);
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
                    BossCommActivityDetailFreeActivity.this);
            builder.setMessage("请先完善个人资料");
            builder.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    BossCommActivityDetailFreeActivity.this,
                                    BossBasicInfoActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("否", null);
            builder.create().show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_comm_activity_detail_free);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        EventBus.getDefault().register(this);

        aid = getIntent().getExtras().getString("aid");

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");

        progress.setVisibility(View.VISIBLE);
        animation = MyAnimation.getScaleAnimationLocation();
        rl_back.setOnClickListener(this);
        LogUtils.d("onResponse", MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                + aid + "&uid=" + cuid + "&type=1");

        new Thread() {
            public void run() {
                communityActivityInfo = GetData
                        .getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                + aid + "&uid=" + cuid + "&type=1");
                if (communityActivityInfo != null && !isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initView() {
        ll_payDetail.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_comm.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_join.setOnClickListener(this);

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
            btn_join.setClickable(false);
            btn_join.setText("限本校参与");
            btn_join.setBackgroundColor(getResources().getColor(
                    R.color.gray_a1a1a1));
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

            case R.id.ll_chat:
                if (communityActivityInfo.getFlag()==0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            BossCommActivityDetailFreeActivity.this);
                    builder.setMessage("请先认证企业");
                    builder.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BossCommActivityDetailFreeActivity.this,
                                            BossIdentificationActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else if (communityActivityInfo.getFlag()==1) {
                    ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "请等待企业审核通过后再联系社团");
                } else if (communityActivityInfo.getFlag()==2) {
                    //启动会话界面
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(BossCommActivityDetailFreeActivity.this, communityActivityInfo.getCommUid(), communityActivityInfo.getCommunityName());
                    }
                } else {
                    ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "网络异常，请稍候再试");
                }
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
                Intent intentComm = new Intent(this, BossCommDetailActivity.class);
                intentComm.putExtra("id", communityActivityInfo.getCommId());
                startActivity(intentComm);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            progress_update_blackbg.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    postData(MyConfig.URL_POST_COMM_ACTI_SUPPORT_BOSS);
                }

                ;
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 是否完善资料
     */
    private void postIsDataPerfect(String uploadHost, final int type) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "1");
        params.addBodyParameter("uid", cuid);
        params.addBodyParameter("cid", communityActivityInfo.getCommId());
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
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
                                progress_update_blackbg.setVisibility(View.GONE);
                                ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "您权限不足");
                            } else {
                                progress_update_blackbg.setVisibility(View.GONE);
                                if (!isDestroyed()) {
                                    handlerNoPerfect.sendEmptyMessage(0);
                                }
                            }
                        } else if (type == 2) {
                            if (result.equals("1") || result.equals("2")) {
                                Intent intent = new Intent(BossCommActivityDetailFreeActivity.this, BossCommSupportPayActivity.class);
                                intent.putExtra("aid", aid);
                                intent.putExtra("cid", communityActivityInfo.getCommId());
                                intent.putExtra("classifyid", communityActivityInfo.getClassify()
                                        + "");
                                intent.putExtra("tip", "1");
                                startActivity(intent);
                            } else if (result.equals("-2")) {
                                progress_update_blackbg.setVisibility(View.GONE);
                                ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "您权限不足");
                            } else {
                                progress_update_blackbg.setVisibility(View.GONE);
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
                        ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 提交数据,提交支付金额
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", aid);
        params.addBodyParameter("cid", communityActivityInfo.getCommId());
        params.addBodyParameter("classifyid",
                communityActivityInfo.getClassify() + "");
        params.addBodyParameter("money", communityActivityInfo.getFree());
        params.addBodyParameter("uid", cuid);
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
                                    communityActivityInfo = GetData
                                            .getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                                    + aid
                                                    + "&uid="
                                                    + cuid
                                                    + "&type=1");
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
                        ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");
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
        params.addBodyParameter("uid", cuid);
        params.addBodyParameter("type", "1");
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
                                    communityActivityInfo = GetData
                                            .getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                                    + aid
                                                    + "&uid="
                                                    + cuid
                                                    + "&type=1");
                                    if (communityActivityInfo != null
                                            && !isDestroyed()) {
                                        handler.sendEmptyMessage(0);
                                    }
                                }

                                ;
                            }.start();
                        } else {
                            ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");
                        progress_update_blackbg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 判断是否设置过支付密码
     *
     * @param uploadHost
     */
    private void isHavePayPW(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", cuid);
        params.addBodyParameter("type", "1");
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
                            ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "请先设置支付密码");
                            Intent intent = new Intent(
                                    BossCommActivityDetailFreeActivity.this,
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
     * 提交数据,收藏
     */
    private void postCollect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", aid);
        params.addBodyParameter("uid", cuid);
        params.addBodyParameter("type", "1");
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
                            ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");

                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");

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
        params.addBodyParameter("uid", cuid);
        params.addBodyParameter("type", "1");
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
                            ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCommActivityDetailFreeActivity.this, "操作失败");
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
                    communityActivityInfo = GetData
                            .getBossCommActiInfo(MyConfig.URL_JSON_ACTI_DETAILS_BOSS
                                    + aid + "&uid=" + cuid + "&type=1");
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

}
