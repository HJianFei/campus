package com.loosoo100.campus100.zzboss.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CustomActivity;
import com.loosoo100.campus100.activities.LoginActivity;
import com.loosoo100.campus100.activities.SettingActivity;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.MyScrollView;
import com.loosoo100.campus100.view.MyScrollView.OnScrollListener;
import com.loosoo100.campus100.wxapi.WXShareUtil;
import com.loosoo100.campus100.zzboss.activities.BossCommInterestActivity;
import com.loosoo100.campus100.zzboss.activities.BossCompanySummaryActivity;
import com.loosoo100.campus100.zzboss.activities.BossHomeActivity;
import com.loosoo100.campus100.zzboss.activities.BossIdentificationActivity;
import com.loosoo100.campus100.zzboss.activities.BossMoneyActivity;
import com.loosoo100.campus100.zzboss.activities.BossMyActiActivity;
import com.loosoo100.campus100.zzboss.activities.BossMyCollectActivity;
import com.loosoo100.campus100.zzboss.activities.BossMyCommActivity;
import com.loosoo100.campus100.zzboss.activities.BossPointActivity;
import com.loosoo100.campus100.zzboss.activities.BossUserOrAddressActivity;
import com.loosoo100.campus100.zzboss.beans.BossCompanyInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * @author yang 我的fragment
 */
public class BossPersonalFragment extends Fragment implements OnClickListener,
        OnScrollListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName; // 昵称
    @ViewInject(R.id.ib_person)
    private CircleView ib_person; // 个人头像
    @ViewInject(R.id.rl_topbar)
    private RelativeLayout rl_topbar;
    @ViewInject(R.id.scrollview)
    private MyScrollView scrollview;
    @ViewInject(R.id.iv_identification)
    private ImageView iv_identification;

    @ViewInject(R.id.iv_userBg)
    private ImageView iv_userBg; // 背景图片
    @ViewInject(R.id.ll_top)
    private LinearLayout ll_top; // 顶部布局
    @ViewInject(R.id.tv_point)
    private TextView tv_point; // 积分
    @ViewInject(R.id.tv_money)
    private TextView tv_money; // 余额
    @ViewInject(R.id.tv_redPaper)
    private TextView tv_redPaper; // 红包个数

    private Intent intent = new Intent();
    ;
    private Activity activity;
    private View view;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;
    private DisplayMetrics metric;

    private String cuid = ""; // 公司ID
    private String nickName = ""; // 昵称
    private String name = ""; // 真实姓名
    private String sex = "1"; // 性别
    private String weixin = ""; // 性别
    private String headShot = ""; // 头像
    private BossCompanyInfo companyInfo = null; // 公司信息
    private Bitmap bitmap = null;
    private WXShareUtil shareUtil;
