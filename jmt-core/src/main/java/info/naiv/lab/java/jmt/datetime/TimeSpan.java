/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayContains;
import static info.naiv.lab.java.jmt.Misc.isBlank;
import static info.naiv.lab.java.jmt.Misc.isEmpty;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.Value;
import info.naiv.lab.java.jmt.monad.Optional;

/**
 *
 * @author enlo
 */
@Value
public class TimeSpan implements Serializable, Comparable<TimeSpan> {

    private static final String[] DAYS = {"D", "DAY", "DAYS"};
    private static final String[] HOURS = {"H", "HOUR", "HOURS"};
    private static final String[] MICROS = {"MC", "MIC", "MICRO", "MICROS", "MICROSEC", "MICROSECOND", "MICROSECONDS"};
    private static final String[] MILLS = {"MS", "MIL", "MILLI", "MILLIS", "MILLISEC", "MILLISECOND", "MILLISECONDS"};
    private static final String[] MINS = {"M", "MIN", "MINUTE", "MINUTES"};
    private static final String[] NANOS = {"N", "NS", "NANO", "NANOS", "NANOSEC", "NANOSECOND", "NANOSECONDS"};
    private static final String[] SECS = {"S", "SEC", "SECOND", "SECONDS"};
    private static final Pattern PATTERN = Pattern.compile("^(\\+|-)?(\\d+)(\\w+)?$", Pattern.CASE_INSENSITIVE);
    private static final long serialVersionUID = -5609761961762572788L;

    private static final Map<TimeUnit, String[]> UNIT_MAP;

    static {
        Map<TimeUnit, String[]> m = new HashMap<>();
        m.put(TimeUnit.DAYS, DAYS);
        m.put(TimeUnit.HOURS, HOURS);
        m.put(TimeUnit.MICROSECONDS, MICROS);
        m.put(TimeUnit.MILLISECONDS, MILLS);
        m.put(TimeUnit.MINUTES, MINS);
        m.put(TimeUnit.NANOSECONDS, NANOS);
        m.put(TimeUnit.SECONDS, SECS);
        UNIT_MAP = Collections.unmodifiableMap(m);
    }

    @Nonnull
    public static TimeSpan distance(@Nonnull Date from, @Nonnull Date to) {
        long dist = to.getTime() - from.getTime();
        return new TimeSpan(dist, TimeUnit.MILLISECONDS);
    }

    @Nonnull
    public static TimeSpan parse(String value) throws ParseException, NumberFormatException {
        if (isBlank(value)) {
            throw new ParseException("value is empty.", 0);
        }
        Matcher m = PATTERN.matcher(value.trim());
        if (!m.matches()) {
            throw new ParseException("invalid pattern.", 0);
        }
        long v;
        String sign = m.group(1);
        if ("-".equals(sign)) {
            v = -Long.parseLong(m.group(2));
        }
        else {
            v = Long.parseLong(m.group(2));
        }

        String unit = m.group(3);
        TimeUnit u = null;
        if (isEmpty(unit)) {
            u = TimeUnit.MILLISECONDS;
        }
        else {
            unit = unit.toUpperCase();
            for (Map.Entry<TimeUnit, String[]> e : UNIT_MAP.entrySet()) {
                if (arrayContains(e.getValue(), unit)) {
                    u = e.getKey();
                    break;
                }
            }
            if (u == null) {
                int unitStart = m.start(3);
                throw new ParseException("invalid unit.", unitStart);
            }
        }
        return new TimeSpan(v, u);
    }

    @Nonnull
    public static Optional<TimeSpan> tryParse(String value) {
        try {
            return Optional.of(parse(value));
        }
        catch (ParseException | RuntimeException ex) {
            return Optional.<TimeSpan>empty();
        }
    }

    private final long value;
    private final TimeUnit unit;

    public TimeSpan(long value, @NonNull TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public int compareTo(TimeSpan o) {
        return Long.compare(toMillis(), o.toMillis());
    }

    @Nonnull
    public TimeSpan newValue(long value) {
        return new TimeSpan(value, unit);
    }

    @Nonnull
    public TimeSpan newUnit(TimeUnit unit) {
        return new TimeSpan(value, unit);
    }

    public long toMillis() {
        return unit.toMillis(value);
    }

    @Override
    public String toString() {
        return value + unit.name();
    }
}
