package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.loosoo100.campus100.anyevent.MEventCollectNeedChange;
import com.loosoo100.campus100.anyevent.MEventCommNeedPay;
import com.loosoo100.campus100.anyevent.MEventSecondPage;
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
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * @author yang 社团需求详情
 */
public class BossCommDemandMoneyDetailActivity extends Activity implements
        OnClickListener, OnTouchListener {
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
    @ViewInject(R.id.tv_overTime)
    private TextView tv_overTime; // 截止日期
    @ViewInject(R.id.tv_offer)
    private TextView tv_offer; // 提供的内容
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar; // 进度条
    @ViewInject(R.id.tv_percent)
    private TextView tv_percent; // 百分比
    @ViewInject(R.id.tv_raiseMoney)
    private TextView tv_raiseMoney; // 已筹集金额
    @ViewInject(R.id.tv_needMoney)
    private TextView tv_needMoney; // 需求
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
    @ViewInject(R.id.ll_chat)
    private LinearLayout ll_chat; // 联系主办社团
    @ViewInject(R.id.ll_collect)
    private LinearLayout ll_collect; // 收藏布局
    @ViewInject(R.id.iv_collect)
    private ImageView iv_collect; // 收藏按钮
    @ViewInject(R.id.tv_over)
    private TextView tv_over; // 已结束
    @ViewInject(R.id.btn_pay)
    private Button btn_pay; // 赞助按钮
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
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more; // 更多
    @ViewInject(R.id.rl_popupwindow)
    private RelativeLayout rl_popupwindow; // 弹框
    @ViewInject(R.id.btn_support)
    private Button btn_support; // 赞助列表

    private String did = ""; // 传进来的需求ID

    private BossCommSupportInfo commSupportInfo;

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

    private Handler handlerPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(BossCommDemandMoneyDetailActivity.this, BossCommNeedPayActivity.class);
            intent.putExtra("aid", did);
            intent.putExtra("cid", commSupportInfo.getCommId());
            intent.putExtra("classifyid", commSupportInfo.getClassify() + "");
            startActivity(intent);
        }
    };
    private Handler handlerNoPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CustomDialog.Builder builder = new CustomDialog.Builder(
                    BossCommDemandMoneyDetailActivity.this);
            builder.setMessage("请先完善个人资料");
            builder.setPositiveButton("是",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    BossCommDemandMoneyDetailActivity.this,
                                    BossBasicInfoActivity.class);
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
        setContentView(R.layout.activity_boss_comm_demand_money_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        EventBus.getDefault().register(this);

        did = getIntent().getExtras().getString("did");

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");

        progress.setVisibility(View.VISIBLE);
        animation = MyAnimation.getScaleAnimationLocation();
        rl_back.setOnClickListener(this);

        new Thread() {
            public void run() {
                commSupportInfo = GetData
                        .getBossNeedsDetailsInfo(MyConfig.URL_JSON_NEED_DETAIL_BOSS
                                + did + "&uid=" + cuid);
                if (commSupportInfo != null && !isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initView() {
        ll_chat.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_comm.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        btn_support.setOnClickListener(this);

        myScrollView01.setOnTouchListener(this);
        myScrollView02.setOnTouchListener(this);

        if (commSupportInfo.getStatus() == 1) {
            tv_over.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.GONE);
        } else {
            tv_over.setVisibility(View.GONE);
            btn_pay.setVisibility(View.VISIBLE);
        }

        if (commSupportInfo.getCanyu() == 1) {
            btn_pay.setText("已参与");
            btn_pay.setClickable(false);
        } else {
            btn_pay.setText("立即参与");
            btn_pay.setClickable(true);
        }

        // if (commSupportInfo.getType().equals("0")) {
        // btn_pay.setClickable(false);
        // btn_pay.setText("限本校参与");
        // btn_pay.setBackgroundColor(getResources().getColor(R.color.gray_a1a1a1));
        // }

        if (commSupportInfo.getCollect() == 0) {
            iv_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect));
        } else {
            iv_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_red));
        }

        tv_raiseMoney.setText("￥" + commSupportInfo.getRaiseMoney());
        tv_needMoney.setText("需求:" + commSupportInfo.getNeedMoney());
        tv_activityName.setText(commSupportInfo.getDemandName());
        tv_readCount.setText("" + commSupportInfo.getReadCount());
        tv_collectCount.setText("" + commSupportInfo.getCollectCount());
        tv_overTime.setText(commSupportInfo.getOverTime());
        tv_id.setText(commSupportInfo.getCommId() + "");
        tv_city_school.setText(commSupportInfo.getCity() + "-"
                + commSupportInfo.getSchool());
        tv_communityName.setText(commSupportInfo.getCommName());
        tv_offer.setText(commSupportInfo.getOffer());

        tv_percent.setText((int) (commSupportInfo.getRaiseMoney()
                / commSupportInfo.getNeedMoney() * 100)
                + "%");
        progressBar.setProgress((int) (commSupportInfo.getRaiseMoney()
                / commSupportInfo.getNeedMoney() * 100));

        webView.loadDataWithBaseURL(null, commSupportInfo.getLetter(),
                "text/html", "utf-8", null);

        if (!commSupportInfo.getHeadthumb().equals("")
                && !commSupportInfo.getHeadthumb().equals("null")) {
            Glide.with(this).load(commSupportInfo.getHeadthumb())
                    .into(iv_picture);
        }
        if (!commSupportInfo.getCommHeadShot().equals("")
                && !commSupportInfo.getCommHeadShot().equals("null")) {
            Glide.with(this).load(commSupportInfo.getCommHeadShot())
                    .into(cv_headShot);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.ll_chat:
                if (commSupportInfo.getFlag()==0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            BossCommDemandMoneyDetailActivity.this);
                    builder.setMessage("请先认证企业");
                    builder.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BossCommDemandMoneyDetailActivity.this,
                                            BossIdentificationActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else if (commSupportInfo.getFlag()==1) {
                    ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "请等待企业审核通过后再联系社团");
                } else if (commSupportInfo.getFlag()==2) {
                    //启动会话界面
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(BossCommDemandMoneyDetailActivity.this, commSupportInfo.getCommUid(), commSupportInfo.getCommName());
                    }
                } else {
                    ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "网络异常，请稍候再试");
                }
                break;

            case R.id.btn_pay:
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postIsDataPerfect(MyConfig.URL_POST_IS_INFO_PERFECT);
                    }

                    ;
                }.start();
                break;

            // 收藏按钮
            case R.id.ll_collect:
                ll_collect.setClickable(false);
                iv_collect.startAnimation(animation);
                if (commSupportInfo.getCollect() == 0) {
                    new Thread() {
                        public void run() {
                            postCollect(MyConfig.URL_POST_NEED_COLLECT_BOSS);
                        }

                        ;
                    }.start();
                } else {
                    new Thread() {
                        public void run() {
                            postCollectCancel(MyConfig.URL_POST_NEED_COLLECT_CANCEL_BOSS);
                        }

                        ;
                    }.start();
                }
                break;

            // 社团
            case R.id.ll_comm:
                Intent intentComm = new Intent(this, BossCommDetailActivity.class);
                intentComm.putExtra("id", commSupportInfo.getCommId());
                startActivity(intentComm);
                break;

            case R.id.rl_more:
                if (rl_popupwindow.getVisibility() == View.VISIBLE) {
                    rl_popupwindow.setVisibility(View.GONE);
                } else {
                    rl_popupwindow.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_support:
                rl_popupwindow.setVisibility(View.GONE);
                Intent intentList = new Intent(this,
                        BossCommNeedListDetailActivity.class);
                intentList.putExtra("did", commSupportInfo.getId());
                startActivity(intentList);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if (resultCode == RESULT_OK && requestCode == 1) {
        // progress_update_blackbg.setVisibility(View.VISIBLE);
        // new Thread() {
        // public void run() {
        // postData(MyConfig.URL_POST_COMM_ACTI_SUPPORT_BOSS);
        // };
        // }.start();
        // }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 是否完善资料
     */
    private void postIsDataPerfect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "1");
        params.addBodyParameter("uid", cuid);
        params.addBodyParameter("cid", commSupportInfo.getCommId());
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
                            ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "您权限不足");
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
                        ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 提交数据,收藏
     */
    private void postCollect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", did);
        params.addBodyParameter("uid", cuid);
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
                            commSupportInfo.setCollect(1);
                            commSupportInfo.setCollectCount(commSupportInfo
                                    .getCollectCount() + 1);
                            tv_collectCount.setText(commSupportInfo
                                    .getCollectCount() + "");
                            EventBus.getDefault().post(
                                    new MEventCollectNeedChange(true));
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "操作失败");
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
        params.addBodyParameter("aid", did);
        params.addBodyParameter("uid", cuid);
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
                            commSupportInfo.setCollect(0);
                            commSupportInfo.setCollectCount(commSupportInfo
                                    .getCollectCount() - 1);
                            tv_collectCount.setText(commSupportInfo
                                    .getCollectCount() + "");
                            EventBus.getDefault().post(
                                    new MEventCollectNeedChange(true));
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "操作失败");
                        }
                        ll_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCommDemandMoneyDetailActivity.this, "操作失败");
                        ll_collect.setClickable(true);
                    }
                });
    }

    public void onEventMainThread(MEventSecondPage event) {
        if (event.isShow() && isFirstShow) {
            isFirstShow = false;
            for (int i = 0; i < commSupportInfo.getPics().size(); i++) {
                ImageView imageView = new ImageView(this);
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                imageView.setAdjustViewBounds(true);
                Glide.with(this).load(commSupportInfo.getPics().get(i))
                        .dontAnimate().placeholder(R.drawable.imgloading) // 设置占位图
                        .into(imageView);
                ll_image.addView(imageView);
            }
        }
    }

    public void onEventMainThread(MEventCommNeedPay event) {
        if (event.isChange()) {
            progress_update_blackbg.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    commSupportInfo = GetData
                            .getBossNeedsDetailsInfo(MyConfig.URL_JSON_NEED_DETAIL_BOSS
                                    + did + "&uid=" + cuid);
                    if (commSupportInfo != null && !isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (rl_popupwindow.getVisibility() == View.VISIBLE) {
                rl_popupwindow.setVisibility(View.GONE);
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
