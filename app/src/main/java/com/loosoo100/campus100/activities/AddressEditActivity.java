package com.loosoo100.campus100.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventAddress;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.togglebutton.ToggleButton;
import com.loosoo100.campus100.view.togglebutton.ToggleButton.OnToggleChanged;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * @author yang 编辑地址activity
 */
public class AddressEditActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private View rl_back;
    @ViewInject(R.id.et_name)
    private EditText et_name; // 姓名
    @ViewInject(R.id.et_telephone)
    private EditText et_telephone; // 电话
    // @ViewInject(R.id.et_zipCode)
    // private EditText et_zipCode; // 邮编
    @ViewInject(R.id.et_address)
    private EditText et_address; // 地址
    // @ViewInject(R.id.radioGroup)
    // private RadioGroup radioGroup; // 单选组
    // @ViewInject(R.id.rb_male)
    // private RadioButton rb_male; // 男
    // @ViewInject(R.id.rb_female)
    // private RadioButton rb_female; // 女
    @ViewInject(R.id.btn_save)
    private Button btn_save; // 保存
    @ViewInject(R.id.rl_delete)
    private RelativeLayout rl_delete; // 删除
    @ViewInject(R.id.ll_address)
    private LinearLayout ll_address; // 所在地区
    @ViewInject(R.id.tv_address)
    private TextView tv_address; // 所在地区
    @ViewInject(R.id.ll_school)
    private LinearLayout ll_school; // 学校
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 学校
    @ViewInject(R.id.toggleButton)
    private ToggleButton toggleButton; // 是否选中默认
    @ViewInject(R.id.btn_store)
    private Button btn_store; // 小卖部
    @ViewInject(R.id.btn_gift)
    private Button btn_gift; // 礼物盒子
    @ViewInject(R.id.ll_school_root)
    private LinearLayout ll_school_root; // 选择学校
    @ViewInject(R.id.ll_city_root)
    private LinearLayout ll_city_root; // 选择地区
    @ViewInject(R.id.tv_remark_root)
    private TextView tv_remark_root; // 备注

    // private String zipCode; // 邮编
    private String school = ""; // 学校
    private String sid = ""; // 学校ID
    private String name = ""; // 姓名
    private String telephone = ""; // 电话
    private String userName = "";
    // private String sex = ""; // 保存选中男或女
    private String userPhone = "";
    // private String postCode = "";
    private String address = "";
    private String addressId = "";