//    private int commCount; // 社团个数

    private Dialog dialog;

    private String downUrl = "http://www.campus100.cn/App/index.php/Home/ApisharePay/link";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (companyInfo == null) {
                return;
            }
            headShot = companyInfo.getHeadShot();
            nickName = companyInfo.getNickName();
            name = companyInfo.getName();
            sex = companyInfo.getSex();
            weixin = companyInfo.getWeixin();

            // 企业是否已认证
            if (companyInfo.getFlag() == 2) {
                iv_identification
                        .setImageResource(R.drawable.icon_boss_identification_yes);

            } else if (companyInfo.getFlag() == 1) {
                iv_identification
                        .setImageResource(R.drawable.icon_boss_identification_ing);
            } else {
                iv_identification
                        .setImageResource(R.drawable.icon_boss_identification_no);
            }
            // 设置积分、余额、红包
            tv_point.setText(companyInfo.getPoint() + "分");
            tv_money.setText(companyInfo.getMoney() + "元");
            tv_redPaper.setText(companyInfo.getZanMoney() + "元");
            // 保存用户余额
            UserInfoDB.setUserInfo(activity, UserInfoDB.MONEY,
                    companyInfo.getMoney() + "");
            UserInfoDB.setUserInfo(activity, UserInfoDB.POINT,
                    companyInfo.getPoint() + "");

            if (headShot != null && !headShot.equals("")) {
                Glide.with(activity).load(headShot).into(ib_person);
                UserInfoDB.setUserInfo(activity, UserInfoDB.HEADSHOT, headShot);
                new Thread() {
                    public void run() {
                        try {
                            bitmap = MyUtils.getBitMap(headShot);
                            if (bitmap != null) {
                                saveUserIcon(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    ;
                }.start();
            } else if (headShot != null && headShot.equals("-1")) {
                // 停止推送
                JPushInterface.setAlias(activity, "", null);
                // 清除本地用户
                UserInfoDB.clearUserInfo(activity);
                EventBus.getDefault().post(new MEventHome(true));
                ToastUtil.showToast(activity, "账号不存在");
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            } else {
                ib_person.setImageDrawable(activity.getResources().getDrawable(
                        R.drawable.imgloading));
            }

            if (nickName != null && !nickName.equals("")) {
                tv_userName.setText(nickName);
                UserInfoDB
                        .setUserInfo(activity, UserInfoDB.NICK_NAME, nickName);
            }
            if (name != null && !name.equals("")) {
                UserInfoDB.setUserInfo(activity, UserInfoDB.NAME, name);
            }
            if (sex != null && !sex.equals("")) {
                UserInfoDB.setUserInfo(activity, UserInfoDB.SEX, sex);
            }
            if (weixin != null && !weixin.equals("")) {
                UserInfoDB.setUserInfo(activity, UserInfoDB.WEIXIN, weixin);
            }

        }

        ;
    };

    // private Handler handlerComm = new Handler() {
    // public void handleMessage(android.os.Message msg) {
    // tv_redPaper.setText(commCount + "个");
    // }
    // };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_boss_personal, container,
                false);
        activity = getActivity();
        ViewUtils.inject(this, view); // 注入view和事件
        // 获取屏幕宽高
        metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        initView();

        MyUtils.setStatusBarHeight(activity, iv_empty);
        shareUtil = new WXShareUtil(activity);

        return view;
    }

    public void initView() {
        cuid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
        ib_person.setOnClickListener(this);
        iv_identification.setOnClickListener(this);
        scrollview.setOnScrollListener(this);

        view.findViewById(R.id.ll_point).setOnClickListener(this);
        view.findViewById(R.id.ll_money).setOnClickListener(this);
        view.findViewById(R.id.ll_redPaper).setOnClickListener(this);

        view.findViewById(R.id.ll_interest_comm).setOnClickListener(this);
        view.findViewById(R.id.ll_summary).setOnClickListener(this);

        view.findViewById(R.id.ll_community).setOnClickListener(this);
        view.findViewById(R.id.ll_activity).setOnClickListener(this);
        view.findViewById(R.id.ll_mycollect).setOnClickListener(this);
        view.findViewById(R.id.ll_invite_friend).setOnClickListener(this);
        view.findViewById(R.id.ll_custom).setOnClickListener(this);
        view.findViewById(R.id.ll_setting).setOnClickListener(this);
        view.findViewById(R.id.bt_custom_hotline).setOnClickListener(this);
        view.findViewById(R.id.ll_userBg).setOnClickListener(this);

        iv_userBg.setImageBitmap(GetData.getBitMap(activity, R.drawable.bg));

        dialog = new Dialog(activity, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View viewDialog = inflater.inflate(R.layout.dialog_weixin_choice, null);
        dialog.setContentView(viewDialog);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        dialog.setContentView(viewDialog, params);
        viewDialog.findViewById(R.id.btn_weixin).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareUtil.shareMessageToSession("校园100", "下载链接",
                                downUrl, null);
                        dialog.dismiss();
                    }
                });

        viewDialog.findViewById(R.id.btn_timeline).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareUtil.shareMessageToTimeLine("校园100", "下载链接",
                                downUrl, null);
                        dialog.dismiss();
                    }
                });

        scrollview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) iv_userBg
                        .getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 手指离开后恢复图片
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            if (scrollview.getScrollY() == 0) {
                                mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.8f); // 滚动距离乘以一个系数
                        if (distance < 0) { // 当前位置比记录位置要小，正常返回
                            break;
                        }
                        ll_top.setY(distance * 0.5f);
                        // 处理放大
                        mScaling = true;
                        lp.width = metric.widthPixels + distance;
                        lp.height = (metric.widthPixels + distance) * 9 / 16;
                        iv_userBg.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });
    }

    public void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) iv_userBg
                .getLayoutParams();
        final float w = iv_userBg.getLayoutParams().width;// 图片当前宽度
        final float h = iv_userBg.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度
        ll_top.setY(0);
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(50);

        anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                iv_userBg.setLayoutParams(lp);
            }
        });
        anim.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_person:
                if (cuid.equals("")) {
                    return;
                }
                // intent.setClass(activity, CampusContactsPersonalActivity.class);
                // activity.startActivity(intent);
                break;

            case R.id.iv_identification:
                if (companyInfo != null && companyInfo.getFlag() == 0) {
                    intent.setClass(activity, BossIdentificationActivity.class);
                    activity.startActivity(intent);
                }
                break;

            case R.id.ll_userBg:
                // 判断用户如果没登录或注册就跳转到登录注册界面
                if (cuid.equals("")) {
                    intent.setClass(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
                // 否则跳转到账户管理界面
                else {
                    intent.setClass(activity, BossUserOrAddressActivity.class);
                    activity.startActivity(intent);
                }
                break;

            case R.id.ll_point:
                intent.setClass(activity, BossPointActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_money:
                intent.setClass(activity, BossMoneyActivity.class);
                activity.startActivity(intent);
                break;

//            case R.id.ll_redPaper:
//                intent.setClass(activity, BossCommInterestActivity.class);
//                activity.startActivity(intent);
//                break;

            // 我的赞助记录
            case R.id.ll_community:
                intent.setClass(activity, BossMyCommActivity.class);
                activity.startActivity(intent);
                break;

            // 关注的社团
            case R.id.ll_interest_comm:
                intent.setClass(activity, BossCommInterestActivity.class);
                activity.startActivity(intent);
                break;

            // 公司简介
            case R.id.ll_summary:
                // }
                if (companyInfo.getFlag() == 1) {
                    ToastUtil.showToast(activity, "正在审核中，请审核通过后再修改信息");
                    return;
                }
                intent.setClass(activity, BossCompanySummaryActivity.class);
                activity.startActivity(intent);
                break;

            // 我的活动
            case R.id.ll_activity:
                intent.setClass(activity, BossMyActiActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_mycollect:
                intent.setClass(activity, BossMyCollectActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_invite_friend:
                dialog.show();
                break;

            case R.id.ll_custom:
                intent.setClass(activity, CustomActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_setting:
                intent.setClass(activity, SettingActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.bt_custom_hotline:
                Intent hotlineIntent = new Intent();
                hotlineIntent.setAction(Intent.ACTION_VIEW);
                hotlineIntent.setData(Uri.parse("tel:4008955065"));
                activity.startActivity(hotlineIntent);
                break;
        }
    }

    // @Override
    // public void onHiddenChanged(boolean hidden) {
    // if (!hidden) {
    // new Thread() {
    // public void run() {
    // commCount = GetData
    // .getMyCommunityCount(MyConfig.URL_JSON_MYCOMMUNITY
    // + uid + "&sid=" + sid);
    // if (!activity.isDestroyed()) {
    // handlerComm.sendEmptyMessage(0);
    // }
    // };
    // }.start();
    // }
    // super.onHiddenChanged(hidden);
    // }

    @Override
    public void onResume() {
        super.onResume();
        cuid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.CUSERID, "");
        if (cuid.equals("")) {
            tv_userName.setText("登录/注册");
            tv_point.setText("分");
            tv_money.setText("元");
            tv_redPaper.setText("元");
            ib_person.setImageDrawable(activity.getResources().getDrawable(
                    R.drawable.imgloading));
            return;
        }
        // 设置用户头像
        File file = new File(MyConfig.IMAGGE_URI + cuid + ".png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(MyConfig.IMAGGE_URI + cuid
                    + ".png");
            ib_person.setImageBitmap(bitmap);
        }
        new Thread() {
            public void run() {
                companyInfo = GetData
                        .getBossUserInfo(MyConfig.URL_JSON_COMPANYINFO + cuid);
//                commCount = GetData
//                        .getBossCommCount(MyConfig.URL_JSON_COMM_INTEREST_BOSS
//                                + cuid);
                if (!activity.isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
        nickName = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.NICK_NAME, "");
        if (tv_userName != null) {
            tv_userName.setText(nickName);
        }
    }

    /**
     * 监听滑动改变透明度
     */
    @Override
    public void onScroll(int scrollY) {
        rl_topbar.setAlpha((float) scrollY / 500);
        iv_empty.setAlpha((float) scrollY / 500);
    }

    private void saveUserIcon(Bitmap bitmap) throws IOException {
        File file = new File(MyConfig.FILE_URI);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dir = new File(MyConfig.IMAGGE_URI + cuid + ".png");
        if (!dir.exists()) {
            dir.createNewFile();
        }
        FileOutputStream iStream = new FileOutputStream(dir);
        bitmap.compress(CompressFormat.PNG, 100, iStream);
        iStream.close();
        iStream = null;
        file = null;
        dir = null;

    }

}
