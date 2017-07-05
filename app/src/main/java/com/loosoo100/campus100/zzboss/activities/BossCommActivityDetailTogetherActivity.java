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
import android.widget.ProgressBar;
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
import com.loosoo100.campus100.activities.CommTogetherDetailActivity;
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

/**
 * @author yang 社团活动详情-众筹
 */
public class BossCommActivityDetailTogetherActivity extends Activity implements
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

    @ViewInject(R.id.tv_supportCount)
    private TextView tv_supportCount; // 支持人数
    @ViewInject(R.id.tv_supportMoney)
    private TextView tv_supportMoney; // 支持金额
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar; // 百分比进度条
    @ViewInject(R.id.tv_percent)
    private TextView tv_percent; // 已筹集的百分比
    @ViewInject(R.id.tv_needMoney)
    private TextView tv_needMoney; // 需求的金额
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
    private String cuid = "";
    // private String org = "";

    private CommunityActivityInfo communityActivityInfo;

    private ScaleAnimation animation; // 收藏按钮点击动画

    private boolean isFirstShow = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            progress.setVisibility(View.GONE);
            progress_update_blackbg.setVisibility(View.GONE);
        }

    };

    private Handler handlerPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(BossCommActivityDetailTogetherActivity.this, BossCommJoinPayActivity.class);
            intent.putExtra("aid", aid);
            intent.putExtra("cid", communityActivityInfo.getCommId());
            intent.putExtra("classifyid", communityActivityInfo.getClassify()
                    + "");
            intent.putExtra("tip", "0");
            startActivity(intent);
        }
    };
    private Handler handlerNoPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CustomDialog.Builder builder = new CustomDialog.Builder(
                    BossCommActivityDetailTogetherActivity.this);
            builder.setMessage("请先完善个人资料");
            builder.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    BossCommActivityDetailTogetherActivity.this,
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
        setContentView(R.layout.activity_boss_comm_activity_detail_together);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setMiuiStatusBarDarkMode(this, true);
        EventBus.getDefault().register(this);

        aid = getIntent().getExtras().getString("aid");

        progress.setVisibility(View.VISIBLE);
        animation = MyAnimation.getScaleAnimationLocation();
        rl_back.setOnClickListener(this);

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");
        LogUtils.d("onResssponse", MyConfig.URL_JSON_ACTI_DETAILS_BOSS
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
        btn_join.setOnClickListener(this);

        if (communityActivityInfo.getStatus() == 1) {
            tv_over.setVisibility(View.VISIBLE);
            btn_join.setVisibility(View.GONE);
        } else {
            tv_over.setVisibility(View.GONE);
            btn_join.setVisibility(View.VISIBLE);
        }

        if (communityActivityInfo.getCanyu() == 1) {
            btn_join.setText("已参与");
            btn_join.setClickable(false);
        } else {
            btn_join.setText("立即参与");
            btn_join.setClickable(true);
        }

        if (communityActivityInfo.getCollect() == 0) {
            iv_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect));
        } else {
            iv_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_red));
        }

        tv_supportCount.setText("" + communityActivityInfo.getSupportCount());
        tv_supportMoney.setText("￥" + communityActivityInfo.getRaisedMoney());
        tv_percent.setText((int) (communityActivityInfo.getRaisedMoney()
                / communityActivityInfo.getNeedMoney() * 100)
                + "%");
        tv_needMoney.setText("" + communityActivityInfo.getNeedMoney());
        progressBar.setProgress((int) (communityActivityInfo.getRaisedMoney()
                / communityActivityInfo.getNeedMoney() * 100));

        tv_activityName.setText(communityActivityInfo.getActivityName());
        tv_readCount.setText("" + communityActivityInfo.getReadCount());
        tv_collectCount.setText("" + communityActivityInfo.getCollectCount());
        tv_remainDay.setText(communityActivityInfo.getRemainDay() + "天");
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
                Intent intent2 = new Intent(this,
                        CommTogetherDetailActivity.class);
                intent2.putExtra("aid", aid);
                startActivity(intent2);
                break;

            case R.id.ll_chat:
                if (communityActivityInfo.getFlag()==0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            BossCommActivityDetailTogetherActivity.this);
                    builder.setMessage("请先认证企业");
                    builder.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BossCommActivityDetailTogetherActivity.this,
                                            BossIdentificationActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else if (communityActivityInfo.getFlag()==1) {
                    ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "请等待企业审核通过后再联系社团");
                } else if (communityActivityInfo.getFlag()==2) {
                    //启动会话界面
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(BossCommActivityDetailTogetherActivity.this, communityActivityInfo.getCommUid(), communityActivityInfo.getCommunityName());
                    }
                } else {
                    ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "网络异常，请稍候再试");
                }
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

            // 参与
            case R.id.btn_join:
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postIsDataPerfect(MyConfig.URL_POST_IS_INFO_PERFECT);
                    }

                    ;
                }.start();
                break;

        }
    }

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
                            ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "操作失败");
                        ll_collect.setClickable(true);
                    }
                });
    }

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
                            ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "操作失败");
                        ll_collect.setClickable(true);
                    }
                });
    }

    /**
     * 是否完善资料
     */
    private void postIsDataPerfect(String uploadHost) {
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
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1") || result.equals("2")) {
                            if (!isDestroyed()) {
                                handlerPerfect.sendEmptyMessage(0);
                            }
                        } else if (result.equals("-2")) {
                            ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "您权限不足");
                        } else {
                            if (!isDestroyed()) {
                                handlerNoPerfect.sendEmptyMessage(0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(BossCommActivityDetailTogetherActivity.this, "操作失败");
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
