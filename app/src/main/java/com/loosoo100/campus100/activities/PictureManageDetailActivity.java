package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.loosoo100.campus100.anyevent.MEventPicChange;
import com.loosoo100.campus100.anyevent.MEventPicManageChange;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 许愿管理详情页activity
 */
public class PictureManageDetailActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
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
    @ViewInject(R.id.iv_like)
    private ImageView iv_like;
    @ViewInject(R.id.iv_dislike)
    private ImageView iv_dislike;
    @ViewInject(R.id.ll_like)
    private LinearLayout ll_like;
    @ViewInject(R.id.ll_dislike)
    private LinearLayout ll_dislike;
    @ViewInject(R.id.tv_time)
    private TextView tv_time; // 发布的时间
    @ViewInject(R.id.btn_delete)
    private Button btn_delete; // 删除按钮
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 加载动画

    // private PictureWallInfo pictureWallInfo;

    private String wid = ""; // 愿望ID
    private String uid = "";
    private String result = "";

    // private int index;
    private String pic = "";
    private String dream = "";
    private int supportCount;
    private int opposeCount;
    private String time = "";
    private String action = "";

    private boolean support;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (result.equals("1")) {
                ToastUtil.showToast(PictureManageDetailActivity.this, "删除成功");
                EventBus.getDefault().post(new MEventPicChange(true));
                // PictureManageActivity.list.remove(index);
                // PictureManageActivity.adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new MEventPicManageChange(true));
                PictureManageDetailActivity.this.finish();
            } else {
                ToastUtil.showToast(PictureManageDetailActivity.this, "删除失败");
            }
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (support) {
                iv_like.setImageDrawable(PictureManageDetailActivity.this
                        .getResources().getDrawable(
                                R.drawable.icon_collect_picture_red));
                tv_like.setText((supportCount + 1) + "");
                action = "1";
                supportCount = supportCount + 1;
                ToastUtil.showToast(PictureManageDetailActivity.this, "赞赞");
            } else {
                iv_dislike.setImageDrawable(PictureManageDetailActivity.this
                        .getResources().getDrawable(
                                R.drawable.icon_bad_color_pictre));
                tv_dislike.setText((opposeCount + 1) + "");
                action = "2";
                opposeCount = opposeCount + 1;
                ToastUtil.showToast(PictureManageDetailActivity.this, "踩踩");
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_manage_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        // 更改状态栏字体颜色为黑色
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        pic = getIntent().getExtras().getString("pic");
        dream = getIntent().getExtras().getString("dream");
        supportCount = getIntent().getExtras().getInt("supportCount");
        opposeCount = getIntent().getExtras().getInt("opposeCount");
        time = getIntent().getExtras().getString("time");
        action = getIntent().getExtras().getString("action");
        wid = getIntent().getExtras().getString("wid");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        ll_dislike.setOnClickListener(this);

		/*
         * private String pic = pictureWallInfo.getPicture(); private String
		 * dream = pictureWallInfo.getDream(); private String supportCount =
		 * pictureWallInfo.getSupportCount(); private String opposeCount =
		 * pictureWallInfo.getOpposeCount(); private String time =
		 * pictureWallInfo.getTime(); private String action =
		 * pictureWallInfo.getAction();
		 */
        Glide.with(this).load(pic).dontAnimate()
                .placeholder(R.drawable.imgloading).into(iv_picture);

        tv_dream.setText(dream + "");
        tv_like.setText(supportCount + "");
        tv_dislike.setText(opposeCount + "");
        tv_time.setText(time);

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
        // tv_dream.setText(pictureWallInfo.getDream() + "");
        // tv_like.setText(pictureWallInfo.getSupportCount() + "");
        // tv_dislike.setText(pictureWallInfo.getOpposeCount() + "");
        // tv_time.setText(pictureWallInfo.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
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

            case R.id.btn_delete:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否确认删除");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        doDelete(MyConfig.URL_POST_PICTURE_WALL_DELETE
                                                + wid);
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

    private void doDelete(String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PictureManageDetailActivity.this, "删除失败");
                        progress_update.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        LogUtils.d("responseInfo", arg0.result);
                        progress_update.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                });
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
                        handler2.sendEmptyMessage(0);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PictureManageDetailActivity.this, "操作失败");
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
                        handler2.sendEmptyMessage(0);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(PictureManageDetailActivity.this, "操作失败");
                    }
                });
    }
}
