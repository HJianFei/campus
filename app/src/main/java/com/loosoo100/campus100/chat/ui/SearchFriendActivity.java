package com.loosoo100.campus100.chat.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.zxing.activity.CaptureActivity;
import com.loosoo100.campus100.zxing.encoding.EncodingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFriendActivity extends FragmentActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.search_type)
    private RelativeLayout search_type;
    @ViewInject(R.id.search_edit)
    private EditText search_edit;
    @ViewInject(R.id.search_friend_list)
    private ListView search_friend_list;
    @ViewInject(R.id.my_rq_code)
    private LinearLayout my_rq_code;
    @ViewInject(R.id.the_same_school)
    private LinearLayout the_same_school;
    @ViewInject(R.id.ll_scan_friend)
    private LinearLayout ll_scan_friend;
    @ViewInject(R.id.type)
    private TextView type;
    @ViewInject(R.id.search_head)
    private LinearLayout search_head;
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    private String uid = "";
    private String tip = "";
    private Dialog dialog;
    private String ed_name = "";
    private String searchType = "";
    private SearchFriendAdapter mAdapter;
    private List<SearchInfo.RootBean> beanList = new ArrayList<>();
    private int position = 10001;

    private Handler search_friend_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.arg1 == 1001) {
                if (null != beanList) {
                    beanList.clear();
                }
                beanList = (List<SearchInfo.RootBean>) msg.obj;
                if (null != beanList) {
                    search_head.setVisibility(View.GONE);
                    initData(beanList, "nickName");
                } else {
                    ToastUtil.showToast(SearchFriendActivity.this, "搜索结果为空");
                    search_head.setVisibility(View.VISIBLE);
                }
            } else if (msg.arg1 == 1002) {
                if (null != beanList) {
                    beanList.clear();
                }
                beanList = (List<SearchInfo.RootBean>) msg.obj;
                if (null != beanList) {
                    search_head.setVisibility(View.GONE);
                    initData(beanList, "phoneNumber");
                } else {
                    ToastUtil.showToast(SearchFriendActivity.this, "搜索结果为空");
                    search_head.setVisibility(View.VISIBLE);
                }
            } else if (msg.arg1 == 1003) {
                position = msg.arg2;
                searchByType(tip, searchType);
            }
            super.handleMessage(msg);
        }
    };

    private void initData(final List<SearchInfo.RootBean> beanList, String types) {
        mAdapter = new SearchFriendAdapter(this, beanList, tip, types, search_friend_handler, position);
        mAdapter.notifyDataSetChanged();
        search_friend_list.setAdapter(mAdapter);
        search_friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchFriendActivity.this, CampusContactsFriendActivity.class);
                intent.putExtra("muid", beanList.get(position).getUserId());
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_friend);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        initView();
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
    }

    private void initView() {
        search_type.setOnClickListener(this);
        my_rq_code.setOnClickListener(this);
        the_same_school.setOnClickListener(this);
        ll_scan_friend.setOnClickListener(this);
        rl_back.setOnClickListener(this);
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
        GsonRequest<SearchInfo> gsonRequest = new GsonRequest<>(MyConfig.SEARCH_USER + "?search=" + tip + "&type=" + searchType + "&userId=" + uid, SearchInfo.class, new Response.Listener<SearchInfo>() {
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
                search_friend_handler.sendMessage(message);
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
            case R.id.the_same_school:
                Intent intent1 = new Intent(SearchFriendActivity.this, SchoolMateActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_scan_friend:
                Intent intent = new Intent(SearchFriendActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 10009);
                break;
            case R.id.my_rq_code:
                showQRCodeDialog();
                break;
            case R.id.rl_back:
                this.finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 10009) {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result");
                //添加好友请求
                if (result.startsWith("myQRCode")) {
                    String[] split = result.split(">");
                    addToFriend(split);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addToFriend(final String[] result) {
//        dialog = new Dialog(this, R.style.MyDialog);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View viewDialog = inflater.inflate(R.layout.layout_alert_dialog, null);
//        dialog.setContentView(viewDialog);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        dialog.setContentView(viewDialog, params);
//        TextView tv_title = (TextView) viewDialog.findViewById(R.id.tv_title);
//        tv_title.setText("验证消息");
//        final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
//        Button btn_ok = (Button) viewDialog.findViewById(R.id.btn_ok);
//        Button btn_cancel = (Button) viewDialog.findViewById(R.id.btn_cancel);
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ed_name = ed_description.getText().toString().trim();
//                dialog.dismiss();
//                sendRequest(ed_name, result);
//
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != dialog) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        dialog.show();
        dialog = new Dialog(this, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.add_friend_layout, null);
        dialog.setContentView(viewDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(viewDialog, params);
        CircleView iv_avatar = (CircleView) viewDialog.findViewById(R.id.iv_avatar);
        Glide.with(this).load(result[2]).error(R.drawable.default_useravatar).into(iv_avatar);
        TextView tv_name = (TextView) viewDialog.findViewById(R.id.tv_name);
        tv_name.setText(result[3]);
        final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
        TextView btn_ok = (TextView) viewDialog.findViewById(R.id.btn_ok);
        TextView btn_cancel = (TextView) viewDialog.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_name = ed_description.getText().toString().trim();
                dialog.dismiss();
                sendRequest(ed_name, result[1]);

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialog) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void sendRequest(final String msg, final String result) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.ADD_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToastUtil.showToast(SearchFriendActivity.this, "请求发送成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                ToastUtil.showToast(SearchFriendActivity.this, "操作失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("to_uid", result);
                map.put("msg", msg);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
    }

    private void showQRCodeDialog() {
        String school_name = (String) SharedPreferencesUtils.getParam(SearchFriendActivity.this, "school_name", "");
        String user_name = (String) SharedPreferencesUtils.getParam(SearchFriendActivity.this, "user_name", "");
        String avatar = (String) SharedPreferencesUtils.getParam(SearchFriendActivity.this, "user_avatar", "");
        Bitmap bitmap = EncodingUtils.createQRCode("myQRCode>" + uid + ">" + avatar + ">" + user_name, 400, 400, null);
        LayoutInflater qr_view = getLayoutInflater();
        View layout = qr_view
                .inflate(R.layout.my_qrcode_dialog, null);
        CircleView iv_qrcode_img = (CircleView) layout
                .findViewById(R.id.cv_qrcode_head);
        Glide.with(this).load(avatar)
                .error(R.drawable.default_useravatar)
                .into(iv_qrcode_img);
        TextView tv_title = (TextView) layout
                .findViewById(R.id.tv_qrcode_communityName);

        tv_title.setText(user_name);
        TextView tv_id = (TextView) layout.findViewById(R.id.tv_qrcode_id);
        tv_id.setText(school_name);
        ImageView iv_qrcode = (ImageView) layout.findViewById(R.id.my_qrcode);
        iv_qrcode.setImageBitmap(bitmap);
        new AlertDialog.Builder(this).setView(layout)
                .setInverseBackgroundForced(true).show();
    }
}
