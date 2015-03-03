package com.cmeiyuan.widget.chart;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cmeiyuan.common.TextUtil;
import com.github.mikephil.charting.utils.Utils;
import com.cmeiyuan.widget.R;

/**
 * @author 334388454@qq.com
 */
public class CircleChartView extends View {

	protected static final String TAG = "CircleChartView";

	// 百分比
	private float mPercentValue;
	// 初始角度
	private int mInitialAngle = -90;
	// 外环宽度
	private float mOuterRingWidth;
	// 内环宽度
	private float mInnerRingWidth;
	// 外环起始颜色
	private final int mOuterRingStartColor;
	// 外环结束颜色
	private int mOuterRingEndColor;
	// 外环颜色
	private int mOuterRingColor;
	// 内环颜色
	private int mInnerRingColor;
	// 内环颜色
	private int mCenterFillColor;
	// 背景层画笔
	private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 中间层画笔
	private Paint mCenterFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 外环圈画笔
	private Paint mPercentFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 主标签画笔
	private final Paint mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 标签后缀画笔
	private final Paint mLableSuffixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 附加标签画笔
	private final Paint mExtraLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// 圆环区域
	private final RectF mRect = new RectF();
	// 渐变变换矩阵
	private final Matrix mMatrix = new Matrix();
	// 扫描渐变
	private SweepGradient mSweepGradient;
	// 小数点截取
	private DecimalFormat mFormat = new DecimalFormat("#0.00");
	// 附加文字
	private String mExtraLabel;
	// 文字间距
	private float mLabelMargin;
	// 黄金分割点、比例数字显示基线
	private final float mGoldenPoint = 0.618f;

	public CircleChartView(Context context) {
		this(context, null);
	}

