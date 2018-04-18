package com.skywalker.syntaxhighlighter.languages

import com.skywalker.syntaxhighlighter.languages.common.CommonRegexRule
import com.skywalker.syntaxhighlighter.languages.common.Mode
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule

import java.util.regex.Pattern

class JavaMode : Mode() {

    companion object {
        private const val type1 = "\\b(boolean|byte|char|double|float|int|long|new|short|this|transient|void)\\b"
        private const val statement = "\\b(break|case|catch|continue|default|do|else|finally|for|if|return|switch|throw|try|while)\\b"
        private const val type2 = "\\b(abstract|class|extends|final|implements|import|instanceof|interface|native|package|private|protected|public|static|strictfp|super|synchronized|throws|volatile)\\b"
        private const val constant = "\\b(true|false|null)\\b"
    }

    override fun setPattern()
    {
        regexRuleList.add(RegExpRule(Mode.KEY_TYPE, Pattern.compile(type1)))
        regexRuleList.add(RegExpRule(Mode.KEY_TYPE, Pattern.compile(type2)))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT, Pattern.compile(statement)))
        regexRuleList.add(RegExpRule(Mode.KEY_CONSTANT, Pattern.compile(constant)))
        regexRuleList.add(CommonRegexRule.NUMBER_RULE)

        regexPairRuleList.add(CommonRegexRule.QUOTE_STRING_RULE)
        regexPairRuleList.add(CommonRegexRule.APOS_STRING_RULE)
        regexPairRuleList.add(CommonRegexRule.C_LINE_COMMENT_RULE)
        regexPairRuleList.add(CommonRegexRule.C_BLOCK_COMMENT_RULE)
    }

    override fun setToken() {
        token = Pattern.compile(Mode.combinePatterns("\"", "'", "//", "/\\*"))
    }
}
