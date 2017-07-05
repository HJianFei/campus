package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.MyViewPagerAdapter;
import com.loosoo100.campus100.controller.BaseController;
import com.loosoo100.campus100.controller.InviteCommController;
import com.loosoo100.campus100.controller.InviteFriendController;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yang 邀请列表activity
 */
public class InviteListActivity extends Activity implements OnClickListener {
    @ViewInject(R.id.iv_empty)
    private ImageView iv_empty;
    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.btn01)
    private Button btn01; // 友友
    @ViewInject(R.id.btn02)
    private Button btn02; // 社团
    @ViewInject(R.id.iv_redline)
    private ImageView iv_redline; // 红条
    @ViewInject(R.id.vp)
    private ViewPager vp;

    private List<BaseController> controllers = new ArrayList<BaseController>();
    private InviteFriendController friendController;
    private InviteCommController commController;

    private float x = 0; // 记录当前白线条所在位置
    private int width; // 屏幕宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_list);
        ViewUtils.inject(this);

        MyUtils.setStatusBarHeight(this, iv_empty);

        WindowManager systemService = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = systemService.getDefaultDisplay().getWidth() / 8;

        // 设置红条的宽度和高度
        LayoutParams params = new LayoutParams(width*2, 10);
        iv_redline.setLayoutParams(params);
        rl_back.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);
        iv_redline.startAnimation(translateAnimation2(1));
        x = width;
        initViewPager();

    }

    private void initViewPager() {
        // 初始化controllers
        friendController = new InviteFriendController(this);
        commController = new InviteCommController(this);
        controllers.add(friendController);
        controllers.add(commController);
        friendController.initData();
        vp.setAdapter(new MyViewPagerAdapter(controllers));
        vp.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    iv_redline.startAnimation(translateAnimation(1));
                    x = width;
                    resetBtnColor();
                    btn01.setTextColor(getResources().getColor(
                            R.color.red_fd3c49));
                    friendController.initData();
                } else {
                    iv_redline.startAnimation(translateAnimation(5));
                    x = 5 * width;
                    resetBtnColor();
                    btn02.setTextColor(getResources().getColor(
                            R.color.red_fd3c49));
                    commController.initData();
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

    /**
     * 按钮字体颜色还原初始化
     */
    private void resetBtnColor() {
        btn01.setTextColor(Color.BLACK);
        btn02.setTextColor(Color.BLACK);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                this.finish();
                break;

            case R.id.btn01:
                vp.setCurrentItem(0);
                break;

            case R.id.btn02:
                vp.setCurrentItem(1);
                break;
        }
    }


}
