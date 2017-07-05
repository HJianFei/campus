package com.loosoo100.campus100.activities;

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
import com.loosoo100.campus100.adapters.MessageAdapter;
import com.loosoo100.campus100.beans.MessageInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 消息列表activity
 */
public class MessageActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private View rl_back; // 返回按钮

	@ViewInject(R.id.lv_message)
	private ListView lv_message; // 消息详情列表
	@ViewInject(R.id.progress)
	private RelativeLayout progress;

	private List<MessageInfo> messageInfos;
	private String uid="";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (messageInfos!=null&&messageInfos.size()>0){
				initListView();
			}
			progress.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		uid=getSharedPreferences(UserInfoDB.USERTABLE,MODE_PRIVATE).getString(UserInfoDB.USERID,"");

		progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);

		new Thread() {
			public void run() {
				messageInfos = GetData.getMessageListInfos(MyConfig.URL_JSON_MESSAGE_LIST+uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	/**
	 * 初始化列表
	 */
	private void initListView() {
		final MessageAdapter adapter = new MessageAdapter(this, messageInfos);
		lv_message.setAdapter(adapter);
		lv_message.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MessageActivity.this,
						MessageDetailActivity.class);
				intent.putExtra("title", messageInfos.get(position).getTitle());
				intent.putExtra("type", messageInfos.get(position).getType());
				startActivity(intent);
				messageInfos.get(position).setStatus("1");
				adapter.notifyDataSetChanged();
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
