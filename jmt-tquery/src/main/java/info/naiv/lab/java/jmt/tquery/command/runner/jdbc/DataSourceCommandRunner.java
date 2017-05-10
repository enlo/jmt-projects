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
package info.naiv.lab.java.jmt.tquery.command.runner.jdbc;

import info.naiv.lab.java.jmt.jdbc.ResultSetAccessHelper;
import info.naiv.lab.java.jmt.jdbc.ResultSetMapper;
import info.naiv.lab.java.jmt.tquery.command.Command;
import info.naiv.lab.java.jmt.tquery.command.CommandParameter;
import info.naiv.lab.java.jmt.tquery.command.CommandParameters;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author enlo
 */
public class DataSourceCommandRunner {

    DataSource dataSource;

    public <TResult> List<TResult> select(Command cmd, ResultSetMapper<TResult> mapper) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(cmd.getQuery());
            CommandParameters params = cmd.getParameters();
            for (int i = 1; i <= params.size(); i++) {
                CommandParameter cp = params.get(i);
                Object val = cp.getValue();
                Object typeHint = cp.getTypeHint();
                if (typeHint instanceof Number) {
                    int type = ((Number) typeHint).intValue();
                    if (val != null) {
                        stmt.setObject(i, val, type);
                    }
                    else {
                        stmt.setNull(i, type);
                    }
                }
                else {
                    if (val == null) {
                        stmt.setNull(i, Types.NULL);
                    }
                    else {
                        stmt.setObject(i, params.get(i).getValue());
                    }
                }
            }
            List<TResult> result = new LinkedList<>();
            if (stmt.execute()) {
                try (ResultSet rs = stmt.getResultSet()) {
                    ResultSetAccessHelper rsah = new ResultSetAccessHelper(rs);
                    while (rs.next()) {
                        TResult obj = mapper.apply(rsah);
                        result.add(obj);
                    }
                }
            }
            return result;
        }
    }
}
