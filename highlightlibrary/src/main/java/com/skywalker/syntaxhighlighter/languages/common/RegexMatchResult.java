package com.skywalker.syntaxhighlighter.languages.common;

public class RegexMatchResult {
    private Integer mKey;
    private int mStart;
    private int mEnd;

    public RegexMatchResult(Integer key) {
        this.mKey = key;
    }

    public void setStart(int start) {
        mStart = start;
    }

    public void setEnd(int end) {
        mEnd = end;
    }

    public Integer getKey() {
        return mKey;
    }

    public int getStart() {
        return mStart;
    }

    public int getEnd() {
        return mEnd;
    }
}
