package com.skywalker.syntaxhighlighter.languages.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Mode {

    public static final int KEY_TEXT=0;
    public static final int KEY_BACKGROUND=1;

    public static final int KEY_STRING = 11;
    public static final int KEY_COMMENT = 12;
    public static final int KEY_TYPE = 13;
    public static final int KEY_NUMBER = 14;
    public static final int KEY_STATEMENT = 15;
    public static final int KEY_CONSTANT = 16;

    protected String mToken;
    protected String mSymbol;
    protected List<RegExpRule> mRegExpRuleList = new ArrayList<>();
    protected List<RegexPairRule> mRegexPairList = new ArrayList<>();

    public Pattern getToken() {
        return Pattern.compile(mToken);
    }

    public String getSymbol() {
        return mSymbol;
    }

    public List<RegExpRule> getRegExpRuleList() {
        return mRegExpRuleList;
    }

    public List<RegexPairRule> getRegexPairList() {
        return mRegexPairList;
    }
}
