package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.BorrowHistoryAdapter;
import com.loosoo100.campus100.beans.BorrowHistoryInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.List;

/**
 * 
 * @author yang 借款记录activity
 */
public class BorrowHistoryActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.lv_history)
	private ListView lv_history; // 列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画

	private BorrowHistoryAdapter adapter;
	private List<BorrowHistoryInfo> list;

	private String uid="";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (list!=null&&list.size()>0) {
				initListView();
				lv_history.setVisibility(View.VISIBLE);
			}else {
				lv_history.setVisibility(View.GONE);
			}
			progress.setVisibility(View.GONE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_history);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		progress.setVisibility(View.VISIBLE);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		rl_back.setOnClickListener(this);

		new Thread() {
			public void run() {
				list = GetData.getBorrowHistoryInfos(MyConfig.URL_JSON_CREDIT
						+ uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			}
		}.start();
	}

	private void initListView() {
		adapter = new BorrowHistoryAdapter(this, list);
		lv_history.setAdapter(adapter);
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