package com.skywalker.syntaxhighlighter.themes;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.skywalker.syntaxhighlighter.R;
import com.skywalker.syntaxhighlighter.languages.common.Mode;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class AtomDarkTheme extends Theme {

    public AtomDarkTheme(Context context){

        mColors.put(Mode.KEY_TEXT,ContextCompat.getColor(context,R.color.atom_dark_text));
        mColors.put(Mode.KEY_BACKGROUND, ContextCompat.getColor(context,R.color.atom_dark_background));
        mColors.put(Mode.KEY_COMMENT, ContextCompat.getColor(context,R.color.atom_dark_comment));
        mColors.put(Mode.KEY_CONSTANT,ContextCompat.getColor(context,R.color.atom_dark_constant));
        mColors.put(Mode.KEY_NUMBER,ContextCompat.getColor(context,R.color.atom_dark_number));
        mColors.put(Mode.KEY_STATEMENT,ContextCompat.getColor(context,R.color.atom_dark_statement));
        mColors.put(Mode.KEY_STRING,ContextCompat.getColor(context,R.color.atom_dark_string));
        mColors.put(Mode.KEY_TYPE, ContextCompat.getColor(context,R.color.atom_dark_type));
    }
}
