package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
import com.loosoo100.campus100.adapters.CampusContactsPersonalAdapter;
import com.loosoo100.campus100.anyevent.MEventCampusContactsAllDel;
import com.loosoo100.campus100.anyevent.MEventCampusContactsChange;
import com.loosoo100.campus100.anyevent.MEventCampusContactsPerChange;
import com.loosoo100.campus100.anyevent.MEventCampusNoRead;
import com.loosoo100.campus100.anyevent.MEventCampusNoReadFriend;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.beans.CampusContactsUserInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyAnimation;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableHelper.ScrollableContainer;
import com.loosoo100.campus100.view.scrollablelayout.ScrollableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 
 * @author yang 校园圈个人中心activity
 */
public class CampusContactsPersonalActivity extends Activity implements
		OnClickListener, OnTouchListener {

	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private RelativeLayout rl_back;
	@ViewInject(R.id.tv_school)
	private TextView tv_school; // 所属学校
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_count)
	private TextView tv_count;
	@ViewInject(R.id.ll_love)
	private LinearLayout ll_love;
	@ViewInject(R.id.ll_unrequited)
	private LinearLayout ll_unrequited; // 暗恋的人
	@ViewInject(R.id.ll_myfriend)
	private LinearLayout ll_myfriend; // 我的友友
	@ViewInject(R.id.iv_circle01)
	private ImageView iv_circle01; // 我的友友引导红点
	@ViewInject(R.id.iv_circle02)
	private ImageView iv_circle02; // 暗恋的人引导红点

	@ViewInject(R.id.cv_headShot)
	private CircleView cv_headShot;
	@ViewInject(R.id.iv_sex)
	private ImageView iv_sex;
	@ViewInject(R.id.tv_noData)
	private TextView tv_noData;
	// @ViewInject(R.id.progress)
	// private RelativeLayout progress;
	@ViewInject(R.id.progress_update_whitebg_campus)
	private RelativeLayout progress_update_whitebg_campus;
	@ViewInject(R.id.scrollableLayout)
	private ScrollableLayout scrollableLayout;
	@ViewInject(R.id.ib_add)
	private ImageButton ib_add; // 发表

	@ViewInject(R.id.lv_campus)
	private ListView lv_campus; // 校园圈列表

	@ViewInject(R.id.iv_bg)
	private ImageView iv_bg; // 背景图片

	private List<CampusContactsInfo> list;
	private CampusContactsPersonalAdapter adapter;

	private String sid = "";
	private String school = "";
	private int type = 0;
	private String uid = "";
	private int page = 1;
	private int status = 2;
	private boolean isLoading = true;

	protected CampusContactsUserInfo userInfo;

	private String fileName = MyConfig.FILE_URI + "/timelinebg.jpg";
	private Dialog dialog;
	private final String IMAGE_UNSPECIFIED = "image/*";
	private Uri imageUri;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// progress.setVisibility(View.GONE);
			progress_update_whitebg_campus.setVisibility(View.GONE);
			if (userInfo != null) {
				ll_love.setVisibility(View.VISIBLE);
				tv_count.setText(userInfo.getCrushNum() + "");
				if (!userInfo.getBg().equals("")
						&& !userInfo.getBg().equals("null")) {
					// 设置背景图
					Glide.with(CampusContactsPersonalActivity.this)
							.load(userInfo.getBg()).into(iv_bg);
					new Thread() {
						public void run() {
							try {
								Bitmap bitmap = MyUtils.getBitMap(userInfo
										.getBg());
								if (bitmap != null) {
									saveUserBg(bitmap);
									bitmap.recycle();
									bitmap = null;
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						};
					}.start();
				}
			}
			if (list != null && list.size() > 0) {
				initListView();
				lv_campus.setVisibility(View.VISIBLE);
				tv_noData.setVisibility(View.GONE);
			} else {
				lv_campus.setVisibility(View.GONE);
				tv_noData.setVisibility(View.VISIBLE);
			}
			page++;
			isLoading = false;
		};
	};

	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (adapter != null) {
				adapter.notifyDataSetChanged();
				// MyUtils.setListViewHeight(lv_campus, 20);
			}
			page++;
			isLoading = false;
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campus_contacts_personal);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 更改状态栏字体颜色为黑色
		MyUtils.setMiuiStatusBarDarkMode(this, true);
		EventBus.getDefault().register(this);

