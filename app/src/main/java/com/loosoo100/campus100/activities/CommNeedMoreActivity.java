package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommActiListHomeAdapter;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.activities.BossCommDemandBoothDetailActivity;
import com.loosoo100.campus100.zzboss.activities.BossCommDemandMoneyDetailActivity;
import com.loosoo100.campus100.zzboss.activities.BossCommSupportActivity;
import com.loosoo100.campus100.zzboss.adapter.BossCommSupportAdapter;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.loosoo100.campus100.utils.GetData.getMyNeedsInfos;
import static com.loosoo100.campus100.view.spinkit.animation.AnimationUtils.start;

/**
 * @author yang 我的社团更多需求
 */
public class CommNeedMoreActivity extends Activity implements
        OnClickListener, OnScrollListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.listView)
    private ListView listView; // 列表
    @ViewInject(R.id.rl_progress)
    private RelativeLayout rl_progress; // 加载动画

    private int page = 2;

    private BossCommSupportAdapter adapter;
    private List<BossCommSupportInfo> list;
    private Intent intent = new Intent();

    private boolean loading = true;
    private String cid = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            rl_progress.setVisibility(View.GONE);
            if (list != null && list.size() > 0) {
                initListView();
                listView.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.GONE);
            }
            page = 2;
            loading = false;
        }
    };

    /*
     * 上拉刷新完成后更新数据
     */
    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            page++;
            loading = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_need_more);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        cid = getIntent().getExtras().getString("cid");

        rl_progress.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        listView.setOnScrollListener(this);

        // 数据后台加载
        new Thread() {
            @Override
            public void run() {
                list = getMyNeedsInfos(MyConfig.URL_JSON_COMM_NEED_MORE
                        + "?id=" + cid);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();

    }

    private void initListView() {
        adapter = new BossCommSupportAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (list.get(position).getClassify() == 0) {
                    Intent intent = new Intent(CommNeedMoreActivity.this,
                            CommDemandMoneyDetailActivity.class);
                    intent.putExtra("did", list.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CommNeedMoreActivity.this,
                            CommDemandBoothDetailActivity.class);
                    intent.putExtra("did", list.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount
                && totalItemCount > 0 && !loading) {
            loading = true;
            new Thread() {
                public void run() {
                    List<BossCommSupportInfo> list2 = GetData
                            .getMyNeedsInfos(MyConfig.URL_JSON_COMM_NEED_MORE
                                    + "?id=" + cid + "&page=" + page);
                    if (list2 != null
                            && list2.size() > 0) {
                        for (int i = 0; i < list2.size(); i++) {
                            list.add(list2.get(i));
                        }
                        handlerRefresh.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }


}
