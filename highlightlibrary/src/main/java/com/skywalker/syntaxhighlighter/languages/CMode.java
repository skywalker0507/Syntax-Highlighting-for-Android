package com.skywalker.syntaxhighlighter.languages;

import com.skywalker.syntaxhighlighter.languages.common.CommonRegexRule;
import com.skywalker.syntaxhighlighter.languages.common.Mode;
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule;
import com.skywalker.syntaxhighlighter.languages.common.RegexPairRule;
import com.skywalker.syntaxhighlighter.languages.common.Utils;

import java.util.regex.Pattern;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/11/20               *
 *******************************/

public class CMode extends Mode{

    @Override
    public Pattern getExtension() {
        return Pattern.compile("(c|h)");
    }

    @Override
    public void init() {
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_TYPE, Pattern.compile("\\b(bool|float|double|char|int|short|long|sizeof|enum|void|static|const|struct|union|typedef|extern|(un)?signed|inline)\\b")));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_TYPE,Pattern.compile("\\b((s?size)|((u_?)?int(8|16|32|64|ptr)))_t\\b")));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(typename|mutable|volatile|register|explicit)\\b")));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(for|while|do|if|else|case|default|switch)\\b")));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(try|throw|catch|operator|delete)\\b")));
        mRegExpRuleList.add(new RegExpRule(Mode.KEY_STATEMENT,Pattern.compile("\\b(goto|continue|break|return)\\b")));
        //mRegExpRuleList.add(new RegExpRule(Mode.KEY_PREPROC,Pattern.compile("^[\\p{javaWhitespace}]*#[\\p{javaWhitespace}]*(define|pragma|include|(un|ifn?)def|endif|el(if|se)|if|warning|error)",Pattern.MULTILINE)));
        mRegExpRuleList.add(CommonRegexRule.NUMBER_RULE);

        mRegexPairList.add(CommonRegexRule.QUOTE_STRING_RULE);
        mRegexPairList.add(CommonRegexRule.APOS_STRING_RULE);
        mRegexPairList.add(CommonRegexRule.C_LINE_COMMENT_RULE);
        mRegexPairList.add(CommonRegexRule.C_BLOCK_COMMENT_RULE);
        RegexPairRule pairRule=new RegexPairRule(Mode.KEY_PREPROC,Pattern.compile("#[\\p{javaWhitespace}]*"),Pattern.compile("(define|pragma|include|(un|ifn?)def|endif|el(if|se)|if|warning|error)"));
        mRegexPairList.add(pairRule);

    }

    @Override
    public void setToken() {
        mToken= Utils.combinePatterns("\"","'","//","/\\*","#");
    }
}
