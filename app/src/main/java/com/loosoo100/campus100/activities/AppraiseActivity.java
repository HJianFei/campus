package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
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
import com.loosoo100.campus100.adapters.AppraiseAdapter;
import com.loosoo100.campus100.anyevent.MEventStoreAppraise;
import com.loosoo100.campus100.beans.StoreOrderInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 评价activity
 */
public class AppraiseActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回按钮
	@ViewInject(R.id.rl_call)
	private RelativeLayout rl_call; // 打电话

	@ViewInject(R.id.lv_appraise)
	private ListView lv_appraise; // 订单列表

	@ViewInject(R.id.iv_campusIcon)
	private ImageView iv_campusIcon; // 店铺图标
	@ViewInject(R.id.tv_campusName)
	private TextView tv_campusName; // 店铺名
	@ViewInject(R.id.ratingBar_appraise)
	private RatingBar ratingBar_appraise; // 服务评星
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画
	@ViewInject(R.id.progress_update)
	private RelativeLayout progress_update; // 加载动画

	@ViewInject(R.id.btn_submit)
	private Button btn_submit; // 提交按钮

	private int score = 0;
	private int index;
	private String oid;

	private StoreOrderInfo orderInfo;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (orderInfo != null) {
				initView();
				initListView();
			}
			progress.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appraise);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		index = getIntent().getExtras().getInt("index");
		oid = getIntent().getExtras().getString("oid");

		progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);
		rl_call.setOnClickListener(this);

		new Thread() {
			public void run() {
				orderInfo = GetData
						.getOrderAppraiseInfo(MyConfig.URL_JSON_GOODS_ORDER_DETAIL
								+ oid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initView() {

		btn_submit.setOnClickListener(this);
		btn_submit.setBackground(getResources().getDrawable(
				R.drawable.shape_red_ff8786));

		tv_campusName.setText(orderInfo.getShopName());
		Glide.with(this).load(orderInfo.getShopImg()).into(iv_campusIcon);

		// 服务评分控件监听
		ratingBar_appraise
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						ratingBar_appraise.setRating(rating);
						score = (int) rating;
						if (rating > 0) {
							btn_submit.setBackground(getResources()
									.getDrawable(R.drawable.shape_red));
						} else {
							btn_submit.setBackground(getResources()
									.getDrawable(R.drawable.shape_red_ff8786));
						}
					}
				});
	}

	private void initListView() {
		lv_appraise.setAdapter(new AppraiseAdapter(this, orderInfo.getList()));
		MyUtils.setListViewHeight(lv_appraise, 0);
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
			Intent.setData(Uri.parse("tel:" + orderInfo.getShopPhone()));
			startActivity(Intent);
			break;

		// 提交按钮
		case R.id.btn_submit:
			if (score > 0) {
				progress_update.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						postData(MyConfig.URL_POST_GOODS_ORDER_APPRAISE);
					};
				}.start();
			}
			break;
		}
	}

	/**
	 * 付款成功后提交订单ID
	 * 
	 * @param uploadHost
	 */
	private void postData(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("shopId", orderInfo.getShopid());
		params.addBodyParameter("userName", orderInfo.getUserName());
		params.addBodyParameter("shopName", orderInfo.getShopName());
		params.addBodyParameter("attitude", score + "");
		params.addBodyParameter("orderId", oid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						ToastUtil.showToast(AppraiseActivity.this,"评价成功");
						// 更改详情页的状态
//						if (StoreOrderDetailActivity.storeOrderInfo != null) {
//							StoreOrderDetailActivity.storeOrderInfo
//									.setIsAppraise("1");
//							StoreOrderDetailActivity.storeOrderInfo.setStar(score);
//						}
						// 更改订单列表的状态
//						if (StoreOrderActivity.list != null) {
//							StoreOrderActivity.list.get(index).setIsAppraise(
//									"1");
//							StoreOrderActivity.adapter.notifyDataSetChanged();
//						}
						EventBus.getDefault().post(new MEventStoreAppraise(index));
						progress_update.setVisibility(View.GONE);
						Intent intent = new Intent();
						intent.putExtra("score", score);
						setResult(RESULT_OK, intent);
						AppraiseActivity.this.finish();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("arg1",arg1.toString());
						progress_update.setVisibility(View.GONE);
						ToastUtil.showToast(AppraiseActivity.this,"操作失败");
					}
				});
	}

}
