package com.skywalker.syntaxhighlighter.languages.common

import java.util.regex.Pattern

abstract class Mode {
    companion object {
        val KEY_TEXT = 0
        val KEY_BACKGROUND = 1
        val KEY_LINE_BAR = 2
        val KEY_LINE_NUMBER = 3

        val KEY_STRING = 11
        val KEY_COMMENT = 12
        val KEY_TYPE = 13
        val KEY_NUMBER = 14
        val KEY_STATEMENT = 15
        val KEY_CONSTANT = 16
        val KEY_SYMBOL = 17
        val KEY_PREPROC = 18

        fun combinePatterns(vararg patterns: String):String{

            val builder=StringBuilder()
            for (i in 0..patterns.size) {
                builder.append("(").append(patterns[i])
                if (i == patterns.size - 1) {
                    builder.append(")")
                } else {
                    builder.append(")|")
                }

            }

            return builder.toString()
        }
    }

    lateinit var token:Pattern
     val regexRuleList= mutableListOf<RegExpRule>()

     val regexPairRuleList= mutableListOf<RegexPairRule>()

    init {
        this.setPattern()
        this.setToken()
    }

    abstract fun setPattern()
    abstract fun setToken()

}