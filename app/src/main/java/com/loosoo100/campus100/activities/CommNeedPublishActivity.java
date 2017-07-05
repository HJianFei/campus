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
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
import com.loosoo100.campus100.adapters.PartnerJoinListAdapter;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.picker.DatePicker;
import com.loosoo100.campus100.view.picker.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 社团需求发布
 *
 * @author yang
 */
public class CommNeedPublishActivity extends Activity implements
        OnClickListener {

    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty;
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;

    @ViewInject(R.id.btn_publish)
    private Button btn_publish; // 发布
    @ViewInject(R.id.et_actiName)
    private EditText et_actiName; // 活动标题
    @ViewInject(R.id.btn_type01)
    private Button btn_type01; // 活动类型
    @ViewInject(R.id.btn_type02)
    private Button btn_type02; // 分类
    @ViewInject(R.id.ll_cover)
    private LinearLayout ll_cover; // 封面
    @ViewInject(R.id.ll_detail)
    private LinearLayout ll_detail; // 详情
    @ViewInject(R.id.iv_cover)
    private ImageView iv_cover; // 封面
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    @ViewInject(R.id.listview)
    private ListView listview;
    @ViewInject(R.id.listview02)
    private ListView listview02;
    @ViewInject(R.id.rl_listview)
    private RelativeLayout rl_listview;
    @ViewInject(R.id.rl_black)
    private RelativeLayout rl_black;

    /*
     * 众筹资金
     */
    @ViewInject(R.id.ll_type_noTogether)
    private LinearLayout ll_type_noTogether; // 众筹资金
    @ViewInject(R.id.tv_signUpTime)
    private TextView tv_signUpTime; // 报名截止时间
    @ViewInject(R.id.btn_type03)
    private Button btn_type03; // 选择人手或摊位
    @ViewInject(R.id.et_needCount)
    private EditText et_needCount; // 赞助所需
    @ViewInject(R.id.et_money)
    private EditText et_money; // 单价
    @ViewInject(R.id.ll_signUpTime)
    private LinearLayout ll_signUpTime; // 报名截止时间

    /*
     * 摊位或人手
     */
    @ViewInject(R.id.ll_type_Together)
    private LinearLayout ll_type_Together; // 摊位或人手
    @ViewInject(R.id.et_unit_price)
    private EditText et_unit_price; // 所需金额
    @ViewInject(R.id.ll_signUpTime02)
    private LinearLayout ll_signUpTime02; // 报名截止时间
    @ViewInject(R.id.tv_signUpTime02)
    private TextView tv_signUpTime02; //报名截止时间

    private Intent intent = new Intent();

    private String actiName = "";
    private int needCount;
    private float money; // 非众筹每人参与费用
    private float needMoney; // 众筹活动所需金额
    private String signUpTime = "";
    private String sid = "";
    private String cid = "";
    private String uid = "";

    private int decimal_digits = 2; // 小数位数

    private List<CampusInfo> typeList01 = new ArrayList<>();
    private List<CampusInfo> typeList02 = new ArrayList<>();
    private List<CampusInfo> typeList03 = new ArrayList<>();

    private final String IMAGE_UNSPECIFIED = "image/*";
    private Uri imageUri;
    private String fileName = MyConfig.FILE_URI + "/cover.jpg";
    private boolean isCover = false; // 是否选择了封面
    private Dialog dialog;
    // 用于保存图片路径
    private ArrayList<String> list_path = new ArrayList<String>();
    private String summary = "";
    private String provide = "";

    private Dialog dialogTime;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Calendar mCalendar;

    private long longSignUpTime = 0;

    private int classify = 0; // 选择类型：0众筹资金 1摊位或人手
    private String cartid = "";
    private int type = -1;

    private Handler handlerList = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (typeList02 != null && typeList02.size() > 0) {
                initListView02();
                rl_listview.setVisibility(View.VISIBLE);
            }
            progress.setVisibility(View.GONE);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_need_publish);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);

        cid = getIntent().getExtras().getString("cid");

        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        CampusInfo campusInfo = new CampusInfo();
        campusInfo.setCampusName("筹集资金");
        typeList01.add(campusInfo);
        CampusInfo campusInfo2 = new CampusInfo();
        campusInfo2.setCampusName("摊位/人手");
        typeList01.add(campusInfo2);

        CampusInfo campusInfo3 = new CampusInfo();
        campusInfo3.setCampusName("摊位");
        typeList03.add(campusInfo3);
        CampusInfo campusInfo4 = new CampusInfo();
        campusInfo4.setCampusName("人手");
        typeList03.add(campusInfo4);

        initView();

    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_publish.setOnClickListener(this);
        ll_signUpTime.setOnClickListener(this);
        ll_signUpTime02.setOnClickListener(this);
        ll_cover.setOnClickListener(this);
        ll_detail.setOnClickListener(this);
        btn_type01.setOnClickListener(this);
        btn_type02.setOnClickListener(this);
        btn_type03.setOnClickListener(this);
        rl_black.setOnClickListener(this);

        /**
         * 设置小数位数控制
         */
        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - decimal_digits;
                    if (diff > 0) {
                        return source.subSequence(start, end - diff);
                    }
                }
                return null;
            }
        };

        // 控制输入框的小数位和长度,这里长度暂时设置为10
        et_money.setFilters(new InputFilter[]{lengthfilter,
                new InputFilter.LengthFilter(20)});
        et_unit_price.setFilters(new InputFilter[]{lengthfilter,
                new InputFilter.LengthFilter(20)});

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
                if (!et_actiName.getText().toString().trim().equals("")
                        || !et_money.getText().toString().trim().equals("")
                        || !et_needCount.getText().toString().trim().equals("")
                        || !et_unit_price.getText().toString().trim().equals("")
                        ) {
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                    builderDel.setMessage("是否取消发布");
                    builderDel.setPositiveButton("是",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    CommNeedPublishActivity.this.finish();
                                }
                            });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                } else {
                    finish();
                }
                break;

            case R.id.btn_type01:
                initListView01();
                break;

            case R.id.btn_type02:
                progress.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        typeList02 = GetData
                                .getCommActiTypes(MyConfig.URL_JSON_COMM_ACTI_TYPE
                                        + "?classify=1");
                        if (!isDestroyed()) {
                            handlerList.sendEmptyMessage(0);
                        }
                    }
                }.start();
                break;

            case R.id.btn_type03:
                initListView03();
                break;

            case R.id.rl_black:
                rl_listview.setVisibility(View.GONE);
                break;

            // 发布
            case R.id.btn_publish:
                actiName = et_actiName.getText().toString().trim();
                if (classify == 0) {
                    if (actiName.equals("")
                            || et_money.getText().toString().trim().equals("") || longSignUpTime == 0
                            || cartid.equals("")) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"请完善信息后再提交");
                        return;
                    }
                    needMoney = Float.valueOf(et_money.getText().toString()
                            .trim());
                    if (needMoney <= 0) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"金额须大于0");
                        return;
                    }
                    if (!isCover) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"请完善赞助需求封面");
                        return;
                    }
                    if (provide.equals("") || (summary.equals("") && list_path.size() == 0)) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"请完善赞助需求详情页");
                        return;
                    }
                    progress.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {
                            postDataTogether(MyConfig.URL_POST_COMM_NEED_NEW);
                        }

                        ;
                    }.start();
                } else {
                    if (actiName.equals("")
                            || longSignUpTime == 0 || type == -1
                            || (et_unit_price.getText().toString().trim().equals(""))
                            || et_needCount.getText().toString().trim()
                            .equals("") || cartid.equals("")) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"请完善信息后再提交");
                        return;
                    }
                    if (!et_unit_price.getText().toString().trim().equals("")) {
                        money = Float.valueOf(et_unit_price.getText().toString().trim());
                    }
                    if (money <= 0) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"金额须大于0");
                        return;
                    }
                    needCount = Integer.valueOf(et_needCount.getText()
                            .toString().trim());
                    if (needCount <= 0) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"赞助所需数量须大于0");
                        return;
                    }
                    if (!isCover) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"请完善赞助需求封面");
                        return;
                    }
                    if (provide.equals("") || (summary.equals("") && list_path.size() == 0)) {
                        ToastUtil.showToast(CommNeedPublishActivity.this,"请完善赞助需求详情页");
                        return;
                    }
                    progress.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {
                            postData(MyConfig.URL_POST_COMM_NEED_NEW);
                        }

                        ;
                    }.start();
                }
                break;

            case R.id.ll_signUpTime:
                showDatePicker(tv_signUpTime, longSignUpTime);
                break;
            case R.id.ll_signUpTime02:
                showDatePicker(tv_signUpTime02, longSignUpTime);
                break;

            // 封面
            case R.id.ll_cover:
                dialog.show();
                break;

            case R.id.ll_detail:
                Intent intent = new Intent(this, CommNeedPublicDetailActivity.class);
                intent.putStringArrayListExtra("list_path", list_path);
                intent.putExtra("summary", summary);
                intent.putExtra("provide", provide);
                startActivityForResult(intent, 66);
                break;

        }
    }

    private void initListView01() {
        rl_listview.setVisibility(View.VISIBLE);
        listview.setVisibility(View.VISIBLE);
        listview02.setVisibility(View.GONE);
        listview.setAdapter(new PartnerJoinListAdapter(this, typeList01));
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                rl_listview.setVisibility(View.GONE);
                cartid = "";
                signUpTime = "";
                longSignUpTime = 0;
                btn_type02.setText("");
                if (position == 0) {
                    classify = 0;
                    ll_type_noTogether.setVisibility(View.GONE);
                    ll_type_Together.setVisibility(View.VISIBLE);
                } else {
                    classify = 1;
                    ll_type_noTogether.setVisibility(View.VISIBLE);
                    ll_type_Together.setVisibility(View.GONE);
                }
                btn_type01.setText(typeList01.get(position).getCampusName());
            }
        });
    }

    private void initListView02() {
        listview.setVisibility(View.VISIBLE);
        listview02.setVisibility(View.GONE);
        listview.setAdapter(new PartnerJoinListAdapter(this, typeList02));
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cartid = typeList02.get(position).getCampusID();
                btn_type02.setText(typeList02.get(position).getCampusName());
                rl_listview.setVisibility(View.GONE);
            }
        });
    }


    private void initListView03() {
        rl_listview.setVisibility(View.VISIBLE);
        listview.setVisibility(View.GONE);
        listview02.setVisibility(View.VISIBLE);
        listview02.setAdapter(new PartnerJoinListAdapter(this, typeList03));
        listview02.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                rl_listview.setVisibility(View.GONE);
                if (position == 0) {
                    type = 0;
                } else {
                    type = 1;
                }
                btn_type03.setText(typeList03.get(position).getCampusName());
            }
        });
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
                        iv_cover.setImageBitmap(bitmap);
                        isCover = true;
                        try {
                            saveImage(bitmap);
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

                case 66:
                    if (data != null) {
                        list_path = data.getStringArrayListExtra("list_path");
                        summary = data.getExtras().getString("summary");
                        provide = data.getExtras().getString("provide");
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 1280);
        intent.putExtra("outputY", 1280);
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

    private void saveImage(Bitmap bitmap) throws IOException {
        File file = new File(MyConfig.FILE_URI);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream iStream = new FileOutputStream(fileName);
        bitmap.compress(CompressFormat.JPEG, 80, iStream);
        iStream.close();
        iStream = null;
        file = null;
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
     * 上传数据 众筹资金
     *
     * @param uploadHost
     */
    private void postDataTogether(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("cid", cid);
        params.addBodyParameter("name", actiName);
        params.addBodyParameter("classify", classify + "");
        params.addBodyParameter("cartid", cartid);
        params.addBodyParameter("time", signUpTime);
        params.addBodyParameter("money1", needMoney + "");
        params.addBodyParameter("headimg", new File(fileName), "image/jpeg");
        params.addBodyParameter("letter", summary);
        params.addBodyParameter("outline", provide);
        for (int i = 0; i < list_path.size(); i++) {
            params.addBodyParameter("pic" + (i + 1),
                    new File(list_path.get(i)), "image/jpeg");
        }
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        progress.setVisibility(View.GONE);
                        int result = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.optInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result > 0) {
                            ToastUtil.showToast(CommNeedPublishActivity.this,"提交成功,请耐心等待审核");
                            finish();
                        } else {
                            ToastUtil.showToast(CommNeedPublishActivity.this,"提交失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(CommNeedPublishActivity.this,"提交失败");
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 上传数据 摊位或人手
     *
     * @param uploadHost
     */
    private void postData(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("cid", cid);
        params.addBodyParameter("name", actiName);
        params.addBodyParameter("classify", classify + "");
        params.addBodyParameter("cartid", cartid);
        params.addBodyParameter("time", signUpTime);
        params.addBodyParameter("money1", needCount + "");
        params.addBodyParameter("type", type + "");
        params.addBodyParameter("tian1", money + "");
        params.addBodyParameter("headimg", new File(fileName), "image/jpeg");
        params.addBodyParameter("letter", summary);
        params.addBodyParameter("outline", provide);
        for (int i = 0; i < list_path.size(); i++) {
            params.addBodyParameter("pic" + (i + 1),
                    new File(list_path.get(i)), "image/jpeg");
        }

        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        progress.setVisibility(View.GONE);
                        int result = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.optInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result > 0) {
                            ToastUtil.showToast(CommNeedPublishActivity.this,"提交成功,请耐心等待审核");
                            finish();
                        } else {
                            ToastUtil.showToast(CommNeedPublishActivity.this,"提交失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(CommNeedPublishActivity.this,"提交失败");
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void showDatePicker(final TextView timeView, long timeInMillis) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        dialogTime = new Dialog(this, R.style.MyDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        dialogTime.setContentView(view);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        dialogTime.setContentView(view, params);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        if (timeInMillis != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeInMillis);
            datePicker.setCalendar(calendar);
            timePicker.setCalendar(calendar);
        }

        Button submitView = (Button) view.findViewById(R.id.btn_get_time);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        submitView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendar.set(Calendar.YEAR, datePicker.getYear());
                mCalendar.set(Calendar.MONTH, datePicker.getMonth());
                mCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
                mCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHourOfDay());
                mCalendar.set(Calendar.MINUTE, timePicker.getMinute());
                timeView.setText(MyUtils.getDateYMDHM(mCalendar
                        .getTimeInMillis()));
                longSignUpTime = mCalendar.getTimeInMillis();
                signUpTime = timeView.getText().toString() + ":00";
                dialogTime.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTime.dismiss();
            }
        });
        dialogTime.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!et_actiName.getText().toString().trim().equals("")
                    || !et_money.getText().toString().trim().equals("")
                    || !et_needCount.getText().toString().trim().equals("")
                    || !et_unit_price.getText().toString().trim().equals("")) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(this);
                builderDel.setMessage("是否取消发布");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                CommNeedPublishActivity.this.finish();
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
        FileUtils.deleteDir();
        FileUtils.deleteFile(new File(fileName));
        super.onDestroy();
    }

}
