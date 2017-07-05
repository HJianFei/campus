package com.loosoo100.campus100.chat.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.MyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateGroupActivity extends FragmentActivity implements View.OnClickListener {
    @ViewInject(R.id.rl_back_group_update)
    private RelativeLayout rl_back_group_update;
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty;
    @ViewInject(R.id.img_Group_portrait)
    private ImageView img_Group_portrait;
    @ViewInject(R.id.group_name)
    private EditText group_name;
    @ViewInject(R.id.group_description)
    private EditText group_description;
    @ViewInject(R.id.group_btn_ok)
    private Button group_btn_ok;
    private Dialog dialog;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String fileName = MyConfig.FILE_URI + "/group.jpg";
    private String gid = "";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group);
        ViewUtils.inject(this);
        gid = getIntent().getStringExtra("group_id");
        group_name.setText(getIntent().getStringExtra("group_name"));
        MyUtils.setStatusBarHeight(this, iv_empty);
        img_Group_portrait.setOnClickListener(this);
        group_btn_ok.setOnClickListener(this);
        rl_back_group_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_Group_portrait:
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
                createDialog();
                break;
            case R.id.group_btn_ok:
                String name = group_name.getText().toString().trim();
                String description = group_description.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast(UpdateGroupActivity.this, "群名称不能为空");
                    return;
                }
                updateGroupInfo(name, description, MyConfig.IMAGGE_URI + gid + ".png");
                break;
            case R.id.rl_back_group_update:
                this.finish();
                break;
        }

    }

    private void updateGroupInfo(String name, String description, String filePath) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", gid);
        params.addBodyParameter("name", name);
        params.addBodyParameter("description", description);
        params.addBodyParameter("avatar", new File(filePath), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, MyConfig.UPDATE_GROUP_INFO, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        ToastUtil.showToast(UpdateGroupActivity.this, "更新成功");
                        UpdateGroupActivity.this.finish();

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                        ToastUtil.showToast(UpdateGroupActivity.this, "操作失败，稍后再试");
                        UpdateGroupActivity.this.finish();

                    }
                });
    }


    private void createDialog() {
        dialog = new Dialog(this, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.dialog_camera, null);
        dialog.setContentView(viewDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(viewDialog, params);
        viewDialog.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
                startActivityForResult(intent, MyConfig.CAMERA_REQUEST_CODE);
                dialog.dismiss();
            }
        });

        viewDialog.findViewById(R.id.btn_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_PICK, null);
                imageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(imageIntent, MyConfig.ALBUM_REQUEST_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();
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
                        img_Group_portrait.setImageBitmap(bitmap);
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
        File dir = new File(MyConfig.IMAGGE_URI + gid + ".png");
        if (!dir.exists()) {
            dir.createNewFile();
        }
        FileOutputStream iStream = new FileOutputStream(dir);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, iStream);
        iStream.close();
        iStream = null;
        file = null;
        dir = null;
    }

}
