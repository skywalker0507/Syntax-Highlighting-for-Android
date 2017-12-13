package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.skywalker.syntaxhighlighter.languages.JavaMode;
import com.skywalker.syntaxhighlighter.themes.DefaultTheme;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private HighlightView mHighlightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //String file="SyntaxHighlighterParser.java";
            String file = "ChartComputator.java";
            //String file="highlight.js";
            InputStream inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            //
            mHighlightView = findViewById(R.id.HighlightView);
            mHighlightView.setContent(new String(buffer));
            mHighlightView.setMovementMethod(ScrollingMovementMethod.getInstance());
            HighlightView.Builder builder = new HighlightView.Builder()
                    .enableEdit(false)
                    .setTheme(new DefaultTheme(this))
                    .showLineNumber(true)
                    .enableZoom(false)
                    .textWrapping(true);
            mHighlightView.setHighlightBuilder(builder);
            mHighlightView.render(new JavaMode());

            final CheckBox linenumber=findViewById(R.id.checkbox_linenumber);
            final CheckBox zoom=findViewById(R.id.checkbox_zoom);
            final CheckBox wrapping=findViewById(R.id.checkbox_wrapping);
            Button button=findViewById(R.id.button_render);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HighlightView.Builder builder = new HighlightView.Builder()
                            .enableEdit(false)
                            .setTheme(new DefaultTheme(MainActivity.this))
                            .showLineNumber(linenumber.isChecked())
                            .enableZoom(zoom.isChecked())
                            .textWrapping(wrapping.isChecked());
                    mHighlightView.setHighlightBuilder(builder);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
