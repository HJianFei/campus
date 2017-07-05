package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 社团架构添加编辑activity
 */
public class CommunityDepAddActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_save)
	private RelativeLayout rl_save; // 保存
	@ViewInject(R.id.et_dep)
	private EditText et_dep; // 内容
	@ViewInject(R.id.tv_wordCount)
	private TextView tv_wordCount; // 备注字数提示
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_dep_add);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_save.setOnClickListener(this);
		/**
		 * 监听备注输入字数
		 */
		et_dep.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_wordCount.setText(s.length() + "/4");
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
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_save:
			if (et_dep.getText().toString().trim().equals("")) {
				ToastUtil.showToast(this,"输入内容不能为空");
			}else {
				ToastUtil.showToast(this,"保存成功");
				Intent intent = new Intent();
				intent.putExtra("dep", et_dep.getText().toString());
				setResult(RESULT_OK, intent);
				this.finish();
			}
			break;
		}
	}
}
