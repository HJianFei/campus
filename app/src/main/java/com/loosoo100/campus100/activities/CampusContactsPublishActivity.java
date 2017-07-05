package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CampusPublishGridViewAdapter;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.imagepicker.ui.activity.ImagesGridActivity;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.utils.campus.ImageTools;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yang 发表校园圈activity
 */
public class CampusContactsPublishActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_publish)
	private RelativeLayout rl_publish; // 发布
	@ViewInject(R.id.et_contacts)
	private EditText et_contacts; // 内容
	@ViewInject(R.id.tv_picCount)
	private TextView tv_picCount; // 图片数量
	@ViewInject(R.id.ll_picture)
	private LinearLayout ll_picture; // 添加的图片布局
	@ViewInject(R.id.btn_feeling)
	private Button btn_feeling; // 心情
	@ViewInject(R.id.btn_trading)
	private Button btn_trading; // 买卖
	@ViewInject(R.id.btn_gogo)
	private Button btn_gogo; // GOGO
	@ViewInject(R.id.btn_lostAndFound)
	private Button btn_lostAndFound; // 失物招领
	@ViewInject(R.id.ll_publicOrPrivate)
	private LinearLayout ll_publicOrPrivate; // 选择公开或私人
	@ViewInject(R.id.tv_publicOrPrivate)
	private TextView tv_publicOrPrivate; // 公开或私人
	@ViewInject(R.id.ll_dialog)
	private LinearLayout ll_dialog; // 公开或私人选择框
	@ViewInject(R.id.btn_public)
	private Button btn_public; // 公开
	@ViewInject(R.id.btn_private)
	private Button btn_private; // 私人
	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg; // 加载动画
	// 图片 九宫格
	@ViewInject(R.id.noScrollgridview)
	private GridView gv;

	private String uid = "";
	private String sid = "";
	private String lettter = "";
	private int isshow = 1; // 是否显示 0不显示 1显示
	private String sName = "";
	private int type = 1; // 1心情 2买卖 3GOGO 4失物招领

	// 图片 九宫格适配器
	private CampusPublishGridViewAdapter gvAdapter;

	// 用于保存图片路径
	private ArrayList<String> list_path = new ArrayList<String>();
	// 拍照
	public static final int IMAGE_CAPTURE = 1;
	// 从相册选择
	public static final int IMAGE_SELECT = 2;
	private String fileName = "";
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_contacts);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");
		sName = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL, "");

		initView();
		initGridView();
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_publish.setOnClickListener(this);
		btn_feeling.setOnClickListener(this);
		btn_trading.setOnClickListener(this);
		btn_gogo.setOnClickListener(this);
		btn_lostAndFound.setOnClickListener(this);
		ll_publicOrPrivate.setOnClickListener(this);
		btn_public.setOnClickListener(this);
		btn_private.setOnClickListener(this);

		dialog = new Dialog(this, R.style.MyDialog);
		LayoutInflater inflater = LayoutInflater.from(this);
		View viewDialog = inflater.inflate(R.layout.dialog_camera, null);
		dialog.setContentView(viewDialog);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		dialog.setContentView(viewDialog, params);
		viewDialog.findViewById(R.id.btn_camera).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						captureImage(FileUtils.SDPATH);
						dialog.dismiss();
					}
				});

		viewDialog.findViewById(R.id.btn_album).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectImage();
						dialog.dismiss();
					}
				});

		/**
		 * 监听备注输入字数
		 */
		// et_contacts.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// tv_wordCount.setText(s.length() + "/150");
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// }
		// });
	}

	private void initGridView() {
		gvAdapter = new CampusPublishGridViewAdapter(this, list_path);
		gv.setAdapter(gvAdapter);
		// gvAdapter.setList(list_path);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (position == (list_path == null ? 0 : list_path.size())) {// 点击“+”号位置添加图片
					dialog.show();
				} else {
					Intent intent = new Intent(
							CampusContactsPublishActivity.this,
							CampusContactsImagePreviewActivity.class);
					intent.putExtra("index", position);
					intent.putExtra("button", false);
					intent.putStringArrayListExtra("urlList", list_path);
					startActivity(intent);
				}
			}
		});

		gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (position == (list_path == null ? 0 : list_path.size())) {// 点击“+”号位置添加图片
					return true;
				} else {
					CustomDialog.Builder builder = new CustomDialog.Builder(
							CampusContactsPublishActivity.this);
					builder.setMessage("是否删除此图片");
					builder.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									FileUtils.delFile(list_path.get(position));
									list_path.remove(position);
									gvAdapter.setList(list_path);
									tv_picCount.setText(list_path.size() + "/9");
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("否", null);
					builder.create().show();
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 取消
		case R.id.rl_back:
			if (!et_contacts.getText().toString().trim().equals("")
					|| list_path.size() != 0) {
				CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
				builderDel.setMessage("是否确认退出编辑");
				builderDel.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CampusContactsPublishActivity.this.finish();
							}
						});
				builderDel.setNegativeButton("否", null);
				builderDel.create().show();
			} else {
				this.finish();
			}
			break;

		// 发表
		case R.id.rl_publish:
			lettter = et_contacts.getText().toString().trim();
			if (lettter.equals("") && list_path.size() == 0) {
				ToastUtil.showToast(CampusContactsPublishActivity.this,"发表内容不能为空");
				return;
			}
			rl_publish.setClickable(false);
			MyUtils.hideSoftInput(this, et_contacts);
			progress_update_blackbg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					postCampusContacts(MyConfig.URL_POST_CAMPUS_PUBLISH,
							list_path);
				};
			}.start();

			break;

		// 心情
		case R.id.btn_feeling:
			type = 1;
			resetButton();
			btn_feeling.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_stroke_small));
			btn_feeling.setTextColor(getResources()
					.getColor(R.color.red_fd3c49));
			break;

		// 买卖
		case R.id.btn_trading:
			type = 2;
			resetButton();
			btn_trading.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_stroke_small));
			btn_trading.setTextColor(getResources()
					.getColor(R.color.red_fd3c49));
			break;

		// GOGO
		case R.id.btn_gogo:
			type = 3;
			resetButton();
			btn_gogo.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_stroke_small));
			btn_gogo.setTextColor(getResources().getColor(R.color.red_fd3c49));
			break;

		// 失物招领
		case R.id.btn_lostAndFound:
			type = 4;
			resetButton();
			btn_lostAndFound.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.shape_red_stroke_small));
			btn_lostAndFound.setTextColor(getResources().getColor(
					R.color.red_fd3c49));
			break;

		// 选择公开或私人
		case R.id.ll_publicOrPrivate:
			if (ll_dialog.getVisibility() == View.VISIBLE) {
				ll_dialog.setVisibility(View.GONE);
				ll_dialog.startAnimation(MyAnimation
						.getScaleAnimationToRightTop());
			} else {
				ll_dialog.setVisibility(View.VISIBLE);
				ll_dialog.startAnimation(MyAnimation
						.getScaleAnimationToLeftBottom());
			}
			break;

		// 公开
		case R.id.btn_public:
			isshow = 1;
			tv_publicOrPrivate.setText("公开");
			btn_public
					.setTextColor(getResources().getColor(R.color.red_fd3c49));
			btn_private.setTextColor(getResources().getColor(R.color.black));
			ll_dialog.setVisibility(View.GONE);
			ll_dialog.startAnimation(MyAnimation.getScaleAnimationToRightTop());
			break;

		// 私人
		case R.id.btn_private:
			isshow = 0;
			tv_publicOrPrivate.setText("私人");
			btn_public.setTextColor(getResources().getColor(R.color.black));
			btn_private.setTextColor(getResources()
					.getColor(R.color.red_fd3c49));
			ll_dialog.setVisibility(View.GONE);
			ll_dialog.startAnimation(MyAnimation.getScaleAnimationToRightTop());
			break;

		}
	}

	/**
	 * 拍照
	 * 
	 * @param path
	 *            照片存放的路径
	 */
	public void captureImage(String path) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		File file2 = new File(path);
		if (!file2.exists()) {
			file2.mkdirs();
		}
		File file = new File(path + "/image.jpg");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Uri uri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, IMAGE_CAPTURE);
	}

	/**
	 * 从图库中选取图片
	 */
	public void selectImage() {
		Intent intent = new Intent(this, ImagesGridActivity.class);
		intent.putExtra("limit", 9 - list_path.size());
		startActivityForResult(intent, IMAGE_SELECT);
	}

	/**
	 * 还原按钮颜色
	 */
	private void resetButton() {
		btn_feeling.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_gray_stroke));
		btn_trading.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_gray_stroke));
		btn_gogo.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_gray_stroke));
		btn_lostAndFound.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shape_gray_stroke));
		btn_feeling.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
		btn_trading.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
		btn_gogo.setTextColor(getResources().getColor(R.color.gray_b3b3b3));
		btn_lostAndFound.setTextColor(getResources().getColor(
				R.color.gray_b3b3b3));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				case IMAGE_CAPTURE:// 拍照返回
					// 将保存在本地的图片取出并缩小后显示在界面上
					Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.SDPATH
							+ "/image.jpg");
					Bitmap newBitmap = MyUtils.ResizeBitmapNoRecycle(bitmap,
							720);
					// 生成一个图片文件名
					fileName = String.valueOf(System.currentTimeMillis());
					// 将处理过的图片添加到缩略图列表并保存到本地
					ImageTools.savePhotoToSDCard(newBitmap, FileUtils.SDPATH,
							fileName);
					// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
					bitmap.recycle();
					list_path.add(FileUtils.SDPATH + "/" + fileName + ".jpg");

					// 更新GrideView
					gvAdapter.setList(list_path);
					tv_picCount.setText(list_path.size() + "/9");
					break;
				case IMAGE_SELECT:// 选择照片返回
					ArrayList<String> list2 = data.getExtras()
							.getStringArrayList("picList");
					for (int j = 0; j < list2.size(); j++) {
						if (list2.get(j).contains(".gif")) {
							list_path.add(list2.get(j));
						} else {
							Bitmap photo = BitmapFactory.decodeFile(list2
									.get(j));
							if (photo != null) {
								Bitmap newBitmap2 = photo;
								// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
								if (photo.getWidth() > 720) {
									newBitmap2 = MyUtils.ResizeBitmapNoRecycle(
											photo, 720);
								}
								// 生成一个图片文件名
								fileName = String.valueOf(System
										.currentTimeMillis());
								// 将处理过的图片添加到缩略图列表并保存到本地
								ImageTools.savePhotoToSDCard(newBitmap2,
										FileUtils.SDPATH, fileName);
								// 释放原始图片占用的内存，防止out of memory异常发生
								photo.recycle();
								list_path.add(FileUtils.SDPATH + "/" + fileName
										+ ".jpg");
							}
						}
					}
					// 更新GrideView
					gvAdapter.setList(list_path);
					tv_picCount.setText(list_path.size() + "/9");
					break;

				}
			}
		}
	}

	/**
	 * 发表圈圈
	 * 
	 * @param uploadHost
	 */
	private void postCampusContacts(String uploadHost, List<String> list) {
		HttpUtils httpUtils = new HttpUtils(100000);
		httpUtils.configCurrentHttpCacheExpiry(5000);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("lettter", lettter);
		params.addBodyParameter("isshow", isshow + "");
		params.addBodyParameter("sName", sName);
		params.addBodyParameter("type", type + "");
		for (int i = 0; i < list.size(); i++) {
			params.addBodyParameter("pic" + (i + 1), new File(list.get(i)),
					"image/jpeg");
		}
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						progress_update_blackbg.setVisibility(View.GONE);
						String result = "";
						rl_publish.setClickable(true);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("")) {
							ToastUtil.showToast(CampusContactsPublishActivity.this,"发表失败");
						} else if (result.equals("0")) {
							ToastUtil.showToast(CampusContactsPublishActivity.this,"发表失败");
						} else {
							ToastUtil.showToast(CampusContactsPublishActivity.this,"发表成功");
							Intent intent = new Intent();
							setResult(RESULT_OK, intent);
							CampusContactsPublishActivity.this.finish();
						}
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
						rl_publish.setClickable(true);
						ToastUtil.showToast(CampusContactsPublishActivity.this,"发表失败");
					}
				});
	}

	@Override
	protected void onDestroy() {
		// 删除文件夹及文件
		FileUtils.deleteDir();
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		if (ll_dialog.getVisibility()==View.VISIBLE) {
			ll_dialog.setVisibility(View.GONE);
		}
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!et_contacts.getText().toString().trim().equals("")
					|| list_path.size() != 0) {
				CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
				builderDel.setMessage("是否退出编辑");
				builderDel.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CampusContactsPublishActivity.this.finish();
							}
						});
				builderDel.setNegativeButton("否", null);
				builderDel.create().show();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
