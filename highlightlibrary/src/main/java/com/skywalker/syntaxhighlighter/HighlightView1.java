package com.skywalker.syntaxhighlighter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.skywalker.syntaxhighlighter.languages.JavaScriptMode;
import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.languages.common.RegexMatchResult;
import com.skywalker.syntaxhighlighter.themes.DefaultTheme;
import com.skywalker.syntaxhighlighter.themes.Theme;

import java.util.ArrayList;
import java.util.List;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class HighlightView1 extends AppCompatEditText {

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
    private List<Integer> mIndexs;

    private Paint mPaint;
    private Paint mNumberPaint;
    private int mLineNumberWidth;
    private int mNumberBarWidth;
    private int left=0;
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

    public HighlightView1(Context context) {
        this(context, null);
    }

    public HighlightView1(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textStyle);
    }

    public HighlightView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(5, 5, 5, 10);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "SourceCodePro-Regular.otf");
        setTypeface(face);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    private void initialize() {
        defaultSize = getTextSize();
        if (mZoom) {
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        }

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(10);

        if (mShowLineNumber) {

            mNumberPaint = new Paint();
            mNumberPaint.setStyle(Paint.Style.FILL);
            mNumberPaint.setColor(Color.WHITE);
            mNumberPaint.setStrokeWidth(getPaint().getStrokeWidth());
            mNumberPaint.setTextSize(getPaint().getTextSize());
        }

        if (mWrapping) {
            mIndexs = new ArrayList<>();
            char key = '\n';
            for (Integer index = mContent.indexOf(key);
                 index >= 0;
                 index = mContent.indexOf(key, index + 1)) {
                mIndexs.add(index);
            }
        } else {

            //setHorizontallyScrolling(true);
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
        this.mContent = content.replaceAll("\r", "");
        ;

        mBuilder = new SpannableStringBuilder(content);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        String strCount = String.valueOf(getLineCount());
        Log.e("getLineCount", strCount);
        //计算最大行号的宽度
        mLineNumberWidth = (int) getPaint().measureText(strCount);
        //设置行号显示部分的宽度
        mNumberBarWidth = mLineNumberWidth + 12;
        Log.e("mNumberBarWidth", "" + mNumberBarWidth);

    }

    public void render() {
        if (mTheme == null) {
            mTheme = new DefaultTheme(getContext());
        }
        if (mContent == null) {
            throw new NullPointerException("文本内容为空");
        }

        setBackgroundColor(mTheme.getColor(Mode.KEY_BACKGROUND));
        setTextColor(mTheme.getColor(Mode.KEY_TEXT));
        Parser parser = new Parser(new JavaScriptMode());
        parser.parse(mContent);
        for (RegexMatchResult result : parser.getMatchResults()) {

            mBuilder.setSpan(new ForegroundColorSpan(
                            mTheme.getColor(result.getKey())),
                    result.getStart(),
                    result.getEnd(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        if (mWrapping) {
            int startIndex = 0;
            int w =(int)getPaint().measureText("9999");
            for (int i = 0; i < mIndexs.size(); i++) {
                mBuilder.setSpan(new NumberSpan(w, i, Color.RED), startIndex, mIndexs.get(i), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                startIndex = mIndexs.get(i) + 1;
            }
        }

        setText(mBuilder);



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
        Log.e("getX", "" + getX());
        Log.e("getScrollX", "" + getScrollX());
        Log.e("getTranslationX", "" + getTranslationX());

        //setPadding(mLineNumberWidth + 16,0,10,0);

        if (mShowLineNumber) {
            //画行号的背景
            canvas.drawRect(0, 0, mNumberBarWidth, getLineHeight() * getLineCount(), mPaint);

        }

        super.onDraw(canvas);
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
