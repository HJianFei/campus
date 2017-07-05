package com.loosoo100.campus100.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.viewpager.VerticalPager;

/**
 * 恋爱保险activity
 * 
 * @author yang
 */
public class LoveSecureActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.rl_question)
	private RelativeLayout rl_question;
//	@ViewInject(R.id.tv01)
//	private TextView tv01;
//	@ViewInject(R.id.tv02)
//	private TextView tv02;
	@ViewInject(R.id.btn_in)
	private Button btn_in;
	@ViewInject(R.id.web)
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_love_secure);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_back.setOnClickListener(this);
		rl_question.setOnClickListener(this);
		btn_in.setOnClickListener(this);
//		AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 0);
//		alphaAnimation.setDuration(1000);
//		alphaAnimation.setRepeatCount(Integer.MAX_VALUE);
//		alphaAnimation.setFillAfter(false);
//		alphaAnimation.setRepeatMode(Animation.REVERSE);

//		AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0.2f);
//		alphaAnimation2.setDuration(1000);
//		alphaAnimation2.setRepeatCount(Integer.MAX_VALUE);
//		alphaAnimation2.setFillAfter(false);
//		alphaAnimation2.setRepeatMode(Animation.REVERSE);

//		tv01.startAnimation(alphaAnimation2);
//		tv02.startAnimation(alphaAnimation);

		loadWeb();

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void loadWeb() {
		String url = "http://f28a37dc.u.h5mc.com/campaigns/57a3103e3bd2082b4c0003f2/20161010053607/57e387cd347a196b022f5d58/index.html";
		WebSettings settings = web.getSettings();
		settings.setJavaScriptEnabled(true);
		web.loadUrl(url);
		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_question:
			Intent intent = new Intent(this, LoveSecureQuestionActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_in:
			Intent intent2 = new Intent(this, GiftGoodsDetailActivity.class);
			intent2.putExtra("giftgoodsId", "230");
			startActivity(intent2);
			break;
		}
	}

//	@Override
//	protected void onDestroy() {
//		tv01.clearAnimation();
//		tv02.clearAnimation();
//		super.onDestroy();
//	}
}
