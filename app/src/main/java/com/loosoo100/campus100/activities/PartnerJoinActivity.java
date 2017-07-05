package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.loosoo100.campus100.adapters.PartnerJoinListAdapter;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.utils.campus.ImageTools;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.MyScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yang 校园拍档申请入驻activity
 */
public class PartnerJoinActivity extends Activity implements OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;

    @ViewInject(R.id.btn_ok)
    private Button btn_ok;
    @ViewInject(R.id.btn_argeement)
    private Button btn_argeement;
    @ViewInject(R.id.btn_province)
    private Button btn_province;
    @ViewInject(R.id.btn_city)
    private Button btn_city;
    @ViewInject(R.id.btn_school)
    private Button btn_school;
    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.rb_Leader)
    private RadioButton rb_Leader;
    @ViewInject(R.id.rb_market)
    private RadioButton rb_market;
    @ViewInject(R.id.ll_leader)
    private LinearLayout ll_leader;
    @ViewInject(R.id.ll_store)
    private LinearLayout ll_store;
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.et_weixin)
    private EditText et_weixin;
    @ViewInject(R.id.et_email)
    private EditText et_email;
    @ViewInject(R.id.et_bussinessNum)
    private EditText et_bussinessNum;
    @ViewInject(R.id.et_id)
    private EditText et_id;
    @ViewInject(R.id.et_address)
    private EditText et_address;
    @ViewInject(R.id.ib_01)
    private ImageButton ib_01;
    @ViewInject(R.id.ib_02)
    private ImageButton ib_02;
    @ViewInject(R.id.ib_03)
    private ImageButton ib_03;
    @ViewInject(R.id.ib_04)
    private ImageButton ib_04;
    @ViewInject(R.id.listview)
    private ListView listview;
    @ViewInject(R.id.rl_listview)
    private RelativeLayout rl_listview;
    @ViewInject(R.id.rl_black)
    private RelativeLayout rl_black;
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg;
    @ViewInject(R.id.scroll)
    private MyScrollView scroll;
    @ViewInject(R.id.checkBox)
    private CheckBox checkBox;
    @ViewInject(R.id.et_id02)
    private EditText et_id02;//负责人版
    @ViewInject(R.id.et_name02)
    private EditText et_name02;    //负责人版
    @ViewInject(R.id.et_phone02)
    private EditText et_phone02;//负责人版
    @ViewInject(R.id.et_weixin02)
    private EditText et_weixin02;//负责人版
    @ViewInject(R.id.et_email02)
    private EditText et_email02;//负责人版
    @ViewInject(R.id.et_bussinessNum02)
    private EditText et_bussinessNum02;//负责人版
    @ViewInject(R.id.ib_0102)
    private ImageButton ib_0102;//负责人版
    @ViewInject(R.id.ib_0202)
    private ImageButton ib_0202;//负责人版
    @ViewInject(R.id.ib_0302)
    private ImageButton ib_0302;//负责人版
    @ViewInject(R.id.ib_0402)
    private ImageButton ib_0402;//负责人版
    @ViewInject(R.id.checkBox02)
    private CheckBox checkBox02;
    @ViewInject(R.id.btn_argeement02)
    private Button btn_argeement02;
    @ViewInject(R.id.btn_ok02)
    private Button btn_ok02;

    private List<String> imgList = new ArrayList<String>();
    private List<CampusInfo> cityList = new ArrayList<CampusInfo>();

    private String img01 = "";
    private String img02 = "";
    private String img03 = "";
    private String img04 = "";
    private String img05 = "";
    private String img06 = "";
    private String img07 = "";
    private String img08 = "";

    private String type = "0"; // 申请类型 0负责人1校园超市
    private String province = ""; // 省份
    private String city = ""; // 市
    private String school = ""; // 学校
    private String name = ""; // 姓名
    private String phone = ""; // 电话
    private String weixin = ""; // 微信
    private String email = ""; // 邮件
    private String businessNum = ""; // 执照编号
    private String idCard = ""; // 身份证号
    private String address = ""; // 地址
    private String uid = ""; // 用户ID
    private String msid = ""; // 负责人学校ID

    private Bitmap resizeBitmap1;
    private Bitmap resizeBitmap2;
    private Bitmap resizeBitmap3;
    private Bitmap resizeBitmap4;

    private boolean isProvince = true;
    private String provinceId = "";
    private String cityId = "";
    private String sid = "";
    // 判断图片是否有选择
    private boolean isImg01 = false;
    private boolean isImg02 = false;
    private boolean isImg03 = false;
    private boolean isImg04 = false;

    private boolean isImg05 = false;
    private boolean isImg06 = false;
    private boolean isImg07 = false;
    private boolean isImg08 = false;

    // 用于保存图片路径(超市)
