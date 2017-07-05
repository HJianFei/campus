package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.GiftTogetherDetailAdapter;
import com.loosoo100.campus100.beans.GiftTogetherInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 凑一凑详情activity
 */
public class TogetherDetailActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.iv_icon)
	private ImageView iv_icon; // 商品图标
	@ViewInject(R.id.tv_name)
	private TextView tv_name; // 商品名
	@ViewInject(R.id.tv_attr)
	private TextView tv_attr; // 商品属性
	@ViewInject(R.id.tv_money)
	private TextView tv_money; // 商品价格
	@ViewInject(R.id.progressBar)
	private ProgressBar progressBar; // 进度条
	@ViewInject(R.id.tv_persent)
	private TextView tv_persent; // 百分比
	@ViewInject(R.id.tv_moneyNotEnough)
	private TextView tv_moneyNotEnough; // 还差多少元
	@ViewInject(R.id.ll_noData)
	private LinearLayout ll_noData; // 没数据时的显示
	@ViewInject(R.id.listView)
	private ListView listView; // 列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画

	private String oid = "";
	private String goodsThums = "";
	private String goodsName = "";
	private String goodsAttr = "";
	private String giftgoodsId = "";
	private float money;
	private int page = 1;

	private boolean isLoading = true;

	private GiftTogetherInfo giftTogetherInfo;

	private GiftTogetherDetailAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (giftTogetherInfo != null) {
				initView();
			}
			if (giftTogetherInfo.getList() != null
					&& giftTogetherInfo.getList().size() > 0) {
				initListView();
				listView.setVisibility(View.VISIBLE);
				ll_noData.setVisibility(View.GONE);
			} else {
				listView.setVisibility(View.GONE);
				ll_noData.setVisibility(View.VISIBLE);
			}
			page++;
			isLoading = false;
			progress.setVisibility(View.GONE);

		}
	};

	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				MyUtils.setListViewHeight(listView, 20);
			}
			isLoading = false;
			page++;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_together_detail);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		progress.setVisibility(View.VISIBLE);

		oid = getIntent().getExtras().getString("oid");
		goodsThums = getIntent().getExtras().getString("goodsThums");
		goodsName = getIntent().getExtras().getString("goodsName");
		goodsAttr = getIntent().getExtras().getString("goodsAttr");
		giftgoodsId = getIntent().getExtras().getString("giftgoodsId");
		money = getIntent().getExtras().getFloat("money");

		rl_back.setOnClickListener(this);

		// 数据后台加载
		new Thread(new Runnable() {

			@Override
			public void run() {
				giftTogetherInfo = GetData
						.getGiftTogetherInfo(MyConfig.URL_JSON_GIFT_TOGETHER_DETAIL
								+ oid + "&page=" + page);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}).start();

	}

	private void initView() {
		if (!goodsThums.equals("")) {
			Glide.with(this).load(goodsThums).override(250, 250).into(iv_icon);
		}
		tv_name.setText(goodsName);
		tv_attr.setText(goodsAttr);
		if (giftTogetherInfo.getNeedPay() == 0) {
			tv_money.setText("￥" + money);
			tv_moneyNotEnough.setText("￥" + money);
		} else {
			tv_money.setText("￥" + giftTogetherInfo.getNeedPay());
			tv_moneyNotEnough.setText("￥"
					+ giftTogetherInfo.getMoneyNotEnough());
		}

		tv_persent
				.setText((int) ((giftTogetherInfo.getNeedPay() - giftTogetherInfo
						.getMoneyNotEnough()) / giftTogetherInfo.getNeedPay() * 100)
						+ "%" + "");
		progressBar
				.setProgress((int) ((giftTogetherInfo.getNeedPay() - giftTogetherInfo
						.getMoneyNotEnough()) / giftTogetherInfo.getNeedPay() * 100));

		listView.setOnScrollListener(new OnScrollListener() {
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
						private GiftTogetherInfo giftTogetherInfo2;

						public void run() {
							giftTogetherInfo2 = GetData
									.getGiftTogetherInfo(MyConfig.URL_JSON_GIFT_TOGETHER_DETAIL
											+ oid + "&page=" + page);
							if (giftTogetherInfo2 != null
									&& giftTogetherInfo2.getList() != null
									&& giftTogetherInfo2.getList().size() > 0) {
								for (int i = 0; i < giftTogetherInfo2.getList()
										.size(); i++) {
									giftTogetherInfo.getList().add(
											giftTogetherInfo2.getList().get(i));
								}
								if (!isDestroyed()) {
									handlerRefresh.sendEmptyMessage(0);
								}
							}
						};
					}.start();
				}
			}
		});
	}

	private void initListView() {
		adapter = new GiftTogetherDetailAdapter(this,
				giftTogetherInfo.getList());
		listView.setAdapter(adapter);
		MyUtils.setListViewHeight(listView, 20);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		}
	}

}
