package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
import com.loosoo100.campus100.adapters.CommActiListHomeAdapter;
import com.loosoo100.campus100.adapters.CommunityDepGridviewAdapter;
import com.loosoo100.campus100.adapters.MyCommunityAdapter;
import com.loosoo100.campus100.anyevent.MEventCommActiPay;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.beans.CommunityMoney;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.utils.campus.FileUtils;
import com.loosoo100.campus100.view.CircleView;
import com.loosoo100.campus100.view.CustomDialog;
import com.loosoo100.campus100.view.MyScrollView;
import com.loosoo100.campus100.view.MyScrollView.OnScrollListener;
import com.loosoo100.campus100.view.splinechart.SplineChartView;
import com.loosoo100.campus100.zxing.encoding.EncodingUtils;
import com.loosoo100.campus100.zzboss.adapter.BossCommSupportAdapter;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author yang 我的社团activity
 */
public class MyCommunityActivity extends Activity implements OnClickListener,
        OnScrollListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.iv_empty2)
    private ImageView iv_empty2; // 空view
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.rl_topbar)
    private RelativeLayout rl_topbar; // 顶部颜色
    @ViewInject(R.id.tv_top01)
    private TextView tv_top01; // 顶部字体带背景颜色
    @ViewInject(R.id.btn_top02)
    private Button btn_top02; // 顶部字体透明背景
    @ViewInject(R.id.abscrollview)
    private MyScrollView scrollview;
    @ViewInject(R.id.iv_picture)
    private ImageView iv_picture; // 社团背景图片
    @ViewInject(R.id.cv_headShot)
    private CircleView cv_headShot; // 社团头像
    @ViewInject(R.id.tv_communityName)
    private TextView tv_communityName; // 社团名称
    @ViewInject(R.id.tv_id)
    private TextView tv_id; // 社团号
    @ViewInject(R.id.tv_school)
    private TextView tv_school; // 所属学校
    @ViewInject(R.id.tv_job)
    private TextView tv_job; // 职位
    @ViewInject(R.id.tv_money)
    public static TextView tv_money; // 资金
    @ViewInject(R.id.tv_slogan)
    private TextView tv_slogan; // 口号
    @ViewInject(R.id.tv_notice)
    private TextView tv_notice; // 社团公告
    @ViewInject(R.id.tv_summary)
    private TextView tv_summary; // 社团简介
    @ViewInject(R.id.ll_more_activity)
    private LinearLayout ll_more_activity; // 更多活动
    @ViewInject(R.id.ll_more_money)
    private LinearLayout ll_more_money; // 更多资金
    @ViewInject(R.id.ll_edit_slogan)
    private LinearLayout ll_edit_slogan; // 口号编辑
    @ViewInject(R.id.ll_edit_notice)
    private LinearLayout ll_edit_notice; // 公告编辑
    @ViewInject(R.id.ll_edit_summary)
    private LinearLayout ll_edit_summary; // 简介编辑
    @ViewInject(R.id.ll_edit_member)
    private LinearLayout ll_edit_member; // 成员编辑
    @ViewInject(R.id.lv_activity)
    private ListView lv_activity; // 活动列表
    @ViewInject(R.id.ll_more_need)
    private LinearLayout ll_more_need; // 更多需求
    @ViewInject(R.id.ll_need)
    private LinearLayout ll_need; // 需求布局
    @ViewInject(R.id.lv_need)
    private ListView lv_need; // 需求列表
    @ViewInject(R.id.iv_back_bg)
    private ImageView iv_back_bg; // 返回背景
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg; // 加载动画
    @ViewInject(R.id.tv_type)
    private TextView tv_type; // 社团类型
    @ViewInject(R.id.gv_dep)
    private GridView gv_dep; // 社团架构
    @ViewInject(R.id.lv_comm)
    private ListView lv_comm; // 顶部我的社团列表
    @ViewInject(R.id.rl_noData)
    private RelativeLayout rl_noData; // 显示暂无数据
    @ViewInject(R.id.spline)
    private SplineChartView spline; // 折线图
    @ViewInject(R.id.rl_more)
    private RelativeLayout rl_more; // 更多
    @ViewInject(R.id.rl_popupwindow)
    private RelativeLayout rl_popupwindow; // 弹出菜单项(社团成员)
    @ViewInject(R.id.rl_popupwindow_creater)
    private RelativeLayout rl_popupwindow_creater;// 弹出菜单项(社团长)
    @ViewInject(R.id.btn_createActi)
    private Button btn_createActi;// 新建活动(社长)
    @ViewInject(R.id.btn_createNeed)
    private Button btn_createNeed;// 新建需求(社长)
    // @ViewInject(R.id.btn_myQRCode_all)
    // private Button btn_myQRCode_all;// 我的二维码(社员)
    @ViewInject(R.id.btn_out)
    private Button btn_out; // 退出社团
    @ViewInject(R.id.btn_report)
    private Button btn_report; // 举报社团
    @ViewInject(R.id.dialog_report)
    private RelativeLayout dialog_report; // 举报界面
    @ViewInject(R.id.rl_black)
    private RelativeLayout rl_black;
    @ViewInject(R.id.btn01)
    private Button btn01;
    @ViewInject(R.id.btn02)
    private Button btn02;
    @ViewInject(R.id.btn03)
    private Button btn03;
    @ViewInject(R.id.btn04)
    private Button btn04;
    @ViewInject(R.id.btn05)
    private Button btn05;
    @ViewInject(R.id.iv_qrcode_click)
    private ImageView iv_qrcode_click;
    @ViewInject(R.id.tv_memCount)
    private TextView tv_memCount;

    public static CommunityBasicInfo communityBasicInfos;
    private CommActiListHomeAdapter listAdapter;

    private CommunityDepGridviewAdapter depGridviewAdapter;
    private BossCommSupportAdapter needAdapter;

    private Intent intent = new Intent();

    private String uid = "";
    private String sid = "";
    private String commID = ""; // 下拉列表点击的社团ID

    private List<CommunityBasicInfo> commList;
    private List<CommunityActivityInfo> list;
    private List<BossCommSupportInfo> needList;

    private String content = "";
    private Dialog dialog;
    private EditText editText;

    private Uri imageUri;
    private Uri imageUriLogo;
    private String fileName = MyConfig.FILE_URI + "/commBg.jpg";
    private String fileNameLogo = MyConfig.FILE_URI + "/commHeadShot.jpg";
    private final String IMAGE_UNSPECIFIED = "image/*";

    private int picIndex = 0;
    private Bitmap bitmapHeadShot;
    private Bitmap bitmapBg;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (communityBasicInfos != null) {
                commID = communityBasicInfos.getId();
                if (communityBasicInfos.getMyCommList() != null
                        && communityBasicInfos.getMyCommList().size() > 0) {
                    rl_noData.setVisibility(View.GONE);
                    commList = communityBasicInfos.getMyCommList();
                    initView();
                    if (communityBasicInfos.getMoneyList() != null
                            && communityBasicInfos.getMoneyList().size() > 0) {
                        initSpline();
                    } else {
                        spline.resetData();
                    }
                    initListView();
                    initNeedListView();
                    initMyCommListView();
                    initDepGridView();
                } else {
                    intent.setClass(MyCommunityActivity.this,
                            CommunityJoinActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                rl_noData.setVisibility(View.VISIBLE);
            }
            btn_top02.setClickable(true);
            rl_more.setClickable(true);
            progress.setVisibility(View.GONE);
        }
    };

    private Handler handlerUpdate = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initView();
            if (communityBasicInfos.getMoneyList() != null
                    && communityBasicInfos.getMoneyList().size() > 0) {
                initSpline();
            } else {
                spline.resetData();
            }
            initListView();
            initNeedListView();
            initDepGridView();
            btn_top02.setClickable(true);
            rl_more.setClickable(true);
            progress.setVisibility(View.GONE);
        }
    };

    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (communityBasicInfos != null) {
                list = communityBasicInfos.getActivityInfos();
            }
            if (list != null && list.size() > 0) {
                listAdapter = new CommActiListHomeAdapter(
                        MyCommunityActivity.this, list);
                lv_activity.setAdapter(listAdapter);
                // 设置listview的高度
                MyUtils.setListViewHeight(lv_activity, 0);
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycommunity);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);
        MyUtils.setStatusBarHeight(this, iv_empty2);

        EventBus.getDefault().register(this);

        progress.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        iv_qrcode_click.setOnClickListener(this);
        iv_picture.setOnClickListener(this);
        cv_headShot.setOnClickListener(this);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");
        sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.SCHOOL_ID, "");

        // 数据后台加载
        new Thread() {
            @Override
            public void run() {
                communityBasicInfos = GetData
                        .getMyCommunityBasicInfo(MyConfig.URL_JSON_MYCOMMUNITY
                                + uid + "&sid=" + sid);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();

        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        dialog = new Dialog(this, R.style.MyDialog);
        View viewDialogCamera = inflater.inflate(R.layout.dialog_camera, null);
        dialog.setContentView(viewDialogCamera);
        dialog.setContentView(viewDialogCamera, params);
        viewDialogCamera.findViewById(R.id.btn_camera).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
    }

    /**
     * 社团架构
     */
    private void initDepGridView() {
        List<String> depList = new ArrayList<String>();
        String[] dep = communityBasicInfos.getDep().split(",");
        for (int i = 0; i < dep.length; i++) {
            depList.add(dep[i]);
        }
        depGridviewAdapter = new CommunityDepGridviewAdapter(this, depList);
        gv_dep.setAdapter(depGridviewAdapter);
        // 设置gridview的高度
        MyUtils.setGridViewHeight(gv_dep, 0, 4);
    }

    /**
     * 活动列表
     */
    private void initListView() {
        if (communityBasicInfos.getActivityInfos() == null) {
            return;
        }
        list = communityBasicInfos.getActivityInfos();
        listAdapter = new CommActiListHomeAdapter(this, list);
        lv_activity.setAdapter(listAdapter);
        lv_activity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (list.get(position).getClassify() == 0) {
                    Intent intent = new Intent(MyCommunityActivity.this,
                            CommActivityDetailTogetherActivity.class);
                    intent.putExtra("aid", list.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyCommunityActivity.this,
                            CommActivityDetailFreeActivity.class);
                    intent.putExtra("aid", list.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        // 设置listview的高度
        MyUtils.setListViewHeight(lv_activity, 0);
    }

    /**
     * 需求列表
     */
    private void initNeedListView() {
        if (communityBasicInfos.getNeedInfos() == null) {
            return;
        }
        needList = communityBasicInfos.getNeedInfos();
        needAdapter = new BossCommSupportAdapter(this, needList);
        lv_need.setAdapter(needAdapter);
        lv_need.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (needList.get(position).getClassify() == 0) {
                    Intent intent = new Intent(MyCommunityActivity.this,
                            CommDemandMoneyDetailActivity.class);
                    intent.putExtra("did", needList.get(position).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyCommunityActivity.this,
                            CommDemandBoothDetailActivity.class);
                    intent.putExtra("did", needList.get(position).getId());
                    startActivity(intent);
                }
            }
        });
        // 设置listview的高度
        MyUtils.setListViewHeight(lv_need, 0);
    }

    /**
     * 我的社团
     */
    private void initMyCommListView() {
        lv_comm.setAdapter(new MyCommunityAdapter(this, commList));
        lv_comm.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                lv_comm.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tv_top01.setText(commList.get(position).getCommunityName());
                btn_top02.setText(commList.get(position).getCommunityName());
                btn_top02.setClickable(false);
                rl_more.setClickable(false);
                commID = commList.get(position).getId();
                // 数据后台加载
                new Thread() {
                    @Override
                    public void run() {
                        communityBasicInfos = GetData
                                .getMyCommunityOtherBasicInfo(MyConfig.URL_JSON_MYCOMMUNITY_DETAIL
                                        + commID + "&sid=" + sid);
                        if (communityBasicInfos != null && !isDestroyed()) {
                            handlerUpdate.sendEmptyMessage(0);
                        }
                    }
                }.start();
            }
        });
    }

    /**
     * 初始化折线图
     */
    private void initSpline() {
        float maxMoney = 0;
        List<CommunityMoney> moneyList = communityBasicInfos.getMoneyList();
        // 底栏标签
        List<String> labels = new ArrayList<String>();
        labels.add("");

        List<SplineData> chartData = new ArrayList<SplineData>();
        // 数据集
        List<PointD> linePoint = new ArrayList<PointD>();

        for (int i = 0; i < moneyList.size(); i++) {
            labels.add(moneyList.get(i).getTime());
            linePoint.add(new PointD(i + 1, Double.valueOf(moneyList.get(i)
                    .getMoney())));
            if (Double.valueOf(moneyList.get(i).getMoney()) > maxMoney) {
                maxMoney = Float.valueOf(moneyList.get(i).getMoney());
            }
        }
        for (int i = 0; i < 7 - moneyList.size(); i++) {
            labels.add("");
        }

        SplineData dataSeries = new SplineData("", linePoint, Color.rgb(253,
                60, 73));
        // 把线弄细点
        // dataSeries.getLinePaint().setStrokeWidth(2);
        // 是否显示标签内容
        // dataSeries.setLabelVisible(true);
        // 设置折线点的半径大小
        dataSeries.setDotRadius(8);
        dataSeries.setDotStyle(XEnum.DotStyle.DOT);
        dataSeries.getDotLabelPaint().setColor(Color.RED);

        // 设置round风格的标签
        // dataSeries.getLabelOptions().showBackground();
        dataSeries.getLabelOptions().getBox().getBackgroundPaint()
                .setColor(Color.GREEN);
        dataSeries.getLabelOptions().getBox().setRoundRadius(8);
        dataSeries.getLabelOptions().setLabelBoxStyle(
                XEnum.LabelBoxStyle.CAPROUNDRECT);
        chartData.add(dataSeries);

        spline.setData(maxMoney, 7, labels, chartData);
    }

    private void initView() {
        ll_more_activity.setOnClickListener(this);
        ll_more_need.setOnClickListener(this);
        ll_more_money.setOnClickListener(this);
        ll_edit_slogan.setOnClickListener(this);
        ll_edit_notice.setOnClickListener(this);
        ll_edit_summary.setOnClickListener(this);
        ll_edit_member.setOnClickListener(this);
        btn_top02.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        rl_black.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn03.setOnClickListener(this);
        btn04.setOnClickListener(this);
        btn05.setOnClickListener(this);
        btn_out.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_createActi.setOnClickListener(this);
        btn_createNeed.setOnClickListener(this);

        scrollview.setOnScrollListener(this);

        tv_communityName.setText(communityBasicInfos.getCommunityName());
        tv_top01.setText(communityBasicInfos.getCommunityName());
        btn_top02.setText(communityBasicInfos.getCommunityName());
        tv_id.setText("" + communityBasicInfos.getId());
        tv_school.setText(communityBasicInfos.getSchool());
        if (communityBasicInfos.getUserId().equals(uid)) {
            tv_job.setText("社长");
            ll_edit_slogan.setVisibility(View.VISIBLE);
            ll_edit_notice.setVisibility(View.VISIBLE);
            ll_edit_summary.setVisibility(View.VISIBLE);
            ll_need.setVisibility(View.VISIBLE);
            // rl_more.setVisibility(View.GONE);
        } else {
            tv_job.setText("成员");
            ll_edit_slogan.setVisibility(View.GONE);
            ll_edit_notice.setVisibility(View.GONE);
            ll_edit_summary.setVisibility(View.GONE);
            ll_need.setVisibility(View.GONE);
            // rl_more.setVisibility(View.VISIBLE);
        }
        tv_slogan.setText(communityBasicInfos.getSlogan());
        tv_summary.setText(communityBasicInfos.getSummary());
        tv_notice.setText(communityBasicInfos.getNotice());
        tv_type.setText(communityBasicInfos.getType());
        if (!communityBasicInfos.getHeadthumb().equals("")
                && !communityBasicInfos.getHeadthumb().equals("null")) {
            Glide.with(this).load(communityBasicInfos.getHeadthumb())
                    .into(cv_headShot);
        } else {
            Glide.with(this).load(R.drawable.imgloading).asBitmap()
                    .into(cv_headShot);
        }
        if (!communityBasicInfos.getCommBg().equals("")
                && !communityBasicInfos.getCommBg().equals("null")) {
            Glide.with(this).load(communityBasicInfos.getCommBg())
                    .placeholder(R.drawable.comm_bg_my).into(iv_picture);
        } else {
            Glide.with(this).load(R.drawable.comm_bg_my).asBitmap()
                    .into(iv_picture);
        }
        tv_money.setText("￥" + communityBasicInfos.getMoney());
        tv_memCount.setText("共" + communityBasicInfos.getMemCount() + "个小伙伴");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.iv_picture:
                if (communityBasicInfos.getUserId().equals(uid)) {
                    picIndex = 0;
                    dialog.show();
                }
                break;

            case R.id.cv_headShot:
                if (communityBasicInfos.getUserId().equals(uid)) {
                    picIndex = 1;
                    dialog.show();
                }
                break;

            case R.id.btn_top02:
                rl_popupwindow.setVisibility(View.GONE);
                rl_popupwindow_creater.setVisibility(View.GONE);
                if (lv_comm.getVisibility() == View.VISIBLE) {
                    lv_comm.setVisibility(View.GONE);
                } else {
                    lv_comm.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.rl_more:
                lv_comm.setVisibility(View.GONE);
                if (communityBasicInfos.getUserId().equals(uid)) {
                    if (rl_popupwindow_creater.getVisibility() == View.GONE) {
                        rl_popupwindow_creater.setVisibility(View.VISIBLE);
                    } else {
                        rl_popupwindow_creater.setVisibility(View.GONE);
                    }
                } else {
                    if (rl_popupwindow.getVisibility() == View.GONE) {
                        rl_popupwindow.setVisibility(View.VISIBLE);
                    } else {
                        rl_popupwindow.setVisibility(View.GONE);
                    }
                }
                break;

            // 新建活动
            case R.id.btn_createActi:
                rl_popupwindow_creater.setVisibility(View.GONE);
                intent.setClass(this, CommActiPublishActivity.class);
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivity(intent);
                break;

            // 新建需求
            case R.id.btn_createNeed:
                rl_popupwindow_creater.setVisibility(View.GONE);
                intent.setClass(this, CommNeedPublishActivity.class);
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivity(intent);
                break;

            case R.id.rl_black:
                if (dialog_report.getVisibility() == View.VISIBLE) {
                    dialog_report.setVisibility(View.GONE);
                }
                break;

            // 右上角加入社团
            case R.id.btn_out:
                rl_popupwindow.setVisibility(View.GONE);
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否确认退出");
                builder.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread() {
                                    public void run() {
                                        postOut(MyConfig.URL_POST_COMMUNITY_OUT2
                                                + "?cid="
                                                + communityBasicInfos.getId()
                                                + "&id="
                                                + communityBasicInfos.getId2());
                                    }

                                    ;
                                }.start();
                            }
                        });
                builder.setNegativeButton("否", null);
                builder.create().show();
                break;
            // 右上角生成二维码(社长)
            // case R.id.btn_myQRCode:
            // rl_popupwindow_creater.setVisibility(View.GONE);
            // showQRCodeDialog();
            // break;
            // case R.id.btn_myQRCode_all:
            // rl_popupwindow.setVisibility(View.GONE);
            // showQRCodeDialog();
            // break;
            // 二维码按钮
            case R.id.iv_qrcode_click:
                showQRCodeDialog();
                break;

            // 右上角举报社团
            case R.id.btn_report:
                rl_popupwindow.setVisibility(View.GONE);
                dialog_report.setVisibility(View.VISIBLE);
                break;

            // 更多活动
            case R.id.ll_more_activity:
                intent.setClass(this, CommunityMyActivityActivity.class);
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivityForResult(intent, 3);
                break;

            // 更多需求
            case R.id.ll_more_need:
                intent.setClass(this, CommNeedMoreActivity.class);
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivity(intent);
                break;

            // 更多资金
            case R.id.ll_more_money:
                boolean flag = false;
                if (communityBasicInfos.getUserId().equals(uid)) {
                    flag = true;
                }
                intent.setClass(this, CommunityMoneyActivity.class);
                intent.putExtra("commID", communityBasicInfos.getId());
                intent.putExtra("sid", sid);
                intent.putExtra("flag", flag);
                startActivity(intent);
                break;

            // 编辑口号
            case R.id.ll_edit_slogan:
                intent.setClass(this, CommunitySloganEditActivity.class);
                intent.putExtra("slogan", tv_slogan.getText().toString());
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivityForResult(intent, 11);
                break;

            // 编辑公告
            case R.id.ll_edit_notice:
                intent.setClass(this, CommunityNoticeEditActivity.class);
                intent.putExtra("notice", tv_notice.getText().toString());
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivityForResult(intent, 6);
                break;

            // 编辑简介
            case R.id.ll_edit_summary:
                intent.setClass(this, CommunitySummaryEditActivity.class);
                intent.putExtra("summary", tv_summary.getText().toString());
                intent.putExtra("cid", communityBasicInfos.getId());
                startActivityForResult(intent, 22);
                break;

            // 编辑成员
            case R.id.ll_edit_member:
                if (communityBasicInfos.getUserId().equals(uid)) {
                    intent.setClass(this, CommunityMemberManageActivity.class);
                    intent.putExtra("cid", communityBasicInfos.getId());
                    startActivity(intent);
                } else {
                    intent.setClass(this, CommMemberActivity.class);
                    intent.putExtra("showName", true);
                    intent.putExtra("cid", communityBasicInfos.getId());
                    startActivity(intent);
                }

                break;

            case R.id.btn01:
                dialog_report.setVisibility(View.GONE);
                content = "虚假活动";
                // progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn02:
                dialog_report.setVisibility(View.GONE);
                content = "淫秽色情";
                // progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn03:
                dialog_report.setVisibility(View.GONE);
                content = "广告骚扰";
                // progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn04:
                dialog_report.setVisibility(View.GONE);
                content = "恶意言论";
                // progress_update_blackbg.setVisibility(View.VISIBLE);
                new Thread() {
                    public void run() {
                        doPostReport(MyConfig.URL_POST_REPORT);
                    }

                    ;
                }.start();
                break;

            case R.id.btn05:
                dialog_report.setVisibility(View.GONE);
                dialog = new Dialog(this, R.style.MyDialog);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.dialog_report_other, null);
                dialog.setContentView(view);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                dialog.setContentView(view, params);
                editText = (EditText) view.findViewById(R.id.et_reason);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                Button btn_ok = (Button) view.findViewById(R.id.btn_ok);

                btn_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        content = editText.getText().toString().trim();
                        if (content.equals("")) {
                            ToastUtil.showToast(MyCommunityActivity.this,"请输入举报内容");
                            return;
                        }
                        dialog.dismiss();
                        // progress_update_blackbg.setVisibility(View.VISIBLE);
                        new Thread() {
                            public void run() {
                                doPostReport(MyConfig.URL_POST_REPORT);
                            }

                            ;
                        }.start();
                    }
                });
                dialog.show();
                // 显示软键盘
                MyUtils.showSoftInput(this, editText);
                break;
        }
    }

    private void showQRCodeDialog() {
        Bitmap bitmap = EncodingUtils.createQRCode(sid + ","
                + communityBasicInfos.getId(), 500, 500, null);
        LayoutInflater qr_view = getLayoutInflater();
        View layout = qr_view
                .inflate(R.layout.my_community_qrcode_dialog, null);
        CircleView iv_qrcode_img = (CircleView) layout
                .findViewById(R.id.cv_qrcode_head);
        if (!communityBasicInfos.getHeadthumb().equals("")) {
            Glide.with(this).load(communityBasicInfos.getHeadthumb())
                    .into(iv_qrcode_img);
        }
        TextView tv_title = (TextView) layout
                .findViewById(R.id.tv_qrcode_communityName);
        tv_title.setText(communityBasicInfos.getCommunityName());
        TextView tv_id = (TextView) layout.findViewById(R.id.tv_qrcode_id);
        tv_id.setText("" + communityBasicInfos.getId());
        ImageView iv_qrcode = (ImageView) layout.findViewById(R.id.my_qrcode);
        iv_qrcode.setImageBitmap(bitmap);
        new AlertDialog.Builder(this).setView(layout)
                .setInverseBackgroundForced(true).show();

    }

    @Override
    public void onScroll(int scrollY) {
        // rl_topbar.setAlpha((float) scrollY / 500);
        // iv_empty.setAlpha((float) scrollY / 500);
        // iv_back_bg.setAlpha(0.3f - (float) scrollY / 500);
        // tv_top01.setAlpha(0.3f - (float) scrollY / 500);
        if (lv_comm.getVisibility() == View.VISIBLE) {
            lv_comm.setVisibility(View.GONE);
        }
        if (rl_popupwindow.getVisibility() == View.VISIBLE) {
            rl_popupwindow.setVisibility(View.GONE);
        }
        if (rl_popupwindow_creater.getVisibility() == View.VISIBLE) {
            rl_popupwindow_creater.setVisibility(View.GONE);
        }
    }

    // 设置更改后的口号
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    tv_slogan.setText(data.getExtras().getString("slogan"));
                    break;
                case 22:
                    tv_summary.setText(data.getExtras().getString("summary"));
                    break;
                case 6:
                    tv_notice.setText(data.getExtras().getString("notice"));
                    break;
                case 0:
                    new Thread() {
                        @Override
                        public void run() {
                            communityBasicInfos = GetData
                                    .getMyCommunityBasicInfo(MyConfig.URL_JSON_MYCOMMUNITY_DETAIL
                                            + commID + "&sid=" + sid);
                            if (!isDestroyed()) {
                                handlerRefresh.sendEmptyMessage(0);
                            }
                        }

                        ;
                    }.start();
                    break;
                case 3:
                    new Thread() {
                        @Override
                        public void run() {
                            communityBasicInfos = GetData
                                    .getMyCommunityBasicInfo(MyConfig.URL_JSON_MYCOMMUNITY_DETAIL
                                            + commID + "&sid=" + sid);
                            if (!isDestroyed()) {
                                handlerRefresh.sendEmptyMessage(0);
                            }
                        }

                        ;
                    }.start();
                    break;
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
                            bitmapBg = decodeUriAsBitmap(imageUri);
                            try {
                                saveImage(bitmapBg);
                                progress_update_whitebg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postUpdateBg(
                                                MyConfig.URL_POST_COMM_UPDATE_BG,
                                                fileName);
                                    }

                                    ;
                                }.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (imageUriLogo != null) {
                            bitmapHeadShot = decodeUriAsBitmap(imageUriLogo);
                            try {
                                saveImageLogo(bitmapHeadShot);
                                progress_update_whitebg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        postUpdateHeadShot(
                                                MyConfig.URL_POST_COMM_UPDATE_BG,
                                                fileNameLogo);
                                    }

                                    ;
                                }.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
        intent.putExtra("aspectX", 9);
        intent.putExtra("aspectY", 4);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 1575);
        intent.putExtra("outputY", 700);
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

    /**
     * 修改背景图
     *
     * @param uploadHost
     * @param picUrl
     */
    private void postUpdateBg(String uploadHost, String picUrl) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("cid", commID);
        params.addBodyParameter("type", "bg");
        params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        String result = "";
                        progress_update_whitebg.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            // 把解析到的位图显示出来
                            // iv_picture.setImageURI(imageUri);
                            iv_picture.setImageBitmap(bitmapBg);
                            ToastUtil.showToast(MyCommunityActivity.this,"背景修改成功");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                        progress_update_whitebg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 修改社团头像
     *
     * @param uploadHost
     * @param picUrl
     */
    private void postUpdateHeadShot(String uploadHost, String picUrl) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("cid", commID);
        params.addBodyParameter("type", "logo");
        params.addBodyParameter("pic", new File(picUrl), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        String result = "";
                        progress_update_whitebg.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            // 把解析到的位图显示出来
                            // cv_headShot.setImageURI(imageUriLogo);
                            cv_headShot.setImageBitmap(bitmapHeadShot);
                            ToastUtil.showToast(MyCommunityActivity.this,"头像修改成功");
                        } else if (result.equals("0")) {
                            ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                        progress_update_whitebg.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 退出社团
     *
     * @param uploadHost
     */
    private void postOut(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
                            ToastUtil.showToast(MyCommunityActivity.this,"退出成功");
                            progress.setVisibility(View.VISIBLE);
                            // 数据后台加载
                            new Thread() {
                                @Override
                                public void run() {
                                    communityBasicInfos = GetData
                                            .getMyCommunityBasicInfo(MyConfig.URL_JSON_MYCOMMUNITY
                                                    + uid + "&sid=" + sid);
                                    if (!isDestroyed()) {
                                        handler.sendEmptyMessage(0);
                                    }
                                }
                            }.start();
                            // MyCommunityActivity.this.finish();
                        } else {
                            ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                    }
                });
    }

    /**
     * 举报
     *
     * @param uploadHost
     */
    private void doPostReport(String uploadHost) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", "3");
        params.addBodyParameter("rid", communityBasicInfos.getId());
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("content", content);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo",responseInfo.result);
                        // prog.setVisibility(View.GONE);
                        String result = "";
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("0")) {
                            ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                        } else if (result.equals("-1")) {
                            ToastUtil.showToast(MyCommunityActivity.this,"您已举报过了");
                        } else {
                            ToastUtil.showToast(MyCommunityActivity.this,"举报成功");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error",arg1.toString());
                        // progress_update_blackbg.setVisibility(View.GONE);
                        ToastUtil.showToast(MyCommunityActivity.this,"操作失败");
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (rl_popupwindow.getVisibility() == View.VISIBLE) {
            rl_popupwindow.setVisibility(View.GONE);
            return true;
        }
        if (rl_popupwindow_creater.getVisibility() == View.VISIBLE) {
            rl_popupwindow_creater.setVisibility(View.GONE);
            return true;
        }
        if (dialog_report.getVisibility() == View.VISIBLE) {
            dialog_report.setVisibility(View.GONE);
            return true;
        }
        if (lv_comm.getVisibility() == View.VISIBLE) {
            lv_comm.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 活动支付更新活动数据
     *
     * @param event
     */
    public void onEventMainThread(MEventCommActiPay event) {
        if (event.isChange()) {
            new Thread() {
                @Override
                public void run() {
                    communityBasicInfos = GetData
                            .getMyCommunityBasicInfo(MyConfig.URL_JSON_MYCOMMUNITY_DETAIL
                                    + commID + "&sid=" + sid);
                    if (!isDestroyed()) {
                        handlerRefresh.sendEmptyMessage(0);
                    }
                }

                ;
            }.start();
        }
    }

    @Override
    protected void onResume() {
        if (lv_comm.getVisibility() == View.VISIBLE) {
            lv_comm.setVisibility(View.GONE);
        }
        if (rl_popupwindow.getVisibility() == View.VISIBLE) {
            rl_popupwindow.setVisibility(View.GONE);
        }
        if (rl_popupwindow_creater.getVisibility() == View.VISIBLE) {
            rl_popupwindow_creater.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        tv_money = null;
        communityBasicInfos = null;
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        FileUtils.deleteFile(new File(fileName));
        FileUtils.deleteFile(new File(fileNameLogo));
        super.onDestroy();
    }

}
