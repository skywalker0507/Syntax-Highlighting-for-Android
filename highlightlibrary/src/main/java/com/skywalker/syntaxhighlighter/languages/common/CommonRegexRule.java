package com.skywalker.syntaxhighlighter.languages.common;

import java.util.regex.Pattern;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/6               *
 *******************************/

public class CommonRegexRule {

    private final static String number = "\\b" +
            "(" +
            "0[bB]([01]+[01_]+[01]+|[01]+)" + // 0b...
            "|" +
            "0[xX]([a-fA-F0-9]+[a-fA-F0-9_]+[a-fA-F0-9]+|[a-fA-F0-9]+)" + // 0x...
            "|" +
            "(" +
            "([\\d]+[\\d_]+[\\d]+|[\\d]+)(\\.([\\d]+[\\d_]+[\\d]+|[\\d]+))?" +
            "|" +
            "\\.([\\d]+[\\d_]+[\\d]+|[\\d]+)" +
            ")" +
            "([eE][-+]?\\d+)?" + // octal, decimal, float
            ")" +
            "[lLfF]?";
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
            Pattern.compile("\\\\."),
            Pattern.compile("'"));

    public static RegexPairRule QUOTE_STRING_RULE=new RegexPairRule(
            Mode.KEY_STRING,
            Pattern.compile("\""),
            Pattern.compile("\\\\."),
            Pattern.compile("\""));

    public static RegExpRule NUMBER_RULE=new RegExpRule(Mode.KEY_NUMBER,Pattern.compile(number));


}
