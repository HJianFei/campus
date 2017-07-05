package com.loosoo100.campus100.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
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
import com.loosoo100.campus100.activities.HomeActivity;
import com.loosoo100.campus100.activities.PayStoreActivity;
import com.loosoo100.campus100.activities.SearchActivity;
import com.loosoo100.campus100.adapters.CartAdapter;
import com.loosoo100.campus100.adapters.GoodsListAdapter01;
import com.loosoo100.campus100.adapters.GoodsListAdapter02;
import com.loosoo100.campus100.adapters.GoodsListAdapter03;
import com.loosoo100.campus100.adapters.GoodsListAdapter04;
import com.loosoo100.campus100.adapters.GoodsListAdapter05;
import com.loosoo100.campus100.adapters.GoodsListAdapter06;
import com.loosoo100.campus100.adapters.GoodsListAdapter07;
import com.loosoo100.campus100.adapters.GoodsListAdapter08;
import com.loosoo100.campus100.adapters.GoodsListAdapter09;
import com.loosoo100.campus100.adapters.GoodsListAdapter10;
import com.loosoo100.campus100.adapters.GoodsListAdapter11;
import com.loosoo100.campus100.adapters.GoodsListAdapter12;
import com.loosoo100.campus100.adapters.GoodsListAdapter13;
import com.loosoo100.campus100.adapters.GoodsListAdapter14;
import com.loosoo100.campus100.adapters.GoodsListAdapter15;
import com.loosoo100.campus100.adapters.MarketCategoryAdapter;
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.anyevent.MEventStoreCart;
import com.loosoo100.campus100.beans.BusinessInfo;
import com.loosoo100.campus100.beans.CartInfo;
import com.loosoo100.campus100.beans.CategoryInfo;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author yang 小卖部fragment
 */
public class StoreFragment extends Fragment implements OnClickListener,
        OnScrollListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_business)
    private RelativeLayout rl_business; // 商家信息按钮

    @ViewInject(R.id.ll_fragment_business)
    public static LinearLayout ll_activity_business; // 商家信息界面

    @ViewInject(R.id.ib_exit)
    private ImageButton ib_exit; // 商家信息关闭按钮

    @ViewInject(R.id.rl_activity_goods)
    public static RelativeLayout rl_activity_goods; // 商品详情界面

    @ViewInject(R.id.ib_exit_goods)
    private ImageButton ib_exit_goods; // 商品详情关闭按钮

    @ViewInject(R.id.rl_goods_page)
    private RelativeLayout rl_goods_page; // 商品详情暗部分

    @ViewInject(R.id.vp_goods)
    public static ViewPager vp_goods; // 商品详情图片
    @ViewInject(R.id.tv_index)
    public static TextView tv_index; // 商品详情序号

    @ViewInject(R.id.tv_goodsName)
    public static TextView tv_goodsName; // 商品详情商品名

    @ViewInject(R.id.tv_goodSold)
    public static TextView tv_goodSold; // 商品详情已售出

    @ViewInject(R.id.tv_goodsStock)
    public static TextView tv_goodsStock; // 商品详情库存

    @ViewInject(R.id.tv_goodsPrice)
    public static TextView tv_goodsPrice; // 商品详情价格

    @ViewInject(R.id.ib_add_ll)
    public static ImageButton ib_add_ll; // 商品详情添加按钮

    @ViewInject(R.id.ib_reduce_ll)
    public static ImageButton ib_reduce_ll; // 商品详情减少按钮

    @ViewInject(R.id.ib_add_red)
    public static ImageButton ib_add_red; // 商品详情单独添加按钮

    @ViewInject(R.id.ll_addOrReduce)
    public static LinearLayout ll_addOrReduce; // 商品详情加减按钮布局

    @ViewInject(R.id.tv_goodsCount)
    public static TextView tv_goodsCount; // 商品详情商品添加数量

    @ViewInject(R.id.ll_clear_cart)
    private LinearLayout ll_clear_cart; // 清除购物车按钮

    @ViewInject(R.id.btn_select_ok)
    public static Button btn_select_ok; // 结算按钮

    @ViewInject(R.id.ll_cart)
    public static LinearLayout ll_cart; // 购物车布局按钮

    @ViewInject(R.id.tv_totalMoney)
    public static TextView tv_totalMoney; // 购物车总价

    @ViewInject(R.id.tv_expressFee)
    public static TextView tv_expressFee; // 配送费

    @ViewInject(R.id.tv_count_cart)
    public static TextView tv_count_cart; // 购物车件数
    // @ViewInject(R.id.tv_count_cart2)
    // private TextView tv_count_cart2; // 购物车件数

    @ViewInject(R.id.tv_category_top)
    private TextView tv_category_top; // 顶部分类名称

    @ViewInject(R.id.btn_campusName)
    private Button btn_campusName; // 学校名按钮

    @ViewInject(R.id.lv_category)
    private ListView lv_category; // 商品分类列表

    private MarketCategoryAdapter categoryAdapter;

    @ViewInject(R.id.lv_goods01)
    private ListView lv_goods01; // 商品名列表
    @ViewInject(R.id.lv_goods02)
    private ListView lv_goods02; // 商品名列表
    @ViewInject(R.id.lv_goods03)
    private ListView lv_goods03; // 商品名列表
    @ViewInject(R.id.lv_goods04)
    private ListView lv_goods04; // 商品名列表
    @ViewInject(R.id.lv_goods05)
    private ListView lv_goods05; // 商品名列表
    @ViewInject(R.id.lv_goods06)
    private ListView lv_goods06; // 商品名列表
    @ViewInject(R.id.lv_goods07)
    private ListView lv_goods07; // 商品名列表
    @ViewInject(R.id.lv_goods08)
    private ListView lv_goods08; // 商品名列表
    @ViewInject(R.id.lv_goods09)
    private ListView lv_goods09; // 商品名列表
    @ViewInject(R.id.lv_goods10)
    private ListView lv_goods10; // 商品名列表
    @ViewInject(R.id.lv_goods11)
    private ListView lv_goods11; // 商品名列表
    @ViewInject(R.id.lv_goods12)
    private ListView lv_goods12; // 商品名列表
    @ViewInject(R.id.lv_goods13)
    private ListView lv_goods13; // 商品名列表
    @ViewInject(R.id.lv_goods14)
    private ListView lv_goods14; // 商品名列表
    @ViewInject(R.id.lv_goods15)
    private ListView lv_goods15; // 商品名列表

    @ViewInject(R.id.lv_cart)
    private ListView lv_cart; // 购物车列表

    @ViewInject(R.id.rl_cart)
    private RelativeLayout rl_cart; // 购物车布局

    @ViewInject(R.id.rl_cart_page)
    public static RelativeLayout rl_cart_page; // 购物车整个页面布局

    // @ViewInject(R.id.refresh)
    // private PtrLayout refresh; // 上拉加载更多
    // private DefaultRefreshView footerView;

    public static GoodsListAdapter01 goodsAdapter01;
    public static GoodsListAdapter02 goodsAdapter02;
    public static GoodsListAdapter03 goodsAdapter03;
    public static GoodsListAdapter04 goodsAdapter04;
    public static GoodsListAdapter05 goodsAdapter05;
    public static GoodsListAdapter06 goodsAdapter06;
    public static GoodsListAdapter07 goodsAdapter07;
    public static GoodsListAdapter08 goodsAdapter08;
    public static GoodsListAdapter09 goodsAdapter09;
    public static GoodsListAdapter10 goodsAdapter10;
    public static GoodsListAdapter11 goodsAdapter11;
    public static GoodsListAdapter12 goodsAdapter12;
    public static GoodsListAdapter13 goodsAdapter13;
    public static GoodsListAdapter14 goodsAdapter14;
    public static GoodsListAdapter15 goodsAdapter15;

    private List<GoodsInfo> goodsInfoData01;
    private List<GoodsInfo> goodsInfoData02;
    private List<GoodsInfo> goodsInfoData03;
    private List<GoodsInfo> goodsInfoData04;
    private List<GoodsInfo> goodsInfoData05;
    private List<GoodsInfo> goodsInfoData06;
    private List<GoodsInfo> goodsInfoData07;
    private List<GoodsInfo> goodsInfoData08;
    private List<GoodsInfo> goodsInfoData09;
    private List<GoodsInfo> goodsInfoData10;
    private List<GoodsInfo> goodsInfoData11;
    private List<GoodsInfo> goodsInfoData12;
    private List<GoodsInfo> goodsInfoData13;
    private List<GoodsInfo> goodsInfoData14;
    private List<GoodsInfo> goodsInfoData15;

    private List<GoodsInfo> goodsInfos; // 加载更多的商品
    // private BusinessInfo businessInfo; // 商铺信息

    public static List<CategoryInfo> goodsCategoryData = new ArrayList<CategoryInfo>();

    // public static float send; // 最低多少元起送

    // private float expressFee; // 配送费

    public static int position = 0; // 记录点击商品位置
    public static int positionCategory = 0; // 记录点击商品分类位置

    private Intent intent;

    private int count; // 获取购物车某商品的数量

    @ViewInject(R.id.rl_pb)
    private RelativeLayout rl_pb; // 加载进度条
    @ViewInject(R.id.rl_again)
    private RelativeLayout rl_again; // 重新加载布局
    @ViewInject(R.id.btn_again)
    private Button btn_again; // 点击重新加载

    @ViewInject(R.id.progress_lv)
    private RelativeLayout progress_lv; // 商品加载进度条
    @ViewInject(R.id.progress)
    private SpinKitView progress; // 商品加载进度条

    private int i = 0; // 滑动viewpager

    // @ViewInject(R.id.iv_circle01)
    // public static ImageButton iv_circle01;
    //
    // @ViewInject(R.id.iv_circle02)
    // public static ImageButton iv_circle02;
    //
    // @ViewInject(R.id.iv_circle03)
    // public static ImageButton iv_circle03;

    @ViewInject(R.id.rl_cart_root)
    public static RelativeLayout rl_cart_root;

    @ViewInject(R.id.rl_cart_root_pay)
    public static RelativeLayout rl_cart_root_pay;

    /**
     * 商铺信息
     */
    @ViewInject(R.id.tv_campus)
    private TextView tv_campus;
    @ViewInject(R.id.tv_business_hours)
    private TextView tv_business_hours;
    @ViewInject(R.id.tv_sendAtLeastMoney)
    private TextView tv_sendAtLeastMoney;
    @ViewInject(R.id.tv_deliverMoney)
    private TextView tv_deliverMoney;
    @ViewInject(R.id.tv_sendTime)
    private TextView tv_sendTime;
    @ViewInject(R.id.tv_business)
    private TextView tv_business;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;

    private View view;

    private static HomeActivity activity;

    private boolean isStop = false;
    private boolean isLoading = true;
    // public static boolean isPay = false;