//		UserInfoDB.setUserInfo(this, UserInfoDB.CAMPUS_NOREAD_HOME_LOVE, "0");
//		UserInfoDB.setUserInfo(this, UserInfoDB.CAMPUS_NOREAD_HOME_FRIEND, "0");

		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL_ID, "");
		school = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.SCHOOL, "");
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		tv_name.setText(getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.NICK_NAME, ""));
		/*
		 * 下滑时当lv_campus滑动到顶部时头部view才显示出来
		 */
		scrollableLayout.getHelper().setCurrentScrollableContainer(
				new ScrollableContainer() {
					@Override
					public View getScrollableView() {
						return lv_campus;
					}
				});

		initView();
		progress_update_whitebg_campus.setVisibility(View.VISIBLE);
		tv_noData.setVisibility(View.GONE);

		new Thread() {
			public void run() {
				userInfo = GetData
						.getCampusContactsUserInfo(MyConfig.URL_JSON_CAMPUS
								+ sid + "&type=" + type + "&uid=" + uid
								+ "&muid=" + uid + "&status=" + status
								+ "&page=" + page);
				list = GetData
						.getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
								+ sid + "&type=" + type + "&uid=" + uid
								+ "&muid=" + uid + "&status=" + status
								+ "&page=" + page);
				if (!isDestroyed()) {
					handler.sendEmptyMessage(0);
				}
			};
		}.start();

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

	private void initListView() {
		adapter = new CampusContactsPersonalAdapter(this, list);
		lv_campus.setAdapter(adapter);
		// MyUtils.setListViewHeight(lv_campus, 20);
		lv_campus.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CampusContactsPersonalActivity.this,
						CampusContactsDetailPersonalActivity.class);
				intent.putExtra("mid", list.get(position).getId());
				// intent.putExtra("index", position);
				startActivity(intent);
			}
		});

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		iv_bg.setOnClickListener(this);
		ll_unrequited.setOnClickListener(this);
		ll_myfriend.setOnClickListener(this);
		cv_headShot.setOnClickListener(this);
		ib_add.setOnClickListener(this);
		ib_add.setOnTouchListener(this);
		tv_school.setText(school);
		
		// 设置用户背景
		File file = new File(MyConfig.IMAGGE_URI + uid + "timelinebg.png");
		if (file.exists()) {
			iv_bg.setImageBitmap(BitmapFactory.decodeFile(MyConfig.IMAGGE_URI + uid
					+ "timelinebg.png"));
		}
		
		String headShot = getSharedPreferences(UserInfoDB.USERTABLE,
				MODE_PRIVATE).getString(UserInfoDB.HEADSHOT, "");
		if (!headShot.equals("")) {
			// 设置头像和性别图标
			Glide.with(this).load(headShot).into(cv_headShot);
		}
		if (getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(
				UserInfoDB.SEX, "").equals("1")) {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_female_picture));
		} else {
			iv_sex.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_male_picture));
		}

		lv_campus.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0 && !isLoading) {
					isLoading = true;
					new Thread() {
						List<CampusContactsInfo> list2 = null;

						public void run() {
							list2 = GetData
									.getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
											+ sid
											+ "&type="
											+ type
											+ "&uid="
											+ uid
											+ "&muid="
											+ uid
											+ "&status="
											+ status + "&page=" + page);
							if (list2 != null && list2.size() > 0) {
								for (int i = 0; i < list2.size(); i++) {
									list.add(list2.get(i));
								}
								if (!isDestroyed()) {
									handler2.sendEmptyMessage(0);
								}
							}
						};
					}.start();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		// 更改背景图
		case R.id.iv_bg:
			File file = new File(MyConfig.FILE_URI);
			if (!file.exists()) {
				file.mkdirs();
			}
			File dir = new File(fileName);
			if (!dir.exists()) {
				try {
					dir.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dialog.show();
			break;

		case R.id.ll_unrequited:
			Intent intent = new Intent(this, CampusContactsLoveActivity.class);
			startActivity(intent);
			break;

		case R.id.ll_myfriend:
			Intent intent2 = new Intent(this,
					CampusContactsMyFriendActivity.class);
			startActivity(intent2);
			break;

		case R.id.cv_headShot:
			Intent intent3 = new Intent(this, PicturePreviewActivity.class);
			intent3.putExtra("picUrl", "person");
			startActivity(intent3);
			break;

		// 发表
		case R.id.ib_add:
			Intent publishIntent = new Intent(this,
					CampusContactsPublishActivity.class);
			startActivityForResult(publishIntent, 8);
			break;
		}
	}

	/**
	 * 红点是否显示
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCampusNoRead event) {
		if (event.isChange()) {
			if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
					.getString(UserInfoDB.CAMPUS_NOREAD_LOVE, "0").equals("0")) {
				iv_circle02.setVisibility(View.VISIBLE);
			} else {
				iv_circle02.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 红点是否显示
	 * 
	 * @param event
	 */
	public void onEventMainThread(MEventCampusNoReadFriend event) {
		if (event.isChange()) {
			if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
					.getString(UserInfoDB.CAMPUS_NOREAD_FRIEND, "0")
					.equals("0")) {
				iv_circle01.setVisibility(View.VISIBLE);
			} else {
				iv_circle01.setVisibility(View.GONE);
			}
		}
	}

	public void onEventMainThread(MEventCampusContactsPerChange event) {
		if (event.isChange()) {
			page = 1;
			// CampusContactsActivity.isChange = true;
			progress_update_whitebg_campus.setVisibility(View.VISIBLE);
			new Thread() {
				public void run() {
					list = GetData
							.getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
									+ sid
									+ "&type="
									+ type
									+ "&uid="
									+ uid
									+ "&muid="
									+ uid
									+ "&status="
									+ status
									+ "&page=" + page);
					if (!isDestroyed()) {
						handler.sendEmptyMessage(0);
					}
				};
			}.start();
		}
	}

	public void onEventMainThread(MEventCampusContactsAllDel event) {
		if (event.isChange()) {
			this.finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 8:
				EventBus.getDefault().post(
						new MEventCampusContactsPerChange(true));
				EventBus.getDefault()
						.post(new MEventCampusContactsChange(true));
				page = 1;
				progress_update_whitebg_campus.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						list = GetData
								.getCampusContactsPersonInfos(MyConfig.URL_JSON_CAMPUS
										+ sid
										+ "&type="
										+ type
										+ "&uid="
										+ uid
										+ "&muid="
										+ uid
										+ "&status="
										+ status
										+ "&page=" + page);
						if (!isDestroyed()) {
							handler.sendEmptyMessage(0);
						}
					};
				}.start();
				break;
			case MyConfig.ALBUM_REQUEST_CODE:
				if (data == null) {
					return;
				}
				startCrop(data.getData());
				break;
			case MyConfig.CROP_REQUEST_CODE:
				if (imageUri != null) {
					Bitmap bitmap = decodeUriAsBitmap(imageUri);
					try {
						saveImage(bitmap);
						progress_update_whitebg_campus
								.setVisibility(View.VISIBLE);
						new Thread() {
							public void run() {
								postUpdateBg(
										MyConfig.URL_POST_CONTACTS_UPDATE_BG,
										fileName);
							};
						}.start();
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
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startCrop(Uri uri) {
		imageUri = Uri.fromFile(new File(fileName));
		Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面
		intent.setDataAndType(uri, MyConfig.IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");// 进行修剪
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 15);
		intent.putExtra("aspectY", 7);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 1500);
		intent.putExtra("outputY", 700);
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

	private void saveImage(Bitmap bitmap) throws IOException {
		File dir = new File(MyConfig.FILE_URI);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream iStream = new FileOutputStream(fileName);
		bitmap.compress(CompressFormat.JPEG, 80, iStream);
		iStream.close();
		iStream = null;
		dir = null;
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

	/**
	 * 修改背景图
	 * 
	 * @param uploadHost
	 * @param picUrl
	 */
	private void postUpdateBg(String uploadHost, String picUrl) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.d("responseInfo",responseInfo.result);
						String result = "";
						progress_update_whitebg_campus.setVisibility(View.GONE);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							result = jsonObject.getString("status");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (result.equals("1")) {
							// 把解析到的位图显示出来
							iv_bg.setImageURI(imageUri);
							ToastUtil.showToast(CampusContactsPersonalActivity.this,"背景修改成功");
							new Thread() {
								public void run() {
									try {
										Bitmap bitmap = MyUtils
												.getBitMap(userInfo.getBg());
										if (bitmap != null) {
											saveUserBg(bitmap);
											bitmap.recycle();
											bitmap = null;
										}
									} catch (IOException e) {
										e.printStackTrace();
									}
								};
							}.start();
						} else if (result.equals("0")) {
							ToastUtil.showToast(CampusContactsPersonalActivity.this,"操作失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						ToastUtil.showToast(CampusContactsPersonalActivity.this,"操作失败");
						progress_update_whitebg_campus.setVisibility(View.GONE);
					}
				});
	}

	private void saveUserBg(Bitmap bitmap) throws IOException {
		File file = new File(MyConfig.FILE_URI);
		if (!file.exists()) {
			file.mkdirs();
		}
		File dir = new File(MyConfig.IMAGGE_URI + uid + "timelinebg.png");
		if (!dir.exists()) {
			dir.createNewFile();
		}
		FileOutputStream iStream = new FileOutputStream(dir);
		bitmap.compress(CompressFormat.PNG, 100, iStream);
		iStream.close();
		iStream = null;
		file = null;
		dir = null;
	}

//	@Override
//	protected void onStop() {
//		UserInfoDB.setUserInfo(this, UserInfoDB.CAMPUS_NOREAD_HOME_LOVE, "0");
//		UserInfoDB.setUserInfo(this, UserInfoDB.CAMPUS_NOREAD_HOME_FRIEND, "0");
//		super.onStop();
//	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		FileUtils.deleteFile(new File(fileName));
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// 设置用户头像
//		File file = new File(MyConfig.IMAGGE_URI + uid + "timelinebg.png");
//		if (file.exists()) {
//			iv_bg.setImageURI(Uri.parse(MyConfig.IMAGGE_URI + uid
//					+ "timelinebg.png"));
//		}
		if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CAMPUS_NOREAD_LOVE, "0").equals("0")) {
			iv_circle02.setVisibility(View.VISIBLE);
		} else {
			iv_circle02.setVisibility(View.GONE);
		}
		if (!getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.CAMPUS_NOREAD_FRIEND, "0").equals("0")) {
			iv_circle01.setVisibility(View.VISIBLE);
		} else {
			iv_circle01.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& v.getId() == R.id.ib_add) {
			ib_add.startAnimation(MyAnimation.getScaleAnimationDown());
		}
		return false;
	}

}
