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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayContains;
import static info.naiv.lab.java.jmt.jdbc.JDBCConstants.BINARY_COMPATIBLE_CLASSES;
import static info.naiv.lab.java.jmt.jdbc.JDBCConstants.CLOB_COMPATIBLE_CLASSES;
import static info.naiv.lab.java.jmt.jdbc.JDBCConstants.NUMBER_COMPATIBLE_CLASSES;
import static info.naiv.lab.java.jmt.jdbc.JDBCConstants.STRING_COMPATIBLE_CLASSES;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import javax.annotation.Nonnull;
import lombok.Getter;

/**
 *
 * @author enlo
 */
@Getter
public enum JDBCTypeTraits {

    /**
     * Types.BIGINT ⇔ Long <br>
     */
    BIGINT(Types.BIGINT, Long.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Long getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getLong(columnLabel);
        }

        @Override
        public Long getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getLong(columnIndex);
        }

    },
    /**
     * Types.BINARY ⇔ byte[] <br>
     */
    BINARY(Types.BINARY, byte[].class, BINARY_COMPATIBLE_CLASSES) {
        @Override
        public byte[] getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBytes(columnIndex);
        }

        @Override
        public byte[] getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBytes(columnLabel);
        }
    },
    /**
     * Types.BIT ⇔ Boolean <br>
     */
    BIT(Types.BIT, Boolean.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Boolean getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBoolean(columnIndex);
        }

        @Override
        public Boolean getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBoolean(columnLabel);
        }
    },
    /**
     * Types.BLOB ⇔ Blob <br>
     */
    BLOB(Types.BLOB, byte[].class, tie(Blob.class)) {
        @Override
        public Object getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBlob(columnIndex);
        }

        @Override
        public Object getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBlob(columnLabel);
        }
    },
    /**
     * Types.BIT ⇔ Boolean <br>
     */
    BOOLEAN(Types.BOOLEAN, Boolean.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Boolean getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBoolean(columnIndex);
        }

        @Override
        public Boolean getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBoolean(columnLabel);
        }
    },
    /**
     * Types.CHAR ⇒ String <br>
     */
    CHAR(Types.CHAR, String.class, STRING_COMPATIBLE_CLASSES) {
        @Override
        public String getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getString(columnIndex);
        }

        @Override
        public String getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getString(columnLabel);
        }
    },
    /**
     * Types.CLOB ⇔ Clob <br>
     */
    CLOB(Types.CLOB, Clob.class, CLOB_COMPATIBLE_CLASSES) {
        @Override
        public Clob getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getClob(columnIndex);
        }

        @Override
        public Clob getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getClob(columnLabel);
        }
    },
    /**
     * Types.DATE ⇔ java.sql.Date <br>
     */
    DATE(Types.DATE, java.sql.Date.class, tie(String.class, java.sql.Date.class, Timestamp.class)) {
        @Override
        public java.sql.Date getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getDate(columnIndex);
        }

        @Override
        public Object getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getDate(columnLabel);
        }
    },
    /**
     * Types.DECIMAL ⇔ BigDecimal <br>
     * Number ⇒ Types.DECIMAL
     */
    DECIMAL(Types.DECIMAL, BigDecimal.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public BigDecimal getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBigDecimal(columnIndex);
        }

        @Override
        public BigDecimal getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBigDecimal(columnLabel);
        }
    },
    /**
     * Types.DOUBLE ⇔ Double <br>
     * Float ⇒ Types.DOUBLE
     */
    DOUBLE(Types.DOUBLE, Double.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Double getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getDouble(columnIndex);
        }

        @Override
        public Double getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getDouble(columnLabel);
        }
    },
    /**
     * Types.FLOAT ⇔ Float <br>
     */
    FLOAT(Types.FLOAT, Float.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Float getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getFloat(columnIndex);
        }

        @Override
        public Float getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getFloat(columnLabel);
        }
    },
    /**
     * Types.INTEGER ⇔ Integer <br>
     * Short | Byte ⇒ Types.INTEGER
     */
    INTEGER(Types.INTEGER, Integer.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Integer getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getInt(columnIndex);
        }

        @Override
        public Integer getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getInt(columnLabel);
        }
    },
    /**
     * Types.LONGVARBINARY ⇔ byte[] <br>
     * 通常、InputStream にマップすべきだが、Beanでの使用を考慮してbyte[]にマップする
     */
    LONGVARBINARY(Types.LONGVARBINARY, byte[].class, BINARY_COMPATIBLE_CLASSES) {
        @Override
        public byte[] getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBytes(columnIndex);
        }

        @Override
        public byte[] getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBytes(columnLabel);
        }
    },
    /**
     * Types.LONGVARCHAR ⇔ String <br>
     * 通常、Reader にマップすべきだが、Beanでの使用を考慮してStringにマップする
     */
    LONGVARCHAR(Types.LONGVARCHAR, String.class, STRING_COMPATIBLE_CLASSES) {
        @Override
        public String getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getString(columnIndex);
        }

        @Override
        public String getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getString(columnLabel);
        }
    },
    /**
     * Types.NCHAR ⇔ String <br>
     */
    NCHAR(Types.NCHAR, String.class, tie(String.class)) {
        @Override
        public String getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getNString(columnIndex);
        }

        @Override
        public String getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getNString(columnLabel);
        }
    },
    /**
     * Types.NCLOB ⇔ NClob <br>
     */
    NCLOB(Types.NCLOB, NClob.class, CLOB_COMPATIBLE_CLASSES) {
        @Override
        public NClob getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getNClob(columnIndex);
        }

        @Override
        public NClob getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getNClob(columnLabel);
        }
    },
    /**
     * Types.NULL ⇔ null <br>
     */
    NULL(Types.NULL, Void.class, tie(Void.class)) {
        @Override
        public Object getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return null;
        }

        @Override
        public Object getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return null;
        }
    },
    /**
     * Types.NUMERIC ⇔ BigDecimal <br>
     * Number ⇒ Types.NUMERIC
     */
    NUMERIC(Types.NUMERIC, BigDecimal.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public BigDecimal getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBigDecimal(columnIndex);
        }

        @Override
        public BigDecimal getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBigDecimal(columnLabel);
        }
    },
    /**
     * Types.NVARCHAR ⇔ String <br>
     */
    NVARCHAR(Types.NVARCHAR, String.class, tie(String.class)) {
        @Override
        public String getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getNString(columnIndex);
        }

        @Override
        public String getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getNString(columnLabel);
        }
    },
    /**
     * Types.OTHER ⇒ Object <br>
     */
    OTHER(Types.OTHER, Object.class, tie()) {
        @Override
        public Object getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getObject(columnIndex);
        }

        @Override
        public Object getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getObject(columnLabel);
        }
    },
    /**
     * Types.REAL ⇔ Float <br>
     */
    REAL(Types.REAL, Float.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Float getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getFloat(columnIndex);
        }

        @Override
        public Float getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getFloat(columnLabel);
        }
    },
    /**
     * Types.SMALLINT ⇔ Short <br>
     */
    SMALLINT(Types.SMALLINT, Short.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Short getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getShort(columnIndex);
        }

        @Override
        public Short getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getShort(columnLabel);
        }
    },
    /**
     * Types.SQLXML ⇔ SQLXML <br>
     */
    SQLXML(Types.SQLXML, SQLXML.class, tie(SQLXML.class)) {
        @Override
        public SQLXML getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getSQLXML(columnIndex);
        }

        @Override
        public SQLXML getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getSQLXML(columnLabel);
        }
    },
    /**
     * Types.STRUCT ⇔ Object <br>
     */
    STRUCT(Types.STRUCT, Struct.class, tie(Struct.class)) {
        @Override
        public Object getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getObject(columnIndex);
        }

        @Override
        public Object getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getObject(columnLabel);
        }
    },
    /**
     * Types.TIME ⇔ java.sql.Time <br>
     */
    TIME(Types.TIME, java.sql.Time.class, tie(String.class, java.sql.Time.class, Timestamp.class)) {
        @Override
        public java.sql.Time getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getTime(columnIndex);
        }

        @Override
        public java.sql.Time getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getTime(columnLabel);
        }
    },
    /**
     * Types.TIMESTAMP ⇔ java.sql.Timestamp <br>
     * java.util.Date | Calendar ⇒ Timestamp
     */
    TIMESTAMP(Types.TIMESTAMP, Timestamp.class,
              tie(Timestamp.class, java.sql.Date.class, java.sql.Time.class, String.class)) {
        @Override
        public Timestamp getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getTimestamp(columnIndex);
        }

        @Override
        public Timestamp getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getTimestamp(columnLabel);
        }
    },
    /**
     * Types.TINYINT ⇔ Byte <br>
     */
    TINYINT(Types.TINYINT, Byte.class, NUMBER_COMPATIBLE_CLASSES) {
        @Override
        public Byte getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getByte(columnIndex);
        }

        @Override
        public Byte getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getByte(columnLabel);
        }
    },
    /**
     * Types.VARBINARY ⇔ byte[] <br>
     */
    VARBINARY(Types.VARBINARY, byte[].class, BINARY_COMPATIBLE_CLASSES) {
        @Override
        public byte[] getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getBytes(columnIndex);
        }

        @Override
        public byte[] getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getBytes(columnLabel);
        }
    },
    /**
     * Types.VARCHAR ⇔ String <br>
     */
    VARCHAR(Types.VARCHAR, String.class, STRING_COMPATIBLE_CLASSES) {
        @Override
        public String getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getString(columnIndex);
        }

        @Override
        public String getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
            return rs.getString(columnLabel);
        }
    },;

    /**
     *
     * @param value
     * @return
     */
    @Nonnull
    public static JDBCTypeTraits valueOf(int value) {
        for (JDBCTypeTraits jt : values()) {
            if (jt.typeValue == value) {
                return jt;
            }
        }
        return OTHER;
    }

    public static JDBCTypeTraits valueOf(@Nonnull Class<?> clazz) {
        if (String.class.equals(clazz) || CharSequence.class.equals(clazz)) {
            return VARCHAR;
        }
        else if (Number.class.isAssignableFrom(clazz)) {
            if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                return INTEGER;
            }
            else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                return BIGINT;
            }
            else if (BigDecimal.class.equals(clazz)) {
                return NUMERIC;
            }
            else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                return SMALLINT;
            }
            else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                return TINYINT;
            }
            else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                return FLOAT;
            }
            else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
                return DOUBLE;
            }
            else {
                return NUMERIC;
            }
        }
        else if (Date.class.isAssignableFrom(clazz)) {
            if (Date.class.equals(clazz)) {
                return TIMESTAMP;
            }
            else if (java.sql.Date.class.equals(clazz)) {
                return DATE;
            }
            else if (java.sql.Time.class.equals(clazz)) {
                return TIME;
            }
            else {
                return TIMESTAMP;
            }
        }
        for (JDBCTypeTraits jt : values()) {
            if (clazz.isAssignableFrom(jt.recommendType)) {
                return jt;
            }
        }
        return OTHER;
    }

    private static Class<?>[] tie(Class<?>... clz) {
        return clz;
    }

    private final Class<?>[] compatibleTypes;
    private final Class<?> recommendType;
    private final int typeValue;

    private JDBCTypeTraits(int code, Class<?> mappedType, Class<?>[] types) {
        this.typeValue = code;
        this.recommendType = mappedType;
        this.compatibleTypes = types;
    }

    public abstract Object getValueFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    public abstract Object getValueFromResultSet(ResultSet rs, String columnLabel) throws SQLException;

    /**
     *
     * @param type
     * @return
     */
    public boolean isAssignableFrom(final Class<?> type) {
        for (Class<?> clazz : compatibleTypes) {
            if (clazz.isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param type
     * @return
     */
    public boolean matchType(Class<?> type) {
        return arrayContains(compatibleTypes, type);
    }
}
