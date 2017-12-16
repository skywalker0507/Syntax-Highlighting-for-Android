package com.skywalker.syntaxhighlighter.languages.common;

import java.util.regex.Pattern;

public class RegexPairRule {
    private Pattern mStart;
    private Pattern mEnd;
    private Pattern mSkip=null;
    private Integer mKey;

    public RegexPairRule(Integer key, Pattern start, Pattern end) {
        this.mKey = key;
        this.mStart = start;
        this.mEnd=end;
    }

    public RegexPairRule(Integer key, Pattern start,Pattern skip, Pattern end) {

        this.mKey=key;
        this.mStart=start;
        this.mSkip=skip;
        this.mEnd =Pattern.compile(Utils.combinePatterns(skip.pattern(),end.pattern()));

    }


    public Integer getKey() {
        return mKey;
    }

    public Pattern getStart() {
        return mStart;
    }

    public Pattern getEndPattern() {

        return mEnd;
    }

    public Pattern getSkip() {
        return mSkip;
    }
}
