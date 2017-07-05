package com.loosoo100.campus100.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsActivity;
import com.loosoo100.campus100.activities.EmergencyActivity;
import com.loosoo100.campus100.activities.GiftActivity;
import com.loosoo100.campus100.activities.LoveSecureActivity;
import com.loosoo100.campus100.activities.OverseasActivity;
import com.loosoo100.campus100.activities.QuestionSurveyActivity;
import com.loosoo100.campus100.anyevent.MEventAddFriendCount;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import de.greenrobot.event.EventBus;

/**
 * @author yang 发现fragment
 */
public class FoundFragment extends Fragment implements OnClickListener {

    private Activity activity;

    private Intent intent = new Intent();

    //ll_find,ll_point_mall
    private LinearLayout ll_campus_circle, ll_overseas,
            ll_gift, ll_love, ll_money, ll_water;

    private ImageView iv_empty; // 空view
    private ImageView iv_circle; // 红点
    private TextView tv_count; // 发现的条数
    private CircleView cv_headShot; // 头像

    private View view;
    private UserInfo userInfo = null; // 用户信息

    private String sid = "";
    private String uid = "";
    private CampusContactsInfo campusContactsTimeInfo;
    private boolean isChecked = false;
    private boolean tag = false; // 判断是否点击校园圈

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            isChecked = false;
            if (userInfo != null) {
                if (!userInfo.getHeadShot().equals("")) {
                    UserInfoDB.setUserInfo(activity, UserInfoDB.HEADSHOT,
                            userInfo.getHeadShot());
                }
                UserInfoDB.setUserInfo(activity, UserInfoDB.NICK_NAME,
                        userInfo.getNickName());
                UserInfoDB.setUserInfo(activity, UserInfoDB.SEX,
                        userInfo.getSex());
            }
            if (campusContactsTimeInfo != null) {
                if (campusContactsTimeInfo.getHeadShot() != null
                        && !campusContactsTimeInfo.getHeadShot().equals("")) {
                    // 头像
                    Glide.with(activity)
                            .load(campusContactsTimeInfo.getHeadShot())
                            .override(90, 90).into(cv_headShot);
                }
                if (campusContactsTimeInfo.getTime() > Long.valueOf(activity
                        .getSharedPreferences(UserInfoDB.USERTABLE,
                                activity.MODE_PRIVATE).getString(
                                UserInfoDB.CAMPUS_TIME, "0"))) {
                    if (tv_count.getVisibility() == View.GONE) {
                        iv_circle.setVisibility(View.VISIBLE);
                    }
                } else {
                    iv_circle.setVisibility(View.GONE);
                }
            }
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            isChecked = false;
            if (campusContactsTimeInfo != null) {
                if (campusContactsTimeInfo.getHeadShot() != null
                        && !campusContactsTimeInfo.getHeadShot().equals("")) {
                    // 头像
                    Glide.with(activity)
                            .load(campusContactsTimeInfo.getHeadShot())
                            .override(90, 90).into(cv_headShot);
                }
                if (campusContactsTimeInfo.getTime() > Long.valueOf(activity
                        .getSharedPreferences(UserInfoDB.USERTABLE,
                                activity.MODE_PRIVATE).getString(
                                UserInfoDB.CAMPUS_TIME, "0"))) {
                    if (tv_count.getVisibility() == View.GONE) {
                        iv_circle.setVisibility(View.VISIBLE);
                    }
                } else {
                    iv_circle.setVisibility(View.GONE);
                }
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_found, container, false);

        activity = getActivity();

        uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        sid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");

