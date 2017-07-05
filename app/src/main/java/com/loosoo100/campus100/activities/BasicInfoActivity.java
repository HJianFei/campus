package com.loosoo100.campus100.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.loosoo100.campus100.anyevent.MEventStoreCart;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.fragments.StoreFragment;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * @author yang 基本信息activity
 */
public class BasicInfoActivity extends Activity implements OnClickListener {
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
    // @ViewInject(R.id.ll_school)
    // private LinearLayout ll_school; // 学校选择
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 学校名字
    @ViewInject(R.id.et_studentID)
    private EditText et_studentID; // 学生证号
    @ViewInject(R.id.et_weixin)
    private EditText et_weixin; // 微信号
    @ViewInject(R.id.btn_change)
    private Button btn_change; // 修改按钮

    private String name = "";
    private String sex = "";
    private String school = "";
    private String weixin = "";
//    private String schoolId = "";
    private String stuNO = "";

    private String uid = "";
    private String result = ""; // 提交数据返回结果

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (result.equals("1")) {
                SharedPreferences sp = getSharedPreferences(
                        UserInfoDB.USERTABLE, MODE_PRIVATE);
                Editor edit = sp.edit();
                edit.putString(UserInfoDB.NAME, name);
//                edit.putString(UserInfoDB.SCHOOL, school);
//                edit.putString(UserInfoDB.SCHOOL_ID, schoolId);
                edit.putString(UserInfoDB.WEIXIN, weixin);
//                edit.putString(UserInfoDB.SCHOOL_PICTURE, school);
//                edit.putString(UserInfoDB.SCHOOL_ID_PICTURE, schoolId);
//                edit.putString(UserInfoDB.SCHOOL_COMM, school);
//                edit.putString(UserInfoDB.SCHOOL_ID_COMM, schoolId);
//                edit.putString(UserInfoDB.SCHOOL_STORE, school);
//                edit.putString(UserInfoDB.SCHOOL_ID_STORE, schoolId);
                edit.putString(UserInfoDB.STUDENT_ID, stuNO);
                edit.putString(UserInfoDB.SEX, sex);
                edit.commit();
//				StoreFragment.isChange = true;
//                EventBus.getDefault().post(new MEventStoreCart(true));
                ToastUtil.showToast(BasicInfoActivity.this,"修改成功");
                BasicInfoActivity.this.finish();
            } else {
                ToastUtil.showToast(BasicInfoActivity.this,"操作失败");
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        school = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL, "");
        weixin = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.WEIXIN, "");
//        schoolId = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//                .getString(UserInfoDB.SCHOOL_ID, "");
        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        // ll_school.setOnClickListener(this);
        btn_change.setOnClickListener(this);

        et_name.setText(getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.NAME, ""));
        et_weixin.setText(getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.WEIXIN, ""));
        tv_school.setText(school);
        et_studentID.setText(getSharedPreferences(UserInfoDB.USERTABLE,
                MODE_PRIVATE).getString(UserInfoDB.STUDENT_ID, ""));
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
            //
            // case R.id.ll_school:
            // Intent intent = new Intent(this, SearchActivity.class);
            // startActivityForResult(intent, school_code);
            // break;

            case R.id.btn_change:
                name = et_name.getText().toString().trim();
                stuNO = et_studentID.getText().toString().trim();
                weixin = et_weixin.getText().toString().trim();
                if (name.equals("")
                        || stuNO.equals("") || weixin.equals("")) {
                    ToastUtil.showToast(this,"请将信息填写完整");
                    return;
                } else {
                    new Thread() {
                        public void run() {
                            postData(MyConfig.URL_POST_USERINFO_EDIT);
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
        params.addBodyParameter("userSex", sex);
//        params.addBodyParameter("school_shopid", schoolId);
//        params.addBodyParameter("school_name", school);
        params.addBodyParameter("stuNo", stuNO);
        params.addBodyParameter("uid", uid);
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
                        LogUtils.d("arg1",arg1.toString());
                    }
                });
    }

}
