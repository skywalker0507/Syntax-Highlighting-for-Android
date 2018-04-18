package com.skywalker.syntaxhighlighter.languages.common

import java.util.regex.Pattern

data class RegexPairRule(val key:Int,val startPattern:Pattern,val skip:Pattern?=null,val endPattern:Pattern) {
    constructor(key:Int,start:Pattern,end:Pattern):this(key,start,null,end)
}