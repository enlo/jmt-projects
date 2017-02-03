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

import info.naiv.lab.java.jmt.SimpleBeanCopierFactory;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.createDate;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getDatePart;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.now;
import java.util.Date;
import javax.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author enlo
 */
public class DateOnly extends java.sql.Date {

    private static final long serialVersionUID = 1L;

    static {
        SimpleBeanCopierFactory.registerConverter(new DateToDateOnlyConverter());
    }

    /**
     * 日付を取得する.
     *
     * @param date 日付. null の場合は0Lで初期化する.
     * @return 日付
     */
    @Nonnull
    public static DateOnly valueOf(Date date) {
        if (date instanceof DateOnly) {
            return ((DateOnly) date).clone();
        }
        else if (date == null) {
            return new DateOnly(0);
        }
        else {
            return new DateOnly(date);
        }
    }

    /**
     *
     */
    public DateOnly() {
        this(now().getTime());
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     */
    public DateOnly(int year, int month, int day) {
        super(createDate(year, month, day).getTime());
    }

    /**
     *
     * @param date
     */
    public DateOnly(long date) {
        this(new Date(date));
    }

    /**
     *
     * @param date
     */
    public DateOnly(Date date) {
        super(getDatePart(date).getTime());
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public DateOnly clone() {
        return (DateOnly) super.clone();
    }

    @Override
    public void setTime(long date) {
        Date newValue = new DateOnly(date);
        super.setTime(newValue.getTime());
    }

    public static class DateToDateOnlyConverter implements Converter<Date, DateOnly> {

        @Override
        public DateOnly convert(Date source) {
            return DateOnly.valueOf(source);
        }
    }
}
