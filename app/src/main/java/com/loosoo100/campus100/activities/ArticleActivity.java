package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 礼物说文章详情activity
 */
public class ArticleActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回按钮
	@ViewInject(R.id.rl_more)
	private RelativeLayout rl_more; // 更多按钮
	@ViewInject(R.id.ll_root_popup)
	private LinearLayout root_popup; // 弹出框ll布局
	@ViewInject(R.id.rl_root_popup)
	private RelativeLayout rl_root_popup; // 弹出框rl布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		//更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);

		initView();
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_more.setOnClickListener(this);
		rl_root_popup.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回按钮
		case R.id.rl_back:
			this.finish();
			break;
		// 更多按钮
		case R.id.rl_more:
			rl_root_popup.setVisibility(View.VISIBLE);
			break;
		// 弹出框更多按钮
		case R.id.rl_root_popup:
			rl_root_popup.setVisibility(View.GONE);
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (rl_root_popup.getVisibility() == View.VISIBLE) {
			rl_root_popup.setVisibility(View.GONE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
