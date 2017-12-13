package com.skywalker.syntaxhighlighter.languages.common;

import java.util.regex.Pattern;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/6               *
 *******************************/

public class CommonMode {
    public static RegexPairRule C_LINE_COMMENT_RULE = new RegexPairRule(
            Mode.KEY_COMMENT,
            Pattern.compile("//"),
            Pattern.compile("$", Pattern.MULTILINE));

    public static RegexPairRule C_BLOCK_COMMENT_RULE = new RegexPairRule(
            Mode.KEY_COMMENT,
            Pattern.compile("/\\*"),
            Pattern.compile("\\*/"));

    public static RegexPairRule APOS_STRING_RULE=new RegexPairRule(
            Mode.KEY_STRING,
            Pattern.compile("'"),
            Pattern.compile("'"));

    public static RegexPairRule QUOTE_STRING_RULE=new RegexPairRule(
            Mode.KEY_STRING,
            Pattern.compile("\""),
            Pattern.compile("\""));

}
