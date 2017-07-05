package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.ZoomImageView;

/**
 * 
 * @author yang 图片预览activity
 */
public class PicturePreviewActivity extends Activity {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.ll_root)
	private LinearLayout ll_root; // 用来添加图片
	@ViewInject(R.id.zoomIv)
	private ZoomImageView zoomIv;

	private String userID;

	private String picUrl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_preview);
		overridePendingTransition(R.anim.in_scale_up2, R.anim.out_scale_down);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		picUrl = getIntent().getExtras().getString("picUrl");

		userID = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		if (picUrl.equals("person")) {
			zoomIv.setImageBitmap(BitmapFactory.decodeFile(MyConfig.IMAGGE_URI
					+ userID + ".png"));
		} else {
			Glide.with(this).load(picUrl).into(zoomIv);
		}

		zoomIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PicturePreviewActivity.this.finish();
			}
		});

		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(500);
		ll_root.startAnimation(alphaAnimation);
	}

}