//    private String result = "";
//    private String result2 = "";
    private boolean gift = false; // 是否礼物盒子
    private String isDefault = "0"; // 1默认地址 0不是默认地址

    private String province = "";
    private String provinceID = "";
    private String city = "";
    private String cityID = "";
    private String area = "";
    private String areaID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        userName = getIntent().getExtras().getString("userName");
        // sex = getIntent().getExtras().getString("sex");
        userPhone = getIntent().getExtras().getString("userPhone");
        // postCode = getIntent().getExtras().getString("postCode");
        address = getIntent().getExtras().getString("address");
        addressId = getIntent().getExtras().getString("addressId");
        isDefault = getIntent().getExtras().getString("isDefault");
        province = getIntent().getExtras().getString("province");
        provinceID = getIntent().getExtras().getString("pid");
        city = getIntent().getExtras().getString("city");
        cityID = getIntent().getExtras().getString("cid");
        area = getIntent().getExtras().getString("area");
        areaID = getIntent().getExtras().getString("aid");
        if (!getIntent().getExtras().getString("school").equals("null")
                && !getIntent().getExtras().getString("school").equals("")) {
            school = getIntent().getExtras().getString("school");
            sid = getIntent().getExtras().getString("sid");
        }

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        rl_delete.setOnClickListener(this);
        ll_school.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        btn_store.setOnClickListener(this);
        btn_gift.setOnClickListener(this);

        et_name.setText(userName);
        et_telephone.setText(userPhone);
        // et_zipCode.setText(postCode);
        et_address.setText(address);
        tv_school.setText(school);

        if (!province.equals("") && !city.equals("") && !area.equals("")
                && !province.equals("null") && !city.equals("null")
                && !area.equals("null")) {
            if (province.equals(city)) {
                tv_address.setText(city + " " + area);
            } else {
                tv_address.setText(province + " " + city + " " + area);
            }
        }

        // if (sex.equals("0")) {
        // rb_male.setChecked(true);
        // } else {
        // rb_female.setChecked(true);
        // }

        if (school.equals("")) {
            gift = true;
            btn_gift.setTextColor(getResources().getColor(R.color.red_fd3c49));
            btn_store.setTextColor(getResources().getColor(R.color.black));
            ll_school_root.setVisibility(View.GONE);
            ll_city_root.setVisibility(View.VISIBLE);
            tv_remark_root.setVisibility(View.GONE);
        } else {
            gift = false;
            btn_store.setTextColor(getResources().getColor(R.color.red_fd3c49));
            btn_gift.setTextColor(getResources().getColor(R.color.black));
            ll_school_root.setVisibility(View.VISIBLE);
            ll_city_root.setVisibility(View.GONE);
            tv_remark_root.setVisibility(View.VISIBLE);
        }

        // radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(RadioGroup group, int checkedId) {
        // if (checkedId == rb_male.getId()) {
        // sex = "0";
        // } else if (checkedId == rb_female.getId()) {
        // sex = "1";
        // }
        // }
        // });

        // 切换无动画
        // toggleButton.toggle(true);
        // 开关切换事件
        toggleButton.setOnToggleChanged(new OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    toggleButton.setToggleOn();
                    isDefault = "1";
                } else {
                    toggleButton.setToggleOff();
                    isDefault = "0";
                }
            }
        });
        if (isDefault.equals("1")) {
            toggleButton.setToggleOn();
        } else {
            toggleButton.setToggleOff();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.btn_store:
                gift = false;
                btn_store.setTextColor(getResources().getColor(R.color.red_fd3c49));
                btn_gift.setTextColor(getResources().getColor(R.color.black));
                ll_school_root.setVisibility(View.VISIBLE);
                tv_remark_root.setVisibility(View.VISIBLE);
                ll_city_root.setVisibility(View.GONE);
                break;

            case R.id.btn_gift:
                gift = true;
                btn_gift.setTextColor(getResources().getColor(R.color.red_fd3c49));
                btn_store.setTextColor(getResources().getColor(R.color.black));
                ll_school_root.setVisibility(View.GONE);
                tv_remark_root.setVisibility(View.GONE);
                ll_city_root.setVisibility(View.VISIBLE);
                break;

            // 单击保存时
            case R.id.btn_save:
                name = et_name.getText().toString();
                telephone = et_telephone.getText().toString();
                // zipCode = et_zipCode.getText().toString();
                address = et_address.getText().toString();
                if (!gift && school.equals("")) {
                    ToastUtil.showToast(AddressEditActivity.this, "请将信息填写完整");
                    return;
                }
                if (gift) {
                    if (provinceID.equals("") || cityID.equals("")
                            || areaID.equals("") || provinceID.equals("0")
                            || cityID.equals("0") || areaID.equals("0")) {
                        ToastUtil.showToast(AddressEditActivity.this, "请将信息填写完整");
                        return;
                    }
                }
                // 判断是否填写完整，是则修改，否则提示
                if (name.equals("") || telephone.equals("") || address.equals("")) {
                    ToastUtil.showToast(AddressEditActivity.this, "请将信息填写完整");
                } else {
                    new Thread() {
                        public void run() {
                            postData(MyConfig.URL_POST_ADDRESS_EDIT);
                        }

                        ;
                    }.start();
                }

                break;

            case R.id.ll_address:
                Intent intent2 = new Intent(this, AddressChoiceActivity01.class);
                startActivity(intent2);
                break;

            case R.id.ll_school:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.rl_delete:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否确认删除");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_ADDRESS_DELETE
                                                + addressId);
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            school = data.getExtras().getString(MyConfig.SCHOOL_SEARCH);
            sid = data.getExtras().getString(MyConfig.SCHOOL_SEARCH_ID);
            tv_school.setText(school);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提交数据
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", addressId);
        params.addBodyParameter("userName", name);
        params.addBodyParameter("userPhone", telephone);
        params.addBodyParameter("address", address);
        // params.addBodyParameter("postCode", zipCode);
        // params.addBodyParameter("userSex", sex);
        if (!gift) {
            params.addBodyParameter("sName", school);
            params.addBodyParameter("sid", sid);
        } else {
            params.addBodyParameter("sName", "");
            params.addBodyParameter("sid", "");
            params.addBodyParameter("areaId1", provinceID);
            params.addBodyParameter("areaId2", cityID);
            params.addBodyParameter("areaId3", areaID);
        }
        params.addBodyParameter("isDefault", isDefault + "");
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
                        if (result.equals("0")) {
                            ToastUtil.showToast(AddressEditActivity.this, "修改失败");
                        } else if (result.equals("1")) {
                            ToastUtil.showToast(AddressEditActivity.this, "修改成功");
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(AddressEditActivity.this, "修改失败");
                    }
                });
    }

    /**
     * 提交数据
     */
    private void postDeleteData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result="";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(AddressEditActivity.this, "删除失败");
                        } else if (result.equals("1")) {
                            ToastUtil.showToast(AddressEditActivity.this, "删除成功");
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("arg1", arg1.toString());
                        ToastUtil.showToast(AddressEditActivity.this, "删除失败");
                    }
                });
    }

    public void onEventMainThread(MEventAddress event) {
        provinceID = event.getPid();
        cityID = event.getCid();
        areaID = event.getAid();
        if (event.getProvince().equals(event.getCity())) {
            tv_address.setText(event.getCity() + " " + event.getArea());
        } else {
            tv_address.setText(event.getProvince() + " " + event.getCity()
                    + " " + event.getArea());
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
