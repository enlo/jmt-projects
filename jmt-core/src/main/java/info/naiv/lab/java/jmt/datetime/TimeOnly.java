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

import info.naiv.lab.java.jmt.Constants;
import info.naiv.lab.java.jmt.SimpleBeanCopierFactory;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getTimePart;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.now;
import java.util.Calendar;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author enlo
 */
public class TimeOnly extends java.sql.Time {

    private static final long serialVersionUID = 1L;

    static {
        SimpleBeanCopierFactory.registerConverter(new DateToTimeOnlyConverter());
    }

    public static TimeOnly valueOf(Date date) {
        if (date instanceof TimeOnly) {
            return ((TimeOnly) date).clone();
        }
        else if (date == null) {
            return new TimeOnly(0);
        }
        else {
            return new TimeOnly(date);
        }
    }

    /**
     *
     */
    public TimeOnly() {
        this(now().getTime());
    }

    /**
     *
     * @param hour
     * @param minute
     * @param second
     */
    public TimeOnly(int hour, int minute, int second) {
        this(hour, minute, second, 0);
    }

    /**
     *
     * @param hour
     * @param minute
     * @param second
     * @param millisecond
     */
    public TimeOnly(int hour, int minute, int second, int millisecond) {
        super(0);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        super.setTime(getTimePart(cal).getTime().getTime());
    }

    /**
     *
     * @param time
     */
    public TimeOnly(long time) {
        this(new Date(time));
    }

    /**
     *
     * @param time
     */
    public TimeOnly(Date time) {
        super(getTimePart(time).getTime());
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public TimeOnly clone() {
        return (TimeOnly) super.clone();
    }

    @Override
    public void setTime(long time) {
        long mills = time % Constants.DAY_IN_MILLS;
        super.setTime(mills);
    }

    public static class DateToTimeOnlyConverter implements Converter<Date, TimeOnly> {

        @Override
        public TimeOnly convert(Date source) {
            return TimeOnly.valueOf(source);
        }
    }
}
