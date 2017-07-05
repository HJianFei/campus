package com.loosoo100.campus100.chat.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.chat.ui.SchoolMateActivity;
import com.loosoo100.campus100.chat.ui.SearchFriendActivity;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.zxing.activity.CaptureActivity;

/**
 * Created by HJianFei on 2016/10/27.
 */

public class AddFriendPopWindow extends PopupWindow {
    private View conentView;

    public AddFriendPopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.add_friend_popupwindow, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 - 100);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        LinearLayout btn_add_friend = (LinearLayout) conentView
                .findViewById(R.id.btn_add_friend);
        LinearLayout btn_same_school = (LinearLayout) conentView
                .findViewById(R.id.btn_same_school);
        LinearLayout btn_scan_friend = (LinearLayout) conentView
                .findViewById(R.id.btn_scan_friend);
        btn_add_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AddFriendPopWindow.this.dismiss();
                Intent intent = new Intent(context, SearchFriendActivity.class);
                context.startActivity(intent);
            }
        });

        btn_same_school.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddFriendPopWindow.this.dismiss();
                Intent intent = new Intent(context, SchoolMateActivity.class);
                context.startActivity(intent);
            }
        });
        btn_scan_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddFriendPopWindow.this.dismiss();
                Intent intent = new Intent(context, CaptureActivity.class);
                context.startActivityForResult(intent, MyConfig.QRCODE);
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }
}
