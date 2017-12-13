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

    private static final int NUMBER_OFFSET = 5;
    private final int mColor;
    private static final int PADDING = 4;
    private int i = 1;
    private int mBarWidth;

    public NumberSpan(int barWidth, int i, @ColorInt int color) {

        mColor = color;
        this.mBarWidth = barWidth;
        this.i = i;
    }

    public int getLeadingMargin(boolean first) {
        return mBarWidth + PADDING;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            int color = p.getColor();
            p.setColor(mColor);
            p.setStyle(Paint.Style.FILL);
            c.drawText(Integer.toString(i), NUMBER_OFFSET + x, baseline, p);
            p.setColor(color);
        }
    }
}
