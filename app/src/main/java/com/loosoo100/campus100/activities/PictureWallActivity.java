package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventPicChange;
import com.loosoo100.campus100.card.PictureWallInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 照片墙activity
 */
public class PictureWallActivity extends Activity implements OnClickListener,
		OnTouchListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;

	@ViewInject(R.id.rl_manager)
	private RelativeLayout rl_manager;
	@ViewInject(R.id.rl_top)
	private RelativeLayout rl_top;
	@ViewInject(R.id.tv_school)
	private TextView tv_school;
	// @ViewInject(R.id.rl_click)
	// private RelativeLayout rl_click;

	@ViewInject(R.id.ib_add)
	private ImageButton ib_add; // 添加按钮
	@ViewInject(R.id.iv_finger)
	private ImageView iv_finger; // 手指引导
	@ViewInject(R.id.progress)
	private RelativeLayout progress; // 加载动画

	@ViewInject(R.id.iv_picture)
	private ImageView iv_picture; // 背景图片
	@ViewInject(R.id.rl_img01)
	private RelativeLayout rl_img01;
	@ViewInject(R.id.rl_img02)
	private RelativeLayout rl_img02;
	@ViewInject(R.id.rl_img03)
	private RelativeLayout rl_img03;
	@ViewInject(R.id.rl_img04)
	private RelativeLayout rl_img04;
	@ViewInject(R.id.rl_img05)
	private RelativeLayout rl_img05;
	@ViewInject(R.id.rl_img06)
	private RelativeLayout rl_img06;
	@ViewInject(R.id.rl_img07)
	private RelativeLayout rl_img07;
	@ViewInject(R.id.rl_img08)
	private RelativeLayout rl_img08;
	@ViewInject(R.id.rl_img09)
	private RelativeLayout rl_img09;
	@ViewInject(R.id.rl_img10)
	private RelativeLayout rl_img10;
	@ViewInject(R.id.rl_img11)
	private RelativeLayout rl_img11;
	@ViewInject(R.id.rl_img12)
	private RelativeLayout rl_img12;
	@ViewInject(R.id.rl_img13)
	private RelativeLayout rl_img13;
	@ViewInject(R.id.rl_img14)
	private RelativeLayout rl_img14;
	@ViewInject(R.id.rl_img15)
	private RelativeLayout rl_img15;
	@ViewInject(R.id.iv_picture01)
	private ImageView iv_picture01; // 小图1
	@ViewInject(R.id.iv_picture02)
	private ImageView iv_picture02; // 小图2
	@ViewInject(R.id.iv_picture03)
	private ImageView iv_picture03; // 小图3
	@ViewInject(R.id.iv_picture04)
	private ImageView iv_picture04; // 小图4
	@ViewInject(R.id.iv_picture05)
	private ImageView iv_picture05; // 小图5
	@ViewInject(R.id.iv_picture06)
	private ImageView iv_picture06; // 小图6
	@ViewInject(R.id.iv_picture07)
	private ImageView iv_picture07; // 小图7
	@ViewInject(R.id.iv_picture08)
	private ImageView iv_picture08; // 小图8
	@ViewInject(R.id.iv_picture09)
	private ImageView iv_picture09; // 小图9
	@ViewInject(R.id.iv_picture10)
	private ImageView iv_picture10; // 小图10
	@ViewInject(R.id.iv_picture11)
	private ImageView iv_picture11; // 小图11
	@ViewInject(R.id.iv_picture12)
	private ImageView iv_picture12; // 小图12
	@ViewInject(R.id.iv_picture13)
	private ImageView iv_picture13; // 小图13
	@ViewInject(R.id.iv_picture14)
	private ImageView iv_picture14; // 小图14
	@ViewInject(R.id.iv_picture15)
	private ImageView iv_picture15; // 小图15
	@ViewInject(R.id.tv_dream01)
	private TextView tv_dream01; // 小图内容1
	@ViewInject(R.id.tv_dream02)
	private TextView tv_dream02; // 小图内容2
	@ViewInject(R.id.tv_dream03)
	private TextView tv_dream03; // 小图内容3
	@ViewInject(R.id.tv_dream04)
	private TextView tv_dream04; // 小图内容4
	@ViewInject(R.id.tv_dream05)
	private TextView tv_dream05; // 小图内容5
	@ViewInject(R.id.tv_dream06)
	private TextView tv_dream06; // 小图内容6
	@ViewInject(R.id.tv_dream07)
	private TextView tv_dream07; // 小图内容7
	@ViewInject(R.id.tv_dream08)
	private TextView tv_dream08; // 小图内容8
	@ViewInject(R.id.tv_dream09)
	private TextView tv_dream09; // 小图内容9
	@ViewInject(R.id.tv_dream10)
	private TextView tv_dream10; // 小图内容10
	@ViewInject(R.id.tv_dream11)
	private TextView tv_dream11; // 小图内容11
	@ViewInject(R.id.tv_dream12)
	private TextView tv_dream12; // 小图内容12
	@ViewInject(R.id.tv_dream13)
	private TextView tv_dream13; // 小图内容13
	@ViewInject(R.id.tv_dream14)
	private TextView tv_dream14; // 小图内容14
	@ViewInject(R.id.tv_dream15)
	private TextView tv_dream15; // 小图内容15

	private Intent intent = new Intent();
	private String school = "";
	private String sid = "";
	private List<PictureWallInfo> pictureWallHomeInfos;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (pictureWallHomeInfos != null) {
				initView();
			}
			progress.setVisibility(View.GONE);
		};
	};
	private ScaleAnimation scaleAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_wall);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

		school = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_PICTURE, "");
		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID_PICTURE, "");
		iv_picture.setImageBitmap(GetData.getBitMap(this,
				R.drawable.picture_wall_bg));
		tv_school.setText(school);
		rl_back.setOnClickListener(this);
		rl_manager.setOnClickListener(this);
		ib_add.setOnClickListener(this);
		rl_top.setOnClickListener(this);
		iv_picture.setOnClickListener(this);
		rl_img01.setOnClickListener(this);
		rl_img02.setOnClickListener(this);
		rl_img03.setOnClickListener(this);
		rl_img04.setOnClickListener(this);
		rl_img05.setOnClickListener(this);
		rl_img06.setOnClickListener(this);
		rl_img07.setOnClickListener(this);
		rl_img08.setOnClickListener(this);
		rl_img09.setOnClickListener(this);
		rl_img10.setOnClickListener(this);
		rl_img11.setOnClickListener(this);
		rl_img12.setOnClickListener(this);
		rl_img13.setOnClickListener(this);
		rl_img14.setOnClickListener(this);
		rl_img15.setOnClickListener(this);

		ib_add.setOnTouchListener(this);

		scaleAnimation = MyAnimation.getScaleAnimation(true);

		progress.setVisibility(View.VISIBLE);
		new Thread() {

			public void run() {
				pictureWallHomeInfos = GetData
						.getPictureWallHomeInfos(MyConfig.URL_JSON_PICTURE_WALL
								+ sid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

	}

	private void initView() {
		if (sid.equals(getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, ""))) {
			ib_add.setVisibility(View.VISIBLE);
		} else {
			ib_add.setVisibility(View.GONE);
		}
		iv_finger.setVisibility(View.VISIBLE);
		iv_finger.startAnimation(scaleAnimation);
		hideView();
		int size = pictureWallHomeInfos.size();
		if (size > 15) {
			size = 15;
		}
		switch (size) {
		case 15:
			Glide.with(this).load(pictureWallHomeInfos.get(14).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture15);
			tv_dream15.setText(pictureWallHomeInfos.get(14).getDream());
			rl_img15.setVisibility(View.VISIBLE);
		case 14:
			Glide.with(this).load(pictureWallHomeInfos.get(13).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture14);
			tv_dream14.setText(pictureWallHomeInfos.get(13).getDream());
			rl_img14.setVisibility(View.VISIBLE);
		case 13:
			Glide.with(this).load(pictureWallHomeInfos.get(12).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture13);
			tv_dream13.setText(pictureWallHomeInfos.get(12).getDream());
			rl_img13.setVisibility(View.VISIBLE);
		case 12:
			Glide.with(this).load(pictureWallHomeInfos.get(11).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture12);
			tv_dream12.setText(pictureWallHomeInfos.get(11).getDream());
			rl_img12.setVisibility(View.VISIBLE);
		case 11:
			Glide.with(this).load(pictureWallHomeInfos.get(10).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture11);
			tv_dream11.setText(pictureWallHomeInfos.get(10).getDream());
			rl_img11.setVisibility(View.VISIBLE);
		case 10:
			Glide.with(this).load(pictureWallHomeInfos.get(9).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture10);
			tv_dream10.setText(pictureWallHomeInfos.get(9).getDream());
			rl_img10.setVisibility(View.VISIBLE);
		case 9:
			Glide.with(this).load(pictureWallHomeInfos.get(8).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture09);
			tv_dream09.setText(pictureWallHomeInfos.get(8).getDream());
			rl_img09.setVisibility(View.VISIBLE);
		case 8:
			Glide.with(this).load(pictureWallHomeInfos.get(7).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture08);
			tv_dream08.setText(pictureWallHomeInfos.get(7).getDream());
			rl_img08.setVisibility(View.VISIBLE);
		case 7:
			Glide.with(this).load(pictureWallHomeInfos.get(6).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture07);
			tv_dream07.setText(pictureWallHomeInfos.get(6).getDream());
			rl_img07.setVisibility(View.VISIBLE);
		case 6:
			Glide.with(this).load(pictureWallHomeInfos.get(5).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture06);
			tv_dream06.setText(pictureWallHomeInfos.get(5).getDream());
			rl_img06.setVisibility(View.VISIBLE);
		case 5:
			Glide.with(this).load(pictureWallHomeInfos.get(4).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture05);
			tv_dream05.setText(pictureWallHomeInfos.get(4).getDream());
			rl_img05.setVisibility(View.VISIBLE);
		case 4:
			Glide.with(this).load(pictureWallHomeInfos.get(3).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture04);
			tv_dream04.setText(pictureWallHomeInfos.get(3).getDream());
			rl_img04.setVisibility(View.VISIBLE);
		case 3:
			Glide.with(this).load(pictureWallHomeInfos.get(2).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture03);
			tv_dream03.setText(pictureWallHomeInfos.get(2).getDream());
			rl_img03.setVisibility(View.VISIBLE);
		case 2:
			Glide.with(this).load(pictureWallHomeInfos.get(1).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture02);
			tv_dream02.setText(pictureWallHomeInfos.get(1).getDream());
			rl_img02.setVisibility(View.VISIBLE);
		case 1:
			Glide.with(this).load(pictureWallHomeInfos.get(0).getPicture())
					.override(200, 230).dontAnimate().into(iv_picture01);
			tv_dream01.setText(pictureWallHomeInfos.get(0).getDream());
			rl_img01.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.rl_top:
			intent.setClass(this, SearchActivity.class);
			startActivityForResult(intent, 1);
			break;

		case R.id.rl_manager:
			intent.setClass(this, PictureManageActivity.class);
			startActivity(intent);
			break;

		case R.id.iv_picture:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("type", "本校照片");
			intent.putExtra("wid", "");
			startActivity(intent);
			break;

		case R.id.ib_add:
			intent.setClass(this, PicturePublishActivity.class);
			startActivity(intent);
			break;

		case R.id.rl_img01:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(0).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img02:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(1).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img03:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(2).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img04:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(3).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img05:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(4).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img06:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(5).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img07:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(6).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img08:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(7).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img09:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(8).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img10:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(9).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img11:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(10).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img12:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(11).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img13:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(12).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img14:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(13).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;

		case R.id.rl_img15:
			iv_finger.setAlpha(0);
			iv_finger.clearAnimation();
			intent.setClass(this, PictureWallDetailActivity.class);
			intent.putExtra("page", 1);
			intent.putExtra("wid", pictureWallHomeInfos.get(14).getId());
			intent.putExtra("type", "本校照片");
			startActivity(intent);
			break;
		}
	}

	private void hideView() {
		rl_img01.setVisibility(View.GONE);
		rl_img02.setVisibility(View.GONE);
		rl_img03.setVisibility(View.GONE);
		rl_img04.setVisibility(View.GONE);
		rl_img05.setVisibility(View.GONE);
		rl_img06.setVisibility(View.GONE);
		rl_img07.setVisibility(View.GONE);
		rl_img08.setVisibility(View.GONE);
		rl_img09.setVisibility(View.GONE);
		rl_img10.setVisibility(View.GONE);
		rl_img11.setVisibility(View.GONE);
		rl_img12.setVisibility(View.GONE);
		rl_img13.setVisibility(View.GONE);
		rl_img14.setVisibility(View.GONE);
		rl_img15.setVisibility(View.GONE);
	}

	public void onEventMainThread(MEventPicChange event) {
		if (event.getChange()) {
			progress.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					pictureWallHomeInfos = GetData
							.getPictureWallHomeInfos(MyConfig.URL_JSON_PICTURE_WALL
									+ sid);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 1) {
			school = data.getExtras().getString(MyConfig.SCHOOL_SEARCH);
			sid = data.getExtras().getString(MyConfig.SCHOOL_SEARCH_ID);
			UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_PICTURE, school);
			UserInfoDB.setUserInfo(this, UserInfoDB.SCHOOL_ID_PICTURE, sid);
			tv_school.setText(school);
			progress.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					pictureWallHomeInfos = GetData
							.getPictureWallHomeInfos(MyConfig.URL_JSON_PICTURE_WALL
									+ sid);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& v.getId() == R.id.ib_add) {
			ib_add.startAnimation(MyAnimation.getScaleAnimationDown());
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
