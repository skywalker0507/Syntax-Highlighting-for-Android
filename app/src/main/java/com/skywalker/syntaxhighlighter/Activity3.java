package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.skywalker.syntaxhighlighter.languages.JavaMode;
import com.skywalker.syntaxhighlighter.themes.AtomDarkTheme;

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
        final HighlightView mHighlightView = findViewById(R.id.highlightView);
        String file = "ChartComputator.java";
        //String file = "highlight.js";
        //String file="ffmpeg.c";
        try {
            InputStream inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            mHighlightView.setContent(new String(buffer));
            mHighlightView.setMovementMethod(ScrollingMovementMethod.getInstance());
            //mHighlightView.setMovementMethod(SelectMovementMethod.getInstance());
            HighlightView.Builder builder = new HighlightView.Builder()
                    .enableEdit(false)
                    .setTheme(new AtomDarkTheme(this))
                    .showLineNumber(true)
                    .enableZoom(true)
                    .textWrapping(false);
            mHighlightView.setHighlightBuilder(builder);
            mHighlightView.render(new JavaMode());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundColorSpan[] s= mHighlightView.getText().getSpans(0,mHighlightView.getText().length(), BackgroundColorSpan.class);
                StringBuilder stringBuilder=new StringBuilder();
                for (BackgroundColorSpan span:s){

                    stringBuilder.append(mHighlightView.getText().subSequence(mHighlightView.getText().getSpanStart(span), mHighlightView.getText().getSpanEnd(span)));
                }

                Log.e("copy",stringBuilder.toString());
            }
        });
    }
}
