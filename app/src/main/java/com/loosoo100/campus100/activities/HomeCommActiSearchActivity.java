package com.loosoo100.campus100.activities;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommActiSearchAdapter;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.activities.CitySearchActivity;
import com.loosoo100.campus100.zzboss.beans.BossCityInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author yang 首页活动搜索activity
 */
public class HomeCommActiSearchActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.et_search)
	private EditText et_search; // 搜索内容
	@ViewInject(R.id.rl_search_back)
	private RelativeLayout rl_search_back; // 返回
	@ViewInject(R.id.rl_search)
	private RelativeLayout rl_search; // 搜索按钮
	@ViewInject(R.id.gridview)
	private GridView gridview;
	@ViewInject(R.id.btn_delete)
	private Button btn_delete; // 输入文本删除
	@ViewInject(R.id.ll_city)
	private LinearLayout ll_city; // 选择城市
	@ViewInject(R.id.tv_city)
	private TextView tv_city; // 选择城市

	private List<BossCityInfo> list;
	private CommActiSearchAdapter adapter;

	private String content = "";
	private String city = "";
	private String cityId = "0";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list != null && list.size() > 0) {
				initListView();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_comm_acti_search);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		initView();

		new Thread() {
			public void run() {
				list = GetData.getHotCityInfos(MyConfig.URL_JSON_HOT_CITY);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	private void initListView() {
		adapter = new CommActiSearchAdapter(this, list);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tv_city.setText(list.get(position).getCity());
				cityId = list.get(position).getCityId();
				adapter.mPosition = position;
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initView() {
		rl_search_back.setOnClickListener(this);
		ll_city.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		rl_search.setOnClickListener(this);

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (et_search.getText().toString().trim().equals("")) {
					btn_delete.setVisibility(View.GONE);
				} else {
					btn_delete.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回按钮
		case R.id.rl_search_back:
			finish();
			break;

		case R.id.ll_city:
			Intent intent = new Intent(this, CitySearchActivity.class);
			startActivityForResult(intent, 1);
			break;

		case R.id.btn_delete:
			et_search.setText("");
			break;

		// 搜索按钮
		case R.id.rl_search:
			content = et_search.getText().toString().trim();
			Intent intent2 = new Intent(this,CommActiListActivity.class);
			intent2.putExtra("cityId", cityId);
			intent2.putExtra("content", content);
			startActivity(intent2);
			finish();
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			city = data.getExtras().getString("city");
			cityId = data.getExtras().getString("cityID");
			tv_city.setText(city);
			boolean flag = false;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getCityId().equals(cityId)) {
					flag = true;
					adapter.mPosition = i;
					adapter.notifyDataSetChanged();
					break;
				}
			}
			if (!flag) {
				adapter.mPosition = -1;
				adapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
