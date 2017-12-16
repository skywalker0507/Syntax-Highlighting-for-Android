package com.skywalker.syntaxhighlighter;


import android.util.Log;

import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule;
import com.skywalker.syntaxhighlighter.languages.common.RegexMatchResult;
import com.skywalker.syntaxhighlighter.languages.common.RegexPairRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


public class Parser {

    private Mode mLanguage;
    private List<RegexMatchResult> mMatchResults = new ArrayList<>();

    public Parser(Mode language) {
        this.mLanguage = language;
    }

    public void parse(String content) {

        long startTime = System.currentTimeMillis();
        SyntaxScanner scanner = new SyntaxScanner(content);
        while (scanner.findWithinHorizon(mLanguage.getToken(), 0) != null) {
            for (int i = 1; i <= scanner.match().groupCount(); i++) {
                if (scanner.match().start(i) != -1) {
                    RegexPairRule pair = mLanguage.getRegexPairList().get(i - 1);
                    RegexMatchResult result = new RegexMatchResult(pair.getKey());
                    result.setStart(scanner.match().start() + scanner.getBase());
                    if (pair.getSkip() != null) {
                        while (scanner.findWithinHorizon(pair.getEndPattern(), 0) != null) {
                            if (scanner.match().start(2) != -1) {
                                result.setEnd(scanner.match().end(2) + scanner.getBase());
                                mMatchResults.add(result);
                                break;
                            } /*else if (scanner.match().start(1) != -1) {
                                //skip
                            }*/
                        }
                    } else {
                        if (scanner.findWithinHorizon(pair.getEndPattern(), 0) != null) {
                            System.out.println(pair.getKey());
                            result.setEnd(scanner.match().end() + scanner.getBase());
                            mMatchResults.add(result);
                        }
                    }


                    /*if (scanner.findWithinHorizon(pair.getEndPattern(), 0) != null) {
                        System.out.println(pair.getKey());
                        result.setEnd(scanner.match().end() + scanner.getBase());
                        mMatchResults.add(result);
                    }*/
                    break;
                }
            }
        }

        List<RegexMatchResult> list = new ArrayList<>(mMatchResults.size());
        int index = 0;
        for (int i = 0; i <= mMatchResults.size(); i++) {
            String substring;
            int start = index;
            if (i == mMatchResults.size()) {
                substring = content.substring(index, content.length());
            } else {
                RegexMatchResult r = mMatchResults.get(i);
                if (r.getStart() == index) {
                    index = r.getEnd();
                    continue;
                }

                substring = content.substring(index, r.getStart());
                index = r.getEnd();

            }

            for (RegExpRule rule : mLanguage.getRegExpRuleList()) {
                Matcher matcher = rule.getPattern().matcher(substring);
                while (matcher.find()) {
                    RegexMatchResult result = new RegexMatchResult(rule.getKey());
                    result.setStart(start + matcher.start());
                    result.setEnd(start + matcher.end());
                    list.add(result);
                }
            }


        }

        mMatchResults.addAll(list);

        System.out.println(list.size());
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        Log.e("totalTime " ,""+ totalTime);

    }

    public List<RegexMatchResult> getMatchResults() {
        return mMatchResults;
    }
}
