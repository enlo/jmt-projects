/*
 * The MIT License
 *
 * Copyright 2015 enlo.
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
package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.Strings.isNotEmpty;
import static info.naiv.lab.java.jmt.Misc.toInt;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public enum DateTimePattern {

    /**
     * 日付のみのパターン.
     */
    DATE_PATTERN("(?<year>\\d{3,})[\\-/.](?<month>\\d{1,2})[\\-/.](?<date>\\d{1,2})") {
        @Override
        protected void toCalendar(Matcher m, Calendar out) {
            out.set(Calendar.YEAR, Integer.parseInt(m.group("year")));
            out.set(Calendar.MONTH, Integer.parseInt(m.group("month")) - 1);
            out.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m.group("date")));
        }
    },
    /**
     * 日付のみのパターン. (短縮年)
     */
    DATE_PATTERN_SY("(?<year>\\d{1,2})[\\-/.](?<month>\\d{1,2})[\\-/.](?<date>\\d{1,2})") {

        @Override
        protected void toCalendar(Matcher m, Calendar out) {
            int year = Integer.parseInt(m.group("year"));
            year = ClassicDateUtils.getShortYearConverter().convert(year, ClassicDateUtils.now());
            out.set(Calendar.YEAR, year);
            out.set(Calendar.MONTH, Integer.parseInt(m.group("month")) - 1);
            out.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m.group("date")));
        }
    },
    /**
     * 時刻のみのパターン.
     */
    TIME_PATTERN("(?<hour>\\d{1,2}):(?<min>\\d{1,2})(:(?<sec>\\d{1,2}))?([.](?<ms>\\d{1,3}))?(?<tz>[+-][0-9:]+)?") {

        @Override
        protected void toCalendar(Matcher m, Calendar out) {
            out.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group("hour")));
            out.set(Calendar.MINUTE, Integer.parseInt(m.group("min")));
            out.set(Calendar.SECOND, toInt(m.group("sec"), 0));
            out.set(Calendar.MILLISECOND, toInt(m.group("ms"), 0));
            String tz = m.group("tz");
            if (isNotEmpty(tz)) {
                out.setTimeZone(TimeZone.getTimeZone("GMT" + tz));
            }
        }

    },
    /**
     * 日付と時刻のパターン.
     */
    DATETIME_PATTERN(DATE_PATTERN, TIME_PATTERN) {

        @Override
        protected void toCalendar(Matcher m, Calendar out) {
            DATE_PATTERN.toCalendar(m, out);
            TIME_PATTERN.toCalendar(m, out);
        }
    },
    /**
     * 日付と時刻のパターン. (短縮年)
     */
    DATETIME_PATTERN_SY(DATE_PATTERN_SY, TIME_PATTERN) {

        @Override
        protected void toCalendar(Matcher m, Calendar out) {
            DATE_PATTERN_SY.toCalendar(m, out);
            TIME_PATTERN.toCalendar(m, out);
        }
    };

    private final String basePattern;
    private final Pattern pattern;

    private DateTimePattern(String pattern) {
        this.basePattern = pattern;
        this.pattern = Pattern.compile("^" + basePattern + "$");
    }

    private DateTimePattern(DateTimePattern date, DateTimePattern time) {
        this(date.basePattern + "(T|\\s+)" + time.basePattern);
    }

    /**
     *
     * @return 正規表現パターン
     */
    @Nonnull
    public String pattern() {
        return this.pattern.pattern();
    }

    /**
     * 解析. 日付の内容が厳密かどうかはここでは判断しない。<br>
     * 必要な場合は、別途行うこと.
     *
     * @param dateText 文字列
     * @param out 解析結果を設定するカレンダー.
     * @return 解析できた場合は true.
     */
    public boolean toCalendar(String dateText, Calendar out) {
        Matcher m = pattern.matcher(dateText);
        if (m.matches()) {
            ClassicDateUtils.clearDatePart(out);
            ClassicDateUtils.clearTimePart(out);
            toCalendar(m, out);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param m
     * @param out
     */
    protected abstract void toCalendar(Matcher m, Calendar out);
}
