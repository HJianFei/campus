package com.loosoo100.campus100.view.picker;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.view.picker.NumberPicker.OnValueChangeListener;

public class DatePicker extends FrameLayout {

    private Context mContext;
    private NumberPicker mDayPicker;
    private NumberPicker mMonthPicker;
    private NumberPicker mYearPicker;
    private Calendar mCalendar;

    private String[] mMonthDisplay;
    private String[] mDayDisplay;

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mCalendar = Calendar.getInstance();
        initMonthDisplay();
        ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.date_picker, this, true);
        mDayPicker = (NumberPicker) findViewById(R.id.date_day);
        mMonthPicker = (NumberPicker) findViewById(R.id.date_month);
        mYearPicker = (NumberPicker) findViewById(R.id.date_year);

        mDayPicker.setMinValue(1);
        mDayPicker.setMaxValue(31);
        mDayPicker.setValue(20);
        mDayPicker.setDisplayedValues(mDayDisplay);
        mDayPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);

        mMonthPicker.setMinValue(0);
        mMonthPicker.setMaxValue(11);
        mMonthPicker.setDisplayedValues(mMonthDisplay);
        mMonthPicker.setValue(mCalendar.get(Calendar.MONTH));

        mYearPicker.setMinValue(1950);
        mYearPicker.setMaxValue(2100);
        mYearPicker.setValue(mCalendar.get(Calendar.YEAR));

        mMonthPicker.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                mCalendar.set(Calendar.MONTH, newVal);
                updateDate();
            }
        });
        mDayPicker.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {

                mCalendar.set(Calendar.DATE, newVal);
                updateDate();
            }
        });
        mYearPicker.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal,
                                      int newVal) {
                mCalendar.set(Calendar.YEAR, newVal);
                updateDate();

            }
        });

        updateDate();

    }

    private void initMonthDisplay() {
        mMonthDisplay = new String[12];
        mMonthDisplay[0] = "01月";
        mMonthDisplay[1] = "02月";
        mMonthDisplay[2] = "03月";
        mMonthDisplay[3] = "04月";
        mMonthDisplay[4] = "05月";
        mMonthDisplay[5] = "06月";
        mMonthDisplay[6] = "07月";
        mMonthDisplay[7] = "08月";
        mMonthDisplay[8] = "09月";
        mMonthDisplay[9] = "10月";
        mMonthDisplay[10] = "11月";
        mMonthDisplay[11] = "12月";

        mDayDisplay = new String[31];
        mDayDisplay[0] = "01日";
        mDayDisplay[1] = "02日";
        mDayDisplay[2] = "03日";
        mDayDisplay[3] = "04日";
        mDayDisplay[4] = "05日";
        mDayDisplay[5] = "06日";
        mDayDisplay[6] = "07日";
        mDayDisplay[7] = "08日";
        mDayDisplay[8] = "09日";
        mDayDisplay[9] = "10日";
        mDayDisplay[10] = "11日";
        mDayDisplay[11] = "12日";
        mDayDisplay[12] = "13日";
        mDayDisplay[13] = "14日";
        mDayDisplay[14] = "15日";
        mDayDisplay[15] = "16日";
        mDayDisplay[16] = "17日";
        mDayDisplay[17] = "18日";
        mDayDisplay[18] = "19日";
        mDayDisplay[19] = "20日";
        mDayDisplay[20] = "21日";
        mDayDisplay[21] = "22日";
        mDayDisplay[22] = "23日";
        mDayDisplay[23] = "24日";
        mDayDisplay[24] = "25日";
        mDayDisplay[25] = "26日";
        mDayDisplay[26] = "27日";
        mDayDisplay[27] = "28日";
        mDayDisplay[28] = "29日";
        mDayDisplay[29] = "30日";
        mDayDisplay[30] = "31日";
    }

    private void updateDate() {
//		System.out.println("Month: " + mCalendar.get(Calendar.MONTH) + " Max: "
//				+ mCalendar.getActualMaximum(Calendar.DATE));
        mDayPicker.setMinValue(mCalendar.getActualMinimum(Calendar.DATE));
        mDayPicker.setMaxValue(mCalendar.getActualMaximum(Calendar.DATE));
        mDayPicker.setValue(mCalendar.get(Calendar.DATE));
        mMonthPicker.setValue(mCalendar.get(Calendar.MONTH));
        mYearPicker.setValue(mCalendar.get(Calendar.YEAR));
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public String getDate() {
        String date = mYearPicker.getValue() + "-"
                + (mMonthPicker.getValue() + 1) + "-" + mDayPicker.getValue();
        return date;

    }

    public int getDay() {
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return mCalendar.get(Calendar.MONTH);
    }

    public int getYear() {
        return mCalendar.get(Calendar.YEAR);
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
        updateDate();
    }

}
