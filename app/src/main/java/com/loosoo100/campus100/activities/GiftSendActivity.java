package com.loosoo100.campus100.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.loosoo100.campus100.beans.GiftOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.wxapi.WXShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.loosoo100.campus100.R.drawable.activity;
import static com.loosoo100.campus100.R.id.btn_get;
import static com.loosoo100.campus100.beans.BusinessInfo.address;

/**
 * @author yang 礼物说送给TA activity
 */
public class GiftSendActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮

    @ViewInject(R.id.btn_send)
    private Button btn_send; // 送给ta按钮
    @ViewInject(R.id.et_remark)
    private EditText et_remark; // 备注
    @ViewInject(R.id.tv_wordCount)
    private TextView tv_wordCount; // 备注字数提示

    @ViewInject(R.id.tv_goodsName)
    private TextView tv_goodsName; // 商品名
    @ViewInject(R.id.tv_goodsCount)
    private TextView tv_goodsCount; // 商品数量
    @ViewInject(R.id.tv_goodsPrice)
    private TextView tv_goodsPrice; // 商品价格
    @ViewInject(R.id.iv_goodsIcon)
    private ImageView iv_goodsIcon; // 商品图标
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg; // 加载动画

    private GiftOrderInfo giftOrderSend = null;
    private String oid = "";
    private int type;
    private WXShareUtil shareUtil;
    private Bitmap bitmap;

    private boolean isWeiXin = false;
    private String touid = "";

    private Dialog dialog;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (giftOrderSend != null) {
                initView();
                progress.setVisibility(View.GONE);
            }
        }

        ;
    };

    private Handler handlerSend = new Handler() {
        public void handleMessage(android.os.Message msg) {
            isWeiXin = true;
            shareUtil.shareMessageToSession("送礼啦："
                            + et_remark.getText().toString().trim(), giftOrderSend
                            .getGiftOrderGoodsInfo().getGoodsName(),
                    MyConfig.SHARE_SEND + "?oid=" + oid + "&uid="
                            + giftOrderSend.getUserID(), bitmap);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_send);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        progress.setVisibility(View.VISIBLE);
        oid = getIntent().getExtras().getString("oid");
        type = getIntent().getExtras().getInt("type");

        rl_back.setOnClickListener(this);
        shareUtil = new WXShareUtil(this);

        new Thread() {
            public void run() {
                giftOrderSend = GetData
                        .getGiftOrderSend(MyConfig.URL_JSON_GIFT_ORDER_DETAIL
                                + oid);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        dialog = new Dialog(this, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.dialog_weixin_or_friend, null);
        dialog.setContentView(viewDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(viewDialog, params);
        viewDialog.findViewById(R.id.btn_weixin).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread() {
                            public void run() {
                                try {
                                    bitmap = MyUtils.getBitMap(giftOrderSend
                                            .getGiftOrderGoodsInfo().getGoodsThums());
                                    if (bitmap != null) {
                                        bitmap = MyUtils.ResizeBitmap(bitmap, 100);
                                    }
                                    handlerSend.sendEmptyMessage(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        dialog.dismiss();
                    }
                });

        viewDialog.findViewById(R.id.btn_myfriend).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent friendIntent = new Intent(GiftSendActivity.this, MyFriendListActivity.class);
                        startActivityForResult(friendIntent, 1);
                        dialog.dismiss();
                    }
                });


    }

    private void initView() {
        btn_send.setOnClickListener(this);

        Glide.with(this)
                .load(giftOrderSend.getGiftOrderGoodsInfo().getGoodsThums())
                .placeholder(R.drawable.imgloading) // 设置占位图
                .into(iv_goodsIcon);
        tv_goodsName.setText(giftOrderSend.getGiftOrderGoodsInfo()
                .getGoodsName());
        tv_goodsPrice.setText("￥"
                + giftOrderSend.getGiftOrderGoodsInfo().getGoodsPrice()
                * giftOrderSend.getGiftOrderGoodsInfo().getGoodsNums());
        tv_goodsCount.setText("数量x"
                + giftOrderSend.getGiftOrderGoodsInfo().getGoodsNums() + "");

        /**
         * 监听备注输入字数
         */
        et_remark.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_wordCount.setText(s.length() + "/20");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                if (type == 3) {
                    Intent intent = new Intent(this,
                            GiftOrderDetailSendActivity.class);
                    intent.putExtra("index", 0);
                    intent.putExtra("type", 3);
                    intent.putExtra("oid", oid);
                    startActivity(intent);
                }
                this.finish();
                break;

            // 送给ta按钮
            case R.id.btn_send:
                dialog.show();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == 3) {
                Intent intent = new Intent(this,
                        GiftOrderDetailSendActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("type", 3);
                intent.putExtra("oid", oid);
                startActivity(intent);
            }
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        if (isWeiXin) {
            if (type == 3) {
                Intent intent = new Intent(this,
                        GiftOrderDetailSendActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("type", 3);
                intent.putExtra("oid", oid);
                startActivity(intent);
            }
            this.finish();
        }
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            progress_update_whitebg.setVisibility(View.VISIBLE);
            touid = data.getExtras().getString("uid");
            System.out.println("touid--->"+touid);
            new Thread() {
                @Override
                public void run() {
                    postData(MyConfig.URL_POST_GIFT_SEND);
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
        params.addBodyParameter("touid", touid);
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
                        if (result.equals("-1")) {
                            ToastUtil.showToast(GiftSendActivity.this, "已经赠送过了");
                        } else if (result.equals("1")) {
                            ToastUtil.showToast(GiftSendActivity.this, "赠送成功");
                            if (type == 3) {
                                Intent intent = new Intent(GiftSendActivity.this,
                                        GiftOrderDetailSendActivity.class);
                                intent.putExtra("index", 0);
                                intent.putExtra("type", 3);
                                intent.putExtra("oid", oid);
                                startActivity(intent);
                            }
                            GiftSendActivity.this.finish();
                        } else {
                            ToastUtil.showToast(GiftSendActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        progress_update_whitebg.setVisibility(View.GONE);
                        ToastUtil.showToast(GiftSendActivity.this, "操作失败");
                    }
                });
    }
}
