package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.SearchAdapter;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyLocationUtil;
import com.loosoo100.campus100.utils.MyUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author yang 搜索activity
 */
public class SearchActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.tv_location)
    private TextView tv_location; // 当前地址

    @ViewInject(R.id.ib_refresh)
    private ImageButton ib_refresh; // 刷新

    @ViewInject(R.id.et_search)
    private EditText et_search; // 搜索内容
    @ViewInject(R.id.rl_search_back)
    private RelativeLayout rl_search_back; // 返回
    @ViewInject(R.id.rl_search)
    private RelativeLayout rl_search; // 搜索按钮
    @ViewInject(R.id.lv_search_campus)
    private ListView lv_search_campus; // 学校列表
    @ViewInject(R.id.btn_delete)
    private Button btn_delete; // 输入文本删除

    private List<CampusInfo> list;
    private SearchAdapter adapter;

    private RotateAnimation rotate;
    private String school = "";
    private String school_encoded; // 转码后的学校

    private int page = 1;
    private boolean isLoading = true;
    private MyLocationUtil myLocationUtil;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                initListView();
                lv_search_campus.setVisibility(View.VISIBLE);
            } else {
                lv_search_campus.setVisibility(View.GONE);
            }
            isLoading = false;
            page++;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewUtils.inject(this);

        myLocationUtil = new MyLocationUtil(getApplicationContext());

        MyUtils.setStatusBarHeight(this, iv_empty);
        // 不自动弹出软键盘
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // list = null;
                school = et_search.getText().toString().trim();
                if (school.equals("")) {
                    btn_delete.setVisibility(View.GONE);
                } else {
                    btn_delete.setVisibility(View.VISIBLE);
                }
                page = 1;
                isLoading = true;
                new Thread() {
                    public void run() {
                        if (school.equals("")) {
                            list = GetData
                                    .getCampusInfos(MyConfig.URL_JSON_SCHOOL_DISTANCE
                                            + MyLocationUtil.latitude
                                            + "&lng="
                                            + MyLocationUtil.longitude);
                        } else {
                            try {
                                school_encoded = java.net.URLEncoder.encode(
                                        school, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            list = GetData
                                    .getSearchCampusInfos(MyConfig.URL_JSON_SCHOOL_SEARCH
                                            + school_encoded + "&page=" + page);
                        }
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new Thread() {
            @Override
            public void run() {
                list = GetData.getCampusInfos(MyConfig.URL_JSON_SCHOOL_DISTANCE
                        + MyLocationUtil.latitude + "&lng="
                        + MyLocationUtil.longitude);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private void initListView() {
        adapter = new SearchAdapter(this, list);
        lv_search_campus.setAdapter(adapter);

    }

    private void initView() {
        rl_search_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        ib_refresh.setOnClickListener(this);

        if (MyLocationUtil.loc != null && !MyLocationUtil.loc.equals("")) {
            tv_location.setText(MyLocationUtil.loc);
        }

        lv_search_campus.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(MyConfig.SCHOOL_SEARCH, list.get(position)
                        .getCampusName());
                intent.putExtra(MyConfig.SCHOOL_SEARCH_ID, list.get(position)
                        .getCampusID());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        lv_search_campus.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading
                        && !school.equals("")) {
                    isLoading = true;
                    new Thread() {
                        private List<CampusInfo> list2;

                        public void run() {
                            list2 = GetData
                                    .getSearchCampusInfos(MyConfig.URL_JSON_SCHOOL_SEARCH
                                            + school_encoded + "&page=" + page);
                            if (list2 != null && list2.size() > 0) {
                                for (int i = 0; i < list2.size(); i++) {
                                    list.add(list2.get(i));
                                }
                                if (!isDestroyed()) {
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
            }
        });

        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        // 设置动画时间
        rotate.setDuration(600);
        // 设置动画结束后控件停留在结束的状态
        rotate.setFillAfter(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回按钮
            case R.id.rl_search_back:
                finish();
                break;

            case R.id.btn_delete:
                et_search.setText("");
                break;

            // 搜索按钮
            case R.id.rl_search:
                school = et_search.getText().toString().trim();
                try {
                    school_encoded = java.net.URLEncoder.encode(school, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new Thread() {
                    public void run() {
                        if (school.equals("")) {
                            list = GetData
                                    .getCampusInfos(MyConfig.URL_JSON_SCHOOL_DISTANCE
                                            + MyLocationUtil.latitude
                                            + "&lng="
                                            + MyLocationUtil.longitude);
                        } else {
                            list = GetData
                                    .getSearchCampusInfos(MyConfig.URL_JSON_SCHOOL_SEARCH
                                            + school_encoded);
                        }
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    ;
                }.start();
                break;

            // 刷新按钮
            case R.id.ib_refresh:
                ib_refresh.startAnimation(rotate);
                myLocationUtil.start();
                if (MyLocationUtil.loc != null && !MyLocationUtil.loc.equals("")) {
                    tv_location.setText(MyLocationUtil.loc);
                }
                // 刷新学校列表
                list = null;
                new Thread() {
                    @Override
                    public void run() {
                        list = GetData
                                .getCampusInfos(MyConfig.URL_JSON_SCHOOL_DISTANCE
                                        + MyLocationUtil.latitude + "&lng="
                                        + MyLocationUtil.longitude);
                        if (!isDestroyed()) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }.start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        myLocationUtil.stop();
        super.onDestroy();
    }

}
