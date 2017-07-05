package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.loosoo100.campus100.anyevent.MEventBasicFinish;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yang 账户管理、收货地址activity
 */
public class UserOrAddressActivity extends Activity implements OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private View rl_back;

    @ViewInject(R.id.cv_person)
    private CircleView cv_person; // 头像

    @ViewInject(R.id.ll_headShot)
    private LinearLayout ll_headShot; // 头像布局

    @ViewInject(R.id.ll_nickName)
    private LinearLayout ll_nickName; // 昵称布局

    @ViewInject(R.id.tv_nickName)
    private TextView tv_nickName; // 昵称

//	@ViewInject(R.id.ll_telephone)
//	private LinearLayout ll_telephone; // 电话布局

    @ViewInject(R.id.tv_telephone)
    private TextView tv_telephone; // 电话

    @ViewInject(R.id.ll_basicInfo)
    private LinearLayout ll_basicInfo; // 基本信息

    @ViewInject(R.id.ll_address)
    private LinearLayout ll_address; // 收货地址

    @ViewInject(R.id.ll_loginPass)
    private LinearLayout ll_loginPass; // 登陆密码

    @ViewInject(R.id.ll_payPass)
    private LinearLayout ll_payPass; // 支付密码
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    private Intent intent = new Intent();

    private String userID = "";

    private Dialog dialog;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String fileName = MyConfig.FILE_URI + "/userHeadShot.jpg";
    private Uri imageUri;
    private String user_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        ll_headShot.setOnClickListener(this);
        ll_nickName.setOnClickListener(this);
//		ll_telephone.setOnClickListener(this);
        ll_basicInfo.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_loginPass.setOnClickListener(this);
        ll_payPass.setOnClickListener(this);

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
                this.finish();
                break;
            case R.id.ll_headShot:
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
                // 打开图库
                // Intent imageIntent = new Intent(Intent.ACTION_PICK, null);
                // imageIntent.setDataAndType(
                // MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                // MyConfig.IMAGE_UNSPECIFIED);
                // startActivityForResult(imageIntent, MyConfig.ALBUM_REQUEST_CODE);
                break;
            case R.id.ll_nickName:
                intent.setClass(this, NickNameActivity.class);
                startActivity(intent);
                break;
//		case R.id.ll_telephone:
//			intent.setClass(this, PhoneUpdateActivity.class);
//			startActivity(intent);
//			break;
            case R.id.ll_basicInfo:
                intent.setClass(this, BasicInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_address:
                intent.setClass(this, AddressActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.ll_loginPass:
                intent.setClass(this, LoginPWUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_payPass:
                intent.setClass(this, PayPWUpdateActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onResume() {
        String userName = getSharedPreferences(UserInfoDB.USERTABLE,
                MODE_PRIVATE).getString(UserInfoDB.NICK_NAME, "");
        tv_nickName.setText(userName);
        userID = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        File file = new File(MyConfig.IMAGGE_URI + userID + ".png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(MyConfig.IMAGGE_URI
                    + userID + ".png");
            cv_person.setImageBitmap(bitmap);
        }
        String phone = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.PHONE, "");
        tv_telephone.setText(phone);
        super.onResume();
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
                        cv_person.setImageBitmap(bitmap);
                        // iv_picture.setImageURI(imageUri);
                        try {
                            saveUserIcon(bitmap);
                            progress.setVisibility(View.VISIBLE);
                            new Thread() {
                                public void run() {
                                    postData(MyConfig.URL_POST_HEADSHOT,
                                            MyConfig.IMAGGE_URI + userID + ".png");
                                }
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
    }

    private void startCrop(Uri uri) {
        // Intent intent = new Intent("com.android.camera.action.CROP");//
        // 调用Android系统自带的一个图片剪裁页面
        // intent.setDataAndType(uri, MyConfig.IMAGE_UNSPECIFIED);
        // intent.putExtra("crop", "true");// 进行修剪
        // // aspectX aspectY 是宽高的比例
        // intent.putExtra("aspectX", 1);
        // intent.putExtra("aspectY", 1);
        // // outputX outputY 是裁剪图片宽高
        // intent.putExtra("outputX", 300);
        // intent.putExtra("outputY", 300);
        // intent.putExtra("scale", true);// 缩放
        // intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
        // intent.putExtra("return-data", true);
        imageUri = Uri.fromFile(new File(fileName));
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面
        intent.setDataAndType(uri, MyConfig.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
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

    private void saveUserIcon(Bitmap bitmap) throws IOException {
        File file = new File(MyConfig.FILE_URI);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dir = new File(MyConfig.IMAGGE_URI + userID + ".png");
        if (!dir.exists()) {
            dir.createNewFile();
        }
        FileOutputStream iStream = new FileOutputStream(dir);
        bitmap.compress(CompressFormat.PNG, 100, iStream);
        iStream.close();
    }

    /**
     * 修改头像
     *
     * @param uploadHost
     * @param picUrl
     */
    private void postData(final String uploadHost, String picUrl) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", userID);
        params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = "";
                        LogUtils.d("onResponse", responseInfo.result);
                        progress.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                            user_avatar = MyConfig.URL + "app/" + jsonObject.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {

                            String user_name = (String) SharedPreferencesUtils.getParam(UserOrAddressActivity.this, "user_name", "");
                            SharedPreferencesUtils.setParam(UserOrAddressActivity.this, "user_avatar", user_avatar);
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userID, user_name, Uri.parse(user_avatar)));
                            ToastUtil.showToast(UserOrAddressActivity.this, "头像修改成功");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(UserOrAddressActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(UserOrAddressActivity.this, "操作失败");
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    public void onEventMainThread(MEventBasicFinish event) {
        if (event.isFinish()) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        FileUtils.deleteFile(new File(fileName));
        super.onDestroy();
    }
}
