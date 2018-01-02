package com.skywalker.syntaxhighlighter.languages;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2018/1/2               *
 *******************************/

public class SelectClickableSpan extends ClickableSpan {
    private Context mContext;

    public SelectClickableSpan(Context context) {
        this.mContext = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }

    /**
     * Performs the click action associated with this span.
     *
     * @param widget
     */
    @Override
    public void onClick(View widget) {
        Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
    }
}
