package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.loosoo100.campus100.adapters.OrderDetailAdapter;
import com.loosoo100.campus100.anyevent.MEventStoreIsPay;
import com.loosoo100.campus100.anyevent.MEventStoreRefund;
import com.loosoo100.campus100.anyevent.MEventStoreRemove;
import com.loosoo100.campus100.anyevent.MEventStoreStatus;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.beans.StoreOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.pulltorefresh.OnRefreshListener;
import com.loosoo100.campus100.view.pulltorefresh.PtrLayout;
import com.loosoo100.campus100.view.pulltorefresh.header.DefaultRefreshView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 小卖部订单详情activity
 */
public class StoreOrderDetailActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮
    @ViewInject(R.id.rl_call)
    private RelativeLayout rl_call;

    @ViewInject(R.id.iv_campusIcon)
    private ImageView iv_campusIcon; // 商家图标

    @ViewInject(R.id.lv_orderDetail)
    private ListView lv_orderDetail; // 订单详情列表

    @ViewInject(R.id.tv_orderState)
    private TextView tv_orderState; // 顶部状态内容

    @ViewInject(R.id.tv_orderState02)
    private TextView tv_orderState02; // 顶部状态内容小

    @ViewInject(R.id.ll_nonPayment)
    private LinearLayout ll_nonPayment; // 未付款显示内容
    @ViewInject(R.id.ll_committed)
    private LinearLayout ll_committed; // 订单已提交显示内容
    @ViewInject(R.id.ll_received)
    private LinearLayout ll_received; // 商家已接单显示内容
    @ViewInject(R.id.ll_sending)
    private LinearLayout ll_sending; // 配送中显示内容
    @ViewInject(R.id.ll_finish)
    private LinearLayout ll_finish; // 已完成显示内容

    @ViewInject(R.id.ll_deliverMember)
    private LinearLayout ll_deliverMember; // 配送员
    @ViewInject(R.id.ll_pay_way)
    private LinearLayout ll_pay_way; // 支付方式内容
    @ViewInject(R.id.ll_order_time_finish)
    private LinearLayout ll_order_time_finish; // 订单签收时间内容

    @ViewInject(R.id.btn_complain)
    private Button btn_complain; // 投诉
    @ViewInject(R.id.btn_refund)
    private Button btn_refund; // 退款
    @ViewInject(R.id.btn_received)
    private Button btn_received; // 确认收货
    @ViewInject(R.id.btn_pay)
    private Button btn_pay; // 付款
    @ViewInject(R.id.btn_appraise)
    private Button btn_appraise; // 评价
    // @ViewInject(R.id.btn_cancel)
    // private Button btn_cancel; // 取消

    @ViewInject(R.id.tv_totalPrice)
    private TextView tv_totalPrice;
    @ViewInject(R.id.tv_campusName)
    private TextView tv_campusName;
    @ViewInject(R.id.tv_expressFee)
    private TextView tv_expressFee;
    @ViewInject(R.id.tv_send_way)
    private TextView tv_send_way;
    @ViewInject(R.id.tv_send_person)
    private TextView tv_send_person;
    @ViewInject(R.id.tv_orderNumber)
    private TextView tv_orderNumber;
    @ViewInject(R.id.tv_customName)
    private TextView tv_customName;
    @ViewInject(R.id.tv_customTel)
    private TextView tv_customTel;
    @ViewInject(R.id.tv_customAddress)
    private TextView tv_customAddress;
    @ViewInject(R.id.tv_pay_way)
    private TextView tv_pay_way;
    @ViewInject(R.id.tv_order_time_submit)
    private TextView tv_order_time_submit;
    @ViewInject(R.id.tv_order_time_finish)
    private TextView tv_order_time_finish;

    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 加载动画
    @ViewInject(R.id.ratingBar)
    private RatingBar ratingBar; // 星星评分

    @ViewInject(R.id.refresh)
    private PtrLayout refresh; // 刷新
    private DefaultRefreshView headerView;
    private boolean isLoading = false;

    private OrderDetailAdapter adapter;

    private List<GoodsInfo> orderList;

    // private int status;
    // private String isPay = "";
    private String payType = "0"; // 0钱包 1 微信 2支付宝
    private String oid = "";
    // private String uid="";
    private int index;
    private int type; // 判断是从付款后进入还是从订单列表进入0订单列表1付款窗口

    private Intent intent = new Intent();

    private StoreOrderInfo storeOrderInfo;
    // public static StoreOrderInfo storeOrderInfo;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            if (storeOrderInfo.getList() != null) {
                // 初始化listView
                initListView();
            }
            if (storeOrderInfo.getIsPay().equals("1")) {
                if (type == 0) {
                    // StoreOrderActivity.list.get(index).setIsPay("1");
                    // StoreOrderActivity.adapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new MEventStoreIsPay(index));
                }
            }
            isLoading = false;
            progress.setVisibility(View.GONE);
            progress_update.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_order_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        progress.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        rl_call.setOnClickListener(this);

        btn_complain.setOnClickListener(this);
        btn_refund.setOnClickListener(this);
        btn_received.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_appraise.setOnClickListener(this);
        // btn_cancel.setOnClickListener(this);

        oid = getIntent().getExtras().getString("oid");
        index = getIntent().getExtras().getInt("index");
        type = getIntent().getExtras().getInt("type");
        // uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
        // .getString(UserInfoDB.USERID, "");

        updateData();

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

    }

    /**
     * 初始化listView
     */
    private void initListView() {
        orderList = storeOrderInfo.getList();
        adapter = new OrderDetailAdapter(this, orderList);
        lv_orderDetail.setAdapter(adapter);
        MyUtils.setListViewHeight(lv_orderDetail, 0);
    }

    private void initView() {
        tv_totalPrice.setText("￥" + storeOrderInfo.getNeedPay());
        tv_campusName.setText(storeOrderInfo.getShopName());
        tv_expressFee.setText("￥" + storeOrderInfo.getDeliverMoney());
        if (storeOrderInfo.getIsSelf().equals("0")) {
            tv_send_way.setText("商家配送");
        } else if (storeOrderInfo.getIsSelf().equals("1")) {
            tv_send_way.setText("到店自取");
        }
        if (storeOrderInfo.getPayType().equals("0")) {
            tv_pay_way.setText("余额支付");
        } else if (storeOrderInfo.getPayType().equals("1")) {
            tv_pay_way.setText("微信支付");
        } else if (storeOrderInfo.getPayType().equals("2")) {
            tv_pay_way.setText("支付宝支付");
        }
        tv_send_person.setText(storeOrderInfo.getDeliverMember());
        tv_orderNumber.setText("订单号 " + storeOrderInfo.getOrderNo());
        tv_customName.setText(storeOrderInfo.getUserName());
        tv_customTel.setText(storeOrderInfo.getUserPhone());
        tv_customAddress.setText(storeOrderInfo.getUserAddress());
        tv_order_time_submit.setText(storeOrderInfo.getCreateTime());
        Glide.with(this).load(storeOrderInfo.getShopImg()).into(iv_campusIcon);

        // 未付款
        if (storeOrderInfo.getIsPay().equals("0")) {
            hideAll();
            tv_orderState.setText("未付款");
            tv_orderState02.setText("等待您付款");
            ll_nonPayment.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.VISIBLE);
            // btn_cancel.setVisibility(View.VISIBLE);
        }

        // 订单已提交
        else if (storeOrderInfo.getIsPay().equals("1")
                && storeOrderInfo.getOrderStatus() == 0) {
            hideAll();
            tv_orderState.setText("订单已提交");
            tv_orderState02.setText("等待商家接单");

            ll_committed.setVisibility(View.VISIBLE);
            ll_pay_way.setVisibility(View.VISIBLE);
            // btn_cancel.setVisibility(View.VISIBLE);
            btn_refund.setVisibility(View.VISIBLE);
            btn_complain.setVisibility(View.VISIBLE);
        }

        // 商家已接单
        else if (storeOrderInfo.getOrderStatus() == 1) {
            hideAll();
            tv_orderState.setText("商家已接单");
            tv_orderState02.setText("正在为您准备商品");

            ll_received.setVisibility(View.VISIBLE);
            ll_pay_way.setVisibility(View.VISIBLE);
            btn_complain.setVisibility(View.VISIBLE);
            btn_refund.setVisibility(View.VISIBLE);
        }

        // 配送中
        else if (storeOrderInfo.getOrderStatus() == 3) {
            hideAll();
            tv_orderState.setText("配送中");
            tv_orderState02.setText("正在为您配送商品");

            ll_sending.setVisibility(View.VISIBLE);
            ll_deliverMember.setVisibility(View.VISIBLE);
            ll_pay_way.setVisibility(View.VISIBLE);
            btn_complain.setVisibility(View.VISIBLE);
            btn_refund.setVisibility(View.VISIBLE);
            btn_received.setVisibility(View.VISIBLE);
        }

        // 已完成
        else if (storeOrderInfo.getOrderStatus() == 4) {
            hideAll();
            tv_orderState.setText("已完成");
            tv_orderState02.setText("欢迎再次购买");
            if (storeOrderInfo.getFinishTime() != null
                    && !storeOrderInfo.getFinishTime().equals("")) {
                tv_order_time_finish.setText(storeOrderInfo.getFinishTime());
                ll_order_time_finish.setVisibility(View.VISIBLE);
            }
            ll_finish.setVisibility(View.VISIBLE);
            ll_deliverMember.setVisibility(View.VISIBLE);
            ll_pay_way.setVisibility(View.VISIBLE);
            btn_complain.setVisibility(View.VISIBLE);
            btn_appraise.setVisibility(View.VISIBLE);
        }

        // 退款中
        if (storeOrderInfo.getIsRefund().toString().equals("1")) {
            // hideAll();
            btn_refund.setVisibility(View.GONE);
            btn_received.setVisibility(View.GONE);
            tv_orderState.setText("退款中");
            tv_orderState02.setText("商家处理中");
            btn_complain.setVisibility(View.VISIBLE);
        }

        // 退款成功
        if (storeOrderInfo.getIsRefund().toString().equals("2")) {
            hideAll();
            tv_orderState.setText("退款成功");
            tv_orderState02.setText("欢迎再次购买");
            ll_nonPayment.setVisibility(View.VISIBLE);
            // btn_cancel.setVisibility(View.VISIBLE);
        }

        // 设置评分
        if (storeOrderInfo.getIsAppraise().equals("1")) {
            ratingBar.setVisibility(View.VISIBLE);
            btn_appraise.setVisibility(View.GONE);
            ratingBar.setNumStars(storeOrderInfo.getStar());
        } else {
            ratingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_back:
                this.finish();
                break;

            // 打电话
            case R.id.rl_call:
                Intent Intent = new Intent();
                Intent.setAction(Intent.ACTION_VIEW);
                Intent.setData(Uri.parse("tel:" + storeOrderInfo.getShopPhone()));
                startActivity(Intent);
                break;

            // 评价
            case R.id.btn_appraise:
                intent.setClass(this, AppraiseActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("oid", oid);
                startActivityForResult(intent, 2);
                break;

            // 投诉
            case R.id.btn_complain:
                intent.setClass(this, ComplainActivity.class);
                intent.putExtra("orderId", storeOrderInfo.getOrderId());
                intent.putExtra("shopId", storeOrderInfo.getShopid());
                startActivity(intent);
                break;

            // 退款
            case R.id.btn_refund:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否确认申请退款");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postDataRefund(MyConfig.URL_POST_GOODS_ORDER_REFUND);
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();

                // progress_update.setVisibility(View.VISIBLE);
                // new Thread() {
                // public void run() {
                // postDataRefund(MyConfig.URL_POST_GOODS_ORDER_REFUND);
                // };
                // }.start();
                break;

            // 收货
            case R.id.btn_received:
                CustomDialog.Builder builder2 = new CustomDialog.Builder(this);
                builder2.setMessage("是否确认收货");
                builder2.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postSureData(MyConfig.URL_POST_GOODS_ORDER_SURE
                                                + storeOrderInfo.getOrderId());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder2.setNegativeButton("否", null);
                builder2.create().show();

                // progress_update.setVisibility(View.VISIBLE);
                // new Thread() {
                // public void run() {
                // postSureData(MyConfig.URL_POST_GOODS_ORDER_SURE
                // + storeOrderInfo.getOrderId());
                // };
                // }.start();
                break;

            // 取消
            case R.id.btn_cancel:
                CustomDialog.Builder builder3 = new CustomDialog.Builder(this);
                builder3.setMessage("是否确认删除");
                builder3.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_GOODS_ORDER_DELETE
                                                + storeOrderInfo.getOrderId());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder3.setNegativeButton("否", null);
                builder3.create().show();

                // progress_update.setVisibility(View.VISIBLE);
                // new Thread() {
                // public void run() {
                // postDeleteData(MyConfig.URL_POST_GOODS_ORDER_DELETE
                // + storeOrderInfo.getOrderId());
                // };
                // }.start();
                break;

            // 付款
            case R.id.btn_pay:
                intent.setClass(this, PayActivity.class);
                intent.putExtra("money", storeOrderInfo.getNeedPay());
                intent.putExtra("orderId", oid);
                intent.putExtra("cid", "0");
                intent.putExtra("type", "mark");
                startActivityForResult(intent, 1);
                break;

        }
    }

    /**
     * 隐藏下方按钮和所有状态显示内容
     */
    private void hideAll() {
        ll_nonPayment.setVisibility(View.GONE);
        ll_committed.setVisibility(View.GONE);
        ll_received.setVisibility(View.GONE);
        ll_sending.setVisibility(View.GONE);
        ll_finish.setVisibility(View.GONE);

        btn_complain.setVisibility(View.GONE);
        btn_refund.setVisibility(View.GONE);
        btn_received.setVisibility(View.GONE);
        btn_pay.setVisibility(View.GONE);
        btn_appraise.setVisibility(View.GONE);
        // btn_cancel.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            progress_update.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    postOrderIdXUtils(MyConfig.URL_POST_GOODS_PAY_SUCCESS);
                }

                ;
            }.start();
        }
        if (resultCode == RESULT_CANCELED && requestCode == 1) {
            updateData();
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            storeOrderInfo.setIsAppraise("1");
            storeOrderInfo.setStar(data.getExtras().getInt("score"));
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                        progress_update.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(StoreOrderDetailActivity.this, "操作成功");
                            if (type == 0) {
                                EventBus.getDefault().post(
                                        new MEventStoreStatus(index));
                            }
                            progress_update.setVisibility(View.VISIBLE);
                            updateData();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(StoreOrderDetailActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(StoreOrderDetailActivity.this, "操作失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 付款成功后提交订单ID
     *
     * @param uploadHost
     */
    private void postOrderIdXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderId", storeOrderInfo.getOrderId() + "");
        params.addBodyParameter("isPay", "1");
        params.addBodyParameter("payType", payType);
        params.addBodyParameter("needPay", storeOrderInfo.getNeedPay() + "");
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
                        if (result.equals("1")) {
                            ToastUtil.showToast(StoreOrderDetailActivity.this, "支付成功");
                            if (type == 0) {
                                EventBus.getDefault().post(
                                        new MEventStoreIsPay(index));
                            }
                            progress_update.setVisibility(View.VISIBLE);
                            updateData();
                        } else {
                            ToastUtil.showToast(StoreOrderDetailActivity.this, "支付失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update.setVisibility(View.GONE);
                        ToastUtil.showToast(StoreOrderDetailActivity.this, "支付失败");
                    }
                });
    }

    /**
     * 提交数据,删除订单
     */
    private void postDeleteData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
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
                        if (result.equals("1")) {
                            ToastUtil.showToast(StoreOrderDetailActivity.this, "操作成功");
                            if (type == 0) {
                                EventBus.getDefault().post(
                                        new MEventStoreRemove(index));
                            }
                            StoreOrderDetailActivity.this.finish();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(StoreOrderDetailActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(StoreOrderDetailActivity.this, "操作失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 提交数据,退款
     *
     * @param uploadHost
     */
    private void postDataRefund(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderid", storeOrderInfo.getOrderId());
        params.addBodyParameter("needPay", storeOrderInfo.getNeedPay() + "");
        params.addBodyParameter("userid", storeOrderInfo.getUserId());
        params.addBodyParameter("shopid", storeOrderInfo.getShopid());
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        if (type == 0) {
                            EventBus.getDefault().post(
                                    new MEventStoreRefund(index));
                        }
                        ToastUtil.showToast(StoreOrderDetailActivity.this, "申请退款成功");
                        progress_update.setVisibility(View.VISIBLE);
                        updateData();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(StoreOrderDetailActivity.this, "提交失败");
                    }
                });
    }

    @Override
    protected void onRestart() {
        if (storeOrderInfo.getIsAppraise().equals("1")) {
            ratingBar.setVisibility(View.VISIBLE);
            btn_appraise.setVisibility(View.GONE);
            ratingBar.setNumStars(storeOrderInfo.getStar());
        } else {
            ratingBar.setVisibility(View.GONE);
        }
        super.onRestart();
    }

    /**
     * 获取数据
     */
    private void updateData() {
        isLoading = true;
        new Thread() {
            public void run() {
                storeOrderInfo = GetData
                        .getOrderInfo(MyConfig.URL_JSON_GOODS_ORDER_DETAIL
                                + oid);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
    }

    @Override
    protected void onDestroy() {
        storeOrderInfo = null;
        super.onDestroy();
    }

}
