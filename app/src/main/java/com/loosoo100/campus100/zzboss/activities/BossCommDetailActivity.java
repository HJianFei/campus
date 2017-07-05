package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.BasicInfoActivity;
import com.loosoo100.campus100.activities.CommDetailActivity;
import com.loosoo100.campus100.adapters.CommActiListHomeAdapter;
import com.loosoo100.campus100.adapters.CommunityDepGridviewAdapter;
import com.loosoo100.campus100.anyevent.MEventCollectCommChange;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.chat.bean.Community_Info;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.MyScrollView;
import com.loosoo100.campus100.view.MyScrollView.OnScrollListener;
import com.loosoo100.campus100.zzboss.chat.bean.ConcernInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * @author yang 社团详情activity
 */
public class BossCommDetailActivity extends Activity implements OnClickListener, OnScrollListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.iv_empty2)
    private ImageView iv_empty2; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.rl_topbar)
    private RelativeLayout rl_topbar; // 顶部颜色
    @ViewInject(R.id.abscrollview)
    private MyScrollView scrollview;
    @ViewInject(R.id.iv_picture)
    private ImageView iv_picture; // 社团背景图
    @ViewInject(R.id.cv_headShot)
    private CircleView cv_headShot; // 社团头像
    @ViewInject(R.id.tv_communityName)
    private TextView tv_communityName; // 社团名称
    @ViewInject(R.id.tv_id)
    private TextView tv_id; // 社团号
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 所属学校
    @ViewInject(R.id.tv_name)
    private TextView tv_name; // 创建人
    @ViewInject(R.id.tv_slogan)
    private TextView tv_slogan; // 口号
    @ViewInject(R.id.ll_notice)
    private LinearLayout ll_notice; // 社团公告布局
    @ViewInject(R.id.tv_notice)
    private TextView tv_notice; // 社团公告
    @ViewInject(R.id.tv_summary)
    private TextView tv_summary; // 社团简介
    @ViewInject(R.id.ll_more)
    private LinearLayout ll_more; // 社团活动更多
    //	@ViewInject(R.id.ll_mem_more)
//	private LinearLayout ll_mem_more; // 社团成员更多
    @ViewInject(R.id.lv_activity)
    private ListView lv_activity; // 活动列表
    @ViewInject(R.id.iv_back_bg)
    private ImageView iv_back_bg; // 返回背景
    @ViewInject(R.id.tv_type)
    private TextView tv_type; // 社团类型
    @ViewInject(R.id.gv_dep)
    private GridView gv_dep; // 社团架构

    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update_blackbg)
    private RelativeLayout progress_update_blackbg; // 加载动画
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more; // 更多
    @ViewInject(R.id.rl_popupwindow)
    private RelativeLayout rl_popupwindow; // 弹出菜单项
    @ViewInject(R.id.btn_join)
    private Button btn_join; // 关注社团
    @ViewInject(R.id.btn_chat)
    private Button btn_chat; // 联系社团
