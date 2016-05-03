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
import static info.naiv.lab.java.jmt.Constants.KNOWN_INT_TYPES;
import info.naiv.lab.java.jmt.IntegerEnum;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Types;
import java.util.Calendar;
import lombok.Getter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

/**
 *
 * @author enlo
 */
@Getter
public enum JdbcType implements IntegerEnum {

    /**
     * Types.BIGINT ⇔ Long <br> {@link KNOWN_INT_TYPES} ⇒ Types.BIGINT
     */
    BIGINT(Types.BIGINT,
           Long.class,
           tie(KNOWN_INT_TYPES)),
    /**
     * Types.BINARY ⇔ byte[] <br>
     */
    BINARY(Types.BINARY,
           byte[].class,
           tie(byte[].class)),
    /**
     * Types.BIT ⇔ Boolean <br>
     */
    BIT(Types.BIT,
        Boolean.class,
        tie(Boolean.class, boolean.class)),
    /**
     * Types.BLOB ⇔ Blob <br>
     */
    BLOB(Types.BLOB,
         byte[].class,
         tie(Blob.class)),
    /**
     * Types.BIT ⇔ Boolean <br>
     */
    BOOLEAN(Types.BOOLEAN,
            Boolean.class,
            tie(Boolean.class, boolean.class)),
    /**
     * Types.CHAR ⇒ String <br>
     */
    CHAR(Types.CHAR,
         String.class,
         tie(String.class)),
    /**
     * Types.CLOB ⇔ Clob <br>
     */
    CLOB(Types.CLOB,
         Clob.class,
         tie(Clob.class)),
    /**
     * Types.DATE ⇔ java.sql.Date <br>
     */
    DATE(Types.DATE,
         java.sql.Date.class,
         tie(java.sql.Date.class)),
    /**
     * Types.DECIMAL ⇔ BigDecimal <br>
     * Number ⇒ Types.DECIMAL
     */
    DECIMAL(Types.DECIMAL,
            BigDecimal.class,
            tie(Number.class)),
    /**
     * Types.DOUBLE ⇔ Double <br>
     * Float ⇒ Types.DOUBLE
     */
    DOUBLE(Types.DOUBLE,
           Double.class,
           tie(Double.class, double.class, Float.class, float.class)),
    /**
     * Types.FLOAT ⇔ Float <br>
     */
    FLOAT(Types.FLOAT,
          Float.class,
          tie(Float.class, float.class)),
    /**
     * Types.INTEGER ⇔ Integer <br>
     * Short | Byte ⇒ Types.INTEGER
     */
    INTEGER(Types.INTEGER,
            Integer.class,
            tie(Integer.class, int.class, Short.class, short.class, Byte.class, byte.class)),
    /**
     * Types.LONGVARBINARY ⇔ byte[] <br>
     */
    LONGVARBINARY(Types.LONGVARBINARY,
                  byte[].class,
                  tie(byte[].class)),
    /**
     * Types.LONGVARCHAR ⇔ String <br>
     */
    LONGVARCHAR(Types.LONGVARCHAR,
                String.class,
                tie(String.class)),
    /**
     * Types.NCHAR ⇔ String <br>
     */
    NCHAR(Types.NCHAR,
          String.class,
          tie(String.class)),
    /**
     * Types.NCLOB ⇔ NClob <br>
     */
    NCLOB(Types.NCLOB,
          NClob.class,
          tie(NClob.class)),
    /**
     * Types.NULL ⇔ Void <br>
     */
    NULL(Types.NULL,
         Void.class,
         tie(Void.class)),
    /**
     * Types.NUMERIC ⇔ BigDecimal <br>
     * Number ⇒ Types.NUMERIC
     */
    NUMERIC(Types.NUMERIC,
            BigDecimal.class,
            tie(Number.class)),
    /**
     * Types.NVARCHAR ⇔ String <br>
     */
    NVARCHAR(Types.NVARCHAR,
             String.class,
             tie(String.class)),
    /**
     * Types.OTHER ⇒ Object <br>
     */
    OTHER(Types.OTHER, Object.class, tie()),
    /**
     * Types.REAL ⇔ Float <br>
     */
    REAL(Types.REAL,
         Float.class,
         tie(Float.class, float.class)),
    /**
     * Types.SMALLINT ⇔ Short <br>
     */
    SMALLINT(Types.SMALLINT,
             Short.class,
             tie(Short.class, short.class, Byte.class, byte.class)),
    /**
     * Types.SQLXML ⇔ SQLXML <br>
     */
    SQLXML(Types.SQLXML,
           SQLXML.class,
           tie(SQLXML.class)),
    /**
     * Types.STRUCT ⇔ Struct <br>
     */
    STRUCT(Types.STRUCT,
           Struct.class,
           tie(Struct.class)),
    /**
     * Types.TIME ⇔ java.sql.Time <br>
     */
    TIME(Types.TIME,
         java.sql.Time.class,
         tie(java.sql.Time.class)),
    /**
     * Types.TIMESTAMP ⇔ java.sql.Timestamp <br>
     * java.util.Date | Calendar ⇒ Timestamp
     */
    TIMESTAMP(Types.TIMESTAMP,
              java.sql.Timestamp.class,
              tie(java.sql.Timestamp.class, java.util.Date.class, Calendar.class)),
    /**
     * Types.TINYINT ⇔ Byte <br>
     */
    TINYINT(Types.TINYINT,
            Byte.class,
            tie(Byte.class, byte.class)),
    /**
     * UNKNOWN Type.
     */
    UNKNOWN(SqlTypeValue.TYPE_UNKNOWN, Void.class, tie()),
    /**
     * Types.VARBINARY ⇔ byte[] <br>
     */
    VARBINARY(Types.VARBINARY,
              byte[].class,
              tie(byte[].class)),
    /**
     * Types.VARCHAR ⇔ String <br>
     */
    VARCHAR(Types.VARCHAR,
            String.class,
            tie(String.class)),;

