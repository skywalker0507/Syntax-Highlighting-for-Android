package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;

import com.skywalker.syntaxhighlighter.themes.DefaultTheme;

import java.io.IOException;
import java.io.InputStream;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/13               *
 *******************************/

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        HighlightView1 mHighlightView = findViewById(R.id.HighlightView1);

        String file = "ChartComputator.java";
        //String file="highlight.js";
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            mHighlightView.setContent(new String(buffer));
            mHighlightView.setMovementMethod(ScrollingMovementMethod.getInstance());
            HighlightView1.Builder builder = new HighlightView1.Builder()
                    .enableEdit(false)
                    .setTheme(new DefaultTheme(this))
                    .showLineNumber(true)
                    .enableZoom(true)
                    .textWrapping(true);
            mHighlightView.setHighlightBuilder(builder);
            mHighlightView.render();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