//	@ViewInject(R.id.tv_memCount)
//	private TextView tv_memCount; //社团成员数

    private String cid = ""; // 传进来的社团ID
    private String cuid = "";

    private List<CommunityActivityInfo> list;
    private CommunityBasicInfo communityBasicInfos;
    private int focusStatus;    //是否关注社团

    private CommActiListHomeAdapter listAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (communityBasicInfos != null) {
                initView();
                initListView();
                initDepGridView();
                progress.setVisibility(View.GONE);
            } else {
                progress.setVisibility(View.VISIBLE);
            }

        }
    };

    private Handler handlerUpdate = new Handler() {
        public void handleMessage(android.os.Message msg) {
            list = communityBasicInfos.getActivityInfos();
            if (list != null && list.size() > 0) {
                listAdapter = new CommActiListHomeAdapter(
                        BossCommDetailActivity.this, list);
                lv_activity.setAdapter(listAdapter);
                // 设置listview的高度
                MyUtils.setListViewHeight(lv_activity, 0);
            }
        }
    };
    private Handler handler_id = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1001) {
                cid = (String) msg.obj;
                // 数据后台加载
                new Thread() {
                    @Override
                    public void run() {
                        communityBasicInfos = GetData
                                .getCommunityDetailInfos(MyConfig.URL_JSON_COMMUNITY_DETAIL
                                        + cid + "&uid=" + cuid + "&type=1");
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }.start();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_comm_detail);
        ViewUtils.inject(this);

        EventBus.getDefault().register(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setStatusBarHeight(this, iv_empty2);

        progress.setVisibility(View.VISIBLE);

        cid = getIntent().getExtras().getString("id");
        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");
        rl_back.setOnClickListener(this);

        String community_id = getIntent().getStringExtra("company");
        if (community_id != null) {
            getCommunityId(community_id);
        } else {
            LogUtils.d("onResponse", "here...");
            cid = getIntent().getExtras().getString("id");
            // 数据后台加载
            new Thread() {
                @Override
                public void run() {
                    communityBasicInfos = GetData
                            .getCommunityDetailInfos(MyConfig.URL_JSON_COMMUNITY_DETAIL
                                    + cid + "&uid=" + cuid + "&type=1");
                    if (!isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }
            }.start();

        }

    }
    private void getCommunityId(final String community_id) {
        final Message message = Message.obtain();
        GsonRequest<Community_Info> gsonRequest = new GsonRequest<>(MyConfig.GET_COMMUNITY_ID + "?uid=" + community_id, Community_Info.class, new Response.Listener<Community_Info>() {
            @Override
            public void onResponse(Community_Info community_info) {
                if (community_info.getCode() == 200) {
                    message.arg1 = 1001;
                    message.obj = community_info.getCommunity_id();
                    handler_id.sendMessage(message);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    /**
     * 社团架构
     */
    private void initDepGridView() {
        if (communityBasicInfos.getDep().trim().equals("")) {
            return;
        }
        List<String> depList = new ArrayList<String>();
        String[] dep = communityBasicInfos.getDep().split(",");
        for (int i = 0; i < dep.length; i++) {
            depList.add(dep[i]);
        }
        gv_dep.setAdapter(new CommunityDepGridviewAdapter(this, depList));
        // 设置gridview的高度
        MyUtils.setGridViewHeight(gv_dep, 0, 4);
    }

    private void initListView() {
        list = communityBasicInfos.getActivityInfos();
        listAdapter = new CommActiListHomeAdapter(this, list);
        lv_activity.setAdapter(listAdapter);

        lv_activity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (list.get(position).getClassify() == 0) {
                    Intent intent = new Intent(BossCommDetailActivity.this,
                            BossCommActivityDetailTogetherActivity.class);
                    intent.putExtra("aid", list.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BossCommDetailActivity.this,
                            BossCommActivityDetailFreeActivity.class);
                    intent.putExtra("aid", list.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        // 设置listview的高度
        MyUtils.setListViewHeight(lv_activity, 0);
    }

    private void initView() {
        ll_more.setOnClickListener(this);
//		ll_mem_more.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        scrollview.setOnScrollListener(this);

        tv_communityName.setText(communityBasicInfos.getCommunityName());
        tv_id.setText("" + communityBasicInfos.getId());
        tv_school.setText(communityBasicInfos.getSchool());
        tv_name.setText(communityBasicInfos.getName());
        tv_slogan.setText(communityBasicInfos.getSlogan());
        tv_summary.setText(communityBasicInfos.getSummary());
        tv_notice.setText(communityBasicInfos.getNotice());
//		tv_memCount.setText(communityBasicInfos.getMemCount()+"人");

        tv_type.setText(communityBasicInfos.getType());

        if (focusStatus == 0) {
            btn_join.setText("关注社团");
        } else {
            btn_join.setText("取消关注");
        }

        if (!communityBasicInfos.getHeadthumb().equals("")
                && !communityBasicInfos.getHeadthumb().equals("null")) {
            Glide.with(this).load(communityBasicInfos.getHeadthumb())
                    .into(cv_headShot);
        }
        if (!communityBasicInfos.getCommBg().equals("")
                && !communityBasicInfos.getCommBg().equals("null")) {
            Glide.with(this).load(communityBasicInfos.getCommBg())
                    .placeholder(R.drawable.comm_bg).into(iv_picture);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            // 社团活动
            case R.id.ll_more:
                Intent intentActi = new Intent(this, BossCommActiMoreActivity.class);
                intentActi.putExtra("id", communityBasicInfos.getId());
                startActivity(intentActi);
                break;

//		// 社团成员
//		case R.id.ll_mem_more:
//			Intent intent = new Intent(this, BossCommMemberActivity.class);
//			intent.putExtra("cid", cid);
//			startActivity(intent);
//			break;

            // 右上角更多
            case R.id.rl_more:
                if (rl_popupwindow.getVisibility() == View.GONE) {
                    rl_popupwindow.setVisibility(View.VISIBLE);
                } else {
                    rl_popupwindow.setVisibility(View.GONE);
                }
                break;

            // 右上角关注社团
            case R.id.btn_join:
                rl_popupwindow.setVisibility(View.GONE);
                if (communityBasicInfos.getFlag()==0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            BossCommDetailActivity.this);
                    builder.setMessage("请先认证企业");
                    builder.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BossCommDetailActivity.this,
                                            BossIdentificationActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else if (communityBasicInfos.getFlag()==1) {
                    ToastUtil.showToast(BossCommDetailActivity.this, "请等待企业审核通过后再关注社团");
                } else if (communityBasicInfos.getFlag()==2) {
                    if (btn_join.getText().toString().equals("关注社团")) {
                        //添加公司关注社团
                        addToFollow(cuid, communityBasicInfos.getId());
                    } else {
                        //取消公司关注社团
                        delToFollow(cuid, communityBasicInfos.getId());
                    }
                }
                break;

            // 右上角联系社团
            case R.id.btn_chat:
                rl_popupwindow.setVisibility(View.GONE);
                if (communityBasicInfos.getFlag()==0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            BossCommDetailActivity.this);
                    builder.setMessage("请先认证企业");
                    builder.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(BossCommDetailActivity.this,
                                            BossIdentificationActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("否", null);
                    builder.create().show();
                } else if (communityBasicInfos.getFlag()==1) {
                    ToastUtil.showToast(BossCommDetailActivity.this, "请等待企业审核通过后再联系社团");
                } else if (communityBasicInfos.getFlag()==2) {
                    //启动会话界面
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(BossCommDetailActivity.this, communityBasicInfos.getUserId(), communityBasicInfos.getCommunityName());
                    }
                } else {
                    ToastUtil.showToast(BossCommDetailActivity.this, "网络异常，请稍候再试");
                }

                break;

        }
    }

    /**
     * 取消公司关注社团
     *
     * @param cuid
     * @param cid
     */
    private void delToFollow(final String cuid, final String cid) {
        GsonRequest<RequestCode_1> gsonRequest = new GsonRequest<RequestCode_1>(Request.Method.POST, MyConfig.DEL_FOLLOWS, RequestCode_1.class, new Response.Listener<RequestCode_1>() {
            @Override
            public void onResponse(RequestCode_1 requestCode_1) {
                if (requestCode_1.getCode() == 200) {
                    ToastUtil.showToast(BossCommDetailActivity.this, "取消成功");
                    btn_join.setText("关注社团");
                } else {
                    ToastUtil.showToast(BossCommDetailActivity.this, "取消失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast(BossCommDetailActivity.this, "取消失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("cmpId", cuid);
                map.put("cmmId", cid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    /**
     * 添加公司关注社团
     *
     * @param cuid
     * @param cid
     */
    private void addToFollow(final String cuid, final String cid) {
        GsonRequest<ConcernInfo> gsonRequest = new GsonRequest<ConcernInfo>(Request.Method.POST, MyConfig.ADD_FOLLOWS, ConcernInfo.class, new Response.Listener<ConcernInfo>() {
            @Override
            public void onResponse(ConcernInfo concernInfo) {
                LogUtils.d("onResponse", concernInfo.getCode() + "");
                if (concernInfo.getCode() == 200) {
                    ToastUtil.showToast(BossCommDetailActivity.this, "关注成功");
                    btn_join.setText("取消关注");
                } else {
                    ToastUtil.showToast(BossCommDetailActivity.this, "操作失败，稍后再试");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.d("onResponse", volleyError.toString());
                ToastUtil.showToast(BossCommDetailActivity.this, "操作失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("cmpId", cuid);
                map.put("cmmId", communityBasicInfos.getId());
                return map;
            }
        };
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    @Override
    public void onScroll(int scrollY) {
        // rl_topbar.setAlpha((float) scrollY / 500);
        // iv_empty.setAlpha((float) scrollY / 500);
        // iv_back_bg.setAlpha(0.3f - (float) scrollY / 500);
        rl_popupwindow.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            new Thread() {
                @Override
                public void run() {
                    communityBasicInfos = GetData
                            .getCommunityDetailInfos(MyConfig.URL_JSON_COMMUNITY_DETAIL
                                    + cid + "&uid=" + cuid + "&type=1");
                    if (communityBasicInfos != null && !isDestroyed()) {
                        handlerUpdate.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 活动支付更新活动数据
     *
     * @param event
     */
    public void onEventMainThread(MEventCommActiPay event) {
        if (event.isChange()) {
            new Thread() {
                @Override
                public void run() {
                    communityBasicInfos = GetData
                            .getCommunityDetailInfos(MyConfig.URL_JSON_COMMUNITY_DETAIL
                                    + cid + "&uid=" + cuid + "&type=1");
                    if (communityBasicInfos != null && !isDestroyed()) {
                        handlerUpdate.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    protected void onPause() {
        rl_popupwindow.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (rl_popupwindow.getVisibility() == View.VISIBLE) {
            rl_popupwindow.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
