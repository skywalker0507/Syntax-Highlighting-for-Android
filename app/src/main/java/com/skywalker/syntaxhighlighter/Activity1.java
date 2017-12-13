package com.skywalker.syntaxhighlighter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/11               *
 *******************************/

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        String file = "highlight.js";
        TextView textView = findViewById(R.id.test);
        try {
            InputStream inputStream = null;
            inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String s=new String(buffer);
            String content=s.replaceAll("\r","");
            SpannableString spannableString = new SpannableString(content);

            List<Integer> indexs=new ArrayList<>();
            char key='\n';
            for (Integer index = content.indexOf(key);
                 index >= 0;
                 index = content.indexOf(key, index + 1))
            {
                indexs.add(index);
            }
            System.out.println(indexs.size());

            int startIndex=0;
            for (int i=0;i<indexs.size();i++){
                spannableString.setSpan(new NumberSpan(52, i, Color.RED), startIndex, indexs.get(i), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex=indexs.get(i)+1;
            }

            textView.setText(spannableString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
