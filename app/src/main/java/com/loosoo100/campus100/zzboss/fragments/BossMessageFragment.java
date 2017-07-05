package com.loosoo100.campus100.zzboss.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.application.MyApplication;
import com.loosoo100.campus100.chat.adapter.ConversationListAdapterEx;
import com.loosoo100.campus100.utils.MyUtils;
import com.loosoo100.campus100.zzboss.chat.fragment.BossContactsFragment;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 
 * @author yang 消息fragment
 */
public class BossMessageFragment extends Fragment implements OnClickListener {

	private View view;
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.btn_message)
	private Button btn_message; // 消息
	@ViewInject(R.id.btn_comm)
	private Button btn_comm; // 社团
	@ViewInject(R.id.iv_whiteline)
	private ImageView iv_whiteline; // 白线条
	@ViewInject(R.id.vp)
	private ViewPager vp;

	private float x = 0; // 记录当前白线条所在位置
	private int width; // 屏幕宽度

	private Activity activity;

	private List<Fragment> fragments = new ArrayList<>();
	/**
	 * 会话列表的fragment
	 */
	private Fragment mConversationListFragment = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_boss_message, container,
				false);
		ViewUtils.inject(this, view); // 注入view和事件
		activity = getActivity();

		MyUtils.setStatusBarHeight(activity, iv_empty);

		WindowManager systemService = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		width = systemService.getDefaultDisplay().getWidth() / 4;
		// 设置红条的宽度和高度
		LayoutParams params = new LayoutParams(width, 5);
		iv_whiteline.setLayoutParams(params);
		iv_whiteline.startAnimation(translateAnimation2(1));
		x = width;

		btn_message.setOnClickListener(this);
		btn_comm.setOnClickListener(this);

		initViewPager();

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_message:
			if (x == width) {
				return;
			}
			vp.setCurrentItem(0);
			break;

		case R.id.btn_comm:
			if (x == 2 * width) {
				return;
			}
			vp.setCurrentItem(1);

		}
	}

	private void initViewPager() {
		Fragment conversationList = initConversationList();
		fragments.add(conversationList);
		fragments.add(new BossContactsFragment().newInstance("contact","contact"));
		final FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return fragments.get(position);
			}

			@Override
			public int getCount() {
				return fragments.size();
			}
		};
		vp.setAdapter(fragmentPagerAdapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					btn_message.setTextSize(16);
					btn_comm.setTextSize(14);
					iv_whiteline.startAnimation(translateAnimation(1));
					x = width;
				} else {
					btn_comm.setTextSize(16);
					btn_message.setTextSize(14);
					iv_whiteline.startAnimation(translateAnimation(2));
					x = 2 * width;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private Fragment initConversationList() {
		if (mConversationListFragment == null) {
			ConversationListFragment listFragment = ConversationListFragment.getInstance();
			listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
			Uri uri = Uri.parse("rong://" + MyApplication.getInstance().getApplicationInfo().packageName).buildUpon()
					.appendPath("conversationlist")
					.appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
					.appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
					.appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
					.appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
					.appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
					.build();
			listFragment.setUri(uri);
			return listFragment;
		} else {
			return mConversationListFragment;
		}
	}

	/**
	 * 设置平移动画
	 * 
	 * @return
	 */
	private TranslateAnimation translateAnimation(int index) {
		TranslateAnimation translateAnimation = new TranslateAnimation(x, index
				* width, 0, 0);
		translateAnimation.setDuration(300);
		translateAnimation.setFillBefore(true);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

	/**
	 * 设置平移动画
	 * 
	 * @return
	 */
	private TranslateAnimation translateAnimation2(int index) {
		TranslateAnimation translateAnimation = new TranslateAnimation(x, index
				* width, 0, 0);
		translateAnimation.setDuration(0);
		translateAnimation.setFillBefore(true);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}

}
