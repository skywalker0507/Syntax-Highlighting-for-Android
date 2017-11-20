package com.skywalker.languages.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Mode {
    public static final String KEY_STRING = "string";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_KEYWORDS = "keywords";
    public static final String KEY_TYPES = "type";
    public static final String KEY_OPERATORS = "operators";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_STATEMENT = "statement";
    public static final String KEY_CONSTANT = "constant";
    protected String mToken;
    protected List<RegExpRule> mRegExpRuleList = new ArrayList<>();
    protected List<RegexPairRule> mRegexPairList = new ArrayList<>();

    public Pattern getToken() {
        return Pattern.compile(mToken);
    }

    public void setToken(String token) {
        mToken = token;
    }

    public List<RegExpRule> getRegExpRuleList() {
        return mRegExpRuleList;
    }

    public List<RegexPairRule> getRegexPairList() {
        return mRegexPairList;
    }
}
