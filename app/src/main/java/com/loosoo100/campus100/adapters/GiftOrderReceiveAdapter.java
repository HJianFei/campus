package com.loosoo100.campus100.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.GiftOrderReceiveActivity;
import com.loosoo100.campus100.activities.PayActivity;
import com.loosoo100.campus100.anyevent.MEventGiftRecOrder;
import com.loosoo100.campus100.beans.GiftOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 礼物说收到订单适配器
 */
public class GiftOrderReceiveAdapter extends BaseAdapter {

    private List<GiftOrderInfo> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder = null;

    private GiftOrderReceiveActivity activity;

    private int mPosition;

    public GiftOrderReceiveAdapter(Context context, List<GiftOrderInfo> list) {
        this.list = list;

        activity = (GiftOrderReceiveActivity) context;

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
            convertView = inflater.inflate(R.layout.item_gift_order, null);
            viewHolder.ll_persent = (LinearLayout) convertView
                    .findViewById(R.id.ll_persent);
            viewHolder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_status);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.tv_goodsName = (TextView) convertView
                    .findViewById(R.id.tv_goodsName);
            viewHolder.tv_count = (TextView) convertView
                    .findViewById(R.id.tv_count);
            viewHolder.tv_money = (TextView) convertView
                    .findViewById(R.id.tv_money);
            viewHolder.tv_standard = (TextView) convertView
                    .findViewById(R.id.tv_standard);
            viewHolder.iv_icon = (ImageView) convertView
                    .findViewById(R.id.iv_icon);
            viewHolder.btn_delete = (Button) convertView
                    .findViewById(R.id.btn_delete);
            viewHolder.btn_sure = (Button) convertView
                    .findViewById(R.id.btn_sure);
            viewHolder.btn_send = (Button) convertView
                    .findViewById(R.id.btn_send);
            viewHolder.btn_pay = (Button) convertView
                    .findViewById(R.id.btn_pay);
            viewHolder.btn_cancel = (Button) convertView
                    .findViewById(R.id.btn_cancel);
            viewHolder.btn_remind = (Button) convertView
                    .findViewById(R.id.btn_remind);
            viewHolder.btn_together = (Button) convertView
                    .findViewById(R.id.btn_together);
            viewHolder.btn_self = (Button) convertView
                    .findViewById(R.id.btn_self);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ll_persent.setVisibility(View.GONE);
        viewHolder.tv_time.setText("" + list.get(position).getTime());
        viewHolder.tv_goodsName.setText(""
                + list.get(position).getGiftOrderGoodsInfo().getGoodsName());
        viewHolder.tv_count.setText("x"
                + list.get(position).getGiftOrderGoodsInfo().getGoodsNums());
        viewHolder.tv_money.setText("￥" + list.get(position).getNeedPrice());
        viewHolder.tv_standard.setText(list.get(position)
                .getGiftOrderGoodsInfo().getGoodsAttrName());

        viewHolder.btn_pay.setTag(position);
        viewHolder.btn_send.setTag(position);
        viewHolder.btn_delete.setTag(position);
        viewHolder.btn_sure.setTag(position);
        viewHolder.btn_cancel.setTag(position);
        viewHolder.btn_remind.setTag(position);
        viewHolder.btn_self.setTag(position);
        viewHolder.btn_together.setTag(position);

        Glide.with(activity)
                .load(list.get(position).getGiftOrderGoodsInfo()
                        .getGoodsThums()).override(250, 250)
                .placeholder(R.drawable.imgloading).into(viewHolder.iv_icon);

