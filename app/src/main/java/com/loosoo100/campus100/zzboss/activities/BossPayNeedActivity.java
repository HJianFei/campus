package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.loosoo100.campus100.anyevent.MEventPayChoiceIsSelect;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 需求最终支付activity
 */
public class BossPayNeedActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.root_pay)
    private LinearLayout root_pay; // 整个支付界面
    @ViewInject(R.id.iv_payIcon)
    private ImageView iv_payIcon; // 支付方式图标
    @ViewInject(R.id.ll_payOtherWay)
    private LinearLayout ll_payOtherWay; // 其它支付方式
    @ViewInject(R.id.tv_moneyPay)
    private TextView tv_moneyPay; // 金额
    @ViewInject(R.id.tv_moneyNotEnoughPay)
    private TextView tv_moneyNotEnoughPay; // 余额不足
    @ViewInject(R.id.rl_close_pay)
    private RelativeLayout rl_close_pay; // 关闭按钮
    @ViewInject(R.id.iv_pass1)
    private ImageView iv_pass1; // 密码1
    @ViewInject(R.id.iv_pass2)
    private ImageView iv_pass2; // 密码2
    @ViewInject(R.id.iv_pass3)
    private ImageView iv_pass3; // 密码3
    @ViewInject(R.id.iv_pass4)
    private ImageView iv_pass4; // 密码4
    @ViewInject(R.id.iv_pass5)
    private ImageView iv_pass5; // 密码5
    @ViewInject(R.id.iv_pass6)
    private ImageView iv_pass6; // 密码6
    @ViewInject(R.id.et_pass)
    private EditText et_pass; // 密码输入
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    private View[] passwords; // 存放密码

    private boolean isPay = false;

    private InputMethodManager imm;

    private int index = 1; // 当前选择的支付方式，1钱包支付，2微信支付，3支付宝支付
    private float money; // 要支付的金额
    private float totalMoney; // 用户余额

    private String cuid = ""; // 公司ID
    private String aid = ""; // 活动ID
    private String cid = ""; // 社团ID
    private String result = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress.setVisibility(View.GONE);
            if (result.equals("3")) {
                isPay = true;
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                ToastUtil.showToast(BossPayNeedActivity.this, "支付成功");
                finish();
            } else if (result.equals("-5")) {
                imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
                ToastUtil.showToast(BossPayNeedActivity.this, "余额不足");
                // 0.3秒后重置密码框
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_pass.setText("");
                        for (int j = 0; j < 6; j++) {
                            passwords[j].setVisibility(View.GONE);
                        }
                    }
                }, 300);
            } else if (result.equals("4")) {
                imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
                ToastUtil.showToast(BossPayNeedActivity.this, "密码错误");
                // 0.3秒后重置密码框
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_pass.setText("");
                        for (int j = 0; j < 6; j++) {
                            passwords[j].setVisibility(View.GONE);
                        }
                    }
                }, 300);
            } else {
                imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
                ToastUtil.showToast(BossPayNeedActivity.this, "支付失败");
                // 0.3秒后重置密码框
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        et_pass.setText("");
                        for (int j = 0; j < 6; j++) {
                            passwords[j].setVisibility(View.GONE);
                        }
                    }
                }, 300);
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_pay_need);
        ViewUtils.inject(this);

        EventBus.getDefault().register(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");

        money = getIntent().getExtras().getFloat("money");
        aid = getIntent().getExtras().getString("aid");
        cid = getIntent().getExtras().getString("cid");
        totalMoney = Float.valueOf(getSharedPreferences(UserInfoDB.USERTABLE,
                MODE_PRIVATE).getString(UserInfoDB.MONEY, "0"));

        initView();

    }

    private void initView() {
        ll_payOtherWay.setOnClickListener(this);
        rl_close_pay.setOnClickListener(this);

        tv_moneyPay.setText("￥" + money);

        if (money > totalMoney) {
            tv_moneyNotEnoughPay.setVisibility(View.VISIBLE);
        } else {
            tv_moneyNotEnoughPay.setVisibility(View.GONE);
        }

        passwords = new View[]{iv_pass1, iv_pass2, iv_pass3, iv_pass4,
                iv_pass5, iv_pass6};
        et_pass.addTextChangedListener(new EditChangedListener());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 支付其它方式
            case R.id.ll_payOtherWay:
                Intent paychoiceIntent = new Intent(BossPayNeedActivity.this,
                        BossPayChoiceNeedActivity.class);
                paychoiceIntent.putExtra("money", money);
                paychoiceIntent.putExtra("aid", aid);
                paychoiceIntent.putExtra("cid", cid);
                startActivity(paychoiceIntent);
                // finish();
                break;

            // 支付关闭按钮
            case R.id.rl_close_pay:
                finish();
                break;

        }
    }

    /**
     * 密码输入监听
     */
    class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            for (int j = s.length(); j < 6; j++) {
                passwords[j].setVisibility(View.GONE);
            }
            for (int i = 0; i < s.length(); i++) {
                passwords[i].setVisibility(View.VISIBLE);
                if (i == 5) {
                    if (money > totalMoney) {
                        imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
                        ToastUtil.showToast(BossPayNeedActivity.this, "余额不足");
                        // 0.3秒后重置密码框
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_pass.setText("");
                                for (int j = 0; j < 6; j++) {
                                    passwords[j].setVisibility(View.GONE);
                                }
                            }
                        }, 300);
                        return;
                    }
                    progress.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {
                            postOrderIdXUtils(MyConfig.URL_POST_PAY_PW);
                        }

                        ;
                    }.start();

                }
            }

        }
    }

    /**
     * 提交用户输入的密码和金额，判断是否正确或金额是否足够
     *
     * @param uploadHost
     */
    private void postOrderIdXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("payPwd", et_pass.getText().toString());
        params.addBodyParameter("userId", cuid);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("money", money + "");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
                        progress.setVisibility(View.GONE);
                        ToastUtil.showToast(BossPayNeedActivity.this, "支付失败");
                        // 0.3秒后重置密码框
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_pass.setText("");
                                for (int j = 0; j < 6; j++) {
                                    passwords[j].setVisibility(View.GONE);
                                }
                            }
                        }, 300);
                    }
                });
    }

    public void onEventMainThread(MEventPayChoiceIsSelect event) {
        if (event.getFlag()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (!isPay) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        super.onDestroy();
    }

}
