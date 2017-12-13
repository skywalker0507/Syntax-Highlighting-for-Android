package com.skywalker.syntaxhighlighter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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

public class HighlightView extends AppCompatEditText {

    private String mContent;
    private SpannableStringBuilder mBuilder;

    private static final String TAG = "HighlightView";
    private static final int MAX_LINES = 9999;
    private ScaleGestureDetector mScaleDetector;
    private static final int NUMBER_OFFSET = 5;
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
    private int mLineNumberWidth;
    private int mNumberBarWidth;

    private List<Integer> mIndexs;

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
        Typeface face = Typeface.createFromAsset(context.getAssets(), "SourceCodePro-Regular.otf");
        setTypeface(face);
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
        }

        setHorizontallyScrolling(!mWrapping);
        setFocusable(mEditable);

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

    public void setContent(String content) throws Exception {
        this.mContent = content.replaceAll("\r", "");
        mIndexs = new ArrayList<>();
        char key = '\n';
        for (Integer index = mContent.indexOf(key);
             index >= 0;
             index = mContent.indexOf(key, index + 1)) {
            mIndexs.add(index);
        }

        if (mIndexs.size() > MAX_LINES) {
            throw new Exception("文本超过最大支持长度");
        }

        mBuilder = new SpannableStringBuilder(content);

    }

    public void render(Mode mode) {
        if (mTheme == null) {
            mTheme = new DefaultTheme(getContext());
        }
        if (mContent == null) {
            throw new NullPointerException("文本内容为空");
        }

        setBackgroundColor(mTheme.getColor(Mode.KEY_BACKGROUND));
        setTextColor(mTheme.getColor(Mode.KEY_TEXT));
        Parser parser = new Parser(mode);
        parser.parse(mContent);
        for (RegexMatchResult result : parser.getMatchResults()) {
            mBuilder.setSpan(new ForegroundColorSpan(
                            mTheme.getColor(result.getKey())),
                    result.getStart(),
                    result.getEnd(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        if (mWrapping && mShowLineNumber) {
            mLineNumberWidth = (int) getPaint().measureText(Integer.toString(MAX_LINES));
            //设置行号显示部分的宽度
            mNumberBarWidth = mLineNumberWidth + NUMBER_OFFSET * 2;
            int startIndex = 0;
            for (int i = 0; i < mIndexs.size(); i++) {
                mBuilder.setSpan(new NumberSpan(mNumberBarWidth, i, Color.RED), startIndex, mIndexs.get(i), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                startIndex = mIndexs.get(i) + 1;
            }
        }

        setText(mBuilder);
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        //设置文本缩放
        if (mZoom && !mWrapping) {
            mScaleDetector.onTouchEvent(ev);
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mWrapping) {
            //计算最大行号的宽度
            mLineNumberWidth = (int) getPaint().measureText(Integer.toString(getLineCount()));
            //设置行号显示部分的宽度
            mNumberBarWidth = mLineNumberWidth + NUMBER_OFFSET * 2;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int baseline = getBaseline();

        if (mShowLineNumber) {
            //画行号的背景
            canvas.drawRect(getScaleX(), 0, mNumberBarWidth + getScrollX(), getLineHeight() * getLineCount(), mPaint);

            if (!mWrapping) {
                //绘制行号数
                int color = getPaint().getColor();
                getPaint().setColor(Color.WHITE);

                for (int i = 1; i <= getLineCount(); i++) {
                    canvas.drawText(Integer.toString(i), NUMBER_OFFSET + getScrollX(), baseline, getPaint());
                    baseline += getLineHeight();
                }

                //方法1： 使用canvas.save()和canvas.restore()
                /*canvas.save();
                canvas.translate(mNumberBarWidth + 5, 0);
                getPaint().setColor(color);
                super.onDraw(canvas);
                canvas.restore();*/

                //方法2： 使用canvas.translate
                canvas.translate(mNumberBarWidth + 5, 0);
                getPaint().setColor(color);
                super.onDraw(canvas);
                canvas.translate(-mNumberBarWidth + 5, 0);
            } else {
                super.onDraw(canvas);
            }


        } else {
            super.onDraw(canvas);
        }


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
