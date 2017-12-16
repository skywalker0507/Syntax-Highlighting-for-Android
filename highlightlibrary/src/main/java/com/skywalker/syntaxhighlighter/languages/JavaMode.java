package com.skywalker.syntaxhighlighter.languages;

import com.skywalker.syntaxhighlighter.languages.common.CommonRegexRule;
import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule;
import com.skywalker.syntaxhighlighter.languages.common.Utils;

import java.util.regex.Pattern;

public class JavaMode extends Mode {
    private final static String type1 = "\\b(boolean|byte|char|double|float|int|long|new|short|this|transient|void)\\b";
    private final static String statement = "\\b(break|case|catch|continue|default|do|else|finally|for|if|return|switch|throw|try|while)\\b";
    private final static String type2 = "\\b(abstract|class|extends|final|implements|import|instanceof|interface|native|package|private|protected|public|static|strictfp|super|synchronized|throws|volatile)\\b";
    private final static String constant = "\\b(true|false|null)\\b";



    public JavaMode() {

        mRegExpRuleList.add(new RegExpRule(Mode.KEY_TYPE, Pattern.compile(type1)));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_TYPE, Pattern.compile(type2)));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_STATEMENT, Pattern.compile(statement)));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_CONSTANT, Pattern.compile(constant)));
        mRegExpRuleList.add(CommonRegexRule.NUMBER_RULE);

        mRegexPairList.add(CommonRegexRule.QUOTE_STRING_RULE);
        mRegexPairList.add(CommonRegexRule.APOS_STRING_RULE);
        mRegexPairList.add(CommonRegexRule.C_LINE_COMMENT_RULE);
        mRegexPairList.add(CommonRegexRule.C_BLOCK_COMMENT_RULE);

        mToken= Utils.combinePatterns("\"","'","//","/\\*");

    }

    @Override
    public Pattern getExtension() {
        return Pattern.compile("java");
    }
}
