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
package info.naiv.lab.java.jmt.support.jdbc.sql;

import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.FloatSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.TimeSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.BigDecimalSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.BinarySqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.BooleanSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.IntegerSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.StringSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.TimestampSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.DoubleSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.ObjectSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.DateSqlParameter;
import info.naiv.lab.java.jmt.support.jdbc.sql.parameter.LongSqlParameter;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author enlo
 */
public class SqlParameterFactory {

    @ReturnNonNull
    public static SqlParameter from(String name, Object object) {
        if (object == null) {
            return new ObjectSqlParameter(JdbcType.NULL, name, null);
        }
        else if (object instanceof SqlParameter) {
            return (SqlParameter) object;
        }
        else if (object instanceof BigDecimal) {
            return new BigDecimalSqlParameter(name, (BigDecimal) object);
        }
        else if (object instanceof byte[]) {
            return new BinarySqlParameter(name, (byte[]) object);
        }
        else if (object instanceof Boolean) {
            return new BooleanSqlParameter(name, (Boolean) object);
        }
        else if (object instanceof java.sql.Date) {
            return new DateSqlParameter(name, (Date) object);
        }
        else if (object instanceof Double) {
            return new DoubleSqlParameter(name, (Double) object);
        }
        else if (object instanceof Float) {
            return new FloatSqlParameter(name, (Float) object);
        }
        else if (object instanceof Integer) {
            return new IntegerSqlParameter(name, (Integer) object);
        }
        else if (object instanceof Long) {
            return new LongSqlParameter(name, (Long) object);
        }
        else if (object instanceof String) {
            return new StringSqlParameter(name, (String) object);
        }
        else if (object instanceof String) {
            return new StringSqlParameter(name, (String) object);
        }
        else if (object instanceof java.sql.Time) {
            return new TimeSqlParameter(name, (Date) object);
        }
        else if (object instanceof java.sql.Timestamp) {
            return new TimestampSqlParameter(name, (Date) object);
        }
        else {
            Class<?> type = object.getClass();
            for (JdbcType t : JdbcType.values()) {
                Class<?> jdbcType = t.getType();
                if (jdbcType != null && jdbcType.isAssignableFrom(type)) {
                    return new ObjectSqlParameter(t, name, object);
                }
            }
            return new ObjectSqlParameter(JdbcType.UNDEFINED, name, object);
        }
    }

    private SqlParameterFactory() {
    }
}
