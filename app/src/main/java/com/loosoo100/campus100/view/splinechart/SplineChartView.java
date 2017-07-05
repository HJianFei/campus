package com.loosoo100.campus100.view.splinechart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SplineChartView extends DemoView {

	private String TAG = "SplineChart01View";
	private SplineChart chart = new SplineChart();
	// 分类轴标签集合
	private List<String> labels = new ArrayList<String>();
	private List<SplineData> chartData = new ArrayList<SplineData>();
	Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);

	public SplineChartView(Context context) {
		super(context);
		initView();
	}

	public SplineChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SplineChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public void setData(float maxMoney, int maxX, List<String> labels,
			List<SplineData> chartData) {
		float money = maxMoney;
		int count = 0;
		int num = 5;
		int j = 1;

		if (money <= 100) {
			money = 100;
		} else if (money <= 500) {
			money = 500;
		} else if (money <= 1000) {
			money = 1000;
		} else {
			// 计算位数
			while (maxMoney > 1) {
				maxMoney = maxMoney / 10;
				count++;
			}
			for (int i = 2; i < count; i++) {
				num = num * 10;
			}
			while (num * j < money) {
				j++;
			}
			money = num * j;
		}

		// 设置数据轴最大值
		chart.getDataAxis().setAxisMax(money);
		chart.getDataAxis().setAxisSteps(money / 5);
		chart.setCategories(labels);
		chart.setDataSource(chartData);
		// 标签轴最大值
		chart.setCategoryAxisMax(maxX);
		this.chartData = chartData;
		this.invalidate();
	}

	public void resetData() {
		labels.clear();
		chartData.clear();
		labels.add("");
		labels.add("一");
		labels.add("二");
		labels.add("三");
		labels.add("四");
		labels.add("五");
		labels.add("六");
		labels.add("日");
		chartDataSet();
		// 设置数据轴最大值
		chart.getDataAxis().setAxisMax(1000);
		chart.getDataAxis().setAxisSteps(1000 / 5);
		chart.setCategories(labels);
		chart.setDataSource(chartData);
		// 标签轴最大值
		chart.setCategoryAxisMax(8);
		this.invalidate();
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();

		// 綁定手势滑动事件
		// this.bindTouch(this,chart);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 图所占范围大小
		chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {
			// 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

			// 平移时收缩下
			// float margin = DensityUtil.dip2px(getContext(), 20);
			// chart.setXTickMarksOffsetMargin(margin);

			// 显示边框
			// chart.showRoundBorder();

			// 数据源
			chart.setCategories(labels);
			chart.setDataSource(chartData);

			// 坐标系
			// 数据轴最大值
			chart.getDataAxis().setAxisMax(1000);
			// chart.getDataAxis().setAxisMin(0);
			// 数据轴刻度间隔
			chart.getDataAxis().setAxisSteps(200);

			// 标签轴最大值
			chart.setCategoryAxisMax(8);
			// 标签轴最小值
			chart.setCategoryAxisMin(0);

			// 设置图的背景色
			// chart.setBackgroupColor(true,Color.BLACK);
			// 设置绘图区的背景色
			// chart.getPlotArea().setBackgroupColor(true, Color.WHITE);

			// 背景网格
			PlotGrid plot = chart.getPlotGrid();
			// 显示水平线
			plot.showHorizontalLines();
			// 显示垂直线
			// plot.showVerticalLines();
			// 水平线粗细
			plot.getHorizontalLinePaint().setStrokeWidth(3);
			// 水平线颜色
			plot.getHorizontalLinePaint().setColor(Color.rgb(239, 239, 239));
			// 水平线风格
			// plot.setHorizontalLineStyle(XEnum.LineStyle.DOT);

			// 把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
			chart.getDataAxis()
					.getAxisPaint()
					.setStrokeWidth(
							plot.getHorizontalLinePaint().getStrokeWidth());
			chart.getCategoryAxis()
					.getAxisPaint()
					.setStrokeWidth(
							plot.getHorizontalLinePaint().getStrokeWidth());

			chart.getDataAxis().getAxisPaint()
					.setColor(Color.rgb(208, 208, 208));
			chart.getCategoryAxis().getAxisPaint()
					.setColor(Color.rgb(208, 208, 208));
			// 设置X轴字体大小
			chart.getCategoryAxis().getTickLabelPaint().setTextSize(24);

			chart.getDataAxis().getTickMarksPaint()
					.setColor(Color.rgb(208, 208, 208));
			chart.getCategoryAxis().getTickMarksPaint()
					.setColor(Color.rgb(208, 208, 208));

			// 居中
			chart.getDataAxis().setHorizontalTickAlign(Align.CENTER);
			// 设置Y轴字体对齐方式
			chart.getDataAxis().getTickLabelPaint().setTextAlign(Align.RIGHT);
			// 设置Y轴字体大小
			chart.getDataAxis().getTickLabelPaint().setTextSize(24);

			// 居中显示轴
			// plot.hideHorizontalLines();
			// plot.hideVerticalLines();
			// chart.setDataAxisLocation(XEnum.AxisLocation.VERTICAL_CENTER);
			// chart.setCategoryAxisLocation(XEnum.AxisLocation.HORIZONTAL_CENTER);

			// 定义交叉点标签显示格式,特别备注,因曲线图的特殊性，所以返回格式为: x值,y值
			// 请自行分析定制
			chart.setDotLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					String label = "(" + value + ")";
					return (label);
				}

			});
			// 标题
			// chart.setTitle("社团近7天资金");
			// chart.addSubtitle("(单位:元)");

			// 激活点击监听
			chart.ActiveListenItemClick();
			// 为了让触发更灵敏，可以扩大15px的点击监听范围
			chart.extPointClickRange(25);
			chart.showClikedFocus();

			// 显示十字交叉线
			chart.showDyLine();
			chart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);
			// 扩大实际绘制宽度
			// chart.getPlotArea().extWidth(500.f);

			// 封闭轴
			chart.setAxesClosed(true);

			// 将线显示为直线，而不是平滑的
			chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);

			// 不使用精确计算，忽略Java计算误差,提高性能
			chart.disableHighPrecision();

			// 仅能横向移动
			chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {
		// 数据集
		List<PointD> linePoint = new ArrayList<PointD>();
		// linePoint.add(new PointD(0d, 195d));
		// linePoint.add(new PointD(1d, 40d));
		// linePoint.add(new PointD(2d, 59d));
		// linePoint.add(new PointD(3d, 153d));
		// linePoint.add(new PointD(4d, 95d));
		// linePoint.add(new PointD(5d, 35d));
		// linePoint.add(new PointD(6d, 135d));
		SplineData dataSeries = new SplineData("", linePoint, Color.rgb(253,
				60, 73));
		// 把线弄细点
		// dataSeries.getLinePaint().setStrokeWidth(2);
		// 是否显示标签内容
		// dataSeries.setLabelVisible(true);
		// 设置折线点的半径大小
		dataSeries.setDotRadius(10);
		dataSeries.setDotStyle(XEnum.DotStyle.DOT);
		dataSeries.getDotLabelPaint().setColor(Color.RED);
		// 设置round风格的标签
		// dataSeries.getLabelOptions().showBackground();
		dataSeries.getLabelOptions().getBox().getBackgroundPaint()
				.setColor(Color.GREEN);
		dataSeries.getLabelOptions().getBox().setRoundRadius(8);
		dataSeries.getLabelOptions().setLabelBoxStyle(
				XEnum.LabelBoxStyle.CIRCLE);
		chartData.add(dataSeries);
	}

	private void chartLabels() {
		 labels.add("");
		labels.add("一");
		labels.add("二");
		labels.add("三");
		labels.add("四");
		labels.add("五");
		labels.add("六");
		labels.add("日");
		 labels.add("");
		// labels.add("");
		// labels.add("");
		// labels.add("");
		// labels.add("");
		// labels.add("");
		// labels.add("");
		// labels.add("");
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_UP) {
			triggerClick(event.getX(), event.getY());
		}
		return true;
	}

	// 触发监听
	private void triggerClick(float x, float y) {
		// 交叉线
		// if (chart.getDyLineVisible())
		// chart.getDyLine().setCurrentXY(x, y);
		if (!chart.getListenItemClickStatus()) {
			if (chart.getDyLineVisible() && chart.getDyLine().isInvalidate())
				this.invalidate();
		} else {
			PointPosition record = chart.getPositionRecord(x, y);
			if (null == record)
				return;

			if (record.getDataID() >= chartData.size())
				return;
			SplineData lData = chartData.get(record.getDataID());
			List<PointD> linePoint = lData.getLineDataSet();
			int pos = record.getDataChildID();
			int i = 0;
			Iterator it = linePoint.iterator();
			while (it.hasNext()) {
				PointD entry = (PointD) it.next();

				if (pos == i) {
					// Double xValue = entry.x;
					Double yValue = entry.y;

					// float r = record.getRadius();
					// chart.showFocusPointF(record.getPosition(), r * 2);
					// chart.getFocusPaint().setStyle(Style.STROKE);
					// chart.getFocusPaint().setStrokeWidth(3);
					// if (record.getDataID() >= 2) {
					// chart.getFocusPaint().setColor(Color.BLUE);
					// } else {
					// chart.getFocusPaint().setColor(Color.RED);
					// }

					// 在点击处显示tooltip
					pToolTip.setColor(Color.GRAY);
					pToolTip.setTextSize(30);
					chart.getToolTip().setCurrentXY(x, y);
					// chart.getToolTip().addToolTip(" Key:"+lData.getLineKey(),pToolTip);
					// chart.getToolTip().addToolTip(" Label:"+lData.getLabel(),pToolTip);
					// chart.getToolTip().addToolTip(" Current Value:"
					// +Double.toString(xValue)+","+Double.toString(yValue),pToolTip);
					chart.getToolTip().addToolTip(Double.toString(yValue),
							pToolTip);
					// 设置提示背景透明度
					chart.getToolTip().getBackgroundPaint().setAlpha(0);
					// 隐藏提示线框
					chart.getToolTip().hideBorder();
					this.invalidate();
					break;
				}
				i++;
			}// end while
		}
	}
}
