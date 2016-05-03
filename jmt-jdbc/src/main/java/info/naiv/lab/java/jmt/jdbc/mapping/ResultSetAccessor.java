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
package info.naiv.lab.java.jmt.jdbc.mapping;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * オブジェクトの名前で結果セットにアクセスするためのクラス. <br>
 * 結果リストに名前がない場合、例外を発生させるのではなく null を戻す
 *
 * @author enlo
 */
@Slf4j
public class ResultSetAccessor {

    private final Map<String, Integer> columnMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final ResultSetMetaData metaData;

    /**
     *
     * @param rs
     * @throws SQLException
     */
    public ResultSetAccessor(@NonNull ResultSet rs) throws SQLException {
        metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            String columnName = metaData.getColumnName(i);
            columnMap.put(columnName, i);
        }
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Array getArray(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getArray(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public InputStream getAsciiStream(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getAsciiStream(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public BigDecimal getBigDecimal(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBigDecimal(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public InputStream getBinaryStream(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBinaryStream(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Blob getBlob(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBlob(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Boolean getBoolean(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBoolean(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Byte getByte(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getByte(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public byte[] getBytes(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBytes(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Reader getCharacterStream(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getCharacterStream(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Clob getClob(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getClob(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Date getDate(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getDate(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Double getDouble(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getDouble(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Float getFloat(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getFloat(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Integer getInteger(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getInt(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Long getLong(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getLong(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Reader getNCharacterStream(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getNCharacterStream(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public NClob getNClob(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getNClob(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public String getNString(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getNString(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Object getObject(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getObject(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Ref getRef(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getRef(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public RowId getRowId(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getRowId(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public SQLXML getSQLXML(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getSQLXML(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Short getShort(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getShort(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public String getString(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getString(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Time getTime(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getTime(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Timestamp getTimestamp(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getTimestamp(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public URL getURL(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getURL(columnIndex);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public InputStream getUnicodeStream(ResultSet rs, String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getUnicodeStream(columnIndex);
    }
}
