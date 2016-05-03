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

import info.naiv.lab.java.jmt.jdbc.mapping.RowMapperFactory;
import info.naiv.lab.java.jmt.jdbc.mapping.RowMapperFactoryResultSetExtractor;
import info.naiv.lab.java.jmt.jdbc.sql.dialect.Dialect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.InterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author enlo
 */
@ToString
@EqualsAndHashCode
@Slf4j
public class SqlQuery implements Query {

    final SqlQueryContext context;

    int fetchSize = -1;
    int maxRowSize = -1;

    @Getter
    PagingOption pagingOption;

    final InternalPreparedStatementSetter setter;
    final String sql;

    public SqlQuery(String sql, SqlQueryContext context) {
        this.sql = sql;
        this.context = context;
        this.setter = new InternalPreparedStatementSetter(context.getParameters());
    }

    private SqlQuery(String sql, SqlQueryContext context, PreparedStatementSetter newSetter) {
        this.sql = sql;
        this.context = context;
        this.setter = new InternalPreparedStatementSetter(newSetter);
    }

    private SqlQuery(String sql, SqlQueryContext context, Object[] args) {
        this.sql = sql;
        this.context = context;
        this.setter = new InternalPreparedStatementSetter(args);
    }

    /**
     *
     * @param jdbcOperations
     * @param batchValues
     * @return
     */
    @Override
    public int[] batchUpadate(JdbcOperations jdbcOperations, List<Object[]> batchValues) {
        return batchUpadate(jdbcOperations, batchValues, new int[0]);
    }

    @Override
    public int[] batchUpadate(JdbcOperations jdbcOperations, final List<Object[]> batchValues, final int[] columnTypes) {
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
    public int[] batchUpadate(JdbcOperations jdbcOperations, final BatchPreparedStatementSetter batchStatementSetter) {
        String q = getSql();

        BatchPreparedStatementSetter bpss;
        if (batchStatementSetter instanceof InterruptibleBatchPreparedStatementSetter) {
            final InterruptibleBatchPreparedStatementSetter ibpss = (InterruptibleBatchPreparedStatementSetter) batchStatementSetter;
            bpss = new InterruptibleBatchPreparedStatementSetter() {

                @Override
                public int getBatchSize() {
                    return ibpss.getBatchSize();
                }

                @Override
                public boolean isBatchExhausted(int i) {
                    return ibpss.isBatchExhausted(i);
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
            };
        }
        else {
            bpss = new BatchPreparedStatementSetter() {

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
            };
        }

        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.batchUpdate(q, bpss);
    }

    boolean debugLogging;

    @Override
    public void enableDebugLogging(boolean flag) {
        debugLogging = flag;
    }

    @Override
    public void execute(JdbcOperations jdbcOperations) {
        String q = modifyForQuery(getSql());
        jdbcOperations.execute(q, new PreparedStatementCallback<Void>() {

            @Override
            public Void doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                setter.setValues(ps);
                ps.execute();
                return null;
            }
        });
    }

    @Override
    public <T> T execute(JdbcOperations jdbcOperations, final PreparedStatementCallback<T> action) {
        String q = modifyForQuery(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.execute(q, new PreparedStatementCallback<T>() {
            @Override
            public T doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                setter.setValues(ps);
                return action.doInPreparedStatement(ps);
            }
        });
    }

    @Override
    public int getFetchSize() {
        return fetchSize;
    }

    @Override
    public int getMaxRowSize() {
        return maxRowSize;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public <T> List<T> query(JdbcOperations jdbcOperations, RowMapper<T> rowMapper) {
        return query(jdbcOperations, new RowMapperResultSetExtractor<>(rowMapper));
    }

    @Override
    public <T> T query(JdbcOperations jdbcOperations, ResultSetExtractor<T> resultSetExtractor) {
        String q = modifyForQuery(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.query(q, setter, resultSetExtractor);
    }

    @Override
    public <T> List<T> query(JdbcOperations jdbcOperations, RowMapperFactory<T> rowMapperFactory) {
        String q = modifyForQuery(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.query(q, setter, new RowMapperFactoryResultSetExtractor<>(rowMapperFactory));
    }

    @Override
    public <T> T queryForBean(JdbcOperations jdbcOperations, Class<T> mappedClass) {
        return queryForObject(jdbcOperations, BeanPropertyRowMapper.newInstance(mappedClass));
    }

    @Override
    public <T> List<T> queryForBeanList(JdbcOperations jdbcOperations, Class<T> mappedClass) {
        return query(jdbcOperations, BeanPropertyRowMapper.newInstance(mappedClass));
    }

    @Override
    public Map<String, Object> queryForMap(JdbcOperations jdbcOperations) {
        return queryForObject(jdbcOperations, new ColumnMapRowMapper());
    }

    @Override
    public List<Map<String, Object>> queryForMapList(JdbcOperations jdbcOperations) {
        String q = modifyForQuery(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.query(q, setter, new ColumnMapRowMapper());
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
    public <T> List<T> queryForObjectList(JdbcOperations jdbcOperations, Class<T> elementType) {
        String q = modifyForQuery(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.query(q, setter, new SingleColumnRowMapper<>(elementType));
    }

    @Override
    public SqlRowSet queryForRowSet(JdbcOperations jdbcOperations) {
        String q = modifyForQuery(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.query(q, setter, new SqlRowSetResultSetExtractor());
    }

    @Override
    public SqlQuery rebind(PreparedStatementSetter newSetter) {
        return new SqlQuery(getSql(), context, newSetter);
    }

    @Override
    public SqlQuery rebind(List<?> args) {
        return new SqlQuery(getSql(), context, args.toArray());
    }

    @Override
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    @Override
    public void setMaxRowSize(int maxRowSize) {
        this.maxRowSize = maxRowSize;
    }

    @Override
    public void setPage(int offset, int size) {
        this.pagingOption = new PagingOption(offset, size);
    }

    @Override
    public int update(JdbcOperations jdbcOperations) {
        String q = modifyForUpdate(getSql());
        if (this.debugLogging) {
            logger.debug("batchUpdate Sql={}", q);
        }
        return jdbcOperations.update(q, setter);
    }

    @Override
    public int update(JdbcOperations jdbcOperations, KeyHolder keyHolder) {
        final boolean db = debugLogging;
        return jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String q = modifyForUpdate(getSql());
                if (db) {
                    logger.debug("batchUpdate Sql={}", q);
                }
                PreparedStatement ps = con.prepareStatement(q);
                setter.setValues(ps);
                return ps;
            }
        }, keyHolder);
    }

    protected String modifyForQuery(String query) {
        Dialect dialect = context.getDialect();
        if (dialect != null) {
            if (pagingOption != null) {
                query = pagingOption.modify(query, dialect);
            }
        }
        return query;
    }

    protected String modifyForUpdate(String query) {
        return query;
    }

    private class InternalPreparedStatementSetter implements PreparedStatementSetter {

        PreparedStatementSetter internalSetter;

        InternalPreparedStatementSetter(PreparedStatementSetter setter) {
            this.internalSetter = setter;
        }

        InternalPreparedStatementSetter(List<?> args) {
            this(args.toArray());
        }

        InternalPreparedStatementSetter(Object[] args) {
            this(new ArgumentPreparedStatementSetter(args));
        }

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {
            if (0 <= maxRowSize) {
                ps.setMaxRows(maxRowSize);
            }
            if (0 <= fetchSize) {
                ps.setFetchSize(fetchSize);
            }
            internalSetter.setValues(ps);
        }

    }
}
