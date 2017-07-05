package com.loosoo100.campus100.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.activities.CourseActivity;
import com.loosoo100.campus100.chat.utils.ToastUtil;
import com.loosoo100.campus100.utils.MyUtils;

import java.util.List;

/**
 * @author yang 课程表适配器
 */
public class CourseAdapter extends BaseAdapter {

    private List<List<String>> list;
    private List<String> colorList;
    private Context context;
    private LayoutInflater inflater;
    private CourseActivity activity;

    public static boolean clickable = false;
    public static boolean isShowAdd = false;

    // 课程背景颜色
    private int colors[] = {Color.rgb(0xf0, 0x96, 0x09),
            Color.rgb(0x8c, 0xbf, 0x26), Color.rgb(0x00, 0xab, 0xa9),
            Color.rgb(0x99, 0x6c, 0x33), Color.rgb(0x3b, 0x92, 0xbc),
            Color.rgb(0xd5, 0x4d, 0x34), Color.rgb(0xc6, 0xac, 0xcc),
            Color.rgb(0xac, 0x5c, 0xc3), Color.rgb(0xff, 0xc1, 0xc1),
            Color.rgb(0xff, 0x83, 0xfa), Color.rgb(0xff, 0x6e, 0xb4),
            Color.rgb(0xa3, 0xa3, 0xa3), Color.rgb(0xa4, 0xd3, 0xee),
            Color.rgb(0x8b, 0x8b, 0x00), Color.rgb(0x7a, 0x67, 0xee),
            Color.rgb(0x71, 0xc6, 0x71), Color.rgb(0x70, 0x80, 0x90),
            Color.rgb(0x66, 0x8b, 0x8b), Color.rgb(0x2c, 0x84, 0x44),
            Color.rgb(0x7d, 0x4c, 0x97)};

