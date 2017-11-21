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
import com.skywalker.syntaxhighlighter.themes.Theme;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class HighlightView extends AppCompatEditText {
    private Theme mTheme;
    private String mContent;
    private SpannableStringBuilder mBuilder;

    private static final String TAG = "HighlightView";
    private ScaleGestureDetector mScaleDetector;

    private float mScaleFactor = 1.f;
    private float defaultSize;

    private float zoomLimit = 3.0f;
    private Paint paint;


    public HighlightView(Context context) {
        this(context, null);
    }

    public HighlightView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textStyle);
    }

    public HighlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    public void setTheme(Theme theme) {
        this.mTheme = theme;
        setBackgroundColor(theme.getColor(Mode.KEY_BACKGROUND));
        setTextColor(theme.getColor(Mode.KEY_TEXT));
    }

    public void setContent(String content) {
        this.mContent = content;
        mBuilder = new SpannableStringBuilder(content);
    }

    public void render() {
        Parser parser = new Parser(new JavaMode());
        parser.pase(mContent);
        for (RegexMatchResult result : parser.getMatchResults()) {

            mBuilder.setSpan(new ForegroundColorSpan(
                    mTheme.getColor(result.getKey())),
                    result.getStart(),
                    result.getEnd(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        setText(mBuilder);
    }


    private void initialize() {
        defaultSize = getTextSize();
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        /*paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);*/
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
    }

    /***
     * @param zoomLimit
     * Default value is 3, 3 means text can zoom 3 times the default size
     */

    public void setZoomLimit(float zoomLimit) {
        this.zoomLimit = zoomLimit;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        mScaleDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String strCount = String.valueOf(getLineCount());
        int width = (int) (getPaint().measureText(strCount) * 1.5);
        int baseline = getBaseline();
        setPadding(width, 0, 0, 0);

        paint.setStrokeWidth(width * 1.5f);
        canvas.drawLine(0, 0, 0, getLineHeight() * getLineCount(), paint);

        for (int i = 0; i < getLineCount(); i++) {
            canvas.drawText("" + (i + 1), 0, baseline, getPaint());
            baseline += getLineHeight();
        }
        super.onDraw(canvas);


        /*String strCount = String.valueOf(getLineCount());

        float[] symbolWidths = new float[strCount.length()];
        paint.getTextWidths(strCount, symbolWidths);

        float strokeWidth = 0;
        for (float width : symbolWidths)
            strokeWidth += width;
        strokeWidth = strokeWidth *2*//*I dnt knw y*//* + strokeWidth;
        paint.setStrokeWidth(strokeWidth);
        setPadding((int)strokeWidth / 2, 0, 0, 0); // text padding

        canvas.drawLine(rect.left, getLineHeight() * getLineCount(), rect.right, rect.top, paint);

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
