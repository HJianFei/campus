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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.GiftOrderTogetherActivity;
import com.loosoo100.campus100.activities.GiftTogetherActivity;
import com.loosoo100.campus100.activities.GiftTogetherPayActivity;
import com.loosoo100.campus100.anyevent.MEventGiftTogOrder;
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
 * @author yang 礼物说凑一凑订单适配器
 */
public class GiftOrderTogetherAdapter extends BaseAdapter {

    private List<GiftOrderInfo> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder = null;

    private GiftOrderTogetherActivity activity;

    private int mPosition;

    public GiftOrderTogetherAdapter(Context context, List<GiftOrderInfo> list) {
        this.list = list;

        activity = (GiftOrderTogetherActivity) context;

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
            convertView = inflater.inflate(R.layout.item_gift_order_together,
                    null);
            viewHolder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_status);
            viewHolder.tv_persent = (TextView) convertView
                    .findViewById(R.id.tv_persent);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.progressBar = (ProgressBar) convertView
                    .findViewById(R.id.progressBar);
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
            viewHolder.btn_sure = (Button) convertView
                    .findViewById(R.id.btn_sure);
            viewHolder.btn_delete = (Button) convertView
                    .findViewById(R.id.btn_delete);
            viewHolder.btn_remind = (Button) convertView
                    .findViewById(R.id.btn_remind);
            viewHolder.btn_cancel = (Button) convertView
                    .findViewById(R.id.btn_cancel);
            viewHolder.btn_self = (Button) convertView
                    .findViewById(R.id.btn_self);
            viewHolder.btn_together = (Button) convertView
                    .findViewById(R.id.btn_together);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.btn_delete.setTag(position);
        viewHolder.btn_sure.setTag(position);
        viewHolder.btn_cancel.setTag(position);
        viewHolder.btn_remind.setTag(position);
        viewHolder.btn_self.setTag(position);
        viewHolder.btn_together.setTag(position);

        if (list.get(position).getPriceNotEnough() <= 0) {
            viewHolder.tv_persent.setText("还差: "
                    + "0元 (已完成 "
                    + (int) ((list.get(position).getNeedPrice() - list.get(
                    position).getPriceNotEnough())
                    / list.get(position).getNeedPrice() * 100) + "%)");
        } else {
            viewHolder.tv_persent.setText("还差: "
                    + list.get(position).getPriceNotEnough()
                    + "元 (已完成 "
                    + (int) ((list.get(position).getNeedPrice() - list.get(
                    position).getPriceNotEnough())
                    / list.get(position).getNeedPrice() * 100) + "%)");
        }

        viewHolder.tv_time.setText(list.get(position).getTime());
        viewHolder.progressBar.setProgress((int) ((list.get(position)
                .getNeedPrice() - list.get(position).getPriceNotEnough())
                / list.get(position).getNeedPrice() * 100));
        viewHolder.tv_goodsName.setText(""
                + list.get(position).getGiftOrderGoodsInfo().getGoodsName());
        viewHolder.tv_count.setText("x"
                + list.get(position).getGiftOrderGoodsInfo().getGoodsNums());
        viewHolder.tv_money.setText("￥" + list.get(position).getNeedPrice());
        viewHolder.tv_standard.setText(list.get(position)
                .getGiftOrderGoodsInfo().getGoodsAttrName());

        Glide.with(activity)
                .load(list.get(position).getGiftOrderGoodsInfo()
                        .getGoodsThums()).override(250, 250)
                .placeholder(R.drawable.imgloading).into(viewHolder.iv_icon);

