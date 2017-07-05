package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.loosoo100.campus100.adapters.MessageDetailAdapter;
import com.loosoo100.campus100.adapters.MessageDetailCommAdapter;
import com.loosoo100.campus100.beans.MessageInfo;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import static com.loosoo100.campus100.utils.GetData.getMessageDetailListInfos;

/**
 * @author yang
 *         消息详情列表activity
 */
public class MessageDetailActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view

    @ViewInject(R.id.rl_back)
    private View rl_back;    //返回按钮

    @ViewInject(R.id.tv_title)
    private TextView tv_title;    //消息详情topbar标题

    @ViewInject(R.id.lv_message_detail)
    private ListView lv_message_detail;    //消息详情列表
    @ViewInject(R.id.progress)
    private RelativeLayout progress;
    @ViewInject(R.id.progress_update_whitebg)
    private RelativeLayout progress_update_whitebg;

    private List<MessageInfo> messageInfos;
    private MessageDetailAdapter adapter;
    private MessageDetailCommAdapter adapterComm;
    private String uid = "";
    private String type = "";

    private Intent intent = new Intent();

    private boolean isLoading = true;
    private int page = 2;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (messageInfos != null && messageInfos.size() > 0) {
                if (type.equals("1") || type.equals("2")) {
                    initListView();
                } else if (type.equals("3") || type.equals("4") || type.equals("6")) {
                    initListViewComm();
                }
            }
            isLoading = false;
            page = 2;
            progress.setVisibility(View.GONE);
        }
    };

    private Handler handlerUpdate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            if (adapterComm != null) {
                adapterComm.notifyDataSetChanged();
            }
            page++;
            isLoading = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");

        final String title = getIntent().getExtras().getString("title");
        type = getIntent().getExtras().getString("type");
        tv_title.setText(title + "消息");

        rl_back.setOnClickListener(this);
        progress.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                messageInfos = getMessageDetailListInfos(MyConfig.URL_JSON_MESSAGE_DETAIL_LIST + uid + "&type=" + type);
                if (!isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

        lv_message_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        @Override
                        public void run() {
                            List<MessageInfo> list2 = GetData.getMessageDetailListInfos(MyConfig.URL_JSON_MESSAGE_DETAIL_LIST + uid + "&type=" + type + "&page=" + page);
                            if (list2 != null && list2.size() > 0) {
                                for (int i = 0; i < list2.size(); i++) {
                                    messageInfos.add(list2.get(i));
                                }
                                if (!isDestroyed()) {
                                    handlerUpdate.sendEmptyMessage(0);
                                }
                            }

                        }
                    }.start();
                }
            }
        });
    }

    /**
     * 初始化列表
     */
    private void initListView() {
        adapter = new MessageDetailAdapter(this, messageInfos);
        lv_message_detail.setAdapter(adapter);

        lv_message_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageInfos.get(position).setStatus("1");
                adapter.notifyDataSetChanged();
                if (type.equals("1")) {
                    intent.setClass(MessageDetailActivity.this, StoreOrderDetailActivity.class);
                    intent.putExtra("oid", messageInfos.get(position).getOid());
                    intent.putExtra("index", 0);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                } else if (type.equals("2")) {
                    if (messageInfos.get(position).getType().equals("0")) {
                        intent.setClass(MessageDetailActivity.this, GiftOrderDetailReceiveActivity.class);
                        intent.putExtra("oid", messageInfos.get(position).getOid());
                        intent.putExtra("index", 0);
                        intent.putExtra("type", 3);
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("1")) {
                        intent.setClass(MessageDetailActivity.this, GiftOrderDetailSendActivity.class);
                        intent.putExtra("oid", messageInfos.get(position).getOid());
                        intent.putExtra("index", 0);
                        intent.putExtra("type", 3);
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("2")) {
                        intent.setClass(MessageDetailActivity.this, GiftOrderDetailTogetherActivity.class);
                        intent.putExtra("oid", messageInfos.get(position).getOid());
                        intent.putExtra("index", 0);
                        intent.putExtra("type", 3);
                        startActivity(intent);
                    }else if (messageInfos.get(position).getType().equals("4")) {
                        intent.setClass(MessageDetailActivity.this, GiftGetActivity.class);
                        intent.putExtra("oid",messageInfos.get(position).getOid());
                        startActivity(intent);
                    }

                }
            }
        });

        lv_message_detail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(MessageDetailActivity.this);
                builderDel.setMessage("是否确认删除");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update_whitebg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        postDelete(MyConfig.URL_POST_MESSAGE_DEL, messageInfos.get(position).getMid(), position);
                                    }
                                }.start();
                            }
                        });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                return true;
            }
        });
    }

    /**
     * 初始化列表--社团
     */
    private void initListViewComm() {
        adapterComm = new MessageDetailCommAdapter(this, messageInfos);
        lv_message_detail.setAdapter(adapterComm);

        lv_message_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageInfos.get(position).setStatus("1");
                adapterComm.notifyDataSetChanged();
                if (type.equals("3")) {
                    if (messageInfos.get(position).getType().equals("1") && messageInfos.get(position).getClassify().equals("1")) {//活动详情
                        intent.setClass(MessageDetailActivity.this, CommActivityDetailFreeActivity.class);
                        intent.putExtra("aid", messageInfos.get(position).getAid());
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("1") && messageInfos.get(position).getClassify().equals("0")) {//活动详情
                        intent.setClass(MessageDetailActivity.this, CommActivityDetailTogetherActivity.class);
                        intent.putExtra("aid", messageInfos.get(position).getAid());
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("2")) {//我的社团
                        intent.setClass(MessageDetailActivity.this, MyCommunityActivity.class);
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("3")) {//成员审核
                        intent.setClass(MessageDetailActivity.this, CommunityMemberCheckedActivity.class);
                        intent.putExtra("cid", messageInfos.get(position).getCid());
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("4")) {//成员列表
                        intent.setClass(MessageDetailActivity.this, CommunityMemberManageActivity.class);
                        intent.putExtra("cid", messageInfos.get(position).getCid());
                        startActivity(intent);
                    }
                } else if (type.equals("4")) {
                    intent.setClass(MessageDetailActivity.this, MyCreditActivity.class);
                    startActivity(intent);
                } else if (type.equals("6")) {
                    if (messageInfos.get(position).getType().equals("1")) {//暗恋的人
                        intent.setClass(MessageDetailActivity.this, CampusContactsLoveActivity.class);
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("2")) {//我的友友请求
                        intent.setClass(MessageDetailActivity.this, CampusContactsMyFriendRequestActivity.class);
                        startActivity(intent);
                    } else if (messageInfos.get(position).getType().equals("3")) {//我的友友列表
                        intent.setClass(MessageDetailActivity.this, CampusContactsMyFriendActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        lv_message_detail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CustomDialog.Builder builderDel = new CustomDialog.Builder(MessageDetailActivity.this);
                builderDel.setMessage("是否确认删除");
                builderDel.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress_update_whitebg.setVisibility(View.VISIBLE);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        postDelete(MyConfig.URL_POST_MESSAGE_DEL, messageInfos.get(position).getMid(), position);
                                    }
                                }.start();
                            }
                        });
                builderDel.setNegativeButton("否", null);
                builderDel.create().show();
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;
        }
    }

    /**
     * 删除消息
     *
     * @param uploadHost
     */
    private void postDelete(String uploadHost, String mid, final int mPosition) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        LogUtils.d("responseInfo", responseInfo.result);
                        String result = "";
                        progress_update_whitebg.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(
                                    responseInfo.result);
                            result = jsonObject.optString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result.equals("1")) {
//                            Message message = handlerDel.obtainMessage();
//                            message.what = mPosition;
//                            handlerDel.sendMessage(message);
                            if (type.equals("1") || type.equals("2")) {
                                messageInfos.remove(mPosition);
                                adapter.notifyDataSetChanged();
                            } else {
                                messageInfos.remove(mPosition);
                                adapterComm.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtil.showToast(MessageDetailActivity.this, "操作失败");
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        LogUtils.d("error", arg1.toString());
                        ToastUtil.showToast(MessageDetailActivity.this, "操作失败");
                        progress_update_whitebg.setVisibility(View.GONE);
                    }
                });
    }


}