//    public static boolean isChange = false;

    private int page01 = 2;
    private int page02 = 2;
    private int page03 = 2;
    private int page04 = 2;
    private int page05 = 2;
    private int page06 = 2;
    private int page07 = 2;
    private int page08 = 2;
    private int page09 = 2;
    private int page10 = 2;
    private int page11 = 2;
    private int page12 = 2;
    private int page13 = 2;
    private int page14 = 2;
    private int page15 = 2;

    private String sid = ""; // 学校ID
    private String school = "";
    public static boolean isWork = false; // 判断商家是否营业中

    public static List<Map<String, Object>> cartList = new ArrayList<Map<String, Object>>();

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            rl_pb.setVisibility(View.GONE);
            if (goodsCategoryData != null && goodsCategoryData.size() > 0) {
                rl_again.setVisibility(View.GONE);
                initListViewCategory();
                initListViewGoods01();
                initShop();
                rl_business.setClickable(true);
            } else {
                rl_again.setVisibility(View.VISIBLE);
            }
            isLoading = false;
            btn_campusName.setClickable(true);

        }

    };
    private Handler goodsHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress_lv.setVisibility(View.GONE);
            switch (positionCategory) {
                case 0:
                    initListViewGoods01();
                    break;
                case 1:
                    initListViewGoods02();
                    break;
                case 2:
                    initListViewGoods03();
                    break;
                case 3:
                    initListViewGoods04();
                    break;
                case 4:
                    initListViewGoods05();
                    break;
                case 5:
                    initListViewGoods06();
                    break;
                case 6:
                    initListViewGoods07();
                    break;
                case 7:
                    initListViewGoods08();
                    break;
                case 8:
                    initListViewGoods09();
                    break;
                case 9:
                    initListViewGoods10();
                    break;
                case 10:
                    initListViewGoods11();
                    break;
                case 11:
                    initListViewGoods12();
                    break;
                case 12:
                    initListViewGoods13();
                    break;
                case 13:
                    initListViewGoods14();
                    break;
                case 14:
                    initListViewGoods15();
                    break;
            }
        }

        ;
    };

    /*
     * 加载更多后更新数据
     */
    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            isLoading = false;
            switch (positionCategory + 1) {
                case 1:
                    if (goodsAdapter01 != null) {
                        goodsAdapter01.notifyDataSetChanged();
                    }
                    page01++;
                    break;
                case 2:
                    if (goodsAdapter02 != null) {
                        goodsAdapter02.notifyDataSetChanged();
                    }
                    page02++;
                    break;
                case 3:
                    if (goodsAdapter03 != null) {
                        goodsAdapter03.notifyDataSetChanged();
                    }
                    page03++;
                    break;
                case 4:
                    if (goodsAdapter04 != null) {
                        goodsAdapter04.notifyDataSetChanged();
                    }
                    page04++;
                    break;
                case 5:
                    if (goodsAdapter05 != null) {
                        goodsAdapter05.notifyDataSetChanged();
                    }
                    page05++;
                    break;
                case 6:
                    if (goodsAdapter06 != null) {
                        goodsAdapter06.notifyDataSetChanged();
                    }
                    page06++;
                    break;
                case 7:
                    if (goodsAdapter07 != null) {
                        goodsAdapter07.notifyDataSetChanged();
                    }
                    page07++;
                    break;
                case 8:
                    if (goodsAdapter08 != null) {
                        goodsAdapter08.notifyDataSetChanged();
                    }
                    page08++;
                    break;
                case 9:
                    if (goodsAdapter09 != null) {
                        goodsAdapter09.notifyDataSetChanged();
                    }
                    page09++;
                    break;
                case 10:
                    if (goodsAdapter10 != null) {
                        goodsAdapter10.notifyDataSetChanged();
                    }
                    page10++;
                    break;
                case 11:
                    if (goodsAdapter11 != null) {
                        goodsAdapter11.notifyDataSetChanged();
                    }
                    page11++;
                    break;
                case 12:
                    if (goodsAdapter12 != null) {
                        goodsAdapter12.notifyDataSetChanged();
                    }
                    page12++;
                    break;
                case 13:
                    if (goodsAdapter13 != null) {
                        goodsAdapter13.notifyDataSetChanged();
                    }
                    page13++;
                    break;
                case 14:
                    if (goodsAdapter14 != null) {
                        goodsAdapter14.notifyDataSetChanged();
                    }
                    page14++;
                    break;
                case 15:
                    if (goodsAdapter15 != null) {
                        goodsAdapter15.notifyDataSetChanged();
                    }
                    page15++;
                    break;
            }

        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, container, false);
        activity = (HomeActivity) getActivity();
        ViewUtils.inject(this, view); // 注入view和事件

        EventBus.getDefault().register(this);
        sid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID_STORE, "");
