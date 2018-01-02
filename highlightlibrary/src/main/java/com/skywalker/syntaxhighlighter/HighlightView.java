package com.skywalker.syntaxhighlighter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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

import java.io.InputStream;
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
    private static final int PADDING_RIGHT = 10;
    private float mScaleFactor = 1.f;
    private float mDefaultTextSize;

    private int mTransparentBackground;
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

    private List<Integer> mIndexs = new ArrayList<>();
    private List<Integer> mSelectedList = new ArrayList<>();

    public static class Builder {

        private boolean mShowLineNumber = true;
        private boolean mWrapping = false;
        private boolean mZoom = true;
        private boolean mEditable = false;
        private Theme mTheme;

        private float mZoomUpperLimit = 6;
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
        this(context, attrs, 0);
    }

    public HighlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HighlightView,
                0, 0);

        try {
            mEditable = a.getBoolean(R.styleable.HighlightView_editable, false);
            mShowLineNumber = a.getBoolean(R.styleable.HighlightView_showLineNumber, true);
            mWrapping = a.getBoolean(R.styleable.HighlightView_wrapping, false);
            mZoom = a.getBoolean(R.styleable.HighlightView_zoom, true);
            mZoomUpperLimit = a.getFloat(R.styleable.HighlightView_zoomUpperLimit, 3);
            mZoomLowerLimit = a.getFloat(R.styleable.HighlightView_zoomLowerLimit, 1);

        } finally {
            a.recycle();
        }

        Typeface face = Typeface.createFromAsset(context.getAssets(), "SourceCodePro-Regular.otf");
        setTypeface(face);
        setTextIsSelectable(true);

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


    private void initialize() {
        mDefaultTextSize = getTextSize();
        if (mZoom) {
            mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        }

        if (mShowLineNumber) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mTheme.getColor(Mode.KEY_LINE_BAR));
            mPaint.setStrokeWidth(10);
        }

        setHorizontallyScrolling(!mWrapping);

        setFocusable(mEditable);
        setFocusableInTouchMode(mEditable);


    }

    public void setContent(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        inputStream.close();
        String t = new String(buffer);
        setContent(t);

    }

    public void setContent(String content) throws Exception {
        this.mContent = content.replaceAll("\r\n", "\n");
        char key = '\n';
        for (Integer index = mContent.indexOf(key);
             index >= 0;
             index = mContent.indexOf(key, index + 1)) {
            mIndexs.add(index);
        }

        if (mIndexs.size() > MAX_LINES) {
            throw new Exception("文本超过最大支持长度");
        }

        mBuilder = new SpannableStringBuilder(mContent);

    }


    public void render(Theme theme, Mode mode) {
        this.mTheme = theme;
        render(mode);
    }

    public void render(Mode mode) {
        if (mTheme == null) {
            mTheme = new DefaultTheme(getContext());
        }
        if (mContent == null) {
            throw new NullPointerException("文本内容为空");
        }
        mTransparentBackground = Color.parseColor("#80282828");

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
                mBuilder.setSpan(new NumberSpan(mNumberBarWidth, i + 1, mTheme.getColor(Mode.KEY_LINE_NUMBER)), startIndex, mIndexs.get(i), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                startIndex = mIndexs.get(i) + 1;
            }
        }

        setText(mBuilder);
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        //设置文本缩放
        if (mZoom && !mWrapping) {
            mScaleDetector.onTouchEvent(event);
        }

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            if (x < mNumberBarWidth) {
                int line = getLayout().getLineForVertical(y);
                int off = getLayout().getOffsetForHorizontal(line, x);

                Log.e("line", "" + line);
                Log.e("off", "" + off);
                int[] t = findLine(off);
                if (mSelectedList.contains(t[0])) {
                    mSelectedList.remove(Integer.valueOf(t[0]));
                    BackgroundColorSpan span[] = getText().getSpans(t[0], t[1], BackgroundColorSpan.class);
                    if (span.length > 0) {
                        getText().removeSpan(span[0]);
                    }
                } else {
                    mSelectedList.add(t[0]);
                    getText().setSpan(new BackgroundColorSpan(Color.YELLOW), t[0], t[1], Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                }
            }


        }
        super.onTouchEvent(event);
        return true;
    }

    private int[] findLine(int offset) {
        int[] result = new int[2];
        for (int i = 0; i < mIndexs.size(); i++) {
            if (mIndexs.get(i) >= offset) {
                result[0] = mIndexs.get(i);
                if (i == mIndexs.size() - 1) {
                    result[1] = mBuilder.length();
                } else {
                    result[1] = mIndexs.get(i + 1);
                }

                break;

            }
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mWrapping) {
            //计算最大行号的宽度
            mLineNumberWidth = (int) getPaint().measureText(Integer.toString(getLineCount()));
            //设置行号显示部分的宽度
            mNumberBarWidth = mLineNumberWidth + NUMBER_OFFSET * 2;
            setPadding(0, 0, mNumberBarWidth + PADDING_RIGHT, 0);
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


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, mZoomUpperLimit));
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize * mScaleFactor);
            Log.e(TAG, String.valueOf(mScaleFactor));
            return true;
        }
    }

}