    /**
     *
     * @param value
     * @return
     */
    @ReturnNonNull
    public static JdbcType valueOf(int value) {
        for (JdbcType jt : values()) {
            if (jt.value == value) {
                return jt;
            }
        }
        return JdbcType.UNKNOWN;
    }

    /**
     *
     * @param type
     * @return
     */
    @ReturnNonNull
    public static JdbcType valueOf(Class<?> type) {
        int jdbcType = StatementCreatorUtils.javaTypeToSqlParameterType(type);
        if (jdbcType == SqlTypeValue.TYPE_UNKNOWN) {
            for (JdbcType jt : values()) {
                if (jt.isAssignableFrom(type)) {
                    return jt;
                }
            }
        }
        return valueOf(jdbcType);
    }

    /**
     *
     * @param obj
     * @return
     */
    public static Object wrap(Object obj) {
        if (obj == null) {
            return null;
        }
        JdbcType jt = valueOf(obj.getClass());
        if (jt == UNKNOWN) {
            return obj;
        }
        else {
            return jt.toSqlParameterValue(obj);
        }
    }

    private static Class<?>[] tie(Class<?>... clz) {
        return clz;
    }

    private final Class<?>[] compatibleTypes;
    private final Class<?> mappedType;
    private final int value;

    JdbcType(int code, Class<?> mappedType, Class<?>[] types) {
        this.value = code;
        this.mappedType = mappedType;
        this.compatibleTypes = types;
    }

    @Override
    public String getName() {
        return this.name();
    }

    /**
     *
     * @param type
     * @return
     */
    public boolean isAssignableFrom(final Class<?> type) {
        return arrayContains(compatibleTypes, new Predicate1<Class<?>>() {
            @Override
            public boolean test(Class<?> obj) {
                return obj.isAssignableFrom(type);
            }
        });
    }

    /**
     *
     * @param type
     * @return
     */
    public boolean matchType(Class<?> type) {
        return arrayContains(compatibleTypes, type);
    }

    /**
     *
     * @return
     */
    public SqlParameter toSqlParameter() {
        return new SqlParameter(value);
    }

    /**
     *
     * @param scale
     * @return
     */
    public SqlParameter toSqlParameter(int scale) {
        return new SqlParameter(value, scale);
    }

    /**
     *
     * @param obj
     * @return
     */
    public SqlParameterValue toSqlParameterValue(Object obj) {
        return new SqlParameterValue(value, obj);
    }

}
