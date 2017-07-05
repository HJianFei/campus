package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.FriendInfo;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.chat.sortlistview.PinyinComparator;
import com.loosoo100.campus100.chat.sortlistview.SideBar;
import com.loosoo100.campus100.chat.sortlistview.SortAdapter;
import com.loosoo100.campus100.chat.sortlistview.SortFriend;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.tools.CharacterParser;

/**
 * @author yang 我的好友列表（礼物盒子领取）activity
 */
public class MyFriendListActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty;
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.sideBar)
    private SideBar sideBar;
    @ViewInject(R.id.sortListView)
    private ListView sortListView;
    @ViewInject(R.id.dialog)
    private TextView dialog;

    private String uid = "";
    private SortAdapter adapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortFriend> SourceDateList;
    private List<Friend> friendList = new ArrayList<>();
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private FriendDao friendDao;
    private List<SortFriend> mSortList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriend_list);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");

        rl_back.setOnClickListener(this);

        friendDao = new FriendDao(this);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initData();
        initViews();
    }

    private void initData() {
        GsonRequest<FriendInfo> gsonRequest = new GsonRequest<FriendInfo>(MyConfig.MY_FRIEND_LIST + "?uid=" + uid, FriendInfo.class, new Response.Listener<FriendInfo>() {
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
                        friendList.add(friend);
                    }

                }
                SourceDateList.clear();
                SourceDateList = filledData(friendList);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapter(MyFriendListActivity.this, SourceDateList);
                sortListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }


    private void initViews() {
        SourceDateList = filledData(friendList);
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(MyFriendListActivity.this);
                builderDel.setMessage("是否确认赠送给ta");
                builderDel.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("uid",SourceDateList.get(position).getUid());
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
            }
        });

    }

    /**
     * 为ListView填充数据
     *
     * @param friends
     * @rn
     */
    private List<SortFriend> filledData(List<Friend> friends) {
        mSortList = new ArrayList<>();
        mSortList.clear();
        for (int i = 0; i < friends.size(); i++) {
            SortFriend sortFriend = new SortFriend();
            sortFriend.setId(friendList.get(i).getId());
            sortFriend.setUid(friendList.get(i).getUid());
            sortFriend.setNickname(friends.get(i).getNickname());
            sortFriend.setAvatar(friendList.get(i).getAvatar());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(friends.get(i).getNickname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortFriend.setLetters(sortString.toUpperCase());
            } else {
                sortFriend.setLetters("#");
            }

            mSortList.add(sortFriend);
        }
        return mSortList;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }

}
