package com.loosoo100.campus100.chat.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.Friend_Reqs_List;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import java.util.HashMap;
import java.util.Map;

public class FriendReqsDetailActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_req)
    private RelativeLayout rl_back_req;
    @ViewInject(R.id.req_name)
    private TextView req_name;
    @ViewInject(R.id.req_avatar)
    private CircleView req_avatar;
    @ViewInject(R.id.tv_message)
    private TextView tv_message;
    @ViewInject(R.id.agree_req)
    private Button agree_req;
    @ViewInject(R.id.move_to_black_list)
    private Button move_to_black_list;
    private Friend_Reqs_List.RootBean bean;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_reqs_detail);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        rl_back_req.setOnClickListener(this);
        agree_req.setOnClickListener(this);
        move_to_black_list.setOnClickListener(this);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        bean = (Friend_Reqs_List.RootBean) getIntent().getBundleExtra("info").get("FriendReqInfo");
        initView();
    }

    private void initView() {
        StringBuilder builder = new StringBuilder();
        req_name.setText(bean.getUserName());
        Glide.with(this)
                .load(MyConfig.PIC_AVATAR + bean.getUserPhotoThums())
                .error(R.drawable.default_useravatar)
                .placeholder(R.drawable.default_useravatar)
                .into(req_avatar);
        for (int i = 0; i < bean.getMsg().size(); i++) {
            builder.append(bean.getMsg().get(i) + "\n");

        }
        tv_message.setText(builder.toString());
        if (bean.getStatus().equals("2")) {
            agree_req.setText("已同意");
            agree_req.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back_req:
                this.finish();
                break;
            case R.id.agree_req:
                if (!bean.getStatus().equals("2")) {
                    LogUtils.d("onResponse", ">>>>");
                    updateReqFriendStatus(bean.getUid(), uid);
                }
                break;
            case R.id.move_to_black_list:
                moveToBlackList(bean.getUid(), uid);
                break;
        }

    }

    private void updateReqFriendStatus(final String to_uid, final String uid) {
        LogUtils.d("onResponse", ">>>LLLL>");
        StringRequest agreeRequest = new StringRequest(Request.Method.POST, MyConfig.UPDATE_REQU_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d("onResponse", response.toString());
                FriendDao friendDao = new FriendDao(FriendReqsDetailActivity.this);
                Friend friend = new Friend();
                friend.setUid(bean.getUid());
                friend.setNickname(bean.getUserName());
                friend.setAvatar(MyConfig.PIC_AVATAR + bean.getUserPhotoThums());
                friendDao.addFriend(friend);
                ToastUtil.showToast(FriendReqsDetailActivity.this, "已同意");
                setResult(10001);
                FriendReqsDetailActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("onResponse", "fffff" + error.toString());
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", to_uid);
                map.put("to_uid", uid);
                map.put("status", "2");
                return map;
            }
        };
        MyApplication.getRequestQueue().add(agreeRequest);
    }

    private void moveToBlackList(final String black_uid, final String uid) {
        StringRequest agreeRequest = new StringRequest(Request.Method.POST, MyConfig.MOVE_TO_BLACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToastUtil.showToast(FriendReqsDetailActivity.this, "拉黑成功");
                setResult(10002);
                FriendReqsDetailActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(FriendReqsDetailActivity.this, "操作失败，稍后再试");
                Log.e("TAG", error.getMessage(), error);
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
        MyApplication.getRequestQueue().add(agreeRequest);
    }
}
