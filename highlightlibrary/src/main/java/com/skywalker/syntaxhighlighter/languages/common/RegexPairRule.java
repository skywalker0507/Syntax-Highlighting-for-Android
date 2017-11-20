package com.skywalker.languages.common;

import java.util.regex.Pattern;

public class RegexPairRule {
    private Pattern mStart;
    private Pattern mEnd;
    private String mKey;

    public RegexPairRule(String key, Pattern start, Pattern end){
        this.mKey=key;
        this.mStart=start;
        this.mEnd=end;
    }

    public String getKey() {
        return mKey;
    }

    public Pattern getStart() {
        return mStart;
    }

    public Pattern getEnd() {
        return mEnd;
    }
}
