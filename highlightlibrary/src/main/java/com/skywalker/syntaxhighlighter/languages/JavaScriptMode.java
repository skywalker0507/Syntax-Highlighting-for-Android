package com.skywalker.syntaxhighlighter.languages;

import com.skywalker.syntaxhighlighter.languages.common.CommonMode;
import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule;

import java.util.regex.Pattern;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class JavaScriptMode extends Mode{
    public JavaScriptMode(){
        mRegExpRuleList.add(
                new RegExpRule(Mode.KEY_NUMBER, Pattern.compile("\\b[-+]?([1-9][0-9]*|0[0-7]*|0x[0-9a-fA-F]+)([uU][lL]?|[lL][uU]?)?\\b")));
        mRegExpRuleList.add(
                new RegExpRule(Mode.KEY_NUMBER,Pattern.compile("\\b[-+]?([0-9]+\\.[0-9]*|[0-9]*\\.[0-9]+)([EePp][+-]?[0-9]+)?[fFlL]?")));
        mRegExpRuleList.add(
                new RegExpRule(Mode.KEY_NUMBER,Pattern.compile("\\b[-+]?([0-9]+[EePp][+-]?[0-9]+)[fFlL]?")));
        mRegExpRuleList.add(new RegExpRule(KEY_STATEMENT,Pattern.compile("\\b(break|case|catch|continue|default|delete|do|else|finally)\\b")));
        mRegExpRuleList.add(new RegExpRule(KEY_STATEMENT,Pattern.compile("\\b(for|function|class|extends|get|if|in|instanceof|new|return|set|switch)\\b")));
        mRegExpRuleList.add(new RegExpRule(KEY_STATEMENT,Pattern.compile("\\b(switch|this|throw|try|typeof|var|const|let|void|while|with)\\b")));
        mRegExpRuleList.add(new RegExpRule(KEY_CONSTANT,Pattern.compile("\\b(null|undefined|NaN)\\b")));
        mRegExpRuleList.add(new RegExpRule(KEY_CONSTANT,Pattern.compile("\\b(true|false)\\b")));

        mRegexPairList.add(CommonMode.QUOTE_STRING_RULE);
        mRegexPairList.add(CommonMode.APOS_STRING_RULE);
        mRegexPairList.add(CommonMode.C_LINE_COMMENT_RULE);
        mRegexPairList.add(CommonMode.C_BLOCK_COMMENT_RULE);

        mToken=combinePatterns("\"","'","//","/\\*");


    }
}
