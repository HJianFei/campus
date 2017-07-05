package com.loosoo100.campus100.activities;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.AddressAdapter;
import com.loosoo100.campus100.beans.AddressInfo;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

/**
 * @author yang 收货地址activity
 */
public class AddressActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back)
    private View rl_back;
    @ViewInject(R.id.btn_add)
    private Button btn_add; // 新增地址
    @ViewInject(R.id.lv_address)
    private ListView lv_address; // 地址列表
    @ViewInject(R.id.progress)
    private RelativeLayout progress; // 加载动画

    private List<AddressInfo> list;
    private AddressAdapter adapter;

    private String uid = "";
    private int type;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                initListView();
                lv_address.setVisibility(View.VISIBLE);
            } else {
                lv_address.setVisibility(View.GONE);
            }
            progress.setVisibility(View.GONE);
        }

        ;
    };

    private Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                lv_address.setVisibility(View.VISIBLE);
                adapter = new AddressAdapter(AddressActivity.this, list);
                lv_address.setAdapter(adapter);
            } else {
                lv_address.setVisibility(View.GONE);
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        type = getIntent().getExtras().getInt("type");

        progress.setVisibility(View.VISIBLE);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
                .getString(UserInfoDB.USERID, "");

        initView();

        new Thread() {
            public void run() {
                list = GetData.getAddressInfos(MyConfig.URL_JSON_ADDRESS + uid);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
    }

    private void initListView() {
        adapter = new AddressAdapter(this, list);
        lv_address.setAdapter(adapter);
    }

    private void initView() {
        rl_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        lv_address.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                   final int position, long id) {
                if (type == 0) {
                    Intent intent = new Intent(AddressActivity.this,
                            AddressEditActivity.class);
                    intent.putExtra("userName", list.get(position)
                            .getUserName());
                    // intent.putExtra("sex", list.get(position).getSex());
                    intent.putExtra("userPhone", list.get(position)
                            .getUserPhone());
                    // intent.putExtra("postCode", list.get(position)
                    // .getPostcode());
                    intent.putExtra("address", list.get(position).getAddress());
                    intent.putExtra("addressId", list.get(position)
                            .getAddressId());
                    intent.putExtra("school", list.get(position).getSchool());
                    intent.putExtra("sid", list.get(position).getSid());
                    intent.putExtra("province", list.get(position)
                            .getProvince());
                    intent.putExtra("pid", list.get(position).getPid());
                    intent.putExtra("city", list.get(position).getCity());
                    intent.putExtra("cid", list.get(position).getCid());
                    intent.putExtra("area", list.get(position).getArea());
                    intent.putExtra("aid", list.get(position).getAid());
                    intent.putExtra("isDefault", list.get(position)
                            .getIsDefault());
                    startActivity(intent);
                } else if (type == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("userId", list.get(position).getUserId());
                    intent.putExtra("name", list.get(position).getUserName());
                    intent.putExtra("phone", list.get(position).getUserPhone());
                    intent.putExtra("address", list.get(position).getAddress());
                    intent.putExtra("addressId", list.get(position)
                            .getAddressId());
                    intent.putExtra("province", list.get(position)
                            .getProvince());
                    intent.putExtra("pid", list.get(position).getPid());
                    intent.putExtra("city", list.get(position).getCity());
                    intent.putExtra("cid", list.get(position).getCid());
                    intent.putExtra("area", list.get(position).getArea());
                    intent.putExtra("aid", list.get(position).getAid());
                    intent.putExtra("school", list.get(position).getSchool());
                    intent.putExtra("sid", list.get(position).getSid());
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (type == 3) {
                    if (!list.get(position).getSchool().equals("")
                                    &&!list.get(position).getSchool().equals("null")) {
                        ToastUtil.showToast(AddressActivity.this, "请选择礼物盒子的收货地址");
                        return;
                    }
                    CustomDialog.Builder builderDel = new CustomDialog.Builder(AddressActivity.this);
                    builderDel.setMessage("是否确认领取礼物");
                    builderDel.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("name", list.get(position).getUserName());
                            intent.putExtra("phone", list.get(position).getUserPhone());
                            intent.putExtra("address", list.get(position).getAddress());
                            intent.putExtra("province", list.get(position)
                                    .getProvince());
                            intent.putExtra("pid", list.get(position).getPid());
                            intent.putExtra("city", list.get(position).getCity());
                            intent.putExtra("cid", list.get(position).getCid());
                            intent.putExtra("area", list.get(position).getArea());
                            intent.putExtra("aid", list.get(position).getAid());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    builderDel.setNegativeButton("否", null);
                    builderDel.create().show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.btn_add:
                Intent intent = new Intent(this, AddressAddActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        new Thread() {
            public void run() {
                list = GetData.getAddressInfos(MyConfig.URL_JSON_ADDRESS + uid);
                if (!isDestroyed()) {
                    handler2.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
        super.onRestart();
    }
}
