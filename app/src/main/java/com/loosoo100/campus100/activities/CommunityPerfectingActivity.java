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
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yang 完善社团信息activity
 */
public class CommunityPerfectingActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回
    @ViewInject(R.id.rl_sure)
    private RelativeLayout rl_sure; // 确认
    @ViewInject(R.id.tv_number)
    private TextView tv_number; // 社团号
    @ViewInject(R.id.tv_name)
    private TextView tv_name; // 社团名称
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 所属学校
    @ViewInject(R.id.et_slogan)
    private EditText et_slogan; // 社团口号
    @ViewInject(R.id.et_notice)
    private EditText et_notice; // 社团公告
    @ViewInject(R.id.et_summary)
    private EditText et_summary; // 社团简介
    @ViewInject(R.id.rl_logo)
    private RelativeLayout rl_logo; // 社团logo
    @ViewInject(R.id.cv_logo)
    private CircleView cv_logo; // 社团logo图标
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    private String filePath = ""; // 上传logo文件路径

    private String sn = "";
    private String uid = "";
    private String school = "";
    private String sid = "";
    private String cname = "";
    private String attr = "";
    private String type = "";
    private String dep = "";
    private String uname = "";
    private String phone = "";
    private String weixin = "";
    private String qq = "";
    private String tuijian = "";

    private String slogan = "";
    private String notice = "";
    private String summary = "";

    private Dialog dialog;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String fileName = MyConfig.FILE_URI + "/commIcon.jpg";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_perfecting);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        school = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL, "");

        sn = getIntent().getExtras().getString("sn");
        cname = getIntent().getExtras().getString("cname");
        attr = getIntent().getExtras().getString("attr");
        type = getIntent().getExtras().getString("type");
        dep = getIntent().getExtras().getString("dep");
        uname = getIntent().getExtras().getString("uname");
        phone = getIntent().getExtras().getString("phone");
        weixin = getIntent().getExtras().getString("weixin");
        qq = getIntent().getExtras().getString("qq");
        tuijian = getIntent().getExtras().getString("tuijian");

        initView();

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

    private void initView() {
        rl_back.setOnClickListener(this);
        rl_sure.setOnClickListener(this);
        rl_logo.setOnClickListener(this);

        tv_number.setText(sn + "");
        tv_name.setText(cname + "");
        tv_school.setText(school + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                if (!et_slogan.getText().toString().trim().equals("")
                        || !et_notice.getText().toString().trim().equals("")
                        || !et_summary.getText().toString().trim().equals("")
                        || !filePath.equals("")) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否退出编辑");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    CommunityPerfectingActivity.this.finish();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.rl_sure:
                slogan = et_slogan.getText().toString();
                notice = et_notice.getText().toString();
                summary = et_summary.getText().toString();
                if (slogan.equals("") || notice.equals("") || summary.equals("")
                        || filePath.equals("")) {
                    ToastUtil.showToast(this, "请完善信息后再提交");
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostXUtils(MyConfig.URL_POST_COMMUNITY_CREATE2, filePath);
                    }

                    ;
                }.start();
                break;

            case R.id.rl_logo:
                dialog.show();
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
                    // if (data == null) {
                    // return;
                    // }
                    // Bundle extras = data.getExtras();
                    // if (extras != null) {
                    // Bitmap photo = extras.getParcelable("data");
                    // cv_logo.setImageBitmap(photo);
                    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // photo.compress(Bitmap.CompressFormat.PNG, 80, stream);
                    // try {
                    // saveUserIcon(photo);
                    // } catch (IOException e) {
                    // e.printStackTrace();
                    // }
                    // }
                    if (imageUri != null) {
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);
                        // 把解析到的位图显示出来
                        cv_logo.setImageBitmap(bitmap);
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
        intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
        // 面部识别 这里用不上
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, MyConfig.CROP_REQUEST_CODE);
    }

    private void saveUserIcon(Bitmap bitmap) throws IOException {
        File file = new File(MyConfig.FILE_URI);
        if (!file.exists()) {
            file.mkdirs();
        }
        filePath = MyConfig.IMAGGE_URI + sn + ".png";
        File dir = new File(filePath);
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
     * 上传数据 和 图片
     *
     * @param uploadHost
     * @param picUrl
     */
    private void doPostXUtils(String uploadHost, final String picUrl) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("cname", cname);
        params.addBodyParameter("attr", attr);
        params.addBodyParameter("type", type);
        params.addBodyParameter("dep", dep);
        params.addBodyParameter("uname", uname);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("weixin", weixin);
        params.addBodyParameter("qq", qq);
        params.addBodyParameter("tuijian", tuijian);
        params.addBodyParameter("sn", sn);
        params.addBodyParameter("kouhao", slogan);
        params.addBodyParameter("notice", notice);
        params.addBodyParameter("info", summary);
        params.addBodyParameter("logo", new File(picUrl), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        progress.setVisibility(View.GONE);
                        //创建群组
                        createGroup(uid, cname, et_summary.getText().toString().trim(), tv_number.getText().toString(), picUrl);

                        LogUtils.d("responseInfo", responseInfo.result);
                        ToastUtil.showToast(CommunityPerfectingActivity.this, "提交成功,请耐心等待审核结果");
                        // CommunityCreateActivity.mfinish();
                        CommunityPerfectingActivity.this.finish();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(CommunityPerfectingActivity.this, "提交失败,请重新提交");

                        // rl_sure.setClickable(true);
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 创建群组
     *
     * @param uid
     * @param cname
     * @param summary
     * @param community_id
     * @param filePath
     */
    private void createGroup(String uid, String cname, String summary, String community_id, String filePath) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("name", cname);
        params.addBodyParameter("description", summary);
        params.addBodyParameter("community_id", community_id);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("status", "0");
        params.addBodyParameter("avatar", new File(filePath), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, MyConfig.CREATE_GROUP, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        LogUtils.d("onResponse", "社团群创建成功：" + responseInfo.toString());
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("onResponse", "社团群创建失败：" + arg1);
                    }
                });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!et_slogan.getText().toString().trim().equals("")
                    || !et_notice.getText().toString().trim().equals("")
                    || !et_summary.getText().toString().trim().equals("")
                    || !filePath.equals("")) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否退出编辑");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                CommunityPerfectingActivity.this.finish();
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
