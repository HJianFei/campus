package com.loosoo100.campus100.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.loosoo100.campus100.anyevent.MEventAddress;
import com.loosoo100.campus100.anyevent.MEventAddressFinish;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 收货地址选择地区3
 * 
 * @author yang
 */
public class AddressChoiceActivity03 extends Activity implements
OnClickListener{
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.listView)
	private ListView listView;
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;

	private List<CampusInfo> cityList = new ArrayList<CampusInfo>();
	private AddressChoiceAdapter adapter;
	private String province = "";
	private String provinceId = "";
	private String city = "";
	private String cityID = "";

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
		setContentView(R.layout.activity_address_choice03);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		province = getIntent().getExtras().getString("province");
		provinceId = getIntent().getExtras().getString("provinceId");
		city = getIntent().getExtras().getString("city");
		cityID = getIntent().getExtras().getString("cityID");

		rl_back.setOnClickListener(this);
		
		new Thread() {
			public void run() {
				cityList = GetData
						.getCityInfos(MyConfig.URL_JSON_CITY + cityID);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	private void initListView() {
		adapter = new AddressChoiceAdapter(this, cityList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventBus.getDefault().post(new MEventAddressFinish(true));
				EventBus.getDefault().post(
						new MEventAddress(cityList.get(position)
								.getCampusName(), city, province, cityList.get(
								position).getCampusID(), cityID, provinceId));
				AddressChoiceActivity03.this.finish();
			}
		});
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		}
	}
}
