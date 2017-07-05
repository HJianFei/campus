package com.loosoo100.campus100.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.interfaces.ILetterIndexer;

public class LetterIndexView extends View {
	/**
	 * 索引字符串数组
	 */
	private String[] arrays = new String[] { "", "#", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };

	private int width;// 字母索引控件宽度
	private int height;// 字母索引控件高度
	private boolean isDown = false;// 标识是否按下，用作改变控件背景色
	private int oldSelect = -1;// 上次选中项索引

	private int textsize;// 画笔字体大小

	private ListView listView;// 绑定ListView
	private TextView overlay;// 当前索引显示框

	public LetterIndexView(Context context) {
		super(context);
		initIndexView(context, null, 0);
	}

	public LetterIndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initIndexView(context, attrs, 0);
	}

	public LetterIndexView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initIndexView(context, attrs, defStyle);
	}

	/**
	 * 该方法的作用：初始化索引控件
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	private void initIndexView(Context context, AttributeSet attrs, int defStyle) {
		initOverlay(context);

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.LetterIndexPaint, defStyle, 0);
		if (typedArray != null) {
			textsize = typedArray.getInt(R.styleable.LetterIndexPaint_textsize,
					12);
			typedArray.recycle();
		}
	}

	/**
	 * 该方法的作用：初始化当前索引显示框
	 * 
	 * @param context
	 */
	private void initOverlay(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		overlay = (TextView) inflater.inflate(R.layout.overlay_layout, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	/**
	 * 该方法的作用：设置ListView绑定
	 * 
	 * @param listView
	 */
	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float y = event.getY();
		int c = (int) (y / getHeight() * arrays.length);
		if (c < 0 || c > arrays.length) {
			return false;
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			isDown = true;
			setSelection(c);
			break;
		case MotionEvent.ACTION_UP:
			isDown = false;
			overlay.setVisibility(View.GONE);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			setSelection(c);
			break;

		}
		return true;
	}

	/**
	 * 该方法的作用：控件核心代码 ，通过索引设置选中项
	 * 
	 * @param index
	 */
	private void setSelection(int index) {
		if (index < 0 || index >=arrays.length) {
			return;
		}
		oldSelect = index;
		overlay.setText(arrays[index]);
		if (overlay.getText().toString().equals("")) {
			overlay.setVisibility(View.GONE);
		} else {
			overlay.setVisibility(View.VISIBLE);
			if (listView != null && listView.getAdapter() != null) {
				if (listView.getAdapter() instanceof ILetterIndexer) {
					ILetterIndexer letterIndexer = (ILetterIndexer) listView
							.getAdapter();
					// 获取选中索引在listView中位置（position）
					int position = letterIndexer
							.getPositionForSection(arrays[index]);
					listView.setSelection(position);
				}
			}

		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isDown && overlay.getVisibility() == View.VISIBLE) {
			canvas.drawColor(Color.parseColor("#30000000"));
		}
		width = getWidth();
		height = getHeight();
		Paint paint = new Paint();
		paint.setTextSize(textsize);
		paint.setAntiAlias(true);
		for (int i = 0; i < arrays.length; i++) {
			if (i == oldSelect) {
				// paint.setColor(Color.BLUE);
			} else {
				paint.setColor(Color.GRAY);
			}
			float xPos = width / 2 - paint.measureText(arrays[i]) / 2;
			float yPos = height / arrays.length * (i + 1);
			canvas.drawText(arrays[i], xPos, yPos, paint);
		}
	}
}
