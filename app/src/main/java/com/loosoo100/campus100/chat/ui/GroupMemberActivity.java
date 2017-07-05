package com.loosoo100.campus100.chat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.bean.User_Info;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;

public class GroupMemberActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_group_member)
    private RelativeLayout rl_back_group_member;
    @ViewInject(R.id.group_member_info)
    private LinearLayout group_member_info;
    @ViewInject(R.id.btn_manager)
    private Button btn_manager;
    @ViewInject(R.id.group_member_header)
    private CircleView group_member_header;
    @ViewInject(R.id.group_member_name)
    private TextView group_member_name;
    private String gid = "";
    private String group_member_id = "";
    private String uid = "";
    private String isGrant = "";

    private User_Info user = new User_Info();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1001) {
                user = (User_Info) msg.obj;
                group_member_name.setText(user.getUserName());
                Glide.with(GroupMemberActivity.this)
                        .load(MyConfig.PIC_AVATAR + user.getUserPhotoThums())
                        .error(R.drawable.default_useravatar)
                        .into(group_member_header);
            } else if (msg.arg1 == 10010) {
                btn_manager.setText("取消管理员权限");

            } else if (msg.arg1 == 10011) {
                btn_manager.setText("设置为群管理员");

            }
            super.handleMessage(msg);
        }
    };
    private String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        gid = getIntent().getStringExtra("gid");
        group_member_id = getIntent().getStringExtra("group_member_id");
        group_id = getIntent().getStringExtra("group_id");
        isGrant = getIntent().getStringExtra("isGrant");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        initView();
        initData();
    }

    private void initData() {
        GsonRequest<User_Info> gsonRequest = new GsonRequest<User_Info>(MyConfig.GET_USER_INFO + "?userId=" + group_member_id, User_Info.class, new Response.Listener<User_Info>() {
            @Override
            public void onResponse(User_Info user_info) {
                Message message = Message.obtain();
                message.obj = user_info;
                message.arg1 = 1001;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void initView() {
        if (uid.equals(group_id) && !uid.equals(group_member_id)) {
            btn_manager.setVisibility(View.VISIBLE);
        }
        if (isGrant.equals("2")) {
            btn_manager.setText("取消管理员权限");
        }
        btn_manager.setOnClickListener(this);
        rl_back_group_member.setOnClickListener(this);
        group_member_info.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_manager:
                if (btn_manager.getText().toString().equals("设置为群管理员")) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(GroupMemberActivity.this);
                    builder.setMessage("设置Ta为管理员？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            grantGroupMember();
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else if (btn_manager.getText().toString().equals("取消管理员权限")) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(GroupMemberActivity.this);
                    builder.setMessage("取消Ta的管理员权限？");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resetGrantGroupMember();
                        }
                    });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                }
                break;
            case R.id.rl_back_group_member:
                this.finish();
                break;
            case R.id.group_member_info:
                Intent intent;
                if (uid.equals(group_member_id)) {

                    intent = new Intent(GroupMemberActivity.this, CampusContactsPersonalActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(GroupMemberActivity.this, CampusContactsFriendActivity.class);
                    intent.putExtra("muid", group_member_id);
                    startActivity(intent);
                }

                break;
        }

    }

    private void resetGrantGroupMember() {
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<>(MyConfig.RESET_GRANT_GROUP_MEMBER + "?gid=" + gid + "&uid=" + group_member_id, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                if (requestCode_1.getCode() == 200) {
                    Message message = Message.obtain();
                    message.arg1 = 10011;
                    handler.sendMessage(message);
                    ToastUtil.showToast(GroupMemberActivity.this, "取消权限成功");
                } else {
                    ToastUtil.showToast(GroupMemberActivity.this, "操作失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast(GroupMemberActivity.this, "操作失败，稍后再试");

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void grantGroupMember() {
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<>(MyConfig.GRANT_GROUP_MEMS + "?gid=" + gid + "&uid=" + group_member_id, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                if (requestCode_1.getCode() == 200) {
                    Message message = Message.obtain();
                    message.arg1 = 10010;
                    handler.sendMessage(message);
                    ToastUtil.showToast(GroupMemberActivity.this, "设置成功");
                } else {
                    ToastUtil.showToast(GroupMemberActivity.this, "操作失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast(GroupMemberActivity.this, "操作失败，稍后再试");

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);

    }
}
