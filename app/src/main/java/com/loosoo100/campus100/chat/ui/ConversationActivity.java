package com.loosoo100.campus100.chat.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.Company_Info;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.FriendInfo;
import com.loosoo100.campus100.chat.bean.Group;
import com.loosoo100.campus100.chat.bean.Group_Info;
import com.loosoo100.campus100.chat.bean.User_Info;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.Tool;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.zzboss.chat.bean.Company_Friend;
import com.loosoo100.campus100.zzboss.chat.bean.FollowInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static com.loosoo100.campus100.config.MyConfig.GET_USER_INFO;


public class ConversationActivity extends FragmentActivity implements RongIM.LocationProvider, View.OnClickListener, RongIM.GroupInfoProvider, RongIM.UserInfoProvider {

    @ViewInject(R.id.rl_back_rl)
    private RelativeLayout rl_back_rl;
    @ViewInject(R.id.tv_nickname)
    private TextView tv_nickname;
    @ViewInject(R.id.iv_type)
    private ImageView iv_type;
    private String uid;
    private String cuid;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private String targetId;
    private String nickname;
    private List<Friend> friendLists = new ArrayList<>();
    private List<Group.GroupBean> groupBean = new ArrayList<>();
    private FriendDao friendDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ViewUtils.inject(this);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
        friendDao = new FriendDao(this);
        rl_back_rl.setOnClickListener(this);
        iv_type.setOnClickListener(this);
        targetId = getIntent().getData().getQueryParameter("targetId");
        nickname = getIntent().getData().getQueryParameter("title");
        if (!TextUtils.isEmpty(nickname)) {
            tv_nickname.setText(nickname);
        } else {
            tv_nickname.setText(targetId);

        }
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));
        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
            iv_type.setImageDrawable(getResources().getDrawable(R.drawable.icon2_menu));
        } else if (mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
            iv_type.setImageDrawable(getResources().getDrawable(R.drawable.icon1_menu));
        } else {
            iv_type.setVisibility(View.GONE);
            iv_type.setClickable(false);
        }
        if (!uid.equals("") && cuid.equals("")) {
            getFriend(uid);
            getGroup(uid);
        }
        if (uid.equals("") && !cuid.equals("")) {
            getConcernInfo(cuid);
        }
        RongIM.setUserInfoProvider(this, true);
        RongIM.setGroupInfoProvider(this, true);
        RongIM.setLocationProvider(this);


    }


    private void getConcernInfo(String cuid) {
        Friend friend = new Friend();
        friend.setUid((String) SharedPreferencesUtils.getParam(this, "companyID", ""));
        friend.setNickname((String) SharedPreferencesUtils.getParam(this, "company_name", ""));
        friend.setAvatar((String) SharedPreferencesUtils.getParam(this, "company_avatar", ""));
        friendLists.add(friend);
        GsonRequest<FollowInfo> gsonRequest = new GsonRequest<>(MyConfig.GET_FOLLOWS + "?cmpId=" + cuid, FollowInfo.class, new Response.Listener<FollowInfo>() {
            @Override
            public void onResponse(FollowInfo followInfo) {
                List<FollowInfo.RootBean> rootBeen = followInfo.getRoot();
                Friend fr;
                if (null != rootBeen) {
                    for (FollowInfo.RootBean f : rootBeen) {
                        fr = new Friend();
                        fr.setUid(f.getUid());
                        fr.setNickname(f.getUserName());
                        fr.setAvatar(f.getUserPhotoThums());
                        friendLists.add(fr);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void getGroup(String uid) {
        Friend friendw = friendDao.queryWhereById(uid);
        friendLists.add(friendw);
        Group.GroupBean grouBean = new Group.GroupBean();
        grouBean.setId(friendw.getUid());
        grouBean.setName(friendw.getNickname());
        grouBean.setAvatar(MyConfig.PIC_AVATAR + friendw.getAvatar());
        groupBean.add(grouBean);

        GsonRequest<Group> gsonRequest = new GsonRequest<>(MyConfig.GET_PERSON_GROUP + "?uid=" + uid, Group.class, new Response.Listener<Group>() {
            @Override
            public void onResponse(Group group) {
                if (null != group.getRoot()) {
                    groupBean = group.getRoot();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void getFriend(String uid) {
        GsonRequest<FriendInfo> gsonRequest = new GsonRequest<>(MyConfig.MY_FRIEND_LIST + "?uid=" + uid, FriendInfo.class, new Response.Listener<FriendInfo>() {
            @Override
            public void onResponse(FriendInfo friendInfo) {
                List<FriendInfo.RootBean> rootBeen = friendInfo.getRoot();
                Friend friend;
                if (null != rootBeen) {
                    for (FriendInfo.RootBean f : rootBeen) {
                        friend = new Friend();
                        friend.setUid(f.getUid());
                        friend.setNickname(f.getUserName());
                        friend.setAvatar(MyConfig.PIC_AVATAR + f.getUserPhotoThums());
                        friendLists.add(friend);
                    }

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
            case R.id.rl_back_rl:
                this.finish();
                break;
            case R.id.iv_type:

                Intent intent = null;
                if (mConversationType == Conversation.ConversationType.GROUP) {
                    intent = new Intent(this, GroupDetailActivity.class);
                } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
                    intent = new Intent(this, FriendDetailActivity.class);
                }
                intent.putExtra("targetId", targetId);
                intent.putExtra("name", nickname);
                startActivityForResult(intent, 101);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 102) {
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public UserInfo getUserInfo(String s) {
        LogUtils.d("onResponse", "新来的：" + s);
        if (null != friendLists) {
            LogUtils.d("onResponse", "群成员不为空");
            for (Friend f : friendLists) {

                if (s.equals(f.getUid()) || s.equals(("cmp" + f.getUid()))) {
                    LogUtils.d("onResponse", "在列表中");
                    return new UserInfo(f.getUid(), f.getNickname(), Uri.parse(f.getAvatar()));

                } else if (!uid.equals("") && cuid.equals("")) {//学生

                    LogUtils.d("onResponse", "不在列表中");

                    if (!s.startsWith("cmp")) {
                        LogUtils.d("onResponse", "不在列表中，获取学生头像信息");
                        //学生与学生聊（群组头像，不是好友的状态）
                        getGroupMemberInfo(s);
                    } else {
                        LogUtils.d("onResponse", "不在列表中，获取企业头像信息");
                        //学生端获取企业的头像，名称等信息
                        getCompanyInfo(s);
                    }

                } else if (uid.equals("") && !cuid.equals("")) {
                    LogUtils.d("onResponse", "不在列表中，企业获取学生头像信息");
                    getCompanyMember(s);
                }
            }
        }
        return null;
    }

    private void getCompanyInfo(String s) {
        GsonRequest<Company_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_COMPANY_INFO + "?companyId=" + s.substring(3, s.length()), Company_Info.class, new Response.Listener<Company_Info>() {
            @Override
            public void onResponse(Company_Info company_info) {
                Friend friend = new Friend();
                friend.setUid("cmp" + company_info.getCompany_id());
                friend.setNickname(company_info.getCompany_name());
                friend.setAvatar(MyConfig.PIC_AVATAR + company_info.getCompany_logo());
                friendLists.add(friend);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo("cmp" + company_info.getCompany_id(), company_info.getCompany_name(), Uri.parse(MyConfig.PIC_AVATAR + company_info.getCompany_logo())));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void getCompanyMember(String s) {
        GsonRequest<Company_Friend> gsonRequest = new GsonRequest<>(MyConfig.GET_COMPANY_MEMBER + "?uid=" + s, Company_Friend.class, new Response.Listener<Company_Friend>() {
            @Override
            public void onResponse(Company_Friend company_friend) {

                Friend friend = new Friend();
                friend.setUid(company_friend.getUserId());
                friend.setNickname(company_friend.getUserName());
                friend.setAvatar(company_friend.getUserPhotoThums());
                friendLists.add(friend);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(company_friend.getUserId(), company_friend.getUserName(), Uri.parse(company_friend.getUserPhotoThums())));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);


    }

    private void getGroupMemberInfo(String s) {
        GsonRequest<User_Info> gsonRequest = new GsonRequest<>(GET_USER_INFO + "?userId=" + s, User_Info.class, new Response.Listener<User_Info>() {
            @Override
            public void onResponse(User_Info user_info) {
                Friend friend = new Friend();
                friend.setUid(user_info.getUserId());
                friend.setNickname(user_info.getUserName());
                friend.setAvatar(MyConfig.PIC_AVATAR + user_info.getUserPhotoThums());
                friendLists.add(friend);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(user_info.getUserId(), user_info.getUserName(), Uri.parse(MyConfig.PIC_AVATAR + user_info.getUserPhotoThums())));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    @Override
    public io.rong.imlib.model.Group getGroupInfo(String s) {
        if (null != groupBean) {
            for (Group.GroupBean g : groupBean) {
                if (g.getId().equals(s)) {
                    return new io.rong.imlib.model.Group(g.getId(), g.getName(), Uri.parse(MyConfig.PIC_AVATAR + g.getAvatar()));
                } else {
                    getGroup_Info(s);
                }
            }
        }
        return null;
    }

    private void getGroup_Info(String gid) {
        GsonRequest<Group_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_GROUP_INFO + "?id=" + gid, Group_Info.class, new Response.Listener<Group_Info>() {
            @Override
            public void onResponse(Group_Info group_info) {
                Group.GroupBean bean = new Group.GroupBean();
                bean.setId(group_info.getId());
                bean.setName(group_info.getName());
                bean.setAvatar(MyConfig.PIC_AVATAR + group_info.getAvatar());
                groupBean.add(bean);
                RongIM.getInstance().refreshGroupInfoCache(new io.rong.imlib.model.Group(group_info.getId(), group_info.getName(), Uri.parse(MyConfig.PIC_AVATAR + group_info.getAvatar())));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }


    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        Tool.mLastLocationCallback = locationCallback;
        Intent intent = new Intent(context, MapLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
