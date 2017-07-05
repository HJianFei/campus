package com.loosoo100.campus100.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.AllGroupMembersAdapter;
import com.loosoo100.campus100.chat.bean.Groupnumber;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class AllGroupMembersActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_group_detail)
    private RelativeLayout rl_back_group_detail;
    @ViewInject(R.id.all_group_members)
    private ListView all_group_members;
    private String gid = "";
    private AllGroupMembersAdapter mAdapter;
    private Groupnumber groupNumbers = new Groupnumber();
    private List<Groupnumber.RootBean> groupRootBeanList = new ArrayList<>();
    private String uid = "";
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_group_members);
        gid = getIntent().getStringExtra("group_id");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");

    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        GsonRequest<Groupnumber> gsonRequest = new GsonRequest<>(MyConfig.GET_GROUP_NUMBER + "?gid=" + gid + "&uid=" + uid + "&page=" + page, Groupnumber.class, new Response.Listener<Groupnumber>() {
            @Override
            public void onResponse(Groupnumber groupnumber) {
                if (null != groupnumber) {
                    groupNumbers = groupnumber;
                    groupRootBeanList = groupnumber.getRoot();

                    initView();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void initView() {
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        rl_back_group_detail.setOnClickListener(this);
        mAdapter = new AllGroupMembersAdapter(AllGroupMembersActivity.this, groupNumbers, groupRootBeanList);
        all_group_members.setAdapter(mAdapter);
        all_group_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllGroupMembersActivity.this, GroupMemberActivity.class);
                intent.putExtra("gid", gid);
                intent.putExtra("group_id", groupNumbers.getUid());
                intent.putExtra("group_member_id", groupNumbers.getRoot().get(position).getUserId());
                intent.putExtra("isGrant", groupNumbers.getRoot().get(position).getStatus());
                startActivity(intent);

            }
        });
        all_group_members.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && page < groupNumbers.getTotalPage()) {
                    page++;
                    loadMoreData(uid, page);

                }
            }
        });
    }

    private void loadMoreData(String uid, int page) {
        GsonRequest<Groupnumber> gsonRequest = new GsonRequest<>(MyConfig.GET_GROUP_NUMBER + "?gid=" + gid + "&uid=" + uid + "&page=" + page, Groupnumber.class, new Response.Listener<Groupnumber>() {
            @Override
            public void onResponse(Groupnumber groupnumber) {
                if (null != groupnumber) {
                    groupNumbers = groupnumber;
                    groupRootBeanList.addAll(groupnumber.getRoot());
                    initView();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back_group_detail:
                this.finish();
                break;
        }

    }
}
