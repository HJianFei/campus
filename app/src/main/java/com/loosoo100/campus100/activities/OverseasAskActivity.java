package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yang 海归发起提问activity
 */
public class OverseasAskActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回
    @ViewInject(R.id.rl_publish)
    private RelativeLayout rl_publish; // 发布
    @ViewInject(R.id.et_question)
    private EditText et_question; // 内容
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg; // 加载动画

    private String question = "";
    private String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overseas_ask);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        // 更改状态栏字体颜色为黑色
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        rl_publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                if (!et_question.getText().toString().trim().equals("")) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否退出编辑");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    OverseasAskActivity.this.finish();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.rl_publish:
                question = et_question.getText().toString().trim();
                if (question.equals("")) {
                    ToastUtil.showToast(OverseasAskActivity.this, "输入内容不能为空");
                    return;
                }
                rl_publish.setClickable(false);
                progress_update_whitebg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postAsk(MyConfig.URL_POST_OVERSEAS_QUESTION_PUBLISH);
                    }

                    ;
                }.start();
                break;

        }
    }

    /**
     * 提交数据,发表提问
     */
    private void postAsk(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("ques", question);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_whitebg.setVisibility(View.GONE);
                        String result = "";
                        rl_publish.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            rl_publish.setClickable(true);
                            ToastUtil.showToast(OverseasAskActivity.this, "操作失败");
                        } else {
                            ToastUtil.showToast(OverseasAskActivity.this, "发布成功");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        rl_publish.setClickable(true);
                        progress_update_whitebg.setVisibility(View.GONE);
                        ToastUtil.showToast(OverseasAskActivity.this, "操作失败");
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!et_question.getText().toString().trim().equals("")) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否退出编辑");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                OverseasAskActivity.this.finish();
                            }
                        });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
