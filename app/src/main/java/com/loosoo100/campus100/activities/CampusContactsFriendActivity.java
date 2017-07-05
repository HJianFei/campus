package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CampusContactsPersonalAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAllDel;
import com.loosoo100.campus100.anyevent.MEventCampusContactsChange;
import com.loosoo100.campus100.anyevent.MEventCampusContactsLove;
import com.loosoo100.campus100.anyevent.MEventCampusContactsMyFriend;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.beans.CampusContactsUserInfo;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.bean.RequestCode_3;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableHelper.ScrollableContainer;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * @author yang 校园圈朋友activity
 */
public class CampusContactsFriendActivity extends Activity implements
        OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.ll_love)
    private LinearLayout ll_love;
    @ViewInject(R.id.ll_weixin)
    private LinearLayout ll_weixin;
    @ViewInject(R.id.ll_call)
    private LinearLayout ll_call;
    @ViewInject(R.id.tv_weixin)
    private TextView tv_weixin;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_text)
    private TextView tv_text;

    @ViewInject(R.id.iv_bg)
    private ImageView iv_bg;
    @ViewInject(R.id.cv_headShot)
    private CircleView cv_headShot;
    @ViewInject(R.id.rl_top)
    private RelativeLayout rl_top;
    @ViewInject(R.id.rl_bottom)
    private RelativeLayout rl_bottom;
    @ViewInject(R.id.iv_sex)
    private ImageView iv_sex;
    @ViewInject(R.id.tv_noData)
    private TextView tv_noData;
    @ViewInject(R.id.progress_update_whitebg_campus)
    private RelativeLayout progress_update_whitebg_campus;

    @ViewInject(R.id.lv_campus)
    private ListView lv_campus; // 校园圈列表
    @ViewInject(R.id.tv_unrequited_love)
    private TextView tv_unrequited_love; // 暗恋文本
    @ViewInject(R.id.tv_count)
    private TextView tv_count; // 暗恋人数
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 学校
    @ViewInject(R.id.scrollableLayout)
    private ScrollableLayout scrollableLayout;
    @ViewInject(R.id.btn_chat)
    private Button btn_chat;
    @ViewInject(R.id.rl_popupwindow)
    private RelativeLayout rl_popupwindow; // 弹出菜单项
    @ViewInject(R.id.btn_blacklist)
    private Button btn_blacklist; // 屏蔽ta
    @ViewInject(R.id.btn_status)
    private Button btn_status; // (加为好友、拉黑、申请中。。。)
    @ViewInject(R.id.ll_contacts)
    private LinearLayout ll_contacts; // 申请互换的布局

    private List<CampusContactsInfo> list;
    private CampusContactsPersonalAdapter adapter;
    private CampusContactsUserInfo userInfo;
    private RequestCode_3 requestCode_3;

    private Dialog dialog;
    private String ed_name = "";
    private String sid = "";
    private int type = 0;
    private String uid = "";
    private String muid = "";
    // private String sex = "";
    // private String headShot = "";
    // private String name = "";
    private int page = 1;
    private int status = 2;
    private boolean isLoading = true;

    private String cid = "0"; // 0代表没有暗恋,大于0代表暗恋ID
    private int islove = 0; // 0代表没有暗恋,1代表互相暗恋
    private String phone = "-2"; // 0同意，1已申请，-2互换，其他数字表示相应号码
    private String weixin = "-2"; // 0同意，1已申请，-2互换，其他数字表示相应号码

    // 跳转到QQ
    // private String qqUrl = "";
    private int weixinStatus = 1; // 判断是否填写过微信号,-1没有 0有

    private int blackStatus = -1; // 判断是否屏蔽了ta，-1代表已屏蔽
    private Dialog dialogCopy;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress_update_whitebg_campus.setVisibility(View.GONE);
            rl_more.setClickable(true);
            if (blackStatus == -1) {
                ll_contacts.setVisibility(View.GONE);
                btn_blacklist.setText("取消屏蔽");
            } else {
                ll_contacts.setVisibility(View.VISIBLE);
                btn_blacklist.setText("屏蔽ta");
            }
            initView();
            if (list != null && list.size() > 0) {
                initListView();
                lv_campus.setVisibility(View.VISIBLE);
                tv_noData.setVisibility(View.GONE);
            } else {
                lv_campus.setVisibility(View.GONE);
                tv_noData.setVisibility(View.VISIBLE);
            }
            page = 2;
            isLoading = false;
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                // MyUtils.setListViewHeight(lv_campus, 20);
            }
            page++;
            isLoading = false;
        }

        ;
    };

    private Handler handler3 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                rl_top.setOnClickListener(CampusContactsFriendActivity.this);
                rl_bottom.setOnClickListener(CampusContactsFriendActivity.this);
                if (cid.equals("0")) {
                    tv_unrequited_love.setText("暗恋ta");
                } else {
                    if (islove == 0) {
                        tv_unrequited_love.setText("已暗恋");
                    } else {
                        tv_unrequited_love.setText("暧昧中");
                    }
                }
            } else if (msg.what == 1) {
                userInfo.setCrushNum(userInfo.getCrushNum() + 1);
                tv_count.setText(userInfo.getCrushNum() + "");
                if (islove == 0) {
                    tv_unrequited_love.setText("已暗恋");
                } else {
                    tv_unrequited_love.setText("暧昧中");
                }
            } else if (msg.what == 2) {
                tv_unrequited_love.setText("暗恋ta");
                userInfo.setCrushNum(userInfo.getCrushNum() - 1);
                tv_count.setText(userInfo.getCrushNum() + "");
            } else if (msg.what == 3) {
                if (phone.equals("0")) {
                    tv_phone.setText("对方请求中");
                    ll_call.setClickable(true);
                } else if (phone.equals("1")) {
                    tv_phone.setText("已请求");
                    ll_call.setClickable(false);
                } else if (phone.equals("-2")) {
                    tv_phone.setText("Phone number");
                    ll_call.setClickable(true);
                } else {
                    tv_text.setText("联系Ta");
                    tv_phone.setText("" + phone);
                    ll_call.setClickable(true);

                }
                if (weixin.equals("0")) {
                    tv_weixin.setText("对方请求中");
                    ll_weixin.setClickable(true);
                } else if (weixin.equals("1")) {
                    tv_weixin.setText("已请求");
                    ll_weixin.setClickable(false);
                } else if (weixin.equals("-2")) {
                    tv_weixin.setText("WeChat");
                    ll_weixin.setClickable(true);
                } else {
                    tv_text.setText("联系Ta");
                    tv_weixin.setText("" + weixin);
                    ll_weixin.setClickable(true);
                    // 长按复制内容
                    ll_weixin.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            dialogCopy.show();
                            return true;
                        }
                    });
                    // qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + weixin
                    // + "&version=1";
                }
            }
        }

        ;
    };

    private Handler handler4 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress_update_whitebg_campus.setVisibility(View.GONE);
            if (list != null && list.size() > 0) {
                initListView();
                lv_campus.setVisibility(View.VISIBLE);
                tv_noData.setVisibility(View.GONE);
            } else {
                lv_campus.setVisibility(View.GONE);
                tv_noData.setVisibility(View.VISIBLE);
            }
            page = 2;
            isLoading = false;
        }

        ;
    };

    private Handler handlerqq = new Handler() {
        public void handleMessage(Message msg) {
            ll_weixin.setClickable(true);
            if (weixinStatus == -1) {
                CustomDialog.Builder dialog = new CustomDialog.Builder(
                        CampusContactsFriendActivity.this);
                dialog.setMessage("请先填写微信号？");
                dialog.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(
                                        CampusContactsFriendActivity.this,
                                        BasicInfoActivity.class);
                                startActivity(intent);
                            }
                        });
                dialog.setNegativeButton("否", null);
                dialog.create().show();
            } else if (weixinStatus == 0) {
                CustomDialog.Builder dialog = new CustomDialog.Builder(
                        CampusContactsFriendActivity.this);
                if (weixin.equals("0")) {
                    dialog.setMessage("是否同意互换微信");
                    dialog.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ll_weixin.setClickable(false);
                                    new Thread() {
                                        public void run() {
                                            postAgreeWeChat(MyConfig.URL_POST_CAMPUS_CHANGE_AGREE);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    dialog.setNegativeButton("否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ll_weixin.setClickable(false);
                                    new Thread() {
                                        public void run() {
                                            postNoAgreeWeChat(MyConfig.URL_POST_CAMPUS_CHANGE_NOAGREE);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                } else {
                    dialog.setMessage("是否请求互换微信");
                    dialog.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ll_weixin.setClickable(false);
                                    new Thread() {
                                        public void run() {
                                            postChangeWeChat(MyConfig.URL_POST_CAMPUS_CHANGE);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    dialog.setNegativeButton("否", null);
                }
                dialog.create().show();
            } else {
                ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
            }
        }

        ;
    };
    private Handler is_friend = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            btn_status.setText((String) msg.obj);
            super.handleMessage(msg);
        }
    };
    private Handler handler_req = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 100001) {
                btn_status.setText((String) msg.obj);
            }
            if (msg.arg1 == 100002) {
                btn_status.setText((String) msg.obj);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_contacts_friend);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        // 更改状态栏字体颜色为黑色
        MyUtils.setMiuiStatusBarDarkMode(this, true);
        EventBus.getDefault().register(this);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");

        muid = getIntent().getExtras().getString("muid");

        progress_update_whitebg_campus.setVisibility(View.VISIBLE);
        tv_noData.setVisibility(View.GONE);
        iv_sex.setVisibility(View.GONE);

        rl_back.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        rl_more.setClickable(false);
        btn_blacklist.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_status.setOnClickListener(this);
        ll_weixin.setOnClickListener(CampusContactsFriendActivity.this);
        ll_call.setOnClickListener(CampusContactsFriendActivity.this);
        ll_weixin.setClickable(false);
        ll_call.setClickable(false);

		/*
         * 下滑时当lv_campus滑动到顶部时头部view才显示出来
		 */
        scrollableLayout.getHelper().setCurrentScrollableContainer(
                new ScrollableContainer() {
                    @Override
                    public View getScrollableView() {
                        return lv_campus;
                    }
                });

        new Thread() {
            public void run() {
                // 判断是否暗恋ta
                getIsLove(MyConfig.URL_JSON_CAMPUS_ISLOVE);
                // 判断是否交换了手机微信
                getWeiXinStatus(MyConfig.URL_JSON_CAMPUS_CHANGE_STATUS);
            }

            ;
        }.start();
        //判断是否为好友
        getIsFriend(uid, muid);

        new Thread() {
            public void run() {
                // campusStatus =
                // GetData.getCampusContactsStatus(MyConfig.URL_JSON_CAMPUS_CHANGE_STATUS
                // + uid + "&thisuid=" + muid);
                userInfo = GetData
                        .getCampusContactsUserInfo(MyConfig.URL_JSON_CAMPUS
                                + sid + "&type=" + type + "&muid=" + muid
                                + "&uid=" + uid + "&status=" + status
                                + "&page=" + page);
                list = GetData
                        .getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
                                + sid + "&type=" + type + "&muid=" + muid
                                + "&uid=" + uid + "&status=" + status
                                + "&page=" + page);
                blackStatus = GetData
                        .getCampusContactsStatus(MyConfig.URL_JSON_CAMPUS + sid
                                + "&type=" + type + "&muid=" + muid + "&uid="
                                + uid + "&status=" + status + "&page=" + page);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        dialogCopy = new Dialog(this, R.style.MyDialog);
        View viewDialog = LayoutInflater.from(this).inflate(
                R.layout.dialog_copy, null);
        dialogCopy.setContentView(viewDialog);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        dialogCopy.setContentView(viewDialog, params);
        viewDialog.findViewById(R.id.btn_copy).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(weixin);
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "已复制");
                        dialogCopy.dismiss();
                    }
                });

    }

    /**
     * 判断是否为好友
     *
     * @param uid
     * @param muid
     */
    private void getIsFriend(final String uid, final String muid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.IS_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d("onResponse", response.toString());
                Gson gson = new Gson();
                Message message = Message.obtain();
                requestCode_3 = gson.fromJson(response.toString(), RequestCode_3.class);

                if (requestCode_3.getCode() == 200) {
                    if (requestCode_3.getIsFriend() == 1) {
                        if (requestCode_3.getInBlacklist() == 1) {
                            message.obj = "移除黑名单";
                        } else {
                            message.obj = "删除好友";
                        }
                    } else {
                        if (requestCode_3.getInBlacklist() == 1) {
                            message.obj = "移除黑名单";
                        } else {
                            message.obj = "加为好友";
                        }
                    }
                }
                is_friend.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("target_uid", muid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    private void initListView() {
        adapter = new CampusContactsPersonalAdapter(this, list);
        lv_campus.setAdapter(adapter);
        // MyUtils.setListViewHeight(lv_campus, 20);
        lv_campus.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CampusContactsFriendActivity.this,
                        CampusContactsDetailFriendActivity.class);
                intent.putExtra("mid", list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        if (userInfo != null) {
            iv_sex.setVisibility(View.VISIBLE);
            ll_love.setVisibility(View.VISIBLE);
            tv_name.setText(userInfo.getName());
            tv_count.setText(userInfo.getCrushNum() + "");
            tv_school.setText(userInfo.getSchool());
            if (!userInfo.getHeadShot().equals("")) {
                // 设置头像和性别图标
                Glide.with(this).load(userInfo.getHeadShot()).into(cv_headShot);
            }
            if (!userInfo.getBg().equals("")) {
                // 设置背景图
                Glide.with(CampusContactsFriendActivity.this)
                        .load(userInfo.getBg()).into(iv_bg);
            }
            if (userInfo.getSex().equals("0")) {
                iv_sex.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_male_picture));
            } else {
                iv_sex.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_female_picture));
            }
        }

        lv_campus.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (rl_popupwindow.getVisibility() == View.VISIBLE) {
                    rl_popupwindow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        List<CampusContactsInfo> list2 = null;

                        public void run() {
                            list2 = GetData
                                    .getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
                                            + sid
                                            + "&type="
                                            + type
                                            + "&uid="
                                            + uid
                                            + "&muid=" + muid
                                            + "&status="
                                            + status
                                            + "&page=" + page);
                            if (list2 != null && list2.size() > 0) {
                                for (int i = 0; i < list2.size(); i++) {
                                    list.add(list2.get(i));
                                }
                                if (!isDestroyed()) {
                                    handler2.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.rl_more:
                if (rl_popupwindow.getVisibility() == View.GONE) {
                    rl_popupwindow.setVisibility(View.VISIBLE);
                } else {
                    rl_popupwindow.setVisibility(View.GONE);
                }
                break;

            case R.id.btn_blacklist:
                rl_popupwindow.setVisibility(View.GONE);
                if (blackStatus == -1) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否取消屏蔽");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    new Thread() {
                                        public void run() {
                                            postDelBlacklist(MyConfig.URL_POST_CAMPUS_BLACK_DEL);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否屏蔽ta");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    new Thread() {
                                        public void run() {
                                            postBlacklist(MyConfig.URL_POST_CAMPUS_BLACK);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                }

                break;
            case R.id.btn_status:
                rl_popupwindow.setVisibility(View.GONE);
                if (btn_status.getText().toString().equals("加为好友")) {
                    addToFriend();

                } else if (btn_status.getText().toString().equals("删除好友")) {
                    deleteFriend(muid);

                } else if (btn_status.getText().toString().equals("移除黑名单")) {
                    removeBlackList(uid, muid);

                }

                break;

            // 聊天
            case R.id.btn_chat:
                //启动会话界面
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(CampusContactsFriendActivity.this, muid, "");
                }

                break;

            case R.id.ll_weixin:
                // && !qqUrl.equals("")
                if (!weixin.equals("0") && !weixin.equals("1")
                        && !weixin.equals("-2")) {
                    return;
                }
                ll_weixin.setClickable(false);
                new Thread() {
                    public void run() {
                        weixinStatus = GetData
                                .getQQStatus(MyConfig.URL_JSON_CAMPUS_CHANGE_QQ_STATUS
                                        + uid);
                        if (!isDestroyed()) {
                            handlerqq.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.ll_call:
                if (!phone.equals("0") && !phone.equals("1") && !phone.equals("-2")
                        && !phone.equals("")) {
                    Intent hotlineIntent = new Intent();
                    hotlineIntent.setAction(Intent.ACTION_VIEW);
                    hotlineIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(hotlineIntent);
                    return;
                }
                if (phone.equals("0")) {
                    CustomDialog.Builder dialog2 = new CustomDialog.Builder(
                            CampusContactsFriendActivity.this);
                    dialog2.setMessage("是否同意互换电话");
                    dialog2.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ll_call.setClickable(false);
                                    new Thread() {
                                        public void run() {
                                            postAgreePhone(MyConfig.URL_POST_CAMPUS_CHANGE_AGREE);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    dialog2.setNegativeButton("否",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ll_call.setClickable(false);
                                    new Thread() {
                                        public void run() {
                                            postNoAgreePhone(MyConfig.URL_POST_CAMPUS_CHANGE_NOAGREE);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    dialog2.create().show();
                } else {
                    CustomDialog.Builder dialog2 = new CustomDialog.Builder(
                            CampusContactsFriendActivity.this);
                    dialog2.setMessage("是否请求互换电话");
                    dialog2.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ll_call.setClickable(false);
                                    new Thread() {
                                        public void run() {
                                            postChangePhone(MyConfig.URL_POST_CAMPUS_CHANGE);
                                        }

                                        ;
                                    }.start();
                                }
                            });
                    dialog2.setNegativeButton("否", null);
                    dialog2.create().show();
                }
                break;

            case R.id.rl_top:
                if (userInfo == null) {
                    return;
                }
                Intent intent = new Intent(this, PicturePreviewActivity.class);
                intent.putExtra("picUrl", userInfo.getHeadShot());
                startActivity(intent);
                break;

            case R.id.rl_bottom:
                CustomDialog.Builder builderSure = new CustomDialog.Builder(this);
                builderSure.setMessage(cid.equals("0") ? "暗恋Ta？" : "另有所恋？");
                builderSure.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread() {
                                    public void run() {
                                        if (cid.equals("0")) {
                                            postLove(MyConfig.URL_POST_CAMPUS_LOVE);
                                        } else {
                                            postCancelLove(MyConfig.URL_POST_CAMPUS_NOLOVE);
                                        }
                                    }

                                    ;
                                }.start();
                            }
                        });
                builderSure.setNegativeButton("否", null);
                builderSure.create().show();
                break;
        }
    }

    private void removeBlackList(final String uid, final String muid) {
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<RequestCode_1>(Request.Method.POST, MyConfig.REMOVE_FROM_BLACK_LIST, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                LogUtils.d("requestCode1", requestCode_1.getCode() + "");
                if (requestCode_1.getCode() == 200) {
                    Message message = Message.obtain();
                    message.arg1 = 100002;
                    if (requestCode_3.getIsFriend() == 1) {
                        message.obj = "删除好友";
                    } else {
                        message.obj = "加为好友";
                    }
                    handler_req.sendMessage(message);
                    ToastUtil.showToast(CampusContactsFriendActivity.this, "移除成功");
                } else {
                    ToastUtil.showToast(CampusContactsFriendActivity.this, "移除失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast(CampusContactsFriendActivity.this, "移除失败，稍后再试");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("black_uid", muid);
                return map;
            }

        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    private void deleteFriend(final String muid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.DELETE_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RequestCode_1 requestCode_1 = gson.fromJson(response.toString(), RequestCode_1.class);
                if (requestCode_1.getCode() == 200) {
                    Message message = Message.obtain();
                    message.arg1 = 100001;
                    message.obj = "加为好友";
                    handler_req.sendMessage(message);
                    ToastUtil.showToast(CampusContactsFriendActivity.this, "删除成功");
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, muid);

                } else {
                    ToastUtil.showToast(CampusContactsFriendActivity.this, "删除失败");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("TAG", error.getMessage());
                ToastUtil.showToast(CampusContactsFriendActivity.this, "删除失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("del_uid", muid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    private void addToFriend() {
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
//                sendRequest(ed_name);
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
        Glide.with(this).load(userInfo.getHeadShot()).error(R.drawable.default_useravatar).into(iv_avatar);
        TextView tv_name = (TextView) viewDialog.findViewById(R.id.tv_name);
        tv_name.setText(userInfo.getName());
        final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
        TextView btn_ok = (TextView) viewDialog.findViewById(R.id.btn_ok);
        TextView btn_cancel = (TextView) viewDialog.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_name = ed_description.getText().toString().trim();
                dialog.dismiss();
                sendRequest(ed_name);

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

    private void sendRequest(final String msg) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.ADD_FRIEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.d("response", response);
                Message message = Message.obtain();
                message.obj = "申请中";
                is_friend.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("to_uid", muid);
                map.put("msg", msg);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /**
     * 判断微信手机状态
     *
     * @param uploadHost
     */
    private void getWeiXinStatus(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("thisuid", muid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            phone = jsonObject.getString("phone");
                            weixin = jsonObject.getString("weixin");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain();
                        message.what = 3;
                        handler3.sendMessage(message);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }
                });
    }

    /**
     * 判断是否暗恋ta
     *
     * @param uploadHost
     */
    private void getIsLove(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", uid);
        params.addBodyParameter("wid", muid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            cid = jsonObject.getString("cid");
                            islove = jsonObject.getInt("islove");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain();
                        message.what = 0;
                        handler3.sendMessage(message);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                    }
                });
    }

    /**
     * 暗恋ta
     *
     * @param uploadHost
     */
    private void postLove(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", uid);
        params.addBodyParameter("wid", muid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("cid");
                            islove = jsonObject.getInt("islove");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!result.equals("0")) {
                            EventBus.getDefault().post(
                                    new MEventCampusContactsLove(true));
                            cid = result;
                            Message message = Message.obtain();
                            message.what = 1;
                            handler3.sendMessage(message);
                        } else {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 取消暗恋
     *
     * @param uploadHost
     */
    private void postCancelLove(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("cid", cid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            EventBus.getDefault().post(
                                    new MEventCampusContactsLove(true));
                            cid = "0";
                            Message message = Message.obtain();
                            message.what = 2;
                            handler3.sendMessage(message);
                        } else {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                    }
                });
    }

    private void postChangeWeChat(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("touid", muid);
        params.addBodyParameter("type", "1");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("-1")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "对方已申请跟你互换");
                            tv_weixin.setText("对方请求中");
                            weixin = "0";
                            ll_weixin.setClickable(true);
                        } else if (result.equals("-3")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "您还未填写微信号");
                            ll_weixin.setClickable(true);
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                            ll_weixin.setClickable(true);
                        } else {
                            tv_weixin.setText("已请求");
                            ll_weixin.setClickable(false);
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "请求成功");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        ll_weixin.setClickable(true);
                    }
                });
    }

    private void postChangePhone(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("touid", muid);
        params.addBodyParameter("type", "0");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("-1")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "对方已申请跟你互换");
                            phone = "0";
                            tv_phone.setText("对方请求中");
                            ll_call.setClickable(true);
                        } else if (result.equals("-3")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "您还未填写微信号");
                            ll_call.setClickable(true);
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                            ll_call.setClickable(true);
                        } else {
                            tv_phone.setText("已请求");
                            ll_call.setClickable(false);
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "请求成功");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        ll_call.setClickable(true);
                    }
                });
    }

    private void postAgreePhone(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("touid", muid);
        params.addBodyParameter("type", "0");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        ll_call.setClickable(true);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        } else {
                            phone = result;
                            tv_phone.setText(phone);
                            tv_text.setText("联系Ta");
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "电话互换成功");
                            EventBus.getDefault().post(
                                    new MEventCampusContactsMyFriend(true));
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        ll_call.setClickable(true);
                    }
                });
    }

    private void postNoAgreePhone(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("touid", muid);
        params.addBodyParameter("type", "0");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        ll_call.setClickable(true);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            phone = "-2";
                            tv_phone.setText("Phone number");
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "拒绝成功");
                        } else {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        ll_call.setClickable(true);
                    }
                });
    }

    private void postAgreeWeChat(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("touid", muid);
        params.addBodyParameter("type", "1");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        ll_weixin.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("-1")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "您还未填写微信号");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        } else {
                            weixin = result;
                            tv_weixin.setText(weixin);
                            tv_text.setText("联系Ta");
                            // 长按复制内容
                            ll_weixin.setOnLongClickListener(new OnLongClickListener() {

                                @Override
                                public boolean onLongClick(View v) {
                                    dialogCopy.show();
                                    return true;
                                }
                            });
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "微信互换成功");
                            EventBus.getDefault().post(
                                    new MEventCampusContactsMyFriend(true));
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        ll_weixin.setClickable(true);
                    }
                });
    }

    private void postNoAgreeWeChat(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("touid", muid);
        params.addBodyParameter("type", "1");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        ll_weixin.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            weixin = "-2";
                            tv_weixin.setText("WeChat");
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "拒绝成功");
                        } else {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        ll_weixin.setClickable(true);
                    }
                });
    }

    private void postBlacklist(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("delid", muid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "屏蔽成功");
                            btn_blacklist.setText("取消屏蔽");
                            ll_contacts.setVisibility(View.GONE);
                            blackStatus = -1;
                            if (list != null) {
                                list.clear();
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            // 通知更新圈圈首页内容
                            EventBus.getDefault().post(
                                    new MEventCampusContactsChange(true));
                        } else {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                    }
                });
    }

    private void postDelBlacklist(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("delid", muid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "已取消屏蔽");
                            ll_contacts.setVisibility(View.VISIBLE);
                            btn_blacklist.setText("屏蔽ta");
                            blackStatus = 0;
                            progress_update_whitebg_campus
                                    .setVisibility(View.VISIBLE);
                            new Thread() {
                                public void run() {
                                    list = GetData
                                            .getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
                                                    + sid
                                                    + "&type="
                                                    + type
                                                    + "&muid="
                                                    + muid
                                                    + "&uid="
                                                    + uid
                                                    + "&status="
                                                    + status
                                                    + "&page=1");
                                    if (!isDestroyed()) {
                                        handler4.sendEmptyMessage(0);
                                    }
                                }

                                ;
                            }.start();
                            // 通知更新圈圈首页内容
                            EventBus.getDefault().post(
                                    new MEventCampusContactsChange(true));
                        } else {
                            ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(CampusContactsFriendActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 判断是否安装过QQ客户端
     *
     * @param context
     * @return
     */
    private boolean isQQClientAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite")
                        || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onEventMainThread(MEventCampusContactsAllDel event) {
        if (event.isChange()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        rl_popupwindow.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_popupwindow.getVisibility() == View.VISIBLE) {
                rl_popupwindow.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}