        initView();
        MyUtils.setStatusBarHeight(activity, iv_empty);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && !isChecked) {
            new Thread() {
                public void run() {
                    campusContactsTimeInfo = GetData
                            .getCampusContactsTimeInfo(MyConfig.URL_JSON_CAMPUS
                                    + sid + "&type=0" + "&uid=" + uid
                                    + "&status=3" + "&page=1");
                    if (!activity.isDestroyed()) {
                        handler2.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
        super.onHiddenChanged(hidden);
    }

    private void initView() {
        ll_campus_circle = (LinearLayout) view
                .findViewById(R.id.ll_campus_circle);

//		ll_point_mall = (LinearLayout) view.findViewById(R.id.ll_point_mall);
//		ll_find = (LinearLayout) view.findViewById(R.id.ll_find);
        ll_overseas = (LinearLayout) view.findViewById(R.id.ll_overseas);
        ll_gift = (LinearLayout) view.findViewById(R.id.ll_gift);
        ll_love = (LinearLayout) view.findViewById(R.id.ll_love);
        ll_money = (LinearLayout) view.findViewById(R.id.ll_money);
        ll_water = (LinearLayout) view.findViewById(R.id.ll_water);
        iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
        iv_circle = (ImageView) view.findViewById(R.id.iv_circle);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        cv_headShot = (CircleView) view.findViewById(R.id.cv_headShot);

        ll_campus_circle.setOnClickListener(this);
//		ll_point_mall.setOnClickListener(this);
//		ll_find.setOnClickListener(this);
        ll_overseas.setOnClickListener(this);
        ll_gift.setOnClickListener(this);
        ll_love.setOnClickListener(this);
        ll_money.setOnClickListener(this);
        ll_water.setOnClickListener(this);
        isChecked = true;

        new Thread() {
            public void run() {
                userInfo = GetData.getUserInfo(MyConfig.URL_JSON_USERINFO
                        + activity.getSharedPreferences(UserInfoDB.USERTABLE,
                        activity.MODE_PRIVATE).getString(
                        UserInfoDB.USERID, ""));
                campusContactsTimeInfo = GetData
                        .getCampusContactsTimeInfo(MyConfig.URL_JSON_CAMPUS
                                + sid + "&type=0" + "&uid=" + uid + "&status=3"
                                + "&page=1");
                if (!activity.isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_campus_circle:
                tag = true;
                iv_circle.setVisibility(View.GONE);
                intent.setClass(activity, CampusContactsActivity.class);
                activity.startActivity(intent);
                break;

//		case R.id.ll_point_mall:
//			// intent.setClass(activity, PointMallActivity.class);
//			// activity.startActivity(intent);
//			intent.setClass(activity, YetOpenActivity.class);
//			activity.startActivity(intent);
//			break;

//		case R.id.ll_find:
//			intent.setClass(activity, YetOpenActivity.class);
//			activity.startActivity(intent);
//			break;

            case R.id.ll_overseas:
                intent.setClass(activity, OverseasActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_gift:
                intent.setClass(activity, GiftActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_love:
                intent.setClass(activity, LoveSecureActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_money:
                intent.setClass(activity, EmergencyActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.ll_water:
                intent.setClass(activity, QuestionSurveyActivity.class);
                activity.startActivity(intent);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (tag) {
            tag = false;
            new Thread() {
                public void run() {
                    userInfo = GetData.getUserInfo(MyConfig.URL_JSON_USERINFO
                            + activity
                            .getSharedPreferences(UserInfoDB.USERTABLE,
                                    activity.MODE_PRIVATE).getString(
                                    UserInfoDB.USERID, ""));
                    campusContactsTimeInfo = GetData
                            .getCampusContactsTimeInfo(MyConfig.URL_JSON_CAMPUS
                                    + sid + "&type=0" + "&uid=" + uid
                                    + "&status=3" + "&page=1");
                    if (!activity.isDestroyed()) {
                        handler.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    public void onEventMainThread(MEventAddFriendCount event) {
        if (event.getCount() > 0) {
            tv_count.setText(event.getCount() + "");
            tv_count.setVisibility(View.VISIBLE);
            iv_circle.setVisibility(View.GONE);
        } else {
            tv_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
