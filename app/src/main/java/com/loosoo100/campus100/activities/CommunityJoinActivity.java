package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommunityJoinAdapter;
import com.loosoo100.campus100.anyevent.MEventComJoin;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 加入社团activity
 */
public class CommunityJoinActivity extends Activity implements OnClickListener, OnScrollListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.listView)
    private ListView listView; // 列表
    @ViewInject(R.id.et_search)
    private EditText et_search; // 搜索内容
    @ViewInject(R.id.ll_search)
    private LinearLayout ll_search; // 搜索框
    @ViewInject(R.id.rl_progress)
    private RelativeLayout rl_progress; // 加载动画
    @ViewInject(R.id.iv_qrcode) // 二维码扫描
    private ImageView iv_qrcode;

    private int page = 1;
    private int mPosition;

    private boolean isLoading = true;

    private String sid = ""; // 学校ID
    private String uid = ""; // 用户ID
    private String num = ""; // 搜索关键词

    private CommunityJoinAdapter adapter;
    private List<CommunityBasicInfo> list;

    private List<CommunityBasicInfo> communityBasicInfos;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                initListView();
                listView.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.GONE);
            }
            page++;
            isLoading = false;
            rl_progress.setVisibility(View.GONE);
        }

        ;
    };

    /*
     * 加载更多后更新数据
     */
    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            page++;
            isLoading = false;
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_join);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        EventBus.getDefault().register(this);

        rl_progress.setVisibility(View.VISIBLE);
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");

        initView();

        // 数据后台加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                list = GetData.getCommunityBasicInfos(
                        MyConfig.URL_JSON_COMMUNITY_SEARCH + num + "&sid=" + sid + "&uid=" + uid + "&page=" + page);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void initListView() {
        adapter = new CommunityJoinAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                Intent intent = new Intent(CommunityJoinActivity.this, CommDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        listView.setOnScrollListener(this);
        iv_qrcode.setOnClickListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num = et_search.getText().toString().trim();
                page = 1;
                isLoading = true;
                new Thread() {
                    public void run() {
                        list = GetData.getCommunityBasicInfos(MyConfig.URL_JSON_COMMUNITY_SEARCH + num + "&sid=" + sid
                                + "&uid=" + uid + "&page=" + page);
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onEventMainThread(MEventComJoin event) {
        if (event.getStatus().equals("0")) {
            list.get(mPosition).setStatus("0");
            adapter.notifyDataSetChanged();
        } else if (event.getStatus().equals("1")) {
            list.get(mPosition).setStatus("1");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;
            case R.id.iv_qrcode:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, MyConfig.QRCODE);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && !isLoading) {
            isLoading = true;
            new Thread() {
                public void run() {
                    communityBasicInfos = GetData.getCommunityBasicInfos(
                            MyConfig.URL_JSON_COMMUNITY_SEARCH + num + "&sid=" + sid + "&uid=" + uid + "&page=" + page);
                    if (communityBasicInfos != null && communityBasicInfos.size() > 0) {
                        for (int i = 0; i < communityBasicInfos.size(); i++) {
                            list.add(communityBasicInfos.get(i));
                        }
                        handlerRefresh.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            boolean contains = result.contains(",");
            if (contains) {
                if (result.split(",")[0].equals(sid)) {
                    joinCommunity(result.split(",")[1]);
                } else {
                    ToastUtil.showToast(CommunityJoinActivity.this, "不是本校成员不能加入");
                }

            } else {
                ToastUtil.showToast(CommunityJoinActivity.this, "社团不存在");

            }
        }
    }

    private void joinCommunity(String comid) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userid", uid);
        params.addBodyParameter("comnid", comid);
        params.addBodyParameter("sid", sid);
        httpUtils.send(HttpRequest.HttpMethod.POST, MyConfig.URL_POST_COMMUNITY_JOIN, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            list.get(mPosition).setStatus("0");
                            adapter.notifyDataSetChanged();
                            ToastUtil.showToast(CommunityJoinActivity.this, "提交成功,请耐心等待审核");
                        } else if (result.equals("-1")) {
                            CustomDialog.Builder builder = new CustomDialog.Builder(CommunityJoinActivity.this);
                            builder.setMessage("请先完善个人资料");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CommunityJoinActivity.this, BasicInfoActivity.class);
                                    startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("否", null);
                            builder.create().show();
                        } else {
                            ToastUtil.showToast(CommunityJoinActivity.this, "提交失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(CommunityJoinActivity.this, "提交失败");
                    }
                });

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
