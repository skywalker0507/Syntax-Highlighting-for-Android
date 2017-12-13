package com.skywalker.syntaxhighlighter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/11               *
 *******************************/

public class NumberSpan implements LeadingMarginSpan {

    private final int mColor;
    //private int mOffset;
    private int i = 1;
    private int mBar;
    public NumberSpan(int bar, int i, @ColorInt int color) {

        mColor = color;
        //this.mOffset = offset;
        this.mBar=bar;
        this.i = i;
    }

    public int getLeadingMargin(boolean first) {
        return mBar;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            int color = p.getColor();
            p.setColor(mColor);
            p.setStyle(Paint.Style.FILL);
            c.drawText(Integer.toString(i), 6+x, baseline, p);
            p.setColor(color);
        }
    }
}
