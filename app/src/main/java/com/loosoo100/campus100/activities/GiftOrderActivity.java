package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.GiftOrderAdapter;
import com.loosoo100.campus100.anyevent.MEventGiftIsPay;
import com.loosoo100.campus100.anyevent.MEventGiftNotEnough;
import com.loosoo100.campus100.anyevent.MEventGiftOrder;
import com.loosoo100.campus100.anyevent.MEventGiftRemove;
import com.loosoo100.campus100.anyevent.MEventGiftStatus;
import com.loosoo100.campus100.beans.GiftOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.pulltorefresh.OnRefreshListener;
import com.loosoo100.campus100.view.pulltorefresh.PtrLayout;
import com.loosoo100.campus100.view.pulltorefresh.header.DefaultRefreshView;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author yang 礼品清单activity
 */
public class GiftOrderActivity extends Activity implements OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private View rl_back;

    @ViewInject(R.id.ll_send)
    private LinearLayout ll_send; // 送出的礼物

    @ViewInject(R.id.ll_receive)
    private LinearLayout ll_receive; // 收到的礼物

    @ViewInject(R.id.ll_together)
    private LinearLayout ll_together; // 凑一凑

    @ViewInject(R.id.lv_giftOrder)
    private ListView lv_giftOrder; // 订单列表

    @ViewInject(R.id.progress_small)
    private RelativeLayout progress_small; // 点击不同分类的加载进度条
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 更新时显示的加载动画
    @ViewInject(R.id.progress)
    private SpinKitView progress; // 点击不同分类的加载进度条

    @ViewInject(R.id.refresh)
    private PtrLayout refresh; // 刷新
    @ViewInject(R.id.tv_nodata)
    private TextView tv_nodata; // 暂无数据

    private List<GiftOrderInfo> list;
    // public static List<GiftOrderInfo> list;
    private List<GiftOrderInfo> list2;
    private GiftOrderAdapter adapter;
    // public static GiftOrderAdapter adapter;

    private boolean isLoading = true; // 是否正在获取数据
    private int page = 2;
    private String uid = "";
    public static int mPosition;

    private boolean isChange = false;
    public static boolean isUpdate = false; // 支付后更新界面

    private Intent intent = new Intent();

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                initListView();
                lv_giftOrder.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
            } else {
                lv_giftOrder.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
            progress_small.setVisibility(View.GONE);
            isLoading = false;
        }

        ;
    };

    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            isLoading = false;
            page++;
        }

        ;
    };

    private Handler handlerUpdate = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress_update.setVisibility(View.GONE);
            if (list != null && list.size() > 0) {
                adapter = new GiftOrderAdapter(GiftOrderActivity.this, list);
                lv_giftOrder.setAdapter(adapter);
                lv_giftOrder.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
            } else {
                lv_giftOrder.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
            isLoading = false;
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_order);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        EventBus.getDefault().register(this);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        // 设置加载动画样式
        Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
        progress_small.setVisibility(View.VISIBLE);

        page = 2;
        isUpdate = false;
        mPosition = 0;
        initView();

        int[] colors = new int[]{getResources().getColor(R.color.red_fd3c49)};
        final DefaultRefreshView headerView = new DefaultRefreshView(this);
        headerView.setColorSchemeColors(colors);
        headerView.setIsPullDown(true);
        refresh.setHeaderView(headerView);

        refresh.setOnPullDownRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread() {
                    public void run() {
                        list = GetData
                                .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER
                                        + uid + "&page=1");
                        page = 2;
                        headerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refresh.onRefreshComplete();
                            }
                        }, 1000);
                        if (!isDestroyed()) {
                            handlerUpdate.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
            }
        });

        new Thread() {
            public void run() {
                list = GetData.getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER
                        + uid + "&page=1");
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    private void initListView() {
        adapter = new GiftOrderAdapter(this, list);
        lv_giftOrder.setAdapter(adapter);

        lv_giftOrder.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * NeedType: 0为收到的礼物,1为送出的礼物,2凑一凑
                 */
                /*
				 * 收到的礼物
				 */
                if (list.get(position).getNeedType() == 0) {
                    intent.setClass(GiftOrderActivity.this,
                            GiftOrderDetailReceiveActivity.class);
                    intent.putExtra("index", position);
                    intent.putExtra("type", 0);
                    intent.putExtra("oid", list.get(position).getOrderID());
                    startActivity(intent);
                }
				/*
				 * 送出的礼物
				 */
                else if (list.get(position).getNeedType() == 1) {
                    intent.setClass(GiftOrderActivity.this,
                            GiftOrderDetailSendActivity.class);
                    intent.putExtra("index", position);
                    intent.putExtra("type", 0);
                    intent.putExtra("oid", list.get(position).getOrderID());
                    startActivity(intent);
                }
				/*
				 * 凑一凑
				 */
                else if (list.get(position).getNeedType() == 2) {
                    intent.setClass(GiftOrderActivity.this,
                            GiftOrderDetailTogetherActivity.class);
                    intent.putExtra("index", position);
                    intent.putExtra("type", 0);
                    intent.putExtra("oid", list.get(position).getOrderID());
                    startActivity(intent);
                }

            }
        });
    }

    private void initView() {
        rl_back.setOnClickListener(this);
        ll_send.setOnClickListener(this);
        ll_receive.setOnClickListener(this);
        ll_together.setOnClickListener(this);

        lv_giftOrder.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            list2 = GetData
                                    .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER
                                            + uid + "&page=" + page);
                            if (list2 != null && list2.size() > 0) {
                                for (int i = 0; i < list2.size(); i++) {
                                    list.add(list2.get(i));
                                }
                                if (!isDestroyed()) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.ll_send:
                isChange = true;
                intent.setClass(this, GiftOrderSendActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_receive:
                isChange = true;
                intent.setClass(this, GiftOrderReceiveActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_together:
                isChange = true;
                intent.setClass(this, GiftOrderTogetherActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 2) {
            if (progress_small.getVisibility() == View.GONE) {
                progress_update.setVisibility(View.VISIBLE);
            }
            // 付款成功后post订单ID到后台
            new Thread() {
                public void run() {
                    postOrderIdXUtils(MyConfig.URL_POST_GIFT_PAY_SUCCESS);
                }

                ;
            }.start();
        }
    }

    /**
     * 付款成功后提交订单ID
     *
     * @param uploadHost
     */
    private void postOrderIdXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderId", list.get(mPosition).getOrderID());
        params.addBodyParameter("isPay", "1");
        params.addBodyParameter("needPay",list.get(mPosition).getNeedPrice()+"");
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
                            ToastUtil.showToast(GiftOrderActivity.this, "支付成功");
                            // 更新订单的状态
                            progress_update.setVisibility(View.GONE);
                            list.get(mPosition).setIsPay(1);
                            adapter.notifyDataSetChanged();
                            if (list.get(mPosition).getNeedType() == 1) {
                                Intent intent = new Intent(GiftOrderActivity.this,
                                        GiftSendActivity.class);
                                intent.putExtra("type", 3);
                                intent.putExtra("oid", list.get(mPosition)
                                        .getOrderID());
                                startActivity(intent);
                            }
                        }else{
                            ToastUtil.showToast(GiftOrderActivity.this, "支付失败");
                        }

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(GiftOrderActivity.this, "支付失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onResume() {
        if (isChange || isUpdate) {
            progress_update.setVisibility(View.VISIBLE);
            isChange = false;
            isUpdate = false;
            new Thread() {
                public void run() {
                    list = GetData
                            .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER
                                    + uid + "&page=1");
                    page = 2;
                    if (!isDestroyed()) {
                        handlerUpdate.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
        super.onResume();
    }

    /**
     * 是否显示加载动画
     *
     * @param event
     */
    public void onEventMainThread(MEventGiftOrder event) {
        if (event.getShow()) {
            progress_update.setVisibility(View.VISIBLE);
        } else {
            progress_update.setVisibility(View.GONE);
        }
    }

    /**
     * 更新订单状态为已付款
     *
     * @param event
     */
    public void onEventMainThread(MEventGiftIsPay event) {
        list.get(event.getPosition()).setIsPay(1);
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新凑一凑订单还差多少钱
     *
     * @param event
     */
    public void onEventMainThread(MEventGiftNotEnough event) {
        if (event.getYetMoney() == 0) {
            list.get(event.getPosition()).setIsPay(1);
        }
        list.get(event.getPosition()).setPriceNotEnough(event.getYetMoney());
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除订单
     *
     * @param event
     */
    public void onEventMainThread(MEventGiftRemove event) {
        list.remove(event.getPosition());
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新订单状态
     *
     * @param event
     */
    public void onEventMainThread(MEventGiftStatus event) {
        list.get(event.getPosition()).setStatus(2);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
