package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.anyevent.MEventCollectOverseasChange;
import com.loosoo100.campus100.anyevent.MEventOverseas;
import com.loosoo100.campus100.anyevent.MEventSecondPage;
import com.loosoo100.campus100.beans.OverseasInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.MyScrollView;
import com.loosoo100.campus100.wxapi.WXShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 海归详情activity
 */
public class OverseasDetailActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_topbar)
	private RelativeLayout rl_topbar; // 顶部红色
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回按钮
	@ViewInject(R.id.rl_message)
	private RelativeLayout rl_message; // 消息按钮
	@ViewInject(R.id.iv_back_bg)
	private ImageView iv_back_bg; // 返回背景
	@ViewInject(R.id.iv_message_bg)
	private ImageView iv_message_bg; // 消息背景
	// @ViewInject(R.id.ib_share)
	// private ImageButton ib_share; // 分享按钮
	@ViewInject(R.id.ib_collect)
	private ImageButton ib_collect; // 收藏按钮
	// @ViewInject(R.id.btn_recommend)
	// private Button btn_recommend; // 我要推荐
	@ViewInject(R.id.btn_recommendTA)
	private Button btn_recommendTA; // 为ta推荐
	@ViewInject(R.id.vp_picture)
	private ViewPager vp_picture; // 图片轮播
	@ViewInject(R.id.tv_schoolName)
	private TextView tv_schoolName; // 学校名称
	@ViewInject(R.id.tv_schoolNameEng)
	private TextView tv_schoolNameEng; // 学校英文名称
	@ViewInject(R.id.tv_fee)
	private TextView tv_fee; // 大约费用
	@ViewInject(R.id.tv_collectCount)
	private TextView tv_collectCount; // 收藏人数
	@ViewInject(R.id.ratingBar1)
	private RatingBar ratingBar1; // 星星
	@ViewInject(R.id.ratingBar2)
	private RatingBar ratingBar2; // 星星
	@ViewInject(R.id.ratingBar3)
	private RatingBar ratingBar3; // 星星
	@ViewInject(R.id.ratingBar4)
	private RatingBar ratingBar4; // 星星
	@ViewInject(R.id.iv_circle01)
	public ImageButton iv_circle01;
	@ViewInject(R.id.iv_circle02)
	private ImageButton iv_circle02;
	@ViewInject(R.id.iv_circle03)
	private ImageButton iv_circle03;
	@ViewInject(R.id.abscrollview)
	private MyScrollView scrollview;
	@ViewInject(R.id.progress)
	private RelativeLayout progress;
	@ViewInject(R.id.ll_image)
	private LinearLayout ll_image; // 图片详情
	@ViewInject(R.id.webView)
	private WebView webView;

	private OverseasInfo overseasInfo;
	private String sid = ""; // 记录传进来的学校ID
	private String uid = "";
	private List<View> listview = new ArrayList<View>();
	private ScaleAnimation animation; // 收藏按钮点击动画

	private boolean isFirstShow = true;

	private WXShareUtil shareUtil;
	private String downUrl = "http://www.campus100.cn/App/index.php/Home/ApisharePay/link";

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (overseasInfo != null) {
				initView();
				initViewPager();
			}
			progress.setVisibility(View.GONE);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overseas_detail);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		EventBus.getDefault().register(this);

		sid = getIntent().getExtras().getString("sid");
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		shareUtil = new WXShareUtil(this);

		progress.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);
		rl_message.setOnClickListener(this);
		animation = MyAnimation.getScaleAnimationLocation();

		new Thread() {
			public void run() {
				overseasInfo = GetData
						.getOverseasInfo(MyConfig.URL_JSON_OVERSEAS_DETAIL
								+ sid + "&uid=" + uid);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		Glide.with(this).load(overseasInfo.getPicUrl01())
				.placeholder(R.drawable.imgloading).into(imageView);
		listview.add(imageView);

		ImageView imageView2 = new ImageView(this);
		imageView2.setScaleType(ScaleType.CENTER_CROP);
		Glide.with(this).load(overseasInfo.getPicUrl02())
				.placeholder(R.drawable.imgloading).into(imageView2);
		listview.add(imageView2);

		ImageView imageView3 = new ImageView(this);
		imageView3.setScaleType(ScaleType.CENTER_CROP);
		Glide.with(this).load(overseasInfo.getPicUrl03())
				.placeholder(R.drawable.imgloading).into(imageView3);
		listview.add(imageView3);

		vp_picture.setAdapter(new ViewPagerAdapter(listview));
		vp_picture.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					resetCircleImageColor();
					iv_circle01.setImageResource(R.drawable.circle_point_color);
					break;

				case 1:
					resetCircleImageColor();
					iv_circle02.setImageResource(R.drawable.circle_point_color);
					break;

				case 2:
					resetCircleImageColor();
					iv_circle03.setImageResource(R.drawable.circle_point_color);
					break;
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

	private void initView() {
		ib_collect.setOnClickListener(this);
		btn_recommendTA.setOnClickListener(this);

		tv_schoolName.setText(overseasInfo.getSchoolName());
		tv_schoolNameEng.setText(overseasInfo.getSchoolNameENG());
		tv_fee.setText(overseasInfo.getFee() + "");
		tv_collectCount.setText(overseasInfo.getCollectCount() + "");
		ratingBar1.setNumStars(overseasInfo.getEnterSchoolDifficulty());
		ratingBar2.setNumStars(overseasInfo.getSatisfaction());
		ratingBar3.setNumStars(overseasInfo.getFuture());
		ratingBar4.setNumStars(overseasInfo.getOurAppraise());
		if (overseasInfo.getStatus() == 0) {
			ib_collect.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.icon_collect));
		} else {
			ib_collect.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.icon_collect_red));
		}

		if (!overseasInfo.getOverLetter().equals("")) {
			webView.loadDataWithBaseURL(null, overseasInfo.getOverLetter(),
					"text/html", "utf-8", null);
			webView.setVisibility(View.VISIBLE);
		} else {
			webView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回按钮
		case R.id.rl_back:
			this.finish();
			break;

		// 消息按钮
		case R.id.rl_message:
			Intent intent = new Intent(this, MessageActivity.class);
			startActivity(intent);
			break;

		// 收藏按钮
		case R.id.ib_collect:
			ib_collect.setClickable(false);
			ib_collect.startAnimation(animation);
			if (overseasInfo.getStatus() == 0) {
				new Thread() {
					public void run() {
						postCollect(MyConfig.URL_POST_OVERSEAS_COLLECT);
					};
				}.start();
			} else {
				new Thread() {
					public void run() {
						postCollectCancel(MyConfig.URL_POST_OVERSEAS_COLLECT_CANCEL);
					};
				}.start();
			}
			break;

		// 为ta推荐
		case R.id.btn_recommendTA:
			shareUtil.shareMessageToSession("校园100", "下载链接", downUrl, null);
			break;

		}
	}

	/**
	 * 设置轮播小圆点的初始化颜色
	 */
	public void resetCircleImageColor() {
		iv_circle01.setImageResource(R.drawable.circle_point);
		iv_circle02.setImageResource(R.drawable.circle_point);
		iv_circle03.setImageResource(R.drawable.circle_point);
	}

	private void postCollect(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("uid", uid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							ib_collect.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.icon_collect_red));
							overseasInfo.setStatus(1);
							overseasInfo.setCollectCount(overseasInfo
									.getCollectCount() + 1);
							tv_collectCount.setText(overseasInfo
									.getCollectCount() + "");
							EventBus.getDefault()
									.post(new MEventOverseas(true));
							EventBus.getDefault().post(
									new MEventCollectOverseasChange(true));
						} else if (result.equals("0")) {
							ToastUtil.showToast(OverseasDetailActivity.this,"操作失败");
						}
						ib_collect.setClickable(true);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(OverseasDetailActivity.this,"操作失败");
						ib_collect.setClickable(true);
					}
				});
	}

	private void postCollectCancel(String uploadHost) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("uid", uid);
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							ib_collect.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.icon_collect));
							overseasInfo.setStatus(0);
							overseasInfo.setCollectCount(overseasInfo
									.getCollectCount() - 1);
							tv_collectCount.setText(overseasInfo
									.getCollectCount() + "");
							EventBus.getDefault().post(
									new MEventOverseas(false));
							EventBus.getDefault().post(
									new MEventCollectOverseasChange(true));
						} else if (result.equals("0")) {
							ToastUtil.showToast(OverseasDetailActivity.this,"操作失败");
						}
						ib_collect.setClickable(true);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(OverseasDetailActivity.this,"操作失败");
						ib_collect.setClickable(true);
					}
				});
	}

	public void onEventMainThread(MEventSecondPage event) {
		if (event.isShow() && isFirstShow) {
			isFirstShow = false;
			for (int i = 0; i < overseasInfo.getPicList().size(); i++) {
				ImageView imageView = new ImageView(this);
				LayoutParams params = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				imageView.setLayoutParams(params);
				imageView.setAdjustViewBounds(true);
				Glide.with(this).load(overseasInfo.getPicList().get(i))
						.dontAnimate().placeholder(R.drawable.imgloading) // 设置占位图
						.into(imageView);
				ll_image.addView(imageView);
			}
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
