package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang 全部晒单activity
 */
public class OrderShowActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	@ViewInject(R.id.rl_back)
	private View rl_back;
	@ViewInject(R.id.root)
	private LinearLayout root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_show);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);

		rl_back.setOnClickListener(this);

//		getWindow().getDecorView().setDrawingCacheEnabled(true);
//		try {
//			File myCaptureFile = new File("/mnt/sdcard/"
//					+ System.currentTimeMillis() + ".jpg");
//			BufferedOutputStream bos = new BufferedOutputStream(
//					new FileOutputStream(myCaptureFile));
//			getWindow().getDecorView().getDrawingCache()
//					.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//			bos.flush();
//			bos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		getWindow().getDecorView().setDrawingCacheEnabled(false);
//		
//		
//		root.setDrawingCacheEnabled(true);
//        try {
//            File myCaptureFile = new File("/mnt/sdcard/"
//                    + System.currentTimeMillis() + ".jpg");
//            BufferedOutputStream bos = new BufferedOutputStream(
//                    new FileOutputStream(myCaptureFile));
//            root.getDrawingCache()
//                    .compress(Bitmap.CompressFormat.JPEG, 80, bos);
//            bos.flush();
//            bos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        root.setDrawingCacheEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		}
	}

}
