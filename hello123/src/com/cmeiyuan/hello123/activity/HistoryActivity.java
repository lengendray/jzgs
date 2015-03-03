package com.cmeiyuan.hello123.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.cmeiyuan.hello123.Constants;
import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.api.BaseApi.AsyncCallBack;
import com.cmeiyuan.hello123.api.BaseApi.Error;
import com.cmeiyuan.hello123.api.HistoryNetValueApi;
import com.cmeiyuan.hello123.bean.FundHold;
import com.cmeiyuan.hello123.bean.HistoryNetValue;
import com.cmeiyuan.hello123.bean.NetValue;
import com.cmeiyuan.hello123.util.StringUtil;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LabelColorFormatter;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabelFormatter;

public class HistoryActivity extends AnalysisActivity implements
		OnClickListener {

	public static final String FUND = "fund";

	private LineChart mLineChart;
	private TextView btn_month;
	private TextView btn_season;
	private TextView btn_half;
	private TextView btn_year;
	private TextView tv_net_value;
	private TextView tv_grow_value;
	private TextView tv_date_value;
	private TextView tv_range_grow_value;
	private TextView tv_label_range;
	private FundHold fund;
	private HistoryNetValueApi api = new HistoryNetValueApi();
	private final DecimalFormat yLabelFormat = new DecimalFormat("#0.00");
	private final SimpleDateFormat xLabelFormat = new SimpleDateFormat(
			"MM月dd日", Locale.CHINA);

	private HashMap<String, List<NetValue>> dataCache = new HashMap<String, List<NetValue>>();

	private String range = HistoryNetValueApi.MONTH;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_history);
		mLineChart = (LineChart) findViewById(R.id.lineChart);

		btn_month = (TextView) findViewById(R.id.btn_month);
		btn_season = (TextView) findViewById(R.id.btn_season);
		btn_half = (TextView) findViewById(R.id.btn_half);
		btn_year = (TextView) findViewById(R.id.btn_year);

		tv_net_value = (TextView) findViewById(R.id.tv_net_value);
		tv_grow_value = (TextView) findViewById(R.id.tv_grow_value);
		tv_range_grow_value = (TextView) findViewById(R.id.tv_range_grow_value);
		tv_date_value = (TextView) findViewById(R.id.tv_date_value);
		tv_label_range = (TextView) findViewById(R.id.tv_label_range_grow);

		btn_month.setOnClickListener(this);
		btn_season.setOnClickListener(this);
		btn_half.setOnClickListener(this);
		btn_year.setOnClickListener(this);

		btn_month.setSelected(true);

		setLineChart();

		api.setAsyncCallBack(new AsyncCallBack<HistoryNetValue>() {

			@Override
			public void onSuccess(HistoryNetValue t) {
				List<NetValue> list = t.getNetValues();
				dataCache.put(range, list);
				setLineData(list);
				showSelectData(list.get(0));
				showRangeData();
			}

			@Override
			public void onFailed(Error error) {
				String text = error.getMessage();
				if (StringUtil.isEmpty(text)) {
					text = "数据加载失败";
				}
				showToast(text);
			}
		});

		try {
			fund = (FundHold) getIntent().getSerializableExtra(FUND);
		} catch (Exception e) {
			showToast("传入数据有误 ：" + e.toString());
		}

		if (fund != null) {
			setTitle(fund.fundName);
			get(fund.fundCode);
		}

	}

	@Override
	protected void onInitTopBar(TextView left, TextView right, TextView center) {
		left.setText("返回");
		right.setVisibility(View.GONE);
	}

	@Override
	protected void onTopBarSelected(View v) {
		if (v == getLeftTextView()) {
			finish();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	protected void setLineChart() {

		// 无描述
		mLineChart.setDescription("");
		// 无数据时描述
		mLineChart.setNoDataText("");
		mLineChart.setNoDataTextDescription("");
		// y轴不从0开始
		mLineChart.setStartAtZero(false);
		// 绘制横向网络线
		mLineChart.setDrawHorizontalGrid(true);
		// 绘制垂直网络线1
		mLineChart.setDrawVerticalGrid(false);
		// 不绘制网络背景
		mLineChart.setDrawGridBackground(false);
		// 设置网格线颜色
		mLineChart.setGridColor(Color.parseColor("#414141"));
		// 设置网格线宽度
		mLineChart.setGridWidth(1.25f);
		// 可触摸
		mLineChart.setTouchEnabled(true);
		// 不可拖动、缩放
		mLineChart.setDragScaleEnabled(false);
		// 禁用双击缩放
		mLineChart.setDoubleTapToZoomEnabled(false);
		// 绘制图例
		mLineChart.setDrawLegend(false);
		// 设置x轴标签在下方
		mLineChart.getXLabels().setPosition(XLabelPosition.BOTTOM);
		// 不绘制y值
		mLineChart.setDrawYValues(false);
		// 绘制边框
		mLineChart.setDrawBorder(true);
		// 设置边框位置
		BorderPosition[] border = new BorderPosition[] { BorderPosition.BOTTOM };
		mLineChart.setBorderPositions(border);
		// 启用高亮线
		mLineChart.setHighlightEnabled(true);
		mLineChart.setHighlightIndicatorEnabled(true);
		mLineChart.setHighlightLineWidth(0.1f);
		// 设置单位
		mLineChart.setUnit("元");
		// 设置内容边距
		mLineChart.setOffsets(40, 30, 20, 25);
		// 设置网络线风格为破折线
		// Paint paint = mLineChart.getPaint(Chart.PAINT_GRID);
		// paint.setColor(Color.GRAY);
		// paint.setPathEffect(new DashPathEffect(new float[] { 4, 4 }, 0));

		mLineChart.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Highlight hight = mLineChart.getHighlightByTouchPoint(
						event.getX(), event.getY());

				if (hight != null) {
					mLineChart.highlightTouch(new Highlight(hight.getXIndex(),
							0));

					Entry entry = mLineChart.getEntryByDataSetIndex(
							hight.getXIndex(), 0);

					if (entry != null) {
						NetValue value = (NetValue) entry.getData();
						showSelectData(value);
					}

				}

				int action = event.getAction();

				if (action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_CANCEL
						|| action == MotionEvent.ACTION_OUTSIDE) {
				}

				return true;
			}
		});

		mLineChart.getYLabels().setFormatter(new YLabelFormatter() {

			@Override
			public String getFormattedLabel(float arg0) {
				return yLabelFormat.format(arg0);
			}
		});

		mLineChart.getYLabels().setLabelColorFormatter(
				new LabelColorFormatter() {

					@Override
					public int getFormattedColor(float value) {
						int resId = R.color.val_zero;
						return getResources().getColor(resId);
					}
				});

		mLineChart.getYLabels().setLabelCount(6);
	}

	protected void setLineData(List<NetValue> list) {
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		int size = list.size();

		for (int i = 0; i < size; i++) {
			NetValue value = list.get(i);
			xVals.add(xLabelFormat.format(value.getApplyDate()));
			float val = (float) value.getNetValue();
			Entry entry = new Entry(val, i);
			entry.setData(value);
			yVals1.add(entry);
		}

		LineDataSet dataSet1 = new LineDataSet(yVals1, "净值");
		dataSet1.setDrawCubic(true);
		dataSet1.setLineWidth(1f);
		dataSet1.setDrawFilled(false);
		dataSet1.setFillColor(Color.parseColor("#22FF0000"));
		dataSet1.setColor(Constants.COLOR_POSITIVE);
		dataSet1.setDrawCircles(false);
		dataSet1.setHighLightColor(Color.BLACK);

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(dataSet1);
		LineData data = new LineData(xVals, dataSets);

		mLineChart.setData(data);
		mLineChart.invalidate();
		mLineChart.animateX(2000);
	}

	@Override
	public void onClick(View v) {

		if (v instanceof TextView) {
			TextView tv = (TextView) v;
			tv_label_range.setText(tv.getText().toString().trim());
			showRangeData();
		}

		btn_month.setSelected(false);
		btn_season.setSelected(false);
		btn_half.setSelected(false);
		btn_year.setSelected(false);

		v.setSelected(true);

		if (v == btn_month) {
			range = HistoryNetValueApi.MONTH;
		} else if (v == btn_season) {
			range = HistoryNetValueApi.SEASON;
		} else if (v == btn_half) {
			range = HistoryNetValueApi.HALF;
		} else if (v == btn_year) {
			range = HistoryNetValueApi.YEAR;
		}

		if (fund != null) {
			get(fund.fundCode);
		}
	}

	private void get(String fundCode) {
		mLineChart.highlightTouch(null);
		List<NetValue> list = dataCache.get(range);
		if (list != null) {
			setLineData(list);
		} else {
			api.get(fundCode, range);
		}
	}

	private void showSelectData(NetValue value) {
		tv_net_value.setText(String.valueOf(value.getNetValue()));
		String growValue = yLabelFormat.format(value.getGrowPercent()) + "%";
		tv_grow_value.setText(growValue);
		tv_date_value.setText(xLabelFormat.format(value.getApplyDate()));
		tv_grow_value.setTextColor(getColor(value.getGrowPercent()));
	}

	private void showRangeData() {
		List<NetValue> list = dataCache.get(range);
		if (list != null) {
			double first = list.get(0).getNetValue();
			double end = list.get(list.size() - 1).getNetValue();
			double percent = (end - first) / first * 100;
			String growValue = yLabelFormat.format(percent) + "%";
			tv_range_grow_value.setText(growValue);
			tv_range_grow_value.setTextColor(getColor(percent));
		}
	}

	private int getColor(double value) {
		int resId = R.color.val_zero;
		if (value == 0) {
			resId = R.color.val_zero;
		} else if (value < 0) {
			resId = R.color.val_negative;
		} else if (value > 0) {
			resId = R.color.val_positive;
		}
		return getResources().getColor(resId);
	}

}
