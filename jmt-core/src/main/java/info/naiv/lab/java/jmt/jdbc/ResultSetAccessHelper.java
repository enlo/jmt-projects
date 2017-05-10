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
package info.naiv.lab.java.jmt.jdbc;

import info.naiv.lab.java.jmt.collection.Lookup;
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
import javax.annotation.Nonnull;
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
public class ResultSetAccessHelper
        implements Lookup<String, Object>, AutoCloseable {

    private final ResultSet baseResultSet;
    private final Map<String, Integer> columnMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final ResultSetMetaData metaData;

    /**
     *
     * @param rs
     * @throws SQLException
     */
    public ResultSetAccessHelper(@NonNull ResultSet rs) throws SQLException {
        baseResultSet = rs;
        metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            String columnName = metaData.getColumnName(i);
            columnMap.put(columnName, i);
        }
    }

    @Override
    public void close() throws Exception {
        if (!baseResultSet.isClosed()) {
            baseResultSet.close();
        }
    }

    @Override
    public boolean containsKey(String key) {
        return columnMap.containsKey(key);
    }

    @Override
    public Object get(String key) {
        try {
            return getObject(key);
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object get(String key, JDBCTypeTraits traits) {
        try {
            return getObject(key, traits);
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Array getArray(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getArray(columnIndex);
    }

    /**
     *
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Array getArray(@Nonnull String columnName) throws SQLException {
        return getArray(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public InputStream getAsciiStream(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(@Nonnull String columnName) throws SQLException {
        return getAsciiStream(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public BigDecimal getBigDecimal(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(@Nonnull String columnName) throws SQLException {
        return getBigDecimal(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public InputStream getBinaryStream(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(@Nonnull String columnName) throws SQLException {
        return getBinaryStream(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Blob getBlob(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBlob(columnIndex);
    }

    public Blob getBlob(@Nonnull String columnName) throws SQLException {
        return getBlob(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Boolean getBoolean(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBoolean(columnIndex);
    }

    public Boolean getBoolean(@Nonnull String columnName) throws SQLException {
        return getBoolean(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Byte getByte(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getByte(columnIndex);
    }

    public Byte getByte(@Nonnull String columnName) throws SQLException {
        return getByte(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public byte[] getBytes(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getBytes(columnIndex);
    }

    public byte[] getBytes(@Nonnull String columnName) throws SQLException {
        return getBytes(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Reader getCharacterStream(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(@Nonnull String columnName) throws SQLException {
        return getCharacterStream(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Clob getClob(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getClob(columnIndex);
    }

    public Clob getClob(@Nonnull String columnName) throws SQLException {
        return getClob(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Date getDate(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getDate(columnIndex);
    }

    public Date getDate(@Nonnull String columnName) throws SQLException {
        return getDate(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Double getDouble(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getDouble(columnIndex);
    }

    public Double getDouble(@Nonnull String columnName) throws SQLException {
        return getDouble(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Float getFloat(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getFloat(columnIndex);
    }

    public Float getFloat(@Nonnull String columnName) throws SQLException {
        return getFloat(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Integer getInteger(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getInt(columnIndex);
    }

    public Integer getInteger(@Nonnull String columnName) throws SQLException {
        return getInteger(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Long getLong(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getLong(columnIndex);
    }

    public Long getLong(@Nonnull String columnName) throws SQLException {
        return getLong(baseResultSet, columnName);
    }

    public Class<?> getMappedType(String columnName) {
        try {
            Integer columnIndex = columnMap.get(columnName);
            if (columnIndex == null) {
                return null;
            }

            int type = metaData.getColumnType(columnIndex);
            return JDBCTypeTraits.valueOf(type).getRecommendType();
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Reader getNCharacterStream(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getNCharacterStream(columnIndex);
    }

    public Reader getNCharacterStream(@Nonnull String columnName) throws SQLException {
        return getNCharacterStream(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public NClob getNClob(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getNClob(columnIndex);
    }

    public NClob getNClob(@Nonnull String columnName) throws SQLException {
        return getNClob(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public String getNString(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getNString(columnIndex);
    }

    public String getNString(@Nonnull String columnName) throws SQLException {
        return getNString(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Object getObject(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getObject(columnIndex);
    }

    public Object getObject(@Nonnull String columnName) throws SQLException {
        return getObject(baseResultSet, columnName);
    }

    public <T> T getObject(@Nonnull String columnName, @Nonnull Class<T> hint) throws SQLException {
        return getObject(baseResultSet, columnName, hint);
    }

    public Object getObject(@Nonnull String columnName, @Nonnull JDBCTypeTraits hint) throws SQLException {
        return getObject(baseResultSet, columnName, hint);
    }

    public Object getObject(@Nonnull ResultSet rs, @Nonnull String columnName, @Nonnull JDBCTypeTraits hint) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return hint.getValueFromResultSet(rs, columnIndex);
    }

    public <T> T getObject(@Nonnull ResultSet rs, @Nonnull String columnName, @Nonnull Class<T> hint) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getObject(columnIndex, hint);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Ref getRef(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getRef(columnIndex);
    }

    public Ref getRef(@Nonnull String columnName) throws SQLException {
        return getRef(baseResultSet, columnName);
    }

    @Nonnull
    public ResultSet getResultSet() {
        return baseResultSet;
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public RowId getRowId(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getRowId(columnIndex);
    }

    public RowId getRowId(@Nonnull String columnName) throws SQLException {
        return getRowId(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public SQLXML getSQLXML(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getSQLXML(columnIndex);
    }

    public SQLXML getSQLXML(@Nonnull String columnName) throws SQLException {
        return getSQLXML(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Short getShort(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getShort(columnIndex);
    }

    public Short getShort(@Nonnull String columnName) throws SQLException {
        return getShort(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public String getString(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getString(columnIndex);
    }

    public String getString(@Nonnull String columnName) throws SQLException {
        return getString(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Time getTime(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getTime(columnIndex);
    }

    public Time getTime(@Nonnull String columnName) throws SQLException {
        return getTime(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public Timestamp getTimestamp(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(@Nonnull String columnName) throws SQLException {
        return getTimestamp(baseResultSet, columnName);
    }

    /**
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    public URL getURL(@Nonnull ResultSet rs, @Nonnull String columnName) throws SQLException {
        Integer columnIndex = columnMap.get(columnName);
        if (columnIndex == null) {
            return null;
        }
        return rs.getURL(columnIndex);
    }

    public URL getURL(@Nonnull String columnName) throws SQLException {
        return getURL(baseResultSet, columnName);
    }

}
