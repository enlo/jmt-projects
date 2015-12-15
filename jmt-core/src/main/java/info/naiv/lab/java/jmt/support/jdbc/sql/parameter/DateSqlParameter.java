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

import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getDatePart;
import info.naiv.lab.java.jmt.support.jdbc.JdbcType;
import static info.naiv.lab.java.jmt.support.jdbc.JdbcType.DATE;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author enlo
 */
public class DateSqlParameter extends AbstractSqlParameter<Date> {

    private static final long serialVersionUID = 1L;

    private static Date from(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new Date(getDatePart(date).getTime());
    }

    public DateSqlParameter(java.util.Date value) {
        super(from(value));
    }

    public DateSqlParameter(String name, java.util.Date value) {
        super(name, from(value));
    }

    @Override
    public JdbcType getJdbcType() {
        return DATE;
    }

    @Override
    protected void internalSetTo(PreparedStatement ps, int i) throws SQLException {
        ps.setDate(i, value);
    }

}