        if (list.get(position).getIsPay() == 0) {
            viewHolder.tv_status.setText("待付款");
            if (Integer.valueOf(viewHolder.btn_pay.getTag().toString()) == position
                    && Integer.valueOf(viewHolder.btn_cancel.getTag()
                    .toString()) == position) {
                hidenButton();
                viewHolder.btn_pay.setVisibility(View.VISIBLE);
                viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && list.get(position).getNeedType() == 1
                && list.get(position).getUserAddress().equals("")) {
            viewHolder.tv_status.setText("待送出");
            if (Integer.valueOf(viewHolder.btn_send.getTag().toString()) == position) {
                hidenButton();
                viewHolder.btn_send.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && !list.get(position).getUserAddress().equals("")
                && list.get(position).getStatus() == 0) {
            viewHolder.tv_status.setText("待发货");
            if (Integer.valueOf(viewHolder.btn_remind.getTag().toString()) == position) {
                hidenButton();
                viewHolder.btn_remind.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && list.get(position).getStatus() == 1) {
            viewHolder.tv_status.setText("待确认");
            if (Integer.valueOf(viewHolder.btn_sure.getTag().toString()) == position) {
                hidenButton();
                viewHolder.btn_sure.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && list.get(position).getStatus() == 2) {
            viewHolder.tv_status.setText("已完成");
            if (Integer.valueOf(viewHolder.btn_delete.getTag().toString()) == position) {
                hidenButton();
                viewHolder.btn_delete.setVisibility(View.VISIBLE);
            }
        }

		/*
         * 删除订单
		 */
        viewHolder.btn_delete.setOnClickListener(new OnClickListener() {
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
                                        new MEventGiftRecOrder(true));
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_GIFT_ORDER_DELETE
                                                + list.get(position)
                                                .getOrderID());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();

            }
        });

		/*
		 * 立即付款
		 */
        viewHolder.btn_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftOrderReceiveActivity.mPosition = position;
                GiftOrderReceiveActivity.isUpdate = true;
                Intent intent = new Intent(activity, PayActivity.class);
                intent.putExtra("money", list.get(position).getNeedPrice());
                intent.putExtra("orderId", list.get(position).getOrderID());
                intent.putExtra("cid", "0");
                intent.putExtra("type", "gift");
                activity.startActivityForResult(intent, 2);
            }
        });

		/*
		 * 提醒发货
		 */
        viewHolder.btn_remind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        postRemindData(MyConfig.URL_POST_GIFT_ORDER_REMIND
                                + list.get(position).getOrderID());
                    }

                    ;
                }.start();
            }
        });

		/*
		 * 取消订单
		 */
        viewHolder.btn_cancel.setOnClickListener(new OnClickListener() {
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
                                        new MEventGiftRecOrder(true));
                                new Thread() {
                                    public void run() {
                                        postDeleteData(MyConfig.URL_POST_GIFT_ORDER_DELETE
                                                + list.get(position)
                                                .getOrderID());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();

            }
        });

		/*
		 * 送出礼物
		 */
        viewHolder.btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

		/*
		 * 确认收货
		 */
        viewHolder.btn_sure.setOnClickListener(new OnClickListener() {
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
                                        new MEventGiftRecOrder(true));
                                new Thread() {
                                    public void run() {
                                        postSureData(MyConfig.URL_POST_GIFT_ORDER_SURE
                                                + list.get(position)
                                                .getOrderID());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();

            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView tv_status, tv_time, tv_goodsName, tv_count, tv_money,
                tv_standard;
        public Button btn_delete, btn_sure, btn_send, btn_pay, btn_cancel,
                btn_remind, btn_self, btn_together;
        public ImageView iv_icon;
        public LinearLayout ll_persent;
    }

    public void hidenButton() {
        viewHolder.btn_delete.setVisibility(View.GONE);
        viewHolder.btn_sure.setVisibility(View.GONE);
        viewHolder.btn_send.setVisibility(View.GONE);
        viewHolder.btn_pay.setVisibility(View.GONE);
        viewHolder.btn_cancel.setVisibility(View.GONE);
        viewHolder.btn_remind.setVisibility(View.GONE);
        viewHolder.btn_self.setVisibility(View.GONE);
        viewHolder.btn_together.setVisibility(View.GONE);
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
                            list.remove(mPosition);
                            GiftOrderReceiveAdapter.this.notifyDataSetChanged();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                        EventBus.getDefault().post(
                                new MEventGiftRecOrder(false));
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                        EventBus.getDefault().post(
                                new MEventGiftRecOrder(false));
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
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(activity, "订单已确认");
                            list.remove(mPosition);
                            GiftOrderReceiveAdapter.this.notifyDataSetChanged();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                        EventBus.getDefault().post(
                                new MEventGiftRecOrder(false));
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                        EventBus.getDefault().post(
                                new MEventGiftRecOrder(false));
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
                            ToastUtil.showToast(activity, "已提醒商家尽早发货");
                        } else if (result.equals("2")) {
                            ToastUtil.showToast(activity, "您已提醒过了,请勿重复操作");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                    }
                });
    }

}
