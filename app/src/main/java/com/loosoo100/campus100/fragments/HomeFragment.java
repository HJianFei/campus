package com.loosoo100.campus100.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CommActiListActivity;
import com.loosoo100.campus100.activities.CommActivityDetailFreeActivity;
import com.loosoo100.campus100.activities.CommActivityDetailTogetherActivity;
import com.loosoo100.campus100.activities.CommDetailActivity;
import com.loosoo100.campus100.activities.CommHeartActivity;
import com.loosoo100.campus100.activities.CommListActivity;
import com.loosoo100.campus100.activities.CommunityActivity;
import com.loosoo100.campus100.activities.CompanySummaryActivity;
import com.loosoo100.campus100.activities.GiftActivity;
import com.loosoo100.campus100.activities.PictureWallActivity;
import com.loosoo100.campus100.activities.SearchActivity;
import com.loosoo100.campus100.adapters.CommActiListHomeAdapter;
import com.loosoo100.campus100.adapters.CommunityBossHomeAdapter;
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.beans.ImageInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.MyScrollView;
import com.loosoo100.campus100.view.MyScrollView.OnScrollListener;
import com.loosoo100.campus100.view.pulltorefresh.OnRefreshListener;
import com.loosoo100.campus100.view.pulltorefresh.PtrLayout;
import com.loosoo100.campus100.view.pulltorefresh.header.DefaultRefreshView;
import com.loosoo100.campus100.view.viewpager.autoscroll.AutoScrollViewPager;
import com.loosoo100.campus100.zxing.activity.CaptureActivity;
import com.loosoo100.campus100.zzboss.activities.BossCompanyListActivity;
import com.loosoo100.campus100.zzboss.beans.BossCompanySummaryInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 首页fragment
 */