	public CircleChartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleChartView, defStyle, 0);

		int percent = a.getInt(R.styleable.CircleChartView_percent, 0);
		int outerRingColor = a.getColor(R.styleable.CircleChartView_outerRingColor, Color.RED);
		int innerRingColor = a.getColor(R.styleable.CircleChartView_innerRingColor, Color.GRAY);
		int outerRingStartColor = a.getColor(R.styleable.CircleChartView_outerRingStartColor, Color.YELLOW);
		int outerRingEndColor = a.getColor(R.styleable.CircleChartView_outerRingEndColor, Color.RED);
		int centerAreaColor = a.getColor(R.styleable.CircleChartView_centerAreaColor, Color.WHITE);
		float outerRingWidth = a.getDimension(R.styleable.CircleChartView_outerRingWidth, 60);
		float innerRingWidth = a.getDimension(R.styleable.CircleChartView_innerRingWidth, 45);
		float textSize = a.getDimension(R.styleable.CircleChartView_textSize, 120);
		int textColor = a.getColor(R.styleable.CircleChartView_textColor, Color.RED);
		float extraTextSize = a.getDimension(R.styleable.CircleChartView_extraTextSize, 120);
		int extraTextColor = a.getColor(R.styleable.CircleChartView_extraTextColor, Color.BLACK);
		float margin = a.getDimension(R.styleable.CircleChartView_textLabelMagin, 30);
		String fontName = a.getString(R.styleable.CircleChartView_fontName);

		this.mPercentValue = percent;
		this.mOuterRingColor = outerRingColor;
		this.mInnerRingColor = innerRingColor;
		this.mOuterRingStartColor = outerRingStartColor;
		this.mOuterRingEndColor = outerRingEndColor;
		this.mCenterFillColor = centerAreaColor;
		this.mOuterRingWidth = outerRingWidth;
		this.mInnerRingWidth = innerRingWidth;
		this.mLabelPaint.setTextSize(textSize);
		this.mLabelPaint.setColor(textColor);
		this.mLableSuffixPaint.setColor(textColor);
		this.mLableSuffixPaint.setTextSize(textSize / 2);
		this.mExtraLabelPaint.setTextSize(extraTextSize);
		this.mExtraLabelPaint.setColor(extraTextColor);
		this.mLabelMargin = margin;

		if (fontName != null && fontName.length() > 0) {
			try {
				mLabelPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontName));
				mLableSuffixPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontName));
			} catch (Exception e) {
			}
		}
		a.recycle();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 中心点坐标
		float cirX = getCenterX();
		float cirY = getCenterY();
		// 半径
		float radius = getRadius();
		float sweepAngle = mPercentValue * 360 / 100;

		float left = cirX - radius;
		float top = cirY - radius;
		float right = cirX + radius;
		float bottom = cirY + radius;

		// 绘制背景
		mCenterFillPaint.setStyle(Style.FILL);
		mCenterFillPaint.setColor(mCenterFillColor);
		float bgRadius = radius - Math.abs(mOuterRingWidth - mInnerRingWidth);
		canvas.drawCircle(cirX, cirY, bgRadius, mCenterFillPaint);

		// 绘制内环
		float innerL = left + (mOuterRingWidth - mInnerRingWidth / 2);
		float innerT = top + (mOuterRingWidth - mInnerRingWidth / 2);
		float innerR = right - (mOuterRingWidth - mInnerRingWidth / 2);
		float innerB = bottom - (mOuterRingWidth - mInnerRingWidth / 2);
		mRect.set(innerL, innerT, innerR, innerB);
		mBackgroundPaint.setStyle(Style.STROKE);
		mBackgroundPaint.setColor(mInnerRingColor);
		mBackgroundPaint.setStrokeWidth(mInnerRingWidth);
		canvas.drawArc(mRect, getInitialAngle(), -360, false, mBackgroundPaint);

		// 绘制外环
		if (mSweepGradient != null) {
			mMatrix.reset();
			mMatrix.postRotate(270, cirX, cirX);
			mSweepGradient.setLocalMatrix(mMatrix);
			mPercentFillPaint.setShader(mSweepGradient);
		}

		float offset = 0;
		float outerL = left + (mOuterRingWidth / 2 + offset);
		float outerT = top + (mOuterRingWidth / 2 + offset);
		float ourerR = right - (mOuterRingWidth / 2 + offset);
		float outerB = bottom - (mOuterRingWidth / 2 + offset);
		mRect.set(outerL, outerT, ourerR, outerB);
		mPercentFillPaint.setStyle(Style.STROKE);
		mPercentFillPaint.setStrokeWidth(mOuterRingWidth);
		mPercentFillPaint.setColor(mOuterRingColor);
		canvas.drawArc(mRect, getInitialAngle() + 0.2f, sweepAngle, false, mPercentFillPaint);

		String labelText = mFormat.format(mPercentValue);
		String suffixText = "%";
		String extraText = mExtraLabel;

		Paint lablePaint = mLabelPaint;
		Paint lableSuffixPaint = mLableSuffixPaint;
		Paint extraLabelPaint = mExtraLabelPaint;

		int labelH = TextUtil.getTextHeight(lablePaint, "63.23");

		// 绘制数字
		if (labelText != null && labelText.length() > 0) {
			float p = mGoldenPoint;
			boolean isSingleLine = TextUtils.isEmpty(extraText);
			if (isSingleLine) {
				lableSuffixPaint.setTextSize(lablePaint.getTextSize() * 0.7f);
			}
			float x1 = cirX - lableSuffixPaint.getTextSize() * 0.3f;
			float y1 = isSingleLine ? cirY + lablePaint.getTextSize() * 0.3f : (getHeight() * p);

			lablePaint.setTextAlign(Align.CENTER);
			canvas.drawText(labelText, x1, y1, lablePaint);

			lableSuffixPaint.setTextAlign(Align.LEFT);
			canvas.drawText(suffixText, x1 + lablePaint.measureText(labelText) / 2 + lableSuffixPaint.getTextSize()
					* 0.12f, y1, lableSuffixPaint);
		}

		// 绘制附加文字
		if (extraText != null && extraText.length() > 0) {
			float p = mGoldenPoint;
			int x1 = (int) cirX;
			int y1 = (int) (getHeight() * p - labelH - mLabelMargin);
			extraLabelPaint.setTextAlign(Align.CENTER);
			canvas.drawText(extraText, x1, y1, extraLabelPaint);
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w > 0 && h > 0) {
			float cirX = getCenterX();
			float cirY = getCenterY();
			int startColor = mOuterRingStartColor;
			int endColor = mOuterRingEndColor;
			mSweepGradient = new SweepGradient(cirX, cirY, startColor, endColor);
		}
	}

	protected float getRadius() {
		return (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - Utils.convertDpToPixel(5)) / 2f;
	}

	protected float getCenterX() {
		return getMeasuredWidth() / 2f;
	}

	protected float getCenterY() {
		return getMeasuredHeight() / 2f;
	}

	public void setFloatFormat(String format) {
		mFormat = new DecimalFormat(format);
	}

	public float getOuterRingWidth() {
		return mOuterRingWidth;
	}

	public void setOuterRingWidth(float width) {
		this.mOuterRingWidth = width;
	}

	public float getInnerRingWidth() {
		return mInnerRingWidth;
	}

	public void setInnerRingWidth(float width) {
		this.mInnerRingWidth = width;
	}

	public int getOuterRingStartColor() {
		return mOuterRingStartColor;
	}

	public int getOuterRingEndColor() {
		return mOuterRingEndColor;
	}

	public int getOuterRingColor() {
		return mOuterRingColor;
	}

	public void setOuterRingColor(int color) {
		this.mOuterRingColor = color;
	}

	public int getInnerRingColor() {
		return mInnerRingColor;
	}

	public void setInnerRingColor(int color) {
		this.mInnerRingColor = color;
	}

	public int getCenterFillColor() {
		return mCenterFillColor;
	}

	public void setCenterFillColor(int color) {
		this.mCenterFillColor = color;
	}

	public Paint getBackgroundPaint() {
		return mBackgroundPaint;
	}

	public void setBackgroundPaint(Paint paint) {
		this.mBackgroundPaint = paint;
	}

	public Paint getCenterFillPaint() {
		return mCenterFillPaint;
	}

	public void setCenterFillPaint(Paint paint) {
		this.mCenterFillPaint = paint;
	}

	public Paint getPercentFillPaint() {
		return mPercentFillPaint;
	}

	public void setPercentFillPaint(Paint paint) {
		this.mPercentFillPaint = paint;
	}

	public Paint getLabelPaint() {
		return mLabelPaint;
	}

	public Paint getLableSuffixPaint() {
		return mLableSuffixPaint;
	}

	public Paint getExtraLabelPaint() {
		return mExtraLabelPaint;
	}

	public float getTextLabelMargin() {
		return mLabelMargin;
	}

	public void setTextLabelMargin(float margin) {
		this.mLabelMargin = margin;
	}

	public void setPercent(float percent) {
		this.mPercentValue = percent;
		invalidate();
	}

	public float getPercent() {
		return mPercentValue;
	}

	public void setExtraLabel(String text) {
		this.mExtraLabel = text;
	}

	public String getExtraLabel() {
		return mExtraLabel;
	}

	public void setTextSize(float textSize) {
		this.mLabelPaint.setTextSize(textSize);
	}

	public void setTextColor(int color) {
		this.mLabelPaint.setColor(color);
	}

	public void setOuterRingStartColor(int color) {
		this.mOuterRingColor = color;
	}

	public void setOuterRingEndColor(int color) {
		this.mOuterRingEndColor = color;
	}

	public int getInitialAngle() {
		return mInitialAngle;
	}

	public void setInitialAngle(int angle) {
		this.mInitialAngle = angle;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = 0;
		int height = 0;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = widthSize;
		}

		if (heightMode == MeasureSpec.UNSPECIFIED) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = heightSize;
		}

		int wh = Math.min(width, height);
		setMeasuredDimension(wh, wh);
	}

}
