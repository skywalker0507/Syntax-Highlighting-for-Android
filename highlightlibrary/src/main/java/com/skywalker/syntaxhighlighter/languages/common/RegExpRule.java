package com.skywalker.languages.common;

import java.util.regex.Pattern;

public class RegExpRule {
    private String mKey;
    private Pattern mPattern;

    public RegExpRule(String key,Pattern pattern){
        this.mKey=key;
        this.mPattern=pattern;
    }

    public String getKey() {
        return mKey;
    }

    public Pattern getPattern() {
        return mPattern;
    }
}
