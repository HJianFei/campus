package com.loosoo100.campus100.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.loosoo100.campus100.activities.CampusContactsPersonalActivity;
import com.loosoo100.campus100.activities.CommunityJoinActivity;
import com.loosoo100.campus100.activities.CustomActivity;
import com.loosoo100.campus100.activities.GiftOrderActivity;
import com.loosoo100.campus100.activities.InviteListActivity;
import com.loosoo100.campus100.activities.LoginActivity;
import com.loosoo100.campus100.activities.MessageActivity;
import com.loosoo100.campus100.activities.MoneyActivity;
import com.loosoo100.campus100.activities.MyActivityActivity;
import com.loosoo100.campus100.activities.MyCollectActivity;
import com.loosoo100.campus100.activities.MyCommunityActivity;
import com.loosoo100.campus100.activities.MyCreditActivity;
import com.loosoo100.campus100.activities.PartnerJoinActivity;
import com.loosoo100.campus100.activities.PointActivity;
import com.loosoo100.campus100.activities.SettingActivity;
import com.loosoo100.campus100.activities.StoreOrderActivity;
import com.loosoo100.campus100.activities.UserOrAddressActivity;
import com.loosoo100.campus100.anyevent.MEventHome;
import com.loosoo100.campus100.anyevent.MEventMessageNoRead;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.chat.ui.SearchFriendActivity;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.MyScrollView;
import com.loosoo100.campus100.view.MyScrollView.OnScrollListener;
import com.loosoo100.campus100.wxapi.WXShareUtil;
import com.loosoo100.campus100.zxing.encoding.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;


/**
 * @author yang 我的fragment
 */
public class PersonalFragment extends Fragment implements OnClickListener,
        OnScrollListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName; // 昵称
    @ViewInject(R.id.ib_person)
    private CircleView ib_person; // 个人头像
    // @ViewInject(R.id.iv_sex)
    // public static ImageView iv_sex; // 性别图标
    @ViewInject(R.id.rl_topbar)
    private RelativeLayout rl_topbar;
    @ViewInject(R.id.scrollview)
    private MyScrollView scrollview;
    //    @ViewInject(R.id.iv_circle)
