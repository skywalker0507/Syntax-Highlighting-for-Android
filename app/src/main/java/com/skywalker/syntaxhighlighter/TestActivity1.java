package com.skywalker.syntaxhighlighter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/21               *
 *******************************/

public class TestActivity1 extends AppCompatActivity {

    private HighlightLayout mLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mLayout=findViewById(R.id.HighlightLayout);
        String file="ChartComputator.java";

        try {
            InputStream inputStream=getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            mLayout.setText(new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
