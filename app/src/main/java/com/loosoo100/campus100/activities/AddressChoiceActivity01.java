package com.loosoo100.campus100.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.AddressChoiceAdapter;
import com.loosoo100.campus100.anyevent.MEventAddressFinish;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 收货地址选择地区1
 * 
 * @author yang
 */
public class AddressChoiceActivity01 extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.listView)
	private ListView listView;

	private List<CampusInfo> cityList = new ArrayList<CampusInfo>();
	private AddressChoiceAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (cityList != null && cityList.size() > 0) {
				initListView();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_choice01);
		ViewUtils.inject(this);
		EventBus.getDefault().register(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_back.setOnClickListener(this);

		new Thread() {
			public void run() {
				cityList = GetData.getCityInfos(MyConfig.URL_JSON_PROVINCE);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	private void initListView() {
		adapter = new AddressChoiceAdapter(this, cityList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AddressChoiceActivity01.this,
						AddressChoiceActivity02.class);
				intent.putExtra("province", cityList.get(position)
						.getCampusName());
				intent.putExtra("provinceId", cityList.get(position)
						.getCampusID());
				startActivity(intent);
			}
		});
	};

	public void onEventMainThread(MEventAddressFinish event) {
		if (event.getFinish()) {
			this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
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
