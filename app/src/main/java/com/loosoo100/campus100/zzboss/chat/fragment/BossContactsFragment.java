package com.loosoo100.campus100.zzboss.chat.fragment;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.db.FriendDao;
import com.loosoo100.campus100.chat.sortlistview.ClearEditText;
import com.loosoo100.campus100.chat.sortlistview.PinyinComparators;
import com.loosoo100.campus100.chat.sortlistview.SideBar;
import com.loosoo100.campus100.chat.sortlistview.SortAdapters;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.zzboss.chat.bean.FollowInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.tools.CharacterParser;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


public class BossContactsFragment extends Fragment implements View.OnClickListener, RongIM.UserInfoProvider {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String cuid = "";
    private String mParam1;
    private String mParam2;
    private ListView sortListView;
    private ClearEditText mClearEditText;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapters adapter;
    private int index;
    private View view;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortFollows> SourceDateList;
    private List<FollowInfo.RootBean> followBean = new ArrayList<>();
    private List<FollowInfo.RootBean> followBeans = new ArrayList<>();
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparators pinyinComparator;
    private AlertDialog alertDialog;
    private FriendDao friendDao;
    private List<SortFollows> mSortList;


    public BossContactsFragment() {

    }


    public static BossContactsFragment newInstance(String param1, String param2) {
        BossContactsFragment fragment = new BossContactsFragment();
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
        view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        cuid = getActivity().getSharedPreferences(UserInfoDB.USERTABLE, getActivity().MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        friendDao = new FriendDao(getActivity());
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparators();
        initViews(view);
        RongIM.setUserInfoProvider(this, true);
        return view;
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        followBeans.clear();
        FollowInfo.RootBean sortFollow = new FollowInfo.RootBean();
        sortFollow.setUid((String) SharedPreferencesUtils.getParam(getActivity(), "companyID", ""));
        sortFollow.setUserName((String) SharedPreferencesUtils.getParam(getActivity(), "company_name", ""));
        sortFollow.setUserPhotoThums((String) SharedPreferencesUtils.getParam(getActivity(), "company_avatar", ""));
        followBeans.add(sortFollow);
        GsonRequest<FollowInfo> gsonRequest = new GsonRequest<>(MyConfig.GET_FOLLOWS + "?cmpId=" + cuid, FollowInfo.class, new Response.Listener<FollowInfo>() {
            @Override
            public void onResponse(FollowInfo followInfo) {
                List<FollowInfo.RootBean> rootBeen = followInfo.getRoot();
                followBean.clear();
                if (null != rootBeen) {
                    for (FollowInfo.RootBean f : rootBeen) {
                        followBean.add(f);
                        followBeans.add(f);
                    }
                }
                SourceDateList.clear();
                SourceDateList = filledData(followBean);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortAdapters(getActivity(), SourceDateList);
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
        SourceDateList = filledData(followBean);
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
                    RongIM.getInstance().startPrivateChat(getActivity(), SourceDateList.get(position).getUid(), SourceDateList.get(position).getUserName());
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
        View layout = View.inflate(getActivity(), R.layout.boss_content_dialog, null);
        layout.findViewById(R.id._cancel_notice).setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(getActivity()).setView(layout).setInverseBackgroundForced(true).create();
        alertDialog.show();
    }

    /**
     * 为ListView填充数据
     *
     * @param followBeans
     * @rn
     */
    private List<SortFollows> filledData(List<FollowInfo.RootBean> followBeans) {
        mSortList = new ArrayList<>();
        mSortList.clear();
        for (int i = 0; i < followBeans.size(); i++) {
            SortFollows sortFollows = new SortFollows();
            sortFollows.setUid(followBeans.get(i).getUid());
            sortFollows.setCmmId(followBeans.get(i).getCmmId());
            sortFollows.setCmpId(followBeans.get(i).getCmpId());
            sortFollows.setUserName(followBeans.get(i).getUserName());
            sortFollows.setSchool_name(followBeans.get(i).getSchool_name());
            sortFollows.setUserPhotoThums(followBeans.get(i).getUserPhotoThums());

            //汉字转换成拼音
            String pinyin = characterParser.getSelling(followBeans.get(i).getUserName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortFollows.setLetters(sortString.toUpperCase());
            } else {
                sortFollows.setLetters("#");
            }

            mSortList.add(sortFollows);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortFollows> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortFollows sortModel : SourceDateList) {
                String name = sortModel.getUserName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id._cancel_notice:
                if (null != alertDialog) {
                    alertDialog.dismiss();
                }
                cancelConcern(index);
                break;
        }

    }


    private void cancelConcern(final int index) {
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<RequestCode_1>(Request.Method.POST, MyConfig.DEL_FOLLOWS, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                if (requestCode_1.getCode() == 200) {
                    SourceDateList.remove(index);
                    adapter.notifyDataSetChanged();
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, index + "");
                    ToastUtil.showToast(getActivity(), "取消成功");
                } else {
                    ToastUtil.showToast(getActivity(), "取消失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast(getActivity(), "取消失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("cmpId", cuid);
                map.put("cmmId", SourceDateList.get(index).getCmmId());
                return map;
            }
        };
        MyApplication.getRequestQueue().add(gsonRequest);

    }

    @Override
    public UserInfo getUserInfo(String s) {

        if (null != followBeans) {
            for (FollowInfo.RootBean f : followBeans) {
                if (f.getUid().equals(s)) {
                    return new UserInfo(f.getUid(), f.getUserName(), Uri.parse(f.getUserPhotoThums()));
                }
            }
        }
        return null;
    }
}
