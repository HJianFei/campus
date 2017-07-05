package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 企业入驻协议activity
 */
public class BossJoinArgeementActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.webView)
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_argeement_boss);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_back.setOnClickListener(this);
		webView.loadUrl("file:///android_asset/boss_join.html");
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
