package com.loosoo100.campus100.zzboss.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

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
import com.loosoo100.campus100.activities.BossJoinArgeementActivity;
import com.loosoo100.campus100.alipayapi.AlipayUtil;
import com.loosoo100.campus100.anyevent.MEventAliPay;
import com.loosoo100.campus100.anyevent.MEventWXPay;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.utils.campus.ImageTools;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.wxapi.WXPayUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * @author yang 公司认证
 */
public class BossIdentificationActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private View rl_back;
    @ViewInject(R.id.rl_save)
    private RelativeLayout rl_save;
    @ViewInject(R.id.et_company)
    private EditText et_company;
    @ViewInject(R.id.et_number)
    private EditText et_number;
    @ViewInject(R.id.et_address)
    private EditText et_address;
    @ViewInject(R.id.et_person)
    private EditText et_person;
    @ViewInject(R.id.et_phone01)
    private EditText et_phone01;
    @ViewInject(R.id.et_phone02)
    private EditText et_phone02;
    @ViewInject(R.id.et_host)
    private EditText et_host;
    @ViewInject(R.id.iv_add)
    private ImageView iv_add;
    @ViewInject(R.id.iv_pic)
    private ImageView iv_pic;
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg;
    @ViewInject(R.id.et_frame)
    private EditText et_frame;
    @ViewInject(R.id.btn_property)
    private Button btn_property;
    @ViewInject(R.id.btn_size)
    private Button btn_size;
    @ViewInject(R.id.btn_argeement)
    private Button btn_argeement;
    @ViewInject(R.id.checkBox)
    private CheckBox checkBox;

    private String company = "";
    private String number = "";
    private String address = "";
    private String person = "";
    private String phone01 = "";
    private String phone02 = "";
    private String host = "";
    private String cuid = "";
    private String frame = "";
    private String property = "";
    private String size = "";
    private boolean isCheck = false;

    private Dialog dialog;
    private Dialog dialogProperty;
    private Dialog dialogSize;
    private String fileName = "";
    private String filePath = "";

    private Dialog dialogPay;
    private float money = 1000;
    private String seedOid = "";
    // private boolean isPaying = false;
    private AlipayUtil alipayUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_identification);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        EventBus.getDefault().register(this);

        cuid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.CUSERID, "");

        et_company.setText(getSharedPreferences(UserInfoDB.USERTABLE,
                MODE_PRIVATE).getString(UserInfoDB.COMPANY, ""));

        initView();
        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        dialogPay = new Dialog(this, R.style.MyDialog);
        View viewDialogPay = inflater.inflate(R.layout.dialog_pay_choice, null);
        dialogPay.setContentView(viewDialogPay);
        dialogPay.setContentView(viewDialogPay, params);
        viewDialogPay.findViewById(R.id.ll_pay_weixin).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WXPayUtil wp = new WXPayUtil(
                                BossIdentificationActivity.this, "bail"
                                + seedOid, cuid, "loosoo100", "", money);
                        wp.sendPay();
                        dialogPay.dismiss();
                    }
                });

        viewDialogPay.findViewById(R.id.ll_pay_zhifubao).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alipayUtil = new AlipayUtil(
                                BossIdentificationActivity.this, "loosoo100",
                                "校园100", money + "", cuid, seedOid, "bail");
                        alipayUtil.pay();
                        // isPaying = true;
                        // new Thread() {
                        // public void run() {
                        // while (isPaying) {
                        // if (alipayUtil.isPay == 1) {
                        // isPaying = false;
                        // finish();
                        // }
                        // }
                        // };
                        // }.start();
                        dialogPay.dismiss();
                    }
                });

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
                        btn_size.setText("0-50人");
                        dialogSize.dismiss();
                    }
                });

        viewDialog2.findViewById(R.id.btn_size02).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        size = "50-100人";
                        btn_size.setText("50-100人");
                        dialogSize.dismiss();
                    }
                });

        viewDialog2.findViewById(R.id.btn_size03).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        size = "100人以上";
                        btn_size.setText("100人以上");
                        dialogSize.dismiss();
                    }
                });

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        rl_save.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        iv_pic.setOnClickListener(this);
        btn_property.setOnClickListener(this);
        btn_size.setOnClickListener(this);
        btn_argeement.setOnClickListener(this);
        btn_argeement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (checkBox.isChecked()) {
                    isCheck = true;
                } else {
                    isCheck = false;
                }
            }
        });

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                if (!et_host.getText().toString().trim().equals("")
                        || !et_number.getText().toString().trim().equals("")
                        || !et_person.getText().toString().trim().equals("")
                        || !et_phone01.getText().toString().trim().equals("")
                        || !et_phone02.getText().toString().trim().equals("")
                        || !et_frame.getText().toString().trim().equals("")
                        || !property.equals("") || !size.equals("")) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否退出编辑");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    BossIdentificationActivity.this.finish();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.btn_property:
                dialogProperty.show();
                break;

            case R.id.btn_size:
                dialogSize.show();
                break;

            case R.id.iv_add:
                dialog.show();
                break;

            case R.id.iv_pic:
                dialog.show();
                break;

            case R.id.rl_save:
                company = et_company.getText().toString().trim();
                number = et_number.getText().toString().trim();
                address = et_address.getText().toString().trim();
                person = et_person.getText().toString().trim();
                phone01 = et_phone01.getText().toString().trim();
                phone02 = et_phone02.getText().toString().trim();
                host = et_host.getText().toString().trim();
                frame = et_frame.getText().toString().trim();
                if (company.equals("") || number.equals("") || address.equals("")
                        || person.equals("") || phone01.equals("")
                        || phone02.equals("") || host.equals("")
                        || frame.equals("") || property.equals("")
                        || size.equals("") || !isCheck) {
                    ToastUtil.showToast(this, "请将信息填写完整");
                    return;
                }
                rl_save.setClickable(false);
                progress_update_whitebg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostXUtils(MyConfig.URL_POST_IDENTIFICATION_BOSS,
                                filePath);
                    }

                    ;
                }.start();
                break;

            case R.id.btn_argeement:
                Intent intent = new Intent(this, BossJoinArgeementActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 拍照
     *
     * @param path 照片存放的路径
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
        startActivityForResult(intent, 1);
    }

    /**
     * 从图库中选取图片
     */
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case 1:// 拍照返回
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
                        filePath = FileUtils.SDPATH + "/" + fileName + ".jpg";
                        Glide.with(BossIdentificationActivity.this).load(filePath)
                                .into(iv_pic);
                        iv_pic.setVisibility(View.VISIBLE);
                        // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                        bitmap.recycle();
                        break;

                    case 2:// 选择照片返回
                        ContentResolver resolver = getContentResolver();
                        // 照片的原始资源地址ַ
                        Uri originalUri = data.getData();
                        iv_pic.setImageURI(originalUri);
                        iv_pic.setVisibility(View.VISIBLE);
                        try {
                            // 使用ContentProvider通过URI获取原始图片
                            Bitmap photo = MediaStore.Images.Media.getBitmap(
                                    resolver, originalUri);
                            if (photo != null) {
                                // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                                Bitmap newBitmap2 = MyUtils.ResizeBitmapNoRecycle(
                                        photo, 720);
                                // 生成一个图片文件名
                                fileName = String.valueOf(System
                                        .currentTimeMillis());
                                // 将处理过的图片添加到缩略图列表并保存到本地
                                ImageTools.savePhotoToSDCard(newBitmap2,
                                        FileUtils.SDPATH, fileName);
                                filePath = FileUtils.SDPATH + "/" + fileName
                                        + ".jpg";
                                // 释放原始图片占用的内存，防止out of memory异常发生
                                photo.recycle();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    private void doPostXUtils(String uploadHost, String picUrl) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", company);
        params.addBodyParameter("license", number);
        params.addBodyParameter("address", address);
        params.addBodyParameter("person", person);
        params.addBodyParameter("tel", phone01 + phone02);
        params.addBodyParameter("url", host);
        params.addBodyParameter("cuid", cuid);
        params.addBodyParameter("frame", frame);
        params.addBodyParameter("property", property);
        params.addBodyParameter("size", size);
        if (!picUrl.equals("")) {
            params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        progress_update_whitebg.setVisibility(View.GONE);
                        String result = "";
                        rl_save.setClickable(true);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(BossIdentificationActivity.this, "提交失败");
                        } else {
                            seedOid = result;
                            dialogPay.show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(BossIdentificationActivity.this, "提交失败");
                        rl_save.setClickable(true);
                        progress_update_whitebg.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!et_host.getText().toString().trim().equals("")
                    || !et_number.getText().toString().trim().equals("")
                    || !et_person.getText().toString().trim().equals("")
                    || !et_phone01.getText().toString().trim().equals("")
                    || !et_phone02.getText().toString().trim().equals("")
                    || !et_frame.getText().toString().trim().equals("")
                    || !property.equals("") || !size.equals("")
                    || !filePath.equals("")) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否退出编辑");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                BossIdentificationActivity.this.finish();
                            }
                        });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 支付宝支付成功
     *
     * @param event
     */
    public void onEventMainThread(MEventAliPay event) {
        if (event.isSuccess()) {
            finish();
        }
    }

    /**
     * 微信支付成功
     *
     * @param event
     */
    public void onEventMainThread(MEventWXPay event) {
        if (event.isSuccess()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
