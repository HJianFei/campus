package com.loosoo100.campus100.zzboss.activities;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
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
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.SharedPreferencesUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.zzboss.beans.BossCompanySummaryInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yang 公司简介
 */
public class BossCompanySummaryActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    // @ViewInject(R.id.rl_save)
    // private RelativeLayout rl_save;
    @ViewInject(R.id.btn_save)
    private Button btn_save;
    @ViewInject(R.id.iv_bg)
    private ImageView iv_bg;
    @ViewInject(R.id.cv_logo)
    private CircleView cv_logo;
    @ViewInject(R.id.iv_v)
    private ImageView iv_v;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.tv_count)
    private TextView tv_count;
    @ViewInject(R.id.et_company)
    private EditText et_company;
    @ViewInject(R.id.et_host)
    private EditText et_host;
    @ViewInject(R.id.et_address)
    private EditText et_address;
    @ViewInject(R.id.et_frame)
    private EditText et_frame;
    @ViewInject(R.id.btn_property)
    private Button btn_property;
    @ViewInject(R.id.btn_person)
    private Button btn_person;
    @ViewInject(R.id.et_summary)
    private EditText et_summary;
    @ViewInject(R.id.progress)
    private RelativeLayout progress;
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg;

    private BossCompanySummaryInfo summaryInfo;
    private String cuid = "";

    private Dialog dialog;
    private Dialog dialogProperty;
    private Dialog dialogSize;

    private String company = "";
    private String frame = "";
    private String property = "";
    private String size = "";
    private String host = "";
    private String address = "";
    private String summary = "";
    private String logo = "";

    private Uri imageUri;
    private Uri imageUriLogo;
    private String fileName = MyConfig.FILE_URI + "/company.jpg";
    private String fileNameLogo = MyConfig.FILE_URI + "/companyLogo.jpg";
    private final String IMAGE_UNSPECIFIED = "image/*";
    private boolean isImg = false;
    private boolean isImg2 = false;

    private int picIndex = 0;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            progress.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_company_summary);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");

        rl_back.setOnClickListener(this);
        btn_person.setOnClickListener(this);
        btn_property.setOnClickListener(this);
        iv_bg.setOnClickListener(this);
        cv_logo.setOnClickListener(this);

        setEditable(false);
        et_company.setEnabled(false);
        // rl_save.setVisibility(View.VISIBLE);

        progress.setVisibility(View.VISIBLE);

        File dir = new File(MyConfig.FILE_URI);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File file2 = new File(fileNameLogo);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new Thread() {
            public void run() {
                summaryInfo = GetData
                        .getBossCompanySummaryInfo(MyConfig.URL_JSON_SUMMARY_BOSS
                                + cuid);
                if (summaryInfo != null && !isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        dialogProperty = new Dialog(this, R.style.MyDialog);
        View viewDialog = inflater.inflate(R.layout.dialog_boss_property, null);
        dialogProperty.setContentView(viewDialog);
        dialogProperty.setContentView(viewDialog, params);
        viewDialog.findViewById(R.id.btn_property01).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        property = "国有";
                        btn_property.setText("国有");
                        dialogProperty.dismiss();
                    }
                });
        viewDialog.findViewById(R.id.btn_property02).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        property = "私营";
                        btn_property.setText("私营");
                        dialogProperty.dismiss();
                    }
                });
        viewDialog.findViewById(R.id.btn_property03).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        property = "股份制";
                        btn_property.setText("股份制");
                        dialogProperty.dismiss();
                    }
                });
        viewDialog.findViewById(R.id.btn_property04).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        property = "中外合资";
                        btn_property.setText("中外合资");
                        dialogProperty.dismiss();
                    }
                });

        dialogSize = new Dialog(this, R.style.MyDialog);
        View viewDialog2 = inflater.inflate(R.layout.dialog_boss_size, null);
        dialogSize.setContentView(viewDialog2);
        dialogSize.setContentView(viewDialog2, params);
        viewDialog2.findViewById(R.id.btn_size01).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        size = "0-50人";
                        btn_person.setText("0-50人");
                        dialogSize.dismiss();
                    }
                });

        viewDialog2.findViewById(R.id.btn_size02).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        size = "50-100人";
                        btn_person.setText("50-100人");
                        dialogSize.dismiss();
                    }
                });

        viewDialog2.findViewById(R.id.btn_size03).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        size = "100人以上";
                        btn_person.setText("100人以上");
                        dialogSize.dismiss();
                    }
                });

        dialog = new Dialog(this, R.style.MyDialog);
        View viewDialogCamera = inflater.inflate(R.layout.dialog_camera, null);
        dialog.setContentView(viewDialogCamera);
        dialog.setContentView(viewDialogCamera, params);
        viewDialogCamera.findViewById(R.id.btn_camera).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // captureImage(FileUtils.SDPATH);
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        if (picIndex == 0) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(fileName)));
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(fileNameLogo)));
                        }
                        startActivityForResult(intent,
                                MyConfig.CAMERA_REQUEST_CODE);
                        dialog.dismiss();
                    }
                });

        viewDialogCamera.findViewById(R.id.btn_album).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // selectImage();
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
        if (summaryInfo.getFlag() == 2) {
            iv_v.setImageResource(R.drawable.icon_v_summary);
            et_company.setEnabled(false);
        } else {
            iv_v.setImageResource(R.drawable.icon_v_summary_no);
        }
        btn_save.setOnClickListener(this);
        Glide.with(this).load(summaryInfo.getBgThumb())
                .placeholder(R.drawable.imgloading_big).into(iv_bg);
        if (!summaryInfo.getLogo().equals("")) {
            Glide.with(this).load(summaryInfo.getLogo()).into(cv_logo);
        }

        size = summaryInfo.getSize();
        property = summaryInfo.getProperty();
        company = summaryInfo.getCompany();

        if (!summaryInfo.getMoney().equals("")) {
            tv_money.setText(summaryInfo.getMoney());
        }
        tv_count.setText(summaryInfo.getCount() + "");
        et_company.setText(summaryInfo.getCompany());
        et_host.setText(summaryInfo.getHost());
        et_address.setText(summaryInfo.getAddress());
        et_frame.setText(summaryInfo.getFrame());
        btn_property.setText(summaryInfo.getProperty());
        btn_person.setText(summaryInfo.getSize());
        et_summary.setText(summaryInfo.getSummary());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;

            case R.id.iv_bg:
                picIndex = 0;
                dialog.show();
                break;

            case R.id.cv_logo:
                picIndex = 1;
                dialog.show();
                break;

            case R.id.btn_save:
                if (btn_save.getText().toString().equals("编辑")) {
                    setEditable(true);
                    btn_save.setText("保存");
                    if (summaryInfo != null && summaryInfo.getFlag() != 2) {
                        et_company.setEnabled(true);
                    }
                    return;
                }
                company = et_company.getText().toString().trim();
                frame = et_frame.getText().toString().trim();
                summary = et_summary.getText().toString();
                host = et_host.getText().toString().trim();
                address = et_address.getText().toString().trim();

                if (frame.equals("") || company.equals("") || host.equals("")
                        || address.equals("")) {
                    ToastUtil.showToast(this, "请将信息填写完整");
                    return;
                }
                progress_update_whitebg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostXUtils(MyConfig.URL_POST_SUMMARY_BOSS, fileName,
                                fileNameLogo);
                    }

                    ;
                }.start();
                break;

            case R.id.btn_property:
                dialogProperty.show();
                break;

            case R.id.btn_person:
                dialogSize.show();
                break;

        }
    }

    /**
     * 拍照
     *
     * @param path
     *            照片存放的路径
     */
    // public void captureImage(String path) {
    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
    // File file2 = new File(path);
    // if (!file2.exists()) {
    // file2.mkdirs();
    // }
    // File file = new File(path + "/image.jpg");
    // if (!file.exists()) {
    // try {
    // file.createNewFile();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // Uri uri = Uri.fromFile(file);
    // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    // startActivityForResult(intent, 1);
    // }

    /**
     * 从图库中选取图片
     */
    // public void selectImage() {
    // Intent intent = new Intent();
    // intent.setType("image/*");
    // intent.setAction(Intent.ACTION_PICK);
    // startActivityForResult(intent, 2);
    // }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MyConfig.ALBUM_REQUEST_CODE:
                    if (data == null) {
                        return;
                    }
                    if (picIndex == 0) {
                        startCrop(data.getData());
                    } else {
                        startCropLogo(data.getData());
                    }
                    break;
                case MyConfig.CROP_REQUEST_CODE:
                    if (picIndex == 0) {
                        if (imageUri != null) {
                            Bitmap bitmap = decodeUriAsBitmap(imageUri);
                            // 把解析到的位图显示出来
                            // iv_bg.setImageBitmap(bitmap);
                            iv_bg.setImageURI(imageUri);
                            try {
                                saveImage(bitmap);
                                // Glide.with(this).load(fileName).into(iv_bg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isImg = true;
                            // bitmap.recycle();
                        }
                    } else {
                        if (imageUriLogo != null) {
                            Bitmap bitmap = decodeUriAsBitmap(imageUriLogo);
                            // 把解析到的位图显示出来
                            cv_logo.setImageBitmap(bitmap);
                            // iv_bg.setImageURI(imageUri);
                            try {
                                saveImageLogo(bitmap);
                                // Glide.with(this).load(fileNameLogo).into(cv_logo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isImg2 = true;
                            // bitmap.recycle();
                        }
                    }
                    break;
                case MyConfig.CAMERA_REQUEST_CODE:
                    // 设置文件保存路径
                    if (picIndex == 0) {
                        File picture = new File(fileName);
                        startCrop(Uri.fromFile(picture));
                    } else {
                        File picture = new File(fileNameLogo);
                        startCropLogo(Uri.fromFile(picture));
                    }

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
        intent.putExtra("aspectX", 5);
        intent.putExtra("aspectY", 2);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 1500);
        intent.putExtra("outputY", 600);
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

    private void startCropLogo(Uri uri) {
        imageUriLogo = Uri.fromFile(new File(fileNameLogo));
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
        intent.putExtra("output", imageUriLogo);
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

    private void saveImageLogo(Bitmap bitmap) throws IOException {
        File dir = new File(MyConfig.FILE_URI);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(fileNameLogo);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream iStream = new FileOutputStream(fileNameLogo);
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

    private void doPostXUtils(String uploadHost, String picUrl, String picUrl2) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", company);
        params.addBodyParameter("url", host);
        params.addBodyParameter("address", address);
        params.addBodyParameter("frame", frame);
        params.addBodyParameter("property", property);
        params.addBodyParameter("size", size);
        params.addBodyParameter("profile", summary);
        params.addBodyParameter("cuid", cuid);
        if (isImg) {
            params.addBodyParameter("pic1", new File(picUrl), "image/jpeg");
        }
        if (isImg2) {
            params.addBodyParameter("pic2", new File(picUrl2), "image/jpeg");
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progress_update_whitebg.setVisibility(View.GONE);
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        btn_save.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                            logo = MyConfig.PIC_AVATAR + jsonObject.getString("logo");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("")) {
                            ToastUtil.showToast(BossCompanySummaryActivity.this, "保存失败");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(BossCompanySummaryActivity.this, "保存失败");
                        } else {


                            String company_name = (String) SharedPreferencesUtils.getParam(BossCompanySummaryActivity.this, "company_name", "");
                            SharedPreferencesUtils.setParam(BossCompanySummaryActivity.this, "company_avatar", logo);
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo("cmp" + cuid, company_name, Uri.parse(logo)));
                            ToastUtil.showToast(BossCompanySummaryActivity.this, "保存成功");
                            BossCompanySummaryActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(BossCompanySummaryActivity.this, "保存失败");
                        btn_save.setClickable(true);
                        progress_update_whitebg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 设置是否可编辑
     *
     * @param editable
     */
    private void setEditable(boolean editable) {
        iv_bg.setClickable(editable);
        cv_logo.setClickable(editable);
        et_host.setEnabled(editable);
        et_address.setEnabled(editable);
        et_frame.setEnabled(editable);
        btn_property.setClickable(editable);
        btn_person.setClickable(editable);
        et_summary.setEnabled(editable);
    }

}
