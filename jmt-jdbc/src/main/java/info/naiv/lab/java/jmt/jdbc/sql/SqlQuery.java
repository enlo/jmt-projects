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
package info.naiv.lab.java.jmt.jdbc.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author enlo
 */
@Value
public class SqlQuery implements Query {
    
    final PreparedStatementSetter setter;
    final String sql;
    
    public SqlQuery(String sql, Object[] args) {
        this(sql, new ArgumentPreparedStatementSetter(args));
    }
    
    public SqlQuery(String sql, List<?> args) {
        this(sql, new ArgumentPreparedStatementSetter(args.toArray()));
    }
    
    public SqlQuery(PreparedStatementCreatorFactory factory, List<?> args) {
        this.setter = factory.newPreparedStatementSetter(args);
        this.sql = ((SqlProvider) this.setter).getSql();
    }
    
    public SqlQuery(PreparedStatementCreatorFactory factory, Object[] args) {
        this.setter = factory.newPreparedStatementSetter(args);
        this.sql = ((SqlProvider) this.setter).getSql();
    }
    
    public SqlQuery(String sql, PreparedStatementSetter setter) {
        this.sql = sql;
        this.setter = setter;
    }
    
    public SqlQuery(String sql) {
        this(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
            }
        });
    }

    /**
     *
     * @param <T>
     * @param jdbcOperations
     * @param batchValues
     * @return
     */
    @Override
    public <T> int[] batchUpadate(JdbcOperations jdbcOperations, List<Object[]> batchValues) {
        return batchUpadate(jdbcOperations, batchValues, new int[0]);
    }
    
    @Override
    public <T> int[] batchUpadate(JdbcOperations jdbcOperations, final List<Object[]> batchValues, final int[] columnTypes) {
        return batchUpadate(jdbcOperations, new BatchPreparedStatementSetter() {
            
            @Override
            public int getBatchSize() {
                return batchValues.size();
            }
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] values = batchValues.get(i);
                for (int colIndex = 0; colIndex < values.length; colIndex++) {
                    Object value = values[colIndex];
                    int colType;
                    if (columnTypes == null || columnTypes.length <= colIndex) {
                        colType = SqlTypeValue.TYPE_UNKNOWN;
                    }
                    else {
                        colType = columnTypes[colIndex];
                    }
                    StatementCreatorUtils.setParameterValue(ps, colIndex + 1, colType, value);
                }
            }
        });
    }
    
    @Override
    public <T> int[] batchUpadate(JdbcOperations jdbcOperations, final BatchPreparedStatementSetter batchStatementSetter) {
        return jdbcOperations.batchUpdate(sql, new BatchPreparedStatementSetter() {
            
            @Override
            public int getBatchSize() {
                return batchStatementSetter.getBatchSize() + 1;
            }
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                if (i == 0) {
                    setter.setValues(ps);
                }
                else {
                    batchStatementSetter.setValues(ps, i - 1);
                }
            }
        });
    }
    
    @Override
    public void execute(JdbcOperations jdbcOperations) {
        jdbcOperations.execute(sql);
    }
    
    @Override
    public <T> void execute(JdbcOperations jdbcOperations, final PreparedStatementCallback<T> action) {
        jdbcOperations.execute(sql, new PreparedStatementCallback<T>() {
            @Override
            public T doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                setter.setValues(ps);
                return action.doInPreparedStatement(ps);
            }
        });
    }
    
    @Override
    public <T> List<T> query(JdbcOperations jdbcOperations, RowMapper<T> rowMapper) {
        return query(jdbcOperations, new RowMapperResultSetExtractor<>(rowMapper));
    }
    
    @Override
    public <T> List<T> query(JdbcOperations jdbcOperations, ResultSetExtractor<List<T>> resultSetExtractor) {
        return jdbcOperations.query(sql, setter, resultSetExtractor);
    }
    
    @Override
    public <T> List<T> queryForList(JdbcOperations jdbcOperations, Class<T> elementType) {
        return jdbcOperations.query(sql, setter, new SingleColumnRowMapper<>(elementType));
    }
    
    @Override
    public List<Map<String, Object>> queryForList(JdbcOperations jdbcOperations) {
        return jdbcOperations.query(sql, setter, new ColumnMapRowMapper());
    }
    
    @Override
    public Map<String, Object> queryForMap(JdbcOperations jdbcOperations) {
        return queryForObject(jdbcOperations, new ColumnMapRowMapper());
    }
    
    @Override
    public <T> T queryForObject(JdbcOperations jdbcOperations, RowMapper<T> rowMapper) {
        List<T> results = query(jdbcOperations, new RowMapperResultSetExtractor<>(rowMapper, 1));
        return DataAccessUtils.requiredSingleResult(results);
    }
    
    @Override
    public <T> T queryForObject(JdbcOperations jdbcOperations, Class<T> requiredType) {
        return queryForObject(jdbcOperations, new SingleColumnRowMapper<>(requiredType));
    }
    
    @Override
    public SqlRowSet queryForRowSet(JdbcOperations jdbcOperations) {
        return jdbcOperations.query(sql, setter, new SqlRowSetResultSetExtractor());
    }
    
    @Override
    public SqlQuery rebind(PreparedStatementSetter newSetter) {
        return new SqlQuery(sql, newSetter);
    }
    
    @Override
    public SqlQuery rebind(List<?> args) {
        return new SqlQuery(sql, args);
    }
}
