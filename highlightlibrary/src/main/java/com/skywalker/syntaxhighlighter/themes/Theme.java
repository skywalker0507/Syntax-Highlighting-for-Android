package com.skywalker.syntaxhighlighter.themes;

import android.support.annotation.ColorInt;
import android.util.SparseIntArray;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class Theme {

    protected SparseIntArray mColors=new SparseIntArray();

    public @ColorInt int getColor(int key){
        return mColors.get(key);
    }
}
