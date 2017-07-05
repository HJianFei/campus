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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yang 注册信息完善activity
 */
public class Register02Activity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_login_bg)
    private ImageView iv_login_bg;
    @ViewInject(R.id.cv_headShot)
    private CircleView cv_headShot;
    @ViewInject(R.id.et_nickName)
    private EditText et_nickName;
    // @ViewInject(R.id.et_school)
    // private EditText et_school;
    @ViewInject(R.id.ll_sex_bg01)
    private LinearLayout ll_sex_bg01;
    @ViewInject(R.id.ll_sex_bg02)
    private LinearLayout ll_sex_bg02;
    @ViewInject(R.id.iv_male)
    private ImageView iv_male;
    @ViewInject(R.id.iv_female)
    private ImageView iv_female;
    @ViewInject(R.id.ll_grade_bg01)
    private LinearLayout ll_grade_bg01;
    @ViewInject(R.id.ll_grade_bg02)
    private LinearLayout ll_grade_bg02;
    @ViewInject(R.id.ll_grade_bg03)
    private LinearLayout ll_grade_bg03;
    @ViewInject(R.id.ll_grade_bg04)
    private LinearLayout ll_grade_bg04;
    @ViewInject(R.id.tv_grade01)
    private TextView tv_grade01;
    @ViewInject(R.id.tv_grade02)
    private TextView tv_grade02;
    @ViewInject(R.id.tv_grade03)
    private TextView tv_grade03;
    @ViewInject(R.id.tv_grade04)
    private TextView tv_grade04;
    @ViewInject(R.id.btn_ok)
    private Button btn_ok;
    @ViewInject(R.id.btn_change)
    private Button btn_change; // 切换账号
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 学校
    @ViewInject(R.id.ll_school)
    private LinearLayout ll_school; // 选择学校
    @ViewInject(R.id.et_invites)
    private EditText et_invites; // 邀请人
    @ViewInject(R.id.progress_update)
    private RelativeLayout progress_update; // 加载动画

    private String uid = ""; // 用户ID
    private String nickName = "";
    private String userEducation = "";
    private String schoolId = "";
    private String schoolName = "";
    private int sex = -1;

    private boolean loadImg = false; // 如果用户没有选择图片则不上传图片
    private String result = "";

    private boolean isFirstExit = true;
    private Dialog dialog;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String fileName = MyConfig.FILE_URI + "/userIcon.jpg";
    private Uri imageUri;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (result.equals("1")) {
                ToastUtil.showToast(Register02Activity.this, "提交成功");
                // 保存用户昵称
                UserInfoDB.setUserInfo(Register02Activity.this,
                        UserInfoDB.NICK_NAME, nickName);
                // 保存用户性别
                UserInfoDB.setUserInfo(Register02Activity.this, UserInfoDB.SEX,
                        sex + "");
                // 保存用户所在学校
                UserInfoDB.setUserInfo(Register02Activity.this,
                        UserInfoDB.SCHOOL, schoolName);
                UserInfoDB.setUserInfo(Register02Activity.this,
                        UserInfoDB.SCHOOL_STORE, schoolName);
                // 保存用户所在学校ID
                UserInfoDB.setUserInfo(Register02Activity.this,
                        UserInfoDB.SCHOOL_ID, schoolId);
                UserInfoDB.setUserInfo(Register02Activity.this,
                        UserInfoDB.SCHOOL_ID_STORE, schoolId);
                // 保存用户学历
                UserInfoDB.setUserInfo(Register02Activity.this,
                        UserInfoDB.GRADE, userEducation);
                progress_update.setVisibility(View.GONE);
                UserInfoDB.setUserInfo(Register02Activity.this, UserInfoDB.ORG,
                        "0");
                Intent intent = new Intent(Register02Activity.this,
                        HomeActivity.class);
                startActivity(intent);
                Register02Activity.this.finish();
            } else if (result.equals("-1")) {
                ToastUtil.showToast(Register02Activity.this, "昵称已存在");
                progress_update.setVisibility(View.GONE);
            } else {
                ToastUtil.showToast(Register02Activity.this, "提交失败");
                progress_update.setVisibility(View.GONE);
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register02);
        ViewUtils.inject(this);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        iv_login_bg
                .setImageBitmap(GetData.getBitMap(this, R.drawable.login_bg));
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
        initView();

    }

    private void initView() {
        ll_sex_bg01.setOnClickListener(this);
        ll_sex_bg02.setOnClickListener(this);
        ll_grade_bg01.setOnClickListener(this);
        ll_grade_bg02.setOnClickListener(this);
        ll_grade_bg03.setOnClickListener(this);
        ll_grade_bg04.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_change.setOnClickListener(this);
        cv_headShot.setOnClickListener(this);
        ll_school.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sex_bg01:
                sex = 0;
                ll_sex_bg01.setBackground(getResources().getDrawable(
                        R.drawable.shape_blue_50cbd8));
                iv_male.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_male_select));
                ll_sex_bg02.setBackground(getResources().getDrawable(
                        R.color.none_color));
                iv_female.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_female_noselect));
                break;

            case R.id.ll_sex_bg02:
                sex = 1;
                ll_sex_bg01.setBackground(getResources().getDrawable(
                        R.color.none_color));
                iv_male.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_male_noselect));
                ll_sex_bg02.setBackground(getResources().getDrawable(
                        R.drawable.shape_pink_ffaeab));
                iv_female.setImageDrawable(getResources().getDrawable(
                        R.drawable.icon_female_select));
                break;

            case R.id.ll_grade_bg01:
                userEducation = "大一";
                resetGrade();
                ll_grade_bg01.setBackground(getResources().getDrawable(
                        R.drawable.shape_yellow_fff45c));
                tv_grade01
                        .setTextColor(getResources().getColor(R.color.red_fd3c49));
                break;

            case R.id.ll_grade_bg02:
                userEducation = "大二";
                resetGrade();
                ll_grade_bg02.setBackground(getResources().getDrawable(
                        R.drawable.shape_yellow_fff45c));
                tv_grade02
                        .setTextColor(getResources().getColor(R.color.red_fd3c49));
                break;

            case R.id.ll_grade_bg03:
                userEducation = "大三";
                resetGrade();
                ll_grade_bg03.setBackground(getResources().getDrawable(
                        R.drawable.shape_yellow_fff45c));
                tv_grade03
                        .setTextColor(getResources().getColor(R.color.red_fd3c49));
                break;

            case R.id.ll_grade_bg04:
                userEducation = "大四";
                resetGrade();
                ll_grade_bg04.setBackground(getResources().getDrawable(
                        R.drawable.shape_yellow_fff45c));
                tv_grade04
                        .setTextColor(getResources().getColor(R.color.red_fd3c49));
                break;

            case R.id.ll_school:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, 8);
                break;

            case R.id.btn_ok:
                nickName = et_nickName.getText().toString().trim();
                if (nickName.equals("") || schoolName.equals("") || sex == -1
                        || userEducation.equals("")) {
                    ToastUtil.showToast(Register02Activity.this, "请将信息完善");
                    return;
                }
                if (!loadImg) {
                    ToastUtil.showToast(Register02Activity.this, "请选择头像");
                    return;
                }
                progress_update.setVisibility(View.VISIBLE);
                // // 设置环信昵称
                // initEaseName(nickName);
                if (et_invites.getText().toString().trim().equals("")) {
                    new Thread() {
                        public void run() {
                            postData(MyConfig.URL_POST_REGISTER_PERFECT,
                                    MyConfig.IMAGGE_URI + uid + ".png");
                        }
                    }.start();
                } else {
                    new Thread() {
                        public void run() {
                            postIsAgentExist(MyConfig.URL_POST_ISAGENTEXITS);
                        }
                    }.start();
                }
                break;

            case R.id.cv_headShot:
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

            case R.id.btn_change:
                Intent changeIntent = new Intent(this, LoginActivity.class);
                startActivity(changeIntent);
                this.finish();
                break;
        }
    }

    // // 设置环信昵称
    // private void initEaseName(final String nickName) {
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(nickName);
    // }
    //
    // }).start();
    //
    // }

    private void resetGrade() {
        ll_grade_bg01.setBackground(getResources().getDrawable(
                R.color.none_color));
        // tv_grade01.setTextColor(Color.WHITE);
        ll_grade_bg02.setBackground(getResources().getDrawable(
                R.color.none_color));
        // tv_grade02.setTextColor(Color.WHITE);
        ll_grade_bg03.setBackground(getResources().getDrawable(
                R.color.none_color));
        // tv_grade03.setTextColor(Color.WHITE);
        ll_grade_bg04.setBackground(getResources().getDrawable(
                R.color.none_color));
        // tv_grade04.setTextColor(Color.WHITE);
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
        // intent.putExtra("return-data", true);
        // intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
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
                    // if (data == null) {
                    // return;
                    // }
                    // Bundle extras = data.getExtras();
                    // if (extras != null) {
                    // Bitmap photo = extras.getParcelable("data");
                    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // photo.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    // try {
                    // cv_headShot.setImageBitmap(photo);
                    // saveUserIcon(photo);
                    // loadImg = true;
                    // } catch (IOException e) {
                    // e.printStackTrace();
                    // }
                    // }
                    if (imageUri != null) {
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);
                        // 把解析到的位图显示出来
                        cv_headShot.setImageBitmap(bitmap);
                        loadImg = true;
                        // iv_picture.setImageURI(imageUri);
                        try {
                            saveUserIcon(bitmap);
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
                case 8:
                    schoolName = data.getExtras().getString(MyConfig.SCHOOL_SEARCH);
                    schoolId = data.getExtras()
                            .getString(MyConfig.SCHOOL_SEARCH_ID);
                    tv_school.setText(schoolName);
                    break;
            }
        }
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

    private void postData(String uploadHost, String picUrl) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("sex", sex + "");
        params.addBodyParameter("userEducation", userEducation);
        params.addBodyParameter("schoolId", schoolId);
        params.addBodyParameter("userName", nickName);
        params.addBodyParameter("schoolName", schoolName);
        params.addBodyParameter("status", "0");
        params.addBodyParameter("imei", MyUtils.getIMEI(this));
        if (!et_invites.getText().toString().trim().equals("")) {
            params.addBodyParameter("agentPhone", et_invites.getText().toString().trim());
        }
        params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(Register02Activity.this, "提交失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    private void postIsAgentExist(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("agentPhone", et_invites.getText().toString().trim());
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        String result = "";
                        progress_update.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            progress_update.setVisibility(View.VISIBLE);
                            new Thread() {
                                public void run() {
                                    postData(MyConfig.URL_POST_REGISTER_PERFECT,
                                            MyConfig.IMAGGE_URI + uid + ".png");
                                }
                            }.start();
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(Register02Activity.this, "推荐人不存在");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(Register02Activity.this, "提交失败");
                        progress_update.setVisibility(View.GONE);
                    }
                });
    }

    private void saveUserIcon(Bitmap bitmap) throws IOException {
        File file = new File(MyConfig.FILE_URI);
        if (!file.exists()) {
            file.mkdirs();
        }
        File dir = new File(MyConfig.IMAGGE_URI + uid + ".png");
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

    /**
     * 监听手机按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断2秒内是否按了两次返回键，如果是则退出应用，否则提示用户再按一次退出程序
            if (isFirstExit) {
                ToastUtil.showToast(Register02Activity.this, "再按一次退出程序");
                isFirstExit = false;
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isFirstExit = true;
                    }

                    ;
                }.start();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
