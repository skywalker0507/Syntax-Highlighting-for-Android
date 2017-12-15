package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import com.skywalker.syntaxhighlighter.languages.CMode;
import com.skywalker.syntaxhighlighter.themes.DefaultTheme;

import java.io.IOException;
import java.io.InputStream;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/13               *
 *******************************/

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);
        HighlightView mHighlightView = findViewById(R.id.highlightView);

        //String file = "ChartComputator.java";
        //String file = "highlight.js";
        String file="ffmpeg.c";
        try {
            InputStream inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            mHighlightView.setContent(new String(buffer));
            mHighlightView.setMovementMethod(ScrollingMovementMethod.getInstance());
            HighlightView.Builder builder = new HighlightView.Builder()
                    .enableEdit(false)
                    .setTheme(new DefaultTheme(this))
                    .showLineNumber(true)
                    .enableZoom(true)
                    .textWrapping(false);
            mHighlightView.setHighlightBuilder(builder);
            mHighlightView.render(new CMode());
            int length=mHighlightView.getText().toString().length();
            Log.e("length",""+length);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
