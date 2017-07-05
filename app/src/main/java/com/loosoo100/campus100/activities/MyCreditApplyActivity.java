package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yang 申请借款activity
 */
public class MyCreditApplyActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.ll_purpose_borrow)
	private LinearLayout ll_purpose_borrow; // 借款用途
	@ViewInject(R.id.ll_contacts)
	private LinearLayout ll_contacts; // 获取联系人
	@ViewInject(R.id.btn_ok)
	private Button btn_ok; // 提交按钮

	@ViewInject(R.id.tv_purpose)
	private TextView tv_purpose; // 用途
	@ViewInject(R.id.et_name)
	private EditText et_name; // 姓名
	@ViewInject(R.id.et_phone)
	private EditText et_phone; // 手机号
	@ViewInject(R.id.et_id)
	private EditText et_id; // 身份证
	@ViewInject(R.id.et_school)
	private EditText et_school; // 学校
	@ViewInject(R.id.et_studentID)
	private EditText et_studentID; // 学生证

	@ViewInject(R.id.ib_01)
	private ImageButton ib_01;
	@ViewInject(R.id.ib_02)
	private ImageButton ib_02;
	@ViewInject(R.id.ib_03)
	private ImageButton ib_03;
	@ViewInject(R.id.ib_04)
	private ImageButton ib_04;
	@ViewInject(R.id.tv_contacts)
	private TextView tv_contacts; // 紧急联系人

	@ViewInject(R.id.progress_update_blackbg)
	private RelativeLayout progress_update_blackbg; // 加载动画

	private List<String> imgList = new ArrayList<String>();

	private String img01 = MyConfig.FILE_URI + "/001.jpg";
	private String img02 = MyConfig.FILE_URI + "/002.jpg";
	private String img03 = MyConfig.FILE_URI + "/003.jpg";
	private String img04 = MyConfig.FILE_URI + "/004.jpg";
	private String img011 = MyConfig.FILE_URI + "/0011.jpg";
	private String img022 = MyConfig.FILE_URI + "/0022.jpg";
	private String img033 = MyConfig.FILE_URI + "/0033.jpg";
	private String img044 = MyConfig.FILE_URI + "/0044.jpg";

	private String purpose = "";
	private String name = "";
	private String phone = "";
	private String id = "";
	private String school = "";
	private String studentID = "";
	private String contacts = "";
	// private String rootPath = "";
	private String uid = "";
	private Bitmap resizeBitmap1;
	private Bitmap resizeBitmap2;
	private Bitmap resizeBitmap3;
	private Bitmap resizeBitmap4;

	// 判断图片是否有选择
	private boolean isImg01 = false;
	private boolean isImg02 = false;
	private boolean isImg03 = false;
	private boolean isImg04 = false;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					isImg01 = true;
					Uri u = Uri.parse(android.provider.MediaStore.Images.Media
							.insertImage(getContentResolver(), img011, null,
									null));
					ib_01.setImageURI(u);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				break;

			case 2:
				try {
					isImg02 = true;
					Uri u = Uri.parse(android.provider.MediaStore.Images.Media
							.insertImage(getContentResolver(), img022, null,
									null));
					ib_02.setImageURI(u);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				break;

			case 3:
				try {
					isImg03 = true;
					Uri u = Uri.parse(android.provider.MediaStore.Images.Media
							.insertImage(getContentResolver(), img033, null,
									null));
					ib_03.setImageURI(u);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				break;

			case 4:
				try {
					isImg04 = true;
					Uri u = Uri.parse(android.provider.MediaStore.Images.Media
							.insertImage(getContentResolver(), img044, null,
									null));
					ib_04.setImageURI(u);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycredit_apply);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		// 不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getString(UserInfoDB.USERID, "");

		initView();

	}

	private void initView() {
		rl_back.setOnClickListener(this);
		ll_purpose_borrow.setOnClickListener(this);
		ll_contacts.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		ib_01.setOnClickListener(this);
		ib_02.setOnClickListener(this);
		ib_03.setOnClickListener(this);
		ib_04.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		// 借贷用途
		case R.id.ll_purpose_borrow:
			Intent intent = new Intent(this, MyCreditPurposeActivity.class);
			intent.putExtra("purpose", purpose);
			startActivityForResult(intent, 0);
			break;

		// 获取联系人
		case R.id.ll_contacts:
			Intent contactsIntent = new Intent(Intent.ACTION_PICK,
					android.provider.ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(contactsIntent, 1);
			break;

		// 身份证正面
		case R.id.ib_01:
			Intent iDFrontIntent = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			iDFrontIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img01)));
			startActivityForResult(iDFrontIntent, 2);
			break;

		// 身份证反面
		case R.id.ib_02:
			Intent IDBackgroundIntent = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			IDBackgroundIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img02)));
			startActivityForResult(IDBackgroundIntent, 3);
			break;

		// 手持证件照
		case R.id.ib_03:
			Intent stuIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			stuIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img03)));
			startActivityForResult(stuIntent, 4);
			break;

		// 学生证
		case R.id.ib_04:
			Intent cardIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			cardIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(img04)));
			startActivityForResult(cardIntent, 5);
			break;

		// 提交
		case R.id.btn_ok:
			name = et_name.getText().toString();
			phone = et_phone.getText().toString();
			id = et_id.getText().toString();
			school = et_school.getText().toString();
			studentID = et_studentID.getText().toString();
			if (name.equals("") || phone.equals("") || id.equals("")
					|| school.equals("") || studentID.equals("") || !isImg01
					|| !isImg02 || !isImg03 || !isImg04 || contacts.equals("")
					|| purpose.equals("")) {
				ToastUtil.showToast(MyCreditApplyActivity.this,"请将信息填写完整");
				return;
			}
			progress_update_blackbg.setVisibility(View.VISIBLE);
			imgList.add(img011);
			imgList.add(img022);
			imgList.add(img033);
			imgList.add(img044);

			new Thread() {
				public void run() {
					postDataXutils(MyConfig.URL_POST_CREDIT, imgList);
				}
			}.start();
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 2) {
			resizeBitmap1 = MyUtils.ResizeBitmap(
					BitmapFactory.decodeFile(img01), 720);
			new Thread() {
				public void run() {
					try {
						saveUserIcon(resizeBitmap1, img011);
						Message message = Message.obtain();
						message.what = 1;
						handler.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		if (resultCode == RESULT_OK && requestCode == 3) {
			resizeBitmap2 = MyUtils.ResizeBitmap(
					BitmapFactory.decodeFile(img02), 720);
			new Thread() {
				public void run() {
					try {
						saveUserIcon(resizeBitmap2, img022);
						Message message = Message.obtain();
						message.what = 2;
						handler.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		if (resultCode == RESULT_OK && requestCode == 4) {
			resizeBitmap3 = MyUtils.ResizeBitmap(
					BitmapFactory.decodeFile(img03), 720);
			new Thread() {
				public void run() {
					try {
						saveUserIcon(resizeBitmap3, img033);
						Message message = Message.obtain();
						message.what = 3;
						handler.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		if (resultCode == RESULT_OK && requestCode == 5) {
			resizeBitmap4 = MyUtils.ResizeBitmap(
					BitmapFactory.decodeFile(img04), 720);
			new Thread() {
				public void run() {
					try {
						saveUserIcon(resizeBitmap4, img044);
						Message message = Message.obtain();
						message.what = 4;
						handler.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		if (resultCode == RESULT_OK && requestCode == 1) {
			Uri contactData = data.getData();
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			contacts = this.getContactPhone(cursor);
			tv_contacts.setText(contacts);
		}
		if (resultCode == RESULT_OK && requestCode == 0) {
			purpose = (String) data.getExtras().get("purpose");
			tv_purpose.setText(purpose);
		}
//		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取联系人号码
	 * 
	 * @param cursor
	 * @return
	 */
	private String getContactPhone(Cursor cursor) {
		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String result = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人电话的cursor
			Cursor phone = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
							+ contactId, null, null);
			if (phone.moveToFirst()) {
				for (; !phone.isAfterLast(); phone.moveToNext()) {
					int index = phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					String phoneNumber = phone.getString(index);
					result = phoneNumber;
				}
				if (!phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}

	private void saveUserIcon(Bitmap bitmap, String imgPath) throws IOException {
		File file = new File(MyConfig.FILE_URI);
		if (!file.exists()) {
			file.mkdirs();
		}
		File dir = new File(imgPath);
		if (!dir.exists()) {
			dir.createNewFile();
		}
		FileOutputStream iStream = new FileOutputStream(dir);
		bitmap.compress(CompressFormat.JPEG, 80, iStream);
		iStream.close();
		iStream = null;
		// file = null;
		dir = null;
	}

	private void postDataXutils(String uploadHost, List<String> list) {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("purpose", purpose);
		params.addBodyParameter("username", name);
		params.addBodyParameter("phone", phone);
		params.addBodyParameter("cardid", id);
		params.addBodyParameter("userid", uid);
		params.addBodyParameter("school", school);
		params.addBodyParameter("studentid", studentID);
		params.addBodyParameter("emphone", contacts);
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
						ToastUtil.showToast(MyCreditApplyActivity.this,"申请提交成功，等待审核");
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						MyCreditApplyActivity.this.finish();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("error",arg1.toString());
						progress_update_blackbg.setVisibility(View.GONE);
						ToastUtil.showToast(MyCreditApplyActivity.this,"提交失败");
					}
				});
	}

}
