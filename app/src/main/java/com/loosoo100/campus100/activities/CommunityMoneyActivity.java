package com.loosoo100.campus100.activities;

import java.util.ArrayList;
import java.util.List;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommunityMoneyAdapter;
import com.loosoo100.campus100.beans.CommunityMoney;
import com.loosoo100.campus100.beans.CommunityMoneyInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.splinechart.SplineChartView;

/**
 * 
 * @author yang 社团资金activity
 */
public class CommunityMoneyActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private View rl_back;

	@ViewInject(R.id.lv_money)
	private ListView lv_money;
	@ViewInject(R.id.tv_money)
	private TextView tv_money; // 资金
	@ViewInject(R.id.btn_cashout)
	private Button btn_cashout; // 转出
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画
	@ViewInject(R.id.spline)
	private SplineChartView spline; // 加载动画

	private CommunityMoneyAdapter adapter;
	private List<CommunityMoneyInfo> list;

	private String commID = "";
	private String sid = "";
	private boolean flag = false;

	private String money = "";

	private int page = 1;
	private boolean isLoading = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_money.setVisibility(View.VISIBLE);
			} else {
				lv_money.setVisibility(View.GONE);
			}
			if (MyCommunityActivity.communityBasicInfos != null
					&& MyCommunityActivity.communityBasicInfos.getMoneyList()
							.size() > 0) {
				initSpline();
			}
			if (!money.equals("")) {
				tv_money.setText("￥" + money);
				MyCommunityActivity.tv_money.setText("￥" + money);
			}
			page++;
			isLoading = false;
			progress.setVisibility(View.GONE);
		}
	};

	private Handler handlerUpdate = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				lv_money.setVisibility(View.VISIBLE);
			} else {
				lv_money.setVisibility(View.GONE);
			}
			if (!money.equals("")) {
				tv_money.setText("￥" + money);
				MyCommunityActivity.tv_money.setText("￥" + money);
			}
			isLoading = false;
			page = 2;
		}
	};

	private Handler handlerRefresh = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				// 设置listview的高度
				MyUtils.setListViewHeight(lv_money, 50);
			}
			isLoading = false;
			page++;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_money);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		commID = getIntent().getExtras().getString("commID");
		sid = getIntent().getExtras().getString("sid");
		flag = getIntent().getExtras().getBoolean("flag");

		progress.setVisibility(View.VISIBLE);

		rl_back.setOnClickListener(this);
		btn_cashout.setOnClickListener(this);

		if (flag) {
			btn_cashout.setVisibility(View.VISIBLE);
		} else {
			btn_cashout.setVisibility(View.GONE);
		}
		;
		new Thread() {
			public void run() {
				list = GetData
						.getCommunityMoneyInfos(MyConfig.URL_JSON_MYCOMMUNITY_MONEY
								+ commID);
				money = GetData
						.getCommunityMoney(MyConfig.URL_JSON_MYCOMMUNITY_MONEY
								+ commID);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		adapter = new CommunityMoneyAdapter(this, list);
		lv_money.setAdapter(adapter);
		// 设置listview的高度
		MyUtils.setListViewHeight(lv_money, 50);

		lv_money.setOnScrollListener(new OnScrollListener() {

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
							List<CommunityMoneyInfo> list2 = GetData
									.getCommunityMoneyInfos(MyConfig.URL_JSON_MYCOMMUNITY_MONEY
											+ commID + "&p=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
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

	/**
	 * 初始化折线图
	 */
	private void initSpline() {
		float maxMoney = 0;
		List<CommunityMoney> moneyList = MyCommunityActivity.communityBasicInfos
				.getMoneyList();
		// 底栏标签
		List<String> labels = new ArrayList<String>();
		labels.add("");

		List<SplineData> chartData = new ArrayList<SplineData>();
		// 数据集
		List<PointD> linePoint = new ArrayList<PointD>();

		for (int i = 0; i < moneyList.size(); i++) {
			labels.add(moneyList.get(i).getTime());
			linePoint.add(new PointD(i + 1, Double.valueOf(moneyList.get(i)
					.getMoney())));
			if (Double.valueOf(moneyList.get(i).getMoney()) > maxMoney) {
				maxMoney = Float.valueOf(moneyList.get(i).getMoney());
			}
		}
		for (int i = 0; i < 7 - moneyList.size(); i++) {
			labels.add("");
		}
		// labels.add("");

		SplineData dataSeries = new SplineData("", linePoint, Color.rgb(253,
				60, 73));
		// 把线弄细点
		// dataSeries.getLinePaint().setStrokeWidth(2);
		// 是否显示标签内容
		// dataSeries.setLabelVisible(true);
		// 设置折线点的半径大小
		dataSeries.setDotRadius(8);
		dataSeries.setDotStyle(XEnum.DotStyle.DOT);
		dataSeries.getDotLabelPaint().setColor(Color.RED);

		// 设置round风格的标签
		// dataSeries.getLabelOptions().showBackground();
		dataSeries.getLabelOptions().getBox().getBackgroundPaint()
				.setColor(Color.GREEN);
		dataSeries.getLabelOptions().getBox().setRoundRadius(8);
		dataSeries.getLabelOptions().setLabelBoxStyle(
				XEnum.LabelBoxStyle.CAPROUNDRECT);
		chartData.add(dataSeries);

		spline.setData(maxMoney, 7, labels, chartData);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		// 转出
		case R.id.btn_cashout:
			Intent intent = new Intent(this, CommunityCashOutActivity.class);
			intent.putExtra("cid", commID);
			intent.putExtra("money", Float.valueOf(money));
			startActivityForResult(intent, 1);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			new Thread() {
				public void run() {
					list = GetData
							.getCommunityMoneyInfos(MyConfig.URL_JSON_MYCOMMUNITY_MONEY
									+ commID);
					money = GetData
							.getCommunityMoney(MyConfig.URL_JSON_MYCOMMUNITY_MONEY
									+ commID);
					if (!isDestroyed()) {
						handlerUpdate.sendEmptyMessage(0);
					}
				};
			}.start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