//    private ImageView iv_circle; // 引导红点
    @ViewInject(R.id.tv_count)
    private TextView tv_count; // 引导红点-消息

    @ViewInject(R.id.iv_userBg)
    private ImageView iv_userBg; // 背景图片
    @ViewInject(R.id.ll_top)
    private LinearLayout ll_top; // 顶部布局
    @ViewInject(R.id.tv_point)
    private TextView tv_point; // 积分
    @ViewInject(R.id.tv_money)
    private TextView tv_money; // 余额
    @ViewInject(R.id.tv_redPaper)
    private TextView tv_redPaper; // 社团个数
    // @ViewInject(R.id.dialog)
    // private RelativeLayout dialog; // 微信选择框
    // @ViewInject(R.id.btn_weixin)
    // private Button btn_weixin; // 微信
    // @ViewInject(R.id.btn_timeline)
    // private Button btn_timeline; // 朋友圈

    private Intent intent = new Intent();
    ;
    private Activity activity;
    private View view;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;
    private DisplayMetrics metric;

    private String uid = ""; // 用户ID
    private String sid = ""; // 学校ID
    private String nickName = ""; // 昵称
    private String headShot = ""; // 头像
    private UserInfo userInfo = null; // 用户信息
    private Bitmap bitmap = null;
    private WXShareUtil shareUtil;
    private int commCount = -1; // 社团个数

    private int noReadCountMes = 0; // 系统消息未读个数

    private Dialog dialog;

    private String downUrl = "http://www.campus100.cn/App/index.php/Home/ApisharePay/link";

    private boolean isLoading = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (userInfo == null) {
                return;
            }
            headShot = userInfo.getHeadShot();
            nickName = userInfo.getNickName();
            // 设置积分、余额、红包
            tv_point.setText(userInfo.getPoint() + "分");
            tv_money.setText(userInfo.getMoney() + "元");
            if (commCount != -1) {
                tv_redPaper.setText(commCount + "个");
            }
            // 保存用户余额
            UserInfoDB.setUserInfo(activity, UserInfoDB.MONEY,
                    userInfo.getMoney() + "");
            UserInfoDB.setUserInfo(activity, UserInfoDB.POINT,
                    userInfo.getPoint() + "");
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
            if (noReadCountMes > 0) {
                if (noReadCountMes > 99) {
                    tv_count.setText("99+");
                } else {
                    tv_count.setText(noReadCountMes + "");
                }
                tv_count.setVisibility(View.VISIBLE);
            } else {
                tv_count.setVisibility(View.GONE);
            }
            isLoading = false;
            UserInfoDB
                    .setUserInfo(activity, UserInfoDB.NOREAD_MESSAGE_COUNT, noReadCountMes + "");
            EventBus.getDefault().post(new MEventMessageNoRead(true));
        }
    };

    private Handler handlerComm = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (commCount != -1) {
                tv_redPaper.setText(commCount + "个");
            }
            if (noReadCountMes > 0) {
                if (noReadCountMes > 99) {
                    tv_count.setText("99+");
                } else {
                    tv_count.setText(noReadCountMes + "");
                }
                tv_count.setVisibility(View.VISIBLE);
            } else {
                tv_count.setVisibility(View.GONE);
            }
            UserInfoDB
                    .setUserInfo(activity, UserInfoDB.NOREAD_MESSAGE_COUNT, noReadCountMes + "");
            EventBus.getDefault().post(new MEventMessageNoRead(true));
            isLoading = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        activity = getActivity();
        ViewUtils.inject(this, view); // 注入view和事件
        EventBus.getDefault().register(this);
        // 获取屏幕宽高
        metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        initView();

        MyUtils.setStatusBarHeight(activity, iv_empty);
        shareUtil = new WXShareUtil(activity);

        return view;
    }

    public void initView() {
        uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        sid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");
//        ib_person.setOnClickListener(this);
        scrollview.setOnScrollListener(this);

        view.findViewById(R.id.rl_top_click).setOnClickListener(this);
        view.findViewById(R.id.rl_bottom_click).setOnClickListener(this);
        view.findViewById(R.id.ll_point).setOnClickListener(this);
        view.findViewById(R.id.ll_money).setOnClickListener(this);
        view.findViewById(R.id.ll_redPaper).setOnClickListener(this);
        view.findViewById(R.id.rl_message).setOnClickListener(this);

        view.findViewById(R.id.ll_campusOrder).setOnClickListener(this);
        view.findViewById(R.id.ll_giftOrder).setOnClickListener(this);
        // view.findViewById(R.id.ll_bus).setOnClickListener(this);
        // view.findViewById(R.id.ll_gogo).setOnClickListener(this);
        view.findViewById(R.id.ll_mycredit).setOnClickListener(this);
        view.findViewById(R.id.ll_community).setOnClickListener(this);
        view.findViewById(R.id.ll_activity).setOnClickListener(this);
        // view.findViewById(R.id.ll_mycourse).setOnClickListener(this);
        // view.findViewById(R.id.ll_myresume).setOnClickListener(this);
        view.findViewById(R.id.ll_mycollect).setOnClickListener(this);
        view.findViewById(R.id.ll_invite_friend).setOnClickListener(this);
        view.findViewById(R.id.ll_business).setOnClickListener(this);
        view.findViewById(R.id.ll_custom).setOnClickListener(this);
        view.findViewById(R.id.ll_setting).setOnClickListener(this);
        view.findViewById(R.id.bt_custom_hotline).setOnClickListener(this);
        view.findViewById(R.id.ll_userBg).setOnClickListener(this);

        iv_userBg.setImageBitmap(GetData.getBitMap(activity, R.drawable.bg));

        dialog = new Dialog(activity, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View viewDialog = inflater.inflate(R.layout.dialog_weixin_choice_person, null);
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

        viewDialog.findViewById(R.id.btn_invitelist).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, InviteListActivity.class);
                        activity.startActivity(intent);
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
            case R.id.rl_top_click:
                intent.setClass(activity, CampusContactsPersonalActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.rl_bottom_click:
                showQRCodeDialog();
                break;

            case R.id.ll_userBg:
                // 判断用户如果没登录或注册就跳转到登录注册界面
                if (uid.equals("")) {
                    intent.setClass(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
                // 否则跳转到账户管理界面
                else {
                    intent.setClass(activity, UserOrAddressActivity.class);
                    activity.startActivity(intent);
                }
                break;

            case R.id.ll_point:
                intent.setClass(activity, PointActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_money:
                intent.setClass(activity, MoneyActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_redPaper:
                if (commCount == -1) {
                    ToastUtil.showToast(activity, "网络不给力,请稍候");
                    return;
                }
                if (commCount == 0) {
                    intent.setClass(activity, CommunityJoinActivity.class);
                    activity.startActivity(intent);
                } else {
                    intent.setClass(activity, MyCommunityActivity.class);
                    activity.startActivity(intent);
                }
                break;

            case R.id.rl_message:
                intent.setClass(activity, MessageActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_campusOrder:
                intent.setClass(activity, StoreOrderActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_giftOrder:
                intent.setClass(activity, GiftOrderActivity.class);
                activity.startActivity(intent);
                break;

            // case R.id.ll_bus:
            // intent.setClass(activity, BusOrderActivity.class);
            // activity.startActivity(intent);
            // break;
            //
            // case R.id.ll_gogo:
            // intent.setClass(activity, MyGOGOActivity.class);
            // activity.startActivity(intent);
            // break;

            case R.id.ll_mycredit:
                intent.setClass(activity, MyCreditActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_community:
                if (commCount == -1) {
                    ToastUtil.showToast(activity, "网络不给力,请稍候");
                    return;
                }
                if (commCount == 0) {
                    intent.setClass(activity, CommunityJoinActivity.class);
                    activity.startActivity(intent);
                } else {
                    intent.setClass(activity, MyCommunityActivity.class);
                    activity.startActivity(intent);
                }
                break;

            case R.id.ll_activity:
                intent.setClass(activity, MyActivityActivity.class);
                activity.startActivity(intent);
                break;

            // case R.id.ll_mycourse:
            // intent.setClass(activity, CourseActivity.class);
            // activity.startActivity(intent);
            // break;
            //
            // case R.id.ll_myresume:
            // intent.setClass(activity, MyResumeActivity.class);
            // activity.startActivity(intent);
            // break;

            case R.id.ll_mycollect:
                intent.setClass(activity, MyCollectActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_invite_friend:
                dialog.show();
                // AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // builder.setTitle("请选择");
                // builder.setItems(new String[] { "微信好友", "微信朋友圈" },
                // new DialogInterface.OnClickListener() {
                // public void onClick(DialogInterface dialog, int which) {
                // if (which == 0) {
                // shareUtil.shareMessageToSession("校园100",
                // "下载链接", downUrl, null);
                // } else {
                // shareUtil.shareMessageToTimeLine("校园100",
                // "下载链接", downUrl, null);
                // }
                // dialog.dismiss();
                // }
                // });
                // builder.show();
                break;

            case R.id.ll_business:
                intent.setClass(activity, PartnerJoinActivity.class);
                activity.startActivity(intent);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (isLoading) {
                return;
            }
            isLoading = true;
            new Thread() {
                public void run() {
                    int count = GetData
                            .getMyCommunityCount(MyConfig.URL_JSON_MYCOMMUNITY
                                    + uid + "&sid=" + sid);
                    if (count != -1) {
                        commCount = count;
                    }
                    noReadCountMes = GetData
                            .getNoReadCount(MyConfig.URL_JSON_MESSAGE_NOREAD
                                    + uid);
                    if (!activity.isDestroyed()) {
                        handlerComm.sendEmptyMessage(0);
                    }
                }
            }.start();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!activity
//                .getSharedPreferences(UserInfoDB.USERTABLE,
//                        activity.MODE_PRIVATE)
//                .getString(UserInfoDB.CAMPUS_NOREAD_HOME_LOVE, "0").equals("0")
//                || !activity
//                .getSharedPreferences(UserInfoDB.USERTABLE,
//                        activity.MODE_PRIVATE)
//                .getString(UserInfoDB.CAMPUS_NOREAD_HOME_FRIEND, "0")
//                .equals("0")) {
//            iv_circle.setVisibility(View.VISIBLE);
//        } else {
//            iv_circle.setVisibility(View.GONE);
//        }
        if (isLoading) {
            return;
        }
        isLoading = true;
        uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        if (uid.equals("")) {
            // iv_sex.setVisibility(View.GONE);
            tv_userName.setText("登录/注册");
            tv_point.setText("分");
            tv_money.setText("元");
            tv_redPaper.setText("个");
            ib_person.setImageDrawable(activity.getResources().getDrawable(
                    R.drawable.imgloading));
            return;
        }
        // 设置用户头像
        File file = new File(MyConfig.IMAGGE_URI + uid + ".png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(MyConfig.IMAGGE_URI + uid
                    + ".png");
            ib_person.setImageBitmap(bitmap);
        }
        new Thread() {
            public void run() {
                userInfo = GetData
                        .getUserInfo(MyConfig.URL_JSON_USERINFO + uid);
                int count = GetData
                        .getMyCommunityCount(MyConfig.URL_JSON_MYCOMMUNITY
                                + uid + "&sid=" + sid);
                noReadCountMes = GetData
                        .getNoReadCount(MyConfig.URL_JSON_MESSAGE_NOREAD
                                + uid);
                if (count != -1) {
                    commCount = count;
                }
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
        // if (activity
        // .getSharedPreferences(UserInfoDB.USERTABLE,
        // activity.MODE_PRIVATE).getString(UserInfoDB.SEX, "1")
        // .equals("1")) {
        // iv_sex.setImageDrawable(activity.getResources().getDrawable(
        // R.drawable.icon_female_picture));
        // iv_sex.setVisibility(View.VISIBLE);
        // } else {
        // iv_sex.setImageDrawable(activity.getResources().getDrawable(
        // R.drawable.icon_male_picture));
        // iv_sex.setVisibility(View.VISIBLE);
        // }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
        File dir = new File(MyConfig.IMAGGE_URI + uid + ".png");
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

    private void showQRCodeDialog() {
        String school_name = (String) SharedPreferencesUtils.getParam(activity, "school_name", "");
        String user_name = (String) SharedPreferencesUtils.getParam(activity, "user_name", "");
        String avatar = (String) SharedPreferencesUtils.getParam(activity, "user_avatar", "");
        Bitmap bitmap = EncodingUtils.createQRCode("myQRCode>" + uid + ">" + avatar + ">" + user_name, 400, 400, null);
        LayoutInflater qr_view = activity.getLayoutInflater();
        View layout = qr_view
                .inflate(R.layout.my_qrcode_dialog, null);
        CircleView iv_qrcode_img = (CircleView) layout
                .findViewById(R.id.cv_qrcode_head);
        Glide.with(this).load(avatar)
                .error(R.drawable.default_useravatar)
                .into(iv_qrcode_img);
        TextView tv_title = (TextView) layout
                .findViewById(R.id.tv_qrcode_communityName);

        tv_title.setText(user_name);
        TextView tv_id = (TextView) layout.findViewById(R.id.tv_qrcode_id);
        tv_id.setText(school_name);
        ImageView iv_qrcode = (ImageView) layout.findViewById(R.id.my_qrcode);
        iv_qrcode.setImageBitmap(bitmap);
        new AlertDialog.Builder(activity).setView(layout)
                .setInverseBackgroundForced(true).show();
    }

    /**
     * 红点是否显示
     *
     * @param event
     */
    public void onEventMainThread(MEventMessageNoRead event) {
        if (event.isChange()) {
            if (event.isChange()) {
                String count = activity.getSharedPreferences(UserInfoDB.USERTABLE, activity.MODE_PRIVATE)
                        .getString(UserInfoDB.NOREAD_MESSAGE_COUNT,
                                "0");
                if (!count.equals("0")) {
                    if (Integer.valueOf(count) > 99) {
                        tv_count.setText("99+");
                    } else {
                        tv_count.setText(count + "");
                    }
                    tv_count.setVisibility(View.VISIBLE);
                } else {
                    tv_count.setVisibility(View.GONE);
                }
            }
        }
    }

}
