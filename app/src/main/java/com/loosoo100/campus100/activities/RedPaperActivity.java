package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.utils.MyUtils;

/**
 * 
 * @author yang
 *	红包activity
 */
public class RedPaperActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // 空view
	
	@ViewInject(R.id.rl_back)
	private View rl_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_red_paper);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		
		rl_back.setOnClickListener(this);
		
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
