package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.GiftOrderSendAdapter;
import com.loosoo100.campus100.anyevent.MEventGiftSendOrder;
import com.loosoo100.campus100.anyevent.MEventGiftSendRemove;
import com.loosoo100.campus100.beans.GiftOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.spinkit.SpinKitView;
import com.loosoo100.campus100.view.spinkit.style.Wave;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 礼物说订单已送出activity
 */
public class GiftOrderSendActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮

    @ViewInject(R.id.btn_pay)
    private Button btn_pay; // 待付款
    @ViewInject(R.id.btn_send)
    private Button btn_send; // 待送出
    @ViewInject(R.id.btn_delivery)
    private Button btn_delivery; // 待发货
    @ViewInject(R.id.btn_sure)
    private Button btn_sure; // 待确认
    @ViewInject(R.id.btn_finish)
    private Button btn_finish; // 已完成
    @ViewInject(R.id.iv_redline)
    private ImageView iv_redline; // 红条

    @ViewInject(R.id.lv_giftOrder)
    private ListView lv_giftOrder;

    @ViewInject(R.id.progress_small)
    private RelativeLayout progress_small; // 点击不同分类的加载进度条
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 更新时显示的加载动画
    @ViewInject(R.id.progress)
    private SpinKitView progress; // 点击不同分类的加载进度条

    private int x = 0; // 记录当前红条所在位置
    private int width; // 屏幕宽度

    private List<GiftOrderInfo> list;
    private GiftOrderSendAdapter adapter;
    private List<GiftOrderInfo> list2;

    private boolean isLoading = true;
    private int page = 2;
    private String uid = "";
    private int action = 0;

    private Intent intent = new Intent();

    public static boolean isUpdate = false; // 支付后更新界面

    public static int mPosition;
