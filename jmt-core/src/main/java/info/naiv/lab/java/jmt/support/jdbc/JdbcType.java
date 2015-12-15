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
package info.naiv.lab.java.jmt.support.jdbc;

import info.naiv.lab.java.jmt.IntegerEnum;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;

/**
 *
 * @author enlo
 */
public enum JdbcType implements IntegerEnum {

    BIT(Types.BIT),
    TINYINT(Types.TINYINT, Byte.class),
    SMALLINT(Types.SMALLINT, Short.class),
    INTEGER(Types.INTEGER, Integer.class),
    BIGINT(Types.BIGINT, Long.class),
    FLOAT(Types.FLOAT),
    REAL(Types.REAL, Float.class),
    DOUBLE(Types.DOUBLE, Double.class),
    NUMERIC(Types.NUMERIC),
    DECIMAL(Types.DECIMAL, BigDecimal.class),
    CHAR(Types.CHAR),
    VARCHAR(Types.VARCHAR, String.class),
    LONGVARCHAR(Types.LONGVARCHAR),
    DATE(Types.DATE, java.sql.Date.class),
    TIME(Types.TIME, java.sql.Time.class),
    TIMESTAMP(Types.TIMESTAMP, java.sql.Timestamp.class),
    BINARY(Types.BINARY, byte[].class),
    VARBINARY(Types.VARBINARY),
    LONGVARBINARY(Types.LONGVARBINARY),
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    BLOB(Types.BLOB, Blob.class),
    CLOB(Types.CLOB, Clob.class),
    BOOLEAN(Types.BOOLEAN, Boolean.class),
    NVARCHAR(Types.NVARCHAR),
    NCHAR(Types.NCHAR),
    NCLOB(Types.NCLOB),
    SQLXML(Types.SQLXML, java.sql.SQLXML.class),
    STRUCT(Types.STRUCT),
    UNDEFINED(Integer.MIN_VALUE + 1000),;

    private final int value;
    private final Class<?> type;

    JdbcType(int code) {
        value = code;
        type = null;
    }

    JdbcType(int code, Class<?> type) {
        this.value = code;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getValue() {
        return value;
    }

    public Class<?> getType() {
        return type;
    }

    @ReturnNonNull
    public static JdbcType valueOf(int value) {
        for (JdbcType jt : values()) {
            if (jt.value == value) {
                return jt;
            }
        }
        return JdbcType.UNDEFINED;
    }

}
