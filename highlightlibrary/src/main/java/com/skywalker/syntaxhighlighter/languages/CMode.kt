package com.skywalker.syntaxhighlighter.languages

import com.skywalker.syntaxhighlighter.languages.common.CommonRegexRule
import com.skywalker.syntaxhighlighter.languages.common.Mode
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule
import java.util.regex.Pattern
import com.skywalker.syntaxhighlighter.languages.common.RegexPairRule



class CMode:Mode() {

    override fun setPattern() {
        regexRuleList.add(RegExpRule(KEY_TYPE, Pattern.compile("\\b(bool|float|double|char|int|short|long|sizeof|enum|void|static|const|struct|union|typedef|extern|(un)?signed|inline)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_TYPE,Pattern.compile("\\b((s?size)|((u_?)?int(8|16|32|64|ptr)))_t\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(typename|mutable|volatile|register|explicit)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(for|while|do|if|else|case|default|switch)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(try|throw|catch|operator|delete)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(goto|continue|break|return)\\b")))
        regexRuleList.add(RegExpRule(Mode.KEY_PREPROC,Pattern.compile("^[\\p{javaWhitespace}]*#[\\p{javaWhitespace}]*(define|pragma|include|(un|ifn?)def|endif|el(if|se)|if|warning|error)",Pattern.MULTILINE)))
        regexRuleList.add(CommonRegexRule.NUMBER_RULE)

        regexPairRuleList.add(CommonRegexRule.QUOTE_STRING_RULE)
        regexPairRuleList.add(CommonRegexRule.APOS_STRING_RULE)
        regexPairRuleList.add(CommonRegexRule.C_LINE_COMMENT_RULE)
        regexPairRuleList.add(CommonRegexRule.C_BLOCK_COMMENT_RULE)
        val pairRule = RegexPairRule(Mode.KEY_PREPROC, Pattern.compile("#[\\p{javaWhitespace}]*"), Pattern.compile("(define|pragma|include|(un|ifn?)def|endif|el(if|se)|if|warning|error)"))
        regexPairRuleList.add(pairRule)
    }

    override fun setToken() {
        token= Pattern.compile(Mode.combinePatterns("\"","'","//","/\\*","#"))
    }
}