package com.skywalker.languages.common;

public class RegexMatchResult {
    private String mKey;
    private int mStart;
    private int mEnd;

    public RegexMatchResult(String key){
        this.mKey=key;
    }

    public void setStart(int start) {
        mStart = start;
    }

    public void setEnd(int end) {
        mEnd = end;
    }

    public String getKey() {
        return mKey;
    }

    public int getStart() {
        return mStart;
    }

    public int getEnd() {
        return mEnd;
    }
}
