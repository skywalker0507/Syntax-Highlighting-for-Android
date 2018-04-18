package com.skywalker.syntaxhighlighter.languages.common

import java.util.regex.Pattern

class CommonRegexRule {


    companion object {
        private val number = "\\b" +
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
                "[lLfF]?"

        var C_LINE_COMMENT_RULE = RegexPairRule(
                Mode.KEY_COMMENT,
                Pattern.compile("//"),
                Pattern.compile("$", Pattern.MULTILINE))

        var C_BLOCK_COMMENT_RULE = RegexPairRule(
                Mode.KEY_COMMENT,
                Pattern.compile("/\\*"),
                Pattern.compile("\\*/"))

        var APOS_STRING_RULE = RegexPairRule(
                Mode.KEY_STRING,
                Pattern.compile("'"),
                Pattern.compile("\\\\."),
                Pattern.compile("'"))

        var QUOTE_STRING_RULE = RegexPairRule(
                Mode.KEY_STRING,
                Pattern.compile("\""),
                Pattern.compile("\\\\."),
                Pattern.compile("\""))

        var NUMBER_RULE = RegExpRule(Mode.KEY_NUMBER, Pattern.compile(number))
    }

}