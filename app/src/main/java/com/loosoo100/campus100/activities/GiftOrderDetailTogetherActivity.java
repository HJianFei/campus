package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventGiftRemove;
import com.loosoo100.campus100.anyevent.MEventGiftStatus;
import com.loosoo100.campus100.anyevent.MEventGiftTogRemove;
import com.loosoo100.campus100.beans.GiftOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.pulltorefresh.OnRefreshListener;
import com.loosoo100.campus100.view.pulltorefresh.PtrLayout;
import com.loosoo100.campus100.view.pulltorefresh.header.DefaultRefreshView;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 礼物说订单详情-凑一凑activity
 */
public class GiftOrderDetailTogetherActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮
    @ViewInject(R.id.btn_deliver)
    private Button btn_deliver; // 查看物流详情

    @ViewInject(R.id.ll_percent)
    private LinearLayout ll_percent; // 点击跳转到凑一凑详情页
    @ViewInject(R.id.ll_goods)
    private LinearLayout ll_goods; // 点击跳转到商品详情页
    @ViewInject(R.id.iv_icon)
    private ImageView iv_icon; // 图片
    @ViewInject(R.id.tv_name)
    private TextView tv_name; // 商品名
    @ViewInject(R.id.tv_attr)
    private TextView tv_attr; // 商品属性
    @ViewInject(R.id.tv_count)
    private TextView tv_count; // 商品数量
    @ViewInject(R.id.tv_deliverMoney)
    private TextView tv_deliverMoney; // 运费
    @ViewInject(R.id.tv_money)
    private TextView tv_money; // 商品价格
    @ViewInject(R.id.tv_time)
    private TextView tv_time; // 订单时间
    @ViewInject(R.id.tv_orderNo)
    private TextView tv_orderNo; // 订单编号
    @ViewInject(R.id.tv_userName)
    private TextView tv_userName; // 收货人
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone; // 手机号
    @ViewInject(R.id.tv_address)
    private TextView tv_address; // 收货地址
    @ViewInject(R.id.tv_delivery)
    private TextView tv_delivery; // 快递名称
    @ViewInject(R.id.tv_deliveryNo)
    private TextView tv_deliveryNo; // 快递单号
    @ViewInject(R.id.tv_persent)
    private TextView tv_persent; // 完成百分比
    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar; // 进度条
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update_blackbg)
    private RelativeLayout progress_update_blackbg; // 加载动画

    @ViewInject(R.id.btn_delete)
    private Button btn_delete; // 删除订单按钮
    @ViewInject(R.id.btn_sure)
    private Button btn_sure; // 确认收货按钮
    @ViewInject(R.id.btn_remind)
    private Button btn_remind; // 提醒发货按钮
    @ViewInject(R.id.btn_together)
    private Button btn_together; // 找人凑凑按钮
    @ViewInject(R.id.btn_cancel)
    private Button btn_cancel; // 取消订单按钮
    @ViewInject(R.id.btn_self)
    private Button btn_self; // 自己支付按钮
    @ViewInject(R.id.ll_delivery)
    private LinearLayout ll_delivery; // 物流布局
    @ViewInject(R.id.ll_receiver)
    private LinearLayout ll_receiver; // 收货信息布局
    @ViewInject(R.id.together_ongoing)
    private LinearLayout together_ongoing; // 正在凑的顶部布局
    @ViewInject(R.id.together_delivery)
    private LinearLayout together_delivery; // 待发货的顶部布局
    @ViewInject(R.id.together_sure)
    private LinearLayout together_sure; // 待确认的顶部布局
    @ViewInject(R.id.together_finish)
    private LinearLayout together_finish; // 已完成的顶部布局
    @ViewInject(R.id.refresh)
    private PtrLayout refresh; // 刷新
    private DefaultRefreshView headerView;

    private int type;

    private GiftOrderInfo giftOrderInfo;
    private int index;
    private String oid = "";
    private String uid = "";
    private boolean isLoading = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (giftOrderInfo != null) {
                initView();
            }
            isLoading = false;
            progress_update_blackbg.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            headerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh.onRefreshComplete();
                }
            }, 1000);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_order_detail_together);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        progress.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_remind.setOnClickListener(this);
        btn_together.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_self.setOnClickListener(this);
        btn_deliver.setOnClickListener(this);
        ll_goods.setOnClickListener(this);
        ll_percent.setOnClickListener(this);

        index = getIntent().getExtras().getInt("index");
        type = getIntent().getExtras().getInt("type");
        oid = getIntent().getExtras().getString("oid");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        int[] colors = new int[]{getResources().getColor(R.color.red_fd3c49)};
        headerView = new DefaultRefreshView(this);
        headerView.setColorSchemeColors(colors);
        headerView.setIsPullDown(true);
        refresh.setHeaderView(headerView);

        refresh.setOnPullDownRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (!isLoading) {
                    updateData();
                }
            }
        });

        updateData();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        // 正在凑状态
        if (giftOrderInfo.getIsPay() == 0) {
            hidenAll();
            together_ongoing.setVisibility(View.VISIBLE);
            btn_self.setVisibility(View.VISIBLE);
            btn_together.setVisibility(View.VISIBLE);
            // btn_cancel.setVisibility(View.VISIBLE);
        }
        // 待发货状态
        else if (giftOrderInfo.getIsPay() == 1
                && !giftOrderInfo.getUserAddress().equals("")
                && giftOrderInfo.getStatus() == 0) {
            hidenAll();
            together_delivery.setVisibility(View.VISIBLE);
            btn_remind.setVisibility(View.VISIBLE);
        }
        // 待确认状态
        else if (giftOrderInfo.getIsPay() == 1
                && giftOrderInfo.getStatus() == 1) {
            hidenAll();
            ll_delivery.setVisibility(View.VISIBLE);
            together_sure.setVisibility(View.VISIBLE);
            btn_sure.setVisibility(View.VISIBLE);
        }
        // 已完成状态
        else if (giftOrderInfo.getIsPay() == 1
                && giftOrderInfo.getStatus() == 2) {
            hidenAll();
            ll_delivery.setVisibility(View.VISIBLE);
            together_finish.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
        }

        Glide.with(this)
                .load(giftOrderInfo.getGiftOrderGoodsInfo().getGoodsThums())
                .override(250, 250).placeholder(R.drawable.imgloading)
                .into(iv_icon);
        tv_name.setText(giftOrderInfo.getGiftOrderGoodsInfo().getGoodsName());
        tv_attr.setText(giftOrderInfo.getGiftOrderGoodsInfo()
                .getGoodsAttrName());
        tv_count.setText("x"
                + giftOrderInfo.getGiftOrderGoodsInfo().getGoodsNums());
        tv_deliverMoney.setText("" + giftOrderInfo.getDeliverMoney());
        tv_money.setText("￥" + giftOrderInfo.getNeedPrice());
        tv_time.setText(giftOrderInfo.getTime());
        tv_orderNo.setText(giftOrderInfo.getOrderNO());
        tv_userName.setText(giftOrderInfo.getUserName());
        tv_phone.setText(giftOrderInfo.getUserPhone());
        tv_address.setText(giftOrderInfo.getUserAddress());
        tv_delivery.setText(giftOrderInfo.getLogisticsName());
        tv_deliveryNo.setText(giftOrderInfo.getLogisticsNum());
        if (giftOrderInfo.getPriceNotEnough() <= 0) {
            tv_persent.setText("还差: "
                    + "0元 (已完成 "
                    + (int) ((giftOrderInfo.getNeedPrice() - giftOrderInfo
                    .getPriceNotEnough())
                    / giftOrderInfo.getNeedPrice() * 100) + "%)");
        } else {
            tv_persent.setText("还差: "
                    + giftOrderInfo.getPriceNotEnough()
                    + "元 (已完成 "
                    + (int) ((giftOrderInfo.getNeedPrice() - giftOrderInfo
                    .getPriceNotEnough())
                    / giftOrderInfo.getNeedPrice() * 100) + "%)");
        }

        progressBar
                .setProgress((int) ((giftOrderInfo.getNeedPrice() - giftOrderInfo
                        .getPriceNotEnough()) / giftOrderInfo.getNeedPrice() * 100));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                this.finish();
                break;

            // 跳转到商品详情页
            case R.id.ll_goods:
                Intent intentgoods = new Intent(this, GiftGoodsDetailActivity.class);
                intentgoods.putExtra("giftgoodsId", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsId());
                startActivity(intentgoods);
                GiftOrderReceiveActivity.isUpdate = true;
                GiftOrderActivity.isUpdate = true;
                this.finish();
                break;

            // 跳转到凑一凑详情页
            case R.id.ll_percent:
                Intent intentPercent = new Intent(this,
                        TogetherDetailActivity.class);
                intentPercent.putExtra("oid", oid);
                intentPercent.putExtra("goodsThums", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsThums());
                intentPercent.putExtra("goodsName", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsName());
                intentPercent.putExtra("goodsAttr", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsAttrName());
                intentPercent.putExtra("giftgoodsId", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsId());
                intentPercent.putExtra("money", giftOrderInfo.getNeedPrice());
                startActivity(intentPercent);
                break;

            // 物流详情
            case R.id.btn_deliver:
                Intent intent = new Intent(this, DeliverActivity.class);
                intent.putExtra("num", giftOrderInfo.getLogisticsNum());
                intent.putExtra("com", giftOrderInfo.getLogisticsComs());
                intent.putExtra("name", giftOrderInfo.getLogisticsName());
                startActivity(intent);
                break;

            // 删除订单按钮
            case R.id.btn_delete:
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否确认删除");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update_blackbg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_GIFT_ORDER_DELETE
                                                + giftOrderInfo.getOrderID());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                break;

            // 确认收货按钮
            case R.id.btn_sure:
                CustomDialog.Builder builderSure = new CustomDialog.Builder(this);
                builderSure.setMessage("是否确认收货");
                builderSure.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update_blackbg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postSureData(MyConfig.URL_POST_GIFT_ORDER_SURE
                                                + giftOrderInfo.getOrderID());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builderSure.setNegativeButton("否", null);
                builderSure.create().show();
                break;

            // 提醒收货按钮
            case R.id.btn_remind:
                new Thread() {
                    public void run() {
                        postRemindData(MyConfig.URL_POST_GIFT_ORDER_REMIND
                                + giftOrderInfo.getOrderID());
                    }

                    ;
                }.start();
                break;

            // 凑一凑
            case R.id.btn_together:
                // 跳转到凑一凑填写页面
                Intent friendIntent = new Intent(this, GiftTogetherActivity.class);
                friendIntent.putExtra("oid", giftOrderInfo.getOrderID());
                friendIntent.putExtra("giftgoodsId", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsId());
                friendIntent.putExtra("giftgoodsName", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsName());
                friendIntent.putExtra("giftgoodsImg", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsThums());
                friendIntent.putExtra("shopPrice", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsPrice());
                friendIntent.putExtra("count", giftOrderInfo
                        .getGiftOrderGoodsInfo().getGoodsNums());
                friendIntent.putExtra("type", 2);
                startActivity(friendIntent);
                break;

            // 取消
            case R.id.btn_cancel:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否确认取消");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update_blackbg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_GIFT_ORDER_DELETE
                                                + oid);
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();
                break;

            // 自己支付
            case R.id.btn_self:
                Intent intentSelf = new Intent(this, GiftTogetherPayActivity.class);
                intentSelf.putExtra("index", index);
                intentSelf.putExtra("money", giftOrderInfo.getPriceNotEnough());
                intentSelf.putExtra("oid", giftOrderInfo.getOrderID());
                intentSelf.putExtra("type", type);
                startActivityForResult(intentSelf, 2);
                break;
        }
    }

    /**
     * 提交数据,删除或取消订单
     */
    private void postDeleteData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作成功");
                            if (type == 0) {
                                EventBus.getDefault().post(
                                        new MEventGiftRemove(index));
                            } else if (type == 1) {
                                EventBus.getDefault().post(
                                        new MEventGiftTogRemove(index));
                            }
                            finish();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 提交数据,确认订单
     */
    private void postSureData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_blackbg.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作成功");
                            if (type == 0) {
                                EventBus.getDefault().post(
                                        new MEventGiftStatus(index));
                            } else if (type == 1) {
                                EventBus.getDefault().post(
                                        new MEventGiftTogRemove(index));
                            }
                            progress_update_blackbg.setVisibility(View.VISIBLE);
                            updateData();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作失败");
                    }
                });
    }

    /**
     * 提交数据,提醒发货
     */
    private void postRemindData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
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
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "已提醒商家尽早发货");
                        } else if (result.equals("2")) {
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "您已提醒过了,请勿重复操作");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(GiftOrderDetailTogetherActivity.this, "操作失败");
                    }
                });
    }


    /**
     * 隐藏所有布局按钮
     */
    public void hidenAll() {
        // 隐藏所有按钮
        btn_delete.setVisibility(View.GONE);
        btn_sure.setVisibility(View.GONE);
        btn_remind.setVisibility(View.GONE);
        btn_together.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        btn_self.setVisibility(View.GONE);

        // 隐藏所有状态的顶部布局
        together_ongoing.setVisibility(View.GONE);
        together_delivery.setVisibility(View.GONE);
        together_sure.setVisibility(View.GONE);
        together_finish.setVisibility(View.GONE);

        // 隐藏物流和收货信息
        ll_delivery.setVisibility(View.GONE);

    }

    /**
     * 获取数据
     */
    private void updateData() {
        isLoading = true;
        new Thread() {
            public void run() {
                giftOrderInfo = GetData
                        .getGiftOrderDetailInfo(MyConfig.URL_JSON_GIFT_ORDER_DETAIL
                                + oid);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 2) {
            progress_update_blackbg.setVisibility(View.VISIBLE);
            updateData();
        }
        if (resultCode == RESULT_CANCELED && requestCode == 2) {
            updateData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
