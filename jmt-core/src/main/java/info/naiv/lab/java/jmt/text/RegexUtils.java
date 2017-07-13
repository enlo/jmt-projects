/*
 * The MIT License
 *
 * Copyright 2017 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.text;

import info.naiv.lab.java.jmt.fx.Function1;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

public class RegexUtils {

    public static <T> T fetch(@Nonnull CharSequence source,
                              @Nonnull Pattern pattern,
                              @Nonnull MatcherCallback<T> mc) {
        Matcher matcher = pattern.matcher(source);
        T context = mc.onInit();
        if (matcher.find()) {
            int total = source.length();
            int pos = 0;
            do {
                context = mc.onNext(context, source, pos, matcher.start(), matcher.toMatchResult());
                pos = matcher.end();
            }
            while (matcher.find());
            context = mc.onEnd(context, source, pos, total);
        }
        return context;
    }

    /**
     * コールバック置換.
     *
     * @param source
     * @param pattern
     * @param replacement
     * @return
     */
    public static String replaceAll(@Nonnull CharSequence source,
                                    @Nonnull Pattern pattern,
                                    @Nonnull Function1<MatchResult, String> replacement) {
        if (source instanceof String) {
            return replaceForString(pattern, (String) source, replacement);
        }
        else {
            return replaceForCharSeq(pattern, source, replacement);
        }
    }

    /**
     * コールバック置換.
     *
     * @param source
     * @param pattern
     * @param replacement
     * @return
     */
    public static String replaceAll(@Nonnull CharSequence source,
                                    @Nonnull String pattern,
                                    @Nonnull Function1<MatchResult, String> replacement) {
        return replaceAll(source, Pattern.compile(pattern), replacement);
    }

    private static String replaceForCharSeq(Pattern pattern,
                                            CharSequence source,
                                            Function1<MatchResult, String> replacement) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            int total = source.length();
            StringBuilder sb = new StringBuilder((int) (total * 1.5));
            int pos = 0;
            do {
                sb.append(source, pos, matcher.start());
                sb.append(replacement.apply(matcher.toMatchResult()));
                pos = matcher.end();
            }
            while (matcher.find());
            sb.append(source, pos, total);
            return sb.toString();
        }
        return source.toString();
    }

    private static String replaceForString(Pattern pattern,
                                           String source,
                                           Function1<MatchResult, String> replacement) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            int total = source.length();
            char[] cs = source.toCharArray();
            StringBuilder sb = new StringBuilder((int) (total * 1.5));
            int pos = 0;
            do {
                sb.append(cs, pos, matcher.start());
                sb.append(replacement.apply(matcher.toMatchResult()));
                pos = matcher.end();
            }
            while (matcher.find());
            sb.append(cs, pos, total);
            return sb.toString();
        }
        return source;
    }

    private RegexUtils() {
    }

    public interface MatcherCallback<T> {

        T onInit();

        T onNext(T context, CharSequence source, int begin, int matchStart, MatchResult mr);

        T onEnd(T context, CharSequence source, int begin, int end);
    }
}
