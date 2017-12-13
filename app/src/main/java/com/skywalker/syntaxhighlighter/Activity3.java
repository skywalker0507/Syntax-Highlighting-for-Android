package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;

import com.skywalker.syntaxhighlighter.languages.JavaMode;
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

        String file = "ChartComputator.java";
        //String file="highlight.js";

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
                    .showLineNumber(false)
                    .enableZoom(true)
                    .textWrapping(false);
            mHighlightView.setHighlightBuilder(builder);
            mHighlightView.render(new JavaMode());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
