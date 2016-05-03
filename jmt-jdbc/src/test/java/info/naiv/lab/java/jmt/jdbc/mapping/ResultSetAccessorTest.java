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

import info.naiv.lab.java.jmt.datetime.DateOnly;
import info.naiv.lab.java.jmt.datetime.TimeOnly;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author enlo
 */
public class ResultSetAccessorTest {

    private ResultSetAccessor accessor;
    private ResultSet mockResultSet;

    public ResultSetAccessorTest() {
    }

    @Before
    public void setUp() throws SQLException {
        mockResultSet = mock(ResultSet.class);

        ResultSetMetaData md = mock(ResultSetMetaData.class);
        when(md.getColumnCount()).thenReturn(10);
        when(md.getColumnName(1)).thenReturn("USERID");
        when(md.getColumnName(2)).thenReturn("NAME");
        when(md.getColumnName(3)).thenReturn("AGE");
        when(md.getColumnName(4)).thenReturn("BIRTH");
        when(md.getColumnName(5)).thenReturn("PHONE");
        when(md.getColumnName(6)).thenReturn("POSTAL");
        when(md.getColumnName(7)).thenReturn("CITY");
        when(md.getColumnName(8)).thenReturn("STREET");
        when(md.getColumnName(9)).thenReturn("MAIL");
        when(md.getColumnName(10)).thenReturn("OFFICE");
        when(mockResultSet.getMetaData()).thenReturn(md);

        accessor = new ResultSetAccessor(mockResultSet);
    }

