package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventGiftNotEnough;
import com.loosoo100.campus100.anyevent.MEventGiftTogNotEnough;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 凑一凑支付activity
 */
public class GiftTogetherPayActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private View rl_back;
    @ViewInject(R.id.btn_ok)
    private Button btn_ok; // 确认付款

    @ViewInject(R.id.tv_money)
    private TextView tv_money; // 支付上限的金额
    @ViewInject(R.id.et_money)
    private EditText et_money; // 支付的金额
    @ViewInject(R.id.progress_update_blackbg)
    private RelativeLayout progress_update_blackbg; // 加载动画

    private float money;
    private float payMoney;
    private String oid = "";
    private String uid = "";

    private int type; // 判断是从哪里进入0代表全部订单1代表凑一凑订单3下单后进入
    private int index;

    private int decimal_digits = 2; // 小数位数

    // private String result=""; //当支付金额高于后台所差的金额数时返回的金额数

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            tv_money.setText("￥" + money + "元");
            progress_update_blackbg.setVisibility(View.GONE);
            if (type == 0) {
                EventBus.getDefault().post(
                        new MEventGiftNotEnough(index, money));
            } else if (type == 1) {
                // GiftOrderTogetherActivity.list.get(index).setPriceNotEnough(
                // money);
                // GiftOrderTogetherActivity.adapter.notifyDataSetChanged();
                EventBus.getDefault().post(
                        new MEventGiftTogNotEnough(index, money));
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_together_pay);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        money = getIntent().getExtras().getFloat("money");
        oid = getIntent().getExtras().getString("oid");
        type = getIntent().getExtras().getInt("type");
        index = getIntent().getExtras().getInt("index");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_money.setText("￥" + money + "元");

        /**
         * 设置小数位数控制
         */
        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - decimal_digits;
                    if (diff > 0) {
                        return source.subSequence(start, end - diff);
                    }
                }
                return null;
            }
        };

        // et_money.addTextChangedListener(mTextWatcher);
        // 控制输入框的小数位和长度,这里长度暂时设置为10
        et_money.setFilters(new InputFilter[]{lengthfilter,
                new InputFilter.LengthFilter(20)});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.btn_ok:
                // 没有输入金额时不执行下一步
                if (et_money.getText().toString().equals("")) {
                    ToastUtil.showToast(this, "请输入金额");
                    return;
                }
                payMoney = Float.valueOf(et_money.getText().toString());
                if (payMoney == 0) {
                    ToastUtil.showToast(this, "输入金额不能为0");
                    return;
                }
                if (payMoney > money) {
                    ToastUtil.showToast(this, "亲,不用给多哦");
                    return;
                }
                progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        // 判断支付金额是否满足订单所应付的金额数
                        postDataIsOK(MyConfig.URL_POST_GIFT_ORDER_TOGETHER_PAY);
                    }

                    ;
                }.start();

                break;
        }
    }

    /**
     * 提交数据,判断支付的金额是否超过后台的待付款金额
     */
    private void postDataIsOK(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("money", payMoney + "");
        params.addBodyParameter("isOK", "0");
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
                            Intent intent = new Intent(
                                    GiftTogetherPayActivity.this,
                                    PayActivity.class);
                            intent.putExtra("money", payMoney);
                            intent.putExtra("orderId", oid);
                            intent.putExtra("cid", "0");
                            intent.putExtra("type", "gcou");
                            startActivityForResult(intent, 1);
                        } else {
                            if (Float.valueOf(result) <= 0) {
                                ToastUtil.showToast(GiftTogetherPayActivity.this, "订单已凑完了");
                            } else {
                                ToastUtil.showToast(GiftTogetherPayActivity.this, "您支付金额太高了,还差\" + result + \"元就凑够了");
                            }
                            // 更改支付金额上限
                            money = Float.valueOf(result);
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(GiftTogetherPayActivity.this, "提交失败");
                        progress_update_blackbg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 提交数据,提交支付金额
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("oid", oid);
        params.addBodyParameter("money", payMoney + "");
        params.addBodyParameter("isOK", "1");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        progress_update_blackbg.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(GiftTogetherPayActivity.this, "支付成功");
                            if (type == 0) {
                                EventBus.getDefault().post(
                                        new MEventGiftNotEnough(index, money
                                                - payMoney));
                            } else if (type == 1) {
                                EventBus.getDefault().post(
                                        new MEventGiftTogNotEnough(index, money));
                            }
                            // 支付成功后提醒详单详情或订单列表更新数据
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            GiftTogetherPayActivity.this.finish();
                            progress_update_blackbg.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showToast(GiftTogetherPayActivity.this, "支付失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(GiftTogetherPayActivity.this, "支付失败");
                        progress_update_blackbg.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            // 通知凑一凑和全部订单列表更新
            GiftOrderTogetherActivity.isUpdate = true;
            GiftOrderActivity.isUpdate = true;
            new Thread() {
                public void run() {
                    postData(MyConfig.URL_POST_GIFT_ORDER_TOGETHER_PAY);
                }

                ;
            }.start();
        } else if (resultCode == RESULT_CANCELED && requestCode == 1) {
            // 通知凑一凑和全部订单列表更新
            GiftOrderTogetherActivity.isUpdate = true;
            GiftOrderActivity.isUpdate = true;
            progress_update_blackbg.setVisibility(View.GONE);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            GiftTogetherPayActivity.this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
