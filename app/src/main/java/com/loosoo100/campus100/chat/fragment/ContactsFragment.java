package com.loosoo100.campus100.chat.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventAddFriendNoRead;
import com.loosoo100.campus100.anyevent.MEventMessageNoRead;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.FriendInfo;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.chat.sortlistview.ClearEditText;
import com.loosoo100.campus100.chat.sortlistview.PinyinComparator;
import com.loosoo100.campus100.chat.sortlistview.SideBar;
import com.loosoo100.campus100.chat.sortlistview.SortAdapter;
import com.loosoo100.campus100.chat.sortlistview.SortFriend;
import com.loosoo100.campus100.chat.ui.GroupListActivity;
import com.loosoo100.campus100.chat.ui.NewFriendListActivity;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.view.CustomDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.tools.CharacterParser;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 好友列表
 */
public class ContactsFragment extends Fragment implements View.OnClickListener, RongIM.UserInfoProvider {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String uid = "";
    private String mParam1;
    private String mParam2;
    private ListView sortListView;
    private ClearEditText mClearEditText;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private View mHeadView;
    private TextView tv_unread;
    private int index;
    private View view;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortFriend> SourceDateList;
    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> friendLists = new ArrayList<>();
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private AlertDialog alertDialog;
    private FriendDao friendDao;
    private List<SortFriend> mSortList;

    private int status = 1;  //新的好友未读状态0未读 1已读

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (status == 0) {
                tv_unread.setVisibility(View.VISIBLE);
            } else {
                tv_unread.setVisibility(View.GONE);
            }
        }
    };

    public ContactsFragment() {

    }


    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        uid = getActivity().getSharedPreferences(UserInfoDB.USERTABLE, getActivity().MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        mHeadView = view.inflate(getActivity(), R.layout.fragment_contacts_list_head, null);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
        tv_unread = (TextView) mHeadView.findViewById(R.id.tv_unread);
        newFriendsLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        sortListView.addHeaderView(mHeadView);
        friendDao = new FriendDao(getActivity());
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        RongIM.setUserInfoProvider(this, true);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        new Thread() {
            public void run() {
                status = GetData.getFriendNoRead(MyConfig.GET_FRIEND_NO_READ + "?to_uid=" + uid);
                if (!getActivity().isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
        initData();
        super.onResume();
    }

    private void initData() {
        friendList.clear();
        friendLists.clear();
        Friend friendw = friendDao.queryWhereById(uid);
        friendLists.add(friendw);
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
                        friendLists.add(friend);
                    }

                }
                SourceDateList.clear();
                SourceDateList = filledData(friendList);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapter(getActivity(), SourceDateList);
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


    private void initViews(View view) {
        SourceDateList = filledData(friendList);
        mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
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
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //启动会话界面
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(getActivity(), SourceDateList.get(position - 1).getUid(), SourceDateList.get(position - 1).getNickname());
                }
            }
        });

        sortListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                showAlertDialog();

                return true;
            }
        });

    }

    private void showAlertDialog() {
        View layout = View.inflate(getActivity(), R.layout.content_dialog, null);
        layout.findViewById(R.id._delete_friend).setOnClickListener(this);
        layout.findViewById(R.id._black_list).setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(getActivity()).setView(layout).setInverseBackgroundForced(true).create();
        alertDialog.show();
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

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortFriend> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortFriend sortModel : SourceDateList) {
                String name = sortModel.getNickname();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        if (adapter != null) {
            adapter.updateListView(filterDateList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_chatroom:
                Intent intent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);
                break;
            case R.id.re_newfriends:
                Intent intent1 = new Intent(getActivity(), NewFriendListActivity.class);
                startActivity(intent1);
                break;
            case R.id._black_list:
                if (null != alertDialog) {
                    alertDialog.dismiss();
                }
                CustomDialog.Builder builderDel = new CustomDialog.Builder(getActivity());
                builderDel.setMessage("是否确认拉黑？");
                builderDel.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveToBlackList(index);
                    }
                });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                break;
            case R.id._delete_friend:
                if (null != alertDialog) {
                    alertDialog.dismiss();
                }
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("是否确认删除？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFriend(index);
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();

                break;
        }

    }


    private void moveToBlackList(final int index) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.MOVE_TO_BLACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RequestCode_1 requestCode_1 = gson.fromJson(response.toString(), RequestCode_1.class);
                if (requestCode_1.getCode() == 200) {
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, SourceDateList.get(index - 1).getUid());
                    ToastUtil.showToast(getActivity(), "成功移到黑名单");
                    SourceDateList.remove(index - 1);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showToast(getActivity(), "移到黑名单失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("TAG", error.getMessage());
                ToastUtil.showToast(getActivity(), "移到黑名单失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("black_uid", SourceDateList.get(index - 1).getUid());
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    private void deleteFriend(final int index) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.DELETE_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RequestCode_1 requestCode_1 = gson.fromJson(response.toString(), RequestCode_1.class);
                if (requestCode_1.getCode() == 200) {
                    ToastUtil.showToast(getActivity(), "删除成功");
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, SourceDateList.get(index - 1).getUid());
                    SourceDateList.remove(index - 1);
                    adapter.notifyDataSetChanged();

                } else {
                    ToastUtil.showToast(getActivity(), "删除失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("TAG", error.getMessage());
                ToastUtil.showToast(getActivity(), "删除失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("del_uid", SourceDateList.get(index - 1).getUid());
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    @Override
    public UserInfo getUserInfo(String s) {
        if (null != friendList) {
            for (Friend f : friendLists) {
                if (f.getUid().equals(s)) {
                    return new UserInfo(f.getUid(), f.getNickname(), Uri.parse(f.getAvatar()));
                }
            }
        }
        return null;
    }

    public void onEventMainThread(MEventAddFriendNoRead event) {
        if (event.isChange()) {
            new Thread() {
                public void run() {
                    status = GetData.getFriendNoRead(MyConfig.GET_FRIEND_NO_READ + "?to_uid=" + uid);
                    if (!getActivity().isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }
            }.start();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
