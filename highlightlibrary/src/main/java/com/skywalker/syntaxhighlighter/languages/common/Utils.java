package com.skywalker.syntaxhighlighter.languages.common;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/12/14               *
 *******************************/

public class Utils {
    public static String combinePatterns(String... patterns) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < patterns.length; i++) {
            builder.append("(").append(patterns[i]);
            if (i == patterns.length - 1) {
                builder.append(")");
            } else {
                builder.append(")|");
            }

        }
        return builder.toString();
    }
}
