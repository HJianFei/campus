package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CustomAdapter;
import com.loosoo100.campus100.beans.CustomInfo;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableHelper.ScrollableContainer;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableLayout;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * @author yang 客服中心activity
 */
public class CustomActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;

    @ViewInject(R.id.rl_online)
    private RelativeLayout rl_online; // 在线咨询

    @ViewInject(R.id.rl_call)
    private RelativeLayout rl_call; // 意见反馈

    @ViewInject(R.id.rl_feedback)
    private RelativeLayout rl_feedback; // 意见反馈

    @ViewInject(R.id.ll_root)
    private LinearLayout ll_root; // 问题类型视图

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

    @ViewInject(R.id.iv_redline)
    private ImageView iv_redline; // 红条

    @ViewInject(R.id.scrollableLayout)
    private ScrollableLayout scrollableLayout;
    @ViewInject(R.id.lv_custom)
    private ListView lv_custom; // 列表
    @ViewInject(R.id.progress_custom)
    private RelativeLayout progress_custom; // 加载动画

    private int width; // 屏幕宽度
    private int x = 0; // 记录当前红条所在位置

    private CustomAdapter adapter;

    private Intent intent = new Intent();

    // 跳转到QQ
    private String qqUrl = "";
    private String qqNum = "204865540";

    private List<CustomInfo> customInfos;
    private List<CustomInfo> types;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (types != null && types.size() == 5) {
                btn01.setText(types.get(0).getType());
                btn02.setText(types.get(1).getType());
                btn03.setText(types.get(2).getType());
                btn04.setText(types.get(3).getType());
                btn05.setText(types.get(4).getType());
                ll_root.setVisibility(View.VISIBLE);
                if (customInfos != null && customInfos.size() > 0) {
                    initListView();
                }
            } else {
                ll_root.setVisibility(View.GONE);
            }
            progress_custom.setVisibility(View.GONE);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        // 更改状态栏字体颜色为黑色
        MyUtils.setMiuiStatusBarDarkMode(this, true);

        progress_custom.setVisibility(View.VISIBLE);

        WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = systemService.getDefaultDisplay().getWidth() / 5;

        qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1";

        // 设置红条的宽度和高度
        LayoutParams params = new LayoutParams(width, 10);
        iv_redline.setLayoutParams(params);

		/*
         * 下滑时当lv_campus滑动到顶部时头部view才显示出来
		 */
        scrollableLayout.getHelper().setCurrentScrollableContainer(
                new ScrollableContainer() {
                    @Override
                    public View getScrollableView() {
                        return lv_custom;
                    }
                });

        initView();

        new Thread() {
            public void run() {
                types = GetData.getCustomTitleInfos(MyConfig.URL_JSON_CUSTOM);
                customInfos = GetData.getCustomInfos(MyConfig.URL_JSON_CUSTOM);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initListView() {
        adapter = new CustomAdapter(this, customInfos);
        lv_custom.setAdapter(adapter);
    }

    private void initView() {
        rl_back.setOnClickListener(this);
        rl_online.setOnClickListener(this);
        rl_call.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);

        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);
        btn05.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            // 在线咨询
            case R.id.rl_online:
                // intent.setClass(this, OnlineConsultActivity.class);
                // startActivity(intent);
//			if (isQQClientAvailable(this)) {
//				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
//			} else {
//				ToastUtil.showToast(this,"请先安装QQ客户端");
//			}
                RongIM.getInstance().startPrivateChat(this, "527", "校园100在线客服");
                break;

            // 电话咨询
            case R.id.rl_call:
                Intent hotlineIntent = new Intent();
                hotlineIntent.setAction(Intent.ACTION_VIEW);
                hotlineIntent.setData(Uri.parse("tel:4008955065"));
                startActivity(hotlineIntent);
                break;

            // 意见反馈
            case R.id.rl_feedback:
                intent.setClass(this, FeedbackActivity.class);
                startActivity(intent);
                break;

            case R.id.btn01:
                if (x == 0) {
                    return;
                }
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(0));
                // 记录当前红色线条在什么位置
                x = 0;
                resetBtnColor();
                btn01.setTextColor(getResources().getColor(R.color.red_fd3c49));
                progress_custom.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        // 停留0.3秒让红条滑动不卡顿
                        SystemClock.sleep(300);
                        customInfos = GetData
                                .getCustomInfos(MyConfig.URL_JSON_CUSTOM + "?type="
                                        + types.get(0).getId());
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn02:
                if (x == width) {
                    return;
                }
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(1));
                // 记录当前红色线条在什么位置
                x = width;
                resetBtnColor();
                btn02.setTextColor(getResources().getColor(R.color.red_fd3c49));
                progress_custom.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        // 停留0.3秒让红条滑动不卡顿
                        SystemClock.sleep(300);
                        customInfos = GetData
                                .getCustomInfos(MyConfig.URL_JSON_CUSTOM + "?type="
                                        + types.get(1).getId());
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn03:
                if (x == 2 * width) {
                    return;
                }
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(2));
                // 记录当前红色线条在什么位置
                x = 2 * width;
                resetBtnColor();
                btn03.setTextColor(getResources().getColor(R.color.red_fd3c49));
                progress_custom.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        // 停留0.3秒让红条滑动不卡顿
                        SystemClock.sleep(300);
                        customInfos = GetData
                                .getCustomInfos(MyConfig.URL_JSON_CUSTOM + "?type="
                                        + types.get(2).getId());
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn04:
                if (x == 3 * width) {
                    return;
                }
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(3));
                // 记录当前红色线条在什么位置
                x = 3 * width;
                resetBtnColor();
                btn04.setTextColor(getResources().getColor(R.color.red_fd3c49));
                progress_custom.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        // 停留0.3秒让红条滑动不卡顿
                        SystemClock.sleep(300);
                        customInfos = GetData
                                .getCustomInfos(MyConfig.URL_JSON_CUSTOM + "?type="
                                        + types.get(3).getId());
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn05:
                if (x == 4 * width) {
                    return;
                }
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(4));
                // 记录当前红色线条在什么位置
                x = 4 * width;
                resetBtnColor();
                btn05.setTextColor(getResources().getColor(R.color.red_fd3c49));
                progress_custom.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        // 停留0.3秒让红条滑动不卡顿
                        SystemClock.sleep(300);
                        customInfos = GetData
                                .getCustomInfos(MyConfig.URL_JSON_CUSTOM + "?type="
                                        + types.get(4).getId());
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

        }
    }

    /**
     * 按钮字体颜色还原初始化
     */
    private void resetBtnColor() {
        btn01.setTextColor(Color.BLACK);
        btn02.setTextColor(Color.BLACK);
        btn03.setTextColor(Color.BLACK);
        btn04.setTextColor(Color.BLACK);
        btn05.setTextColor(Color.BLACK);
    }

    /**
     * 设置平移动画
     *
     * @return
     */
    private TranslateAnimation translateAnimation(int index) {
        TranslateAnimation translateAnimation = new TranslateAnimation(x, index
                * width, 0, 0);
        translateAnimation.setDuration(300);
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    /**
     * 判断是否安装过QQ客户端
     *
     * @param context
     * @return
     */
    private boolean isQQClientAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite")
                        || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}
