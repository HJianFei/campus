package com.loosoo100.campus100.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CampusContactsFriendActivity;
import com.loosoo100.campus100.activities.GiftGoodsDetailActivity;
import com.loosoo100.campus100.activities.InviteListActivity;
import com.loosoo100.campus100.activities.MyCollectActivity;
import com.loosoo100.campus100.adapters.InviteFriendAdapter;
import com.loosoo100.campus100.adapters.MyCollectAdapter;
import com.loosoo100.campus100.beans.MyCollectInfo;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;

import java.util.List;

/**
 * 邀请列表-友友
 *
 * @author yang
 */
public class InviteFriendController extends BaseController {

    private LayoutInflater inflater;

    private InviteListActivity activity;

    private ListView listView;
    private RelativeLayout progress;
    private TextView tv_count;
    private TextView tv_phone;

    private InviteFriendAdapter adapter;
    private List<UserInfo> list;
    private String uid = "";
    private boolean isLoading = true;
    private int page = 1;
    private int count;
    private boolean first = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (list != null && list.size() > 0) {
                initListView();
                listView.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.GONE);
            }
            if (count != -1) {
                tv_count.setText(count + "");
            }
            page = 2;
            isLoading = false;
            progress.setVisibility(View.GONE);
        }

        ;
    };

    private Handler handlerRefresh = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            isLoading = false;
            page++;
        }

        ;
    };

    public InviteFriendController(Context context) {
        super(context);

        activity = (InviteListActivity) context;

    }

    @Override
    public void initData() {
        if (!first) {
            return;
        }
        first = false;
        listView = (ListView) mRootView.findViewById(R.id.listView);
        progress = (RelativeLayout) mRootView.findViewById(R.id.progress);
        tv_count = (TextView) mRootView.findViewById(R.id.tv_count);
        tv_phone = (TextView) mRootView.findViewById(R.id.tv_phone);

        uid = activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        tv_phone.setText(activity.getSharedPreferences(UserInfoDB.USERTABLE,
                activity.MODE_PRIVATE).getString(UserInfoDB.PHONE, ""));

        progress.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                list = GetData
                        .getInviteUserInfos(MyConfig.URL_JSON_AGENTLIST
                                + "?uid=" + uid + "&type=0");
                count = GetData
                        .getInviteCount(MyConfig.URL_JSON_AGENTLIST
                                + "?uid=" + uid + "&type=0");
                if (!activity.isDestroyed()) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();
        super.initData();
    }

    @Override
    protected View initView(Context context) {
        inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.controller_invite_friend, null);
    }

    private void initListView() {
        adapter = new InviteFriendAdapter(activity, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(activity,
                        CampusContactsFriendActivity.class);
                intent.putExtra("muid", list.get(position).getUid());
                activity.startActivity(intent);
            }
        });

        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && totalItemCount > 0 && !isLoading) {
                    isLoading = true;
                    new Thread() {
                        public void run() {
                            List<UserInfo> list2 = GetData
                                    .getInviteUserInfos(MyConfig.URL_JSON_AGENTLIST
                                            + "?uid=" + uid + "&type=0" + "&page=" + page);
                            if (list2 != null && list2.size() > 0) {
                                for (int i = 0; i < list2.size(); i++) {
                                    list.add(list2.get(i));
                                }
                                if (!activity.isDestroyed()) {
                                    handlerRefresh.sendEmptyMessage(0);
                                }
                            }
                        }

                        ;
                    }.start();
                }
            }
        });
    }


}
