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

import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import static info.naiv.lab.java.jmt.support.jdbc.JdbcType.TIMESTAMP;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author enlo
 */
public class TimestampSqlParameter extends AbstractSqlParameter<Timestamp> {

    private static final long serialVersionUID = 1L;

    private static Timestamp from(Date date, int nanosec) {
        if (date == null) {
            return null;
        }
        else if (date instanceof Timestamp) {
            return (Timestamp) date;
        }
        else {
            Timestamp ts = new Timestamp(date.getTime());
            ts.setNanos(nanosec);
            return ts;
        }
    }

    public TimestampSqlParameter(Date value) {
        this(value, 0);
    }

    public TimestampSqlParameter(String name, Date value) {
        this(name, value, 0);
    }

    public TimestampSqlParameter(Date value, int nanos) {
        super(from(value, nanos));
    }

    public TimestampSqlParameter(String name, Date value, int nanos) {
        super(name, from(value, nanos));
    }

    @Override
    public JdbcType getJdbcType() {
        return TIMESTAMP;
    }

    @Override
    public void internalSetTo(PreparedStatement ps, int i) throws SQLException {
        ps.setTimestamp(i, value);
    }

}