        if (list.get(position).getIsPay() == 0) {
            viewHolder.tv_status.setText("进行中");
            if (Integer.valueOf(viewHolder.btn_self.getTag().toString()) == position
                    && Integer.valueOf(viewHolder.btn_together.getTag()
                    .toString()) == position
                    && Integer.valueOf(viewHolder.btn_cancel.getTag()
                    .toString()) == position) {
                resetButton();
                viewHolder.btn_self.setVisibility(View.VISIBLE);
                viewHolder.btn_together.setVisibility(View.VISIBLE);
                // viewHolder.btn_cancel.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && !list.get(position).getUserAddress().equals("")
                && list.get(position).getStatus() == 0) {
            viewHolder.tv_status.setText("待发货");
            if (Integer.valueOf(viewHolder.btn_remind.getTag().toString()) == position) {
                resetButton();
                viewHolder.btn_remind.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && list.get(position).getStatus() == 1) {
            viewHolder.tv_status.setText("待确认");
            if (Integer.valueOf(viewHolder.btn_sure.getTag().toString()) == position) {
                resetButton();
                viewHolder.btn_sure.setVisibility(View.VISIBLE);
            }
        } else if (list.get(position).getIsPay() == 1
                && list.get(position).getStatus() == 2) {
            viewHolder.tv_status.setText("已完成");
            if (Integer.valueOf(viewHolder.btn_delete.getTag().toString()) == position) {
                resetButton();
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
                                        new MEventGiftTogOrder(true));
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
		 * 自己支付
		 */
        viewHolder.btn_self.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                Intent intent = new Intent(activity,
                        GiftTogetherPayActivity.class);
                intent.putExtra("money", list.get(position).getPriceNotEnough());
                intent.putExtra("oid", list.get(position).getOrderID());
                intent.putExtra("index", position);
                intent.putExtra("type", 1);
                activity.startActivityForResult(intent, 0);
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
                                        new MEventGiftTogOrder(true));
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
		 * 找人凑一凑
		 */
        viewHolder.btn_together.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                // 跳转到凑一凑填写页面
                Intent friendIntent = new Intent(activity,
                        GiftTogetherActivity.class);
                friendIntent.putExtra("oid", list.get(position).getOrderID());
                friendIntent.putExtra("giftgoodsId", list.get(position)
                        .getGiftOrderGoodsInfo().getGoodsId());
                friendIntent.putExtra("giftgoodsName", list.get(position)
                        .getGiftOrderGoodsInfo().getGoodsName());
                friendIntent.putExtra("giftgoodsImg", list.get(position)
                        .getGiftOrderGoodsInfo().getGoodsThums());
                friendIntent.putExtra("shopPrice", list.get(position)
                        .getGiftOrderGoodsInfo().getGoodsPrice());
                friendIntent.putExtra("count", list.get(position)
                        .getGiftOrderGoodsInfo().getGoodsNums());
                friendIntent.putExtra("type", 1);
                activity.startActivity(friendIntent);
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
                                        new MEventGiftTogOrder(true));
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
        public TextView tv_persent, tv_status, tv_goodsName, tv_count,
                tv_money, tv_standard, tv_time;
        public Button btn_sure, btn_cancel, btn_self, btn_together, btn_delete,
                btn_remind;
        public ImageView iv_icon;
        public ProgressBar progressBar;
    }

    public void resetButton() {
        viewHolder.btn_sure.setVisibility(View.GONE);
        viewHolder.btn_cancel.setVisibility(View.GONE);
        viewHolder.btn_self.setVisibility(View.GONE);
        viewHolder.btn_together.setVisibility(View.GONE);
        viewHolder.btn_delete.setVisibility(View.GONE);
        viewHolder.btn_remind.setVisibility(View.GONE);
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
                            GiftOrderTogetherAdapter.this
                                    .notifyDataSetChanged();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                        EventBus.getDefault().post(
                                new MEventGiftTogOrder(false));
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                        EventBus.getDefault().post(
                                new MEventGiftTogOrder(false));
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
                            ToastUtil.showToast(activity, "操作成功");
                            list.remove(mPosition);
                            GiftOrderTogetherAdapter.this
                                    .notifyDataSetChanged();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(activity, "操作失败");
                        }
                        EventBus.getDefault().post(
                                new MEventGiftTogOrder(false));
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(activity, "操作失败");
                        EventBus.getDefault().post(
                                new MEventGiftTogOrder(false));
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
