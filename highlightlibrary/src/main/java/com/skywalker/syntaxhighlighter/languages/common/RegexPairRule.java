package com.skywalker.syntaxhighlighter.languages.common;

import java.util.regex.Pattern;

public class RegexPairRule {
    private Pattern mStart;
    private Pattern mEnd;
    private Pattern mSkip;
    private Integer mKey;

    public RegexPairRule(Integer key, Pattern start, Pattern end) {
        this.mKey = key;
        this.mStart = start;
        this.mEnd = end;
    }

    public Integer getKey() {
        return mKey;
    }

    public Pattern getStart() {
        return mStart;
    }

    public Pattern getEnd() {
        return mEnd;
    }

    public Pattern getSkip() {
        return mSkip;
    }
}
