package com.skywalker;

import com.skywalker.languages.common.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


public class Parser {
    private Mode mLanguage;
    private List<RegexMatchResult> mMatchResults = new ArrayList<>();

    public Parser(Mode language) {
        this.mLanguage = language;
    }

    public void Pase(String content) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();
        Scanner scanner = new Scanner(content);
        while (scanner.findWithinHorizon(mLanguage.getToken(), 0) != null) {
            for (int i = 1; i <= scanner.match().groupCount(); i++) {
                if (scanner.match().start(i) != -1) {
                    RegexPairRule pair = mLanguage.getRegexPairList().get(i - 1);
                    RegexMatchResult result = new RegexMatchResult(pair.getKey());
                    result.setStart(scanner.match().start() + scanner.getBase());
                    if (scanner.findWithinHorizon(pair.getEnd(), 0) != null) {
                        System.out.println(pair.getKey());
                        result.setEnd(scanner.match().end() + scanner.getBase());
                        mMatchResults.add(result);
                        break;
                    }

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
                    index = r.getEnd() + 1;
                    continue;
                }
                substring = content.substring(index, r.getStart());
                index = r.getEnd() + 1;
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
        /*for (RegexMatchResult r : mMatchResults) {
            String substring = content.substring(index, r.getStart());
            for (RegExpRule rule : mLanguage.getRegExpRuleList()) {
                Matcher matcher = rule.getPattern().matcher(substring);
                while (matcher.find()) {
                    RegexMatchResult result = new RegexMatchResult(rule.getKey());
                    result.setStart(index+matcher.start());
                    result.setEnd(index+matcher.end());
                    list.add(result);
                }
            }
            index = r.getEnd()+1;
        }*/

        mMatchResults.addAll(list);

        System.out.println(list.size());
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("totalTime " + totalTime);


        /*startTime = System.currentTimeMillis();
        for (RegExpRule rule : mLanguage.getRegExpRuleList()) {
            RegexMatchResult result = new RegexMatchResult(rule.getKey());
            Matcher matcher = rule.getPattern().matcher(content);
            while (matcher.find()) {
                result.setStart(matcher.start());
                result.setEnd(matcher.end());
                mPairMatchResults.add(result);
            }
        }
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println(totalTime);*/
    }

    public List<RegexMatchResult> getMatchResults() {
        return mMatchResults;
    }
}
