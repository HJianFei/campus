package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.DeliverAdapter;
import com.loosoo100.campus100.beans.DeliverInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * @author yang 物流信息详情activity
 */
public class DeliverActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.lv_deliver)
    private ListView lv_deliver; // 物流信息
    @ViewInject(R.id.tv_num)
    private TextView tv_num; // 物流单号
    @ViewInject(R.id.tv_company)
    private TextView tv_company; // 物流公司

    private DeliverInfo deliverInfo = null;
    private DeliverAdapter adapter;

    private String num = "";// 物流单号
    private String com = ""; // 物流公司字母名称
    private String name = ""; // 物流公司中文名称

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (deliverInfo != null) {
                initView();
                if (deliverInfo.getList() != null
                        && deliverInfo.getList().size() > 0) {
                    initListView();
                    lv_deliver.setVisibility(View.VISIBLE);
                } else {
                    lv_deliver.setVisibility(View.GONE);
                }
            } else {
                lv_deliver.setVisibility(View.GONE);
            }
            progress.setVisibility(View.GONE);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        rl_back.setOnClickListener(this);
        progress.setVisibility(View.VISIBLE);

        num = getIntent().getExtras().getString("num");
        com = getIntent().getExtras().getString("com");
        name = getIntent().getExtras().getString("name");
        new Thread() {
            public void run() {
                deliverInfo = GetData.getDeliverInfo(MyConfig.URL_JSON_DELIVER
                        + com + "&nums=" + num);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initListView() {
        adapter = new DeliverAdapter(this, deliverInfo.getList());
        lv_deliver.setAdapter(adapter);
        // MyUtils.setListViewHeight(lv_deliver,0);
    }

    private void initView() {
        tv_num.setText(num);
        tv_company.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                this.finish();
                break;

        }
    }

}
