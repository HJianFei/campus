package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.loosoo100.campus100.adapters.CommunityDepAdapter;
import com.loosoo100.campus100.beans.CommunityDepInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yang 创建社团activity
 */
public class CommunityCreateActivity extends Activity implements OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty;
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;

    @ViewInject(R.id.et_communityName)
    private EditText et_communityName; // 社团名称
    @ViewInject(R.id.et_name)
    private EditText et_name; // 姓名
    @ViewInject(R.id.et_phone)
    private EditText et_phone; // 电话
    @ViewInject(R.id.et_weixin)
    private EditText et_weixin; // 微信
    @ViewInject(R.id.et_qq)
    private EditText et_qq; // QQ
    @ViewInject(R.id.et_recommend)
    private EditText et_recommend; // 推荐人

    @ViewInject(R.id.btn_school_attribute)
    private Button btn_school_attribute; // 校级社团
    @ViewInject(R.id.btn_major_attribute)
    private Button btn_major_attribute; // 院级社团
    @ViewInject(R.id.btn_interest_attribute)
    private Button btn_interest_attribute; // 兴趣社团

    @ViewInject(R.id.btn_sports)
    private Button btn_sports; // 体育竞技
    @ViewInject(R.id.btn_arts)
    private Button btn_arts; // 文化艺术
    @ViewInject(R.id.btn_heart)
    private Button btn_heart; // 爱心公益
    @ViewInject(R.id.btn_practice)
    private Button btn_practice; // 社会实践
    @ViewInject(R.id.btn_science)
    private Button btn_science; // 学术科研
    @ViewInject(R.id.btn_interest)
    private Button btn_interest; // 兴趣爱好
    @ViewInject(R.id.btn_others)
    private Button btn_others; // 其它

    @ViewInject(R.id.gridview)
    private GridView gridview; // 社团架构
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    private CommunityDepAdapter depAdapter;
    private List<CommunityDepInfo> tvList = new ArrayList<CommunityDepInfo>();

    @ViewInject(R.id.btn_create)
    private Button btn_create; // 创建
    private Intent intent = new Intent();

    private String attr = ""; // 社团属性
    private String type = ""; // 社团类型
    private String dep = ""; // 社团架构

    private String communityName = "";
    private String name = "";
    private String phone = "";
    private String weixin = "";
    private String qq = "";
    private String recommend = "";
    private String sid = "";
    private String uid = "";

    private String postResult = ""; // post返回的数据
    private CommunityDepInfo depInfo;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress.setVisibility(View.GONE);
            if (postResult.equals("0")) {
                btn_create.setClickable(true);
                ToastUtil.showToast(CommunityCreateActivity.this, "社团名称已存在，请修改");
            } else if (postResult.equals("-1")) {
                btn_create.setClickable(true);
                ToastUtil.showToast(CommunityCreateActivity.this, "您有社团正在申请中");
            } else if (postResult.equals("-2")) {
                btn_create.setClickable(true);
                ToastUtil.showToast(CommunityCreateActivity.this, "您已创建过社团");
            } else if (postResult.equals("-3")) {
                btn_create.setClickable(true);
                ToastUtil.showToast(CommunityCreateActivity.this, "您已创建过小卖部不能再创建社团了");
            } else if (postResult.equals("-4")) {
                CustomDialog.Builder builder = new CustomDialog.Builder(CommunityCreateActivity.this);
                builder.setMessage("请先完善个人资料");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CommunityCreateActivity.this, BasicInfoActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();
            } else {
                new Thread() {
                    public void run() {
                        postIsAgentExist(MyConfig.URL_POST_ISAGENTEXITS);
                    }
                }.start();

//				intent.setClass(CommunityCreateActivity.this, CommunityPerfectingActivity.class);
//				intent.putExtra("sn", postResult);
//				intent.putExtra("cname", communityName);
//				intent.putExtra("attr", attr);
//				intent.putExtra("type", type);
//				intent.putExtra("dep", dep);
//				intent.putExtra("uname", name);
//				intent.putExtra("phone", phone);
//				intent.putExtra("weixin", weixin);
//				intent.putExtra("qq", qq);
//				intent.putExtra("tuijian", recommend);
//				startActivity(intent);
//				finish();
            }
        }

        ;
    };
    private Handler handlerPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            new Thread() {
                public void run() {
                    postData(MyConfig.URL_POST_COMMUNITY_CREATE);
                }

                ;
            }.start();
        }

        ;
    };
    private Handler handlerNoPerfect = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CustomDialog.Builder builder = new CustomDialog.Builder(CommunityCreateActivity.this);
            builder.setMessage("请先完善个人资料");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(CommunityCreateActivity.this, BasicInfoActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("否", null);
            builder.create().show();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_create);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);

        // activity = CommunityCreateActivity.this;
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");

        initView();

        initGridView();

    }

    private void initGridView() {
        String[] texts = new String[]{"主席团", "宣传部", "组织部", "技术部", "秘书部", "文娱部", "+"};
        for (int i = 0; i < texts.length; i++) {
            depInfo = new CommunityDepInfo();
            depInfo.setChecked(false);
            depInfo.setText(texts[i]);
            tvList.add(depInfo);
        }
        depAdapter = new CommunityDepAdapter(this, tvList);
        gridview.setAdapter(depAdapter);
        MyUtils.setGridViewHeight(gridview, 0, 4);

        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == tvList.size() - 1) {
                    Intent intent = new Intent(CommunityCreateActivity.this, CommunityDepAddActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (!tvList.get(position).isChecked()) {
                        tvList.get(position).setChecked(true);
                    } else {
                        tvList.get(position).setChecked(false);
                    }
                    depAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initView() {
        rl_back.setOnClickListener(this);

        btn_school_attribute.setOnClickListener(this);
        btn_major_attribute.setOnClickListener(this);
        btn_interest_attribute.setOnClickListener(this);

        btn_sports.setOnClickListener(this);
        btn_arts.setOnClickListener(this);
        btn_heart.setOnClickListener(this);
        btn_practice.setOnClickListener(this);
        btn_science.setOnClickListener(this);
        btn_interest.setOnClickListener(this);
        btn_others.setOnClickListener(this);

        btn_create.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                if (!et_communityName.getText().toString().trim().equals("")
                        || !et_name.getText().toString().trim().equals("")
                        || !et_phone.getText().toString().trim().equals("")
                        || !et_weixin.getText().toString().trim().equals("")
                        || !et_qq.getText().toString().trim().equals("")
                        || !et_recommend.getText().toString().trim().equals("")) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否退出编辑");
                    builderDel.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommunityCreateActivity.this.finish();
                        }
                    });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            // 创建
            case R.id.btn_create:
                communityName = et_communityName.getText().toString().trim();
                name = et_name.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                weixin = et_weixin.getText().toString().trim();
                qq = et_qq.getText().toString().trim();
                recommend = et_recommend.getText().toString().trim();
                dep = "";
                for (int i = 0; i < tvList.size(); i++) {
                    if (tvList.get(i).isChecked()) {
                        dep = dep + tvList.get(i).getText() + ",";
                    }
                }
                if (dep.length() > 0) {
                    dep = dep.substring(0, dep.length() - 1);
                }
                if (communityName.equals("") || name.equals("") || phone.equals("") || weixin.equals("") || qq.equals("")
                        || attr.equals("") || type.equals("") || dep.equals("")) {
                    ToastUtil.showToast(CommunityCreateActivity.this, "请完善信息后再提交");
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        postIsDataPerfect(MyConfig.URL_POST_IS_INFO_PERFECT);
                    }

                    ;
                }.start();

                break;

		/*
         * 第一组按钮
		 */
            case R.id.btn_school_attribute:
                resetButton01();
                btn_school_attribute.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_school_attribute.setTextColor(getResources().getColor(R.color.red_fd3c49));
                attr = btn_school_attribute.getText().toString();
                break;

            case R.id.btn_major_attribute:
                resetButton01();
                btn_major_attribute.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_major_attribute.setTextColor(getResources().getColor(R.color.red_fd3c49));
                attr = btn_major_attribute.getText().toString();
                break;

            case R.id.btn_interest_attribute:
                resetButton01();
                btn_interest_attribute.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_interest_attribute.setTextColor(getResources().getColor(R.color.red_fd3c49));
                attr = btn_interest_attribute.getText().toString();
                break;

		/*
		 * 第二组按钮
		 */
            case R.id.btn_sports:
                // iv_empty2.setVisibility(View.VISIBLE);
                resetButton02();
                btn_sports.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_sports.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_sports.getText().toString();
                break;

            case R.id.btn_arts:
                resetButton02();
                btn_arts.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_arts.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_arts.getText().toString();
                break;

            case R.id.btn_heart:
                resetButton02();
                btn_heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_heart.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_heart.getText().toString();
                break;

            case R.id.btn_practice:
                resetButton02();
                btn_practice.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_practice.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_practice.getText().toString();
                break;

            case R.id.btn_science:
                resetButton02();
                btn_science.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_science.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_science.getText().toString();
                break;

            case R.id.btn_interest:
                resetButton02();
                btn_interest.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_interest.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_interest.getText().toString();
                break;

            case R.id.btn_others:
                resetButton02();
                btn_others.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_stroke_small));
                btn_others.setTextColor(getResources().getColor(R.color.red_fd3c49));
                type = btn_others.getText().toString();
                break;

        }
    }

    /**
     * 还原第一组按钮
     */
    public void resetButton01() {
        btn_school_attribute.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_school_attribute.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_major_attribute.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_major_attribute.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_interest_attribute.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_interest_attribute.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
    }

    /**
     * 还原第二组按钮
     */
    public void resetButton02() {
        btn_sports.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_sports.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_arts.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_arts.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_heart.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_practice.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_practice.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_science.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_science.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_interest.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_interest.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
        btn_others.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_gray_stroke));
        btn_others.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // btn_add.setText(data.getExtras().getString("dep"));
                CommunityDepInfo depInfo = new CommunityDepInfo();
                depInfo.setChecked(true);
                depInfo.setText(data.getExtras().getString("dep"));
                tvList.add(tvList.size() - 1, depInfo);
                depAdapter.notifyDataSetChanged();
                MyUtils.setGridViewHeight(gridview, 0, 4);
            }
        }
    }

    @Override
    protected void onResume() {
        btn_create.setClickable(true);
        super.onResume();
    }

    /**
     * 判断社团名是否已存在
     *
     * @param uploadHost
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", communityName);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.d("responseInfo",responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    postResult = jsonObject.getString("status");
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
                ToastUtil.showToast(CommunityCreateActivity.this, "提交失败");
                progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!et_communityName.getText().toString().trim().equals("")
                    || !et_name.getText().toString().trim().equals("")
                    || !et_phone.getText().toString().trim().equals("")
                    || !et_weixin.getText().toString().trim().equals("")
                    || !et_qq.getText().toString().trim().equals("")
                    || !et_recommend.getText().toString().trim().equals("")) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否退出编辑");
                builderDel.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommunityCreateActivity.this.finish();
                    }
                });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void postIsAgentExist(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("agentPhone", recommend);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        String result = "";
                        progress.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            intent.setClass(CommunityCreateActivity.this, CommunityPerfectingActivity.class);
                            intent.putExtra("sn", postResult);
                            intent.putExtra("cname", communityName);
                            intent.putExtra("attr", attr);
                            intent.putExtra("type", type);
                            intent.putExtra("dep", dep);
                            intent.putExtra("uname", name);
                            intent.putExtra("phone", phone);
                            intent.putExtra("weixin", weixin);
                            intent.putExtra("qq", qq);
                            intent.putExtra("tuijian", recommend);
                            startActivity(intent);
                            finish();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(CommunityCreateActivity.this, "推荐人不存在");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(CommunityCreateActivity.this, "提交失败");
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 是否完善资料
     */
    private void postIsDataPerfect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "0");
        params.addBodyParameter("uid", uid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.d("responseInfo",responseInfo.result);
                progress.setVisibility(View.GONE);
                String result = "";
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    result = jsonObject.getString("status1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("1")) {
                    handlerPerfect.sendEmptyMessage(0);
                } else {
                    handlerNoPerfect.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.d("error",arg1.toString());
                progress.setVisibility(View.GONE);
                ToastUtil.showToast(CommunityCreateActivity.this, "操作失败");
            }
        });
    }
}
