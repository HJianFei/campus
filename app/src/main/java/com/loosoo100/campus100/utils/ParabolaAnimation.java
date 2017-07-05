package com.loosoo100.campus100.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loosoo100.campus100.R;

/**
 * 购物车抛物线动画
 * 
 * @author yang
 * 
 */
public class ParabolaAnimation {

	private Activity context;
	private ViewGroup anim_mask_layout = null;// 动画层

	// private ImageView addView;

	public ParabolaAnimation(Context context) {
		this.context = (Activity) context;
		anim_mask_layout = createAnimLayout();

	}

	public void setAnim(int[] start_location, View endView) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
		final ImageView addView = new ImageView(context);
		addView.setImageDrawable(context.getResources().getDrawable(
				R.drawable.shape_circle_red_small));// 设置ball的图片
//		addView.setLayoutParams(params);

		anim_mask_layout.addView(addView);// 把动画小球添加到动画层
		// 将组件添加到我们的动画层上
		final View view = addViewToAnimLayout(anim_mask_layout, addView,
				start_location);
		int[] end_location = new int[2];
		endView.getLocationInWindow(end_location);
		// 计算位移
		int endX = end_location[0] - start_location[0];
		int endY = end_location[1] - start_location[1];

		Animation mTranslateAnimation = new TranslateAnimation(0, endX, 0, endY);// 移动
		mTranslateAnimation.setFillAfter(false);
		mTranslateAnimation.setDuration(1000);

		view.startAnimation(mTranslateAnimation);

		// 动画监听事件
		mTranslateAnimation.setAnimationListener(new AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				// addView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				// addView.setVisibility(View.GONE);
				if (!addView.isActivated()) {
					anim_mask_layout.removeAllViews();
				}
			}
		});

	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	private View addViewToAnimLayout(ViewGroup parent, View view, int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}
}
