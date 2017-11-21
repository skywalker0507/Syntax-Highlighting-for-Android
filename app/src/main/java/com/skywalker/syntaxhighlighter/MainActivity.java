package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import com.skywalker.syntaxhighlighter.themes.DefaultTheme;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private HighlightView mHighlightView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHighlightView=findViewById(R.id.HighlightView);
        mHighlightView.setMovementMethod(ScrollingMovementMethod.getInstance());
        //mHighlightView.setHorizontallyScrolling(true);
        try {
            //String file="SyntaxHighlighterParser.java";
            String file="ChartComputator.java";
            InputStream inputStream=getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            long startTime = System.currentTimeMillis();
            mHighlightView.setContent(new String(buffer));
            mHighlightView.setTheme(new DefaultTheme(this));
            mHighlightView.render();
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            Log.e("totalTime " , ""+totalTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
