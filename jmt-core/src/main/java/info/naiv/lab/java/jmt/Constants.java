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
package info.naiv.lab.java.jmt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定数.
 *
 * @author enlo
 */
public class Constants {

    /**
     * 改行コード CR
     */
    public static final String CR = "\r";
    /**
     * 改行コード CR+LF
     */
    public static final String CRLF = "\r\n";
    /**
     * 1日のミリ秒.
     */
    public static final long DAY_IN_MILLS = TimeUnit.DAYS.toMillis(1);

    /**
     * 既知の整数型
     */
    public static final List<Class<?>> KNOWN_INT_TYPES = Arrays.<Class<?>>asList(
            BigInteger.class,
            Long.class,
            Integer.class,
            Short.class,
            Byte.class);

    /**
     * 既知の実数型
     */
    public static final List<Class<?>> KNOWN_REAL_TYPES = Arrays.<Class<?>>asList(
            BigDecimal.class,
            Double.class,
            Float.class);

    /**
     * 改行コード LF
     */
    public static final String LF = "\n";
    /**
     * 7DAYS
     */
    public static final int SEVEN_DAYS = 7;
    /**
     * BOM または ZWNBSP.
     */
    public static final String ZWNBSP = "\uFEFF";

    private Constants() {
    }

}
