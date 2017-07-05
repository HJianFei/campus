package com.loosoo100.campus100.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.wxapi.WXShareUtil;

/**
 * 
 * @author yang 礼物说凑一凑activity
 */
public class GiftTogetherActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view

	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回按钮
	@ViewInject(R.id.btn_sure_pay)
	private Button btn_sure_pay; // 找人帮付按钮

	@ViewInject(R.id.et_remark)
	private EditText et_remark; // 备注

	@ViewInject(R.id.tv_wordCount)
	private TextView tv_wordCount; // 备注字数提示

	@ViewInject(R.id.iv_goodsIcon)
	private ImageView iv_goodsIcon; // 商品图标

	@ViewInject(R.id.tv_goodsName)
	private TextView tv_goodsName; // 商品名

	@ViewInject(R.id.tv_goodsCount)
	private TextView tv_goodsCount; // 商品数量

	@ViewInject(R.id.tv_goodsPrice)
	private TextView tv_goodsPrice; // 商品价格

	private String oid = "";
//	private String giftgoodsId = "";
	private String giftgoodsName = "";
	private String giftgoodsImg = "";
	private float shopPrice;
	private int count;
	private Bitmap bitmap;

	// private String oid = "";

	private int type;

	private String remark = "";
	private WXShareUtil shareUtil;

	private boolean isWeiXin = false;

	private Dialog dialog;

	private Handler handlerSend = new Handler() {
		public void handleMessage(android.os.Message msg) {
			isWeiXin = true;
			// AlertDialog.Builder builder = new AlertDialog.Builder(
			// GiftTogetherActivity.this);
			// builder.setTitle("请选择");
			// builder.setItems(new String[] { "微信好友", "微信朋友圈" },
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog, int which) {
			// if (which == 0) {
			// shareUtil.shareMessageToSession(
			// "凑一凑：" + remark, giftgoodsName,
			// MyConfig.SHARE_PAY_TOGETHER + oid
			// + "&type=1", bitmap);
			// } else {
			// shareUtil.shareMessageToTimeLine("凑一凑："
			// + remark, giftgoodsName,
			// MyConfig.SHARE_PAY_TOGETHER + oid
			// + "&type=1", bitmap);
			// }
			// dialog.dismiss();
			// }
			// });
			// builder.show();
			dialog.show();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_together);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		shareUtil = new WXShareUtil(this);

//		giftgoodsId = getIntent().getExtras().getString("giftgoodsId");
		oid = getIntent().getExtras().getString("oid");
		giftgoodsName = getIntent().getExtras().getString("giftgoodsName");
		giftgoodsImg = getIntent().getExtras().getString("giftgoodsImg");
		count = getIntent().getExtras().getInt("count");
		shopPrice = getIntent().getExtras().getFloat("shopPrice");
		type = getIntent().getExtras().getInt("type");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_sure_pay.setOnClickListener(this);

		Glide.with(this).load(giftgoodsImg).placeholder(R.drawable.imgloading) // 设置占位图
				.into(iv_goodsIcon);
		tv_goodsName.setText(giftgoodsName);
		tv_goodsPrice.setText("￥" + shopPrice * count);
		tv_goodsCount.setText("数量x" + count);

		dialog = new Dialog(this, R.style.MyDialog);
		LayoutInflater inflater = LayoutInflater.from(this);
		View viewDialog = inflater.inflate(R.layout.dialog_weixin_choice, null);
		dialog.setContentView(viewDialog);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialog.setContentView(viewDialog, params);
		viewDialog.findViewById(R.id.btn_weixin).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						shareUtil.shareMessageToSession("凑一凑：" + remark,
								giftgoodsName, MyConfig.SHARE_PAY_TOGETHER
										+ oid + "&type=1", bitmap);
					}
				});

		viewDialog.findViewById(R.id.btn_timeline).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						shareUtil.shareMessageToTimeLine("凑一凑：" + remark,
								giftgoodsName, MyConfig.SHARE_PAY_TOGETHER
										+ oid + "&type=1", bitmap);
					}
				});

		/**
		 * 监听备注输入字数
		 */
		et_remark.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_wordCount.setText(s.length() + "/20");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回按钮
		case R.id.rl_back:
			if (type == 0 || type == 1) {
				GiftTogetherActivity.this.finish();
			} else {
				// 跳转到凑一凑的正在凑详情页面
				Intent intent = new Intent(GiftTogetherActivity.this,
						GiftOrderDetailTogetherActivity.class);
				intent.putExtra("index", 0);
				intent.putExtra("type", 3);
				intent.putExtra("oid", oid);
				startActivity(intent);
				GiftTogetherActivity.this.finish();
			}
			break;

		// 找人帮付按钮
		case R.id.btn_sure_pay:
			remark = et_remark.getText().toString().trim();
			new Thread() {
				public void run() {
					try {
						bitmap = MyUtils.getBitMap(giftgoodsImg);
						if (bitmap != null) {
							bitmap = MyUtils.ResizeBitmap(bitmap, 100);
						}
						handlerSend.sendEmptyMessage(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();

			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (type == 0 || type == 1) {
				GiftTogetherActivity.this.finish();
			} else {
				// 跳转到凑一凑的正在凑详情页面
				Intent intent = new Intent(GiftTogetherActivity.this,
						GiftOrderDetailTogetherActivity.class);
				intent.putExtra("index", 0);
				intent.putExtra("type", 3);
				intent.putExtra("oid", oid);
				startActivity(intent);
				GiftTogetherActivity.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		if (isWeiXin) {
			if (type == 0 || type == 1) {
				GiftTogetherActivity.this.finish();
			} else {
				// 跳转到凑一凑的正在凑详情页面
				Intent intent = new Intent(GiftTogetherActivity.this,
						GiftOrderDetailTogetherActivity.class);
				intent.putExtra("index", 0);
				intent.putExtra("type", 3);
				intent.putExtra("oid", oid);
				startActivity(intent);
				GiftTogetherActivity.this.finish();
			}
		}
		super.onRestart();
	}

}
