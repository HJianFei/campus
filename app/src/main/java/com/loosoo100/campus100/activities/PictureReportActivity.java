package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.loosoo100.campus100.anyevent.MEventPicReport;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 许愿墙举报activity
 */
public class PictureReportActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.iv_empty2)
    private ImageView iv_empty2; // 空view2
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回
    @ViewInject(R.id.iv_picture)
    private ImageView iv_picture; // 照片
    @ViewInject(R.id.tv_dream)
    private TextView tv_dream; // 愿望
    @ViewInject(R.id.tv_like)
    private TextView tv_like; // 喜欢的人数
    @ViewInject(R.id.tv_dislike)
    private TextView tv_dislike; // 不喜欢的人数
    @ViewInject(R.id.tv_time)
    private TextView tv_time; // 发布的时间
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 加载动画
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more; // 更多
    @ViewInject(R.id.rl_popupwindow)
    private RelativeLayout rl_popupwindow; // 弹出菜单项
    @ViewInject(R.id.rl_report)
    private RelativeLayout rl_report; // 举报
    @ViewInject(R.id.dialog_report)
    private RelativeLayout dialog_report; // 举报界面
    @ViewInject(R.id.btn01)
    private Button btn01;
    @ViewInject(R.id.btn02)
    private Button btn02;
    @ViewInject(R.id.btn03)
    private Button btn03;
    @ViewInject(R.id.btn04)
    private Button btn04;
    @ViewInject(R.id.btn05)
    private Button btn05;
    @ViewInject(R.id.iv_like)
    private ImageView iv_like; // 喜欢
    @ViewInject(R.id.iv_dislike)
    private ImageView iv_dislike; // 不喜欢
    @ViewInject(R.id.ll_dislike)
    private LinearLayout ll_dislike; // 不喜欢
    @ViewInject(R.id.ll_like)
    private LinearLayout ll_like; // 喜欢

    // private PictureWallInfo pictureWallInfo;

    private String wid = ""; // 愿望ID
    private String uid = "";
    private String sid = "";
    private String content = "";

    private int supportCount;
    private int opposeCount;
    private String action;
    private String pictureUrl;
    private String dream;
    private String time;

    private boolean support;
    private Dialog dialog;
    private EditText editText;

    // private String[] contents = new String[] { "头像、资料虚假", "广告骚扰", "色情低俗",
    // "不文明语言", "其他" };

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (support) {
                iv_like.setImageDrawable(PictureReportActivity.this
                        .getResources().getDrawable(
                                R.drawable.icon_collect_picture_red));
                tv_like.setText((supportCount + 1) + "");
                action = "1";
                ToastUtil.showToast(PictureReportActivity.this, "赞赞");
            } else {
                iv_dislike.setImageDrawable(PictureReportActivity.this
                        .getResources().getDrawable(
                                R.drawable.icon_bad_color_pictre));
                tv_dislike.setText((opposeCount + 1) + "");
                action = "2";
                ToastUtil.showToast(PictureReportActivity.this, "踩踩");
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_report);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setStatusBarHeight(this, iv_empty2);
        // 更改状态栏字体颜色为黑色
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        wid = getIntent().getExtras().getString("wid");
        sid = getIntent().getExtras().getString("sid");
        supportCount = getIntent().getExtras().getInt("supportCount");
        opposeCount = getIntent().getExtras().getInt("opposeCount");
        action = getIntent().getExtras().getString("action");
        pictureUrl = getIntent().getExtras().getString("pictureUrl");
        dream = getIntent().getExtras().getString("dream");
        time = getIntent().getExtras().getString("time");

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        rl_report.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);
        btn05.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        ll_dislike.setOnClickListener(this);

        // pictureWallInfo = PictureWallDetailActivity.list.get(0);

        // wid = pictureWallInfo.getId();
        // sid = pictureWallInfo.getSid();
        Glide.with(this).load(pictureUrl).dontAnimate()
                .placeholder(R.drawable.imgloading).into(iv_picture);

        if (action.equals("0")) {
            iv_like.setImageDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_picture));
            iv_dislike.setImageDrawable(getResources().getDrawable(
                    R.drawable.icon_bad_pictre));
        } else if (action.equals("1")) {
            iv_like.setImageDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_picture_red));
            iv_dislike.setImageDrawable(getResources().getDrawable(
                    R.drawable.icon_bad_pictre));
        } else if (action.equals("2")) {
            iv_like.setImageDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_picture));
            iv_dislike.setImageDrawable(getResources().getDrawable(
                    R.drawable.icon_bad_color_pictre));
        }
        tv_dream.setText(dream);
        tv_like.setText(supportCount + "");
        tv_dislike.setText(opposeCount + "");
        tv_time.setText(time);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.rl_more:
                if (rl_popupwindow.getVisibility() == View.GONE) {
                    rl_popupwindow.setVisibility(View.VISIBLE);
                } else {
                    rl_popupwindow.setVisibility(View.GONE);
                }
                break;

            case R.id.ll_like:
                if (action.equals("0")) {
                    new Thread() {
                        @Override
                        public void run() {
                            doPostSupport(MyConfig.URL_POST_PICTURE_WALL_ATTITUDE);
                        }
                    }.start();
                }
                break;

            case R.id.ll_dislike:
                if (action.equals("0")) {
                    new Thread() {
                        @Override
                        public void run() {
                            doPostOppose(MyConfig.URL_POST_PICTURE_WALL_ATTITUDE);
                        }
                    }.start();
                }
                break;

            case R.id.rl_report:
                rl_popupwindow.setVisibility(View.GONE);
                dialog_report.setVisibility(View.VISIBLE);
                break;

            case R.id.btn01:
                dialog_report.setVisibility(View.GONE);
                content = "头像、资料虚假";
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn02:
                dialog_report.setVisibility(View.GONE);
                content = "广告骚扰";
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn03:
                dialog_report.setVisibility(View.GONE);
                content = "色情低俗";
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn04:
                dialog_report.setVisibility(View.GONE);
                content = "不文明语言";
                progress_update.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn05:
                dialog_report.setVisibility(View.GONE);
                dialog = new Dialog(this, R.style.MyDialog);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.dialog_report_other, null);
                dialog.setContentView(view);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                dialog.setContentView(view, params);
                editText = (EditText) view.findViewById(R.id.et_reason);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

                btn_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        content = editText.getText().toString().trim();
                        if (content.equals("")) {
                            ToastUtil.showToast(PictureReportActivity.this, "请输入举报内容");
                            return;
                        }
                        dialog.dismiss();
                        progress_update.setVisibility(View.VISIBLE);
                        new Thread() {
                            public void run() {
                                doPostReport(MyConfig.URL_POST_REPORT);
                            }

                            ;
                        }.start();
                    }
                });
                dialog.show();
                // 显示软键盘
                MyUtils.showSoftInput(this, editText);
                break;

        }
    }

    private void doPostSupport(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("wid", wid);
        params.addBodyParameter("action", "support");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        support = true;
                        EventBus.getDefault().post(new MEventPicReport(true));
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PictureReportActivity.this, "操作失败");
                    }
                });
    }

    private void doPostOppose(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("wid", wid);
        params.addBodyParameter("action", "oppose");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        support = false;
                        EventBus.getDefault().post(new MEventPicReport(false));
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PictureReportActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 举报
     *
     * @param uploadHost
     */
    private void doPostReport(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "1");
        params.addBodyParameter("rid", wid);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("content", content);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(PictureReportActivity.this, "操作失败");
                        } else if (result.equals("-1")) {
                            ToastUtil.showToast(PictureReportActivity.this, "您已举报过了");
                        } else {
                            ToastUtil.showToast(PictureReportActivity.this, "举报成功");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update.setVisibility(View.GONE);
                        ToastUtil.showToast(PictureReportActivity.this, "操作失败");
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (dialog_report.getVisibility() == View.VISIBLE) {
            dialog_report.setVisibility(View.GONE);
            return true;
        }
        if (rl_popupwindow.getVisibility() == View.VISIBLE) {
            rl_popupwindow.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
