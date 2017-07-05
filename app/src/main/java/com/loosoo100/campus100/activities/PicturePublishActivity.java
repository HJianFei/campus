package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.anyevent.MEventPicChange;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 发表图片activity
 */
public class PicturePublishActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back; // 返回
	@ViewInject(R.id.rl_publish)
	private RelativeLayout rl_publish; // 发布
	@ViewInject(R.id.et_dream)
	private EditText et_dream; // 内容
	@ViewInject(R.id.rl_picture)
	private RelativeLayout rl_picture; // 图片
	@ViewInject(R.id.iv_picture)
	private ImageView iv_picture; // 图片
	@ViewInject(R.id.progress_update_whitebg)
	private RelativeLayout progress_update_whitebg; // 加载动画

	private final String IMAGE_UNSPECIFIED = "image/*";

	private String uid = "";
	private String sid = "";
	private String content = "";

	private Uri imageUri;
	private String fileName = MyConfig.FILE_URI + "/picwall.jpg";
	private boolean isImg = false;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_picture);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);

		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");
		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		rl_publish.setOnClickListener(this);
		rl_picture.setOnClickListener(this);

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
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(fileName)));
						startActivityForResult(intent,
								MyConfig.CAMERA_REQUEST_CODE);
						dialog.dismiss();
					}
				});

		viewDialog.findViewById(R.id.btn_album).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent imageIntent = new Intent(Intent.ACTION_PICK,
								null);
						imageIntent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								IMAGE_UNSPECIFIED);
						startActivityForResult(imageIntent,
								MyConfig.ALBUM_REQUEST_CODE);
						dialog.dismiss();
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			if (!et_dream.getText().toString().trim().equals("") || isImg) {
				CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
				builderDel.setMessage("是否退出编辑");
				builderDel.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								PicturePublishActivity.this.finish();
							}
						});
				builderDel.setNegativeButton("否", null);
				builderDel.create().show();
			} else {
				finish();
			}
			break;

		case R.id.rl_publish:
			content = et_dream.getText().toString().trim();
			if (content.equals("")) {
				ToastUtil.showToast(this,"请输入愿望内容");
				return;
			}
			if (!isImg) {
				ToastUtil.showToast(this,"请添加照片");
				return;
			}
			rl_publish.setClickable(false);
			MyUtils.hideSoftInput(this, et_dream);
			progress_update_whitebg.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					doPostXUtils(MyConfig.URL_POST_PICTURE_WALL_PUBLISH,
							fileName);
				};
			}.start();
			break;

		case R.id.rl_picture:
			dialog.show();
			// Intent imageIntent = new Intent(Intent.ACTION_PICK, null);
			// imageIntent.setDataAndType(
			// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			// IMAGE_UNSPECIFIED);
			// startActivityForResult(imageIntent, MyConfig.ALBUM_REQUEST_CODE);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case MyConfig.ALBUM_REQUEST_CODE:
				if (data == null) {
					return;
				}
				startCrop(data.getData());
				break;
			case MyConfig.CROP_REQUEST_CODE:
				if (imageUri != null) {
					Bitmap bitmap = decodeUriAsBitmap(imageUri);
					// 把解析到的位图显示出来
					iv_picture.setImageBitmap(bitmap);
//					iv_picture.setImageURI(imageUri);
					isImg = true;
					try {
						saveImage(bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			case MyConfig.CAMERA_REQUEST_CODE:
				// 设置文件保存路径
				File picture = new File(fileName);
				startCrop(Uri.fromFile(picture));
				break;
			}
		}
	}

	private void startCrop(Uri uri) {
		imageUri = Uri.fromFile(new File(fileName));
		Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面
		intent.setDataAndType(uri, MyConfig.IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");// 进行修剪
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 1280);
		intent.putExtra("outputY", 1280);
		// intent.putExtra("return-data", true);
		// true的话直接返回bitmap，可能会很占内存 不建议
		intent.putExtra("return-data", false);
		intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
		// 上面设为false的时候将MediaStore.EXTRA_OUTPUT即"output"关联一个Uri
		intent.putExtra("output", imageUri);
		// 看参数即可知道是输出格式
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// 面部识别 这里用不上
		intent.putExtra("noFaceDetection", false);
		startActivityForResult(intent, MyConfig.CROP_REQUEST_CODE);
	}

	/**
	 * 上传数据 和 图片
	 * 
	 * @param uploadHost
	 * @param picUrl
	 */
	private void doPostXUtils(String uploadHost, String picUrl) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("sid", sid);
		params.addBodyParameter("content", content);
		params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						progress_update_whitebg.setVisibility(View.GONE);
						LogUtils.d("responseInfo",responseInfo.result);
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
							ToastUtil.showToast(PicturePublishActivity.this,"发布失败");
						} else if (result.equals("0")) {
							ToastUtil.showToast(PicturePublishActivity.this,"发布失败");
						} else {
							ToastUtil.showToast(PicturePublishActivity.this,"发布成功");
							EventBus.getDefault().post(
									new MEventPicChange(true));
							PicturePublishActivity.this.finish();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(PicturePublishActivity.this,"发布失败");
						rl_publish.setClickable(true);
						progress_update_whitebg.setVisibility(View.GONE);
					}
				});
	}

	// private String getCameraImage(Bundle bundle) {
	// String strState = Environment.getExternalStorageState();
	// if (!strState.equals(Environment.MEDIA_MOUNTED)) {
	// }
	// String fileName = System.currentTimeMillis() + ".jpg";
	// Bitmap bitmap = (Bitmap) bundle.get("data");
	// File file = new File(MyConfig.FILE_URI);
	// if (!file.exists()) {
	// file.mkdirs();
	// }
	// fileName = MyConfig.FILE_URI + "/" + fileName;
	// FileOutputStream stream = null;
	// try {
	// stream = new FileOutputStream(fileName);
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (stream != null) {
	// stream.flush();
	// stream.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return fileName;
	// }

	private void saveImage(Bitmap bitmap) throws IOException {
		File file = new File(MyConfig.FILE_URI);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream iStream = new FileOutputStream(fileName);
		bitmap.compress(CompressFormat.JPEG, 80, iStream);
		iStream.close();
		iStream = null;
		file = null;
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!et_dream.getText().toString().trim().equals("") || isImg) {
				CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
				builderDel.setMessage("是否退出编辑");
				builderDel.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								PicturePublishActivity.this.finish();
							}
						});
				builderDel.setNegativeButton("否", null);
				builderDel.create().show();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		FileUtils.deleteFile(new File(fileName));
		super.onDestroy();
	}
}
