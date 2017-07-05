package com.loosoo100.campus100.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.AppraiseActivity;
import com.loosoo100.campus100.activities.PayActivity;
import com.loosoo100.campus100.activities.StoreOrderActivity;
import com.loosoo100.campus100.anyevent.MEventStoreOrder;
import com.loosoo100.campus100.anyevent.MEventStoreRemove;
import com.loosoo100.campus100.beans.StoreOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 超市订单列表适配器
 */
public class StoreOrderAdapter extends BaseAdapter {

    private List<StoreOrderInfo> list;
    private LayoutInflater inflater;
    private StoreOrderActivity activity;

    public boolean isShowGarbage = false;
    private ViewHolder viewHolder = null;

    private int mPosition;

    public StoreOrderAdapter(Context context, List<StoreOrderInfo> list) {
        this.list = list;

        activity = (StoreOrderActivity) context;
        isShowGarbage = false;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_store_order, null);

            viewHolder.rl_call = (RelativeLayout) convertView
                    .findViewById(R.id.rl_call);
            viewHolder.rl_pay = (RelativeLayout) convertView
                    .findViewById(R.id.rl_pay);
            viewHolder.rl_sureReceived = (RelativeLayout) convertView
                    .findViewById(R.id.rl_sureReceived);
            viewHolder.rl_appraise = (RelativeLayout) convertView
                    .findViewById(R.id.rl_appraise);
            viewHolder.rl_appraised = (RelativeLayout) convertView
                    .findViewById(R.id.rl_appraised);

            viewHolder.tv_orderState = (TextView) convertView
                    .findViewById(R.id.tv_orderState);
            viewHolder.tv_orderTime = (TextView) convertView
                    .findViewById(R.id.tv_orderTime);
            viewHolder.tv_campusName = (TextView) convertView
                    .findViewById(R.id.tv_campusName);
            viewHolder.tv_howmany = (TextView) convertView
                    .findViewById(R.id.tv_howmany);
            viewHolder.tv_howmuch = (TextView) convertView
                    .findViewById(R.id.tv_howmuch);
            viewHolder.iv_campus = (ImageView) convertView
                    .findViewById(R.id.iv_campus);
            viewHolder.iv_garbage = (ImageView) convertView
                    .findViewById(R.id.iv_garbage);
            viewHolder.iv_status_bg = (ImageView) convertView
                    .findViewById(R.id.iv_status_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_orderTime.setText(list.get(position).getCreateTime());
        viewHolder.tv_campusName.setText(list.get(position).getShopName());
        viewHolder.tv_howmany.setText(list.get(position).getCount() + "");
        viewHolder.tv_howmuch.setText(list.get(position).getNeedPay() + "");
        Glide.with(activity).load(list.get(position).getShopImg())
                .into(viewHolder.iv_campus);

        viewHolder.iv_garbage.setTag(position);

        // 是否显示删除图标
        if (isShowGarbage) {
            // 退款成功
            if (list.get(position).getIsRefund().toString().equals("2")) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg.setBackgroundDrawable(activity
                        .getResources().getDrawable(
                                R.drawable.shape_gray_b3b3b3));
                viewHolder.tv_orderState.setText("退款成功");
                viewHolder.iv_garbage.setVisibility(View.VISIBLE);
            }
            // 退款中
            else if (list.get(position).getIsRefund().toString().equals("1")) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("退款中");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }

