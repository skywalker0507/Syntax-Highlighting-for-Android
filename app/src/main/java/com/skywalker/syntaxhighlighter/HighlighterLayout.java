package com.skywalker.syntaxhighlighter;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/21               *
 *******************************/

public class HighlighterLayout extends LinearLayout {

    private TextView mLineNumberView;
    private TextView mTextView;


    public HighlighterLayout(Context context) {
        this(context,null);
    }

    public HighlighterLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HighlighterLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    @TargetApi(21)
    public HighlighterLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.highlighter_layout, this);
        mLineNumberView= findViewById(R.id.LineNumberTextView);
        mTextView=findViewById(R.id.CodeTextView);
    }

    public void setText(String text){
        mTextView.setText(text);

    }


}
