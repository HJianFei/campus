package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.view.ZoomImageView;

/**
 * 
 * @author yang 图片预览activity
 */
public class ImagePreviewActivity extends Activity  {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.ll_root)
	private LinearLayout ll_root;
	@ViewInject(R.id.zoomIv)
	private ZoomImageView zoomIv;

	private String picUrl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_preview);
		ViewUtils.inject(this);

		picUrl = getIntent().getExtras().getString("picUrl");

		Glide.with(this).load(picUrl).into(zoomIv);

//		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//		alphaAnimation.setDuration(500);
//		ll_root.startAnimation(alphaAnimation);
	}

}