//        isChange = false;
        if (cartList != null) {
            cartList.clear();
        }
        CartInfo.resetData();
        isWork = false;
        updateView(0);
        initView();

        rl_business.setClickable(false);

        MyUtils.setStatusBarHeight(activity, iv_empty);
        // 设置加载动画样式
        Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
        rl_pb.setVisibility(View.VISIBLE);
        rl_again.setVisibility(View.GONE);
        intent = new Intent();

        inflater = LayoutInflater.from(activity);
        updateShopData();

        return view;
    }

    /**
     * 初始化商铺信息
     */
    private void initShop() {
        tv_campus.setText(school);
        tv_business_hours.setText("营业时间：" + BusinessInfo.startTime + "-"
                + BusinessInfo.endTime);
        tv_sendAtLeastMoney.setText(BusinessInfo.sendAtLeastMoney + "元");
        tv_deliverMoney.setText(BusinessInfo.expressFee + "元");
        tv_sendTime.setText(BusinessInfo.deliverTime + "分钟");
        tv_business.setText(BusinessInfo.shopName);
        tv_phone.setText(BusinessInfo.phone);
        tv_address.setText(BusinessInfo.address);
        if (isWork) {
            // 设置多少钱起送
            btn_select_ok.setText(BusinessInfo.sendAtLeastMoney + "元起送");
        } else {
            btn_select_ok.setText("休息中");
            btn_select_ok.setClickable(false);
            btn_select_ok.setBackgroundColor(activity.getResources().getColor(
                    R.color.gray_3e3e3e));
        }
        // 设置配送费
        tv_expressFee.setText("￥" + BusinessInfo.expressFee);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_business.setOnClickListener(this);
        btn_again.setOnClickListener(this);
        ll_clear_cart.setOnClickListener(this);
        ib_exit.setOnClickListener(this);
        ib_exit_goods.setOnClickListener(this);
        btn_campusName.setOnClickListener(this);
        school = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_STORE, "");
        if (school.equals("")) {
            btn_campusName.setText("请选择学校");
        } else {
            btn_campusName.setText(school);
        }

        btn_select_ok.setOnClickListener(this);
        btn_select_ok.setClickable(false);

        ll_cart.setOnClickListener(this);
        rl_cart_page.setOnClickListener(this);
        ib_add_ll.setOnClickListener(this);
        ib_reduce_ll.setOnClickListener(this);
        ib_add_red.setOnClickListener(this);
        rl_goods_page.setOnClickListener(this);

        rl_cart_root.setOnClickListener(this);
        rl_cart_root_pay.setOnClickListener(this);

        rl_pb.setVisibility(View.VISIBLE);

        vp_goods.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        // i = 1;
                        tv_index.setText("1/" + i);
                        // resetCircleImageColor();
                        // iv_circle01.setImageResource(R.drawable.circle_point_color);
                        break;

                    case 1:
                        tv_index.setText("2/" + i);
                        // i = 2;
                        // resetCircleImageColor();
                        // iv_circle02.setImageResource(R.drawable.circle_point_color);
                        break;

                    case 2:
                        tv_index.setText("3/" + i);
                        // i = 3;
                        // resetCircleImageColor();
                        // iv_circle03.setImageResource(R.drawable.circle_point_color);
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
        ImageView imageView = new ImageView(activity);
        imageView.setClickable(false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 300);
        imageView.setLayoutParams(params);

        lv_goods01.addFooterView(imageView);
        lv_goods02.addFooterView(imageView);
        lv_goods03.addFooterView(imageView);
        lv_goods04.addFooterView(imageView);
        lv_goods05.addFooterView(imageView);
        lv_goods06.addFooterView(imageView);
        lv_goods07.addFooterView(imageView);
        lv_goods08.addFooterView(imageView);
        lv_goods09.addFooterView(imageView);
        lv_goods10.addFooterView(imageView);
        lv_goods11.addFooterView(imageView);
        lv_goods12.addFooterView(imageView);
        lv_goods13.addFooterView(imageView);
        lv_goods14.addFooterView(imageView);
        lv_goods15.addFooterView(imageView);

        lv_goods01.setOnScrollListener(this);
        lv_goods02.setOnScrollListener(this);
        lv_goods03.setOnScrollListener(this);
        lv_goods04.setOnScrollListener(this);
        lv_goods05.setOnScrollListener(this);
        lv_goods06.setOnScrollListener(this);
        lv_goods07.setOnScrollListener(this);
        lv_goods08.setOnScrollListener(this);
        lv_goods09.setOnScrollListener(this);
        lv_goods10.setOnScrollListener(this);
        lv_goods11.setOnScrollListener(this);
        lv_goods12.setOnScrollListener(this);
        lv_goods13.setOnScrollListener(this);
        lv_goods14.setOnScrollListener(this);
        lv_goods15.setOnScrollListener(this);

    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods01() {
        hideGoods();
        if (goodsAdapter01 == null && goodsInfoData01 != null
                && goodsInfoData01.size() > 0) {
            goodsAdapter01 = new GoodsListAdapter01(activity, goodsInfoData01);
            lv_goods01.setAdapter(goodsAdapter01);
        }
        if (goodsAdapter01 != null) {
            lv_goods01.setVisibility(View.VISIBLE);
        } else {
            lv_goods01.setVisibility(View.GONE);
        }

        lv_goods01.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData01.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData01);
            }
        });

    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods02() {
        hideGoods();
        if (goodsAdapter02 == null && goodsInfoData02 != null
                && goodsInfoData02.size() > 0) {
            goodsAdapter02 = new GoodsListAdapter02(activity, goodsInfoData02);
            lv_goods02.setAdapter(goodsAdapter02);
        }
        if (goodsAdapter02 != null) {
            lv_goods02.setVisibility(View.VISIBLE);
        } else {
            lv_goods02.setVisibility(View.GONE);
        }

        lv_goods02.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData02.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData02);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods03() {
        hideGoods();
        if (goodsAdapter03 == null && goodsInfoData03 != null
                && goodsInfoData03.size() > 0) {
            goodsAdapter03 = new GoodsListAdapter03(activity, goodsInfoData03);
            lv_goods03.setAdapter(goodsAdapter03);
        }
        if (goodsAdapter03 != null) {
            lv_goods03.setVisibility(View.VISIBLE);
        } else {
            lv_goods03.setVisibility(View.GONE);
        }

        lv_goods03.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData03.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData03);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods04() {
        hideGoods();
        if (goodsAdapter04 == null && goodsInfoData04 != null
                && goodsInfoData04.size() > 0) {
            goodsAdapter04 = new GoodsListAdapter04(activity, goodsInfoData04);
            lv_goods04.setAdapter(goodsAdapter04);
        }
        if (goodsAdapter04 != null) {
            lv_goods04.setVisibility(View.VISIBLE);
        } else {
            lv_goods04.setVisibility(View.GONE);
        }
        lv_goods04.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData04.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData04);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods05() {
        hideGoods();
        if (goodsAdapter05 == null && goodsInfoData05 != null
                && goodsInfoData05.size() > 0) {
            goodsAdapter05 = new GoodsListAdapter05(activity, goodsInfoData05);
            lv_goods05.setAdapter(goodsAdapter05);
        }
        if (goodsAdapter05 != null) {
            lv_goods05.setVisibility(View.VISIBLE);
        } else {
            lv_goods05.setVisibility(View.GONE);
        }
        lv_goods05.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData05.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData05);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods06() {
        hideGoods();
        if (goodsAdapter06 == null && goodsInfoData06 != null
                && goodsInfoData06.size() > 0) {
            goodsAdapter06 = new GoodsListAdapter06(activity, goodsInfoData06);
            lv_goods06.setAdapter(goodsAdapter06);
        }
        if (goodsAdapter06 != null) {
            lv_goods06.setVisibility(View.VISIBLE);
        } else {
            lv_goods06.setVisibility(View.GONE);
        }
        lv_goods06.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData06.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData06);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods07() {
        hideGoods();
        if (goodsAdapter07 == null && goodsInfoData07 != null
                && goodsInfoData07.size() > 0) {
            goodsAdapter07 = new GoodsListAdapter07(activity, goodsInfoData07);
            lv_goods07.setAdapter(goodsAdapter07);
        }
        if (goodsAdapter07 != null) {
            lv_goods07.setVisibility(View.VISIBLE);
        } else {
            lv_goods07.setVisibility(View.GONE);
        }
        lv_goods07.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData07.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData07);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods08() {
        hideGoods();
        if (goodsAdapter08 == null && goodsInfoData08 != null
                && goodsInfoData08.size() > 0) {
            goodsAdapter08 = new GoodsListAdapter08(activity, goodsInfoData08);
            lv_goods08.setAdapter(goodsAdapter08);
        }
        if (goodsAdapter08 != null) {
            lv_goods08.setVisibility(View.VISIBLE);
        } else {
            lv_goods08.setVisibility(View.GONE);
        }
        lv_goods08.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData08.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData08);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods09() {
        hideGoods();
        if (goodsAdapter09 == null && goodsInfoData09 != null
                && goodsInfoData09.size() > 0) {
            goodsAdapter09 = new GoodsListAdapter09(activity, goodsInfoData09);
            lv_goods09.setAdapter(goodsAdapter09);
        }
        if (goodsAdapter09 != null) {
            lv_goods09.setVisibility(View.VISIBLE);
        } else {
            lv_goods09.setVisibility(View.GONE);
        }
        lv_goods09.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData09.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData09);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods10() {
        hideGoods();
        if (goodsAdapter10 == null && goodsInfoData10 != null
                && goodsInfoData10.size() > 0) {
            goodsAdapter10 = new GoodsListAdapter10(activity, goodsInfoData10);
            lv_goods10.setAdapter(goodsAdapter10);
        }
        if (goodsAdapter10 != null) {
            lv_goods10.setVisibility(View.VISIBLE);
        } else {
            lv_goods10.setVisibility(View.GONE);
        }
        lv_goods10.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData10.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData10);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods11() {
        hideGoods();
        if (goodsAdapter11 == null && goodsInfoData11 != null
                && goodsInfoData11.size() > 0) {
            goodsAdapter11 = new GoodsListAdapter11(activity, goodsInfoData11);
            lv_goods11.setAdapter(goodsAdapter11);
        }
        if (goodsAdapter11 != null) {
            lv_goods11.setVisibility(View.VISIBLE);
        } else {
            lv_goods11.setVisibility(View.GONE);
        }
        lv_goods11.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData11.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData11);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods12() {
        hideGoods();
        if (goodsAdapter12 == null && goodsInfoData12 != null
                && goodsInfoData12.size() > 0) {
            goodsAdapter12 = new GoodsListAdapter12(activity, goodsInfoData12);
            lv_goods12.setAdapter(goodsAdapter12);
        }
        if (goodsAdapter12 != null) {
            lv_goods12.setVisibility(View.VISIBLE);
        } else {
            lv_goods12.setVisibility(View.GONE);
        }
        lv_goods12.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData12.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData12);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods13() {
        hideGoods();
        if (goodsAdapter13 == null && goodsInfoData13 != null
                && goodsInfoData13.size() > 0) {
            goodsAdapter13 = new GoodsListAdapter13(activity, goodsInfoData13);
            lv_goods13.setAdapter(goodsAdapter13);
        }
        if (goodsAdapter13 != null) {
            lv_goods13.setVisibility(View.VISIBLE);
        } else {
            lv_goods13.setVisibility(View.GONE);
        }
        lv_goods13.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData13.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData13);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods14() {
        hideGoods();
        if (goodsAdapter14 == null && goodsInfoData14 != null
                && goodsInfoData14.size() > 0) {
            goodsAdapter14 = new GoodsListAdapter14(activity, goodsInfoData14);
            lv_goods14.setAdapter(goodsAdapter14);
        }
        if (goodsAdapter14 != null) {
            lv_goods14.setVisibility(View.VISIBLE);
        } else {
            lv_goods14.setVisibility(View.GONE);
        }
        lv_goods14.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData14.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData14);
            }
        });
    }

    /**
     * 初始化商品列表
     */
    private void initListViewGoods15() {
        hideGoods();
        if (goodsAdapter15 == null && goodsInfoData15 != null
                && goodsInfoData15.size() > 0) {
            goodsAdapter15 = new GoodsListAdapter15(activity, goodsInfoData15);
            lv_goods15.setAdapter(goodsAdapter15);
        }
        if (goodsAdapter15 != null) {
            lv_goods15.setVisibility(View.VISIBLE);
        } else {
            lv_goods15.setVisibility(View.GONE);
        }
        lv_goods15.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == goodsInfoData15.size()) {
                    return;
                }
                StoreFragment.position = position;
                showGoodsDetail(goodsInfoData15);
            }
        });
    }

    /**
     * 初始化商品分类列表
     */
    private void initListViewCategory() {
        categoryAdapter = new MarketCategoryAdapter(activity, goodsCategoryData);
        lv_category.setAdapter(categoryAdapter);
        MyUtils.setListViewHeight(lv_category, 300);

        // 刚进入校园超市时显示第一条红条
        showRedline(0);

        // 商品列表顶部显示商品分类名称
        if (goodsCategoryData.size() > 0) {
            tv_category_top.setText(goodsCategoryData.get(0).getCategoryName()
                    + "");
        }

        // 商品分类监听事件
        lv_category.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int mPosition, long id) {
                isLoading = false;
                // 红条显示位置
                showRedline(mPosition);
                positionCategory = mPosition;
                // 更新适配器
                categoryAdapter.notifyDataSetChanged();
                // 商品列表顶部显示商品分类名称
                tv_category_top.setText(goodsCategoryData.get(mPosition)
                        .getCategoryName() + "");

                switch (mPosition) {
                    case 0:
                        // 如果商品数据没加载就开启线程获取数据
                        if (goodsInfoData01 == null || goodsInfoData01.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData01 = GetData
                                            .getGoodsData01(MyConfig.URL_JSON_GOODS);
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            // 如果商品数据已经加载过了则显示
                            initListViewGoods01();
                        }
                        break;
                    case 1:
                        if (goodsInfoData02 == null || goodsInfoData02.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData02 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(1)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods02();
                        }
                        break;
                    case 2:
                        if (goodsInfoData03 == null || goodsInfoData03.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData03 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(2)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods03();
                        }
                        break;
                    case 3:
                        if (goodsInfoData04 == null || goodsInfoData04.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData04 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(3)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods04();
                        }
                        break;
                    case 4:
                        if (goodsInfoData05 == null || goodsInfoData05.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData05 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(4)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods05();
                        }
                        break;
                    case 5:
                        if (goodsInfoData06 == null || goodsInfoData06.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData06 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(5)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods06();
                        }
                        break;
                    case 6:
                        if (goodsInfoData07 == null || goodsInfoData07.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData07 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(6)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods07();
                        }
                        break;
                    case 7:
                        if (goodsInfoData08 == null || goodsInfoData08.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData08 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(7)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods08();
                        }
                        break;
                    case 8:
                        if (goodsInfoData09 == null || goodsInfoData09.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData09 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(8)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods09();
                        }
                        break;
                    case 9:
                        if (goodsInfoData10 == null || goodsInfoData10.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData10 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(9)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods10();
                        }
                        break;
                    case 10:
                        if (goodsInfoData11 == null || goodsInfoData11.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData11 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(10)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods11();
                        }
                        break;
                    case 11:
                        if (goodsInfoData12 == null || goodsInfoData12.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData12 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(11)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods12();
                        }
                        break;
                    case 12:
                        if (goodsInfoData13 == null || goodsInfoData13.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData13 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(12)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods13();
                        }
                        break;
                    case 13:
                        if (goodsInfoData14 == null || goodsInfoData14.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData14 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(13)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods14();
                        }
                        break;
                    case 14:
                        if (goodsInfoData15 == null || goodsInfoData15.size() == 0) {
                            progress_lv.setVisibility(View.VISIBLE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    goodsInfoData15 = GetData
                                            .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                    + goodsCategoryData.get(14)
                                                    .getCatID());
                                    if (!isStop) {
                                        goodsHandler.sendEmptyMessage(0);
                                    }
                                }
                            }).start();
                        } else {
                            initListViewGoods15();
                        }
                        break;

                }
            }
        });

    }

    /**
     * 单击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 商家信息按钮
             */
            case R.id.rl_business:
                ll_activity_business.setVisibility(View.VISIBLE);
                HomeActivity.iv_below.setVisibility(View.GONE);
                HomeActivity.root_below.setVisibility(View.GONE);
                break;

            /**
             * 重新加载
             */
            case R.id.btn_again:
                updateShopData();
                break;

            /**
             * 商家信息关闭按钮
             */
            case R.id.ib_exit:
                ll_activity_business.setVisibility(View.GONE);
                HomeActivity.iv_below.setVisibility(View.VISIBLE);
                HomeActivity.root_below.setVisibility(View.VISIBLE);
                break;

            /**
             * 商品详情关闭按钮
             */
            case R.id.ib_exit_goods:
                rl_activity_goods.setVisibility(View.GONE);
                HomeActivity.iv_below.setVisibility(View.VISIBLE);
                HomeActivity.root_below.setVisibility(View.VISIBLE);
                break;

            /**
             * 商品详情暗部分
             */
            case R.id.rl_goods_page:
                rl_activity_goods.setVisibility(View.GONE);
                HomeActivity.iv_below.setVisibility(View.VISIBLE);
                HomeActivity.root_below.setVisibility(View.VISIBLE);
                break;

            /**
             * 顶部学校名称按钮
             */
            case R.id.btn_campusName:
                isStop = true;
                intent.setClass(activity, SearchActivity.class);
                activity.startActivityForResult(intent, MyConfig.SCHOOL_CODE_STORE);
                break;

            /**
             * 结算按钮
             */
            case R.id.btn_select_ok:
                intent.setClass(activity, PayStoreActivity.class);
                startActivity(intent);
                break;

            /**
             * 清空购物车
             */
            case R.id.ll_clear_cart:
                clearCart();
                break;

            /**
             * 购物车布局按钮
             */
            case R.id.ll_cart:
                if (rl_cart_page.getVisibility() == View.GONE) {
                    if (cartList.size() > 0) {
                        lv_cart.setAdapter(new CartAdapter(activity, cartList));
                        rl_cart_page.setVisibility(View.VISIBLE);
                    }
                } else {
                    rl_cart_page.setVisibility(View.GONE);
                }
                break;

            /**
             * 购物车布局暗部分
             */
            case R.id.rl_cart_page:
                rl_cart_page.setVisibility(View.GONE);
                break;

            /**
             * 购物车圆阴影
             */
            case R.id.rl_cart_root:
                // 显示购物车
                showCartView(1);
                rl_cart_root_pay.setClickable(true);
                break;

            /**
             * 购物车详细信息
             */
            case R.id.rl_cart_root_pay:
                // 当购物车列表数量为0时隐藏自己，否则显示购物车列表详情页
                if (cartList.size() == 0) {
                    // 隐藏购物车
                    showCartView(-1);
                    rl_cart_root_pay.setClickable(false);
                } else {
                    if (rl_cart_page.getVisibility() == View.GONE) {
                        if (cartList.size() > 0) {
                            lv_cart.setAdapter(new CartAdapter(activity, cartList));
                            rl_cart_page.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rl_cart_page.setVisibility(View.GONE);
                    }
                }
                break;

            /**
             * 商品详情单独添加按钮
             */
            case R.id.ib_add_red:
                switch (positionCategory) {
                    case 0:
                        if (goodsInfoData01.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter01.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter01.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter01.updateCartList(position, true);
                            GoodsListAdapter01.list.get(position).setAdding(true);
                            goodsAdapter01.notifyDataSetChanged();
                        }
                        break;

                    case 1:
                        if (goodsInfoData02.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter02.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter02.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter02.updateCartList(position, true);
                            GoodsListAdapter02.list.get(position).setAdding(true);
                            goodsAdapter02.notifyDataSetChanged();
                        }
                        break;

                    case 2:
                        if (goodsInfoData03.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter03.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter03.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter03.updateCartList(position, true);
                            GoodsListAdapter03.list.get(position).setAdding(true);
                            goodsAdapter03.notifyDataSetChanged();
                        }
                        break;
                    case 3:
                        if (goodsInfoData04.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter04.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter04.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter04.updateCartList(position, true);
                            GoodsListAdapter04.list.get(position).setAdding(true);
                            goodsAdapter04.notifyDataSetChanged();
                        }
                        break;
                    case 4:
                        if (goodsInfoData05.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter05.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter05.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter05.updateCartList(position, true);
                            GoodsListAdapter05.list.get(position).setAdding(true);
                            goodsAdapter05.notifyDataSetChanged();
                        }
                        break;
                    case 5:
                        if (goodsInfoData06.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter06.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter06.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter06.updateCartList(position, true);
                            GoodsListAdapter06.list.get(position).setAdding(true);
                            goodsAdapter06.notifyDataSetChanged();
                        }
                        break;
                    case 6:
                        if (goodsInfoData07.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter07.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter07.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter07.updateCartList(position, true);
                            GoodsListAdapter07.list.get(position).setAdding(true);
                            goodsAdapter07.notifyDataSetChanged();
                        }
                        break;
                    case 7:
                        if (goodsInfoData08.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter08.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter08.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter08.updateCartList(position, true);
                            GoodsListAdapter08.list.get(position).setAdding(true);
                            goodsAdapter08.notifyDataSetChanged();
                        }
                        break;
                    case 8:
                        if (goodsInfoData09.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter09.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter09.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter09.updateCartList(position, true);
                            GoodsListAdapter09.list.get(position).setAdding(true);
                            goodsAdapter09.notifyDataSetChanged();
                        }
                        break;
                    case 9:
                        if (goodsInfoData10.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter10.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter10.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter10.updateCartList(position, true);
                            GoodsListAdapter10.list.get(position).setAdding(true);
                            goodsAdapter10.notifyDataSetChanged();
                        }
                        break;
                    case 10:
                        if (goodsInfoData11.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter11.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter11.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter11.updateCartList(position, true);
                            GoodsListAdapter11.list.get(position).setAdding(true);
                            goodsAdapter11.notifyDataSetChanged();
                        }
                        break;
                    case 11:
                        if (goodsInfoData12.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter12.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter12.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter12.updateCartList(position, true);
                            GoodsListAdapter12.list.get(position).setAdding(true);
                            goodsAdapter12.notifyDataSetChanged();
                        }
                        break;
                    case 12:
                        if (goodsInfoData13.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter13.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter13.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter13.updateCartList(position, true);
                            GoodsListAdapter13.list.get(position).setAdding(true);
                            goodsAdapter13.notifyDataSetChanged();
                        }
                        break;
                    case 13:
                        if (goodsInfoData14.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter14.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter14.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter14.updateCartList(position, true);
                            GoodsListAdapter14.list.get(position).setAdding(true);
                            goodsAdapter14.notifyDataSetChanged();
                        }
                        break;
                    case 14:
                        if (goodsInfoData15.get(position).getStock() > 0) {
                            ll_addOrReduce.setVisibility(View.VISIBLE);
                            ib_add_red.setVisibility(View.GONE);
                            GoodsListAdapter15.list.get(position).setCount(1);
                            tv_goodsCount.setText("1");
                            updateView(GoodsListAdapter15.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter15.updateCartList(position, true);
                            GoodsListAdapter15.list.get(position).setAdding(true);
                            goodsAdapter15.notifyDataSetChanged();
                        }
                        break;
                }
                break;
            /**
             * 商品详情添加按钮
             */
            case R.id.ib_add_ll:
                switch (positionCategory) {
                    case 0:
                        count = GoodsListAdapter01.list.get(position).getCount();
                        if (goodsInfoData01.get(position).getStock() > count) {
                            count = count + 1;
                            GoodsListAdapter01.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter01.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter01.updateCartList(position, true);
                            goodsAdapter01.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 1:
                        count = GoodsListAdapter02.list.get(position).getCount();
                        if (goodsInfoData02.get(position).getStock() > count) {
                            count = GoodsListAdapter02.list.get(position).getCount() + 1;
                            GoodsListAdapter02.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter02.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter02.updateCartList(position, true);
                            goodsAdapter02.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 2:
                        count = GoodsListAdapter03.list.get(position).getCount();
                        if (goodsInfoData03.get(position).getStock() > count) {
                            count = GoodsListAdapter03.list.get(position).getCount() + 1;
                            GoodsListAdapter03.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter03.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter03.updateCartList(position, true);
                            goodsAdapter03.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 3:
                        count = GoodsListAdapter04.list.get(position).getCount();
                        if (goodsInfoData04.get(position).getStock() > count) {
                            count = GoodsListAdapter04.list.get(position).getCount() + 1;
                            GoodsListAdapter04.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter04.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter04.updateCartList(position, true);
                            goodsAdapter04.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 4:
                        count = GoodsListAdapter05.list.get(position).getCount();
                        if (goodsInfoData05.get(position).getStock() > count) {
                            count = GoodsListAdapter05.list.get(position).getCount() + 1;
                            GoodsListAdapter05.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter05.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter05.updateCartList(position, true);
                            goodsAdapter05.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 5:
                        count = GoodsListAdapter06.list.get(position).getCount();
                        if (goodsInfoData06.get(position).getStock() > count) {
                            count = GoodsListAdapter06.list.get(position).getCount() + 1;
                            GoodsListAdapter06.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter06.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter06.updateCartList(position, true);
                            goodsAdapter06.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 6:
                        count = GoodsListAdapter07.list.get(position).getCount();
                        if (goodsInfoData07.get(position).getStock() > count) {
                            count = GoodsListAdapter07.list.get(position).getCount() + 1;
                            GoodsListAdapter07.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter07.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter07.updateCartList(position, true);
                            goodsAdapter07.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 7:
                        count = GoodsListAdapter08.list.get(position).getCount();
                        if (goodsInfoData08.get(position).getStock() > count) {
                            count = GoodsListAdapter08.list.get(position).getCount() + 1;
                            GoodsListAdapter08.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter08.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter08.updateCartList(position, true);
                            goodsAdapter08.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 8:
                        count = GoodsListAdapter09.list.get(position).getCount();
                        if (goodsInfoData09.get(position).getStock() > count) {
                            count = GoodsListAdapter09.list.get(position).getCount() + 1;
                            GoodsListAdapter09.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter09.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter09.updateCartList(position, true);
                            goodsAdapter09.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 9:
                        count = GoodsListAdapter10.list.get(position).getCount();
                        if (goodsInfoData10.get(position).getStock() > count) {
                            count = GoodsListAdapter10.list.get(position).getCount() + 1;
                            GoodsListAdapter10.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter10.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter10.updateCartList(position, true);
                            goodsAdapter10.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 10:
                        count = GoodsListAdapter11.list.get(position).getCount();
                        if (goodsInfoData11.get(position).getStock() > count) {
                            count = GoodsListAdapter11.list.get(position).getCount() + 1;
                            GoodsListAdapter11.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter11.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter11.updateCartList(position, true);
                            goodsAdapter11.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 11:
                        count = GoodsListAdapter12.list.get(position).getCount();
                        if (goodsInfoData12.get(position).getStock() > count) {
                            count = GoodsListAdapter12.list.get(position).getCount() + 1;
                            GoodsListAdapter12.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter12.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter12.updateCartList(position, true);
                            goodsAdapter12.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 12:
                        count = GoodsListAdapter13.list.get(position).getCount();
                        if (goodsInfoData13.get(position).getStock() > count) {
                            count = GoodsListAdapter13.list.get(position).getCount() + 1;
                            GoodsListAdapter13.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter13.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter13.updateCartList(position, true);
                            goodsAdapter13.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 13:
                        count = GoodsListAdapter14.list.get(position).getCount();
                        if (goodsInfoData14.get(position).getStock() > count) {
                            count = GoodsListAdapter14.list.get(position).getCount() + 1;
                            GoodsListAdapter14.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter14.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter14.updateCartList(position, true);
                            goodsAdapter14.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                    case 14:
                        count = GoodsListAdapter15.list.get(position).getCount();
                        if (goodsInfoData15.get(position).getStock() > count) {
                            count = GoodsListAdapter15.list.get(position).getCount() + 1;
                            GoodsListAdapter15.list.get(position).setCount(count);
                            tv_goodsCount.setText(count + "");
                            updateView(GoodsListAdapter15.list.get(position)
                                    .getCurrentPrice());
                            GoodsListAdapter15.updateCartList(position, true);
                            goodsAdapter15.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(activity,"已达最大库存,不能继续添加");
                        }
                        break;
                }
                break;
            /**
             * 商品详情减少按钮
             */
            case R.id.ib_reduce_ll:
                switch (positionCategory) {
                    case 0:
                        count = GoodsListAdapter01.list.get(position).getCount() - 1;
                        GoodsListAdapter01.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter01.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter01.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter01.list.get(position).setAdding(false);
                        }
                        goodsAdapter01.notifyDataSetChanged();
                        break;
                    case 1:
                        count = GoodsListAdapter02.list.get(position).getCount() - 1;
                        GoodsListAdapter02.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter02.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter02.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter02.list.get(position).setAdding(false);
                        }
                        goodsAdapter02.notifyDataSetChanged();
                        break;
                    case 2:
                        count = GoodsListAdapter03.list.get(position).getCount() - 1;
                        GoodsListAdapter03.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter03.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter03.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter03.list.get(position).setAdding(false);
                        }
                        goodsAdapter03.notifyDataSetChanged();
                        break;
                    case 3:
                        count = GoodsListAdapter04.list.get(position).getCount() - 1;
                        GoodsListAdapter04.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter04.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter04.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter04.list.get(position).setAdding(false);
                        }
                        goodsAdapter04.notifyDataSetChanged();
                        break;
                    case 4:
                        count = GoodsListAdapter05.list.get(position).getCount() - 1;
                        GoodsListAdapter05.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter05.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter05.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter05.list.get(position).setAdding(false);
                        }
                        goodsAdapter05.notifyDataSetChanged();
                        break;
                    case 5:
                        count = GoodsListAdapter06.list.get(position).getCount() - 1;
                        GoodsListAdapter06.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter06.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter06.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter06.list.get(position).setAdding(false);
                        }
                        goodsAdapter06.notifyDataSetChanged();
                        break;
                    case 6:
                        count = GoodsListAdapter07.list.get(position).getCount() - 1;
                        GoodsListAdapter07.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter07.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter07.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter07.list.get(position).setAdding(false);
                        }
                        goodsAdapter07.notifyDataSetChanged();
                        break;
                    case 7:
                        count = GoodsListAdapter08.list.get(position).getCount() - 1;
                        GoodsListAdapter08.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter08.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter08.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter08.list.get(position).setAdding(false);
                        }
                        goodsAdapter08.notifyDataSetChanged();
                        break;
                    case 8:
                        count = GoodsListAdapter09.list.get(position).getCount() - 1;
                        GoodsListAdapter09.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter09.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter09.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter09.list.get(position).setAdding(false);
                        }
                        goodsAdapter09.notifyDataSetChanged();
                        break;
                    case 9:
                        count = GoodsListAdapter10.list.get(position).getCount() - 1;
                        GoodsListAdapter10.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter10.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter10.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter10.list.get(position).setAdding(false);
                        }
                        goodsAdapter10.notifyDataSetChanged();
                        break;
                    case 10:
                        count = GoodsListAdapter11.list.get(position).getCount() - 1;
                        GoodsListAdapter11.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter11.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter11.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter11.list.get(position).setAdding(false);
                        }
                        goodsAdapter11.notifyDataSetChanged();
                        break;
                    case 11:
                        count = GoodsListAdapter12.list.get(position).getCount() - 1;
                        GoodsListAdapter12.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter12.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter12.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter12.list.get(position).setAdding(false);
                        }
                        goodsAdapter12.notifyDataSetChanged();
                        break;
                    case 12:
                        count = GoodsListAdapter13.list.get(position).getCount() - 1;
                        GoodsListAdapter13.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter13.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter13.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter13.list.get(position).setAdding(false);
                        }
                        goodsAdapter13.notifyDataSetChanged();
                        break;
                    case 13:
                        count = GoodsListAdapter14.list.get(position).getCount() - 1;
                        GoodsListAdapter14.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter14.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter14.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter14.list.get(position).setAdding(false);
                        }
                        goodsAdapter14.notifyDataSetChanged();
                        break;
                    case 14:
                        count = GoodsListAdapter15.list.get(position).getCount() - 1;
                        GoodsListAdapter15.list.get(position).setCount(count);
                        tv_goodsCount.setText(count + "");
                        updateView(-GoodsListAdapter15.list.get(position)
                                .getCurrentPrice());
                        GoodsListAdapter15.updateCartList(position, false);
                        if (count == 0) {
                            ll_addOrReduce.setVisibility(View.GONE);
                            ib_add_red.setVisibility(View.VISIBLE);
                            GoodsListAdapter15.list.get(position).setAdding(false);
                        }
                        goodsAdapter15.notifyDataSetChanged();
                        break;
                }
                break;
        }
    }

    /**
     * 清空购物车
     */
    public void clearCart() {
        if (cartList != null) {
            cartList.clear();
        }
        CartInfo.resetData();
        updateView(0);

        rl_cart_page.setVisibility(View.GONE);
        if (goodsInfoData01 != null && goodsInfoData01.size() > 0) {
            for (int i = 0; i < goodsInfoData01.size(); i++) {
                goodsInfoData01.get(i).setCount(0);
                goodsInfoData01.get(i).setAdding(false);
            }
            if (goodsAdapter01 != null) {
                goodsAdapter01.notifyDataSetChanged();
            }
        }
        if (goodsInfoData02 != null && goodsInfoData02.size() > 0) {
            for (int i = 0; i < goodsInfoData02.size(); i++) {
                goodsInfoData02.get(i).setCount(0);
                goodsInfoData02.get(i).setAdding(false);
            }
            if (goodsAdapter02 != null) {
                goodsAdapter02.notifyDataSetChanged();
            }
        }
        if (goodsInfoData03 != null && goodsInfoData03.size() > 0) {
            for (int i = 0; i < goodsInfoData03.size(); i++) {
                goodsInfoData03.get(i).setCount(0);
                goodsInfoData03.get(i).setAdding(false);
            }
            if (goodsAdapter03 != null) {
                goodsAdapter03.notifyDataSetChanged();
            }
        }
        if (goodsInfoData04 != null && goodsInfoData04.size() > 0) {
            for (int i = 0; i < goodsInfoData04.size(); i++) {
                goodsInfoData04.get(i).setCount(0);
                goodsInfoData04.get(i).setAdding(false);
            }
            if (goodsAdapter04 != null) {
                goodsAdapter04.notifyDataSetChanged();
            }
        }
        if (goodsInfoData05 != null && goodsInfoData05.size() > 0) {
            for (int i = 0; i < goodsInfoData05.size(); i++) {
                goodsInfoData05.get(i).setCount(0);
                goodsInfoData05.get(i).setAdding(false);
            }
            if (goodsAdapter05 != null) {
                goodsAdapter05.notifyDataSetChanged();
            }
        }
        if (goodsInfoData06 != null && goodsInfoData06.size() > 0) {
            for (int i = 0; i < goodsInfoData06.size(); i++) {
                goodsInfoData06.get(i).setCount(0);
                goodsInfoData06.get(i).setAdding(false);
            }
            if (goodsAdapter06 != null) {
                goodsAdapter06.notifyDataSetChanged();
            }
        }
        if (goodsInfoData07 != null && goodsInfoData07.size() > 0) {
            for (int i = 0; i < goodsInfoData07.size(); i++) {
                goodsInfoData07.get(i).setCount(0);
                goodsInfoData07.get(i).setAdding(false);
            }
            if (goodsAdapter07 != null) {
                goodsAdapter07.notifyDataSetChanged();
            }
        }
        if (goodsInfoData08 != null && goodsInfoData08.size() > 0) {
            for (int i = 0; i < goodsInfoData08.size(); i++) {
                goodsInfoData08.get(i).setCount(0);
                goodsInfoData08.get(i).setAdding(false);
            }
            if (goodsAdapter08 != null) {
                goodsAdapter08.notifyDataSetChanged();
            }
        }
        if (goodsInfoData09 != null && goodsInfoData09.size() > 0) {
            for (int i = 0; i < goodsInfoData09.size(); i++) {
                goodsInfoData09.get(i).setCount(0);
                goodsInfoData09.get(i).setAdding(false);
            }
            if (goodsAdapter09 != null) {
                goodsAdapter09.notifyDataSetChanged();
            }
        }
        if (goodsInfoData10 != null && goodsInfoData10.size() > 0) {
            for (int i = 0; i < goodsInfoData10.size(); i++) {
                goodsInfoData10.get(i).setCount(0);
                goodsInfoData10.get(i).setAdding(false);
            }
            if (goodsAdapter10 != null) {
                goodsAdapter10.notifyDataSetChanged();
            }
        }
        if (goodsInfoData11 != null && goodsInfoData11.size() > 0) {
            for (int i = 0; i < goodsInfoData11.size(); i++) {
                goodsInfoData11.get(i).setCount(0);
                goodsInfoData11.get(i).setAdding(false);
            }
            if (goodsAdapter11 != null) {
                goodsAdapter11.notifyDataSetChanged();
            }
        }
        if (goodsInfoData12 != null && goodsInfoData12.size() > 0) {
            for (int i = 0; i < goodsInfoData12.size(); i++) {
                goodsInfoData12.get(i).setCount(0);
                goodsInfoData12.get(i).setAdding(false);
            }
            if (goodsAdapter12 != null) {
                goodsAdapter12.notifyDataSetChanged();
            }
        }
        if (goodsInfoData13 != null && goodsInfoData13.size() > 0) {
            for (int i = 0; i < goodsInfoData13.size(); i++) {
                goodsInfoData13.get(i).setCount(0);
                goodsInfoData13.get(i).setAdding(false);
            }
            if (goodsAdapter13 != null) {
                goodsAdapter13.notifyDataSetChanged();
            }
        }
        if (goodsInfoData14 != null && goodsInfoData14.size() > 0) {
            for (int i = 0; i < goodsInfoData14.size(); i++) {
                goodsInfoData14.get(i).setCount(0);
                goodsInfoData14.get(i).setAdding(false);
            }
            if (goodsAdapter14 != null) {
                goodsAdapter14.notifyDataSetChanged();
            }
        }
        if (goodsInfoData15 != null && goodsInfoData15.size() > 0) {
            for (int i = 0; i < goodsInfoData15.size(); i++) {
                goodsInfoData15.get(i).setCount(0);
                goodsInfoData15.get(i).setAdding(false);
            }
            if (goodsAdapter15 != null) {
                goodsAdapter15.notifyDataSetChanged();
            }
        }

        // 隐藏购物车
        showCartView(0);
    }

    /**
     * 显示商品分类的红条
     */
    public void showRedline(int position) {
        for (int i = 0; i < goodsCategoryData.size(); i++) {
            if (i == position) {
                goodsCategoryData.get(i).setShowRedline(true);
            } else {
                goodsCategoryData.get(i).setShowRedline(false);
            }
        }
    }

    /**
     * 设置轮播小圆点的初始化颜色
     */
    // public static void resetCircleImageColor() {
    // iv_circle01.setImageResource(R.drawable.circle_point);
    // iv_circle02.setImageResource(R.drawable.circle_point);
    // iv_circle03.setImageResource(R.drawable.circle_point);
    // }

    /**
     * 设置是否显示购物车
     *
     * @param flag
     */
    public static void showCartView(int flag) {
        if (flag == 1) {
            rl_cart_root.setVisibility(View.GONE);
            rl_cart_root_pay.setVisibility(View.VISIBLE);
            rl_cart_root_pay
                    .startAnimation(MyAnimation.getTranslateAnimation());
        } else if (flag == -1) {
            rl_cart_root_pay.startAnimation(MyAnimation
                    .getTranslateAnimation2());
            rl_cart_root_pay.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rl_cart_root.setVisibility(View.VISIBLE);
                    rl_cart_root.startAnimation(MyAnimation
                            .getAlphaAnimation(500));
                }
            }, 500);
        } else {
            rl_cart_root.setVisibility(View.VISIBLE);
            rl_cart_root_pay.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏所有商品列表
     */
    private void hideGoods() {
        progress_lv.setVisibility(View.GONE);
        lv_goods01.setVisibility(View.GONE);
        lv_goods02.setVisibility(View.GONE);
        lv_goods03.setVisibility(View.GONE);
        lv_goods04.setVisibility(View.GONE);
        lv_goods05.setVisibility(View.GONE);
        lv_goods06.setVisibility(View.GONE);
        lv_goods07.setVisibility(View.GONE);
        lv_goods08.setVisibility(View.GONE);
        lv_goods09.setVisibility(View.GONE);
        lv_goods10.setVisibility(View.GONE);
        lv_goods11.setVisibility(View.GONE);
        lv_goods12.setVisibility(View.GONE);
        lv_goods13.setVisibility(View.GONE);
        lv_goods14.setVisibility(View.GONE);
        lv_goods15.setVisibility(View.GONE);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        switch (view.getId()) {
            case R.id.lv_goods01:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            // goodsCategoryData.get(0).getCatID()
                            if (goodsCategoryData != null) {
                                goodsInfos = GetData
                                        .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                                + goodsCategoryData.get(0)
                                                .getCatID() + "&page="
                                                + page01);
                            }
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData01.add(goodsInfos.get(i));
                                }
                                goodsInfos.clear();
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;
            case R.id.lv_goods02:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(1).getCatID()
                                            + "&page=" + page02);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData02.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;
            case R.id.lv_goods03:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(2).getCatID()
                                            + "&page=" + page03);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData03.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods04:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(3).getCatID()
                                            + "&page=" + page04);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData04.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods05:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(4).getCatID()
                                            + "&page=" + page05);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData05.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }

                break;

            case R.id.lv_goods06:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(5).getCatID()
                                            + "&page=" + page06);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData06.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods07:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(6).getCatID()
                                            + "&page=" + page07);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData07.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods08:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(7).getCatID()
                                            + "&page=" + page08);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData08.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods09:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(8).getCatID()
                                            + "&page=" + page09);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData09.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }

                break;

            case R.id.lv_goods10:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(9).getCatID()
                                            + "&page=" + page10);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData10.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods11:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(10).getCatID()
                                            + "&page=" + page11);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData11.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.lv_goods12:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(11).getCatID()
                                            + "&page=" + page12);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData12.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }

                break;

            case R.id.lv_goods13:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(12).getCatID()
                                            + "&page=" + page13);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData13.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }

                break;

            case R.id.lv_goods14:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(13).getCatID()
                                            + "&page=" + page14);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData14.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }

                break;

            case R.id.lv_goods15:
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            if (goodsCategoryData == null) {
                                // isLoading = false;
                                return;
                            }
                            goodsInfos = GetData
                                    .getGoodsInfoData(MyConfig.URL_JSON_GOODS
                                            + goodsCategoryData.get(14).getCatID()
                                            + "&page=" + page15);
                            // isLoading = false;
                            if (goodsInfos != null && goodsInfos.size() > 0) {
                                for (int i = 0; i < goodsInfos.size(); i++) {
                                    goodsInfoData15.add(goodsInfos.get(i));
                                }
                                if (!isStop) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
                break;

        }
    }

    /**
     * 更新购物车总价
     *
     * @param price
     */
    public static void updateView(float price) {
        if (btn_select_ok == null) {
            return;
        }
        CartInfo.goodsPricetotal = CartInfo.goodsPricetotal + price;
        if (price > 0) {
            CartInfo.goodsCounttotal = CartInfo.goodsCounttotal + 1;
        } else if (price < 0) {
            CartInfo.goodsCounttotal = CartInfo.goodsCounttotal - 1;
        }
        // 更新购物车总价
        tv_totalMoney.setText("￥" + Math.round(CartInfo.goodsPricetotal * 100)
                / 100.0);
        // 更新购物车数量
        tv_count_cart.setText(CartInfo.goodsCounttotal + "");
        tv_count_cart.setVisibility(View.VISIBLE);
        if (!isWork) {
            if (CartInfo.goodsCounttotal == 0) {
                tv_count_cart.setVisibility(View.GONE);
            } else {
                tv_count_cart.setVisibility(View.VISIBLE);
            }
            btn_select_ok.setClickable(false);
            btn_select_ok.setBackgroundColor(activity.getResources().getColor(
                    R.color.gray_3e3e3e));
            btn_select_ok.setText("休息中");
        } else {
            // 如果购物车总价为0时
            if (CartInfo.goodsCounttotal == 0) {
                btn_select_ok.setClickable(false);
                btn_select_ok.setBackgroundColor(activity.getResources()
                        .getColor(R.color.none_color));
                btn_select_ok.setText(BusinessInfo.sendAtLeastMoney + "元起送");
                tv_count_cart.setVisibility(View.GONE);
            }
            // 购物车总价大于等于最低配送价格时
            else if (CartInfo.goodsPricetotal >= BusinessInfo.sendAtLeastMoney) {
                btn_select_ok.setClickable(true);
                btn_select_ok.setBackgroundColor(activity.getResources()
                        .getColor(R.color.red_fd3c49));
                btn_select_ok.setText("结算");
            }
            // 购物车总价小于最低配送价格时
            else {
                btn_select_ok.setClickable(false);
                btn_select_ok.setBackgroundColor(activity.getResources()
                        .getColor(R.color.none_color));
                btn_select_ok
                        .setText("还差"
                                + (Math.round((BusinessInfo.sendAtLeastMoney - CartInfo.goodsPricetotal) * 100) / 100.0)
                                + "元");
            }
        }

    }

