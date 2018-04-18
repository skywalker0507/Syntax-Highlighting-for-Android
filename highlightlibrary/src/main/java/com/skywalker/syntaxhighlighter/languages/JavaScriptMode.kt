package com.skywalker.syntaxhighlighter.languages

import com.skywalker.syntaxhighlighter.languages.common.CommonRegexRule
import com.skywalker.syntaxhighlighter.languages.common.Mode
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule

import java.util.regex.Pattern

/*******************************
 * Created by liuqiang          *
 *
 * data: 2017/11/20               *
 */

class JavaScriptMode : Mode() {

    override fun setPattern() {
        regexRuleList.add(CommonRegexRule.NUMBER_RULE)
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT, Pattern.compile("\\b(break|case|catch|continue|default|delete|do|else|finally)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT, Pattern.compile("\\b(for|function|class|extends|get|if|in|instanceof|new|return|set|switch)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT, Pattern.compile("\\b(switch|this|throw|try|typeof|var|const|let|void|while|with)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_CONSTANT, Pattern.compile("\\b(null|undefined|NaN)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_CONSTANT, Pattern.compile("\\b(true|false)\\b")))

        regexPairRuleList.add(CommonRegexRule.QUOTE_STRING_RULE)
        regexPairRuleList.add(CommonRegexRule.APOS_STRING_RULE)
        regexPairRuleList.add(CommonRegexRule.C_LINE_COMMENT_RULE)
        regexPairRuleList.add(CommonRegexRule.C_BLOCK_COMMENT_RULE)
    }

    override fun setToken() {
        token = Pattern.compile(Mode.combinePatterns("\"", "'", "//", "/\\*"))
    }
}
