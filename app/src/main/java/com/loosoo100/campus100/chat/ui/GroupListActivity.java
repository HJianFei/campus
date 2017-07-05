package com.loosoo100.campus100.chat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.GroupAdapter;
import com.loosoo100.campus100.chat.bean.Group;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class GroupListActivity extends FragmentActivity implements View.OnClickListener, RongIM.GroupInfoProvider {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_group)
    private View rl_back;
    @ViewInject(R.id.group_listview)
    private ListView mGroupListView;
    @ViewInject(R.id.add_group)
    private ImageView add_group;
    private GroupAdapter adapter;
    private String uid = "";
    private int index = 0;
    private List<Group.GroupBean> groupBean = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1001) {
                groupBean = (List<Group.GroupBean>) msg.obj;
                initView();
            } else if (msg.arg1 == 1002) {
                groupBean.clear();
                initView();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        rl_back.setOnClickListener(this);
        add_group.setOnClickListener(this);
        RongIM.setGroupInfoProvider(this, true);
        initView();

    }

    @Override
    protected void onResume() {
        initData(uid);
        super.onResume();
    }

    private void initView() {
        adapter = new GroupAdapter(this, groupBean);
        mGroupListView.setAdapter(adapter);
        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                //启动融云群聊
                RongIM.getInstance().startGroupChat(GroupListActivity.this, groupBean.get(position).getId(), groupBean.get(position).getName());
            }
        });
    }

    private void initData(String uid) {
        GsonRequest<Group> gsonRequest = new GsonRequest<>(MyConfig.GET_PERSON_GROUP + "?uid=" + uid, Group.class, new Response.Listener<Group>() {
            @Override
            public void onResponse(Group group) {
                Message message = Message.obtain();
                if (null != group.getRoot()) {
                    message.obj = group.getRoot();
                    message.arg1 = 1001;
                    handler.sendMessage(message);
                } else {
                    message.arg1 = 1002;
                    handler.sendMessage(message);
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
            case R.id.rl_back_group:
                this.finish();
                break;
            case R.id.add_group:
                Intent intent = new Intent(GroupListActivity.this, CreateGroupActivity.class);
                startActivity(intent);
                break;
        }

    }


    @Override
    public io.rong.imlib.model.Group getGroupInfo(String s) {
        if (null != groupBean) {
            for (Group.GroupBean g : groupBean) {
                if (g.getId().equals(s)) {
                    return new io.rong.imlib.model.Group(g.getId(), g.getName(), Uri.parse(MyConfig.PIC_AVATAR + g.getAvatar()));
                }
            }
        }
        return null;
    }
}
