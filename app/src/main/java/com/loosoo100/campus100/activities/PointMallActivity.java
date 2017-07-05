package com.loosoo100.campus100.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CampusPublishGridViewAdapter;
import com.loosoo100.campus100.imagepicker.AndroidImagePicker;
import com.loosoo100.campus100.imagepicker.ui.activity.ImagesGridActivity;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 积分商城activity
 */
public class PointMallActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;

	@ViewInject(R.id.btn_ok)
	private Button btn_ok;
	@ViewInject(R.id.noScrollgridview)
	private GridView noScrollgridview;

	private ArrayList<String> list = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point_mall);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		rl_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_ok:
			Intent intent = new Intent(this, ImagesGridActivity.class);
			intent.putExtra("limit", 9 - list.size());
			startActivityForResult(intent, 1);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			ArrayList<String> list2 = data.getExtras().getStringArrayList(
					"picList");
			for (int i = 0; i < list2.size(); i++) {
				list.add(list2.get(i));
			}
			noScrollgridview.setAdapter(new CampusPublishGridViewAdapter(this,
					list));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}