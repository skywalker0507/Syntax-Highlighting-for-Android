package com.skywalker.syntaxhighlighter.themes

import android.support.annotation.ColorInt
import android.util.SparseIntArray

/*******************************
 * Created by liuqiang          *
 *
 * data: 2017/11/20               *
 */

abstract class Theme {

    protected var mColors = SparseIntArray()

    @ColorInt
    fun getColor(key: Int): Int {
        return mColors.get(key)
    }
}
