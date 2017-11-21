package com.skywalker.syntaxhighlighter.languages.common;

import java.util.regex.Pattern;

public class RegExpRule {
    private Integer mKey;
    private Pattern mPattern;

    public RegExpRule(Integer key, Pattern pattern) {
        this.mKey = key;
        this.mPattern = pattern;
    }

    public Integer getKey() {
        return mKey;
    }

    public Pattern getPattern() {
        return mPattern;
    }
}
