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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CommActiPublishImageAdapter;
import com.loosoo100.campus100.imagepicker.ui.activity.ImagesGridActivity;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.utils.campus.ImageTools;
import com.loosoo100.campus100.view.CustomDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.loosoo100.campus100.R.id.progress;
import static com.loosoo100.campus100.R.id.tv_wordCount;

/**
 * 社团需求发布活动详情
 *
 * @author yang
 */
public class CommNeedPublicDetailActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back; // 返回按钮
    @ViewInject(R.id.btn_ok)
    private Button btn_ok; // 确定

    @ViewInject(R.id.et_provide)
    private EditText et_provide; // 为企业提供
    @ViewInject(R.id.et_summary)
    private EditText et_summary; // 活动介绍

    @ViewInject(R.id.tv_wordCount_provide)
    private TextView tv_wordCount_provide; // 备注字数提示
    @ViewInject(R.id.tv_wordCount_summary)
    private TextView tv_wordCount_summary; // 备注字数提示

    @ViewInject(R.id.listView)
    private ListView listView; // 图片列表

    @ViewInject(R.id.btn_add)
    private Button btn_add; // 添加按钮

    @ViewInject(R.id.tv_picCount)
    private TextView tv_picCount; // 图片数量提示

    private int count;

    private String summary = "";
    private String provide = "";

    // 用于保存图片路径
    private ArrayList<String> list_path = new ArrayList<String>();
    private CommActiPublishImageAdapter adapter;
    // 拍照
    public static final int IMAGE_CAPTURE = 1;
    // 从相册选择
    public static final int IMAGE_SELECT = 2;
    private String fileName = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_comm_need_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        list_path = getIntent().getExtras().getStringArrayList("list_path");
        summary = getIntent().getExtras().getString("summary");
        provide = getIntent().getExtras().getString("provide");

        initView();
        initListView();

        et_summary.setText(summary);
        et_provide.setText(provide);
        tv_wordCount_provide.setText(provide.length() + "/30");
        tv_wordCount_summary.setText(summary.length() + "/800");
        tv_picCount.setText(list_path.size() + "/10");
        if (list_path.size() < 10) {
            btn_add.setVisibility(View.VISIBLE);
        } else {
            btn_add.setVisibility(View.GONE);
        }
    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        /**
         * 监听备注输入字数
         */
        et_summary.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_wordCount_summary.setText(s.length() + "/800");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_provide.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tv_wordCount_provide.setText(s.length() + "/30");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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
            // 返回按钮
            case R.id.rl_back:
                if (!et_summary.getText().toString().trim().equals("")
                        ||!et_provide.getText().toString().trim().equals("")
                        || list_path.size() > 0) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否取消编辑");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    CommNeedPublicDetailActivity.this.finish();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.btn_ok:
                Intent intent = new Intent();
                intent.putStringArrayListExtra("list_path", list_path);
                intent.putExtra("summary", et_summary.getText().toString().trim());
                intent.putExtra("provide", et_provide.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btn_add:
                dialog.show();
                break;

        }
    }

    private void initListView() {
        adapter = new CommActiPublishImageAdapter(this, list_path);
        listView.setAdapter(adapter);
        MyUtils.setListViewHeight(listView, 0);

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                if (position == (list_path == null ? 0 : list_path.size())) {// 点击“+”号位置添加图片
                    return true;
                } else {
                    CustomDialog.Builder builder = new CustomDialog.Builder(
                            CommNeedPublicDetailActivity.this);
                    builder.setMessage("是否删除此图片");
                    builder.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    FileUtils.delFile(list_path.get(position));
                                    list_path.remove(position);
                                    adapter.setList(list_path);
                                    MyUtils.setListViewHeight(listView, 0);
                                    tv_picCount.setText(list_path.size()
                                            + "/10");
                                    if (list_path.size() < 10) {
                                        btn_add.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_add.setVisibility(View.GONE);
                                    }
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
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    /**
     * 从图库中选取图片
     */
    public void selectImage() {
        Intent intent = new Intent(this, ImagesGridActivity.class);
        intent.putExtra("limit", 10 - list_path.size());
        startActivityForResult(intent, IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // TODO
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case IMAGE_CAPTURE:// 拍照返回
                        // 将保存在本地的图片取出并缩小后显示在界面上
                        Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.SDPATH
                                + "/image.jpg");
                        Bitmap newBitmap = MyUtils.ResizeBitmapNoRecycle(bitmap,
                                720);
                        // lists.add(newBitmap);
                        // 生成一个图片文件名
                        fileName = String.valueOf(System.currentTimeMillis());
                        // 将处理过的图片添加到缩略图列表并保存到本地
                        ImageTools.savePhotoToSDCard(newBitmap, FileUtils.SDPATH,
                                fileName);
                        // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                        bitmap.recycle();
                        list_path.add(FileUtils.SDPATH + "/" + fileName + ".jpg");

                        adapter.setList(list_path);
                        MyUtils.setListViewHeight(listView, 0);
                        tv_picCount.setText(list_path.size() + "/10");
                        if (list_path.size() < 10) {
                            btn_add.setVisibility(View.VISIBLE);
                        } else {
                            btn_add.setVisibility(View.GONE);
                        }
                        break;
                    case IMAGE_SELECT:// 选择照片返回
                        // ContentResolver resolver = getContentResolver();
                        // // 照片的原始资源地址ַ
                        // Uri originalUri = data.getData();
                        ArrayList<String> list2 = data.getExtras()
                                .getStringArrayList("picList");
                        // for (int i = 0; i < list2.size(); i++) {
                        // list_path.add(list2.get(i));
                        // }
                        // 更新GrideView
                        // gvAdapter.setList(list_path);
                        // tv_picCount.setText(list_path.size() + "/9");
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
                                    // lists.add(newBitmap2);
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
                        adapter.setList(list_path);
                        MyUtils.setListViewHeight(listView, 0);
                        tv_picCount.setText(list_path.size() + "/10");
                        if (list_path.size() < 10) {
                            btn_add.setVisibility(View.VISIBLE);
                        } else {
                            btn_add.setVisibility(View.GONE);
                        }
                        break;

                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!et_summary.getText().toString().trim().equals("") ||!et_provide.getText().toString().trim().equals("")
                    || list_path.size() > 0) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否取消编辑");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                CommNeedPublicDetailActivity.this.finish();
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
