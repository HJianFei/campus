package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.beans.GiftGetInfo;
import com.loosoo100.campus100.chat.bean.Friend;
import com.loosoo100.campus100.chat.bean.FriendInfo;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.R.attr.isDefault;
import static com.loosoo100.campus100.R.drawable.gift;
import static com.loosoo100.campus100.beans.BusinessInfo.address;

/**
 * @author yang 礼物盒子领取activity
 */
public class GiftGetActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮

    @ViewInject(R.id.iv_goodsIcon)
    private ImageView iv_goodsIcon; // 商品图标
    @ViewInject(R.id.btn_get)
    private Button btn_get; // 领取礼物|已领取|查看物流

    @ViewInject(R.id.tv_name)
    private TextView tv_name; // 用户昵称
    @ViewInject(R.id.cv_headshot)
    private CircleView cv_headshot; // 用户头像
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg; // 加载动画

    private String uid = "";
    private String oid = "";
    private String userName = "";
    private String userPhone = "";
    private String address = "";
    private String areaId1 = "";
    private String areaId2 = "";
    private String areaId3 = "";

    private GiftGetInfo.ListBean rootBeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_get);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        oid = getIntent().getExtras().getString("oid");

        rl_back.setOnClickListener(this);
        btn_get.setOnClickListener(this);
        btn_get.setClickable(false);

        progress.setVisibility(View.VISIBLE);
        initData();

    }

    private void initData() {
        GsonRequest<GiftGetInfo> gsonRequest = new GsonRequest<GiftGetInfo>(MyConfig.URL_JSON_GIFT_GET + "?oid=" + oid + "&touid=" + uid, GiftGetInfo.class, new Response.Listener<GiftGetInfo>() {
            @Override
            public void onResponse(GiftGetInfo friendInfo) {
                rootBeen = friendInfo.getList();
                if (null != rootBeen) {
                    if (rootBeen.getStatus() == -1) {
                        ToastUtil.showToast(GiftGetActivity.this, "订单不存在");
                        finish();
                    } else {
                        initView();
                        progress.setVisibility(View.GONE);
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
        btn_get.setOnClickListener(this);
        if (!rootBeen.getGoodsThums().equals("") && !rootBeen.getGoodsThums().equals("null")) {
            Glide.with(this).load(rootBeen.getGoodsThums().trim()).placeholder(R.drawable.imgloading).into(iv_goodsIcon);
        }
        if (!rootBeen.getUserPhoto().equals("") && !rootBeen.getUserPhoto().equals("null")) {
            Glide.with(this).load(rootBeen.getUserPhoto()).into(cv_headshot);
        }
        tv_name.setText(rootBeen.getUserName());
        if (rootBeen.getStatus() == 0) {
            btn_get.setText("领取礼物");
            btn_get.setClickable(true);
        } else if (rootBeen.getStatus() == 1) {
            btn_get.setText("已被领取");
            btn_get.setClickable(false);
        } else if (rootBeen.getStatus() == 2) {
            btn_get.setText("已领取");
            btn_get.setClickable(false);
        } else if (rootBeen.getStatus() == 3) {
            btn_get.setText("查看物流");
            btn_get.setClickable(true);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                finish();
                break;

            case R.id.btn_get:
                if (rootBeen.getStatus() == 0) {
                    Intent intent = new Intent(this, AddressActivity.class);
                    intent.putExtra("type", 3);
                    startActivityForResult(intent, 1);
                } else if (rootBeen.getStatus() == 3) {
                    Intent intent = new Intent(this, DeliverActivity.class);
                    intent.putExtra("num", rootBeen.getLogisticsNum());
                    intent.putExtra("com", rootBeen.getLogisticsComs());
                    intent.putExtra("name", rootBeen.getLogisticsName());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            progress_update_whitebg.setVisibility(View.VISIBLE);
            userName = data.getExtras().getString("name");
            userPhone = data.getExtras().getString("phone");
            address = data.getExtras().getString("province")+data.getExtras().getString("city")+data.getExtras().getString("area")+data.getExtras().getString("address");
            areaId1 = data.getExtras().getString("pid");
            areaId2 = data.getExtras().getString("cid");
            areaId3 = data.getExtras().getString("aid");
            new Thread() {
                @Override
                public void run() {
                    postData(MyConfig.URL_POST_GIFT_GET);
                }
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提交数据
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("touid", uid);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("userPhone", userPhone);
        params.addBodyParameter("address", address);
        params.addBodyParameter("areaId1", areaId1);
        params.addBodyParameter("areaId2", areaId2);
        params.addBodyParameter("areaId3", areaId3);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_whitebg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("2")) {
                            ToastUtil.showToast(GiftGetActivity.this, "已经领取过了");
                        } else if (result.equals("1")) {
                            ToastUtil.showToast(GiftGetActivity.this, "领取成功");
                            btn_get.setText("已领取");
                            btn_get.setClickable(false);
                        } else {
                            ToastUtil.showToast(GiftGetActivity.this, "领取失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        progress_update_whitebg.setVisibility(View.GONE);
                        ToastUtil.showToast(GiftGetActivity.this, "领取失败");
                    }
                });
    }
}
