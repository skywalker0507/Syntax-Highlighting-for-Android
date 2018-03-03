package com.skywalker.syntaxhighlighter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.skywalker.syntaxhighlighter.HighlightView;
import com.skywalker.syntaxhighlighter.languages.CMode;
import com.skywalker.syntaxhighlighter.languages.JavaMode;
import com.skywalker.syntaxhighlighter.languages.JavaScriptMode;
import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.themes.AtomDarkTheme;
import com.skywalker.syntaxhighlighter.themes.DefaultTheme;
import com.skywalker.syntaxhighlighter.themes.Theme;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private HighlightView mHighlightView;
    private String[] mLanguages = {"c.c", "java.java", "javascript.js"};
    private Theme[] mThemes = new Theme[2];
    private Theme mTheme;

    private String[] mCodes = new String[3];
    private String mCode;

    private Mode[] mModes = {new CMode(), new JavaMode(), new JavaScriptMode()};
    private Mode mMode;

    private CheckBox mLinenumber;
    private CheckBox mZoom;
    private CheckBox mWrapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThemes[0] = new DefaultTheme(this);
        mThemes[1] = new AtomDarkTheme(this);

        for (int i = 0; i < mLanguages.length; i++) {
            try {
                InputStream inputStream = getAssets().open(mLanguages[i]);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                mCodes[i] = new String(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Spinner languages = (Spinner) findViewById(R.id.languagesSpinner);
        Spinner themes = (Spinner) findViewById(R.id.themesSpinner);

        languages.setSelection(0);
        themes.setSelection(0);
        mLinenumber = (CheckBox) findViewById(R.id.checkbox_linenumber);
        mZoom = (CheckBox) findViewById(R.id.checkbox_zoom);
        mWrapping = (CheckBox) findViewById(R.id.checkbox_wrapping);

        mLinenumber.setChecked(true);
        mZoom.setChecked(true);
        mWrapping.setChecked(false);

        mHighlightView = findViewById(R.id.HighlightView);
        mHighlightView.setEditable(false);
        mHighlightView.setShowLineNumber(true);
        mHighlightView.setWrapping(false);

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMode = mModes[position];
                mCode = mCodes[position];

                try {
                    mHighlightView.setContent(mCode);
                    mHighlightView.render(mTheme, mMode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        themes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTheme = mThemes[position];
                mHighlightView.render(mTheme, mMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mLinenumber.setOnCheckedChangeListener(this);
        mWrapping.setOnCheckedChangeListener(this);
        mZoom.setOnCheckedChangeListener(this);
        /*try {
            //String file="SyntaxHighlighterParser.java";
            String file = "java.java";
            //String file="javascript.js";
            InputStream inputStream = getAssets().open(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            //

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


            Button button = findViewById(R.id.button_render);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox_linenumber:
                mHighlightView.setShowLineNumber(isChecked);
                break;
            case R.id.checkbox_wrapping:
                mHighlightView.setWrapping(isChecked);
                break;
            case R.id.checkbox_zoom:
                mHighlightView.setZoom(isChecked);
                break;
                default:
                    break;

        }

        mHighlightView.render(mMode);
    }
}
