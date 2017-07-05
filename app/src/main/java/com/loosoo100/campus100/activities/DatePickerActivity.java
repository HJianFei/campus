package com.loosoo100.campus100.activities;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.view.picker.DatePicker;
import com.loosoo100.campus100.view.picker.TimePicker;

/**
 * 暂时无用
 * 
 * @author yang 日期选择activity
 */
public class DatePickerActivity extends Activity {
	DatePicker datePicker;
	TimePicker timePicker;
	TextView timeView;
	Button submitView;

	Calendar mCalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timeView = (TextView) findViewById(R.id.time_view);
		submitView = (Button) findViewById(R.id.get_time_btn);
		mCalendar=Calendar.getInstance();

		submitView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mCalendar.set(Calendar.YEAR, datePicker.getYear());
				mCalendar.set(Calendar.MONTH, datePicker.getMonth());
				mCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
				mCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHourOfDay());
				mCalendar.set(Calendar.MINUTE, timePicker.getMinute());
				timeView.setText(mCalendar.getTime().toLocaleString());
			}
		});

	}
}
