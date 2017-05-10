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
package info.naiv.lab.java.jmt.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.NClob;
import java.util.Date;

/**
 *
 * @author enlo
 */
class JDBCConstants {

    public static final Class<?>[] NUMBER_COMPATIBLE_CLASSES
            = {Byte.class, Short.class, Integer.class, Long.class, Float.class,
                Double.class, BigDecimal.class, Boolean.class, String.class,
                byte.class, short.class, int.class, long.class, float.class, double.class};

    public static final Class<?>[] BINARY_COMPATIBLE_CLASSES
            = {String.class, byte[].class};

    public static final Class<?>[] STRING_COMPATIBLE_CLASSES
            = {Byte.class, Short.class, Integer.class, Long.class, Float.class,
                Double.class, BigDecimal.class, Boolean.class, String.class,
                byte.class, short.class, int.class, long.class, float.class, double.class,
                java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class};

    public static final Class<?>[] CLOB_COMPATIBLE_CLASSES
            = {Clob.class, NClob.class};
}
