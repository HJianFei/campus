package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.zzboss.activities.BossCommActivityDetailFreeActivity;
import com.loosoo100.campus100.zzboss.beans.BossCompanySummaryInfo;

import io.rong.imkit.RongIM;

/**
 * @author yang 公司简介
 */
public class CompanySummaryActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.iv_bg)
    private ImageView iv_bg;
    @ViewInject(R.id.cv_logo)
    private CircleView cv_logo;
    @ViewInject(R.id.iv_v)
    private ImageView iv_v;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.tv_count)
    private TextView tv_count;
    @ViewInject(R.id.tv_company)
    private TextView tv_company;
    @ViewInject(R.id.tv_host)
    private TextView tv_host;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.tv_frame)
    private TextView tv_frame;
    @ViewInject(R.id.tv_property)
    private TextView tv_property;
    @ViewInject(R.id.tv_size)
    private TextView tv_size;
    @ViewInject(R.id.tv_summary)
    private TextView tv_summary;
    @ViewInject(R.id.ll_chat)
    private LinearLayout ll_chat;
    @ViewInject(R.id.progress)
    private RelativeLayout progress;

    private BossCompanySummaryInfo summaryInfo;
    private String cuid = "";
    private String uid = "";

    // private String fileName = "";
    // private String filePath = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            progress.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_summary);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        cuid = getIntent().getExtras().getString("cuid");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        rl_back.setOnClickListener(this);
        ll_chat.setOnClickListener(this);

        progress.setVisibility(View.VISIBLE);

        new Thread() {
            public void run() {
                summaryInfo = GetData
                        .getBossCompanySummaryInfo(MyConfig.URL_JSON_SUMMARY_BOSS
                                + cuid + "&uid=" + uid);
                if (summaryInfo != null && !isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initView() {
        if (summaryInfo.getState() > 0) {
            ll_chat.setVisibility(View.VISIBLE);
        } else {
            ll_chat.setVisibility(View.GONE);
        }
        Glide.with(this).load(summaryInfo.getBgThumb())
                .placeholder(R.drawable.imgloading_big).into(iv_bg);
        if (!summaryInfo.getLogo().equals("")) {
            Glide.with(this).load(summaryInfo.getLogo()).into(cv_logo);
        }

        if (!summaryInfo.getMoney().equals("")) {
            tv_money.setText(summaryInfo.getMoney());
        }
        tv_count.setText(summaryInfo.getCount() + "");
        tv_company.setText(summaryInfo.getCompany());
        tv_host.setText(summaryInfo.getHost());
        tv_address.setText(summaryInfo.getAddress());
        tv_frame.setText(summaryInfo.getFrame());
        tv_property.setText(summaryInfo.getProperty());
        tv_size.setText(summaryInfo.getSize());
        tv_summary.setText(summaryInfo.getSummary());
        if (summaryInfo.getSummary().equals("")) {
            tv_summary.setVisibility(View.GONE);
        }
        //是否已认证
        if (summaryInfo.getFlag() == 2) {
//			iv_v.setVisibility(View.VISIBLE);
            iv_v.setImageResource(R.drawable.icon_v_summary);
        } else {
//			iv_v.setVisibility(View.GONE);
            iv_v.setImageResource(R.drawable.icon_v_summary_no);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.ll_chat:
                if (summaryInfo.getState() < 20) {
                    ToastUtil.showToast(this,"社团成员不足20人，无法联系");
                    return;
                }
                if (summaryInfo.getFlag() != 2) {
                    ToastUtil.showToast(this,"该企业未认证，无法联系");
                    return;
                }
                //启动会话界面
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(this, "cmp" + cuid, summaryInfo.getCompany());
                }
                break;
        }
    }
}