//    private ArrayList<String> list_path_store = new ArrayList<String>();
    // 用于保存图片路径(负责人)
//    private ArrayList<String> list_path_leader = new ArrayList<String>();
    // 拍照
    public static final int IMAGE_CAPTURE = 88;
    private String fileName = "";
    private int position;   //点击哪个按钮

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (cityList != null && cityList.size() > 0) {
                initListView();
                rl_listview.setVisibility(View.VISIBLE);
            }
            progress_update_whitebg.setVisibility(View.GONE);
        }

        ;
    };
//    private Handler handler2 = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 1:
//                    try {
//                        isImg01 = true;
//                        Uri u = Uri.parse(android.provider.MediaStore.Images.Media
//                                .insertImage(getContentResolver(), img011, null,
//                                        null));
//                        ib_01.setImageURI(u);
//                    } catch (FileNotFoundException e1) {
//                        e1.printStackTrace();
//                    }
//                    break;
//
//                case 2:
//                    try {
//                        isImg02 = true;
//                        Uri u = Uri.parse(android.provider.MediaStore.Images.Media
//                                .insertImage(getContentResolver(), img022, null,
//                                        null));
//                        ib_02.setImageURI(u);
//                    } catch (FileNotFoundException e1) {
//                        e1.printStackTrace();
//                    }
//                    break;
//
//                case 3:
//                    try {
//                        isImg03 = true;
//                        Uri u = Uri.parse(android.provider.MediaStore.Images.Media
//                                .insertImage(getContentResolver(), img033, null,
//                                        null));
//                        ib_03.setImageURI(u);
//                    } catch (FileNotFoundException e1) {
//                        e1.printStackTrace();
//                    }
//                    break;
//
//                case 4:
//                    try {
//                        isImg04 = true;
//                        Uri u = Uri.parse(android.provider.MediaStore.Images.Media
//                                .insertImage(getContentResolver(), img044, null,
//                                        null));
//                        ib_04.setImageURI(u);
//                    } catch (FileNotFoundException e1) {
//                        e1.printStackTrace();
//                    }
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_join);

        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        // 不自动弹出软键盘
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        msid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        rl_black.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_ok.setClickable(false);
        btn_ok.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.shape_gray_b3b3b3));
        btn_ok02.setOnClickListener(this);
        btn_ok02.setClickable(false);
        btn_ok02.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.shape_gray_b3b3b3));
        ib_01.setOnClickListener(this);
        ib_02.setOnClickListener(this);
        ib_03.setOnClickListener(this);
        ib_04.setOnClickListener(this);
        ib_0102.setOnClickListener(this);
        ib_0202.setOnClickListener(this);
        ib_0302.setOnClickListener(this);
        ib_0402.setOnClickListener(this);
        btn_province.setOnClickListener(this);
        btn_city.setOnClickListener(this);
        btn_school.setOnClickListener(this);
        btn_argeement.setOnClickListener(this);
        btn_argeement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_argeement02.setOnClickListener(this);
        btn_argeement02.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (checkBox.isChecked() && type.equals("1")) {
                    btn_ok.setClickable(true);
                    btn_ok.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.selector_btn_red));
                } else {
                    btn_ok.setClickable(false);
                    btn_ok.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.shape_gray_b3b3b3));
                }
            }
        });

        checkBox02.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (checkBox02.isChecked() && type.equals("0")) {
                    btn_ok02.setClickable(true);
                    btn_ok02.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.selector_btn_red));
                } else {
                    btn_ok02.setClickable(false);
                    btn_ok02.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.shape_gray_b3b3b3));
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_Leader.getId()) {
                    rb_Leader.setChecked(true);
                    type = "0";
                    ll_leader.setVisibility(View.VISIBLE);
                    ll_store.setVisibility(View.GONE);
                } else if (checkedId == rb_market.getId()) {
                    rb_market.setChecked(true);
                    type = "1";
                    ll_store.setVisibility(View.VISIBLE);
                    ll_leader.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                if (!et_name.getText().toString().trim().equals("")
                        || !et_phone.getText().toString().trim().equals("")
                        || !et_weixin.getText().toString().trim().equals("")
                        || !et_email.getText().toString().trim().equals("")
                        || !et_bussinessNum.getText().toString().trim().equals("")
                        || !et_id.getText().toString().trim().equals("")
                        || !et_name02.getText().toString().trim().equals("")
                        || !et_phone02.getText().toString().trim().equals("")
                        || !et_weixin02.getText().toString().trim().equals("")
                        || !et_email02.getText().toString().trim().equals("")
                        || !et_bussinessNum02.getText().toString().trim().equals("")
                        || !et_id02.getText().toString().trim().equals("")
                        || !et_address.getText().toString().trim().equals("")
                        || isImg01 || isImg02 || isImg03 || isImg04 || isImg05 || isImg06 || isImg07 || isImg08
                        || !provinceId.equals("") || !cityId.equals("")
                        || !sid.equals("")) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否退出编辑");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    PartnerJoinActivity.this.finish();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.rl_black:
                rl_listview.setVisibility(View.GONE);
                break;

            case R.id.btn_argeement:
                Intent intent = new Intent(this, JoinArgeementActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_argeement02:
                Intent intent02 = new Intent(this, LeaderArgeementActivity.class);
                startActivity(intent02);
                break;

            case R.id.btn_province:
                isProvince = true;
                progress_update_whitebg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        cityList = GetData.getCityInfos(MyConfig.URL_JSON_PROVINCE);
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }.start();
                break;

            case R.id.btn_city:
                isProvince = false;
                if (provinceId.equals("")) {
                    return;
                }
                progress_update_whitebg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        cityList = GetData.getCityInfos(MyConfig.URL_JSON_CITY
                                + provinceId);
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }.start();
                break;

            case R.id.btn_school:
                Intent intent2 = new Intent(this, SearchActivity.class);
                startActivityForResult(intent2, 5);
                break;

            case R.id.ib_01:
