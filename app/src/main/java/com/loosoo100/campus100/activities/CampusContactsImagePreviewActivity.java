package com.loosoo100.campus100.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.ViewPagerAdapter;
import com.loosoo100.campus100.chat.utils.LogUtils;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.view.ZoomImageView;

/**
 * @author yang 校园圈图片预览activity
 */
public class CampusContactsImagePreviewActivity extends Activity implements
        OnClickListener {
    @ViewInject(R.id.rl_root)
    private RelativeLayout rl_root;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.tv_index)
    private TextView tv_index;
    @ViewInject(R.id.btn_save)
    private Button btn_save;

    private int index = 0; // 先显示哪一张照片

    private List<View> views = new ArrayList<View>();

    private int size;
    private ArrayList<String> infos = null;

    private List<String> list = new ArrayList<String>();

    private boolean showButton = false; // 是否显示保存按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_image_preview);

        overridePendingTransition(R.anim.in_scale_up, R.anim.out_scale_down);
        ViewUtils.inject(this);

        index = getIntent().getExtras().getInt("index");
        showButton = getIntent().getExtras().getBoolean("button");
        infos = getIntent().getStringArrayListExtra("urlList");

        btn_save.setOnClickListener(this);
        if (showButton) {
            btn_save.setVisibility(View.VISIBLE);
        } else {
            btn_save.setVisibility(View.GONE);
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        rl_root.startAnimation(alphaAnimation);

        if (infos != null) {
            initViewPager();
        }
    }

    private void initViewPager() {
        size = infos.size();
        for (int i = 0; i < size; i++) {
            ZoomImageView view = new ZoomImageView(this);
            view.setFocusableInTouchMode(true);
            view.setImageResource(R.drawable.imgloading);
            views.add(view);
        }
        vp.setAdapter(new ViewPagerAdapter(views));
        // 设置当前显示页数
        vp.setCurrentItem(index);
        tv_index.setText((index + 1) + "/" + size);
        vp.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                index = arg0;
                tv_index.setText((arg0 + 1) + "/" + size);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        for (int j = 0; j < size; j++) {
            Glide.with(this).load(infos.get(j)).dontAnimate()
                    .into((ZoomImageView) views.get(j));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (list.contains(index + "")) {
                    ToastUtil.showToast(this, "图片已下载或正在下载中");
                    return;
                }
                new Thread() {
                    public void run() {
                        downloadXUtils(infos.get(index), index);
                    }

                    ;
                }.start();
                break;
        }
    }

    private void downloadXUtils(String downloadHost, final int index) {
        list.add(index + "");
        String url = MyConfig.FILE_URI;
        File dir = new File(url);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileurl = "";
        if (downloadHost.contains(".gif")) {
            fileurl = url + "/" + System.currentTimeMillis() + ".gif";
        } else if (downloadHost.contains(".png")) {
            fileurl = url + "/" + System.currentTimeMillis() + ".png";
        } else if (downloadHost.contains(".jpeg")) {
            fileurl = url + "/" + System.currentTimeMillis() + ".jpeg";
        } else {
            fileurl = url + "/" + System.currentTimeMillis() + ".jpg";
        }
        File file = new File(fileurl);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HttpUtils http = new HttpUtils();
        HttpHandler handler = http.download(downloadHost, fileurl, true, false,
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        ToastUtil.showToast(CampusContactsImagePreviewActivity.this, "文件已保存至" + MyConfig.FILE_URI + "文件夹");
                        Intent intent = new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(new File(MyConfig.FILE_URI));
                        intent.setData(uri);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.d("error",msg);
                        ToastUtil.showToast(CampusContactsImagePreviewActivity.this, "保存失败");
                        list.remove(index + "");
                    }
                });
    }
}
