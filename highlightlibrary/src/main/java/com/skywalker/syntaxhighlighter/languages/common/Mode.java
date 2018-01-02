package com.skywalker.syntaxhighlighter.languages.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Mode {

    public static final int KEY_TEXT = 0;
    public static final int KEY_BACKGROUND = 1;
    public static final int KEY_LINE_BAR = 2;
    public static final int KEY_LINE_NUMBER = 3;

    public static final int KEY_STRING = 11;
    public static final int KEY_COMMENT = 12;
    public static final int KEY_TYPE = 13;
    public static final int KEY_NUMBER = 14;
    public static final int KEY_STATEMENT = 15;
    public static final int KEY_CONSTANT = 16;
    public static final int KEY_SYMBOL = 17;
    public static final int KEY_PREPROC=18;

    protected String mToken;

    protected List<RegExpRule> mRegExpRuleList = new ArrayList<>();
    protected List<RegexPairRule> mRegexPairList = new ArrayList<>();
    public Mode(){
        init();
        setToken();
    }
    public  abstract Pattern getExtension();
    public abstract void init();
    public abstract void setToken();
    public Pattern getToken() {
        return Pattern.compile(mToken);
    }


    public List<RegExpRule> getRegExpRuleList() {
        return mRegExpRuleList;
    }

    public List<RegexPairRule> getRegexPairList() {
        return mRegexPairList;
    }

}
