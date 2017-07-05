package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventSecondPage;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.imitateTB.MyScrollView;
import com.loosoo100.campus100.zzboss.activities.BossCommNeedMemJoinActivity;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

import de.greenrobot.event.EventBus;

/**
 * @author yang 社团需求详情
 */
public class CommDemandBoothDetailActivity extends Activity implements
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
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
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

    private boolean isFirstShow = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            progress.setVisibility(View.GONE);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_demand_booth_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        EventBus.getDefault().register(this);

        did = getIntent().getExtras().getString("did");

        progress.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);

        new Thread() {
            public void run() {
                commSupportInfo = GetData
                        .getBossNeedsDetailsInfo(MyConfig.URL_JSON_NEED_DETAIL_BOSS
                                + did);
                if (commSupportInfo != null && !isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initView() {
        ll_comm.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        btn_support.setOnClickListener(this);
        btn_support.setText("报名人员");

        myScrollView01.setOnTouchListener(this);
        myScrollView02.setOnTouchListener(this);

        if (Float.valueOf(commSupportInfo.getFree()) == 0) {
            tv_money.setText("免费");

        } else {
            tv_money.setText("￥" + commSupportInfo.getFree());
        }

        tv_personCount.setText("剩余:"
                + (int) (commSupportInfo.getNeedMoney() - commSupportInfo
                .getRaiseMoney()));
        tv_activityName.setText(commSupportInfo.getDemandName());
        tv_readCount.setText("" + commSupportInfo.getReadCount());
        tv_collectCount.setText("" + commSupportInfo.getCollectCount());
        tv_overTime.setText(commSupportInfo.getOverTime());
        tv_id.setText(commSupportInfo.getCommId() + "");
        tv_city_school.setText(commSupportInfo.getCity() + "-"
                + commSupportInfo.getSchool());
        tv_communityName.setText(commSupportInfo.getCommName());
        tv_offer.setText(commSupportInfo.getOffer());

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

            // 社团
            case R.id.ll_comm:
                Intent intentComm = new Intent(this, CommDetailActivity.class);
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
                        BossCommNeedMemJoinActivity.class);
                intentList.putExtra("did", commSupportInfo.getId());
                startActivity(intentList);
                break;
        }
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