public class HomeFragment extends Fragment implements OnClickListener,
        OnScrollListener, OnTouchListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.tv_location01)
    public static TextView tv_location01;
    @ViewInject(R.id.tv_location02)
    public static TextView tv_location02;

    @ViewInject(R.id.viewpager_home)
    private AutoScrollViewPager viewpager_home;
    @ViewInject(R.id.iv_circle01)
    private ImageButton iv_circle01;
    @ViewInject(R.id.iv_circle02)
    private ImageButton iv_circle02;
    @ViewInject(R.id.iv_circle03)
    private ImageButton iv_circle03;

    @ViewInject(R.id.scrollview)
    private MyScrollView scrollview;
    @ViewInject(R.id.rl_topbar)
    private RelativeLayout rl_topbar;
    @ViewInject(R.id.iv_loc_bg)
    private ImageView iv_loc_bg;
    @ViewInject(R.id.iv_search_bg)
    private ImageView iv_search_bg;
    @ViewInject(R.id.iv_location)
    private ImageView iv_location;

    @ViewInject(R.id.iv_expand_pictureWall)
    private ImageView iv_expand_pictureWall;

    @ViewInject(R.id.ll_comm)
    private LinearLayout ll_comm;
    @ViewInject(R.id.ll_acti)
    private LinearLayout ll_acti;
    @ViewInject(R.id.ll_party)
    private LinearLayout ll_party;
    @ViewInject(R.id.ll_heart)
    private LinearLayout ll_heart;
    @ViewInject(R.id.btn_more_acti)
    private Button btn_more_acti;
    @ViewInject(R.id.btn_more_comm)
    private Button btn_more_comm;
    @ViewInject(R.id.btn_more_boss)
    private Button btn_more_boss;
    @ViewInject(R.id.lv_acti)
    private ListView lv_acti;
    @ViewInject(R.id.lv_boss)
    private ListView lv_boss;
    /*
     * 水平滑动社团
     */
    @ViewInject(R.id.ll_community01)
    private LinearLayout ll_community01;
    @ViewInject(R.id.cv_comm01)
    private CircleView cv_comm01;
    @ViewInject(R.id.tv_comm01)
    private TextView tv_comm01;
    @ViewInject(R.id.tv_address01)
    private TextView tv_address01;
    @ViewInject(R.id.tv_school01)
    private TextView tv_school01;
    @ViewInject(R.id.ll_community02)
    private LinearLayout ll_community02;
    @ViewInject(R.id.cv_comm02)
    private CircleView cv_comm02;
    @ViewInject(R.id.tv_comm02)
    private TextView tv_comm02;
    @ViewInject(R.id.tv_address02)
    private TextView tv_address02;
    @ViewInject(R.id.tv_school02)
    private TextView tv_school02;
    @ViewInject(R.id.ll_community03)
    private LinearLayout ll_community03;
    @ViewInject(R.id.cv_comm03)
    private CircleView cv_comm03;
    @ViewInject(R.id.tv_comm03)
    private TextView tv_comm03;
    @ViewInject(R.id.tv_address03)
    private TextView tv_address03;
    @ViewInject(R.id.tv_school03)
    private TextView tv_school03;
    @ViewInject(R.id.ll_community04)
    private LinearLayout ll_community04;
    @ViewInject(R.id.cv_comm04)
    private CircleView cv_comm04;
    @ViewInject(R.id.tv_comm04)
    private TextView tv_comm04;
    @ViewInject(R.id.tv_address04)
    private TextView tv_address04;
    @ViewInject(R.id.tv_school04)
    private TextView tv_school04;
    @ViewInject(R.id.ll_community05)
    private LinearLayout ll_community05;
    @ViewInject(R.id.cv_comm05)
    private CircleView cv_comm05;
    @ViewInject(R.id.tv_comm05)
    private TextView tv_comm05;
    @ViewInject(R.id.tv_address05)
    private TextView tv_address05;
    @ViewInject(R.id.tv_school05)
    private TextView tv_school05;
    @ViewInject(R.id.ll_community06)
    private LinearLayout ll_community06;
    @ViewInject(R.id.cv_comm06)
    private CircleView cv_comm06;
    @ViewInject(R.id.tv_comm06)
    private TextView tv_comm06;
    @ViewInject(R.id.tv_address06)
    private TextView tv_address06;
    @ViewInject(R.id.tv_school06)
    private TextView tv_school06;
    @ViewInject(R.id.ll_community07)
    private LinearLayout ll_community07;
    @ViewInject(R.id.cv_comm07)
    private CircleView cv_comm07;
    @ViewInject(R.id.tv_comm07)
    private TextView tv_comm07;
    @ViewInject(R.id.tv_address07)
    private TextView tv_address07;
    @ViewInject(R.id.tv_school07)
    private TextView tv_school07;
    @ViewInject(R.id.ll_community08)
    private LinearLayout ll_community08;
    @ViewInject(R.id.cv_comm08)
    private CircleView cv_comm08;
    @ViewInject(R.id.tv_comm08)
    private TextView tv_comm08;
    @ViewInject(R.id.tv_address08)
    private TextView tv_address08;
    @ViewInject(R.id.tv_school08)
    private TextView tv_school08;
    @ViewInject(R.id.ll_community09)
    private LinearLayout ll_community09;
    @ViewInject(R.id.cv_comm09)
    private CircleView cv_comm09;
    @ViewInject(R.id.tv_comm09)
    private TextView tv_comm09;
    @ViewInject(R.id.tv_address09)
    private TextView tv_address09;
    @ViewInject(R.id.tv_school09)
    private TextView tv_school09;

    @ViewInject(R.id.refresh)
    private PtrLayout refresh; // 刷新
    // private DefaultRefreshView footerView;
    private DefaultRefreshView headerView;

    private View view;
    private List<View> list = new ArrayList<View>();
    private ScaleAnimation scaleAnimation;

    private List<ImageInfo> imageInfos;
    private Activity activity;
    private Intent intent = new Intent();

    private List<CommunityActivityInfo> activityInfos;
    private CommActiListHomeAdapter actiAdapter;

    private List<BossCompanySummaryInfo> bossInfos;
    private CommunityBossHomeAdapter bossAdapter;

    private List<CommunityBasicInfo> commInfos;
    private String sid = "";

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initViewPager();
            if (activityInfos != null && activityInfos.size() > 0) {
                initActiListView();
            }
            if (bossInfos != null && bossInfos.size() > 0) {
                initBossListView();
            }
            if (commInfos != null) {
                initHoriCommView();
            }
        }

        ;
    };

    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            headerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh.onRefreshComplete();
                }
            }, 1000);
            if (activityInfos != null && activityInfos.size() > 0) {
                initActiListView();
            }
            if (bossInfos != null && bossInfos.size() > 0) {
                initBossListView();
            }
            if (commInfos != null) {
                initHoriCommView();
            }
        }
    };

    private Handler handlerActiRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (activityInfos != null && activityInfos.size() > 0) {
                initActiListView();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();

        EventBus.getDefault().register(this);

        ViewUtils.inject(this, view); // 注入view和事件
        sid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_ID, "");
        initView();
        MyUtils.setStatusBarHeight(activity, iv_empty);

        scaleAnimation = MyAnimation.getScaleAnimation();

        hideComm();

        new Thread() {
            public void run() {
                imageInfos = GetData
                        .getImageInfos(MyConfig.URL_JSON_HOME_PIC_TOP);
                activityInfos = GetData
                        .getBossHomeActivityInfos(MyConfig.URL_JSON_HOME_BOSS
                                + "?type=0" + "&sid=" + sid);
                bossInfos = GetData
                        .getBossHomeBossInfos(MyConfig.URL_JSON_HOME_BOSS
                                + "?type=0" + "&sid=" + sid);
                commInfos = GetData
                        .getBossHomeCommInfos(MyConfig.URL_JSON_HOME_BOSS
                                + "?type=0" + "&sid=" + sid);
                if (!activity.isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        int[] colors = new int[]{getResources().getColor(R.color.red_fd3c49)};
        headerView = new DefaultRefreshView(activity);
        headerView.setColorSchemeColors(colors);
        headerView.setIsPullDown(true);
        refresh.setHeaderView(headerView);

        refresh.setOnPullDownRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread() {
                    public void run() {
                        activityInfos = GetData
                                .getBossHomeActivityInfos(MyConfig.URL_JSON_HOME_BOSS
                                        + "?type=0" + "&sid=" + sid);
                        bossInfos = GetData
                                .getBossHomeBossInfos(MyConfig.URL_JSON_HOME_BOSS
                                        + "?type=0" + "&sid=" + sid);
                        commInfos = GetData
                                .getBossHomeCommInfos(MyConfig.URL_JSON_HOME_BOSS
                                        + "?type=0" + "&sid=" + sid);
                        if (!activity.isDestroyed()) {
                            handlerRefresh.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
            }
        });

        return view;
    }

    /**
     * 初始化控件
     */
    public void initView() {
        view.findViewById(R.id.ll_location).setOnClickListener(this);
        view.findViewById(R.id.ll_location).setOnTouchListener(this);
        view.findViewById(R.id.ll_search).setOnClickListener(this);
        ll_comm.setOnClickListener(this);
        ll_comm.setOnTouchListener(this);
        ll_acti.setOnClickListener(this);
        ll_acti.setOnTouchListener(this);
        ll_party.setOnClickListener(this);
        ll_party.setOnTouchListener(this);
        ll_heart.setOnClickListener(this);
        ll_heart.setOnTouchListener(this);
        btn_more_acti.setOnClickListener(this);
        btn_more_comm.setOnClickListener(this);
        btn_more_boss.setOnClickListener(this);
        ll_community01.setOnClickListener(this);
        ll_community02.setOnClickListener(this);
        ll_community03.setOnClickListener(this);
        ll_community04.setOnClickListener(this);
        ll_community05.setOnClickListener(this);
        ll_community06.setOnClickListener(this);
        ll_community07.setOnClickListener(this);
        ll_community08.setOnClickListener(this);
        ll_community09.setOnClickListener(this);

        scrollview.setOnScrollListener(this);

        // 扩展图片
        iv_expand_pictureWall.setOnClickListener(this);
        iv_expand_pictureWall.setOnTouchListener(this);

        String school = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL, "请选择学校");
        tv_location01.setText(school);
        tv_location02.setText(school);

        iv_expand_pictureWall.setImageResource(R.drawable.homepage_down01);

    }

    /**
     * 最热门活动列表
     */
    private void initActiListView() {
        actiAdapter = new CommActiListHomeAdapter(activity, activityInfos);
        lv_acti.setAdapter(actiAdapter);
        MyUtils.setListViewHeight(lv_acti, 0);

        lv_acti.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (activityInfos.get(position).getClassify() == 0) {
                    Intent intent = new Intent(activity,
                            CommActivityDetailTogetherActivity.class);
                    intent.putExtra("aid", activityInfos.get(position).getId());
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity,
                            CommActivityDetailFreeActivity.class);
                    intent.putExtra("aid", activityInfos.get(position).getId());
                    activity.startActivity(intent);
                }
            }
        });
    }

    ;

    /**
     * 社团
     */
    private void initHoriCommView() {
        hideComm();
        View[] lls = new View[]{ll_community01, ll_community02,
                ll_community03, ll_community04, ll_community05, ll_community06,
                ll_community07, ll_community08, ll_community09};
        CircleView[] cvs = new CircleView[]{cv_comm01, cv_comm02, cv_comm03,
                cv_comm04, cv_comm05, cv_comm06, cv_comm07, cv_comm08,
                cv_comm09};
        TextView[] comms = new TextView[]{tv_comm01, tv_comm02, tv_comm03,
                tv_comm04, tv_comm05, tv_comm06, tv_comm07, tv_comm08,
                tv_comm09};
        TextView[] adds = new TextView[]{tv_address01, tv_address02,
                tv_address03, tv_address04, tv_address05, tv_address06,
                tv_address07, tv_address08, tv_address09};
        TextView[] schs = new TextView[]{tv_school01, tv_school02,
                tv_school03, tv_school04, tv_school05, tv_school06,
                tv_school07, tv_school08, tv_school09};
        for (int i = 0; i < commInfos.size(); i++) {
            lls[i].setVisibility(View.VISIBLE);
            Glide.with(activity).load(commInfos.get(i).getHeadthumb())
                    .into(cvs[i]);
            comms[i].setText(commInfos.get(i).getCommunityName());
            adds[i].setText(commInfos.get(i).getCity());
            schs[i].setText(commInfos.get(i).getSchool());
        }

    }

    /**
     * 企业列表
     */
    private void initBossListView() {
        bossAdapter = new CommunityBossHomeAdapter(activity, bossInfos);
        lv_boss.setAdapter(bossAdapter);
        MyUtils.setListViewHeight(lv_boss, 0);

        lv_boss.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(activity,
                        CompanySummaryActivity.class);
                intent.putExtra("cuid", bossInfos.get(position).getId());
//				intent.putExtra("commLeader", false);
                activity.startActivity(intent);
            }
        });
    }

    ;

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        int[] images = new int[]{R.drawable.homepage01,
                R.drawable.homepage02, R.drawable.homepage03};
        ImageView imageView = new ImageView(activity);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        if (imageInfos != null && imageInfos.size() > 0) {
            Glide.with(activity).load(imageInfos.get(0).getUrl()).dontAnimate()
                    .into(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(activity, WebActivity.class);
                    // intent.putExtra("url", imageInfos.get(0).getGoUrl());
                    // startActivity(intent);
                }
            });
        } else {
            imageView.setImageBitmap(GetData.getBitMap(activity, images[0]));
            // imageView.setImageResource(images[0]);
        }
        list.add(imageView);

        ImageView imageView2 = new ImageView(activity);
        imageView2.setScaleType(ScaleType.CENTER_CROP);
        if (imageInfos != null && imageInfos.size() > 1) {
            Glide.with(activity).load(imageInfos.get(1).getUrl()).dontAnimate()
                    .into(imageView2);
            imageView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(activity, WebActivity.class);
                    // intent.putExtra("url", imageInfos.get(1).getGoUrl());
                    // startActivity(intent);
                }
            });
        } else {
            imageView2.setImageBitmap(GetData.getBitMap(activity, images[1]));
            // imageView2.setImageResource(images[1]);
        }
        list.add(imageView2);

        ImageView imageView3 = new ImageView(activity);
        imageView3.setScaleType(ScaleType.CENTER_CROP);
        if (imageInfos != null && imageInfos.size() > 2) {
            Glide.with(activity).load(imageInfos.get(2).getUrl()).dontAnimate()
                    .into(imageView3);
            imageView3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(activity, WebActivity.class);
                    // intent.putExtra("url", imageInfos.get(2).getGoUrl());
                    // startActivity(intent);
                }
            });
        } else {
            imageView3.setImageBitmap(GetData.getBitMap(activity, images[2]));
            // imageView3.setImageResource(images[2]);
        }
        list.add(imageView3);
        // 设置轮播时间
        viewpager_home.setInterval(4000);
        // 设置是否循环，默认是
        viewpager_home.setCycle(true);
        // 设置轮播方向，默认向右
        viewpager_home.setDirection(AutoScrollViewPager.RIGHT);
        // 设置过渡时间
        viewpager_home.setAutoScrollDurationFactor(10);
        // 设置最后一张跳到第一张时是否有动画
        viewpager_home.setBorderAnimation(false);
        viewpager_home.setAdapter(new ViewPagerAdapter(list));
        // 开始自动轮播
        viewpager_home.startAutoScroll();

        viewpager_home.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0 % 3) {
                    case 0:
                        resetCircleImageColor();
                        iv_circle01.setImageResource(R.drawable.circle_point_color);
                        break;

                    case 1:
                        resetCircleImageColor();
                        iv_circle02.setImageResource(R.drawable.circle_point_color);
                        break;

                    case 2:
                        resetCircleImageColor();
                        iv_circle03.setImageResource(R.drawable.circle_point_color);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 设置轮播小圆点的初始化颜色
     */
    public void resetCircleImageColor() {
        iv_circle01.setImageResource(R.drawable.circle_point);
        iv_circle02.setImageResource(R.drawable.circle_point);
        iv_circle03.setImageResource(R.drawable.circle_point);
    }

    /**
     * 监听点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 定位
            case R.id.ll_location:
                intent.setClass(activity, SearchActivity.class);
                activity.startActivityForResult(intent, MyConfig.SCHOOL_CODE_HOME);
                iv_location.clearAnimation();
                break;

            // 搜索按钮
            case R.id.ll_search:
                // intent.setClass(activity, HomeCommActiSearchActivity.class);
                // activity.startActivity(intent);
                intent.setClass(activity, CaptureActivity.class);
                activity.startActivityForResult(intent, MyConfig.QRCODE);
                break;

            // 社团
            case R.id.ll_comm:
                intent.setClass(activity, CommunityActivity.class);
                activity.startActivity(intent);
                ll_comm.clearAnimation();
                break;

            // 社团活动
            case R.id.ll_acti:
                intent.setClass(activity, CommActiListActivity.class);
                intent.putExtra("cityId", "");
                intent.putExtra("content", "");
                activity.startActivity(intent);
                ll_acti.clearAnimation();
                break;

            // 聚会邂逅
            case R.id.ll_party:
                intent.setClass(activity, GiftActivity.class);
                activity.startActivity(intent);
                ll_party.clearAnimation();
                break;

            // 爱心公益
            case R.id.ll_heart:
                intent.setClass(activity, CommHeartActivity.class);
                activity.startActivity(intent);
                ll_heart.clearAnimation();
                break;

            // 扩展图片-照片墙
            case R.id.iv_expand_pictureWall:
                intent.setClass(activity, PictureWallActivity.class);
                activity.startActivity(intent);
                iv_expand_pictureWall.clearAnimation();
                break;

            // 更多活动
            case R.id.btn_more_acti:
                intent.setClass(activity, CommActiListActivity.class);
                intent.putExtra("cityId", "");
                intent.putExtra("content", "");
                activity.startActivity(intent);
                break;

            // 更多社团
            case R.id.btn_more_comm:
                intent.setClass(activity, CommListActivity.class);
                activity.startActivity(intent);
                break;

            // 更多企业
            case R.id.btn_more_boss:
                intent.setClass(activity, BossCompanyListActivity.class);
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community01:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(0).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community02:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(1).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community03:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(2).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community04:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(3).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community05:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(4).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community06:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(5).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community07:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(6).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community08:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(7).getId());
                activity.startActivity(intent);
                break;

            // 水平滑动社团
            case R.id.ll_community09:
                intent.setClass(activity, CommDetailActivity.class);
                intent.putExtra("id", commInfos.get(8).getId());
                activity.startActivity(intent);
                break;

        }
    }

    /**
     * 监听滑动改变透明度
     */
    @Override
    public void onScroll(int scrollY) {
        rl_topbar.setAlpha((float) scrollY / 500);
        iv_empty.setAlpha((float) scrollY / 500);
        tv_location01.setAlpha(0.3f - (float) scrollY / 500);
        iv_loc_bg.setAlpha(0.3f - (float) scrollY / 500);
        iv_search_bg.setAlpha(0.3f - (float) scrollY / 500);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 如果是按下动作则开始缩放动画
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.ll_location:
                    iv_location.startAnimation(MyAnimation
                            .getScaleAnimationLocation());
                    break;

                case R.id.ll_comm:
                    ll_comm.startAnimation(scaleAnimation);
                    break;

                case R.id.ll_acti:
                    ll_acti.startAnimation(scaleAnimation);
                    break;

                case R.id.ll_party:
                    ll_party.startAnimation(scaleAnimation);
                    break;

                case R.id.ll_heart:
                    ll_heart.startAnimation(scaleAnimation);
                    break;

                case R.id.iv_expand_pictureWall:
                    iv_expand_pictureWall.startAnimation(scaleAnimation);
                    break;
            }
        }
        return false;
    }

    /**
     * 活动支付更新活动数据
     *
     * @param event
     */
    public void onEventMainThread(MEventCommActiPay event) {
        if (event.isChange()) {
            new Thread() {
                public void run() {
                    activityInfos = GetData
                            .getBossHomeActivityInfos(MyConfig.URL_JSON_HOME_BOSS
                                    + "?type=0" + "&sid=" + sid);
                    if (!activity.isDestroyed()) {
                        handlerActiRefresh.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // //开始自动轮播
        viewpager_home.startAutoScroll();
    }

    @Override
    public void onPause() {
        // 停止自动轮播
        viewpager_home.stopAutoScroll();
        super.onPause();
    }

    private void hideComm() {
        ll_community01.setVisibility(View.GONE);
        ll_community02.setVisibility(View.GONE);
        ll_community03.setVisibility(View.GONE);
        ll_community04.setVisibility(View.GONE);
        ll_community05.setVisibility(View.GONE);
        ll_community06.setVisibility(View.GONE);
        ll_community07.setVisibility(View.GONE);
        ll_community08.setVisibility(View.GONE);
        ll_community09.setVisibility(View.GONE);
    }

}
