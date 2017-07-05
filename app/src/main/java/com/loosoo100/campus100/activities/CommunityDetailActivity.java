//package com.loosoo100.campus100.activities;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.loosoo100.campus100.R;
//import com.loosoo100.campus100.adapters.CommunityDepGridviewAdapter;
//import com.loosoo100.campus100.adapters.CommunityDetailActivityAdapter;
//import com.loosoo100.campus100.adapters.CommunityDetialMemberAdapter;
//import com.loosoo100.campus100.anyevent.MEventComJoin;
//import com.loosoo100.campus100.beans.CommunityActivityInfo;
//import com.loosoo100.campus100.beans.CommunityBasicInfo;
//import com.loosoo100.campus100.beans.CommunityMemberInfo;
//import com.loosoo100.campus100.config.MyConfig;
//import com.loosoo100.campus100.db.UserInfoDB;
//import com.loosoo100.campus100.utils.GetData;
//import com.loosoo100.campus100.utils.MyUtils;
//import com.loosoo100.campus100.view.CircleView;
//import com.loosoo100.campus100.view.CustomDialog;
//import com.loosoo100.campus100.view.MyScrollView;
//import com.loosoo100.campus100.view.MyScrollView.OnScrollListener;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 
// * @author yang 社团详情activity
// */
//public class CommunityDetailActivity extends Activity implements
//		OnClickListener, OnScrollListener {
//	@ViewInject(R.id.iv_empty)
//	private ImageView iv_empty; // 空view
//	@ViewInject(R.id.iv_empty2)
//	private ImageView iv_empty2; // 空view
//	@ViewInject(R.id.rl_back)
//	private RelativeLayout rl_back;
//	@ViewInject(R.id.rl_topbar)
//	private RelativeLayout rl_topbar; // 顶部颜色
//	@ViewInject(R.id.abscrollview)
//	private MyScrollView scrollview;
//	@ViewInject(R.id.cv_headShot)
//	private CircleView cv_headShot; // 社团头像
//	@ViewInject(R.id.tv_communityName)
//	private TextView tv_communityName; // 社团名称
//	@ViewInject(R.id.tv_id)
//	private TextView tv_id; // 社团号
//	@ViewInject(R.id.tv_school)
//	private TextView tv_school; // 所属学校
//	@ViewInject(R.id.tv_name)
//	private TextView tv_name; // 创建人
//	@ViewInject(R.id.tv_slogan)
//	private TextView tv_slogan; // 口号
//	@ViewInject(R.id.ll_notice)
//	private LinearLayout ll_notice; // 社团公告布局
//	@ViewInject(R.id.tv_notice)
//	private TextView tv_notice; // 社团公告
//	@ViewInject(R.id.tv_summary)
//	private TextView tv_summary; // 社团简介
//	@ViewInject(R.id.ll_more)
//	private LinearLayout ll_more; // 更多
//	@ViewInject(R.id.lv_activity)
//	private ListView lv_activity; // 活动列表
//	@ViewInject(R.id.gv_member)
//	private GridView gv_member; // 成员列表
//	@ViewInject(R.id.iv_back_bg)
//	private ImageView iv_back_bg; // 返回背景
//	@ViewInject(R.id.tv_type)
//	private TextView tv_type; // 社团类型
//	@ViewInject(R.id.gv_dep)
//	private GridView gv_dep; // 社团架构
//
//	@ViewInject(R.id.progress)
//	private RelativeLayout progress; // 加载动画
//	@ViewInject(R.id.progress_update_blackbg)
//	private RelativeLayout progress_update_blackbg; // 加载动画
//	@ViewInject(R.id.rl_more)
//	private RelativeLayout rl_more; // 更多
//	@ViewInject(R.id.rl_popupwindow)
//	private RelativeLayout rl_popupwindow; // 弹出菜单项
//	@ViewInject(R.id.btn_join)
//	private Button btn_join; // 加入社团
//	@ViewInject(R.id.btn_report)
//	private Button btn_report; // 举报社团
//	@ViewInject(R.id.dialog_report)
//	private RelativeLayout dialog_report; // 举报界面
//	@ViewInject(R.id.rl_black)
//	private RelativeLayout rl_black;
//	@ViewInject(R.id.btn01)
//	private Button btn01;
//	@ViewInject(R.id.btn02)
//	private Button btn02;
//	@ViewInject(R.id.btn03)
//	private Button btn03;
//	@ViewInject(R.id.btn04)
//	private Button btn04;
//	@ViewInject(R.id.btn05)
//	private Button btn05;
//
//	private String cid = ""; // 传进来的社团ID
//	// private int position = -1; // 传进来的position
//	private String sid = "";
//	private String uid = "";
//
//	private List<CommunityActivityInfo> list;
//	private CommunityBasicInfo communityBasicInfos;
//	private CommunityDetailActivityAdapter listAdapter;
//
//	private List<CommunityBasicInfo> basicInfos;
//	private CommunityDetialMemberAdapter gridViewAdapter;
//
//	private Intent intent = new Intent();
//
//	// private String[] contents;
//	private String content = "";
//	private Dialog dialog;
//	private EditText editText;
//	private List<CommunityMemberInfo> memberList;
//
//	// private boolean isSameSchool = true;
//
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (communityBasicInfos != null) {
//				initView();
//				initGridView();
//				initListView();
//				initDepGridView();
//				progress.setVisibility(View.GONE);
//			} else {
//				progress.setVisibility(View.VISIBLE);
//			}
//
//		}
//	};
//
//	private Handler handlerUpdate = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			list = communityBasicInfos.getActivityInfos();
//			if (list != null && list.size() > 0) {
//				listAdapter = new CommunityDetailActivityAdapter(
//						CommunityDetailActivity.this, list);
//				lv_activity.setAdapter(listAdapter);
//				// 设置listview的高度
//				MyUtils.setListViewHeight(lv_activity, 0);
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_community_detail);
//		ViewUtils.inject(this);
//
//		MyUtils.setStatusBarHeight(this, iv_empty);
//		MyUtils.setStatusBarHeight(this, iv_empty2);
//
//		cid = getIntent().getExtras().getString("id");
//		// position = getIntent().getExtras().getInt("position");
//
//		progress.setVisibility(View.VISIBLE);
//		sid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//				.getString(UserInfoDB.SCHOOL_ID, "");
//		uid = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
//				.getString(UserInfoDB.USERID, "");
//
//		// contents = new String[]{"虚假活动","淫秽色情","广告骚扰","恶意言论","其他"};
//
//		// 数据后台加载
//		new Thread() {
//			@Override
//			public void run() {
//				communityBasicInfos = GetData
//						.getCommunityDetailInfos(MyConfig.URL_JSON_COMMUNITY_DETAIL
//								+ cid + "&sid=" + sid + "&uid=" + uid);
//				if (!isDestroyed()) {
//					handler.sendEmptyMessage(0);
//				}
//			}
//		}.start();
//
//	}
//
//	/**
//	 * 社团架构
//	 */
//	private void initDepGridView() {
//		List<String> depList = new ArrayList<String>();
//		String[] dep = communityBasicInfos.getDep().split(",");
//		for (int i = 0; i < dep.length; i++) {
//			depList.add(dep[i]);
//		}
//		gv_dep.setAdapter(new CommunityDepGridviewAdapter(this, depList));
//		// 设置gridview的高度
//		MyUtils.setGridViewHeight(gv_dep, 0, 4);
//	}
//
//	private void initGridView() {
//		memberList = communityBasicInfos.getMemberList();
//		// 判断是否该社团成员，是则显示成员的真实姓名和QQ，否则不显示
//		if (communityBasicInfos.getIn().equals("1")
//				|| communityBasicInfos.getIn().equals("2")) {
//			gridViewAdapter = new CommunityDetialMemberAdapter(this,
//					memberList, true);
//		} else {
//			gridViewAdapter = new CommunityDetialMemberAdapter(this,
//					memberList, false);
//		}
//
//		gv_member.setAdapter(gridViewAdapter);
//		gv_member.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (uid.equals(memberList.get(position).getUserId())) {
//					Intent intent = new Intent(CommunityDetailActivity.this,
//							CampusContactsPersonalActivity.class);
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(CommunityDetailActivity.this,
//							CampusContactsFriendActivity.class);
//					// intent.putExtra("uid", uid);
//					// intent.putExtra("sex", memberList.get(position).getSex()
//					// + "");
//					intent.putExtra("muid", memberList.get(position)
//							.getUserId());
//					// intent.putExtra("headShot", memberList.get(position)
//					// .getHeadShotString());
//					// intent.putExtra("name",
//					// memberList.get(position).getNickName());
//					startActivity(intent);
//				}
//			}
//		});
//		// 设置gridview的高度
//		MyUtils.setGridViewHeight(gv_member, 0, 2);
//	}
//
//	private void initListView() {
//		list = communityBasicInfos.getActivityInfos();
//		listAdapter = new CommunityDetailActivityAdapter(this, list);
//		lv_activity.setAdapter(listAdapter);
//
//		lv_activity.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				intent.setClass(CommunityDetailActivity.this,
//						CommunityActivityDetailActivity.class);
//				intent.putExtra("id", list.get(position).getId());
//				startActivityForResult(intent, 0);
//			}
//		});
//		// 设置listview的高度
//		MyUtils.setListViewHeight(lv_activity, 0);
//	}
//
//	private void initView() {
//		rl_back.setOnClickListener(this);
//		ll_more.setOnClickListener(this);
//		rl_more.setOnClickListener(this);
//		btn_join.setOnClickListener(this);
//		btn_report.setOnClickListener(this);
//		rl_black.setOnClickListener(this);
//		btn01.setOnClickListener(this);
//		btn02.setOnClickListener(this);
//		btn03.setOnClickListener(this);
//		btn04.setOnClickListener(this);
//		btn05.setOnClickListener(this);
//		scrollview.setOnScrollListener(this);
//
//		tv_communityName.setText(communityBasicInfos.getCommunityName());
//		tv_id.setText("" + communityBasicInfos.getId());
//		tv_school.setText(communityBasicInfos.getSchool());
//		tv_name.setText(communityBasicInfos.getName());
//		tv_slogan.setText(communityBasicInfos.getSlogan());
//		tv_summary.setText(communityBasicInfos.getSummary());
//		tv_notice.setText(communityBasicInfos.getNotice());
//
//		tv_type.setText(communityBasicInfos.getType());
//
//		if (communityBasicInfos.getIn().equals("0")) {
//			btn_join.setText("加入社团");
//		} else if (communityBasicInfos.getIn().equals("1")) {
//			btn_join.setText("退出社团");
//			ll_notice.setVisibility(View.VISIBLE);
//		} else if (communityBasicInfos.getIn().equals("-1")) {
//			btn_join.setText("审核中");
//		} else if (communityBasicInfos.getIn().equals("2")) {
//			btn_join.setText("编辑成员");
//			ll_notice.setVisibility(View.VISIBLE);
//			btn_report.setVisibility(View.GONE);
//		} else {
//			// 不同学校不能申请加入和举报
//			rl_more.setVisibility(View.GONE);
//			// isSameSchool = false;
//		}
//
//		if (!communityBasicInfos.getHeadthumb().equals("")
//				&& !communityBasicInfos.getHeadthumb().equals("null")) {
//			Glide.with(this).load(communityBasicInfos.getHeadthumb())
//					.into(cv_headShot);
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.rl_back:
//			this.finish();
//			break;
//
//		case R.id.ll_more:
//			Intent intent = new Intent(this,
//					CommunityActivityMoreActivity.class);
//			intent.putExtra("id", communityBasicInfos.getId());
//			startActivity(intent);
//			break;
//
//		case R.id.rl_black:
//			if (dialog_report.getVisibility() == View.VISIBLE) {
//				dialog_report.setVisibility(View.GONE);
//			}
//			break;
//
//		// 右上角更多
//		case R.id.rl_more:
//			if (rl_popupwindow.getVisibility() == View.GONE) {
//				rl_popupwindow.setVisibility(View.VISIBLE);
//			} else {
//				rl_popupwindow.setVisibility(View.GONE);
//			}
//			break;
//
//		// 右上角加入社团
//		case R.id.btn_join:
//			rl_popupwindow.setVisibility(View.GONE);
//			if (btn_join.getText().toString().equals("加入社团")) {
//				new Thread() {
//					public void run() {
//						postJoin(MyConfig.URL_POST_COMMUNITY_JOIN);
//					};
//				}.start();
//			} else if (btn_join.getText().toString().equals("退出社团")) {
//				CustomDialog.Builder builder = new CustomDialog.Builder(this);
//				builder.setMessage("是否确认退出");
//				builder.setPositiveButton("是",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								new Thread() {
//									public void run() {
//										postOut(MyConfig.URL_POST_COMMUNITY_OUT
//												+ "?uid=" + uid + "&cid=" + cid
//												+ "&sid=" + sid);
//									};
//								}.start();
//							}
//						});
//				builder.setNegativeButton("否", null);
//				builder.create().show();
//			} else if (btn_join.getText().toString().equals("编辑成员")) {
//				Intent intent2 = new Intent(this,
//						CommunityMemberManageActivity.class);
//				intent2.putExtra("cid", cid);
//				intent2.putExtra("uid", uid);
//				intent2.putExtra("sid", sid);
//				startActivity(intent2);
//			}
//			break;
//
//		// 右上角举报社团
//		case R.id.btn_report:
//			rl_popupwindow.setVisibility(View.GONE);
//			dialog_report.setVisibility(View.VISIBLE);
//			break;
//
//		case R.id.btn01:
//			dialog_report.setVisibility(View.GONE);
//			content = "虚假活动";
//			progress_update_blackbg.setVisibility(View.VISIBLE);
//			new Thread() {
//				public void run() {
//					doPostReport(MyConfig.URL_POST_REPORT);
//				};
//			}.start();
//			break;
//
//		case R.id.btn02:
//			dialog_report.setVisibility(View.GONE);
//			content = "淫秽色情";
//			progress_update_blackbg.setVisibility(View.VISIBLE);
//			new Thread() {
//				public void run() {
//					doPostReport(MyConfig.URL_POST_REPORT);
//				};
//			}.start();
//			break;
//
//		case R.id.btn03:
//			dialog_report.setVisibility(View.GONE);
//			content = "广告骚扰";
//			progress_update_blackbg.setVisibility(View.VISIBLE);
//			new Thread() {
//				public void run() {
//					doPostReport(MyConfig.URL_POST_REPORT);
//				};
//			}.start();
//			break;
//
//		case R.id.btn04:
//			dialog_report.setVisibility(View.GONE);
//			content = "恶意言论";
//			progress_update_blackbg.setVisibility(View.VISIBLE);
//			new Thread() {
//				public void run() {
//					doPostReport(MyConfig.URL_POST_REPORT);
//				};
//			}.start();
//			break;
//
//		case R.id.btn05:
//			dialog_report.setVisibility(View.GONE);
//			dialog = new Dialog(this, R.style.MyDialog);
//			LayoutInflater inflater = LayoutInflater.from(this);
//			View view = inflater.inflate(R.layout.dialog_report_other, null);
//			dialog.setContentView(view);
//			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
//					LayoutParams.MATCH_PARENT);
//			dialog.setContentView(view, params);
//			editText = (EditText) view.findViewById(R.id.et_reason);
//			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//			Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
//
//			btn_cancel.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//			btn_ok.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					content = editText.getText().toString().trim();
//					if (content.equals("")) {
//						Toast.makeText(CommunityDetailActivity.this, "请输入举报内容",
//								0).show();
//						return;
//					}
//					dialog.dismiss();
//					progress_update_blackbg.setVisibility(View.VISIBLE);
//					new Thread() {
//						public void run() {
//							doPostReport(MyConfig.URL_POST_REPORT);
//						};
//					}.start();
//				}
//			});
//			dialog.show();
//			// 显示软键盘
//			MyUtils.showSoftInput(this, editText);
//			break;
//
//		}
//	}
//
//	@Override
//	public void onScroll(int scrollY) {
//		// rl_topbar.setAlpha((float) scrollY / 500);
//		// iv_empty.setAlpha((float) scrollY / 500);
//		// iv_back_bg.setAlpha(0.3f - (float) scrollY / 500);
//		rl_popupwindow.setVisibility(View.GONE);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK && requestCode == 0) {
//			new Thread() {
//				@Override
//				public void run() {
//					communityBasicInfos = GetData
//							.getCommunityDetailInfos(MyConfig.URL_JSON_COMMUNITY_DETAIL
//									+ cid + "&sid=" + sid + "&uid=" + uid);
//					if (communityBasicInfos != null && !isDestroyed()) {
//						handlerUpdate.sendEmptyMessage(0);
//					}
//				};
//			}.start();
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	/**
//	 * 加入社团
//	 * 
//	 * @param uploadHost
//	 */
//	private void postJoin(String uploadHost) {
//		HttpUtils httpUtils = new HttpUtils();
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("userid", uid);
//		params.addBodyParameter("comnid", communityBasicInfos.getId());
//		params.addBodyParameter("sid", sid);
//		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
//				new RequestCallBack<String>() {
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						System.out.println("responseInfo.result**"
//								+ responseInfo.result);
//						String result = "";
//						try {
//							JSONObject jsonObject = new JSONObject(
//									responseInfo.result);
//							result = jsonObject.getString("status");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						if (result.equals("1")) {
//							btn_join.setText("审核中");
//							// if (position!=-1) {
//							// CommunityJoinActivity.list.get(position).setStatus("0");
//							// CommunityJoinActivity.adapter.notifyDataSetChanged();
//							// }
//							EventBus.getDefault().post(new MEventComJoin("0"));
//							Toast.makeText(CommunityDetailActivity.this,
//									"提交成功,请耐心等待审核", 0).show();
//						} else if (result.equals("-1")) {
//							CustomDialog.Builder builder = new CustomDialog.Builder(
//									CommunityDetailActivity.this);
//							builder.setMessage("请先完善个人资料");
//							builder.setPositiveButton("是",
//									new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//											Intent intent = new Intent(
//													CommunityDetailActivity.this,
//													BasicInfoActivity.class);
//											startActivity(intent);
//										}
//									});
//							builder.setNegativeButton("否", null);
//							builder.create().show();
//						} else {
//							Toast.makeText(CommunityDetailActivity.this,
//									"提交失败", 0).show();
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						System.out.println("上传数据失败" + arg1.toString());
//						Toast.makeText(CommunityDetailActivity.this, "提交失败", 0)
//								.show();
//					}
//				});
//	}
//
//	/**
//	 * 退出社团
//	 * 
//	 * @param uploadHost
//	 */
//	private void postOut(String uploadHost) {
//		HttpUtils httpUtils = new HttpUtils();
//		httpUtils.send(HttpRequest.HttpMethod.GET, uploadHost,
//				new RequestCallBack<String>() {
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						System.out.println("responseInfo.result**"
//								+ responseInfo.result);
//						String result = "";
//						try {
//							JSONObject jsonObject = new JSONObject(
//									responseInfo.result);
//							result = jsonObject.getString("status");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						if (result.equals("1")) {
//							Toast.makeText(CommunityDetailActivity.this,
//									"退出成功", 0).show();
//							// if (position!=-1) {
//							// CommunityJoinActivity.list.get(position).setStatus("1");
//							// CommunityJoinActivity.adapter.notifyDataSetChanged();
//							// }
//							EventBus.getDefault().post(new MEventComJoin("1"));
//							CommunityDetailActivity.this.finish();
//						} else {
//							Toast.makeText(CommunityDetailActivity.this,
//									"操作失败", 0).show();
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						System.out.println("上传数据失败" + arg1.toString());
//						Toast.makeText(CommunityDetailActivity.this, "操作失败", 0)
//								.show();
//					}
//				});
//	}
//
//	/**
//	 * 举报
//	 * 
//	 * @param uploadHost
//	 */
//	private void doPostReport(String uploadHost) {
//		HttpUtils httpUtils = new HttpUtils();
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("type", "3");
//		params.addBodyParameter("rid", cid);
//		params.addBodyParameter("sid", sid);
//		params.addBodyParameter("uid", uid);
//		params.addBodyParameter("content", content);
//		httpUtils.send(HttpRequest.HttpMethod.POST, uploadHost, params,
//				new RequestCallBack<String>() {
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						System.out.println(responseInfo.result);
//						progress_update_blackbg.setVisibility(View.GONE);
//						String result = "";
//						try {
//							JSONObject jsonObject = new JSONObject(
//									responseInfo.result);
//							result = jsonObject.getString("status");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						if (result.equals("0")) {
//							Toast.makeText(CommunityDetailActivity.this,
//									"操作失败", 0).show();
//						} else if (result.equals("-1")) {
//							Toast.makeText(CommunityDetailActivity.this,
//									"您已举报过了", 0).show();
//						} else {
//							Toast.makeText(CommunityDetailActivity.this,
//									"举报成功", 0).show();
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						System.out.println("上传失败" + arg1.toString());
//						progress_update_blackbg.setVisibility(View.GONE);
//						Toast.makeText(CommunityDetailActivity.this, "操作失败", 0)
//								.show();
//					}
//				});
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (rl_popupwindow.getVisibility() == View.VISIBLE) {
//			rl_popupwindow.setVisibility(View.GONE);
//			return true;
//		}
//		if (dialog_report.getVisibility() == View.VISIBLE) {
//			dialog_report.setVisibility(View.GONE);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//}