    /**
     * Test of getArray method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetArray() throws Exception {
        Array obj1 = mock(Array.class);
        Array obj2 = mock(Array.class);
        when(mockResultSet.getArray(1)).thenReturn(obj1);
        when(mockResultSet.getArray(2)).thenReturn(obj2);
        assertThat(accessor.getArray(mockResultSet, "userId"), is(sameInstance(obj1)));
        assertThat(accessor.getArray(mockResultSet, "NaMe"), is(sameInstance(obj2)));
        assertThat(accessor.getArray(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getAsciiStream method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAsciiStream() throws Exception {
        InputStream obj1 = mock(InputStream.class);
        InputStream obj2 = mock(InputStream.class);
        when(mockResultSet.getAsciiStream(1)).thenReturn(obj1);
        when(mockResultSet.getAsciiStream(2)).thenReturn(obj2);
        assertThat(accessor.getAsciiStream(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getAsciiStream(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getAsciiStream(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getBigDecimal method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBigDecimal() throws Exception {
        BigDecimal obj1 = new BigDecimal(10);
        BigDecimal obj2 = new BigDecimal("0.5");
        when(mockResultSet.getBigDecimal(1)).thenReturn(obj1);
        when(mockResultSet.getBigDecimal(2)).thenReturn(obj2);
        assertThat(accessor.getBigDecimal(mockResultSet, "UserId"), is(obj1));
        assertThat(accessor.getBigDecimal(mockResultSet, "Name"), is(obj2));
        assertThat(accessor.getBigDecimal(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getBinaryStream method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBinaryStream() throws Exception {
        InputStream obj1 = mock(InputStream.class);
        InputStream obj2 = mock(InputStream.class);
        when(mockResultSet.getBinaryStream(1)).thenReturn(obj1);
        when(mockResultSet.getBinaryStream(2)).thenReturn(obj2);
        assertThat(accessor.getBinaryStream(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getBinaryStream(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getBinaryStream(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getBlob method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBlob() throws Exception {
        Blob obj1 = mock(Blob.class);
        Blob obj2 = mock(Blob.class);
        when(mockResultSet.getBlob(1)).thenReturn(obj1);
        when(mockResultSet.getBlob(2)).thenReturn(obj2);
        assertThat(accessor.getBlob(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getBlob(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getBlob(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getBoolean method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBoolean() throws Exception {
        when(mockResultSet.getBoolean(1)).thenReturn(true);
        when(mockResultSet.getBoolean(2)).thenReturn(false);
        assertThat(accessor.getBoolean(mockResultSet, "USERID"), is(Boolean.TRUE));
        assertThat(accessor.getBoolean(mockResultSet, "NAME"), is(false));
        assertThat(accessor.getBoolean(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getByte method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetByte() throws Exception {
        when(mockResultSet.getByte(1)).thenReturn((byte) 1);
        when(mockResultSet.getByte(2)).thenReturn((byte) 2);
        assertThat(accessor.getByte(mockResultSet, "USERID"), is((byte) 1));
        assertThat(accessor.getByte(mockResultSet, "NAME"), is((byte) 2));
        assertThat(accessor.getByte(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getBytes method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBytes() throws Exception {
        byte[] obj1 = "ABC".getBytes();
        byte[] obj2 = "CDF".getBytes();
        when(mockResultSet.getBytes(1)).thenReturn(obj1);
        when(mockResultSet.getBytes(2)).thenReturn(obj2);
        assertThat(accessor.getBytes(mockResultSet, "USERID"), is(equalTo(obj1)));
        assertThat(accessor.getBytes(mockResultSet, "NAME"), is(equalTo(obj2)));
        assertThat(accessor.getBytes(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getCharacterStream method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetCharacterStream() throws Exception {
        Reader obj1 = mock(Reader.class);
        Reader obj2 = mock(Reader.class);
        when(mockResultSet.getCharacterStream(1)).thenReturn(obj1);
        when(mockResultSet.getCharacterStream(2)).thenReturn(obj2);
        assertThat(accessor.getCharacterStream(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getCharacterStream(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getCharacterStream(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getClob method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetClob() throws Exception {
        Clob obj1 = mock(Clob.class);
        Clob obj2 = mock(Clob.class);
        when(mockResultSet.getClob(1)).thenReturn(obj1);
        when(mockResultSet.getClob(2)).thenReturn(obj2);
        assertThat(accessor.getClob(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getClob(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getClob(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getDate method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDate() throws Exception {
        Date obj1 = new DateOnly();
        Date obj2 = new DateOnly(1900, 4, 1);
        when(mockResultSet.getDate(1)).thenReturn(obj1);
        when(mockResultSet.getDate(2)).thenReturn(obj2);
        assertThat(accessor.getDate(mockResultSet, "UserId"), is(obj1));
        assertThat(accessor.getDate(mockResultSet, "Name"), is(obj2));
        assertThat(accessor.getDate(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getDouble method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetDouble() throws Exception {
        double obj1 = 4.5;
        double obj2 = 1234;
        when(mockResultSet.getDouble(1)).thenReturn(obj1);
        when(mockResultSet.getDouble(2)).thenReturn(obj2);
        assertThat(accessor.getDouble(mockResultSet, "UserId"), is(obj1));
        assertThat(accessor.getDouble(mockResultSet, "Name"), is(obj2));
        assertThat(accessor.getDouble(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getFloat method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetFloat() throws Exception {
        float obj1 = 24.5f;
        float obj2 = 1.234f;
        when(mockResultSet.getFloat(1)).thenReturn(obj1);
        when(mockResultSet.getFloat(2)).thenReturn(obj2);
        assertThat(accessor.getFloat(mockResultSet, "UserId"), is(obj1));
        assertThat(accessor.getFloat(mockResultSet, "Name"), is(obj2));
        assertThat(accessor.getFloat(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getInteger method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetInteger() throws Exception {
        int obj1 = Integer.MAX_VALUE;
        int obj2 = 1234;
        when(mockResultSet.getInt(1)).thenReturn(obj1);
        when(mockResultSet.getInt(2)).thenReturn(obj2);
        assertThat(accessor.getInteger(mockResultSet, "UserId"), is(obj1));
        assertThat(accessor.getInteger(mockResultSet, "Name"), is(obj2));
        assertThat(accessor.getInteger(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getLong method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetLong() throws Exception {
        long obj1 = Long.MAX_VALUE;
        long obj2 = 1234l;
        when(mockResultSet.getLong(1)).thenReturn(obj1);
        when(mockResultSet.getLong(2)).thenReturn(obj2);
        assertThat(accessor.getLong(mockResultSet, "UserId"), is(obj1));
        assertThat(accessor.getLong(mockResultSet, "Name"), is(obj2));
        assertThat(accessor.getLong(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getNCharacterStream method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetNCharacterStream() throws Exception {
        Reader obj1 = mock(Reader.class);
        Reader obj2 = mock(Reader.class);
        when(mockResultSet.getNCharacterStream(1)).thenReturn(obj1);
        when(mockResultSet.getNCharacterStream(2)).thenReturn(obj2);
        assertThat(accessor.getNCharacterStream(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getNCharacterStream(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getNCharacterStream(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getNClob method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetNClob() throws Exception {
        NClob obj1 = mock(NClob.class);
        NClob obj2 = mock(NClob.class);
        when(mockResultSet.getNClob(1)).thenReturn(obj1);
        when(mockResultSet.getNClob(2)).thenReturn(obj2);
        assertThat(accessor.getNClob(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getNClob(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getNClob(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getNString method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetNString() throws Exception {
        String obj1 = "Abc";
        String obj2 = "Cdf";
        when(mockResultSet.getNString(1)).thenReturn(obj1);
        when(mockResultSet.getNString(2)).thenReturn(obj2);
        assertThat(accessor.getNString(mockResultSet, "userId"), is(obj1));
        assertThat(accessor.getNString(mockResultSet, "NaMe"), is(obj2));
        assertThat(accessor.getNString(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getObject method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetObject() throws Exception {
        Object obj1 = 134567;
        Object obj2 = "JOHN DOE";
        Object obj3 = (short) 24;
        Object obj4 = new DateOnly(1992, 4, 1);
        Object obj5 = "012-3456-7890";
        Object obj6 = "123-456";
        Object obj7 = "Mute";
        Object obj8 = "AAA Street";
        Object obj9 = "johndoe@foo.bar";
        Object obj10 = "MyOffice";

        when(mockResultSet.getObject(1)).thenReturn(obj1);
        when(mockResultSet.getObject(2)).thenReturn(obj2);
        when(mockResultSet.getObject(3)).thenReturn(obj3);
        when(mockResultSet.getObject(4)).thenReturn(obj4);
        when(mockResultSet.getObject(5)).thenReturn(obj5);
        when(mockResultSet.getObject(6)).thenReturn(obj6);
        when(mockResultSet.getObject(7)).thenReturn(obj7);
        when(mockResultSet.getObject(8)).thenReturn(obj8);
        when(mockResultSet.getObject(9)).thenReturn(obj9);
        when(mockResultSet.getObject(10)).thenReturn(obj10);

        assertThat(accessor.getObject(mockResultSet, "USERID"), is(obj1));
        assertThat(accessor.getObject(mockResultSet, "NAME"), is(obj2));
        assertThat(accessor.getObject(mockResultSet, "AGE"), is(obj3));
        assertThat(accessor.getObject(mockResultSet, "BIRTH"), is(obj4));
        assertThat(accessor.getObject(mockResultSet, "PHONE"), is(obj5));
        assertThat(accessor.getObject(mockResultSet, "POSTAL"), is(obj6));
        assertThat(accessor.getObject(mockResultSet, "CITY"), is(obj7));
        assertThat(accessor.getObject(mockResultSet, "STREET"), is(obj8));
        assertThat(accessor.getObject(mockResultSet, "MAIL"), is(obj9));
        assertThat(accessor.getObject(mockResultSet, "OFFICE"), is(obj10));
        assertThat(accessor.getObject(mockResultSet, "FAX"), is(nullValue()));

    }

    /**
     * Test of getRef method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetRef() throws Exception {
        Ref obj1 = mock(Ref.class);
        Ref obj2 = mock(Ref.class);
        when(mockResultSet.getRef(1)).thenReturn(obj1);
        when(mockResultSet.getRef(2)).thenReturn(obj2);
        assertThat(accessor.getRef(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getRef(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getRef(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getRowId method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetRowId() throws Exception {
        RowId obj1 = mock(RowId.class);
        RowId obj2 = mock(RowId.class);
        when(mockResultSet.getRowId(1)).thenReturn(obj1);
        when(mockResultSet.getRowId(2)).thenReturn(obj2);
        assertThat(accessor.getRowId(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getRowId(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getRowId(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getSQLXML method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetSQLXML() throws Exception {
        SQLXML obj1 = mock(SQLXML.class);
        SQLXML obj2 = mock(SQLXML.class);
        when(mockResultSet.getSQLXML(1)).thenReturn(obj1);
        when(mockResultSet.getSQLXML(2)).thenReturn(obj2);
        assertThat(accessor.getSQLXML(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getSQLXML(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getSQLXML(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getShort method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetShort() throws Exception {
        short obj1 = Short.MAX_VALUE;
        short obj2 = (short) 123;
        when(mockResultSet.getShort(1)).thenReturn(obj1);
        when(mockResultSet.getShort(2)).thenReturn(obj2);
        assertThat(accessor.getShort(mockResultSet, "userId"), is(obj1));
        assertThat(accessor.getShort(mockResultSet, "NaMe"), is(obj2));
        assertThat(accessor.getShort(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getString method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetString() throws Exception {
        String obj1 = "Abc";
        String obj2 = "Cdf";
        when(mockResultSet.getString(1)).thenReturn("Abc");
        when(mockResultSet.getString(2)).thenReturn("Cdf");
        assertThat(accessor.getString(mockResultSet, "userId"), is(obj1));
        assertThat(accessor.getString(mockResultSet, "NaMe"), is(obj2));
        assertThat(accessor.getString(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getTime method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetTime() throws Exception {
        Time obj1 = new TimeOnly();
        Time obj2 = new TimeOnly(23, 50, 1);
        when(mockResultSet.getTime(1)).thenReturn(obj1);
        when(mockResultSet.getTime(2)).thenReturn(obj2);
        assertThat(accessor.getTime(mockResultSet, "userId"), is(obj1));
        assertThat(accessor.getTime(mockResultSet, "NaMe"), is(obj2));
        assertThat(accessor.getTime(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getTimestamp method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetTimestamp() throws Exception {
        Timestamp obj1 = new Timestamp(System.currentTimeMillis());
        Timestamp obj2 = new Timestamp(0);
        when(mockResultSet.getTimestamp(1)).thenReturn(obj1);
        when(mockResultSet.getTimestamp(2)).thenReturn(obj2);
        assertThat(accessor.getTimestamp(mockResultSet, "userId"), is(obj1));
        assertThat(accessor.getTimestamp(mockResultSet, "NaMe"), is(obj2));
        assertThat(accessor.getTimestamp(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getURL method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetURL() throws Exception {
        URL obj1 = new URL("http://naiv.info/");
        URL obj2 = new URL("http://foo.bar/");
        when(mockResultSet.getURL(1)).thenReturn(obj1);
        when(mockResultSet.getURL(2)).thenReturn(obj2);
        assertThat(accessor.getURL(mockResultSet, "userId"), is(obj1));
        assertThat(accessor.getURL(mockResultSet, "NaMe"), is(obj2));
        assertThat(accessor.getURL(mockResultSet, "Fax"), is(nullValue()));
    }

    /**
     * Test of getUnicodeStream method, of class ResultSetAccessor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetUnicodeStream() throws Exception {
        InputStream obj1 = mock(InputStream.class);
        InputStream obj2 = mock(InputStream.class);
        when(mockResultSet.getUnicodeStream(1)).thenReturn(obj1);
        when(mockResultSet.getUnicodeStream(2)).thenReturn(obj2);
        assertThat(accessor.getUnicodeStream(mockResultSet, "UserId"), is(sameInstance(obj1)));
        assertThat(accessor.getUnicodeStream(mockResultSet, "Name"), is(sameInstance(obj2)));
        assertThat(accessor.getUnicodeStream(mockResultSet, "Fax"), is(nullValue()));
    }

}
