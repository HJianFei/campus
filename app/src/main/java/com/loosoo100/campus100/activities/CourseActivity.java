package com.loosoo100.campus100.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loosoo100.campus100.R;
import com.loosoo100.campus100.adapters.CourseAdapter;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.config.MyConfig;
import com.loosoo100.campus100.db.UserInfoDB;
import com.loosoo100.campus100.utils.GetData;
import com.loosoo100.campus100.utils.MyUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 
 * @author yang 我的课程表activity
 */
public class CourseActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.iv_empty)
	private ImageView iv_empty; // view
	@ViewInject(R.id.image)
	private ImageView image;

	@ViewInject(R.id.rl_back)
	private View rl_back;

	@ViewInject(R.id.tv_week01)
	private TextView tv_week01;
	@ViewInject(R.id.tv_week02)
	private TextView tv_week02;
	@ViewInject(R.id.tv_week03)
	private TextView tv_week03;
	@ViewInject(R.id.tv_week04)
	private TextView tv_week04;
	@ViewInject(R.id.tv_week05)
	private TextView tv_week05;
	@ViewInject(R.id.tv_week06)
	private TextView tv_week06;
	@ViewInject(R.id.tv_week07)
	private TextView tv_week07;

	@ViewInject(R.id.tv_day01)
	private TextView tv_day01;
	@ViewInject(R.id.tv_day02)
	private TextView tv_day02;
	@ViewInject(R.id.tv_day03)
	private TextView tv_day03;
	@ViewInject(R.id.tv_day04)
	private TextView tv_day04;
	@ViewInject(R.id.tv_day05)
	private TextView tv_day05;
	@ViewInject(R.id.tv_day06)
	private TextView tv_day06;
	@ViewInject(R.id.tv_day07)
	private TextView tv_day07;

	@ViewInject(R.id.ll_week01)
	private LinearLayout ll_week01;
	@ViewInject(R.id.ll_week02)
	private LinearLayout ll_week02;
	@ViewInject(R.id.ll_week03)
	private LinearLayout ll_week03;
	@ViewInject(R.id.ll_week04)
	private LinearLayout ll_week04;
	@ViewInject(R.id.ll_week05)
	private LinearLayout ll_week05;
	@ViewInject(R.id.ll_week06)
	private LinearLayout ll_week06;
	@ViewInject(R.id.ll_week07)
	private LinearLayout ll_week07;

	@ViewInject(R.id.tv_month)
	private TextView tv_month;

	@ViewInject(R.id.btn_topbar)
	private Button btn_topbar;

	@ViewInject(R.id.btn_edit)
	private Button btn_edit;

	@ViewInject(R.id.lv_course)
	private ListView lv_course; // 课程表列表

	private Calendar calendar;

	private TextView[] days;

	private List<List<String>> courseSingle = new ArrayList<List<String>>();
	private List<List<String>> courseDouble = new ArrayList<List<String>>();
	private List<String> coursess;
	private List<String> coursesd;

	private List<String> colorsList = new ArrayList<String>();

	private CourseAdapter courseAdapter;

	private int currentWeek = 1;

	private int month;

	private String coursePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycourse);
		ViewUtils.inject(this);

		MyUtils.setStatusBarHeight(this, iv_empty);
		
		image.setImageBitmap(GetData.getBitMap(this, R.drawable.mycourse_bg));

		days = new TextView[] { tv_day01, tv_day02, tv_day03, tv_day04,
				tv_day05, tv_day06, tv_day07 };
		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		// 设置每周的第一天为周一
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		month = calendar.get(Calendar.MONTH) + 1;
		initView();

		CourseAdapter.clickable = false;
		CourseAdapter.isShowAdd = false;

	}

	/**
	 * 初始化双周课程表数据
	 */
	private void initDoubleCourse() {
		coursePath = MyConfig.COURSE_DOUBLE_URI;
		File file = new File(coursePath);
		// 如果课程文件存在则写入已存在的课程
		if (file.exists() && file.length() > 0) {
			courseDouble.clear();
			String courseSDcard = getCourseSDcard(coursePath);
			String courseString = courseSDcard.substring(0,
					courseSDcard.length() - 1);
			String[] splits = courseString.split(",");
			for (int i = 0; i < 16; i++) {
				coursesd = new ArrayList<String>();
				for (int j = 0; j < 8; j++) {
					coursesd.add(splits[i * 8 + j]);
				}
				courseDouble.add(coursesd);
			}
		}
		// 如果课程文件不存在，则初始化所有课程
		else {
			// 每天的课程设置
			for (int i = 1; i < 17; i++) {
				coursesd = new ArrayList<String>();
				coursesd.add(i + "");
				for (int j = 0; j < 7; j++) {
					coursesd.add("null");
				}
				courseDouble.add(coursesd);
			}
		}
		courseAdapter = new CourseAdapter(this, courseDouble, colorsList);
		lv_course.setAdapter(courseAdapter);
	}

	/**
	 * 初始化单周课程表数据
	 */
	private void initSingleCourse() {
		String coursePath = MyConfig.COURSE_SINGLE_URI;
		File file = new File(coursePath);
		// 如果课程文件存在则写入已存在的课程
		if (file.exists() && file.length() > 0) {
			courseSingle.clear();
			String courseSDcard = getCourseSDcard(coursePath);
			String courseString = courseSDcard.substring(0,
					courseSDcard.length() - 1);
			String[] splits = courseString.split(",");
			for (int i = 0; i < 16; i++) {
				coursess = new ArrayList<String>();
				for (int j = 0; j < 8; j++) {
					coursess.add(splits[i * 8 + j]);
				}
				courseSingle.add(coursess);
			}
		}
		// 如果课程文件不存在，则初始化所有课程
		else {
			// 每天的课程设置
			for (int i = 1; i < 17; i++) {
				coursess = new ArrayList<String>();
				coursess.add(i + "");
				for (int j = 0; j < 7; j++) {
					coursess.add("null");
				}
				courseSingle.add(coursess);
			}
		}
		courseAdapter = new CourseAdapter(this, courseSingle, colorsList);
		lv_course.setAdapter(courseAdapter);
	}

	private void initView() {
		rl_back.setOnClickListener(this);
		btn_topbar.setOnClickListener(this);
		btn_edit.setOnClickListener(this);

		// 设置月份
		tv_month.setText(month + "月");
		// 获取当前是星期几
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		int week = getSharedPreferences(UserInfoDB.USERTABLE, MODE_PRIVATE)
				.getInt(UserInfoDB.COURSE, 1);
		// 判断当前时间单双周
		if ((currentWeek - week) % 2 == 0) {
			initSingleCourse();
			btn_topbar.setText("单周");
		} else {
			initDoubleCourse();
			btn_topbar.setText("双周");
		}

		switch (i) {
		case 2:
			setCourseDate(1);
			break;
		case 3:
			setCourseDate(2);
			break;
		case 4:
			setCourseDate(3);
			break;
		case 5:
			setCourseDate(4);
			break;
		case 6:
			setCourseDate(5);
			break;
		case 7:
			setCourseDate(6);
			break;
		case 1:
			setCourseDate(7);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			this.finish();
			break;

		case R.id.btn_topbar:
			if (courseDouble.size() > 0) {
				saveDataToSDcard(MyConfig.COURSE_DOUBLE_URI, courseDouble);
			}
			if (courseSingle.size() > 0) {
				saveDataToSDcard(MyConfig.COURSE_SINGLE_URI, courseSingle);
			}
			if (btn_topbar.getText().toString().equals("单周")) {
				btn_topbar.setText("双周");
				initDoubleCourse();
			} else {
				btn_topbar.setText("单周");
				initSingleCourse();
			}
			break;

		case R.id.btn_edit:
			if (btn_edit.getText().toString().equals("编辑")) {
				CourseAdapter.clickable = true;
				CourseAdapter.isShowAdd = true;
				courseAdapter.notifyDataSetChanged();
				btn_edit.setText("完成");
			} else {
				CourseAdapter.clickable = false;
				CourseAdapter.isShowAdd = false;
				courseAdapter.notifyDataSetChanged();
				btn_edit.setText("编辑");
				if (courseDouble.size() > 0) {
					saveDataToSDcard(MyConfig.COURSE_DOUBLE_URI, courseDouble);
				}
				if (courseSingle.size() > 0) {
					saveDataToSDcard(MyConfig.COURSE_SINGLE_URI, courseSingle);
				}
			}
			break;

		}
	}

	/**
	 * 设置日期
	 * 
	 * @param dayOfWeek
	 *            当天是星期几
	 */
	private void setCourseDate(int dayOfWeek) {
		String timeString = null;
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");

		switch (dayOfWeek) {
		case 1:
			resetDateColor();
			ll_week01.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -1);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;
		case 2:
			resetDateColor();
			ll_week02.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -2);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;
		case 3:
			resetDateColor();
			ll_week03.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -3);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;
		case 4:
			resetDateColor();
			ll_week04.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -4);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;
		case 5:
			resetDateColor();
			ll_week05.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -5);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;
		case 6:
			resetDateColor();
			ll_week06.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -6);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;
		case 7:
			resetDateColor();
			ll_week07.setBackgroundColor(getResources().getColor(
					R.color.blue_3ebde6));
			calendar.add(Calendar.DATE, -7);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				timeString = format.format(calendar.getTime());
				days[i].setText(timeString);
			}
			break;

		}
	}

	@Override
	protected void onPause() {
		if (courseDouble.size() > 0) {
			saveDataToSDcard(MyConfig.COURSE_DOUBLE_URI, courseDouble);
		}
		if (courseSingle.size() > 0) {
			saveDataToSDcard(MyConfig.COURSE_SINGLE_URI, courseSingle);
		}

		// 保存当前周是单或双周
		if (btn_topbar.getText().toString().equals("单周")) {
			SharedPreferences sp = getSharedPreferences(UserInfoDB.USERTABLE,
					MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putInt(UserInfoDB.COURSE, currentWeek);
			edit.commit();
		} else {
			SharedPreferences sp = getSharedPreferences(UserInfoDB.USERTABLE,
					MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putInt(UserInfoDB.COURSE, currentWeek - 1);
			edit.commit();
		}
		super.onPause();
	}

	/**
	 * 还原日期颜色，为了突出今天日期
	 */
	private void resetDateColor() {
		ll_week01.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
		ll_week02.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
		ll_week03.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
		ll_week04.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
		ll_week05.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
		ll_week06.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
		ll_week07.setBackgroundColor(getResources()
				.getColor(R.color.none_color));
	}

	// 保存数据到SD卡文件
	public boolean saveDataToSDcard(String fileName, List<List<String>> list) {
		boolean isAvailable = false; // SD是否可读
		FileOutputStream fileOutputStream = null;
		// 创建File对象
		File file = new File(fileName);
		StringBuffer buffer = new StringBuffer();
		// 将list转成String类型
		List<String> cache = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < 8; j++) {
				// 转化为字符串并添加进新的List中
				buffer.append(list.get(i).get(j).toString());
				buffer.append(",");
			}
		}
		// 可存储的字符串数据
		String listStr = buffer.toString();

		// 判断SD卡是否可读写
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			isAvailable = true;
			try {
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(listStr.getBytes());
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showToast(this,"写入失败");
			}

		}
		return isAvailable;
	}

	public String getCourseSDcard(String fileName) {
		String str = "";
		try {
			File urlFile = new File(fileName);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					urlFile), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String mimeTypeLine = null;
			while ((mimeTypeLine = br.readLine()) != null) {
				str = str + mimeTypeLine;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
