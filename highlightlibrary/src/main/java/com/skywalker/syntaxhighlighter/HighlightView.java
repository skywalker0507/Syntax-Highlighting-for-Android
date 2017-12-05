package com.skywalker.syntaxhighlighter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.skywalker.syntaxhighlighter.languages.JavaMode;
import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.languages.common.RegexMatchResult;
import com.skywalker.syntaxhighlighter.themes.DefaultTheme;
import com.skywalker.syntaxhighlighter.themes.Theme;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class HighlightView extends AppCompatEditText {

    private String mContent;
    private SpannableStringBuilder mBuilder;

    private static final String TAG = "HighlightView";
    private ScaleGestureDetector mScaleDetector;

    private float mScaleFactor = 1.f;
    private float defaultSize;

    private float zoomLimit = 3.0f;
    private boolean mShowLineNumber;
    private boolean mWrapping;
    private boolean mZoom;
    private boolean mEditable;
    private float mZoomUpperLimit;
    private float mZoomLowerLimit;
    private Theme mTheme;


    private Paint mPaint;
    private Paint mNumberPaint;
    private int mLineNumberWidth;
    private int mNumberBarWidth;

    public static class Builder {

        private boolean mShowLineNumber = false;
        private boolean mWrapping = false;
        private boolean mZoom = false;
        private boolean mEditable = false;
        private Theme mTheme;

        private float mZoomUpperLimit;
        private float mZoomLowerLimit;

        public Builder showLineNumber(boolean showLineNumber) {
            mShowLineNumber = showLineNumber;
            return this;
        }

        public Builder setTheme(Theme theme) {
            this.mTheme = theme;
            return this;
        }

        public Builder textWrapping(boolean wrapping) {
            this.mWrapping = wrapping;
            return this;
        }

        public Builder enableZoom(boolean isEnable) {
            this.mZoom = isEnable;
            return this;
        }

        public Builder setZoomUpperLimit(float limit) {
            this.mZoomUpperLimit = limit;
            return this;
        }

        public Builder setZoomLowerLimit(float limit) {
            this.mZoomLowerLimit = limit;
            return this;
        }

        public Builder enableEdit(boolean isEnable) {
            this.mEditable = isEnable;
            return this;
        }


    }

    public HighlightView(Context context) {
        this(context, null);
    }

    public HighlightView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textStyle);
    }

    public HighlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    private void initialize() {
        defaultSize = getTextSize();
        if (mZoom) {
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        }


        if (mShowLineNumber) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.GRAY);
            mPaint.setStrokeWidth(10);

            mNumberPaint=new Paint();
            mNumberPaint.setStyle(Paint.Style.FILL);
            mNumberPaint.setColor(Color.WHITE);
            mNumberPaint.setStrokeWidth(getPaint().getStrokeWidth());
            mNumberPaint.setTextSize(getPaint().getTextSize());
        }

        if (!mEditable) {
            setFocusable(false);
        }

    }

    public void setHighlightBuilder(Builder builder) {
        this.mTheme = builder.mTheme;
        this.mEditable = builder.mEditable;
        this.mShowLineNumber = builder.mShowLineNumber;
        this.mZoomUpperLimit = builder.mZoomUpperLimit;
        this.mZoomLowerLimit = builder.mZoomLowerLimit;
        this.mWrapping = builder.mWrapping;
        this.mZoom = builder.mZoom;

        initialize();

    }

    public void setContent(String content) {
        this.mContent = content;
        mBuilder = new SpannableStringBuilder(content);
    }

    public void render() {
        if (mTheme == null) {
            mTheme = new DefaultTheme(getContext());
        }
        if (mContent == null) {
            throw new NullPointerException("文本内容为空");
        }

        if (!mWrapping) {
            setHorizontallyScrolling(true);

        }

        setBackgroundColor(mTheme.getColor(Mode.KEY_BACKGROUND));
        setTextColor(mTheme.getColor(Mode.KEY_TEXT));
        Parser parser = new Parser(new JavaMode());
        parser.parse(mContent);
        for (RegexMatchResult result : parser.getMatchResults()) {

            mBuilder.setSpan(new ForegroundColorSpan(
                            mTheme.getColor(result.getKey())),
                    result.getStart(),
                    result.getEnd(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        setText(mBuilder);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mShowLineNumber) {
            //获取TextView的行数
            String strCount = String.valueOf(getLineCount());
            //计算最大行号的宽度
            mLineNumberWidth = (int) getPaint().measureText(strCount);

            //设置行号显示部分的宽度
            mNumberBarWidth = mLineNumberWidth + 12;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        //设置文本缩放
        if (mZoom) {
            mScaleDetector.onTouchEvent(ev);
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int baseline = getBaseline();
        Log.e("getX",""+getX());
        Log.e("getScrollX",""+getScrollX());
        Log.e("getTranslationX",""+getTranslationX());


        if (mShowLineNumber) {
            //画行号的背景
            canvas.drawRect(getScrollX(), 0, getScrollX()+mNumberBarWidth, getLineHeight() * getLineCount(), mPaint);
            //绘制行号数
            for (int i = 1; i <= getLineCount(); i++) {
                canvas.drawText(Integer.toString(i), 5+getScrollX(), baseline, mNumberPaint);
                baseline += getLineHeight();
            }

            canvas.translate(mLineNumberWidth + 16, 0);
        }
        super.onDraw(canvas);


        /*String strCount = String.valueOf(getLineCount());

        float[] symbolWidths = new float[strCount.length()];
        mPaint.getTextWidths(strCount, symbolWidths);

        float strokeWidth = 0;
        for (float width : symbolWidths)
            strokeWidth += width;
        strokeWidth = strokeWidth *2*//*I dnt knw y*//* + strokeWidth;
        mPaint.setStrokeWidth(strokeWidth);
        setPadding((int)strokeWidth / 2, 0, 0, 0); // text padding

        canvas.drawLine(rect.left, getLineHeight() * getLineCount(), rect.right, rect.top, mPaint);

        super.onDraw(canvas);*/
    }

    /*Scale Gesture listener class,
    mScaleFactor is getting the scaling value
    and mScaleFactor is mapped between 1.0 and and zoomLimit
    that is 3.0 by default. You can also change it. 3.0 means text
    can zoom to 3 times the default value.*/

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, zoomLimit));
            setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize * mScaleFactor);
            Log.e(TAG, String.valueOf(mScaleFactor));
            return true;
        }
    }

}
