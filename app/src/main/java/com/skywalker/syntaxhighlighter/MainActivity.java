package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;

import com.skywalker.syntaxhighlighter.themes.DefaultTheme;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private HighlightView mHighlightView1;
    private HighlightView mHighlightView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //String file="SyntaxHighlighterParser.java";
            String file = "ChartComputator.java";
            InputStream inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            //
            mHighlightView1 = findViewById(R.id.HighlightView1);
            mHighlightView1.setMovementMethod(ScrollingMovementMethod.getInstance());
            HighlightView.Builder builder1 = new HighlightView.Builder()
                    .enableEdit(false)
                    .setTheme(new DefaultTheme(this))
                    .showLineNumber(true)
                    .enableZoom(false)
                    .textWrapping(false);
            mHighlightView1.setHighlightBuilder(builder1);
            mHighlightView1.setContent(new String(buffer));
            mHighlightView1.render();


            mHighlightView2 = findViewById(R.id.HighlightView2);
            mHighlightView2.setMovementMethod(ScrollingMovementMethod.getInstance());
            HighlightView.Builder builder = new HighlightView.Builder()
                    .enableEdit(false)
                    .setTheme(new DefaultTheme(this))
                    .showLineNumber(true)
                    .enableZoom(false)
                    .textWrapping(false);
            mHighlightView2.setHighlightBuilder(builder);
            mHighlightView2.setContent(new String(buffer));
            mHighlightView2.render();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
