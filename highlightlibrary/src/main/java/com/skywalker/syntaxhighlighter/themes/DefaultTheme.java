package com.skywalker.syntaxhighlighter.themes;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.skywalker.syntaxhighlighter.R;
import com.skywalker.syntaxhighlighter.languages.common.Mode;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/21               *
 *******************************/

public class DefaultTheme extends Theme {

    public DefaultTheme(Context context) {

        mColors.put(Mode.KEY_TEXT, ContextCompat.getColor(context, R.color.default_text));
        mColors.put(Mode.KEY_BACKGROUND, ContextCompat.getColor(context, R.color.default_background));
        mColors.put(Mode.KEY_COMMENT, ContextCompat.getColor(context, R.color.default_comment));
        mColors.put(Mode.KEY_CONSTANT, ContextCompat.getColor(context, R.color.default_constant));
        mColors.put(Mode.KEY_NUMBER, ContextCompat.getColor(context, R.color.default_number));
        mColors.put(Mode.KEY_STATEMENT, ContextCompat.getColor(context, R.color.default_statement));
        mColors.put(Mode.KEY_STRING, ContextCompat.getColor(context, R.color.default_string));
        mColors.put(Mode.KEY_TYPE, ContextCompat.getColor(context, R.color.default_type));
    }
}
