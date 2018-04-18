package com.skywalker.syntaxhighlighter

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan

/*******************************
 * Created by liuqiang          *
 *
 * data: 2017/12/11               *
 */

class NumberSpan(private val mBarWidth: Int, private val i: Int, @ColorInt private val mColor: Int) : LeadingMarginSpan {

    private val NUMBER_OFFSET = 5
    private val PADDING = 4

    override fun getLeadingMargin(first: Boolean): Int {
        return mBarWidth + PADDING
    }

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                                   top: Int, baseline: Int, bottom: Int,
                                   text: CharSequence, start: Int, end: Int,
                                   first: Boolean, l: Layout) {
        if ((text as Spanned).getSpanStart(this) == start) {
            val color = p.color
            p.color = mColor
            p.style = Paint.Style.FILL
            c.drawText(Integer.toString(i), (NUMBER_OFFSET + x).toFloat(), baseline.toFloat(), p)
            p.color = color
        }
    }
}