//	public static String orderId;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            progress_small.setVisibility(View.GONE);
            if (list != null && list.size() > 0) {
                initListView();
                lv_giftOrder.setVisibility(View.VISIBLE);
            } else {
                lv_giftOrder.setVisibility(View.GONE);
            }
            isLoading = false;
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            page = 2;
            progress_small.setVisibility(View.GONE);
            progress_update.setVisibility(View.GONE);
            if (list != null && list.size() > 0) {
                adapter = new GiftOrderSendAdapter(GiftOrderSendActivity.this,
                        list);
                lv_giftOrder.setAdapter(adapter);
                lv_giftOrder.setVisibility(View.VISIBLE);
            } else {
                lv_giftOrder.setVisibility(View.GONE);
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_order_send);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        EventBus.getDefault().register(this);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        // 设置加载动画样式
        Wave wave = new Wave();
        progress.setIndeterminateDrawable(wave);
        progress_small.setVisibility(View.VISIBLE);

        WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = systemService.getDefaultDisplay().getWidth() / 5;

        isUpdate = false;
        mPosition = 0;
        initView();

        new Thread() {
            public void run() {
                list = GetData
                        .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                + action + "&userId=" + uid + "&page=1");
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    /**
     * 初始化listView
     */
    private void initListView() {
        adapter = new GiftOrderSendAdapter(this, list);
        lv_giftOrder.setAdapter(adapter);
    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_delivery.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        // 设置红条的宽度和高度
        LayoutParams params = new LayoutParams(width, 10);
        iv_redline.setLayoutParams(params);

        lv_giftOrder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                intent.setClass(GiftOrderSendActivity.this,
                        GiftOrderDetailSendActivity.class);
                intent.putExtra("index", position);
                intent.putExtra("type", 1);
                intent.putExtra("oid", list.get(position).getOrderID());
                startActivity(intent);
            }
        });
        // 监听滑动到底部刷新
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
                                    .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                            + action
                                            + "&userId="
                                            + uid
                                            + "&page=" + page);
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

            case R.id.btn_pay:
                if (action == 0) {
                    return;
                }
                progress_update.setVisibility(View.GONE);
                progress_small.setVisibility(View.VISIBLE);
                action = 0;
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(0));
                // 记录当前红色线条在什么位置
                x = 0 * width;
                resetBtnColor();
                btn_pay.setTextColor(getResources().getColor(R.color.red_fd3c49));
                new Thread() {
                    public void run() {
                        //停留0.3秒让红线滑动不会卡顿
                        SystemClock.sleep(300);
                        if (list != null) {
                            list.clear();
                        }
                        list = GetData
                                .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                        + action + "&userId=" + uid + "&page=1");
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn_send:
                if (action == 1) {
                    return;
                }
                progress_update.setVisibility(View.GONE);
                progress_small.setVisibility(View.VISIBLE);
                action = 1;
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(1));
                // 记录当前红色线条在什么位置
                x = 1 * width;
                resetBtnColor();
                btn_send.setTextColor(getResources().getColor(R.color.red_fd3c49));
                new Thread() {
                    public void run() {
                        //停留0.3秒让红线滑动不会卡顿
                        SystemClock.sleep(300);
                        if (list != null) {
                            list.clear();
                        }
                        list = GetData
                                .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                        + action + "&userId=" + uid + "&page=1");
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn_delivery:
                if (action == 2) {
                    return;
                }
                progress_update.setVisibility(View.GONE);
                progress_small.setVisibility(View.VISIBLE);
                action = 2;
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(2));
                // 记录当前红色线条在什么位置
                x = 2 * width;
                resetBtnColor();
                btn_delivery.setTextColor(getResources().getColor(
                        R.color.red_fd3c49));
                new Thread() {
                    public void run() {
                        //停留0.3秒让红线滑动不会卡顿
                        SystemClock.sleep(300);
                        if (list != null) {
                            list.clear();
                        }
                        list = GetData
                                .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                        + action + "&userId=" + uid + "&page=1");
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn_sure:
                if (action == 3) {
                    return;
                }
                progress_update.setVisibility(View.GONE);
                progress_small.setVisibility(View.VISIBLE);
                action = 3;
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(3));
                // 记录当前红色线条在什么位置
                x = 3 * width;
                resetBtnColor();
                btn_sure.setTextColor(getResources().getColor(R.color.red_fd3c49));
                new Thread() {
                    public void run() {
                        //停留0.3秒让红线滑动不会卡顿
                        SystemClock.sleep(300);
                        if (list != null) {
                            list.clear();
                        }
                        list = GetData
                                .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                        + action + "&userId=" + uid + "&page=1");
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            case R.id.btn_finish:
                if (action == 4) {
                    return;
                }
                progress_update.setVisibility(View.GONE);
                progress_small.setVisibility(View.VISIBLE);
                action = 4;
                // 红色线条平移动画
                iv_redline.startAnimation(translateAnimation(4));
                // 记录当前红色线条在什么位置
                x = 4 * width;
                resetBtnColor();
                btn_finish
                        .setTextColor(getResources().getColor(R.color.red_fd3c49));
                new Thread() {
                    public void run() {
                        //停留0.3秒让红线滑动不会卡顿
                        SystemClock.sleep(300);
                        if (list != null) {
                            list.clear();
                        }
                        list = GetData
                                .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                        + action + "&userId=" + uid + "&page=1");
                        if (!isDestroyed()) {
                            handler2.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

        }
    }

    /**
     * 按钮字体颜色还原初始化
     */
    private void resetBtnColor() {
        btn_pay.setTextColor(Color.BLACK);
        btn_send.setTextColor(Color.BLACK);
        btn_delivery.setTextColor(Color.BLACK);
        btn_sure.setTextColor(Color.BLACK);
        btn_finish.setTextColor(Color.BLACK);
    }

    /**
     * 设置平移动画
     *
     * @return
     */
    private TranslateAnimation translateAnimation(int index) {
        TranslateAnimation translateAnimation = new TranslateAnimation(x, index
                * width, 0, 0);
        translateAnimation.setDuration(300);
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 2) {
            progress_update.setVisibility(View.VISIBLE);
            isUpdate = false;
            new Thread() {
                public void run() {
                    postOrderIdXUtils(MyConfig.URL_POST_GIFT_PAY_SUCCESS);
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
        params.addBodyParameter("orderId", list.get(mPosition).getOrderID());
        params.addBodyParameter("isPay", "1");
        params.addBodyParameter("needPay", list.get(mPosition).getNeedPrice() + "");
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
                            new Thread() {
                                public void run() {
                                    if (list != null) {
                                        list.clear();
                                    }
                                    list = GetData
                                            .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                                    + action
                                                    + "&userId="
                                                    + uid
                                                    + "&page=1");
                                    if (!isDestroyed()) {
                                        handler2.sendEmptyMessage(0);
                                    }
                                }

                                ;
                            }.start();
                            Intent intent = new Intent(GiftOrderSendActivity.this,
                                    GiftSendActivity.class);
                            intent.putExtra("type", 1);
                            intent.putExtra("oid", list.get(mPosition).getOrderID());
                            startActivity(intent);
                        } else {
                            ToastUtil.showToast(GiftOrderSendActivity.this, "支付失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update.setVisibility(View.GONE);
                        ToastUtil.showToast(GiftOrderSendActivity.this, "支付失败");
                    }
                });
    }

    @Override
    protected void onResume() {
        if (isUpdate) {
            progress_update.setVisibility(View.VISIBLE);
            isUpdate = false;
            new Thread() {
                public void run() {
                    if (list != null) {
                        list.clear();
                    }
                    list = GetData
                            .getGiftOrderInfos(MyConfig.URL_JSON_GIFT_ORDER_SEND
                                    + action + "&userId=" + uid + "&page=1");
                    if (!isDestroyed()) {
                        handler2.sendEmptyMessage(0);
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
    public void onEventMainThread(MEventGiftSendOrder event) {
        if (event.getShow()) {
            progress_update.setVisibility(View.VISIBLE);
        } else {
            progress_update.setVisibility(View.GONE);
        }
    }

    /**
     * 删除订单
     *
     * @param event
     */
    public void onEventMainThread(MEventGiftSendRemove event) {
        list.remove(event.getPosition());
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
