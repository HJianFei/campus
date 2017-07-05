package com.loosoo100.campus100.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.SearchFriendAdapter;
import com.loosoo100.campus100.chat.bean.SearchInfo;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class SchoolMateActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.search_type)
    private RelativeLayout search_type;
    @ViewInject(R.id.search_edit)
    private EditText search_edit;
    @ViewInject(R.id.search_friend_list)
    private ListView search_friend_list;
    @ViewInject(R.id.type)
    private TextView type;
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.ex_change)
    private TextView ex_change;
    private String uid = "";
    private String tip = "";
    private String searchType = "";
    private SearchFriendAdapter mAdapter;
    private String school_id = "";
    private List<String> currUserIds = new ArrayList<>();
    private List<SearchInfo.RootBean> beanList = new ArrayList<>();
    private int position = 10001;
    private Handler search_school_mate_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.arg1 == 1001) {
                if (null != beanList) {
                    beanList.clear();
                }
                beanList = (List<SearchInfo.RootBean>) msg.obj;
                if (null != beanList) {
                    initData(beanList, "nickName");
                } else {
                    ToastUtil.showToast(SchoolMateActivity.this, "搜索结果为空");
                }
            } else if (msg.arg1 == 1002) {
                if (null != beanList) {
                    beanList.clear();
                }
                beanList = (List<SearchInfo.RootBean>) msg.obj;
                if (null != beanList) {
                    initData(beanList, "phoneNumber");
                } else {
                    ToastUtil.showToast(SchoolMateActivity.this, "搜索结果为空");
                }
            } else if (msg.arg1 == 1003) {
                position = msg.arg2;
                searchByType(tip, searchType);
            } else if (msg.arg1 == 1004) {
                if (null != beanList) {
                    beanList.clear();
                }
                beanList = (List<SearchInfo.RootBean>) msg.obj;
                if (null != beanList) {
                    initData(beanList, "");
                }
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_mate);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        initView();
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        school_id = (String) SharedPreferencesUtils.getParam(this, "school_id", "");
        //随机获取同校好友列表
        getInitData();
    }

    private void initData(final List<SearchInfo.RootBean> beanList, String types) {
        mAdapter = new SearchFriendAdapter(this, beanList, tip, types, search_school_mate_handler, position);
        mAdapter.notifyDataSetChanged();
        search_friend_list.setAdapter(mAdapter);
        search_friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SchoolMateActivity.this, CampusContactsFriendActivity.class);
                intent.putExtra("muid", beanList.get(position).getUserId());
                startActivity(intent);
            }
        });
    }

    /**
     * 随机获取同校友友列表
     */
    private void getInitData() {
        GsonRequest<SearchInfo> gsonRequest = new GsonRequest<>(MyConfig.SEARCH_SCHOOL_MATE + "?schoolId=" + school_id + "&userId=" + uid + "&currUserIds=" + currUserIds.toString(), SearchInfo.class, new Response.Listener<SearchInfo>() {
            @Override
            public void onResponse(SearchInfo searchInfo) {
                Message message = Message.obtain();
                if (searchType.equals("1")) {
                    //昵称
                    message.arg1 = 1001;
                } else if (searchType.equals("0")) {
                    //手机号码
                    message.arg1 = 1002;
                } else {
                    message.arg1 = 1004;
                }
                message.obj = searchInfo.getRoot();
                search_school_mate_handler.sendMessage(message);
                if (searchInfo.getRoot() != null) {
                    for (int i = 0; i < searchInfo.getRoot().size(); i++) {
                        String str = searchInfo.getRoot().get(i).getUserId();
                        currUserIds.add(str);
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

    private void initView() {
        search_type.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        ex_change.setOnClickListener(this);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (type.getText().toString().trim().equals("昵称")) {
                        tip = search_edit.getText().toString().trim();
                        //按昵称搜索
                        searchType = "1";
                        searchByType(tip, searchType);

                    } else {
                        if (search_edit.getText().toString().trim().length() >= 3) {
                            tip = search_edit.getText().toString().trim();
                            //按手机号码搜索
                            searchType = "0";
                            searchByType(tip, searchType);
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * 按昵称or手机号码搜索
     *
     * @param tip
     */
    private void searchByType(String tip, final String searchType) {
        GsonRequest<SearchInfo> gsonRequest = new GsonRequest<>(MyConfig.SEARCH_USER + "?search=" + tip + "&type=" + searchType + "&userId=" + uid + "&schoolId=" + school_id, SearchInfo.class, new Response.Listener<SearchInfo>() {
            @Override
            public void onResponse(SearchInfo searchInfo) {
                Message message = Message.obtain();
                if (searchType.equals("1")) {
                    //昵称
                    message.arg1 = 1001;
                } else if (searchType.equals("0")) {
                    //手机号码
                    message.arg1 = 1002;
                }
                message.obj = searchInfo.getRoot();
                search_school_mate_handler.sendMessage(message);
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
            case R.id.search_type:
                if (type.getText().toString().equals("昵称")) {
                    search_edit.setInputType(InputType.TYPE_CLASS_PHONE);
                    type.setText("手机号码");
                } else {
                    search_edit.setInputType(InputType.TYPE_CLASS_TEXT);
                    type.setText("昵称");
                }
                break;
            case R.id.rl_back:
                this.finish();
                break;
            case R.id.ex_change:

                getInitData();
                break;
        }
    }
}
