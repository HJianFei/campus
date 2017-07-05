package com.loosoo100.campus100.chat.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.BlackListAdapter;
import com.loosoo100.campus100.chat.bean.BlackList;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BlackListActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_black)
    private RelativeLayout rl_back_black;
    @ViewInject(R.id.blacklsit_list)
    private ListView black_list;
    @ViewInject(R.id.blacklsit_show_data)
    private TextView show_data;
    private String uid = "";
    private int page = 1;
    private BlackList blackLists = new BlackList();
    private List<BlackList.RootBean> beanList = new ArrayList<>();
    private BlackListAdapter mAdapter;
    private AlertDialog alertDialog;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        ViewUtils.inject(this);
        rl_back_black.setOnClickListener(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        initData(uid, page);
    }

    private void initData(final String uid, final int page) {
        GsonRequest<BlackList> gsonRequest = new GsonRequest<BlackList>(Request.Method.POST, MyConfig.GET_BLACK_LIST, BlackList.class, new Response.Listener<BlackList>() {
            @Override
            public void onResponse(BlackList blackList) {
                blackLists = blackList;
                beanList = blackList.getRoot();
                if (null !=beanList) {
                    initView();
                } else {
                    show_data.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("page", page + "");
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void initView() {
        mAdapter = new BlackListAdapter(this, beanList);
        black_list.setAdapter(mAdapter);
        black_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                showAlertDialog();

            }
        });
        black_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && page < blackLists.getTotalPage()) {
                    page++;
                    loadMoreData(uid, page);

                }
            }
        });

    }

    private void loadMoreData(final String uid, final int page) {
        GsonRequest<BlackList> gsonRequest = new GsonRequest<BlackList>(Request.Method.POST, MyConfig.GET_BLACK_LIST, BlackList.class, new Response.Listener<BlackList>() {
            @Override
            public void onResponse(BlackList blackList) {
                blackLists = blackList;
                beanList.addAll(blackList.getRoot());
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("page", page + "");
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void showAlertDialog() {
        View layout = View.inflate(BlackListActivity.this, R.layout.black_list_dialog, null);
        layout.findViewById(R.id._remove_friend).setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(BlackListActivity.this).setView(layout).setInverseBackgroundForced(true).create();
        alertDialog.show();
    }

    private void removeBlackList(final String uid, final String black_uid) {

        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<RequestCode_1>(Request.Method.POST, MyConfig.REMOVE_FROM_BLACK_LIST, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                LogUtils.d("requestCode1", requestCode_1.getCode() + "");
                if (requestCode_1.getCode() == 200) {
                    ToastUtil.showToast(BlackListActivity.this, "移除成功");
                    blackLists.getRoot().remove(index);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(BlackListActivity.this, "移除失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast(BlackListActivity.this, "移除失败，稍后再试");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("black_uid", black_uid);
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back_black:
                this.finish();
                break;
            case R.id._remove_friend:
                if (null != alertDialog) {
                    alertDialog.dismiss();
                }
                removeBlackList(uid, blackLists.getRoot().get(index).getBlack_uid());
                break;
        }

    }
}