//                Intent intent01 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent01.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img01)));
//                startActivityForResult(intent01, 1);
                position = 5;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_02:
//                Intent intent02 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent02.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img02)));
//                startActivityForResult(intent02, 2);
                position = 6;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_03:
//                Intent intent03 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent03.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img03)));
//                startActivityForResult(intent03, 3);
                position = 7;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_04:
//                Intent intent04 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent04.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img04)));
//                startActivityForResult(intent04, 4);
                position = 8;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_0102:
//                Intent intent01 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent01.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img01)));
//                startActivityForResult(intent01, 1);
                position = 1;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_0202:
//                Intent intent02 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent02.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img02)));
//                startActivityForResult(intent02, 2);
                position = 2;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_0302:
//                Intent intent03 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent03.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img03)));
//                startActivityForResult(intent03, 3);
                position = 3;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.ib_0402:
//                Intent intent04 = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent04.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(img04)));
//                startActivityForResult(intent04, 4);
                position = 4;
                captureImage(FileUtils.SDPATH);
                break;

            case R.id.btn_ok:
                name = et_name.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                weixin = et_weixin.getText().toString().trim();
                email = et_email.getText().toString().trim();
                businessNum = et_bussinessNum.getText().toString().trim();
                idCard = et_id.getText().toString().trim();
                address = et_address.getText().toString().trim();
                if (name.equals("") || phone.equals("") || weixin.equals("")
                        || idCard.equals("") || address.equals("") || !isImg05
                        || !isImg07 || !isImg08 || provinceId.equals("")
                        || cityId.equals("") || sid.equals("")) {
                    ToastUtil.showToast(PartnerJoinActivity.this, "请将信息填写完整");
                    return;
                }
                progress_update_whitebg.setVisibility(View.VISIBLE);
                imgList.clear();
                imgList.add(img05);
                if (isImg06) {
                    imgList.add(img06);
                } else {
                    imgList.add("");
                }
                imgList.add(img07);
                imgList.add(img08);
                new Thread() {
                    public void run() {
                        postDataStore(MyConfig.URL_POST_CAMPUS_PARTNER, imgList);
                    }
                }.start();
                break;


            case R.id.btn_ok02:
                name = et_name02.getText().toString().trim();
                phone = et_phone02.getText().toString().trim();
                weixin = et_weixin02.getText().toString().trim();
                email = et_email02.getText().toString().trim();
                businessNum = et_bussinessNum02.getText().toString().trim();
                idCard = et_id02.getText().toString().trim();
                if (name.equals("") || phone.equals("") || weixin.equals("") || businessNum.equals("")
                        || idCard.equals("") || !isImg01
                        || !isImg02 || !isImg03) {
                    ToastUtil.showToast(PartnerJoinActivity.this, "请将信息填写完整");
                    return;
                }
                progress_update_whitebg.setVisibility(View.VISIBLE);
                imgList.clear();
                imgList.add(img01);
                imgList.add(img02);
                imgList.add(img03);
                if (isImg04) {
                    imgList.add(img04);
                } else {
                    imgList.add("");
                }

                new Thread() {
                    public void run() {
                        postDataLeader(MyConfig.URL_POST_CAMPUS_LEADER, imgList);
                    }
                }.start();
                break;

        }
    }

    private void initListView() {
        listview.setAdapter(new PartnerJoinListAdapter(this, cityList));
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                rl_listview.setVisibility(View.GONE);
                if (isProvince) {
                    provinceId = cityList.get(position).getCampusID();
                    btn_province
                            .setText(cityList.get(position).getCampusName());
                    cityId = "";
                    btn_city.setText("请选择");
                } else {
                    cityId = cityList.get(position).getCampusID();
                    btn_city.setText(cityList.get(position).getCampusName());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == 1) {
//            resizeBitmap1 = MyUtils.ResizeBitmap(
//                    BitmapFactory.decodeFile(img01), 720);
//            new Thread() {
//                public void run() {
//                    try {
//                        saveUserIcon(resizeBitmap1, img011);
//                        Message message = Message.obtain();
//                        message.what = 1;
//                        handler2.sendMessage(message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ;
//            }.start();
//        }
//        if (resultCode == RESULT_OK && requestCode == 2) {
//            resizeBitmap2 = MyUtils.ResizeBitmap(
//                    BitmapFactory.decodeFile(img02), 720);
//            new Thread() {
//                public void run() {
//                    try {
//                        saveUserIcon(resizeBitmap2, img022);
//                        Message message = Message.obtain();
//                        message.what = 2;
//                        handler2.sendMessage(message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ;
//            }.start();
//        }
//        if (resultCode == RESULT_OK && requestCode == 3) {
//            resizeBitmap3 = MyUtils.ResizeBitmap(
//                    BitmapFactory.decodeFile(img03), 720);
//            new Thread() {
//                public void run() {
//                    try {
//                        saveUserIcon(resizeBitmap3, img033);
//                        Message message = Message.obtain();
//                        message.what = 3;
//                        handler2.sendMessage(message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ;
//            }.start();
//        }
//        if (resultCode == RESULT_OK && requestCode == 4) {
//            resizeBitmap4 = MyUtils.ResizeBitmap(
//                    BitmapFactory.decodeFile(img04), 720);
//            new Thread() {
//                public void run() {
//                    try {
//                        saveUserIcon(resizeBitmap4, img044);
//                        Message message = Message.obtain();
//                        message.what = 4;
//                        handler2.sendMessage(message);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ;
//            }.start();
//        }
//        if (resultCode == RESULT_OK && requestCode == 5) {
//            school = data.getExtras().getString(MyConfig.SCHOOL_SEARCH);
//            sid = data.getExtras().getString(MyConfig.SCHOOL_SEARCH_ID);
//            btn_school.setText(school);
//        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAPTURE:// 拍照返回
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
                    switch (position) {
                        case 1:
                            isImg01 = true;
                            ib_0102.setImageBitmap(newBitmap);
                            img01 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 2:
                            isImg02 = true;
                            ib_0202.setImageBitmap(newBitmap);
                            img02 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 3:
                            isImg03 = true;
                            ib_0302.setImageBitmap(newBitmap);
                            img03 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 4:
                            isImg04 = true;
                            ib_0402.setImageBitmap(newBitmap);
                            img04 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 5:
                            isImg05 = true;
                            ib_01.setImageBitmap(newBitmap);
                            img05 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 6:
                            isImg06 = true;
                            ib_02.setImageBitmap(newBitmap);
                            img06 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 7:
                            isImg07 = true;
                            ib_03.setImageBitmap(newBitmap);
                            img07 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                        case 8:
                            isImg08 = true;
                            ib_04.setImageBitmap(newBitmap);
                            img08 = FileUtils.SDPATH + "/" + fileName + ".jpg";
                            break;
                    }

                    // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
//                    list_path_store.add(FileUtils.SDPATH + "/" + fileName + ".jpg");
                    break;

                case 5:
                    school = data.getExtras().getString(MyConfig.SCHOOL_SEARCH);
                    sid = data.getExtras().getString(MyConfig.SCHOOL_SEARCH_ID);
                    btn_school.setText(school);
                    break;
            }
        }
    }

    private void postDataStore(String uploadHost, List<String> list) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("status", "1");
        params.addBodyParameter("userId", uid);
        params.addBodyParameter("areaId1", provinceId);
        params.addBodyParameter("areaId2", cityId);
        params.addBodyParameter("schoolid", sid);
        params.addBodyParameter("userName", name);
        params.addBodyParameter("shopTel", phone);
        params.addBodyParameter("weixinNo", weixin);
        params.addBodyParameter("emial", email);
        params.addBodyParameter("permitNo", businessNum);
        params.addBodyParameter("identityNo", idCard);
        params.addBodyParameter("shopAddress", address);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals("")) {
                params.addBodyParameter("pic" + (i + 1), new File(list.get(i)),
                        "image/jpeg");
            }
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_whitebg.setVisibility(View.GONE);
                        String result = "";
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("-1")) {
                            ToastUtil.showToast(PartnerJoinActivity.this, "您已创建过社团或超市,申请失败");
                            return;
                        }
                        ToastUtil.showToast(PartnerJoinActivity.this, "申请提交成功，等待审核");
                        PartnerJoinActivity.this.finish();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_whitebg.setVisibility(View.GONE);
                        ToastUtil.showToast(PartnerJoinActivity.this, "提交失败");
                    }
                });
    }

    private void postDataLeader(String uploadHost, List<String> list) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sid", msid);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("name", name);
        params.addBodyParameter("tel", phone);
        params.addBodyParameter("weixin", weixin);
        params.addBodyParameter("email", email);
        params.addBodyParameter("studentNo", businessNum);
        params.addBodyParameter("identityNo", idCard);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals("")) {
                params.addBodyParameter("pic" + (i + 1), new File(list.get(i)),
                        "image/jpeg");
            }
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        progress_update_whitebg.setVisibility(View.GONE);
                        int result = 0;
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(responseInfo.result);
                            result = jsonObject.optInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result == 1) {
                            ToastUtil.showToast(PartnerJoinActivity.this, "您已提交过");
                            return;
                        } else if (result == 2) {
                            ToastUtil.showToast(PartnerJoinActivity.this, "您已是校园负责人");
                            return;
                        }
                        ToastUtil.showToast(PartnerJoinActivity.this, "申请提交成功，等待审核");
                        PartnerJoinActivity.this.finish();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        progress_update_whitebg.setVisibility(View.GONE);
                        ToastUtil.showToast(PartnerJoinActivity.this, "提交失败");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_listview.getVisibility() == View.VISIBLE) {
                rl_listview.setVisibility(View.GONE);
                return true;
            }
            if (!et_name.getText().toString().trim().equals("")
                    || !et_phone.getText().toString().trim().equals("")
                    || !et_weixin.getText().toString().trim().equals("")
                    || !et_email.getText().toString().trim().equals("")
                    || !et_bussinessNum.getText().toString().trim().equals("")
                    || !et_id.getText().toString().trim().equals("")
                    || !et_name02.getText().toString().trim().equals("")
                    || !et_phone02.getText().toString().trim().equals("")
                    || !et_weixin02.getText().toString().trim().equals("")
                    || !et_email02.getText().toString().trim().equals("")
                    || !et_bussinessNum02.getText().toString().trim().equals("")
                    || !et_id02.getText().toString().trim().equals("")
                    || !et_address.getText().toString().trim().equals("")
                    || isImg01 || isImg02 || isImg03 || isImg04 || isImg05 || isImg06 || isImg07 || isImg08
                    || !provinceId.equals("") || !cityId.equals("")
                    || !sid.equals("")) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否退出编辑");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                PartnerJoinActivity.this.finish();
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
        // 删除文件夹及文件
        FileUtils.deleteDir();
        super.onDestroy();
    }
}
