package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.anyevent.MEventCollectGiftChange;
import com.loosoo100.campus100.anyevent.MEventGiftGoodsDetails;
import com.loosoo100.campus100.anyevent.MEventSecondPage;
import com.loosoo100.campus100.beans.GiftGoodsAttrVal;
import com.loosoo100.campus100.beans.GiftGoodsInfo;
import com.loosoo100.campus100.beans.GiftGoodsStockInfo;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.GoodsInfoInCart;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.XCFlowLayout;
import com.loosoo100.campus100.wxapi.WXShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static cn.jpush.android.api.e.j;

/**
 * @author yang 礼物说商品详情activity
 */
public class GiftGoodsDetailActivity extends Activity implements
        OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮
    @ViewInject(R.id.rl_message)
    private RelativeLayout rl_message; // 消息按钮
    @ViewInject(R.id.rl_topbar)
    private RelativeLayout rl_topbar; // 顶部颜色
    @ViewInject(R.id.ib_share)
    private ImageButton ib_share; // 分享按钮
    @ViewInject(R.id.ib_collect)
    private ImageButton ib_collect; // 收藏按钮
    @ViewInject(R.id.btn_select_ok)
    private Button btn_pay; // 确认订单按钮
    @ViewInject(R.id.ll_select)
    private LinearLayout ll_select; // 规格颜色选择
    @ViewInject(R.id.ib_close)
    private Button ib_close; // 关闭规格颜色界面按钮
    @ViewInject(R.id.root_standard)
    private RelativeLayout root_standard; // 规格颜色界面
    @ViewInject(R.id.rl_bg)
    private RelativeLayout rl_bg; // 规格颜色暗部分界面
    @ViewInject(R.id.vp_gift)
    private ViewPager vp_gift; // 商品轮播图
    @ViewInject(R.id.tv_goodsName)
    private TextView tv_goodsName; // 商品名
    @ViewInject(R.id.tv_goodsPrice)
    private TextView tv_goodsPrice; // 商品价格
    @ViewInject(R.id.tv_goodsOriginalPrice)
    private TextView tv_goodsOriginalPrice; // 商品原价
    @ViewInject(R.id.tv_expressFee)
    private TextView tv_expressFee; // 商品配送费
    @ViewInject(R.id.tv_sold)
    private TextView tv_sold; // 商品已售
    @ViewInject(R.id.tv_collect)
    private TextView tv_collect; // 商品收藏
    @ViewInject(R.id.tv_goodStock)
    private TextView tv_goodStock; // 商品库存
    @ViewInject(R.id.tv_select)
    private TextView tv_select; // 规格颜色文本

    @ViewInject(R.id.btn_IWant)
    private Button btn_IWant; // 我想要
    @ViewInject(R.id.btn_sentTA)
    private Button btn_sentTA; // 送给ta
    @ViewInject(R.id.iv_back_bg)
    private ImageView iv_back_bg; // 返回背景
    @ViewInject(R.id.iv_message_bg)
    private ImageView iv_message_bg; // 消息背景

    @ViewInject(R.id.tv_attr01)
    private TextView tv_attr01; // 规格属性名
    @ViewInject(R.id.tv_attr02)
    private TextView tv_attr02; // 规格属性名
    @ViewInject(R.id.flowlayout)
    private XCFlowLayout mFlowLayout;
    @ViewInject(R.id.flowlayout2)
    private XCFlowLayout mFlowLayout2;

    @ViewInject(R.id.ll_image)
    private LinearLayout ll_image; // 详情图片

    @ViewInject(R.id.iv_circle01)
    public ImageButton iv_circle01;
    @ViewInject(R.id.iv_circle02)
    private ImageButton iv_circle02;
    @ViewInject(R.id.iv_circle03)
    private ImageButton iv_circle03;

    @ViewInject(R.id.progress)
    private RelativeLayout progress;

    public List<GoodsInfo> list = new ArrayList<GoodsInfo>();
    private List<View> views = new ArrayList<View>();
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    /**
     * 规格颜色选择
     */
    // @ViewInject(R.id.gv_standard)
    // private GridView gv_standard; // 规格选择gridview
    // @ViewInject(R.id.gv_color)
    // private GridView gv_color; // 颜色选择gridview
    @ViewInject(R.id.tv_selected)
    private TextView tv_selected; // 选择的规格和颜色
    @ViewInject(R.id.iv_goodsSmallIcon)
    private ImageView iv_goodsSmallIcon; // 小图标
    @ViewInject(R.id.tv_goodsPriceStandard)
    private TextView tv_goodsPriceStandard; // 各规格价格
    @ViewInject(R.id.tv_stock_attr)
    private TextView tv_stock_attr; // 各规格库存
    @ViewInject(R.id.tv_goodsCount)
    private TextView tv_goodsCount; // 数量
    @ViewInject(R.id.ib_reduce)
    private ImageButton ib_reduce; // 减少
    @ViewInject(R.id.ib_add)
    private ImageButton ib_add; // 添加
    @ViewInject(R.id.btn_select_ok)
    private Button btn_select_ok; // 选好了
    // @ViewInject(R.id.pullUpToLoadMore)
    // private PullUpToLoadMore pullUpToLoadMore;
    // @ViewInject(R.id.scroll02)
    // private MyScrollView scroll02;

    // private StandardGridviewAdapter sAdapter;
    // private ColorGridviewAdapter cAdapter;

    private String standard = "";
    private String color = "";
    private String giftgoodsImg = "";
    private float price;

    private int standardSymbol;
    private int colorSymbol;
    private String symbol = "";

    private String giftgoodsId = "";
    private String goodsAttrId = "";
    private String skuId = "";

    private TextView button;

    private GiftGoodsInfo giftGoodsInfos = null;

    private List<GiftGoodsStockInfo> stockInfos = null;

    private List<GiftGoodsAttrVal> attrVals02 = null;
    private List<GiftGoodsAttrVal> attrVals01 = null;

    private boolean isChecked01 = false; // 规格1是否选择了
    private boolean isChecked02 = false; // 规格2是否选择了
    private MyHandler mHandler;

    private String uid = "";
    private String collectID = "";
    private ScaleAnimation animation; // 收藏按钮点击动画
    private boolean isFirstShow = true;

    private WXShareUtil shareUtil;
    private String downUrl = "http://www.campus100.cn/App/index.php/Home/ApisharePay/link";
    private String picUrl = "";

    static class MyHandler extends Handler {
        private WeakReference<GiftGoodsDetailActivity> mActivity;

        MyHandler(GiftGoodsDetailActivity activity) {
            mActivity = new WeakReference<GiftGoodsDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GiftGoodsDetailActivity activity = mActivity.get();
            activity.initView();
            activity.initViewPager();
            activity.progress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_goods_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        EventBus.getDefault().register(this);

        shareUtil = new WXShareUtil(this);
        mHandler = new MyHandler(this);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        giftgoodsId = getIntent().getExtras().getString("giftgoodsId");

        progress.setVisibility(View.VISIBLE);

        rl_back.setOnClickListener(this);
        rl_message.setOnClickListener(this);

        animation = MyAnimation.getScaleAnimationLocation();

        new Thread() {
            public void run() {
                giftGoodsInfos = GetData
                        .getGiftGoodsInfos(MyConfig.URL_JSON_GIFT_GOODS
                                + giftgoodsId + "&userId=" + uid);
                stockInfos = GetData
                        .getGiftGoodsStocks(MyConfig.URL_JSON_GIFT_GOODS
                                + giftgoodsId);
                if (giftGoodsInfos != null && !isDestroyed()) {
                    mHandler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        Glide.with(this).load(giftGoodsInfos.getGiftgoodsImg01())
                .placeholder(R.drawable.imgloading) // 设置占位图
                .into(imageView);
        views.add(imageView);

        ImageView imageView2 = new ImageView(this);
        imageView2.setScaleType(ScaleType.CENTER_CROP);
        Glide.with(this).load(giftGoodsInfos.getGiftgoodsImg02())
                .placeholder(R.drawable.imgloading) // 设置占位图
                .into(imageView2);
        views.add(imageView2);

        ImageView imageView3 = new ImageView(this);
        imageView3.setScaleType(ScaleType.CENTER_CROP);
        Glide.with(this).load(giftGoodsInfos.getGiftgoodsImg03())
                .placeholder(R.drawable.imgloading) // 设置占位图
                .into(imageView3);
        views.add(imageView3);

        vp_gift.setAdapter(new ViewPagerAdapter(views));
        vp_gift.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
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

    private void initView() {
        ib_share.setOnClickListener(this);
        ib_collect.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        ib_close.setOnClickListener(this);
        ll_select.setOnClickListener(this);
        rl_bg.setOnClickListener(this);
        btn_IWant.setOnClickListener(this);
        btn_sentTA.setOnClickListener(this);
        ib_reduce.setOnClickListener(this);
        ib_add.setOnClickListener(this);
        ib_add.setClickable(false);
        btn_select_ok.setOnClickListener(this);
        btn_select_ok.setClickable(false);
        iv_goodsSmallIcon.setOnClickListener(this);

        // 原价设置删除线
        tv_goodsOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        collectID = giftGoodsInfos.getCollectID();

        if (giftGoodsInfos.getCollectID().equals("0")) {
            ib_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect));
        } else {
            ib_collect.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.icon_collect_red));
        }

        tv_goodsName.setText(giftGoodsInfos.getGiftgoodsName() + "");
        tv_goodsPrice.setText("￥" + giftGoodsInfos.getShopPrice());
        tv_goodsOriginalPrice.setText("￥" + giftGoodsInfos.getMarketPrice());
        tv_expressFee.setText("配送费"+giftGoodsInfos.getDeliverMoney()+"元");
        tv_sold.setText("已售出" + giftGoodsInfos.getGiftgoodsSold() + "件");
        tv_collect.setText("收藏" + giftGoodsInfos.getCollect() + "人");
        tv_goodStock.setText("库存:" + giftGoodsInfos.getGiftgoodsStock());

        picUrl=giftGoodsInfos.getGiftgoodsImg01();
        if (!isDestroyed()) {
            // 设置默认小图
            Glide.with(this).load(giftGoodsInfos.getGiftgoodsImg01())
                    .override(200, 200).placeholder(R.drawable.imgloading) // 设置占位图
                    .into(iv_goodsSmallIcon);
        }

        // 设置规格属性名称
        if (giftGoodsInfos.getGoodsAttr().size() > 0) {
            tv_attr01.setText(giftGoodsInfos.getGoodsAttr().get(0)
                    .getAttrName());
        }
        if (giftGoodsInfos.getGoodsAttr().size() > 1) {
            tv_attr02.setText(giftGoodsInfos.getGoodsAttr().get(1)
                    .getAttrName());
        }

        initStandardGridview();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                this.finish();
                break;

            // 规格图片
            case R.id.iv_goodsSmallIcon:
                if(picUrl.equals("")){
                    return;
                }
                Intent intentPic = new Intent(this,ImagePreviewActivity.class);
                intentPic.putExtra("picUrl",picUrl);
                startActivity(intentPic);
                break;

            // 消息按钮
            case R.id.rl_message:
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;

            // 分享按钮
            case R.id.ib_share:
                shareUtil.shareMessageToSession("校园100", "下载链接", downUrl, null);
                break;

            // 收藏按钮
            case R.id.ib_collect:
                ib_collect.setClickable(false);
                ib_collect.startAnimation(animation);
                if (giftGoodsInfos.getCollectID().equals("0")) {
                    new Thread() {
                        public void run() {
                            postCollect(MyConfig.URL_POST_GIFT_COLLECT);
                        }

                        ;
                    }.start();
                } else {
                    new Thread() {
                        public void run() {
                            postCollectCancel(MyConfig.URL_POST_GIFT_COLLECT_CANCEL);
                        }

                        ;
                    }.start();
                }
                break;

            // 选好了
            case R.id.btn_select_ok:
                if (btn_select_ok.getText().toString().equals("我想要")) {
                    Intent iwantIntent = new Intent(GiftGoodsDetailActivity.this,
                            PayIWantActivity.class);
                    iwantIntent.putExtra("giftgoodsId",
                            giftGoodsInfos.getGiftgoodsId());
                    iwantIntent.putExtra("giftgoodsSn",
                            giftGoodsInfos.getGiftgoodsSn());
                    iwantIntent.putExtra("giftgoodsName",
                            giftGoodsInfos.getGiftgoodsName());
                    iwantIntent.putExtra("giftgoodsImg", giftgoodsImg);
                    iwantIntent.putExtra("shopPrice", price);
                    iwantIntent.putExtra("deliverMoney", giftGoodsInfos.getDeliverMoney());
                    iwantIntent.putExtra("stock",
                            Integer.valueOf(tv_stock_attr.getText().toString()));
                    iwantIntent.putExtra("goodsAttrId", goodsAttrId);
                    iwantIntent.putExtra("skuId", skuId);
                    iwantIntent.putExtra("goodsAttrName", tv_attr01.getText()
                            .toString()
                            + ":"
                            + color
                            + ","
                            + tv_attr02.getText().toString() + ":" + standard);
                    startActivity(iwantIntent);
                } else if (btn_select_ok.getText().toString().equals("送给ta")) {
                    Intent sendIntent = new Intent(GiftGoodsDetailActivity.this,
                            PaySendActivity.class);
                    sendIntent.putExtra("giftgoodsId",
                            giftGoodsInfos.getGiftgoodsId());
                    sendIntent.putExtra("giftgoodsSn",
                            giftGoodsInfos.getGiftgoodsSn());
                    sendIntent.putExtra("giftgoodsName",
                            giftGoodsInfos.getGiftgoodsName());
                    sendIntent.putExtra("giftgoodsImg", giftgoodsImg);
                    sendIntent.putExtra("shopPrice", price);
                    sendIntent.putExtra("deliverMoney", giftGoodsInfos.getDeliverMoney());
                    sendIntent.putExtra("stock",
                            Integer.valueOf(tv_stock_attr.getText().toString()));
                    sendIntent.putExtra("goodsAttrId", goodsAttrId);
                    sendIntent.putExtra("skuId", skuId);
                    sendIntent.putExtra("goodsAttrName", tv_attr01.getText()
                            .toString()
                            + ":"
                            + color
                            + ","
                            + tv_attr02.getText().toString() + ":" + standard);
                    startActivity(sendIntent);
                }
                root_standard.setVisibility(View.GONE);
                break;

            // 规格颜色按钮
            case R.id.ll_select:
                btn_select_ok.setText("选好了");
                root_standard.setVisibility(View.VISIBLE);
                tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                // 初始化gridview
                // initStandardGridview();
                break;

            // 关闭规格颜色界面按钮
            case R.id.ib_close:
                root_standard.setVisibility(View.GONE);
                break;

            // 规格颜色暗部分界面
            case R.id.rl_bg:
                root_standard.setVisibility(View.GONE);
                break;

            // 我想要
            case R.id.btn_IWant:
                btn_select_ok.setText("我想要");
                // if (StandardGridviewAdapter.i == -1 || ColorGridviewAdapter.i ==
                // -1
                // || GoodsInfoInCart.goodsCount < 1) {
                if (!isChecked01 || !isChecked02 || GoodsInfoInCart.goodsCount < 1) {
                    root_standard.setVisibility(View.VISIBLE);
                    // 初始化gridview
                    // initStandardGridview();
                } else {
                    Intent iwantIntent = new Intent(GiftGoodsDetailActivity.this,
                            PayIWantActivity.class);
                    iwantIntent.putExtra("giftgoodsId",
                            giftGoodsInfos.getGiftgoodsId());
                    iwantIntent.putExtra("giftgoodsSn",
                            giftGoodsInfos.getGiftgoodsSn());
                    iwantIntent.putExtra("giftgoodsName",
                            giftGoodsInfos.getGiftgoodsName());
                    iwantIntent.putExtra("giftgoodsImg", giftgoodsImg);
                    iwantIntent.putExtra("shopPrice", price);
                    iwantIntent.putExtra("deliverMoney", giftGoodsInfos.getDeliverMoney());
                    iwantIntent.putExtra("stock",
                            Integer.valueOf(tv_stock_attr.getText().toString()));
                    iwantIntent.putExtra("goodsAttrId", goodsAttrId);
                    iwantIntent.putExtra("skuId", skuId);
                    iwantIntent.putExtra("goodsAttrName", tv_attr01.getText()
                            .toString()
                            + ":"
                            + color
                            + ","
                            + tv_attr02.getText().toString() + ":" + standard);
                    startActivity(iwantIntent);
                }
                break;

            // 送给ta
            case R.id.btn_sentTA:
                btn_select_ok.setText("送给ta");
                // if (StandardGridviewAdapter.i == -1 || ColorGridviewAdapter.i ==
                // -1
                // || GoodsInfoInCart.goodsCount < 1) {
                if (!isChecked01 || !isChecked02 || GoodsInfoInCart.goodsCount < 1) {
                    root_standard.setVisibility(View.VISIBLE);
                    // 初始化gridview
                    // initStandardGridview();
                } else {
                    Intent sendIntent = new Intent(GiftGoodsDetailActivity.this,
                            PaySendActivity.class);
                    sendIntent.putExtra("giftgoodsId",
                            giftGoodsInfos.getGiftgoodsId());
                    sendIntent.putExtra("giftgoodsSn",
                            giftGoodsInfos.getGiftgoodsSn());
                    sendIntent.putExtra("giftgoodsName",
                            giftGoodsInfos.getGiftgoodsName());
                    sendIntent.putExtra("giftgoodsImg", giftgoodsImg);
                    sendIntent.putExtra("shopPrice", price);
                    sendIntent.putExtra("deliverMoney", giftGoodsInfos.getDeliverMoney());
                    sendIntent.putExtra("stock",
                            Integer.valueOf(tv_stock_attr.getText().toString()));
                    sendIntent.putExtra("goodsAttrId", goodsAttrId);
                    sendIntent.putExtra("skuId", skuId);
                    sendIntent.putExtra("goodsAttrName", tv_attr01.getText()
                            .toString()
                            + ":"
                            + color
                            + ","
                            + tv_attr02.getText().toString() + ":" + standard);
                    startActivity(sendIntent);
                }
                break;

            case R.id.ib_reduce:
                if (GoodsInfoInCart.goodsCount > 1) {
                    GoodsInfoInCart.goodsCount--;
                    tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                }
                tv_select.setText("选择：" + color + " " + standard + " x"
                        + GoodsInfoInCart.goodsCount);
                break;

            case R.id.ib_add:
                if (GoodsInfoInCart.goodsCount < Integer.valueOf(tv_stock_attr
                        .getText().toString())) {
                    GoodsInfoInCart.goodsCount++;
                    tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                    tv_select.setText("选择：" + color + " " + standard + " x"
                            + GoodsInfoInCart.goodsCount);
                } else {
                    ToastUtil.showToast(this, "已达最大库存,不能继续添加");
                }
                break;

        }
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
     * 监听手机按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (root_standard.getVisibility() == View.VISIBLE) {
                root_standard.setVisibility(View.GONE);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化gridview
     */
    private void initStandardGridview() {
        if (giftGoodsInfos.getGoodsAttr().size() < 2) {
            return;
        }
        attrVals01 = giftGoodsInfos.getGoodsAttr().get(0).getList();
        attrVals02 = giftGoodsInfos.getGoodsAttr().get(1).getList();

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.rightMargin = 30;
        lp.topMargin = 20;
        for (int i = 0; i < attrVals01.size(); i++) {
            button = new TextView(this);
            button.setId(i);
            button.setText(attrVals01.get(i).getAttrValue());
            button.setTextColor(Color.BLACK);
            button.setTextSize(12);
            button.setPadding(30, 10, 30, 10);
            button.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.shape_gray_f8f8f8));
            button.setLayoutParams(lp);
            mFlowLayout.addView(button);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChecked01 = true;
                    int position = v.getId();
                    for (int j = 0; j < attrVals01.size(); j++) {
                        mFlowLayout.getChildAt(j).setBackgroundDrawable(
                                getResources().getDrawable(
                                        R.drawable.shape_gray_f8f8f8));
                        ((TextView) mFlowLayout.getChildAt(j)).setTextColor(Color.BLACK);
                    }
                    mFlowLayout.getChildAt(position).setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.shape_red));
                    ((TextView) mFlowLayout.getChildAt(position)).setTextColor(Color.WHITE);
                    color = attrVals01.get(position).getAttrValue();
                    tv_selected.setText("选择了 " + color + " " + standard);
                    giftgoodsImg = attrVals01.get(position).getAttrPic();

                    picUrl=giftgoodsImg;
                    Glide.with(GiftGoodsDetailActivity.this).load(giftgoodsImg)
                            .override(200, 200)
                            .placeholder(R.drawable.imgloading) // 设置占位图
                            .into(iv_goodsSmallIcon);
                    colorSymbol = attrVals01.get(position).getAttrSymbol();
                    if (standardSymbol <= colorSymbol) {
                        symbol = standardSymbol + "," + colorSymbol;
                    } else {
                        symbol = colorSymbol + "," + standardSymbol;
                    }
                    for (int i = 0; i < stockInfos.size(); i++) {
                        if (stockInfos.get(i).getAttrSymbol().equals(symbol)) {
                            skuId = stockInfos.get(i).getId();
                            price = stockInfos.get(i).getPrice();
                            tv_goodsPriceStandard.setText("￥" + price);
                            tv_stock_attr.setText(stockInfos.get(i).getStock()
                                    + "");
                            goodsAttrId = stockInfos.get(i).getId();
                            break;
                        } else {
                            tv_goodsPriceStandard.setText("暂无价格");
                            tv_stock_attr.setText("0");
                        }

                    }
                    if (Integer.valueOf(tv_stock_attr.getText().toString()) > 0) {
                        GoodsInfoInCart.goodsCount = 1;
                        tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                        ib_add.setClickable(true);
                        btn_select_ok.setClickable(true);
                        btn_select_ok.setBackgroundColor(getResources()
                                .getColor(R.color.red_fd3c49));
                    } else {
                        GoodsInfoInCart.goodsCount = 0;
                        tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                        btn_select_ok.setClickable(false);
                        btn_select_ok.setBackgroundColor(getResources()
                                .getColor(R.color.gray_5f5f5f));
                    }
                    tv_select.setText("选择：" + color + " " + standard + " x"
                            + GoodsInfoInCart.goodsCount);
                }
            });
        }

        for (int i = 0; i < attrVals02.size(); i++) {
            button = new TextView(this);
            button.setId(i);
            button.setText(attrVals02.get(i).getAttrValue());
            button.setTextColor(Color.BLACK);
            button.setTextSize(12);
            button.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.shape_gray_f8f8f8));
            button.setPadding(30, 10, 30, 10);
            button.setLayoutParams(lp);
            mFlowLayout2.addView(button);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChecked02 = true;
                    int position = v.getId();
                    for (int j = 0; j < attrVals02.size(); j++) {
                        mFlowLayout2.getChildAt(j).setBackgroundDrawable(
                                getResources().getDrawable(
                                        R.drawable.shape_gray_f8f8f8));
                        ((TextView) mFlowLayout2.getChildAt(j)).setTextColor(Color.BLACK);
                    }
                    mFlowLayout2.getChildAt(position).setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.shape_red));
                    ((TextView) mFlowLayout2.getChildAt(position)).setTextColor(Color.WHITE);
                    standard = attrVals02.get(position).getAttrValue();
                    tv_selected.setText("选择了 " + color + " " + standard);
                    standardSymbol = attrVals02.get(position).getAttrSymbol();
                    if (standardSymbol <= colorSymbol) {
                        symbol = standardSymbol + "," + colorSymbol;
                    } else {
                        symbol = colorSymbol + "," + standardSymbol;
                    }
                    for (int i = 0; i < stockInfos.size(); i++) {
                        if (stockInfos.get(i).getAttrSymbol().equals(symbol)) {
                            skuId = stockInfos.get(i).getId();
                            price = stockInfos.get(i).getPrice();
                            tv_goodsPriceStandard.setText("￥" + price);
                            tv_stock_attr.setText(stockInfos.get(i).getStock()
                                    + "");
                            goodsAttrId = stockInfos.get(i).getId();
                            break;
                        } else {
                            tv_goodsPriceStandard.setText("暂无价格");
                            tv_stock_attr.setText("0");
                        }
                    }
                    if (Integer.valueOf(tv_stock_attr.getText().toString()) > 0) {
                        GoodsInfoInCart.goodsCount = 1;
                        tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                        ib_add.setClickable(true);
                        btn_select_ok.setClickable(true);
                        btn_select_ok.setBackgroundColor(getResources()
                                .getColor(R.color.red_fd3c49));
                    } else {
                        GoodsInfoInCart.goodsCount = 0;
                        tv_goodsCount.setText("" + GoodsInfoInCart.goodsCount);
                        ib_add.setClickable(false);
                        btn_select_ok.setClickable(false);
                        btn_select_ok.setBackgroundColor(getResources()
                                .getColor(R.color.gray_5f5f5f));
                    }
                    tv_select.setText("选择：" + color + " " + standard + " x"
                            + GoodsInfoInCart.goodsCount);
                }
            });
        }
    }

    private void postCollect(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", uid);
        params.addBodyParameter("giftgoodsId", giftgoodsId);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            collectID = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (collectID.equals("0")) {
                            ToastUtil.showToast(GiftGoodsDetailActivity.this, "操作失败");
                        } else {
                            ib_collect.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.icon_collect_red));
                            giftGoodsInfos.setCollectID(collectID);
                            giftGoodsInfos.setCollect(giftGoodsInfos
                                    .getCollect() + 1);
                            tv_collect.setText("收藏"
                                    + giftGoodsInfos.getCollect() + "人");
                            EventBus.getDefault().post(
                                    new MEventCollectGiftChange(true));
                        }
                        ib_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(GiftGoodsDetailActivity.this, "操作失败");
                        ib_collect.setClickable(true);
                    }
                });
    }

    private void postCollectCancel(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("cid", collectID);
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
                        if (result.equals("1")) {
                            ib_collect.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.icon_collect));
                            giftGoodsInfos.setCollectID("0");
                            giftGoodsInfos.setCollect(giftGoodsInfos
                                    .getCollect() - 1);
                            tv_collect.setText("收藏"
                                    + giftGoodsInfos.getCollect() + "人");
                            EventBus.getDefault().post(
                                    new MEventCollectGiftChange(true));
                        } else {
                            ToastUtil.showToast(GiftGoodsDetailActivity.this, "操作失败");
                        }
                        ib_collect.setClickable(true);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(GiftGoodsDetailActivity.this, "操作失败");
                        ib_collect.setClickable(true);
                    }
                });
    }

    public void onEventMainThread(MEventGiftGoodsDetails event) {
        if (event.isFinish()) {
            this.finish();
        }
    }

    public void onEventMainThread(MEventSecondPage event) {
        if (event.isShow() && isFirstShow) {
            isFirstShow = false;
            for (int i = 0; i < giftGoodsInfos.getGiftgoodsdetail().size(); i++) {
                ImageView imageView = new ImageView(this);
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                imageView.setAdjustViewBounds(true);
                imageView.setImageDrawable(getResources().getDrawable(
                        R.drawable.imgloading));
                Glide.with(this)
                        .load(giftGoodsInfos.getGiftgoodsdetail().get(i))
                        .dontAnimate().placeholder(R.drawable.imgloading) // 设置占位图
                        .into(imageView);
                imageViews.add(imageView);
                ll_image.addView(imageView);
            }

        }
    }

    @Override
    protected void onResume() {
        if (GoodsInfoInCart.goodsCount > 0 && !standard.equals("")) {
            tv_select.setText("选择：" + color + " " + standard + " x"
                    + GoodsInfoInCart.goodsCount);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        GoodsInfoInCart.goodsCount = 0;
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
