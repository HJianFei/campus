package com.loosoo100.campus100.zzboss.activities;

import java.util.HashMap;
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
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.LetterIndexView;
import com.loosoo100.campus100.zzboss.adapter.LetterIndexAdapter;
import com.loosoo100.campus100.zzboss.beans.BossCityInfo;

/**
 * 
 * @author yang
 */
public class CitySearchActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty;
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.liv_index)
	private LetterIndexView liv_index;
	@ViewInject(R.id.listview)
	private ListView listview;
	@ViewInject(R.id.progress)
	private RelativeLayout progress;

	private List<BossCityInfo> list;
	HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
	private LetterIndexAdapter letterIndexAdapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
			}
			progress.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_cityorschool_search);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_back.setOnClickListener(this);

		progress.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				list = GetData.getAllCityInfos(MyConfig.URL_JSON_ALL_CITY);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initListView() {
		for (int i = 0; i < list.size(); i++) {
			if (!hashMap.containsKey(String
					.valueOf(list.get(i).getSortLetter()))) {
				hashMap.put(String.valueOf(list.get(i).getSortLetter()), i);
			}
		}
		letterIndexAdapter = new LetterIndexAdapter(list, this, hashMap);
		listview.setAdapter(letterIndexAdapter);
		liv_index.setListView(listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("city", list.get(position).getCity());
				intent.putExtra("cityID", list.get(position).getCityId());
				setResult(RESULT_OK, intent);
				CitySearchActivity.this.finish();
			}
		});
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
