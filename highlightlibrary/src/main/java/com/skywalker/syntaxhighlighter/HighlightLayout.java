package com.skywalker.syntaxhighlighter;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/21               *
 *******************************/

public class HighlightLayout extends LinearLayout {
    private TextView mLineNumberView;
    private TextView mTextView;
    private float mWidth;
    public HighlightLayout(Context context) {
        this(context,null);
    }

    public HighlightLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HighlightLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0,0);
    }

    @TargetApi(21)
    public HighlightLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        mLineNumberView=new TextView(context);
        mTextView=new TextView(context);
    }

    public void setText(String text){
        mTextView.setText(text);
        String strCount = String.valueOf(mTextView.getLineCount());
        mWidth=mTextView.getPaint().measureText(strCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