            // 未付款
            else if (list.get(position).getIsPay().equals("0")) {
                hideButton();
                // viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.rl_pay.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg.setBackgroundDrawable(activity
                        .getResources().getDrawable(
                                R.drawable.shape_gray_b3b3b3));
                viewHolder.tv_orderState.setText("未付款");
                viewHolder.iv_garbage.setVisibility(View.VISIBLE);
            }
            // 订单已提交
            else if (list.get(position).getIsPay().equals("1")
                    && list.get(position).getOrderStatus() == 0) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("订单已提交");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
            // 商家已接单
            else if (list.get(position).getOrderStatus() == 1) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("商家已接单");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }

            // 配送中
            else if (list.get(position).getOrderStatus() == 3) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.rl_sureReceived.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("配送中");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
            // 已完成
            else if (list.get(position).getOrderStatus() == 4) {
                hideButton();
                // viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("已完成");
                if (list.get(position).getIsAppraise().equals("1")) {
                    viewHolder.rl_appraised.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.rl_appraise.setVisibility(View.VISIBLE);
                }
                viewHolder.iv_garbage.setVisibility(View.VISIBLE);
            }
        } else {
            // 退款成功
            if (list.get(position).getIsRefund().toString().equals("2")) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg.setBackgroundDrawable(activity
                        .getResources().getDrawable(
                                R.drawable.shape_gray_b3b3b3));
                viewHolder.tv_orderState.setText("退款成功");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
            // 退款中
            else if (list.get(position).getIsRefund().toString().equals("1")) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("退款中");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }

            // 未付款
            else if (list.get(position).getIsPay().equals("0")) {
                hideButton();
                // viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.rl_pay.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg.setBackgroundDrawable(activity
                        .getResources().getDrawable(
                                R.drawable.shape_gray_b3b3b3));
                viewHolder.tv_orderState.setText("未付款");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
            // 订单已提交
            else if (list.get(position).getIsPay().equals("1")
                    && list.get(position).getOrderStatus() == 0) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("订单已提交");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
            // 商家已接单
            else if (list.get(position).getOrderStatus() == 1) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("商家已接单");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }

            // 配送中
            else if (list.get(position).getOrderStatus() == 3) {
                hideButton();
                viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.rl_sureReceived.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("配送中");
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
            // 已完成
            else if (list.get(position).getOrderStatus() == 4) {
                hideButton();
                // viewHolder.rl_call.setVisibility(View.VISIBLE);
                viewHolder.iv_status_bg
                        .setBackgroundDrawable(activity.getResources()
                                .getDrawable(R.drawable.shape_red_order));
                viewHolder.tv_orderState.setText("已完成");
                if (list.get(position).getIsAppraise().equals("1")) {
                    viewHolder.rl_appraised.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.rl_appraise.setVisibility(View.VISIBLE);
                }
                viewHolder.iv_garbage.setVisibility(View.GONE);
            }
        }

        // 退款中
        // else if (list.get(position).getOrderState().toString()
        // .equals(MyConfig.STATE_REFUNDING)) {
        // hideButton();
        // viewHolder.rl_call.setVisibility(View.VISIBLE);
        // viewHolder.iv_status_bg.setBackgroundDrawable(activity
        // .getResources().getDrawable(R.drawable.shape_red_order));
        // }

        // 是否显示删除图标
        // if (isShowGarbage) {
        // viewHolder.iv_garbage.setVisibility(View.VISIBLE);
        // } else {
        // viewHolder.iv_garbage.setVisibility(View.GONE);
        // }

        // 删除订单
        viewHolder.iv_garbage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                CustomDialog.Builder builder = new CustomDialog.Builder(
                        activity);
                builder.setMessage("是否确认删除");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                EventBus.getDefault().post(
                                        new MEventStoreOrder(true));
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_GOODS_ORDER_DELETE
                                                + list.get(position)
                                                .getOrderId());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();

                // StoreOrderActivity.progress_update.setVisibility(View.VISIBLE);
                // mPosition = position;
                // new Thread() {
                // public void run() {
                // postDeleteData(MyConfig.URL_POST_GOODS_ORDER_DELETE
                // + list.get(position).getOrderId());
                // };
                // }.start();
            }
        });

        // 联系商家
        viewHolder.rl_call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent();
                Intent.setAction(Intent.ACTION_VIEW);
                Intent.setData(Uri.parse("tel:"
                        + list.get(position).getShopPhone()));
                activity.startActivity(Intent);
            }
        });

        // 立即支付
        viewHolder.rl_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                StoreOrderActivity.position = position;
                Intent intent = new Intent(activity, PayActivity.class);
                intent.putExtra("money", list.get(position).getNeedPay());
                intent.putExtra("orderId", list.get(position).getOrderId());
                intent.putExtra("cid", "0");
                intent.putExtra("type", "mark");
                activity.startActivityForResult(intent, 1);
            }
        });

        // 确认收货
        viewHolder.rl_sureReceived.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                CustomDialog.Builder builder = new CustomDialog.Builder(
                        activity);
                builder.setMessage("是否确认收货");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                EventBus.getDefault().post(
                                        new MEventStoreOrder(true));
                                new Thread() {
                                    public void run() {
                                        postSureData(MyConfig.URL_POST_GOODS_ORDER_SURE
                                                + list.get(position)
                                                .getOrderId());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();
            }
        });

        // 评价按钮
        viewHolder.rl_appraise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                Intent appraiseIntent = new Intent(activity,
                        AppraiseActivity.class);
                appraiseIntent.putExtra("index", position);
                appraiseIntent.putExtra("oid", list.get(position).getOrderId());
                activity.startActivity(appraiseIntent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tv_orderState, tv_orderTime, tv_campusName,
                tv_howmany, tv_howmuch;
        private ImageView iv_campus;
        private ImageView iv_garbage, iv_status_bg;
        private RelativeLayout rl_call; // 联系商家
        private RelativeLayout rl_pay; // 立即付款
        private RelativeLayout rl_sureReceived; // 确认收货
        private RelativeLayout rl_appraise; // 评价
        private RelativeLayout rl_appraised; // 已评价
    }

    /**
     * 隐藏联系商家、确认收货、立即付款、评价按钮
     */
    private void hideButton() {
        viewHolder.rl_call.setVisibility(View.GONE);
        viewHolder.rl_pay.setVisibility(View.GONE);
        viewHolder.rl_sureReceived.setVisibility(View.GONE);
        viewHolder.rl_appraise.setVisibility(View.GONE);
        viewHolder.rl_appraised.setVisibility(View.GONE);
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
                        EventBus.getDefault().post(new MEventStoreOrder(false));
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(activity, "操作成功");
                            list.get(mPosition).setOrderStatus(4);
                            StoreOrderAdapter.this.notifyDataSetChanged();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                        EventBus.getDefault().post(new MEventStoreOrder(false));
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
                        EventBus.getDefault().post(new MEventStoreOrder(false));
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            EventBus.getDefault().post(
                                    new MEventStoreRemove(mPosition));
                            ToastUtil.showToast(activity, "操作成功");
//							list.remove(mPosition);
//							StoreOrderAdapter.this.notifyDataSetChanged();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                        EventBus.getDefault().post(new MEventStoreOrder(false));
                    }
                });
    }

}
