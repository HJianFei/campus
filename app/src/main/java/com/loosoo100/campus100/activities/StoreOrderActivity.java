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
import com.loosoo100.campus100.adapters.StoreOrderAdapter;
import com.loosoo100.campus100.anyevent.MEventStoreAppraise;
import com.loosoo100.campus100.anyevent.MEventStoreIsPay;
import com.loosoo100.campus100.anyevent.MEventStoreOrder;
import com.loosoo100.campus100.anyevent.MEventStoreRefund;
import com.loosoo100.campus100.anyevent.MEventStoreRemove;
import com.loosoo100.campus100.anyevent.MEventStoreStatus;
import com.loosoo100.campus100.beans.StoreOrderInfo;
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
 * @author yang 超市订单activity
 */
public class StoreOrderActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮

    @ViewInject(R.id.rl_edit)
    private RelativeLayout rl_edit; // 编辑按钮

    @ViewInject(R.id.tv_edit)
    private TextView tv_edit; // 编辑文本

    @ViewInject(R.id.lv_campusOrder)
    private ListView lv_campusOrder; // 订单列表
    @ViewInject(R.id.tv_nodata)
    private TextView tv_nodata; // 暂无数据

    @ViewInject(R.id.progress_small)
    private RelativeLayout progress_small; // 点击不同分类的加载进度条
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 更新时显示的加载动画
    @ViewInject(R.id.progress)
    private SpinKitView progress; // 点击不同分类的加载进度条

    @ViewInject(R.id.refresh)
    private PtrLayout refresh; // 刷新

    private List<StoreOrderInfo> list;
    private List<StoreOrderInfo> list2;
    private StoreOrderAdapter adapter;

    private String uid = "";
    public static int position = 0;
    private boolean isLoading = true;
    private int page = 2;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                rl_edit.setVisibility(View.VISIBLE);
                initListView();
                lv_campusOrder.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
            } else {
                rl_edit.setVisibility(View.GONE);
                lv_campusOrder.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
            isLoading = false;
            progress_small.setVisibility(View.GONE);
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
            if (list != null && list.size() > 0) {
                rl_edit.setVisibility(View.VISIBLE);
                adapter = new StoreOrderAdapter(StoreOrderActivity.this, list);
                if (tv_edit.getText().toString().equals("编辑")) {
                    adapter.isShowGarbage = false;
                } else {
                    adapter.isShowGarbage = true;
                }
                lv_campusOrder.setAdapter(adapter);
                lv_campusOrder.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
//				if (tv_edit.getText().toString().equals("编辑")) {
//					adapter.isShowGarbage = true;
//					tv_edit.setText("完成");
//				} else {
//					adapter.isShowGarbage = false;
//					tv_edit.setText("编辑");
//				}
//				adapter.notifyDataSetChanged();
            } else {
                rl_edit.setVisibility(View.GONE);
                lv_campusOrder.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }
            isLoading = false;
            page = 2;
            progress_update.setVisibility(View.GONE);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        EventBus.getDefault().register(this);

        position = 0;
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        initView();

        int[] colors = new int[]{getResources().getColor(R.color.red_fd3c49)};
        final DefaultRefreshView headerView = new DefaultRefreshView(this);
        headerView.setColorSchemeColors(colors);
        headerView.setIsPullDown(true);
        refresh.setHeaderView(headerView);

        new Thread() {
            public void run() {
                list = GetData.getOrderInfos(MyConfig.URL_JSON_GOODS_ORDER
                        + uid + "&page=1");
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        refresh.setOnPullDownRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    public void run() {
                        list = GetData
                                .getOrderInfos(MyConfig.URL_JSON_GOODS_ORDER
                                        + uid + "&page=1");
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

    }

    private void initListView() {
        adapter = new StoreOrderAdapter(this, list);
        lv_campusOrder.setAdapter(adapter);

        lv_campusOrder.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(StoreOrderActivity.this,
                        StoreOrderDetailActivity.class);
                // intent.putExtra("status",
                // list.get(position).getOrderStatus());
                intent.putExtra("type", 0);
                intent.putExtra("index", position);
                intent.putExtra("oid", list.get(position).getOrderId());
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化控件
     */
    private void initView() {
        rl_back.setOnClickListener(this);
        rl_edit.setOnClickListener(this);

        // 设置加载动画样式
        Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
        progress_small.setVisibility(View.VISIBLE);

        lv_campusOrder.setOnScrollListener(new OnScrollListener() {

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
                                    .getOrderInfos(MyConfig.URL_JSON_GOODS_ORDER
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

            case R.id.rl_edit:
                if (adapter == null) {
                    return;
                }
                if (tv_edit.getText().toString().equals("编辑")) {
                    adapter.isShowGarbage = true;
                    tv_edit.setText("完成");
                } else {
                    adapter.isShowGarbage = false;
                    tv_edit.setText("编辑");
                }
                adapter.notifyDataSetChanged();
                break;

        }
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
            progress_update.setVisibility(View.VISIBLE);
            new Thread() {
                public void run() {
                    list = GetData.getOrderInfos(MyConfig.URL_JSON_GOODS_ORDER
                            + uid + "&page=1");
                    if (!isDestroyed()) {
                        handlerUpdate.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 付款成功后提交订单ID
     *
     * @param uploadHost
     */
    private void postOrderIdXUtils(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("orderId", list.get(position).getOrderId() + "");
        params.addBodyParameter("isPay", "1");
        params.addBodyParameter("payType", "0");
        params.addBodyParameter("needPay", list.get(position).getNeedPay() + "");
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
                            ToastUtil.showToast(StoreOrderActivity.this, "支付成功");
                            list.get(position).setIsPay("1");
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(StoreOrderActivity.this, "支付失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update.setVisibility(View.GONE);
                        ToastUtil.showToast(StoreOrderActivity.this, "支付失败");
                    }
                });
    }

    public void onEventMainThread(MEventStoreOrder event) {
        if (event.getShow()) {
            progress_update.setVisibility(View.VISIBLE);
        } else {
            progress_update.setVisibility(View.GONE);
        }
    }

    /**
     * 更新订单状态为已评价
     *
     * @param event
     */
    public void onEventMainThread(MEventStoreAppraise event) {
        list.get(event.getPosition()).setIsAppraise("1");
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新订单状态为4
     *
     * @param event
     */
    public void onEventMainThread(MEventStoreStatus event) {
        list.get(event.getPosition()).setOrderStatus(4);
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新订单状态为已付款
     *
     * @param event
     */
    public void onEventMainThread(MEventStoreIsPay event) {
        list.get(event.getPosition()).setIsPay("1");
        adapter.notifyDataSetChanged();
    }

    /**
     * 更新订单状态为退款中
     *
     * @param event
     */
    public void onEventMainThread(MEventStoreRefund event) {
        list.get(event.getPosition()).setIsRefund("1");
        adapter.notifyDataSetChanged();
    }

    /**
     * 移除订单
     *
     * @param event
     */
    public void onEventMainThread(MEventStoreRemove event) {
        list.remove(event.getPosition());
        adapter.notifyDataSetChanged();
        if (list.size() == 0) {
            lv_campusOrder.setVisibility(View.GONE);
            rl_edit.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        } else {
            lv_campusOrder.setVisibility(View.VISIBLE);
            rl_edit.setVisibility(View.VISIBLE);
            tv_nodata.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
