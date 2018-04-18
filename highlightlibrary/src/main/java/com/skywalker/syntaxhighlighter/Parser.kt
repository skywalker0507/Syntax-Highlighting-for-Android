package com.skywalker.syntaxhighlighter


import android.util.Log

import com.skywalker.syntaxhighlighter.languages.common.Mode
import com.skywalker.syntaxhighlighter.languages.common.RegExpRule
import com.skywalker.syntaxhighlighter.languages.common.RegexMatchResult
import com.skywalker.syntaxhighlighter.languages.common.RegexPairRule

import java.util.ArrayList
import java.util.regex.Matcher


class Parser(private val mLanguage: Mode) {
    private val mMatchResults = ArrayList<RegexMatchResult>()

    val matchResults: List<RegexMatchResult>
        get() = mMatchResults

    fun parse(content: String) {

        val startTime = System.currentTimeMillis()
        if (mLanguage.token != null) {
            val scanner = SyntaxScanner(content)
            while (scanner.findWithinHorizon(mLanguage.token, 0) != null) {
                for (i in 1..scanner.match().groupCount()) {
                    if (scanner.match().start(i) != -1) {
                        val (key, _, skip, endPattern) = mLanguage.regexPairRuleList[i - 1]
                        val result = RegexMatchResult(key)
                        result.start = scanner.match().start() + scanner.base
                        if (skip != null) {
                            while (scanner.findWithinHorizon(endPattern, 0) != null) {
                                if (scanner.match().start(2) != -1) {
                                    result.end = scanner.match().end(2) + scanner.base
                                    mMatchResults.add(result)
                                    break
                                } /*else if (scanner.match().start(1) != -1) {
                                //skip
                            }*/
                            }
                        } else {
                            if (scanner.findWithinHorizon(endPattern, 0) != null) {
                                println(key)
                                result.end = scanner.match().end() + scanner.base
                                mMatchResults.add(result)
                            }
                        }


                        /*if (scanner.findWithinHorizon(pair.getEndPattern(), 0) != null) {
                        System.out.println(pair.getKey());
                        result.setEnd(scanner.match().end() + scanner.getBase());
                        mMatchResults.add(result);
                    }*/
                        break
                    }
                }
            }

            val list = ArrayList<RegexMatchResult>(mMatchResults.size)
            var index = 0
            for (i in 0..mMatchResults.size) {
                val substring: String
                val start = index
                if (i == mMatchResults.size) {
                    substring = content.substring(index, content.length)
                } else {
                    val r = mMatchResults[i]
                    if (r.start == index) {
                        index = r.end
                        continue
                    }

                    substring = content.substring(index, r.start)
                    index = r.end

                }

                for (rule in mLanguage.regexRuleList) {
                    findMatchResults(substring, rule, start, list)
                }


            }

            mMatchResults.addAll(list)

        } else {
            for (rule in mLanguage.regexRuleList) {
                findMatchResults(content, rule, 0, mMatchResults)
            }
        }

        println(mMatchResults.size)
        val endTime = System.currentTimeMillis()
        val totalTime = endTime - startTime
        Log.e("totalTime ", "" + totalTime)

    }

    private fun findMatchResults(s: String, rule: RegExpRule, offset: Int, list: MutableList<RegexMatchResult>) {
        val matcher = rule.pattern.matcher(s)
        while (matcher.find()) {
            val result = RegexMatchResult(rule.key)
            result.start = offset + matcher.start()
            result.end = offset + matcher.end()
            list.add(result)
        }
    }
}
