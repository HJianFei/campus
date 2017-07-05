package com.loosoo100.campus100.zzboss.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.MyViewPagerAdapter;
import com.loosoo100.campus100.anyevent.MEventCollectActiChange;
import com.loosoo100.campus100.anyevent.MEventCollectNeedChange;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.controller.BaseController;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.controllers.BossCollectActiController;
import com.loosoo100.campus100.zzboss.controllers.BossCollectNeedController;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 企业版我的收藏
 */
public class BossMyCollectActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.btn_acti)
	private Button btn_acti; // 活动
	@ViewInject(R.id.btn_need)
	private Button btn_need; // 需求
	@ViewInject(R.id.iv_whiteline)
	private ImageView iv_whiteline; // 白线条
	@ViewInject(R.id.vp)
	private ViewPager vp;

	private List<BaseController> controllers = new ArrayList<BaseController>();
	private BossCollectActiController actiController;
	private BossCollectNeedController needController;

	private float x = 0; // 记录当前白线条所在位置
	private int width; // 屏幕宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boss_mycollect);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		
		EventBus.getDefault().register(this);

		rl_back.setOnClickListener(this);
		btn_acti.setOnClickListener(this);
		btn_need.setOnClickListener(this);

		WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = systemService.getDefaultDisplay().getWidth() / 4;
		// 设置红条的宽度和高度
		LayoutParams params = new LayoutParams(width, 5);
		iv_whiteline.setLayoutParams(params);
		iv_whiteline.startAnimation(translateAnimation2(1));
		x = width;
		initViewPager();

	}

	private void initViewPager() {
		// 初始化controllers
		actiController = new BossCollectActiController(this);
		needController = new BossCollectNeedController(this);
		controllers.add(actiController);
		controllers.add(needController);
		actiController.initData();
		vp.setAdapter(new MyViewPagerAdapter(controllers));
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					btn_acti.setTextSize(16);
					btn_need.setTextSize(14);
					iv_whiteline.startAnimation(translateAnimation(1));
					x = width;
					actiController.initData();
				} else {
					btn_need.setTextSize(16);
					btn_acti.setTextSize(14);
					iv_whiteline.startAnimation(translateAnimation(2));
					x = 2 * width;
					needController.initData();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_acti:
			if (x == width) {
				return;
			}
			vp.setCurrentItem(0);
			// btn_message.setTextSize(16);
			// btn_comm.setTextSize(14);
			// 红色线条平移动画
			// iv_whiteline.startAnimation(translateAnimation(1));
			// 记录当前红色线条在什么位置
			// x = width;
			break;

		case R.id.btn_need:
			if (x == 2 * width) {
				return;
			}
			vp.setCurrentItem(1);
			// btn_comm.setTextSize(16);
			// btn_message.setTextSize(14);
			// 红色线条平移动画
			// iv_whiteline.startAnimation(translateAnimation(2));
			// 记录当前红色线条在什么位置
			// x = 2 * width;
			break;
		}
	}

	/**
	 * 设置平移动画
	 * 
	 * @return
	 */
	private TranslateAnimation translateAnimation(int index) {
		TranslateAnimation translateAnimation = new TranslateAnimation(x, index
				* width, 0, 0);
		translateAnimation.setDuration(300);
		translateAnimation.setFillBefore(true);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

	/**
	 * 设置平移动画
	 * 
	 * @return
	 */
	private TranslateAnimation translateAnimation2(int index) {
		TranslateAnimation translateAnimation = new TranslateAnimation(x, index
				* width, 0, 0);
		translateAnimation.setDuration(0);
		translateAnimation.setFillBefore(true);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

	/**
	 * 活动支付更新活动数据
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCommActiPay event) {
		if (event.isChange()) {
			actiController.updateData();
		}
	}

	public void onEventMainThread(MEventCollectActiChange event) {
		if (event.isChange()) {
			actiController.updateData();
		}
	}
	
	public void onEventMainThread(MEventCollectNeedChange event) {
		if (event.isChange()) {
			needController.updateData();
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
