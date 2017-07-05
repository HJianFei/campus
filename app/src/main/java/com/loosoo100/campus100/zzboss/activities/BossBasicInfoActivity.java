package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yang 基本信息activity
 */
public class BossBasicInfoActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private View rl_back;
    @ViewInject(R.id.et_name)
    private EditText et_name; // 名字
    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup; // 性别
    @ViewInject(R.id.rb_female)
    private RadioButton rb_female; // 女
    @ViewInject(R.id.rb_male)
    private RadioButton rb_male; // 男
    @ViewInject(R.id.et_qq)
    private EditText et_qq; // 微信号
    @ViewInject(R.id.btn_change)
    private Button btn_change; // 修改按钮

    private String name = "";
    private String sex = "";
    private String weixin = "";

    private String cuid = "";
    private String result = ""; // 提交数据返回结果

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (result.equals("1")) {
                UserInfoDB.setUserInfo(BossBasicInfoActivity.this,
                        UserInfoDB.NAME, name);
                UserInfoDB.setUserInfo(BossBasicInfoActivity.this,
                        UserInfoDB.WEIXIN, weixin);
                UserInfoDB.setUserInfo(BossBasicInfoActivity.this,
                        UserInfoDB.SEX, sex);
                ToastUtil.showToast(BossBasicInfoActivity.this, "修改成功");
                BossBasicInfoActivity.this.finish();
            } else {
                ToastUtil.showToast(BossBasicInfoActivity.this, "操作失败");
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_basic_info);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");
        weixin = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.WEIXIN, "");
        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_change.setOnClickListener(this);

        et_name.setText(getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.NAME, ""));
        et_qq.setText(getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.WEIXIN, ""));
        if (getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(
                UserInfoDB.SEX, "1").equals("1")) {
            rb_female.setChecked(true);
            sex = "1";
        } else {
            rb_male.setChecked(true);
            sex = "0";
        }

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_female.getId()) {
                    rb_female.setChecked(true);
                    sex = "1";
                } else if (checkedId == rb_male.getId()) {
                    rb_male.setChecked(true);
                    sex = "0";
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

            case R.id.btn_change:
                name = et_name.getText().toString().trim();
                weixin = et_qq.getText().toString().trim();
                if (name.equals("") || weixin.equals("")) {
                    ToastUtil.showToast(BossBasicInfoActivity.this, "请将信息填写完整");
                    return;
                } else {
                    new Thread() {
                        public void run() {
                            postData(MyConfig.URL_POST_USERINFO_EDIT_BOSS);
                        }

                        ;
                    }.start();
                }
                break;

        }
    }

    /**
     * 修改基本信息
     *
     * @param uploadHost
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("trueName", name);
        params.addBodyParameter("sex", sex);
        params.addBodyParameter("cuid", cuid);
        params.addBodyParameter("weixin", weixin);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
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
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(BossBasicInfoActivity.this, "操作失败");
                    }
                });
    }

}
