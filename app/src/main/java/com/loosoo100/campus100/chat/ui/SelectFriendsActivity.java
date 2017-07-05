package com.loosoo100.campus100.chat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.FriendInfo;
import com.loosoo100.campus100.chat.bean.Groupnumber;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.sortlistview.PinyinComparator;
import com.loosoo100.campus100.chat.sortlistview.SideBar;
import com.loosoo100.campus100.chat.sortlistview.SortFriend;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.chat.widget.SelectableRoundedImageView;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.tools.CharacterParser;

public class SelectFriendsActivity extends FragmentActivity implements View.OnClickListener {

    private ListView selectFriendListView;
    private SideBar sideBar;
    private TextView dialog;
    private SelectFriendAdapter adapter;
    private String uid = "";
    private List<Groupnumber.RootBean> rootBeanFriend = new ArrayList<>();
    private List<Groupnumber.RootBean> rootBeanFriendDelete = new ArrayList<>();
    //用于存储CheckBox选中状态
    public Map<Integer, Boolean> mCBFlag;
    private ArrayList<CheckBox> checkBoxList = new ArrayList<>();
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortFriend> SourceDateList = new ArrayList<>();
    private List<Friend> friendList = new ArrayList<>();
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private TextView btn_ok;
    private RelativeLayout rl_back_select;
    private String gid = "";
    private String[] uids;
    private String type;
    private TextView no_friend;
    private TextView no_friend_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friends);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        rootBeanFriend = (List<Groupnumber.RootBean>) getIntent().getSerializableExtra("AddGroupMember");
        rootBeanFriendDelete = (List<Groupnumber.RootBean>) getIntent().getSerializableExtra("DeleteGroupMember");
        gid = getIntent().getStringExtra("gid");
        type = getIntent().getStringExtra("type");
        selectFriendListView = (ListView) findViewById(R.id.country_lvcountry);
        no_friend = (TextView) findViewById(R.id.no_friend);
        no_friend_del = (TextView) findViewById(R.id.no_friend_del);
        btn_ok = (TextView) findViewById(R.id.btn_ok);
        rl_back_select = (RelativeLayout) findViewById(R.id.rl_back_select);
        btn_ok.setOnClickListener(this);
        rl_back_select.setOnClickListener(this);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initView();

        if (type.equals("add")) {
            initData();
        } else {
            initDeleteData();

        }
    }

    private void initDeleteData() {
        friendList.clear();
        Friend friend;
        for (Groupnumber.RootBean f : rootBeanFriendDelete) {
            if (!f.getUserId().equals(uid)) {
                friend = new Friend();
                friend.setUid(f.getUserId());
                friend.setNickname(f.getUserName());
                friend.setAvatar(MyConfig.PIC_AVATAR + f.getUserPhotoThums());
                friendList.add(friend);
            }
        }
        if (friendList.size() == 0) {
            no_friend_del.setVisibility(View.VISIBLE);
        }
        SourceDateList.clear();
        SourceDateList = filledData(friendList);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SelectFriendAdapter(SelectFriendsActivity.this, SourceDateList, rootBeanFriend);
        selectFriendListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void initData() {
        friendList.clear();
        GsonRequest<FriendInfo> gsonRequest = new GsonRequest<FriendInfo>(MyConfig.MY_AVAILABLE_FRIEND + "?uid=" + uid + "&gid=" + gid, FriendInfo.class, new Response.Listener<FriendInfo>() {
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
                if (friendList.size() == 0) {
                    no_friend.setVisibility(View.VISIBLE);
                }
                SourceDateList.clear();
                SourceDateList = filledData(friendList);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SelectFriendAdapter(SelectFriendsActivity.this, SourceDateList, rootBeanFriend);
                selectFriendListView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void initView() {
        SourceDateList = filledData(friendList);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    selectFriendListView.setSelection(position);
                }

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
        List<SortFriend> mSortList = new ArrayList<>();

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
            case R.id.btn_ok:
                btn_ok.setClickable(false);
                if (mCBFlag != null && SourceDateList != null && SourceDateList.size() > 0) {
                    uids = new String[SourceDateList.size()];
                    for (int i = 0; i < SourceDateList.size(); i++) {
                        if (mCBFlag.get(i)) {
                            uids[i] = SourceDateList.get(i).getUid();
                        }
                    }
                    if (uids.length <= 0) {
                        ToastUtil.showToast(SelectFriendsActivity.this, "至少选择一位好友");
                    } else {
                        if (type.equals("add")) {
                            addGroupMembers(gid, uids);
                        } else {
                            removeFriend(gid, uids);

                        }
                    }
                }


                break;
            case R.id.rl_back_select:
                this.finish();

                break;
        }

    }

    private void removeFriend(final String gid, final String[] uids) {
        final List<String> lists = new ArrayList<>();
        for (int i = 0; i < uids.length; i++) {
            if (null != uids[i]) {
                lists.add(uids[i]);
            }
        }
        LogUtils.d("onResponse", Arrays.toString(uids));
        LogUtils.d("onResponse", lists.toString());
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<RequestCode_1>(Request.Method.POST, MyConfig.QUIET_GROUP, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                LogUtils.d("onResponse", requestCode_1.getCode() + "");
                if (requestCode_1.getCode() == 200) {
                    SelectFriendsActivity.this.finish();
                } else {
                    ToastUtil.showToast(SelectFriendsActivity.this, "操作失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.d("onResponse", volleyError.toString());
                ToastUtil.showToast(SelectFriendsActivity.this, "操作失败，稍后再试");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("gid", gid);
                map.put("uid", lists.toString());
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);


    }

    private void addGroupMembers(final String gid, final String[] uids) {
        final List<String> lists = new ArrayList<>();
        for (int i = 0; i < uids.length; i++) {
            if (null != uids[i]) {
                lists.add(uids[i]);
            }
        }
        LogUtils.d("onResponse", Arrays.toString(uids));
        LogUtils.d("onResponse", lists.toString());
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<RequestCode_1>(Request.Method.POST, MyConfig.ADD_GROUP_MEMBERS, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                LogUtils.d("onResponse", requestCode_1.getCode() + "");
                if (requestCode_1.getCode() == 200) {
                    SelectFriendsActivity.this.finish();
                } else {
                    ToastUtil.showToast(SelectFriendsActivity.this, "操作失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.d("onResponse", volleyError.toString());

                ToastUtil.showToast(SelectFriendsActivity.this, "操作失败，稍后再试");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("gid", gid);
                map.put("uid", lists.toString());
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    /**
     * 内部类
     */
    class SelectFriendAdapter extends BaseAdapter implements SectionIndexer {
        private List<SortFriend> list = null;
        private List<Groupnumber.RootBean> rootBeanList = null;
        private Context mContext;

        public SelectFriendAdapter(Context mContext, List<SortFriend> list, List<Groupnumber.RootBean> rootBeanList) {
            this.mContext = mContext;
            this.list = list;
            this.rootBeanList = rootBeanList;
            mCBFlag = new HashMap<>();
            initCk();
        }

        private void initCk() {
            for (int i = 0; i < list.size(); i++) {
                mCBFlag.put(i, false);
            }
        }

        /**
         * 当ListView数据发生变化时,调用此方法来更新ListView
         *
         * @param list
         */
        public void updateListView(List<SortFriend> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup arg2) {
            final ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_select_friend, null);
                viewHolder.tvTitle = (TextView) view.findViewById(R.id.friendname);
                viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
                viewHolder.iv_avatar = (SelectableRoundedImageView) view.findViewById(R.id.frienduri);
                viewHolder.isSelect = (CheckBox) view.findViewById(R.id.dis_select);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            SortFriend sortFriend = list.get(position);

            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(sortFriend.getLetters());
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
            }

            viewHolder.tvTitle.setText(list.get(position).getNickname());
            Glide.with(MyApplication.getInstance())
                    .load(list.get(position).getAvatar())
                    .into(viewHolder.iv_avatar);
            viewHolder.isSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    if (cb != null) {
                        if (cb.isChecked()) {
//                            for (CheckBox c : checkBoxList) {
//                                c.setChecked(false);
//                            }
//                            checkBoxList.clear();
                            checkBoxList.add(cb);
                        } else {
                            checkBoxList.clear();
                        }
                    }
                }
            });
            viewHolder.isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCBFlag.put(position, viewHolder.isSelect.isChecked());
                }
            });
            viewHolder.isSelect.setChecked(mCBFlag.get(position));

            return view;

        }


        final class ViewHolder {
            TextView tvLetter;
            TextView tvTitle;
            SelectableRoundedImageView iv_avatar;
            CheckBox isSelect;
        }


        /**
         * 根据ListView的当前位置获取分类的首字母的Char ascii值
         */
        public int getSectionForPosition(int position) {
            return list.get(position).getLetters().charAt(0);
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        public int getPositionForSection(int section) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }

            return -1;
        }

        /**
         * 提取英文的首字母，非英文字母用#代替。
         *
         * @param str
         * @return
         */
        private String getAlpha(String str) {
            String sortStr = str.trim().substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortStr.matches("[A-Z]")) {
                return sortStr;
            } else {
                return "#";
            }
        }

        @Override
        public Object[] getSections() {
            return null;
        }
    }

}
