package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyAnimation;

/**
 * 
 * @author yang 社团入驻选择activity
 * 暂时废弃
 */
public class CommunityChoiceActivity extends Activity implements
		OnClickListener, OnTouchListener {

	@ViewInject(R.id.rl_black)
	private RelativeLayout rl_black;
	@ViewInject(R.id.btn_create)
	private Button btn_create; // 创建社团
	@ViewInject(R.id.btn_join)
	private Button btn_join; // 加入社团
	private Intent intent = new Intent();
	private ScaleAnimation scaleAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_choice);
		ViewUtils.inject(this);

		scaleAnimation = MyAnimation.getScaleAnimation();

		initView();

	}

	private void initView() {
		rl_black.setOnClickListener(this);
		btn_create.setOnClickListener(this);
		btn_create.setOnTouchListener(this);
		btn_join.setOnClickListener(this);
		btn_join.setOnTouchListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 暗部分
		case R.id.rl_black:
			this.finish();
			break;

		// 创建社团
		case R.id.btn_create:
			intent.setClass(this, CommunityCreateActivity.class);
			startActivity(intent);
			this.finish();
			break;

		// 加入社团
		case R.id.btn_join:
			intent.setClass(this, CommunityJoinActivity.class);
			startActivity(intent);
			this.finish();
			break;

		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 添加点击效果
			switch (v.getId()) {
			case R.id.btn_create:
				btn_create.startAnimation(scaleAnimation);
				break;

			case R.id.btn_join:
				btn_join.startAnimation(scaleAnimation);
				break;
			}
		}
		return false;
	}
}