    public CourseAdapter(Context context, List<List<String>> list,
                         List<String> colorList) {
        this.context = context;
        this.list = list;
        this.colorList = colorList;

        activity = (CourseActivity) context;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_mycourse, null);
            viewHolder.index = (TextView) convertView.findViewById(R.id.index);
            viewHolder.course01 = (TextView) convertView
                    .findViewById(R.id.course01);
            viewHolder.course02 = (TextView) convertView
                    .findViewById(R.id.course02);
            viewHolder.course03 = (TextView) convertView
                    .findViewById(R.id.course03);
            viewHolder.course04 = (TextView) convertView
                    .findViewById(R.id.course04);
            viewHolder.course05 = (TextView) convertView
                    .findViewById(R.id.course05);
            viewHolder.course06 = (TextView) convertView
                    .findViewById(R.id.course06);
            viewHolder.course07 = (TextView) convertView
                    .findViewById(R.id.course07);
            viewHolder.tv_add01 = (TextView) convertView
                    .findViewById(R.id.tv_add01);
            viewHolder.tv_add02 = (TextView) convertView
                    .findViewById(R.id.tv_add02);
            viewHolder.tv_add03 = (TextView) convertView
                    .findViewById(R.id.tv_add03);
            viewHolder.tv_add04 = (TextView) convertView
                    .findViewById(R.id.tv_add04);
            viewHolder.tv_add05 = (TextView) convertView
                    .findViewById(R.id.tv_add05);
            viewHolder.tv_add06 = (TextView) convertView
                    .findViewById(R.id.tv_add06);
            viewHolder.tv_add07 = (TextView) convertView
                    .findViewById(R.id.tv_add07);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置序列号
        viewHolder.index.setText("" + list.get(position).get(0).toString());
        // 设置课程名称
        viewHolder.course01.setText("" + list.get(position).get(1).toString());
        // 如果课程名称==null，则设置背景色为透明
        if (list.get(position).get(1).toString().equals("null")) {
            viewHolder.course01.setBackgroundColor(Color.TRANSPARENT);
        } else {
            // 如果颜色集合的课程名称没有该课程名称，则为该课程添加颜色并添加到颜色集合中
            if (!colorList.contains(list.get(position).get(1).toString())) {
                colorList.add(list.get(position).get(1).toString());
            }
            viewHolder.course01.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(1).toString()) % 20]);
        }

        viewHolder.course02.setText("" + list.get(position).get(2).toString());
        if (list.get(position).get(2).toString().equals("null")) {
            viewHolder.course02.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (!colorList.contains(list.get(position).get(2).toString())) {
                colorList.add(list.get(position).get(2).toString());
            }
            viewHolder.course02.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(2).toString()) % 20]);
        }

        viewHolder.course03.setText("" + list.get(position).get(3).toString());
        if (list.get(position).get(3).toString().equals("null")) {
            viewHolder.course03.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (!colorList.contains(list.get(position).get(3).toString())) {
                colorList.add(list.get(position).get(3).toString());
            }
            viewHolder.course03.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(3).toString()) % 20]);
        }

        viewHolder.course04.setText("" + list.get(position).get(4).toString());
        if (list.get(position).get(4).toString().equals("null")) {
            viewHolder.course04.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (!colorList.contains(list.get(position).get(4).toString())) {
                colorList.add(list.get(position).get(4).toString());
            }
            viewHolder.course04.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(4).toString()) % 20]);
        }

        viewHolder.course05.setText("" + list.get(position).get(5).toString());
        if (list.get(position).get(5).toString().equals("null")) {
            viewHolder.course05.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (!colorList.contains(list.get(position).get(5).toString())) {
                colorList.add(list.get(position).get(5).toString());
            }
            viewHolder.course05.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(5).toString()) % 20]);
        }

        viewHolder.course06.setText("" + list.get(position).get(6).toString());
        if (list.get(position).get(6).toString().equals("null")) {
            viewHolder.course06.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (!colorList.contains(list.get(position).get(6).toString())) {
                colorList.add(list.get(position).get(6).toString());
            }
            viewHolder.course06.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(6).toString()) % 20]);
        }

        viewHolder.course07.setText("" + list.get(position).get(7).toString());
        if (list.get(position).get(7).toString().equals("null")) {
            viewHolder.course07.setBackgroundColor(Color.TRANSPARENT);
        } else {
            if (!colorList.contains(list.get(position).get(7).toString())) {
                colorList.add(list.get(position).get(7).toString());
            }
            viewHolder.course07.setBackgroundColor(colors[colorList
                    .indexOf(list.get(position).get(7).toString()) % 20]);
        }

        viewHolder.course01.setTag(position);
        viewHolder.course02.setTag(position);
        viewHolder.course03.setTag(position);
        viewHolder.course04.setTag(position);
        viewHolder.course05.setTag(position);
        viewHolder.course06.setTag(position);
        viewHolder.course07.setTag(position);

        OnClickClassListener onClickClassListener = new OnClickClassListener();
        viewHolder.course01.setOnClickListener(onClickClassListener);
        viewHolder.course02.setOnClickListener(onClickClassListener);
        viewHolder.course03.setOnClickListener(onClickClassListener);
        viewHolder.course04.setOnClickListener(onClickClassListener);
        viewHolder.course05.setOnClickListener(onClickClassListener);
        viewHolder.course06.setOnClickListener(onClickClassListener);
        viewHolder.course07.setOnClickListener(onClickClassListener);

        viewHolder.course01.setClickable(clickable);
        viewHolder.course02.setClickable(clickable);
        viewHolder.course03.setClickable(clickable);
        viewHolder.course04.setClickable(clickable);
        viewHolder.course05.setClickable(clickable);
        viewHolder.course06.setClickable(clickable);
        viewHolder.course07.setClickable(clickable);

        if (viewHolder.course01.getText().toString().equals("null")) {
            viewHolder.course01.setAlpha(0);
        } else {
            viewHolder.course01.setAlpha(1);
        }
        if (viewHolder.course02.getText().toString().equals("null")) {
            viewHolder.course02.setAlpha(0);
        } else {
            viewHolder.course02.setAlpha(1);
        }
        if (viewHolder.course03.getText().toString().equals("null")) {
            viewHolder.course03.setAlpha(0);
        } else {
            viewHolder.course03.setAlpha(1);
        }
        if (viewHolder.course04.getText().toString().equals("null")) {
            viewHolder.course04.setAlpha(0);
        } else {
            viewHolder.course04.setAlpha(1);
        }
        if (viewHolder.course05.getText().toString().equals("null")) {
            viewHolder.course05.setAlpha(0);
        } else {
            viewHolder.course05.setAlpha(1);
        }
        if (viewHolder.course06.getText().toString().equals("null")) {
            viewHolder.course06.setAlpha(0);
        } else {
            viewHolder.course06.setAlpha(1);
        }
        if (viewHolder.course07.getText().toString().equals("null")) {
            viewHolder.course07.setAlpha(0);
        } else {
            viewHolder.course07.setAlpha(1);
        }

        if (isShowAdd) {
            if (viewHolder.course01.getText().toString().equals("null")) {
                viewHolder.tv_add01.setVisibility(View.VISIBLE);
            }
            if (viewHolder.course02.getText().toString().equals("null")) {
                viewHolder.tv_add02.setVisibility(View.VISIBLE);
            }
            if (viewHolder.course03.getText().toString().equals("null")) {
                viewHolder.tv_add03.setVisibility(View.VISIBLE);
            }
            if (viewHolder.course04.getText().toString().equals("null")) {
                viewHolder.tv_add04.setVisibility(View.VISIBLE);
            }
            if (viewHolder.course05.getText().toString().equals("null")) {
                viewHolder.tv_add05.setVisibility(View.VISIBLE);
            }
            if (viewHolder.course06.getText().toString().equals("null")) {
                viewHolder.tv_add06.setVisibility(View.VISIBLE);
            }
            if (viewHolder.course07.getText().toString().equals("null")) {
                viewHolder.tv_add07.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.tv_add01.setVisibility(View.GONE);
            viewHolder.tv_add02.setVisibility(View.GONE);
            viewHolder.tv_add03.setVisibility(View.GONE);
            viewHolder.tv_add04.setVisibility(View.GONE);
            viewHolder.tv_add05.setVisibility(View.GONE);
            viewHolder.tv_add06.setVisibility(View.GONE);
            viewHolder.tv_add07.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        public TextView index, course01, course02, course03, course04,
                course05, course06, course07;
        public TextView tv_add01, tv_add02, tv_add03, tv_add04, tv_add05,
                tv_add06, tv_add07;
    }

    // 点击课程的监听器
    class OnClickClassListener implements OnClickListener {
        private String course;
        private TextView textView;
        private EditText editText;
        private Dialog dialog;

        public void onClick(View v) {

            textView = (TextView) v;

            dialog = new Dialog(activity, R.style.MyDialog);
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.dialog_course_add, null);
            dialog.setContentView(view);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            dialog.setContentView(view, params);
            editText = (EditText) view.findViewById(R.id.et_courseName);

            if (textView.getText().toString().equals("null")) {
                editText.setText("");
            } else {
                editText.setText(textView.getText().toString());
            }

            view.findViewById(R.id.btn_add).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            course = editText.getText().toString();
                            if (course.trim().equals("") && textView.getText().toString().equals("null")) {
                                ToastUtil.showToast(activity, "输入课程不能为空");
                                return;
                            }
                            if (course.trim().equals("")) {
                                course = "null";
                            }
                            switch (textView.getId()) {
                                case R.id.course01:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(1, course);
                                    break;
                                case R.id.course02:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(2, course);
                                    break;
                                case R.id.course03:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(3, course);
                                    break;
                                case R.id.course04:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(4, course);
                                    break;
                                case R.id.course05:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(5, course);
                                    break;
                                case R.id.course06:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(6, course);
                                    break;
                                case R.id.course07:
                                    list.get(
                                            Integer.valueOf(textView.getTag()
                                                    .toString())).set(7, course);
                                    break;

                            }

                            CourseAdapter.this.notifyDataSetChanged();
                            ToastUtil.showToast(activity, "修改成功");
                            dialog.dismiss();
                        }
                    });
            dialog.show();
            //显示软键盘
            MyUtils.showSoftInput(context, editText);
        }
    }

}
