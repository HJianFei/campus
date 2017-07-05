package com.loosoo100.campus100.chat.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.GridViewAdapter;
import com.loosoo100.campus100.chat.bean.GroupInfo;
import com.loosoo100.campus100.chat.bean.Group_Info;
import com.loosoo100.campus100.chat.bean.Groupnumber;
import com.loosoo100.campus100.chat.bean.RequestCode_1;
import com.loosoo100.campus100.chat.utils.GsonRequest;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.OperationRong;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.chat.widget.DemoGridView;
import com.loosoo100.campus100.chat.widget.switchbutton.SwitchButton;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.view.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;


public class GroupDetailActivity extends FragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.gridview)
    private DemoGridView mGridView;
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty; // 空view
    @ViewInject(R.id.rl_back_group_detail)
    private RelativeLayout rl_back_group_detail;
    @ViewInject(R.id.group_member_size)
    private TextView group_member_size;
    @ViewInject(R.id.group_member_size_item)
    private RelativeLayout group_member_size_item;
    @ViewInject(R.id.ll_group_port)
    private LinearLayout ll_group_port;
    @ViewInject(R.id.group_header)
    private ImageView group_header;
    @ViewInject(R.id.ll_group_name)
    private LinearLayout ll_group_name;
    @ViewInject(R.id.group_descriptions)
    private LinearLayout group_descriptions;
    @ViewInject(R.id.group_desc)
    private TextView group_desc;
    @ViewInject(R.id.group_name)
    private TextView group_name;
    @ViewInject(R.id.group_quit)
    private Button group_quit;
    @ViewInject(R.id.group_dismiss)
    private Button group_dismiss;
    @ViewInject(R.id.sw_group_notfaction)
    private SwitchButton sw_group_notifition;
    @ViewInject(R.id.sw_group_top)
    private SwitchButton sw_group_top;
    @ViewInject(R.id.group_clean)
    private LinearLayout group_clean;
    private GroupInfo groupInfo = new GroupInfo();
    private String ed_name = "";
    private Dialog dialog;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String fileName = MyConfig.FILE_URI + "/group.jpg";
    private Uri imageUri;
    private String gid = "";
    private String uid = "";
    private int page = 1;
    private GridViewAdapter mAdapter;
    private Groupnumber groupNumbers = new Groupnumber();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1001) {
                groupInfo = (GroupInfo) msg.obj;
                group_name.setText(groupInfo.getName());
                Glide.with(GroupDetailActivity.this)
                        .load(MyConfig.PIC_AVATAR + groupInfo.getAvatar())
//                        .placeholder(R.drawable.default_chatroom)
                        .error(R.drawable.default_chatroom)
                        .into(group_header);
                group_desc.setText(groupInfo.getDescription());
            }
            if (msg.arg1 == 1002) {
                group_name.setText(ed_name);
            }
            if (msg.arg1 == 1003) {
                group_desc.setText(ed_name);
            }
            super.handleMessage(msg);
        }
    };
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ViewUtils.inject(this);
        MyUtils.setStatusBarHeight(this, iv_empty);
        rl_back_group_detail.setOnClickListener(this);
        gid = getIntent().getStringExtra("targetId");
        name = getIntent().getStringExtra("name");
        uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE).getString(UserInfoDB.USERID, "");
        group_quit.setOnClickListener(this);
        group_dismiss.setOnClickListener(this);
        ll_group_port.setOnClickListener(this);
        ll_group_name.setOnClickListener(this);
        group_descriptions.setOnClickListener(this);
        group_member_size_item.setOnClickListener(this);
        sw_group_notifition.setOnCheckedChangeListener(this);
        sw_group_top.setOnCheckedChangeListener(this);
        group_clean.setOnClickListener(this);
        getGroupInfo();

    }

    private void getGroupInfo() {
        GsonRequest<GroupInfo> gsonRequest = new GsonRequest<GroupInfo>(MyConfig.GET_GROUP_DETAIL + "?id=" + gid, GroupInfo.class, new Response.Listener<GroupInfo>() {
            @Override
            public void onResponse(GroupInfo groupInfo) {
                Message message = Message.obtain();
                message.arg1 = 1001;
                message.obj = groupInfo;
                handler.sendMessage(message);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);
    }

    @Override
    protected void onResume() {
        initData(gid);
        super.onResume();
    }

    private void initData(final String gid) {
        LogUtils.d("onResponse", MyConfig.GET_GROUP_NUMBER + "?gid=" + gid + "&uid=" + uid);
        GsonRequest<Groupnumber> gsonRequest = new GsonRequest<>(MyConfig.GET_GROUP_NUMBER + "?gid=" + gid + "&uid=" + uid + "&page=" + page, Groupnumber.class, new Response.Listener<Groupnumber>() {
            @Override
            public void onResponse(Groupnumber groupnumber) {
                if (null != groupnumber) {
                    groupNumbers = groupnumber;
                    initView();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(gsonRequest);

    }

    private void initView() {
        group_member_size.setText("全部群组成员(" + groupNumbers.getRoot().size() + ")");
        if (uid.equals(groupNumbers.getUid())) {
            group_dismiss.setVisibility(View.VISIBLE);
            group_quit.setVisibility(View.GONE);
        } else {
            group_dismiss.setVisibility(View.GONE);
            group_quit.setVisibility(View.VISIBLE);
        }
        mAdapter = new GridViewAdapter(GroupDetailActivity.this, groupNumbers, gid);
        mGridView.setAdapter(mAdapter);
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupInfo.getId(), new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        sw_group_top.setChecked(true);
                    } else {
                        sw_group_top.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, groupInfo.getId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                    if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                        sw_group_notifition.setChecked(true);
                    } else {
                        sw_group_notifition.setChecked(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back_group_detail:
                this.finish();
                break;
            case R.id.group_dismiss:
                CustomDialog.Builder builder = new CustomDialog.Builder(GroupDetailActivity.this);
                builder.setMessage("确认解散群组？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissGroup(gid, uid);
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create().show();

                break;
            case R.id.group_quit:
                CustomDialog.Builder builder1 = new CustomDialog.Builder(GroupDetailActivity.this);
                builder1.setMessage("确认退出群组？");
                builder1.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        quiteGroup(gid, uid);
                    }
                });
                builder1.setNegativeButton("否", null);
                builder1.create().show();

                break;
            case R.id.ll_group_port:
                if (uid.equals(groupNumbers.getUid())) {
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

                }
                break;
            case R.id.ll_group_name:
                if (uid.equals(groupNumbers.getUid())) {
                    dialog = new Dialog(this, R.style.MyDialog);
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View viewDialog = inflater.inflate(R.layout.layout_alert_dialog, null);
                    dialog.setContentView(viewDialog);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(viewDialog, params);
                    TextView tv_title = (TextView) viewDialog.findViewById(R.id.tv_title);
                    tv_title.setText("修改群名称");
                    final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
                    Button btn_ok = (Button) viewDialog.findViewById(R.id.btn_ok);
                    Button btn_cancel = (Button) viewDialog.findViewById(R.id.btn_cancel);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ed_name = ed_description.getText().toString().trim();
                            if (!TextUtils.isEmpty(ed_name)) {
                                dialog.dismiss();
                                updateGroupInfo(ed_name, gid);
                            } else {
                                ToastUtil.showToast(GroupDetailActivity.this, "群描述不能为空");
                                return;
                            }
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();

                }
                break;
            case R.id.group_descriptions:
                if (uid.equals(groupNumbers.getUid())) {
                    dialog = new Dialog(this, R.style.MyDialog);
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View viewDialog = inflater.inflate(R.layout.layout_alert_dialog, null);
                    dialog.setContentView(viewDialog);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.setContentView(viewDialog, params);
                    final EditText ed_description = (EditText) viewDialog.findViewById(R.id.ed_description);
                    Button btn_ok = (Button) viewDialog.findViewById(R.id.btn_ok);
                    Button btn_cancel = (Button) viewDialog.findViewById(R.id.btn_cancel);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ed_name = ed_description.getText().toString().trim();
                            if (!TextUtils.isEmpty(ed_name)) {
                                dialog.dismiss();
                                updateGroupInfo_Desc(ed_name, gid);
                            } else {
                                ToastUtil.showToast(GroupDetailActivity.this, "群描述不能为空");
                                return;
                            }
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.group_member_size_item:
                Intent intent = new Intent(GroupDetailActivity.this, AllGroupMembersActivity.class);
                intent.putExtra("group_id", gid);
                startActivity(intent);
                break;
            case R.id.group_clean:
                CustomDialog.Builder builder_group = new CustomDialog.Builder(GroupDetailActivity.this);
                builder_group.setMessage("是否聊天记录？");
                builder_group.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (RongIM.getInstance() != null) {

                            RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupInfo.getId(), new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    ToastUtil.showToast(GroupDetailActivity.this, "清除成功");
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    ToastUtil.showToast(GroupDetailActivity.this, "清除失败，稍后再试");
                                }
                            });
                        }
                    }
                });
                builder_group.setNegativeButton("否", null);
                builder_group.create().show();
                break;
        }

    }

    private void updateGroupInfo_Desc(final String ed_name, final String gid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.UPDATE_GROUP_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RequestCode_1 requestCode_1 = gson.fromJson(response.toString(), RequestCode_1.class);
                if (requestCode_1.getCode() == 200) {
                    Message message = Message.obtain();
                    message.arg1 = 1003;
                    handler.sendMessage(message);
                    ToastUtil.showToast(GroupDetailActivity.this, "更改成功");
                } else {
                    ToastUtil.showToast(GroupDetailActivity.this, "操作失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                ToastUtil.showToast(GroupDetailActivity.this, "操作失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", gid);
                map.put("description", ed_name);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

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
                        group_header.setImageBitmap(bitmap);
                        try {
                            saveUserIcon(bitmap);
                            updateGroupAvatar(gid, MyConfig.IMAGGE_URI + gid + ".png");
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

    private void updateGroupAvatar(String gid, String filePath) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", gid);
        params.addBodyParameter("avatar", new File(filePath), "image/jpeg");
        httpUtils.send(HttpRequest.HttpMethod.POST, MyConfig.UPDATE_GROUP_AVATAR, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Gson gson = new Gson();
                        Group_Info group_info = gson.fromJson(responseInfo.result, Group_Info.class);
                        if (group_info.getCode() == 200) {
                            RongIM.getInstance().refreshGroupInfoCache(new Group(group_info.getId(), name, Uri.parse(MyConfig.PIC_AVATAR + group_info.getAvatar())));
                            ToastUtil.showToast(GroupDetailActivity.this, "更新成功");
                        }

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(GroupDetailActivity.this, "更新失败");

                    }
                });
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

    private void updateGroupInfo(final String name, final String gid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.UPDATE_GROUP_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                RequestCode_1 requestCode_1 = gson.fromJson(response.toString(), RequestCode_1.class);
                if (requestCode_1.getCode() == 200) {
                    Message message = Message.obtain();
                    message.arg1 = 1002;
                    handler.sendMessage(message);
                    ToastUtil.showToast(GroupDetailActivity.this, "更改成功");
                } else {
                    ToastUtil.showToast(GroupDetailActivity.this, "操作失败，稍后再试");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                ToastUtil.showToast(GroupDetailActivity.this, "操作失败，稍后再试");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", gid);
                map.put("name", name);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    /**
     * 退出群组
     *
     * @param gid
     * @param uid
     */

    private void quiteGroup(final String gid, final String uid) {
        final String[] uids = new String[]{uid};
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.QUIET_GROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, gid);
                ToastUtil.showToast(GroupDetailActivity.this, "退出成功");
                setResult(102);
                GroupDetailActivity.this.finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(GroupDetailActivity.this, "退出失败，稍后再试");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", Arrays.toString(uids));
                map.put("gid", gid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    /**
     * 解散群组
     *
     * @param gid
     * @param uid
     */

    private void dismissGroup(final String gid, final String uid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConfig.DISMESS_GROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, gid);
                ToastUtil.showToast(GroupDetailActivity.this, "群组解散成功");
                setResult(102);
                GroupDetailActivity.this.finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(GroupDetailActivity.this, "操作失败，稍后再试");
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", uid);
                map.put("id", gid);
                return map;
            }
        };
        MyApplication.getRequestQueue().add(stringRequest);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_group_top:
                if (isChecked) {
                    if (groupInfo != null) {
                        OperationRong.setConversationTop(GroupDetailActivity.this, Conversation.ConversationType.GROUP, groupInfo.getId(), true);
                    }
                } else {
                    if (groupInfo != null) {
                        OperationRong.setConversationTop(GroupDetailActivity.this, Conversation.ConversationType.GROUP, groupInfo.getId(), false);
                    }
                }
                break;
            case R.id.sw_group_notfaction:
                if (isChecked) {
                    if (groupInfo != null) {
                        OperationRong.setConverstionNotif(GroupDetailActivity.this, Conversation.ConversationType.GROUP, groupInfo.getId(), true);
                    }
                } else {
                    if (groupInfo != null) {
                        OperationRong.setConverstionNotif(GroupDetailActivity.this, Conversation.ConversationType.GROUP, groupInfo.getId(), false);
                    }
                }

                break;
        }
    }
}
