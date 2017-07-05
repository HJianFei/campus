package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 借款用途activity
 */
public class MyCreditPurposeActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;

	@ViewInject(R.id.ll_01)
	private LinearLayout ll_01; // 学生生活

	@ViewInject(R.id.ll_02)
	private LinearLayout ll_02; // 学习必需品

	@ViewInject(R.id.iv_01)
	private ImageView iv_01;

	@ViewInject(R.id.iv_02)
	private ImageView iv_02;

	@ViewInject(R.id.btn_ok)
	private Button btn_ok;

	private String purpose = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycredit_apply_purpose);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		purpose = getIntent().getExtras().getString("purpose");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		ll_01.setOnClickListener(this);
		ll_02.setOnClickListener(this);
		btn_ok.setOnClickListener(this);

		if (purpose.equals("学习必需品")) {
			iv_02.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select));
		} else {
			purpose = "学生生活";
			iv_01.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;
		case R.id.ll_01:
			resetImage();
			purpose = "学生生活";
			iv_01.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select));
			break;
		case R.id.ll_02:
			resetImage();
			purpose = "学习必需品";
			iv_02.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_select));
			break;
		case R.id.btn_ok:
			Intent intent = new Intent();
			intent.putExtra("purpose", purpose);
			setResult(RESULT_OK, intent);
			finish();
			break;

		}
	}

	/**
	 * 还原图标背景
	 */
	private void resetImage() {
		iv_01.setImageDrawable(getResources().getDrawable(
				R.drawable.icon_select_not));
		iv_02.setImageDrawable(getResources().getDrawable(
				R.drawable.icon_select_not));
	}

}
