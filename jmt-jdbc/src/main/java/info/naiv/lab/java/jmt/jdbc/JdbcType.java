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
package info.naiv.lab.java.jmt.jdbc;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayContains;
import info.naiv.lab.java.jmt.IntegerEnum;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;
import java.util.Calendar;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;

/**
 *
 * @author enlo
 */
public enum JdbcType implements IntegerEnum {

    BIGINT(Types.BIGINT, Long.class, BigInteger.class, long.class),
    BINARY(Types.BINARY, byte[].class),
    BIT(Types.BIT),
    BLOB(Types.BLOB, Blob.class),
    BOOLEAN(Types.BOOLEAN, boolean.class, Boolean.class),
    CHAR(Types.CHAR),
    CLOB(Types.CLOB, Clob.class),
    DATE(Types.DATE, java.sql.Date.class),
    DECIMAL(Types.DECIMAL, BigDecimal.class),
    DOUBLE(Types.DOUBLE, Double.class, double.class),
    FLOAT(Types.FLOAT),
    INTEGER(Types.INTEGER, Integer.class, int.class),
    LONGVARBINARY(Types.LONGVARBINARY),
    LONGVARCHAR(Types.LONGVARCHAR),
    NCHAR(Types.NCHAR),
    NCLOB(Types.NCLOB),
    NULL(Types.NULL),
    NUMERIC(Types.NUMERIC, Number.class),
    NVARCHAR(Types.NVARCHAR),
    OTHER(Types.OTHER),
    REAL(Types.REAL, Float.class, float.class),
    SMALLINT(Types.SMALLINT, Short.class, short.class),
    SQLXML(Types.SQLXML, java.sql.SQLXML.class),
    STRUCT(Types.STRUCT),
    TIME(Types.TIME, java.sql.Time.class),
    TIMESTAMP(Types.TIMESTAMP, java.sql.Timestamp.class, Calendar.class),
    TINYINT(Types.TINYINT, Byte.class, byte.class),
    UNDEFINED(Integer.MIN_VALUE + 1000),
    VARBINARY(Types.VARBINARY),
    VARCHAR(Types.VARCHAR, String.class, CharSequence.class),;

    @ReturnNonNull
    public static JdbcType valueOf(int value) {
        for (JdbcType jt : values()) {
            if (jt.value == value) {
                return jt;
            }
        }
        return JdbcType.UNDEFINED;
    }
    private final Class<?>[] types;
    private final int value;

    JdbcType(int code, Class<?>... type) {
        this.value = code;
        this.types = type;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public Class<?>[] getTypes() {
        return types;
    }

    public boolean matchType(Class<?> type) {
        return arrayContains(types, type);
    }

    public boolean isAssignableFrom(final Class<?> type) {
        return arrayContains(types, new Predicate1<Class<?>>() {
            @Override
            public boolean test(Class<?> obj) {
                return obj.isAssignableFrom(type);
            }
        });
    }

    @Override
    public int getValue() {
        return value;
    }

    @ReturnNonNull
    public static JdbcType valueOf(Class<?> type) {
        for (JdbcType jt : values()) {
            if (jt.matchType(type)) {
                return jt;
            }
        }
        for (JdbcType jt : values()) {
            if (jt.isAssignableFrom(type)) {
                return jt;
            }
        }
        if (java.util.Date.class.isAssignableFrom(type)) {
            return JdbcType.TIMESTAMP;
        }
        return JdbcType.UNDEFINED;
    }

    public SqlParameter toSqlParameter() {
        return new SqlParameter(value);
    }

    public SqlParameter toSqlParameter(int scale) {
        return new SqlParameter(value, scale);
    }

    public SqlParameterValue toSqlParameterValue(Object obj) {
        return new SqlParameterValue(value, obj);
    }

    public static Object wrap(Object obj) {
        if (obj == null) {
            return null;
        }
        JdbcType jt = valueOf(obj.getClass());
        if (jt == UNDEFINED) {
            return obj;
        }
        else {
            return jt.toSqlParameterValue(obj);
        }
    }
}
