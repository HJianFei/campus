package com.loosoo100.campus100.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.FriendReqsAdapter;
import com.loosoo100.campus100.chat.bean.Friend_Reqs_List;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewFriendListActivity extends FragmentActivity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_new)
    private RelativeLayout rl_back_new;
    @ViewInject(R.id.new_friend_list)
    private ListView listView;
    @ViewInject(R.id.black_list)
    private ImageView black_list;
    private String uid = "";
    private int page = 1;
    private int count;
    private List<Friend_Reqs_List.RootBean> rootBeanList = new ArrayList<>();
    private Friend_Reqs_List friend_reqs = new Friend_Reqs_List();
    private FriendReqsAdapter mAdapter;
    @ViewInject(R.id.isData)
    private TextView isData;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1001) {
                initData(uid, page);
                mAdapter.notifyDataSetChanged();
            }
            if (msg.arg1 == 1002) {
                rootBeanList.remove(msg.arg2);
                mAdapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend_list);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        ViewUtils.inject(this);
        rl_back_new.setOnClickListener(this);
        black_list.setOnClickListener(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        initData(uid, page);
        //读取所有未读消息
        new Thread() {
            @Override
            public void run() {
                readAllMessage(MyConfig.FRIEND_READ + "?to_uid=" + uid);
            }
        }.start();
    }

    private void initView() {
        mAdapter = new FriendReqsAdapter(this, rootBeanList, handler);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                count = position;
                Intent intent = new Intent(NewFriendListActivity.this, CampusContactsFriendActivity.class);
                intent.putExtra("muid", rootBeanList.get(position).getUid());
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && page < friend_reqs.getTotalPage()) {
                    page++;
                    loadMoreData(uid, page);
                }
            }
        });

    }

    private void loadMoreData(final String uid, final int page) {
        GsonRequest<Friend_Reqs_List> gsonRequest = new GsonRequest<Friend_Reqs_List>(Request.Method.POST, MyConfig.GET_FRIEND_REQS_LIST, Friend_Reqs_List.class, new Response.Listener<Friend_Reqs_List>() {
            @Override
            public void onResponse(Friend_Reqs_List friend_reqs_list) {
                friend_reqs = friend_reqs_list;
                rootBeanList.addAll(friend_reqs_list.getRoot());
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
                map.put("to_uid", uid);
                map.put("page", page + "");
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);

    }


    private void initData(final String uid, final int page) {
        LogUtils.d("onResponse", MyConfig.GET_FRIEND_REQS_LIST + "?to_uid=" + uid + "&page=1");
        GsonRequest<Friend_Reqs_List> gsonRequest = new GsonRequest<Friend_Reqs_List>(Request.Method.POST, MyConfig.GET_FRIEND_REQS_LIST, Friend_Reqs_List.class, new Response.Listener<Friend_Reqs_List>() {
            @Override
            public void onResponse(Friend_Reqs_List friend_reqs_list) {
                friend_reqs = friend_reqs_list;
                rootBeanList = friend_reqs_list.getRoot();
                if (null != rootBeanList) {
                    initView();
                } else {
                    isData.setVisibility(View.VISIBLE);
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
                map.put("to_uid", uid);
                map.put("page", page + "");
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back_new:
                this.finish();
                break;
            case R.id.black_list:
                Intent intent = new Intent(NewFriendListActivity.this, BlackListActivity.class);
                startActivity(intent);
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10001) {
            initData(uid, page);
        }
        if (resultCode == 10002) {
            Message message = Message.obtain();
            message.arg1 = 1002;
            message.arg2 = count;
            handler.sendMessage(message);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void readAllMessage(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                    }
                });
    }
}