//    @Override
//    public void onResume() {
//        if (isChange) {
//            updateShopData();
//        }
//        super.onResume();
//    }
//
//    /**
//     * fragment切换时调用该方法
//     *
//     * @param hidden
//     */
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        if (!hidden) {
//            if (isChange) {
//                updateShopData();
//            }
//        }
//    }

    /**
     * 判断学校是否改变，是的话就改变商铺信息
     */
    private void updateShopData() {
        positionCategory = 0;
        rl_business.setClickable(false);
        btn_campusName.setClickable(false);
        // 重新获取学校ID
        sid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID_STORE, "");
        school = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.SCHOOL_STORE, "");
        btn_campusName.setText(school);
        isStop = false;
        rl_pb.setVisibility(View.VISIBLE);
        clearCart();
//        isChange = false;
        page01 = 2;
        page02 = 2;
        page03 = 2;
        page04 = 2;
        page05 = 2;
        page06 = 2;
        page07 = 2;
        page08 = 2;
        page09 = 2;
        page10 = 2;
        page11 = 2;
        page12 = 2;
        page13 = 2;
        page14 = 2;
        page15 = 2;// 购物车信息清空
        CartInfo.resetData();
        if (goodsCategoryData != null) {
            goodsCategoryData.clear();
            goodsCategoryData = null;
        }
        goodsInfoData01 = null;
        goodsInfoData02 = null;
        goodsInfoData03 = null;
        goodsInfoData04 = null;
        goodsInfoData05 = null;
        goodsInfoData06 = null;
        goodsInfoData07 = null;
        goodsInfoData08 = null;
        goodsInfoData09 = null;
        goodsInfoData10 = null;
        goodsInfoData11 = null;
        goodsInfoData12 = null;
        goodsInfoData13 = null;
        goodsInfoData14 = null;
        goodsInfoData15 = null;
        goodsAdapter01 = null;
        goodsAdapter02 = null;
        goodsAdapter03 = null;
        goodsAdapter04 = null;
        goodsAdapter05 = null;
        goodsAdapter06 = null;
        goodsAdapter07 = null;
        goodsAdapter08 = null;
        goodsAdapter09 = null;
        goodsAdapter10 = null;
        goodsAdapter11 = null;
        goodsAdapter12 = null;
        goodsAdapter13 = null;
        goodsAdapter14 = null;
        goodsAdapter15 = null;
        // 开启子线程获取数据，并通知主线程更新UI
        new Thread() {

            @Override
            public void run() {

                // 获取分类名
                goodsCategoryData = GetData
                        .getGoodsCategoryData(MyConfig.URL_JSON_CAT + sid);
                // 获取店铺信息
                GetData.setBusinessInfo(MyConfig.URL_JSON_CAT + sid);
                isWork = GetData.getIsBusinessWork(MyConfig.URL_JSON_CAT + sid);
                // 获取商品信息
                goodsInfoData01 = GetData.getGoodsData01(MyConfig.URL_JSON_CAT
                        + sid);
                if (!isStop) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    // }

    private void showGoodsDetail(List<GoodsInfo> list) {
        if (list.get(position).getCount() > 0) {
            ll_addOrReduce.setVisibility(View.VISIBLE);
            ib_add_red.setVisibility(View.GONE);
            tv_goodsCount.setText(list.get(position).getCount() + "");
        } else {
            ll_addOrReduce.setVisibility(View.GONE);
            ib_add_red.setVisibility(View.VISIBLE);
        }
        List<View> listview = new ArrayList<View>();
        if (!list.get(position).getGoodsDetailUrl01().equals("")) {
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            Glide.with(activity).load(list.get(position).getGoodsDetailUrl01())
                    .override(800, 800).placeholder(R.drawable.imgloading)
                    .into(imageView);
            listview.add(imageView);
            i = 1;
        }
        if (!list.get(position).getGoodsDetailUrl02().equals("")) {
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            Glide.with(activity).load(list.get(position).getGoodsDetailUrl02())
                    .override(800, 800).placeholder(R.drawable.imgloading)
                    .into(imageView);
            listview.add(imageView);
            i = 2;
        }
        if (!list.get(position).getGoodsDetailUrl03().equals("")) {
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            Glide.with(activity).load(list.get(position).getGoodsDetailUrl03())
                    .override(800, 800).placeholder(R.drawable.imgloading)
                    .into(imageView);
            listview.add(imageView);
            i = 3;
        }
        vp_goods.setAdapter(new ViewPagerAdapter(listview));
        // i = 0;
        tv_index.setText("1/" + i);
        tv_goodsName.setText(list.get(position).getGoodsName() + "");
        tv_goodSold.setText(list.get(position).getSold() + "");
        tv_goodsStock.setText(list.get(position).getStock() + "");
        tv_goodsStock.setText(list.get(position).getStock() + "");
        tv_goodsPrice.setText("￥" + list.get(position).getCurrentPrice());
        rl_activity_goods.setVisibility(View.VISIBLE);

        HomeActivity.iv_below.setVisibility(View.GONE);
        HomeActivity.root_below.setVisibility(View.GONE);
    }

    public void onEventMainThread(MEventStoreCart event) {
        if (event.getChange()) {
            updateShopData();
        }
    }

    @Override
    public void onDestroy() {
        if (cartList != null) {
            cartList.clear();
        }
        CartInfo.resetData();
        updateView(0);
        isStop = true;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
