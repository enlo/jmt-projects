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
package info.naiv.lab.java.jmt.support.jdbc.sql.parameter;

import info.naiv.lab.java.jmt.SizedBytes;
import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import static info.naiv.lab.java.jmt.support.jdbc.JdbcType.LONGVARBINARY;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author enlo
 */
public class BinarySqlParameter extends AbstractSqlParameter<SizedBytes> {

    private static final long serialVersionUID = 1L;

    public BinarySqlParameter(int size, byte[] data) {
        super(new SizedBytes(size, data));
    }

    public BinarySqlParameter(byte[] data) {
        super(new SizedBytes(data));
    }

    public BinarySqlParameter(String name, int size, byte[] data) {
        super(name, new SizedBytes(size, data));
    }

    public BinarySqlParameter(String name, byte[] data) {
        super(name, new SizedBytes(data));
    }
    
    @Override
    public JdbcType getJdbcType() {
        return LONGVARBINARY;
    }

    @Override
    public void internalSetTo(PreparedStatement ps, int i) throws SQLException {
        try (InputStream stream = value.newInputStream()) {
            ps.setBinaryStream(i, stream);
        }
        catch (IOException e) {
            ps.setBytes(i, value.toByteArray());
        }
    }

}
