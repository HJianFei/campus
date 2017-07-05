package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommCitySearchAdapter;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyLocationUtil;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 社团城市定位
 */
public class CommCitySearchActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.tv_address)
	private TextView tv_address;
	@ViewInject(R.id.gridview)
	private GridView gridview;

	private List<CampusInfo> list;
	private CommCitySearchAdapter adapter;

	private int page = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
				gridview.setVisibility(View.VISIBLE);
			} else {
				gridview.setVisibility(View.GONE);
			}
			page++;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_comm_search);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		tv_address.setText(getIntent().getExtras().getString("city"));

		initView();

		new Thread() {
			@Override
			public void run() {
				list = GetData.getCampusInfos(MyConfig.URL_JSON_SCHOOL_DISTANCE
						+ MyLocationUtil.latitude + "&lng="
						+ MyLocationUtil.longitude);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	private void initListView() {
		adapter = new CommCitySearchAdapter(this, list);
		gridview.setAdapter(adapter);

	}

	private void initView() {
		rl_back.setOnClickListener(this);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("city", list.get(position).getCampusName());
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回按钮
		case R.id.rl_back:
			finish();
			break;

		}
	}
}
