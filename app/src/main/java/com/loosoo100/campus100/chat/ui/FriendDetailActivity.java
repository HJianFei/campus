package com.loosoo100.campus100.chat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CompoundButton;
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
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.User_Info;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.OperationRong;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.chat.widget.switchbutton.SwitchButton;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class FriendDetailActivity extends FragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_friend)
    private RelativeLayout rl_back_friend;
    @ViewInject(R.id.group_info)
    private LinearLayout group_info;
    private SwitchButton messageTop, messageNotification;
    private CircleView mImageView;
    private TextView friendName;
    private String cuid = "";
    private String fromConversationId;
    private User_Info user = new User_Info();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1001) {
                user = (User_Info) msg.obj;
                friendName.setText(user.getUserName());
                Glide.with(FriendDetailActivity.this)
                        .load(MyConfig.PIC_AVATAR + user.getUserPhotoThums())
                        .error(R.drawable.default_useravatar)
                        .into(mImageView);
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        fromConversationId = getIntent().getStringExtra("targetId");
        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");

        initView();
        initData();
    }

    private void initData() {
        GsonRequest<User_Info> gsonRequest = new GsonRequest<User_Info>(MyConfig.GET_USER_INFO + "?userId=" + fromConversationId, User_Info.class, new Response.Listener<User_Info>() {
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
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.PRIVATE, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }

                    if (conversation.isTop()) {
                        messageTop.setChecked(true);
                    } else {
                        messageTop.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE, fromConversationId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                    if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                        messageNotification.setChecked(true);
                    } else {
                        messageNotification.setChecked(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }

    }

    private void initView() {
        LinearLayout cleanMessage = (LinearLayout) findViewById(R.id.clean_friend);
        mImageView = (CircleView) findViewById(R.id.friend_header);
        messageTop = (SwitchButton) findViewById(R.id.sw_freind_top);
        messageNotification = (SwitchButton) findViewById(R.id.sw_friend_notfaction);
        friendName = (TextView) findViewById(R.id.friend_name);
        cleanMessage.setOnClickListener(this);
        messageNotification.setOnCheckedChangeListener(this);
        messageTop.setOnCheckedChangeListener(this);
        rl_back_friend.setOnClickListener(this);
        group_info.setOnClickListener(this);
        if (!cuid.equals("") || fromConversationId.startsWith("cmp")) {
            group_info.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back_friend:
                this.finish();
                break;
            case R.id.group_info:
                Intent intent = new Intent(FriendDetailActivity.this, CampusContactsFriendActivity.class);
                intent.putExtra("muid", fromConversationId);
                startActivity(intent);
                break;
            case R.id.clean_friend:
                CustomDialog.Builder builder = new CustomDialog.Builder(FriendDetailActivity.this);
                builder.setMessage("是否清除会话聊天记录？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (RongIM.getInstance() != null) {

                            RongIM.getInstance().clearMessages(Conversation.ConversationType.PRIVATE, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    ToastUtil.showToast(FriendDetailActivity.this, "清除成功");
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    ToastUtil.showToast(FriendDetailActivity.this, "清除失败，稍后再试");
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();
                break;

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_friend_notfaction:
                if (isChecked) {

                    OperationRong.setConverstionNotif(FriendDetailActivity.this, Conversation.ConversationType.PRIVATE, fromConversationId, true);

                } else {

                    OperationRong.setConverstionNotif(FriendDetailActivity.this, Conversation.ConversationType.PRIVATE, fromConversationId, false);

                }
                break;
            case R.id.sw_freind_top:
                if (isChecked) {

                    OperationRong.setConversationTop(FriendDetailActivity.this, Conversation.ConversationType.PRIVATE, fromConversationId, true);

                } else {

                    OperationRong.setConversationTop(FriendDetailActivity.this, Conversation.ConversationType.PRIVATE, fromConversationId, false);

                }
                break;
        }

    }
}
